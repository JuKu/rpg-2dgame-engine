package com.jukusoft.rpg.game.engine.resource;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.game.engine.utils.GamePlatform;
import com.jukusoft.rpg.graphic.opengl.font.FontAttr;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;

import java.awt.*;
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

    /*public static void getPNGImage (String path) {
        //
    }*/

    /**
     * preload image
     *
     * @param path path to image
     */
    public void preparePNGImage (String path) {
        //preload texture in thread pool
        GamePlatform.runAsync(() -> {
            //TODO: add code here
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

                return texture;
            }
        } else {
            GameLogger.info("ResourceManager", "load new font: " + fontAttr.toString());

            //load font
            texture = new FontTexture(font, charset, color);

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
        });
    }

}
