package com.jukusoft.rpg.graphic.animation;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DTextureRegion;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 11.12.2016.
 */
public class BasicAnimation extends OpenGL2DTextureRegion implements Animable {

    /**
    * timestamp of last frame change
    */
    protected long lastFrameUpdate = 0l;

    /**
    * interval for next frame
    */
    protected long nextInterval = 0l;

    /**
    * index of current frame
    */
    protected int currentFrameIndex = 0;

    /**
    * instance of current frame
    */
    protected volatile Frame currentFrame = null;

    /**
    * list with all frames
    */
    protected List<Frame> frames = new ArrayList<>();

    /**
    * cached value of frames.size(), for performance improvements
    */
    protected int cachedFrameCount = 0;

    /**
    * default constructor
    */
    public BasicAnimation (final float x, final float y, OpenGL2DTexture texture) {
        super(x, y, 0, 0, 0, 0, texture);
    }

    @Override
    public void updateFrame(long now) {
        if (this.cachedFrameCount == 0) {
            //GameLogger.warn("BasicAnimation", "no frames added to animation.");
            return;
        }

        if (this.lastFrameUpdate + this.nextInterval < now) {
            final int frameCount = cachedFrameCount;

            //calculate new frame index
            this.currentFrameIndex = (this.currentFrameIndex + 1) % frameCount;

            //get frame
            this.currentFrame = this.frames.get(this.currentFrameIndex);

            if (this.currentFrame == null) {
                GameLogger.warn("BasicAnimation", "no frame available for animation, add frames with addFrame() method.");
            } else {
                this.nextInterval = this.currentFrame.getDuration();

                //update texture region
                this.updateFrameRegion(this.currentFrame);
            }

            //update last change timestamp
            this.lastFrameUpdate = now;
        }
    }

    private void updateFrameRegion (final Frame frame) {
        this.setRegion(frame.getStartX(), frame.getStartY(), frame.getWidth(), frame.getHeight());
    }

    public void addFrame (Frame frame) {
        this.frames.add(frame);
        this.cachedFrameCount = this.frames.size();
    }

    public void removeFrame (Frame frame) {
        this.frames.remove(frame);
        this.cachedFrameCount = this.frames.size();
    }

    public void clearAllFrames () {
        GameLogger.debug("BasicAnimation", "clear all animation frames.");

        this.cachedFrameCount = 0;
        this.frames.clear();
    }

}
