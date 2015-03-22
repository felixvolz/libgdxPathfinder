package com.felix;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class ImageFactory {

    public static final Animator getDefaultLRUDAnimator(TextureRegion textureRegion){
       return new Animator(textureRegion);
    }
    public static final Animator getAnimatorWithAnimationSequences(TextureRegion textureRegion, int[][] animationSequence){
        return new Animator(textureRegion, animationSequence);
    }

    public static final Animator getDefaultBaddy(TextureRegion textureRegion){
        return new Animator(textureRegion, new int[][]{{0},{1},{2},{3},{4},{5},{6}});
    }
}
