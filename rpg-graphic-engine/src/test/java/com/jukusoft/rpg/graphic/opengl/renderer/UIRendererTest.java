package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.math.Matrix4f;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 07.12.2016.
 */
public class UIRendererTest {

    @Test
    public void testProjMatrix () throws Exception {
        //window width and height
        final int width = 1280;
        final int height = 720;

        //create new UI renderer
        UIRenderer renderer = new UIRenderer();

        //get projection matrix
        Matrix4f projMatrix = renderer.getProjMatrix(width, height);

        //with JOML / LWJGL examples calculated result matrix
        Matrix4f resultMatrix = new Matrix4f(
                0.0015625f, 0, 0, -1f,
                0, -0.0027777778f, 0, 1f,
                0, 0, -1f, 0,
                0, 0, 0, 1f
        );

        //compare matrizes
        assertEquals("matrizes arent equals,\nresult matrix:\n" + projMatrix.toString(true) + "\n\nexpected matrix:\n" + resultMatrix.toString(true), true, projMatrix.equals(resultMatrix));
    }

}
