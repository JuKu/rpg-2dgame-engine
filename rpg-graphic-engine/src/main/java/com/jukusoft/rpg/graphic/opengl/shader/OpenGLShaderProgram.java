package com.jukusoft.rpg.graphic.opengl.shader;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.math.Vector4f;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.exception.OpenGLVersionException;
import com.jukusoft.rpg.graphic.exception.ShaderException;
import com.jukusoft.rpg.graphic.lighting.DirectionalLight;
import com.jukusoft.rpg.graphic.lighting.PointLight;
import com.jukusoft.rpg.graphic.lighting.SpotLight;
import com.jukusoft.rpg.graphic.utils.OpenGLUtils;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    * map with all uniforms
    */
    protected final Map<String, Integer> uniformsMap = new HashMap<>();

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
    * create new uniform
     *
     * @param name name of uniform
    */
    public void createUniform (final String name) throws OpenGLShaderException {
        int uniformID = glGetUniformLocation(this.programID, name);

        if (uniformID < 0) {
            throw new OpenGLShaderException("Could not find uniform:" + name);
        }

        //put uniform to map
        this.uniformsMap.put(name, uniformID);
    }

    /**
    * set matrix value to uniform
    */
    public void setUniform (final String name, Matrix4f matrix) {
        //first get float buffer of matrix
        FloatBuffer buffer = matrix.getFloatBuffer();

        if (!this.uniformsMap.containsKey(name)) {
            throw new OpenGLShaderException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }

        //get uniform id
        int uniformLocation = uniformsMap.get(name);

        //check, if uniform exists
        /*if (!this.uniformsMap.containsKey(name) || uniformLocation <= 0) {
            throw new RuntimeException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists, uniformLocation: " + uniformLocation);
        }*/

        //check, if float buffer is an direct buffer (Off heap), because OpenGL method only supports direct buffers
        if (!buffer.isDirect()) {
            throw new IllegalStateException("Only direct buffers are allowed, but Matrix4f implementation doesnt use direct float buffer.");
        }

        //set matrix value as direct buffer
        glUniformMatrix4fv(uniformLocation, false, buffer);
    }

    public void setUniform (final String name, final int value) {
        //get uniform id
        int uniformID = uniformsMap.get(name);

        //check, if uniform exists
        if (!this.uniformsMap.containsKey(name) || uniformID <= 0) {
            throw new RuntimeException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }

        //set integer value to uniform
        glUniform1i(uniformID, value);
    }

    public void setUniform (final String name, final float value) {
        //get uniform id
        int uniformID = uniformsMap.get(name);

        //check, if uniform exists
        if (!this.uniformsMap.containsKey(name) || uniformID <= 0) {
            throw new RuntimeException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }

        //set float value to uniform
        glUniform1f(uniformID, value);
    }

    public void setUniform (final String name, Vector3f vector) {
        if (!this.uniformsMap.containsKey(name)) {
            throw new OpenGLShaderException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }

        //get uniform id
        int uniformID = uniformsMap.get(name);

        //check, if uniform exists
        /*if (!this.uniformsMap.containsKey(name) || uniformID <= 0) {
            throw new RuntimeException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }*/

        //set vector value to uniform
        glUniform3f(uniformID, vector.getX(), vector.getY(), vector.getZ());
    }

    public void setUniform (final String name, Vector4f vector) {
        if (!this.uniformsMap.containsKey(name)) {
            throw new OpenGLShaderException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }

        //get uniform id
        int uniformID = uniformsMap.get(name);

        //check, if uniform exists
        /*if (!this.uniformsMap.containsKey(name) || uniformID <= 0) {
            throw new RuntimeException("OpenGL Error: Cannot set value for uniform '" + name + "', because uniform doesnt exists.");
        }*/

        //set vector value to uniform
        glUniform4f(uniformID, vector.getX(), vector.getY(), vector.getZ(), vector.getA());
    }

    public void createPointLightUniform (String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createPointLightUniforms (String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            this.createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightUniform (String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void createSpotLightUniforms (String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            this.createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createDirectionalLightUniform (String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void setUniform(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;

        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights[i], i);
        }
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights[i], i);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".color", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
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
