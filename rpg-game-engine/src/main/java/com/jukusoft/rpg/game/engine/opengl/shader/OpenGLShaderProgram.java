package com.jukusoft.rpg.game.engine.opengl.shader;

import com.jukusoft.rpg.game.engine.exception.OpenGLVersionException;
import com.jukusoft.rpg.game.engine.exception.ShaderException;
import com.jukusoft.rpg.game.engine.utils.OpenGLUtils;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Justin on 24.08.2016.
 */
public class OpenGLShaderProgram /*extends Asset*/ {

    /**
    * id of gpu programm
    */
    protected volatile int programID = 0;

    /**
    * id of vertex shader on gpu
    */
    protected int vertexShaderID = 0;

    /**
    * id of fragment shader on gpu
    */
    protected int fragmentShaderID = 0;

    /**
    * id of geometry shader on gpu
    */
    protected int geometryShaderID = 0;

    /**
    * id of compute shader on gpu
    */
    protected int computeShaderID = 0;

    /**
    * id of tessellation evaluation shader on gpu
    */
    protected int tessellationEvaluationShaderID = 0;

    /**
     * id of tessellation control shader on gpu
     */
    protected int tessellationControlShaderID = 0;

    /**
    * default constructor
     *
     * @link https://github.com/lwjglgamedev/lwjglbook/blob/master/chapter04/src/main/java/org/lwjglb/engine/graph/ShaderProgram.java
    */
    public OpenGLShaderProgram() throws ShaderException {
        //https://www.opengl.org/wiki/Shader#Resource_limitations

        this.create();
    }

    /**
    * create opengl shader program on gpu
     *
     * @throws ShaderException if shader program couldnt be created
    */
    protected void create () throws ShaderException {
        //create new empty gpu shader program
        this.programID = glCreateProgram();

        //check for error
        if (this.programID == 0) {
            //throw shader exception, because shader couldnt be created
            throw new ShaderException("Could not create Shader");
        }
    }

    /**
    * get id of shader program on gpu
     *
     * @return id of shader program on gpu
    */
    public int getProgramID () {
        return this.programID;
    }

    /**
    * set vertex shader code
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
    */
    public void setVertexShader (String shaderCode) throws ShaderException {
        //check, if shader already exists
        if (this.vertexShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("vertex shader was already set on shader program " + this.programID + ".");
        }

        //create vertex shader
        this.vertexShaderID = this.addShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * set fragment shader code
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
     */
    public void setFragmentShader (String shaderCode) throws ShaderException {
        //check, if shader already exists
        if (this.fragmentShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("fragment shader was already set on shader program " + this.programID + ".");
        }

        //create fragment shader
        this.fragmentShaderID = this.addShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * set geometry shader code
     *
     * geometry shaders are added on OpenGL 3.2
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
     * @throws OpenGLVersionException if geometry shaders arent supported
     */
    public void setGeometryShader (String shaderCode) throws ShaderException, OpenGLVersionException {
        //check, if shader already exists
        if (this.geometryShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("geometry shader was already set on shader program " + this.programID + ".");
        }

        //check, if OpenGL version supports this feature
        if (OpenGLUtils.getMajorVersion() >= 3 && OpenGLUtils.getMinorVersion() >= 2) {
            //geometry shaders are supported

            //create geometry shader
            this.geometryShaderID = this.addShader(shaderCode, GL32.GL_GEOMETRY_SHADER);
        } else {
            throw new OpenGLVersionException("Geometry shaders require OpenGL 3.2+, you are currently using OpenGL " + OpenGLUtils.getMajorVersion() + "." + OpenGLUtils.getMinorVersion() + ".");
        }
    }

    /**
     * set compute shader code
     *
     * compute shaders are added on OpenGL 4.3
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
     * @throws OpenGLVersionException if compute shaders arent supported
     */
    public void setComputeShader (String shaderCode) throws ShaderException, OpenGLVersionException {
        //check, if shader already exists
        if (this.computeShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("compute shader was already set on shader program " + this.programID + ".");
        }

        //check, if OpenGL version supports this feature
        if (OpenGLUtils.getMajorVersion() >= 4 && OpenGLUtils.getMinorVersion() >= 3) {
            //geometry shaders are supported

            //create geometry shader
            this.computeShaderID = this.addShader(shaderCode, GL43.GL_COMPUTE_SHADER);
        } else {
            throw new OpenGLVersionException("Compute shaders require OpenGL 4.3+, you are currently using OpenGL " + OpenGLUtils.getMajorVersion() + "." + OpenGLUtils.getMinorVersion() + ".");
        }
    }

    /**
     * set tessellation control shader code
     *
     * tessellation control shaders are added on OpenGL 4.0
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
     * @throws OpenGLVersionException if tessellation control shaders arent supported
     */
    public void setTessellationControlShader (String shaderCode) throws ShaderException, OpenGLVersionException {
        //check, if shader already exists
        if (this.tessellationControlShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("tessellation control shader was already set on shader program " + this.programID + ".");
        }

        //check, if OpenGL version supports this feature
        if (OpenGLUtils.getMajorVersion() >= 4 && OpenGLUtils.getMinorVersion() >= 0) {
            //geometry shaders are supported

            //create shader
            this.tessellationControlShaderID = this.addShader(shaderCode, GL40.GL_TESS_CONTROL_SHADER);
        } else {
            throw new OpenGLVersionException("tessellation control shaders require OpenGL 4.0+, you are currently using OpenGL " + OpenGLUtils.getMajorVersion() + "." + OpenGLUtils.getMinorVersion() + ".");
        }
    }

    /**
     * set tessellation evaluation shader code
     *
     * tessellation evaluation shaders are added on OpenGL 4.0
     *
     * @param shaderCode code of shader
     *
     * @throws ShaderException
     * @throws OpenGLVersionException if tessellation evaluation shaders arent supported
     */
    public void setTessellationEvaluationShader (String shaderCode) throws ShaderException, OpenGLVersionException {
        //check, if shader already exists
        if (this.tessellationEvaluationShaderID != 0) {
            //throw exception
            throw new UnsupportedOperationException("tessellation evaluation shader was already set on shader program " + this.programID + ".");
        }

        //check, if OpenGL version supports this feature
        if (OpenGLUtils.getMajorVersion() >= 4 && OpenGLUtils.getMinorVersion() >= 0) {
            //geometry shaders are supported

            //create shader
            this.tessellationEvaluationShaderID = this.addShader(shaderCode, GL40.GL_TESS_EVALUATION_SHADER);
        } else {
            throw new OpenGLVersionException("tessellation evaluation shaders require OpenGL 4.0+, you are currently using OpenGL " + OpenGLUtils.getMajorVersion() + "." + OpenGLUtils.getMinorVersion() + ".");
        }
    }

    /**
    * add shader to shader programm
     *
     * @param shaderCode code of shader
     * @param shaderType type of shader
     *
     * @throws ShaderException
     *
     * @return id of shader
    */
    protected int addShader (String shaderCode, int shaderType) throws ShaderException {
        //create new shader
        int shaderId = glCreateShader(shaderType);

        //check for errors
        if (shaderId == 0) {
            //throw exception
            throw new ShaderException("Error creating shader. Code: " + shaderId);
        }

        //set shader code
        glShaderSource(shaderId, shaderCode);

        //compile shader
        glCompileShader(shaderId);

        //check for errors while compiling shader
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new ShaderException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        //attack shader to shader program / adds shader to shader program
        glAttachShader(this.programID, shaderId);

        //return id of shader
        return shaderId;
    }

    /**
    * link shader program to gpu
    */
    public void link() throws ShaderException {
        //link program, combines all shaders to one shader program
        glLinkProgram(this.programID);

        //check for errors
        if (glGetProgrami(this.programID, GL_LINK_STATUS) == 0) {
            throw new ShaderException("Error linking Shader code: " + glGetShaderInfoLog(this.programID, 1024));
        }

        //validate shader program
        glValidateProgram(this.programID);

        //check for errors
        if (glGetProgrami(this.programID, GL_VALIDATE_STATUS) == 0) {
            /**
            * This means, that validation may fail in some cases vene if the shader is correct, due to the
            * fact that current state is not complete enough to run the shader (some data may have not
            * been uploaded yet). So, instead of failing, we just print an error message to the standard
            * error output.
            */

            //dont throw exception, only log message
            Logger.getRootLogger().warn("Warning validating Shader code: " + glGetShaderInfoLog(this.programID, 1024));
        }

    }

    /**
    * bind shader program on gpu
    */
    public void bind () {
        //bind and use opengl shader program
        glUseProgram(this.programID);
    }

    /**
    * unbind shader program on gpu
    */
    public void unbind () {
        //unbind opengl shader program from gpu
        glUseProgram(0);
    }

    public void compile () {
        //
    }

    /*@Override*/
    public void cleanUp() {
        //unbind shader program first
        unbind();

        //check, if shader program exists
        if (programID != 0) {
            if (this.vertexShaderID != 0) {
                //detach vertex shader
                glDetachShader(this.programID, this.vertexShaderID);
            }

            if (this.fragmentShaderID != 0) {
                glDetachShader(this.programID, this.fragmentShaderID);
            }

            if (this.computeShaderID != 0) {
                glDetachShader(this.programID, this.computeShaderID);
            }

            if (this.tessellationControlShaderID != 0) {
                glDetachShader(this.programID, this.tessellationControlShaderID);
            }

            if (this.tessellationEvaluationShaderID != 0) {
                glDetachShader(this.programID, this.tessellationEvaluationShaderID);
            }

            //remove shader program
            glDeleteProgram(this.programID);
        }
    }

}
