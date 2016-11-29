package com.jukusoft.rpg.graphic.opengl.texture;

import com.jukusoft.rpg.core.asset.image.Image;
import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Created by Justin on 29.11.2016.
 */
public class OpenGL2DTexture {

    /**
    * id of texture on gpu
    */
    protected int textureID = 0;

    /**
     * flag, if texture is bind
     */
    protected boolean isBind = false;

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

    public void upload (Image image) {
        //tell OpenGL how to unpack the RBGA bytes. Each component (pixel) is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //upload texture to gpu
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(),
                image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());

        //generate MipMap for scaling, see https://wiki.delphigl.com/index.php/MipMaps, https://www.opengl.org/sdk/docs/man/html/glGenerateMipmap.xhtml and https://www.opengl.org/wiki/Common_Mistakes#Automatic_mipmap_generation
        glGenerateMipmap(GL_TEXTURE_2D);

        //set uploaded flag to true
        this.isUploaded = true;
    }

    public void uploadIfAbsent (Image image) {
        if (!this.isUploaded) {
            this.upload(image);
        }
    }

    public void uploadIfAbsent (String path) throws IOException, UnsupportedAssetException, AssetNotFoundException {
        if (!this.isUploaded) {
            Image image = new Image(path);

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

    /**
    * create texture on gpu and upload texture data
     *
     * @param image texture image to upload on gpu
    */
    public static OpenGL2DTexture createAndUpload (Image image) {
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

}
