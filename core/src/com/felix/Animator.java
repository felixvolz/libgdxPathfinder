package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by fx on 3/14/2015.
 */
public class Animator  {


    Animation walkAnimation;
    Animation[] walkAnimations;
    Texture walkSheet;
    TextureRegion[]                 walkFrames;
    //TextureRegion[][] walkAnimations;
    //SpriteBatch                     spriteBatch;
    TextureRegion                   currentFrame;
    Sprite sprite;
    float stateTime;
    GridPoint2 position;
    int animationSequences[][];

    public Animator(TextureRegion textureRegion){
        walkSheet = textureRegion.getTexture();
        this.animationSequences = animationSequences;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,  textureRegion.getRegionWidth(),  textureRegion.getRegionHeight());

        int frameCols = walkSheet.getWidth() / textureRegion.getRegionWidth();
        int frameRows= walkSheet.getHeight() / textureRegion.getRegionHeight();

        //Animator - if you dont specify animationSequences it assumes the following ordering
        Animation walkLeft = new Animation(0.1f, tmp[0]);
        Animation walkRight = new Animation(0.1f, tmp[1]);
        Animation walkUp = new Animation(0.1f, tmp[2]);
        Animation walkDown = new Animation(0.1f, tmp[3]);

        walkAnimations = new Animation[]{walkLeft, walkRight, walkUp, walkDown};
        stateTime = 0f;
        position = new GridPoint2(0,0);
        currentFrame = walkAnimations[currentDirection].getKeyFrame(stateTime);
    }

/*    public Animator(Texture texture, int textureWidth, int textureHeight){
        walkSheet = texture;
        this.animationSequences = animationSequences;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,  textureWidth,  textureHeight);

        int frameCols = walkSheet.getWidth() / textureWidth;
        int frameRows= walkSheet.getHeight() / textureHeight;

        //Animator - if you dont specify animationSequences it assumes the following ordering
        Animation walkLeft = new Animation(0.1f, tmp[0]);
        Animation walkRight = new Animation(0.1f, tmp[1]);
        Animation walkUp = new Animation(0.1f, tmp[2]);
        Animation walkDown = new Animation(0.1f, tmp[3]);

        walkAnimations = new Animation[]{walkLeft, walkRight, walkUp, walkDown};
        stateTime = 0f;
        position = new GridPoint2(0,0);
        currentFrame = walkAnimations[currentDirection].getKeyFrame(stateTime);
    }*/
    public Animator(TextureRegion textureRegion, int animationSequences[][]){
        walkSheet = textureRegion.getTexture();
        this.animationSequences = animationSequences;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,  textureRegion.getRegionWidth(),  textureRegion.getRegionHeight());

        int frameCols = walkSheet.getWidth() / textureRegion.getRegionWidth();
        int frameRows= walkSheet.getHeight() / textureRegion.getRegionHeight();

        stateTime = 0f;
        position = new GridPoint2(0,0);


        //TODO tidy up this shockign stuff
        Animation walkLeft = new Animation(0.1f, tmp[animationSequences[0][0]] );
        Animation walkRight = new Animation(0.1f, tmp[animationSequences[1][0]]);
        Animation walkUp = new Animation(0.1f, tmp[animationSequences[2][0]] );
        Animation walkDown = new Animation(0.1f, tmp[animationSequences[3][0]] );

        try {
            //hack as baddie has more animations than player
            Animation found = new Animation(0.1f, tmp[animationSequences[4][0]]);
            Animation lost = new Animation(0.1f, tmp[animationSequences[5][0]]);
            Animation scared = new Animation(0.1f, tmp[animationSequences[6][0]]);


            walkAnimations = new Animation[]{walkLeft, walkRight, walkUp, walkDown, found, lost, scared};
        }catch (Exception e){
            walkAnimations = new Animation[]{walkLeft, walkRight, walkUp, walkDown};
        }
        currentFrame = walkAnimations[currentDirection].getKeyFrame(stateTime);

    }

    public void setPosition(float x, float y ){
        setPosition((int)x, (int)y);
    }
    public void setPosition(int x, int y ){
        setPosition(new GridPoint2(x,y));
    }

    public void setPosition(GridPoint2 p){
        this.position = new GridPoint2(p);
    }

    int currentDirection = MyActor.Moveable.right;
    public void setState(int state){
        try{
            currentDirection = state;


//			for(int i = 0; i < animationSequences[direction].length; i++){
//				System.out.println(animationSequences[direction][i]);
//			}
            //if(animationSequences != null && animationSequences[state]!= null)
                //walkAnimation.setFrameSequence(animationSequences[state]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Batch batch) {
       // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimations[currentDirection].getKeyFrame(stateTime, true);  // #16
        //spriteBatch.begin();
        batch.draw(currentFrame, this.position.x, this.position.y);             // #17
        //spriteBatch.end();
    }
}