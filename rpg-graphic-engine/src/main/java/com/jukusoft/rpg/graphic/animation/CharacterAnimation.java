package com.jukusoft.rpg.graphic.animation;

import com.jukusoft.rpg.core.asset.Direction;

/**
 * Created by Justin on 13.12.2016.
 */
public class CharacterAnimation {

    /**
    *
    */
    protected Direction direction = Direction.DOWN;

    protected boolean isWalking = false;

    /**
    * if there are different animations for different speeds, we can use this variable
    */
    protected int walkingSpeed = 0;

}
