package com.jukusoft.rpg.graphic.math;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.camera.ReadonlyCamera;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.renderer.Renderable;

/**
 * Created by Justin on 07.12.2016.
 */
public class TransformationUtils {

    public static Matrix4f getOrthoProjectionMatrix (final float left, final float right, final float bottom, final float top, Matrix4f dest) {
        dest.setIdentityMatrix();
        dest.setOrtho2D(left, right, bottom, top);

        return dest;
    }

    public static Matrix4f getOrthoProjectionMatrix (final float left, final float right, final float bottom, final float top) {
        Matrix4f dest = new Matrix4f();
        return getOrthoProjectionMatrix(left, right, bottom, top, dest);
    }

    /*public static Matrix4f getOrtoProjModelMatrix (DrawableObject obj, Matrix4f orthoProjMatrix, Matrix4f dest) {
        return getOrtoProjModelMatrix(obj.getPosition(), obj.getRotation(), obj.getScale(), orthoProjMatrix, dest);
    }

    public static Matrix4f getOrtoProjModelMatrix (final Vector3f position, final Vector3f rotation, final float scale, Matrix4f orthoProjMatrix, Matrix4f dest) {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(position);

        modelMatrix.rotateX((float)Math.toRadians(-rotation.getX()));
        modelMatrix.rotateY((float)Math.toRadians(-rotation.getY()));
        modelMatrix.rotateZ((float)Math.toRadians(-rotation.getZ()));
        modelMatrix.scale(scale);

        dest.set(orthoProjMatrix);
        dest.mul(modelMatrix);

        return dest;
    }*/

    public static Matrix4f getOrtoProjModelMatrix(DrawableObject gameItem, Matrix4f orthoMatrix, Matrix4f cachedModelMatrix, Matrix4f dest) {
        return getOrtoProjModelMatrix(gameItem.getRotation(), gameItem.getPosition(), gameItem.getScale(), orthoMatrix, cachedModelMatrix, dest);
    }

    public static Matrix4f getOrtoProjModelMatrix(Renderable gameItem, Matrix4f orthoMatrix, Matrix4f cachedModelMatrix, Matrix4f dest) {
        return getOrtoProjModelMatrix(gameItem.getRotation(), gameItem.getPosition(), gameItem.getScale(), orthoMatrix, cachedModelMatrix, dest);
    }

    public static Matrix4f getOrtoProjModelMatrix(DrawableObject gameItem, Matrix4f orthoMatrix) {
        return getOrtoProjModelMatrix(gameItem.getRotation(), gameItem.getPosition(), gameItem.getScale(), orthoMatrix);
    }

    /**
     * get orthogonal projection model matrix
     *
     * optimized method, so no new matrix instances has to be created
     */
    public static Matrix4f getOrtoProjModelMatrix(Vector3f rotation, Vector3f position, float scale, Matrix4f orthoMatrix, Matrix4f cachedModelMatrix, Matrix4f dest) {
        Matrix4f modelMatrix = cachedModelMatrix;
        modelMatrix.identity();
        modelMatrix.translate(position);
        modelMatrix.rotateX((float) Math.toRadians(-rotation.getX()));
        modelMatrix.rotateY((float) Math.toRadians(-rotation.getY()));
        modelMatrix.rotateZ((float) Math.toRadians(-rotation.getZ()));
        modelMatrix.scale(scale);

        Matrix4f orthoMatrixCurr = dest;
        dest.set(orthoMatrix);
                //new Matrix4f(orthoMatrix);

        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }

    /**
    * get orthogonal projection model matrix
     *
     * from LWJGL examples
    */
    public static Matrix4f getOrtoProjModelMatrix(Vector3f rotation, Vector3f position, float scale, Matrix4f orthoMatrix) {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(position);
        modelMatrix.rotateX((float)Math.toRadians(-rotation.getX()));
        modelMatrix.rotateY((float)Math.toRadians(-rotation.getY()));
        modelMatrix.rotateZ((float)Math.toRadians(-rotation.getZ()));
        modelMatrix.scale(scale);
        Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }

    /**
    * generate view matrix of camera
     *
     * @param camera instance of camera
     * @param destViewMatrix destination matrix (so we dont have to create an new matrix)
    */
    public static Matrix4f getCameraViewMatrix (final ReadonlyCamera camera, final Matrix4f destViewMatrix) {
        //get position and rotation of camera
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        Matrix4f viewMatrix = destViewMatrix;

        viewMatrix.identity();

        //rotate matrix
        viewMatrix.rotate((float)Math.toRadians(rotation.getX()), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.getY()), new Vector3f(0, 1, 0));

        //translate matrix
        viewMatrix.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

        return viewMatrix;
    }

    public final Matrix4f getProjectionMatrix (float fov, float width, float height, float zNear, float zFar, final Matrix4f projectionMatrix) {
        //calculate aspectRatio
        float aspectRatio = width / height;

        //set identity matrix
        projectionMatrix.identity();

        //generate perspective matrix
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar, projectionMatrix);

        return projectionMatrix;
    }

}
