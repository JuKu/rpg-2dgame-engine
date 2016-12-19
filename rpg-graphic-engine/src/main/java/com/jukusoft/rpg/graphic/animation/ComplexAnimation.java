package com.jukusoft.rpg.graphic.animation;

import com.jukusoft.rpg.graphic.opengl.buffer.FrameBufferObject;
import com.jukusoft.rpg.graphic.renderer.BasicRenderable;
import com.jukusoft.rpg.graphic.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * An complex animation contains more than one animation layer, which are drawn in an ordered way,
 * for example an character has an layer for the body images, the weapons and so on
 *
 * Created by Justin on 13.12.2016.
 */
public class ComplexAnimation extends BasicRenderable implements Animable {

    /**
    * current states
    */
    protected List<String> currentStates = new ArrayList<>();

    /**
    * available animation states
    */
    protected List<String> availableStates = new ArrayList<>();

    protected List<BasicAnimation> currentLayers = new ArrayList<>();

    public void setCurrentState (String stateName) {
        //
    }

    @Override
    public void updateFrame(long now) {
        //update each layer
        for (BasicAnimation animation : this.currentLayers) {
            animation.updateFrame(now);
        }
    }

    @Override
    public void render(FrameBufferObject lightingMap) {
        //
    }

    @Override
    public void render() {
        this.render(null);
    }

}
