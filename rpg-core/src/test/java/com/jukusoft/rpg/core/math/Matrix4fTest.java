package com.jukusoft.rpg.core.math;

import org.junit.Test;

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
