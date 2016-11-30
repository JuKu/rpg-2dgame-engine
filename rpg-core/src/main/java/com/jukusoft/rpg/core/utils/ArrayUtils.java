package com.jukusoft.rpg.core.utils;

import java.util.List;

/**
 * Created by Justin on 30.11.2016.
 */
public class ArrayUtils {

    public static float[] convertFloatListToArray (List<Float> list) {
        if (list == null) {
            return new float[0];
        }

        //create new float array
        float array[] = new float[list.size()];

        //iterate through list
        for (int i = 0; i < list.size(); i++) {
            //get float and set to array
            array[i] = list.get(i);
        }

        return array;
    }

}
