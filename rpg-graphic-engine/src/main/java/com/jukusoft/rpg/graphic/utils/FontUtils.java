package com.jukusoft.rpg.graphic.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by Justin on 29.11.2016.
 */
public class FontUtils {

    public static String getAllAvailableCharacters (final String charset) {
        //create new charset encoder
        CharsetEncoder charsetEncoder = Charset.forName(charset).newEncoder();

        //create new string builder
        StringBuilder sb = new StringBuilder();

        //iterate throguh possible characters
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            //check, if font supports this character
            if (charsetEncoder.canEncode(c)) {
                //add character to string
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
