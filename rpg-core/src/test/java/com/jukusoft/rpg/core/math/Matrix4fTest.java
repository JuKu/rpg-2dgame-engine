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
