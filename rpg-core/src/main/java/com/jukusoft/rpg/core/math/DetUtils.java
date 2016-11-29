package com.jukusoft.rpg.core.math;

/**
 * Created by Justin on 23.11.2016.
 */
public class DetUtils {

    /**
    * calculate determinate of 3x3 matrix
     *
     * a b c
     * d e f
     * g h i
    */
    public static float calculateDet3x3 (final float a, final float b, final float c,
                                         final float d, final float e, final float f,
                                         final float g, final float h, final float i) {

        return (a * e * i) + (b * f * g) + (c * d * h) - (g * e * c) - (h * f * a) - (i * d * b);
    }

    /**
     * calculate determinate of 4x4 matrix
     *
     * a b c d
     * e f g h
     * i j k l
     * m n o p
     */
    public static float calculateDet4x4 (final float a, final float b, final float c, final float d,
                                         final float e, final float f, final float g, final float h,
                                         final float i, final float j, final float k, final float l,
                                         final float m, final float n, final float o, final float p) {

        final float v1 = a * DetUtils.calculateDet3x3(
                f, g, h,
                j, k, l,
                n, o, p
        );

        final float v2 = b * DetUtils.calculateDet3x3(
                e, g, h,
                i, k, l,
                m, o, p
        );

        final float v3 = c * DetUtils.calculateDet3x3(
                e, f, h,
                i, j, l,
                m, n, p
        );

        final float v4 = d * DetUtils.calculateDet3x3(
                e, f, g,
                i, j, k,
                m, n, o
        );

        return v1 - v2 + v3 - v4;
    }

}
