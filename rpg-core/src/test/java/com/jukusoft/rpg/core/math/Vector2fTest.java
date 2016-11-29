package com.jukusoft.rpg.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 21.11.2016.
 */
public class Vector2fTest {

    @Test
    public void testVector2f () {
        //create new vector
        Vector2f vector2f = new Vector2f(1, 2);

        //create new vector
        Vector2f vector2f1 = new Vector2f();
    }

    @Test
    public void testGetterAndSetter () {
        //create new vector
        Vector2f vector2f = new Vector2f(3, 4);

        //test getter for integer
        int x = (int) vector2f.getX();
        assertEquals(x, 3);

        //test getter for float
        assertEquals(vector2f.getX(), 3, 0);
        assertEquals(vector2f.getY(), 4, 0);

        //test setter for float
        vector2f.setX(3.4f);
        vector2f.setY(10.3f);

        //test getter for float
        assertEquals(vector2f.getX(), 3.4f, 0);
        assertEquals(vector2f.getY(), 10.3f, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadonly () {
        //create new vector
        Vector2f vector2f = new Vector2f(3, 4);

        //mark vector as readonly
        vector2f.setReadonly();

        vector2f.setX(2);
        vector2f.setY(2);
        vector2f.set(3, 4);
    }

    @Test
    public void testWriteable () {
        //create new vector
        Vector2f vector2f = new Vector2f(3, 4);

        //mark vector as readonly
        vector2f.setReadonly();

        //mark vector as writeable
        vector2f.setWriteable();

        vector2f.setX(2);
        vector2f.setY(2);
        vector2f.set(3, 4);
    }

    @Test
    public void testAbs () {
        //create new vector
        Vector2f vector2f = new Vector2f(3, 4);

        //calculate abs
        float abs = vector2f.abs();

        assertEquals(5, abs, 0);
    }

    @Test
    public void testAddition () {
        //create new vectors
        Vector2f v1 = new Vector2f(3, 4);
        Vector2f v2 = new Vector2f(5, 1);

        //add vectors
        v1.add(v2);

        assertEquals(8, v1.getX(), 0);
        assertEquals(5, v1.getY(), 0);
    }

    @Test
    public void testSubstraction () {
        //create new vectors
        Vector2f v1 = new Vector2f(3, 4);
        Vector2f v2 = new Vector2f(5, 1);

        //add vectors
        v1.sub(v2);

        assertEquals(-2, v1.getX(), 0);
        assertEquals(3, v1.getY(), 0);
    }

    @Test
    public void testEquals () {
        //create new vector
        Vector2f v1 = new Vector2f(3, 4);
        Vector2f v2 = new Vector2f(2, 1);
        Vector2f v3 = new Vector2f(3, 4);

        assertEquals(false, v1.equals(v2));
        assertEquals(true, v1.equals(v3));
    }

    @Test
    public void testScale () {
        //create new vector
        Vector2f v1 = new Vector2f(3, 4);

        //scale vector
        v1.scale(2);

        assertEquals(6, v1.getX(), 0);
        assertEquals(8, v1.getY(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanUp () {
        //create new vector
        Vector2f v1 = new Vector2f(3, 4);

        v1.release();

        float x = v1.getX();
    }

    @Test
    public void testNorm () {
        //create new vector
        Vector2f v1 = new Vector2f(3, 4);

        //normalize vector
        v1.norm();

        assertEquals(0.6f, v1.getX(), 0);
        assertEquals(0.8f, v1.getY(), 0);
    }

    @Test
    public void testCopy () {
        //create new vector
        Vector2f vector2f = new Vector2f(3, 4);

        //copy vector
        Vector2f copiedVector = vector2f.copy();

        assertEquals(vector2f.getX(), copiedVector.getX(), 0);
        assertEquals(vector2f.getY(), copiedVector.getY(), 0);
    }

}
