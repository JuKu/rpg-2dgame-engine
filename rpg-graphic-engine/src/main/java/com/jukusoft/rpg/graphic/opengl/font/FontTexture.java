package com.jukusoft.rpg.graphic.opengl.font;

import com.jukusoft.rpg.core.asset.Asset;
import com.jukusoft.rpg.core.asset.image.Image2D;
import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;
import com.jukusoft.rpg.core.path.GamePaths;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;
import com.jukusoft.rpg.graphic.utils.FontUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * FontTexture, like LWJGL examples
 *
 * Created by Justin on 29.11.2016.
 */
public class FontTexture extends Asset {

    /**
    * font
    */
    protected final Font font;

    protected final String charsetName;

    //width and height of generated image texture
    protected int width = 0;
    protected int height = 0;

    /**
    * map with CharacterInfo for every character
    */
    protected Map<Character,CharacterInfo> characterMap = new HashMap<>();

    //generated image texture
    protected OpenGL2DTexture texture = null;

    protected static final String IMAGE_FORMAT = "png";

    protected Color color = Color.WHITE;

    /**
    * default constructor
     *
     * @param font font
     * @param charsetName name of charset
    */
    public FontTexture (Font font, String charsetName, Color color) {
        this.font = font;
        this.charsetName = charsetName;
        this.color = color;

        //check cache first and remove file, if neccessary
        if (new File(getCachePath()).exists()) {
            //delete file
            new File(getCachePath()).delete();
        }

        //create image texture from charset
        try {
            this.createTexture();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //load font texture from cache
        /*try {
            this.loadFontTextureFromCache();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedAssetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AssetNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }*/
    }

    /**
     * default constructor
     *
     * @param font font
     * @param charsetName name of charset
     */
    public FontTexture (Font font, String charsetName) {
        this.font = font;
        this.charsetName = charsetName;
        this.color = Color.WHITE;

        //check cache first and remove file, if neccessary

        //create image texture from charset
        try {
            this.createTexture();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //load font texture from cache
        /*try {
            this.loadFontTextureFromCache();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedAssetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AssetNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }*/
    }

    /**
    * generate image texture from charset
    */
    protected void createTexture () throws Exception {
        //reset width and height
        this.width = 0;
        this.height = 0;

        //from LWJGL examples: get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        //get all available characters for this charset
        String chars = FontUtils.getAllAvailableCharacters(this.charsetName);

        //iterate through all available characters
        for (char c : chars.toCharArray()) {
            //create new CharacterInfo, startX is the current width of the image, get width of character from font metrics
            CharacterInfo characterInfo = new CharacterInfo(width, fontMetrics.charWidth(c));

            //add character to map
            this.characterMap.put(c, characterInfo);

            //update width of character, add width of character
            this.width += characterInfo.getWidth();

            //update height
            height = Math.max(height, fontMetrics.getHeight());
        }

        //some graphics cards only supports textures with an size of a power of two
        if (this.width % 2 != 0) {
            this.width += 1;
        }

        //dispose Graphics2D
        g2D.dispose();

        //create new image with all available characters
        img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

        //draw all characters to image texture
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(this.color);
        g2D.drawString(chars, 0, fontMetrics.getAscent());
        g2D.dispose();

        // Dump image to a byte buffer
        InputStream is = null;
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, IMAGE_FORMAT, out);
            out.flush();
            is = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.texture = OpenGL2DTexture.createAndUpload(is);

        try {
            //save image to file cache
            ImageIO.write(img, IMAGE_FORMAT, new File(this.getCachePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUploaded () {
        return this.texture != null && this.texture.isUploaded();
    }

    @Deprecated
    public void loadFontTextureFromCache () throws IOException, UnsupportedAssetException, AssetNotFoundException {
        String path = this.getCachePath();

        //check, if font exists in cache first
        if (!new File(path).exists()) {
            throw new IllegalStateException("font " + charsetName + " doesnt exists in cache, path: " + path + ".");
        }

        //load image texture
        Image2D image = new Image2D(path);

        //get width and height of image texture
        this.width = image.getWidth();
        this.height = image.getHeight();

        //create and upload new image texture to gpu
        this.texture = OpenGL2DTexture.createAndUpload(image);

        //release memory of image
        image.callCleanUp();
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    public CharacterInfo getCharacterInfo(char c) {
        return this.characterMap.get(c);
    }

    public OpenGL2DTexture getTexture () {
        return this.texture;
    }

    public String getCachePath () {
        String path = GamePaths.getFontCacheDir() + "/" + this.font.getFontName() + "-" + this.font.getStyle() + "-" + this.font.getSize() + "-" + charsetName + "_" + this.color.getRed() + "-" + this.color.getGreen() + "-" + this.color.getBlue() + "-" + this.color.getAlpha() + ".png";

        return path;
    }

}
