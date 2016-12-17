package com.jukusoft.rpg.core.math;

import java.util.Random;

/**
 * Created by Justin on 17.12.2016.
 */
public class RandomUtils {

    protected static Random random = new Random();

    public static float random () {
        return random.nextFloat();
    }

}
