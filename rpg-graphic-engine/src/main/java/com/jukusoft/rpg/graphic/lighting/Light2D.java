package com.jukusoft.rpg.graphic.lighting;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;

/**
 * Created by Justin on 17.12.2016.
 */
public interface Light2D {

    public float getX ();

    public float getY ();

    public void setPosition2D (final float x, final float y);

    public OpenGL2DImage getLightTexture ();

    public boolean isLightOscillateEnabled ();

    public Vector3f getPosition ();

    public Vector3f getRotation ();

    public float getScale ();

    public Material getMaterial ();

    public void renderLight (final float windowWidth, final float windowHeight);

    public void setTextureBank (int textureBank);

}
