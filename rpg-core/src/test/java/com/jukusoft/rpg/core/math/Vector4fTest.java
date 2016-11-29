package com.jukusoft.rpg.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 27.11.2016.
 */
public class Vector4fTest {

    @Test
    public void testGetterAndSetter () {
        //create new vector
        Vector4f vector = new Vector4f(1, 2, 3);

        //check getter
        assertEquals(1, vector.getX(), 0);
        assertEquals(2, vector.getY(), 0);
        assertEquals(3, vector.getZ(), 0);

        //check setter and getter
        vector.setX(4);
        vector.setY(5);
        vector.setZ(6);

        //check values
        assertEquals(4, vector.getX(), 0);
        assertEquals(5, vector.getY(), 0);
        assertEquals(6, vector.getZ(), 0);
    }

}
