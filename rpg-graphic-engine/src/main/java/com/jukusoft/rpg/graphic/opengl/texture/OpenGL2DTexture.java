package com.jukusoft.rpg.graphic.opengl.texture;

import com.jukusoft.rpg.core.asset.Asset;
import com.jukusoft.rpg.core.asset.image.Image2D;
import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.utils.BufferUtils;
import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by Justin on 29.11.2016.
 */
public class OpenGL2DTexture extends Asset {

    /**
    * id of texture on gpu
    */
    protected int textureID = 0;

    /**
     * flag, if texture is bind
     */
    protected boolean isBind = false;

    protected int width = 0;
    protected int height = 0;

    /**
    * flag, if texture was already uploaded to gpu
    */
    protected boolean isUploaded = false;

    public OpenGL2DTexture() {
        this.create();

        //http://stackoverflow.com/questions/30488155/opengl-fastest-way-to-draw-2d-image

        //https://en.wikibooks.org/wiki/OpenGL_Programming/Modern_OpenGL_Tutorial_2D
    }

    /**
     * create new vertex buffer object on gpu
     */
    protected void create () {
        //create new texture
        this.textureID = glGenTextures();
    }

    /**
     * bind texture on gpu
     */
    public void bind () {
        //bind texture
        glBindTexture(GL_TEXTURE_2D, this.textureID);

        //set bind flag to true
        this.isBind = true;
    }

    public void unbind () {
        //unbind the texture
        glBindTexture(GL_TEXTURE_2D, 0);

        //set bind flag to false
        this.isBind = false;
    }

    public void upload (Image2D image) {
        if (!isBind) {
            throw new IllegalStateException("OpenGL2DTexture has to be bind before texture can be uploaded to gpu.");
        }

        //tell OpenGL how to unpack the RBGA bytes. Each component (pixel) is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //upload texture to gpu
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(),
                image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());

        //generate MipMap for scaling, see https://wiki.delphigl.com/index.php/MipMaps, https://www.opengl.org/sdk/docs/man/html/glGenerateMipmap.xhtml and https://www.opengl.org/wiki/Common_Mistakes#Automatic_mipmap_generation
        glGenerateMipmap(GL_TEXTURE_2D);

        this.width = image.getWidth();
        this.height = image.getHeight();

        GameLogger.debug("OpenGL2DTexture", "uploaded texture with ID " + this.getTextureID() + ", width: " + getWidth() + " and height: " + getHeight() + ".");

        //set uploaded flag to true
        this.isUploaded = true;
    }

    public void upload (InputStream is) throws Exception {
        if (!isBind) {
            throw new IllegalStateException("OpenGL2DTexture has to be bind before texture can be uploaded to gpu.");
        }

        // Load Texture file
        PNGDecoder decoder = new PNGDecoder(is);

        this.width = decoder.getWidth();
        this.height = decoder.getHeight();

        //load texture into an byte buffer
        ByteBuffer buf = BufferUtils.createByteBuffer(
                4 * decoder.getWidth() * decoder.getHeight());

        //decode image
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

        //flip byte buffer to reset reader index
        buf.flip();

        //tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //upload texture to gpu
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        //generate MipMap
        glGenerateMipmap(GL_TEXTURE_2D);

        GameLogger.debug("OpenGL2DTexture", "uploaded texture with ID " + this.getTextureID() + ", width: " + getWidth() + " and height: " + getHeight() + ".");

        this.isUploaded = true;
    }

    public void uploadIfAbsent (Image2D image) {
        if (!this.isUploaded) {
            this.upload(image);
        }
    }

    public void uploadIfAbsent (String path) throws IOException, UnsupportedAssetException, AssetNotFoundException {
        if (!this.isUploaded) {
            Image2D image = new Image2D(path);

            //bind texture on gpu first
            this.bind();

            //upload texture data
            this.upload(image);

            //unbind texture on gpu
            this.unbind();

            //cleanUp image from RAM, because it isnt needed anymore
            image.callCleanUp();
        }
    }

    /**
    * get id of texture on gpu
     *
     * @return id of texture on gpu
    */
    public int getTextureID () {
        return this.textureID;
    }

    /**
    * check, if texture is bind to gpu
    */
    public boolean isBind () {
        return this.isBind;
    }

    /**
    * check, if texture was uploaded to gpu
     *
     * @return true, if texture was uploaded to gpu
    */
    public boolean isUploaded () {
        return this.isUploaded;
    }

    public void delete () {
        //https://www.khronos.org/opengles/sdk/docs/man/xhtml/glDeleteTextures.xml
        glDeleteTextures(this.textureID);
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    /**
    * create texture on gpu and upload texture data
     *
     * @param image texture image to upload on gpu
    */
    public static OpenGL2DTexture createAndUpload (Image2D image) {
        //create new OpenGL texture
        OpenGL2DTexture texture = new OpenGL2DTexture();

        //bind texture on gpu first
        texture.bind();

        //upload texture data
        texture.upload(image);

        //unbind texture on gpu
        texture.unbind();

        return texture;
    }

    /**
     * create texture on gpu and upload texture data
     *
     * @param is input stream to texture to upload on gpu
     */
    public static OpenGL2DTexture createAndUpload (InputStream is) throws Exception {
        //create new OpenGL texture
        OpenGL2DTexture texture = new OpenGL2DTexture();

        //bind texture on gpu first
        texture.bind();

        //upload texture data
        texture.upload(is);

        //unbind texture on gpu
        texture.unbind();

        return texture;
    }

}
