package com.jukusoft.rpg.graphic.math;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.camera.DefaultCamera;
import com.jukusoft.rpg.graphic.camera.ReadonlyCamera;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 07.12.2016.
 */
public class TransformationUtilsTest {

    @Test
    public void testOrthoProjMatrix () {
        //create new matrix
        Matrix4f matrix = new Matrix4f();

        //calculate 2D orthogonal matrix
        matrix = TransformationUtils.getOrthoProjectionMatrix(0, 1280, 720, 0, matrix);

        //expected result matrix, calculated with JOML
        Matrix4f resultMatrix = new Matrix4f(
                0.0015625f, 0f, 0f, -1f,
                0f, -0.0027777778f, 0f, 1f,
                0f, 0, -1f, 0f,
                0f, 0f, 0f, 1f
        );

        assertEquals("matrizes arent equals.", true, matrix.equals(resultMatrix));
    }

    @Test
    public void testTranslate () {
        Vector3f position = new Vector3f(3, 6, 9);

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(position);

        //with JOML calculated result
        Matrix4f resultMatrix = new Matrix4f(
                1, 0, 0, 3,
                0, 1, 0, 6,
                0, 0, 1, 9,
                0, 0, 0, 1
        );

        assertEquals("matrizes arent equals, result matrix:\n" + modelMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, modelMatrix.equals(resultMatrix));
    }

    @Test
    public void testModelMatrix () {
        Vector3f position = new Vector3f(3, 6, 9);
        Vector3f rotation = new Vector3f(0, 0, 0);
        float scale = 1;

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(position);

        modelMatrix.rotateX((float)Math.toRadians(-rotation.getX()));
        modelMatrix.rotateY((float)Math.toRadians(-rotation.getY()));
        modelMatrix.rotateZ((float)Math.toRadians(-rotation.getZ()));

        //with JOML calculated result
        Matrix4f resultMatrix = new Matrix4f(
                1, 0, 0, 3,
                0, 1, 0, 6,
                0, 0, 1, 9,
                0, 0, 0, 1
        );

        assertEquals("matrizes arent equals, result matrix:\n" + modelMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, modelMatrix.equals(resultMatrix));

        //scale matrix
        modelMatrix.scale(1);

        assertEquals("matrizes arent equals, result matrix:\n" + modelMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, modelMatrix.equals(resultMatrix));

        Matrix4f orthoMatrix = new Matrix4f();
        orthoMatrix = TransformationUtils.getOrthoProjectionMatrix(0, 1280, 720, 0, orthoMatrix);

        //expected result matrix, calculated with JOML
        Matrix4f resultOrthoMatrix = new Matrix4f(
                0.0015625f, 0f, 0f, -1f,
                0f, -0.0027777778f, 0f, 1f,
                0f, 0, -1f, 0f,
                0f, 0f, 0f, 1f
        );

        assertEquals("ortho matrizes arent equals.", true, orthoMatrix.equals(resultOrthoMatrix));

        //create new matrix
        Matrix4f dest = new Matrix4f();
        dest.set(orthoMatrix);

        assertEquals("ortho and destination matrizes arent equals.", true, dest.equals(orthoMatrix));

        dest.mul(modelMatrix);

        Matrix4f resultMulMatrix = new Matrix4f(
                0.0015625f, 0, 0, -0.9953125f,
                0, -0.0027777778f, 0, 0.98333335f,
                0, 0, -1, -9,
                0, 0, 0, 1
        );

        assertEquals("multiplized matrizes arent equals,\nresult matrix:\n" + dest.toString(true) + "\n\nexpected matrix:\n" + resultMulMatrix.toString(true), true, dest.equals(resultMulMatrix));
    }

    @Test
    public void testOrtoProjModelMatrix () {
        //create new matrix
        Matrix4f ortho = new Matrix4f();
        ortho = TransformationUtils.getOrthoProjectionMatrix(0, 1280, 720, 0, ortho);

        //expected result matrix, calculated with JOML
        Matrix4f resultOrthoMatrix = new Matrix4f(
                0.0015625f, 0f, 0f, -1f,
                0f, -0.0027777778f, 0f, 1f,
                0f, 0, -1f, 0f,
                0f, 0f, 0f, 1f
        );

        assertEquals("ortho matrizes arent equals,\nresult matrix:\n" + ortho.toString(true) + "\n\nexpected matrix:\n" + resultOrthoMatrix.toString(true), true, ortho.equals(resultOrthoMatrix));

        final Vector3f rotation = new Vector3f(0, 0, 0);
        final Vector3f position = new Vector3f(10, 20, 0);
        final float scale = 1;

        //create new matrix
        Matrix4f matrix = new Matrix4f();

        matrix = TransformationUtils.getOrtoProjModelMatrix(rotation, position, scale, ortho);
                //.getOrtoProjModelMatrix(rotation, position, scale, ortho, matrix);

        //result matrix, calculated with JOML
        Matrix4f resultMatrix = new Matrix4f(
                0.0015625f, 0, 0, -0.984375f,
                0, -0.0027777778f, 0, 0.9444444f,
                0, 0, -1f, 0,
                0, 0, 0, 1f
        );

        //compare matrizes
        assertEquals("projection model matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testCameraViewMatrix () {
        //create new camera
        DefaultCamera camera = new DefaultCamera();
        camera.setPosition(40, 50, 60);
        camera.setRotation(3, 5, 7);

        //create new matrix
        Matrix4f matrix = new Matrix4f();

        //calculate view matrix
        matrix = TransformationUtils.getCameraViewMatrix(camera, matrix);

        //expected result matrix
        Matrix4f resultMatrix = new Matrix4f(
                0.9961947f, 0, 0.08715574f, -45.077133f,
                0.004561379f, 0.9986295f, -0.052136805f, -46.985725f,
                -0.08703629f, 0.05233596f, 0.9948294f, -58.82511f,
                0, 0, 0, 1
        );

        //compare matrizes
        assertEquals("view matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

}
