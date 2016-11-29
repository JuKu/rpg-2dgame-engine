package com.jukusoft.rpg.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Justin on 23.08.2016.
 */
public class ResourceUtils {

    public static String loadStringFromResource (String path) throws IOException {
        String str = "";

        //read file
        try (InputStream in = ResourceUtils.class.getClass().getResourceAsStream(path)) {
            str = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }

        return str;
    }

}
