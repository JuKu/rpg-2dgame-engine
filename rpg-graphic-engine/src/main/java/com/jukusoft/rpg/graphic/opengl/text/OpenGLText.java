package com.jukusoft.rpg.graphic.opengl.text;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;

/**
 * Class to draw an text on HUD
 *
 * @link https://github.com/lwjglgamedev/lwjglbook/blob/82a4ae9741fcc69ecd1f0173dba162ea84559c27/chapter12/c12-p1/src/main/java/org/lwjglb/engine/TextItem.java
 *
 * Created by Justin on 29.11.2016.
 */
public class OpenGLText extends DrawableObject {

    protected static final float ZPOS = 0.0f;

    protected static final int VERTICES_PER_QUAD = 4;

    protected String text = "";

    //font image texture
    protected FontTexture fontTexture = null;

    public OpenGLText (final float x, final float y, String text, FontTexture fontTexture) {
        this.text = text;
        this.fontTexture = fontTexture;

        //generate mesh
        Mesh mesh = TextMeshFactory.createMesh(fontTexture, text);

        //set mesh
        this.setMesh(mesh);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        //set position
        this.setPosition(x, y, 0);

        //https://github.com/lwjglgamedev/lwjglbook/blob/82a4ae9741fcc69ecd1f0173dba162ea84559c27/chapter12/c12-p3/src/main/java/org/lwjglb/game/Hud.java
    }

    public String getText () {
        return this.text;
    }

    public void setText (String text) {
        this.text = text;

        //generate new mesh
        Mesh mesh = TextMeshFactory.createMesh(this.fontTexture, text);

        //delete old mesh
        Mesh oldMesh = getMesh();

        //set new mesh
        this.setMesh(mesh);

        //release old mesh
        oldMesh.cleanUp();
    }

}
