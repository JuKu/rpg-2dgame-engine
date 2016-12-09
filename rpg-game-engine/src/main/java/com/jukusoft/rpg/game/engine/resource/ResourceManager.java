package com.jukusoft.rpg.game.engine.resource;

import com.jukusoft.rpg.core.asset.image.Image2D;
import com.jukusoft.rpg.core.exception.AssetException;
import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.path.GamePaths;
import com.jukusoft.rpg.game.engine.utils.GamePlatform;
import com.jukusoft.rpg.graphic.opengl.font.FontAttr;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 29.11.2016.
 */
public class ResourceManager {

    /**
    * font cache
    */
    protected Map<FontAttr,FontTexture> fontCache = new ConcurrentHashMap<>();

    /**
    * image cache
    */
    protected Map<String,Image2D> imageCache = new ConcurrentHashMap<>();

    /**
    * texture cache
    */
    protected Map<String,OpenGL2DTexture> textureMap = new ConcurrentHashMap<>();

    protected static ResourceManager instance = null;

    protected ResourceManager () {
        //
    }

    public static ResourceManager getInstance () {
        if (instance == null) {
            instance = new ResourceManager();
        }

        return instance;
    }

    public Image2D getImage (String path) {
        path = GamePaths.getImagePath(path);

        //get image from cache
        Image2D image = this.imageCache.get(path);

        //check, if image is in cache
        if (image == null) {
            GameLogger.info("ResourceManager", "load new image from data directory: " + path);

            //load image from File I/O
            try {
                image = new Image2D(path);
            } catch (UnsupportedAssetException e) {
                e.printStackTrace();
                GameLogger.warn("ResourceManager", "UnsupportedAssetException: " + e.getLocalizedMessage());

                throw new AssetException("UnsupportedAssetException: " + e.getLocalizedMessage(), e);
            } catch (IOException e) {
                e.printStackTrace();
                GameLogger.warn("ResourceManager", "IOException: " + e.getLocalizedMessage());

                throw new AssetException("IOException: " + e.getLocalizedMessage(), e);
            } catch (AssetNotFoundException e) {
                e.printStackTrace();
                GameLogger.warn("ResourceManager", "AssetNotFoundException: " + e.getLocalizedMessage());

                throw new AssetException("AssetNotFoundException: " + e.getLocalizedMessage(), e);
            }

            //add image to cache
            this.imageCache.put(path, image);
        } else {
            GameLogger.debug("ResourceManager", "load image from cache: " + image);
        }

        //increment texture refCount
        image.incrementReference();

        //set last access timestamp
        image.setLastAccess();

        return image;
    }

    /**
     * preload font
     */
    public void preloadImage (final String path) {
        //preload font in thread pool
        GamePlatform.runAsync(() -> {
            Image2D image = null;

            try {
                //load image
                image = getImage(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (image == null) {
                //set last access timestamp
                image.setLastAccess();
            }
        });
    }

    public boolean isImageInCache (final String path) {
        return this.imageCache.get(path) != null;
    }

    public OpenGL2DTexture getTexture (String path) {
        path = GamePaths.getImagePath(path);

        //get texture from cache
        OpenGL2DTexture texture = this.textureMap.get(path);

        //check, if texture is in cache
        if (texture == null) {
            GameLogger.info("ResourceManager", "load new OpenGL2DTexture from data directory: " + path);

            Image2D image = null;

            //check, if image is in cache
            if (isImageInCache(path)) {
                image = getImage(path);
            } else {
                try {
                    image = new Image2D(path);
                } catch (UnsupportedAssetException e) {
                    e.printStackTrace();
                    throw new AssetException("UnsupportedAssetException: " + e.getLocalizedMessage(), e);
                } catch (AssetNotFoundException e) {
                    e.printStackTrace();
                    throw new AssetException("AssetNotFoundException: " + e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new AssetException("IOException: " + e.getLocalizedMessage(), e);
                }
            }

            //create new texture
            texture = OpenGL2DTexture.createAndUpload(image);

            //add texture to cache
            this.textureMap.put(path, texture);
        } else {
            GameLogger.debug("ResourceManager", "load OpenGL2DTexture from cache: " + path);
        }

        //increment texture refCount
        texture.incrementReference();

        //set last access timestamp
        texture.setLastAccess();

        return texture;
    }

    /**
     * preload texture
     */
    public void preloadOpenGL2DTexture (final String path) {
        //preload font in thread pool
        GamePlatform.runAsync(() -> {
            OpenGL2DTexture texture = null;

            try {
                //load texture
                texture = getTexture(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (texture == null) {
                //set last access timestamp
                texture.setLastAccess();
            }
        });
    }

    public FontTexture getFontTexture (Font font, String charset, Color color) {
        FontAttr fontAttr = new FontAttr(font, charset, color);
        FontTexture texture = this.fontCache.get(fontAttr);

        //check, if font is in cache
        if (texture != null) {
            //check, if texture was uploaded to gpu
            if (!texture.isUploaded()) {
                GameLogger.warn("ResourceManager", "font was in cache, but not uploaded to gpu: " + fontAttr.toString());
                throw new UnsupportedOperationException("Currently no texture uploading to gpu is supported on resource manager");
            } else {
                GameLogger.debug("ResourceManager", "load font from cache: " + fontAttr.toString());

                //increment texture refCount
                texture.incrementReference();

                //set last access timestamp
                texture.setLastAccess();

                return texture;
            }
        } else {
            GameLogger.info("ResourceManager", "load new font: " + fontAttr.toString());

            //load font
            texture = new FontTexture(font, charset, color);

            //set last access timestamp
            texture.setLastAccess();

            //add texture to cache
            this.fontCache.put(fontAttr, texture);

            return texture;
        }
    }

    /**
     * preload font
     */
    public void preloadFont (Font font, String charset, Color color) {
        //preload font in thread pool
        GamePlatform.runAsync(() -> {
            //load font
            FontTexture fontTexture = getFontTexture(font, charset, color);

            //set last access timestamp
            fontTexture.setLastAccess();
        });
    }

}
