package com.jukusoft.rpg.graphic.opengl.buffer;

import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import static com.jukusoft.rpg.graphic.opengl.texture.TextureFilterMode.NEAREST;
import static jdk.nashorn.internal.runtime.regexp.joni.constants.StackType.REPEAT;
import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

/**
 * Created by Justin on 13.12.2016.
 */
public class FrameBufferObject {

    /**
    *  links:
     *
     * http://wiki.lwjgl.org/wiki/Using_Frame_Buffer_Objects_(FBO).html
     * https://www.opengl.org/wiki/Framebuffer_Object_Examples
     * http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-14-render-to-texture/
     * https://github.com/mattdesl/lwjgl-basics/blob/master/src/mdesl/graphics/Texture.java
     * https://github.com/mattdesl/lwjgl-basics/wiki/FrameBufferObjects
    */

    /**
    * frame buffer object
    */
    protected int fboID = 0;

    /**
     * flag, if VBO is bind
     */
    protected boolean isBind = false;

    /**
    * texture
    */
    protected OpenGL2DTexture texture = null;

    protected boolean removeTextureOnCleanUp = false;

    /*public static boolean isAvailable () {
        return GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }*/

    public FrameBufferObject (OpenGL2DTexture texture, boolean removeTextureOnCleanUp) {
        this.texture = texture;
        this.removeTextureOnCleanUp = removeTextureOnCleanUp;

        this.create(texture);
    }

    public FrameBufferObject (OpenGL2DTexture texture) {
        this(texture, false);
    }

    public FrameBufferObject (int width, int height, int filter, int wrap) {
        this(new OpenGL2DTexture(width, height, filter, wrap));
    }

    public FrameBufferObject (int width, int height) {
        this(width, height, GL_NEAREST, GL_REPEAT);
    }

    protected void create (OpenGL2DTexture texture) {
        //bind texture
        this.texture.bind();

        //create new frmaebuffer
        this.fboID = glGenFramebuffersEXT();

        //bind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER, this.fboID);

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                texture.getTarget(), texture.getTextureID(), 0);

        //check framebuffer status
        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER);

        //check, if framebuffer was created successfully
        if (result != GL_FRAMEBUFFER_COMPLETE) {
            //unbind framebuffer
            glBindFramebufferEXT(GL_FRAMEBUFFER, 0);

            //delete framebuffer
            glDeleteFramebuffers(this.fboID);

            throw new RuntimeException("Exception while creating new framebuffer, result: " + result);
        }

        //unbind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    public OpenGL2DTexture getTexture () {
        return this.texture;
    }

    /**
    * bind frame buffer object
    */
    public void bind () {
        //set viewport to texture size
        glViewport(0, 0, getWidth(), getHeight());

        //bind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.fboID);

        //set bind flag to true
        this.isBind = true;
    }

    /**
    * unbind frame buffer object
     *
     * @param originalWindowWidth width of GLFW window
     * @param originWindowHeight height of GLFW window
    */
    public void unbind (int originalWindowWidth, int originWindowHeight) {
        //reset viewport
        glViewport(0, 0, originalWindowWidth, originWindowHeight);

        //unbind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        //set bind flag to false
        this.isBind = false;
    }

    /**
     * unbind frame buffer object
     */
    public void unbind () {
        //unbind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        //set bind flag to false
        this.isBind = false;
    }

    /**
     * check, if framebuffer is bind to gpu
     */
    public boolean isBind () {
        return this.isBind;
    }

    public int getWidth () {
        return this.texture.getWidth();
    }

    public int getHeight () {
        return this.texture.getHeight();
    }

    /**
    * delete framebuffer
    */
    public void delete () {
        //unbind framebuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        //delete framebuffer
        glDeleteFramebuffersEXT(this.fboID);

        if (this.removeTextureOnCleanUp) {
            this.texture.delete();
        }

        //reset framebuffer id
        this.fboID = 0;
    }

}
