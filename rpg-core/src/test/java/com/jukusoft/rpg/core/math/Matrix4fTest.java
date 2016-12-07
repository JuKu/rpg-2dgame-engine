package com.jukusoft.rpg.core.math;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 23.11.2016.
 */
public class Matrix4fTest {

    @Test
    public void testMatrix4f () {
        Matrix4f matrix = new Matrix4f();
    }

    @Test
    public void testIndex () {
        //OpenGL requires specific indexes, described here: https://javadoc.lwjgl.org/org/lwjgl/opengl/GL11.html#glLoadMatrixf-java.nio.FloatBuffer-
        assertEquals(0, Matrix4f.index(0, 0));
        assertEquals(4, Matrix4f.index(1, 0));
        assertEquals(1, Matrix4f.index(0, 1));
        assertEquals(15, Matrix4f.index(3, 3));
    }

    @Test
    public void testGetterAndSetter () {
        //create new matrix
        Matrix4f matrix = new Matrix4f();

        //set some values
        matrix.set(0, 0, 20);
        matrix.set(1, 0, 18);
        matrix.set(0, 3, 2.5f);

        //test getters
        assertEquals(20, matrix.get(0, 0), 0);
        assertEquals(18, matrix.get(1, 0), 0);
        assertEquals(2.5f, matrix.get(0, 3), 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testArrayOutOfBoundsException () {
        //create new matrix
        Matrix4f matrix = new Matrix4f();

        //set some values
        matrix.set(4, 4, 20);
    }

    @Test
    public void testConstructor () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 0, 0, 0,
                0, 2, 0, 0,
                0, 0, 3, 0,
                0, 0, 0, 4);

        //test getter
        assertEquals(1f, matrix.get(0, 0), 0);
        assertEquals(2f, matrix.get(1, 1), 0);
        assertEquals(3f, matrix.get(2, 2), 0);
        assertEquals(4f, matrix.get(3, 3), 0);

        assertEquals(0f, matrix.get(1, 0), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFloatBufferConstructor () {
        //create new buffer for matrix
        ByteBuffer buffer = ByteBuffer.allocateDirect(15 * 4);
        FloatBuffer floatBuffer = buffer.asFloatBuffer();

        Matrix4f matrix = new Matrix4f(buffer);
    }

    @Test
    public void testEquals () {
        Matrix4f matrix1 = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        Matrix4f matrix2 = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        assertEquals("matrix arent equals.", true, matrix1.equals(matrix2));

        Matrix4f matrix3 = new Matrix4f(
                1, 2, 5, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        assertEquals("matrix are equals.", false, matrix1.equals(matrix3));
    }

    @Test
    public void testMul () {
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        Matrix4f matrix1 = new Matrix4f(
                17, 18, 19, 20,
                21, 22, 23, 24,
                25, 26, 27, 28,
                29, 30, 31, 32
        );

        matrix.mul(matrix1);

        //expected, with JOML calculated result matrix
        Matrix4f resultMatrix = new Matrix4f(
                250, 260, 270, 280,
                618, 644, 670, 696,
                986, 1028, 1070, 1112,
                1354, 1412, 1470, 1528
        );

        assertEquals("multiplized matrizes arent equals,\n\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testTranslate () throws Exception {
        //create new example matrix
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        //print matrix
        matrix.print(true);

        //create new vector to translate
        Vector3f vector = new Vector3f(3, 6, 9);

        //translate matrix with vector
        matrix.translate(vector);

        System.out.println("translated matrix: ");

        //print matrix
        matrix.print(true);

        //create matrix and set result values, calculated with JOML library
        Matrix4f resultMatrix = new Matrix4f(
                1, 2, 3, 46,
                5, 6, 7, 122,
                9, 10, 11, 198,
                13, 14, 15, 274
        );

        //check, if result matrix is equals to calculated matrix
        assertEquals("calculated matrix isnt equals, result:\n" + matrix.toString(true) + "\n, expected result:\n" + resultMatrix.toString(true), matrix, resultMatrix);
    }

    @Test
    public void testScale () {
        //create new example matrix
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        //scale matrix
        matrix.scale(2);

        //with JOML calculated scaled matrix
        Matrix4f resultMatrix = new Matrix4f(
                2, 4, 6, 4,
                10, 12, 14, 8,
                18, 20, 22, 12,
                26, 28, 30, 16
        );

        assertEquals("matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testIdentityMatrix () {
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 6
        );

        Matrix4f matrix1 = matrix.copy();

        //set identity matrix (Einheitsmatrix)
        matrix1.identity();

        //test identity matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != j) {
                    //value has to be 0
                    assertEquals("(" + i + ", " + j + ") has to be 0.", 0, matrix1.get(i, j), 0);
                } else {
                    //value has to be 1
                    assertEquals("(" + i + ", " + j + ") has to be 1.", 1, matrix1.get(i, j), 0);
                }
            }
        }
    }

    @Test
    public void testRotateX () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        //rotate matrix on x axis
        matrix.rotateX(50);

        //result matrix, calculated with JOML
        Matrix4f resultMatrix = new Matrix4f(
                1f, 1.1428075f, 3.4196477f, 4f,
                5f, 3.953172f, 8.329011f, 8f,
                9f, 6.7635365f, 13.238375f, 12f,
                13f, 9.573902f, 18.147738f, 16f
        );

        assertEquals("matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testRotateY () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        //rotate matrix on x axis
        matrix.rotateY(50);

        //result matrix, calculated with JOML
        Matrix4f resultMatrix = new Matrix4f(
                1.7520905f, 2f, 2.632523f, 4f,
                6.661454f, 6f, 5.442888f, 8f,
                11.570818f, 10f, 8.253252f, 12f,
                16.48018f, 14f, 11.063618f, 16f
        );

        assertEquals("matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testRotateZ () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        );

        //rotate matrix on x axis
        matrix.rotateZ(50);

        //result matrix, calculated with JOML
        Matrix4f resultMatrix = new Matrix4f(
                0.4402163f, 2.1923068f, 3f, 4f,
                3.250581f, 7.1016703f, 7f, 8f,
                6.0609455f, 12.011034f, 11f, 12f,
                8.871309f, 16.920397f, 15f, 16f
        );

        assertEquals("matrizes arent equals,\nresult matrix:\n" + matrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, matrix.equals(resultMatrix));
    }

    @Test
    public void testOrtho2DMatrix () throws Exception {
        Matrix4f matrix = new Matrix4f(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 6
                );

        final int windowWidth = 1280;
        final int windowHeight = 720;

        //generate projection matrix / view matrix
        Matrix4f projMatrix = new Matrix4f();
        //projMatrix.setIdentityMatrix();
        projMatrix.setOrtho2D(0, windowWidth, windowHeight, 0);

        projMatrix.print(true);

        //create matrix with results from JOML
        /*Matrix4f resultMatrix = new Matrix4f(
                0.0033333334f, 0f, 0f, 0f,
                0f, -0.004166667f, 0f, 0f,
                0f, 0, -1f, 0f,
                -1f, 1f, 0f, 1f
        );*/

        Matrix4f resultMatrix = new Matrix4f(
                0.0015625f, 0f, 0f, -1f,
                0f, -0.0027777778f, 0f, 1f,
                0f, 0, -1f, 0f,
                0f, 0f, 0f, 1f
        );

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("value of (" + i + ", " + j + ") isnt the same, result value: " + projMatrix.get(i, j) + ", expected value: " + resultMatrix.get(i, j) + "\n\nresult matrix:\n" + projMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true) + "", resultMatrix.get(i, j), projMatrix.get(i, j), 0);
            }
        }

        //assertEquals("matrix arent equals, result matrix:\n" + projMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true) + "", true, resultMatrix.equals(projMatrix));
    }

    @Test
    public void testCopy () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 0, 0, 0,
                0, 2, 0, 0,
                0, 0, 3, 0,
                0, 0, 0, 4);

        //copy matrix
        Matrix4f copy = matrix.copy();

        //check, if copied matrix is equals to original matrix
        assertEquals("matrizes arent equals.", true, matrix.equals(copy));

        //change an value in original matrix
        matrix.set(3, 3, 10);

        //now matrix shouldnt be equals
        assertEquals(false, matrix.equals(copy));
    }

    /*@Test
    public void testTransform () {
        //create new matrix
        Matrix4f matrix = new Matrix4f(
                1, 0, 0, 0,
                0, 2, 0, 0,
                0, 0, 3, 0,
                0, 0, 0, 4);

        //transform matrix
        matrix.transform();

        //test getter
        assertEquals(1f, matrix.get(0, 3), 0);
        assertEquals(2f, matrix.get(1, 3), 0);
        assertEquals(3f, matrix.get(2, 1), 0);
        assertEquals(4f, matrix.get(3, 0), 0);
    }*/

}
