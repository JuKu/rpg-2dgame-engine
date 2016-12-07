package com.jukusoft.rpg.graphic.math;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;

/**
 * Created by Justin on 07.12.2016.
 */
public class TransformationUtils {

    public static Matrix4f getOrthoProjMatrix (final float left, final float right, final float bottom, final float top, Matrix4f dest) {
        dest.setIdentityMatrix();
        dest.setOrtho2D(left, right, bottom, top);

        return dest;
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

    public static Matrix4f getOrtoProjModelMatrix(DrawableObject gameItem, Matrix4f orthoMatrix) {
        return getOrtoProjModelMatrix(gameItem.getRotation(), gameItem.getPosition(), gameItem.getScale(), orthoMatrix);
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

}
