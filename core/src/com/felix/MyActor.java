package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.felix.game.TiledApp;

/**
 * Created by fx on 3/9/2015.
 */

/*
TODO remove cellwidth, cellheight info - where put?
 */
public class MyActor extends Actor {
    private static final String TAG = MyActor.class.getName();
    static class Moveable{
        public static final int left = 0;
        public static final int right = 1;
        public static final int up = 2;
        public static final int down = 3;

        //baddie
        public static final int found = 4;
        public static final int lost = 5;
        public static final int scared = 6;
    }
    //Sprite sprite;
    Animator sprite;

    stateOfMind brain; //used by  PathManager.determineStrategy
    public GridPoint2 goal;
    public GridPoint2 cellPos;

    public DefaultGraphPath<Node> path;

    public MyActor(Animator sprite,GridPoint2 cellPos){
        this.sprite = sprite;
        this.cellPos = cellPos;

        //TODO do I need this
        setHeight(sprite.currentFrame.getRegionHeight());
        setWidth(sprite.currentFrame.getRegionWidth());

        setCellPosition(cellPos);

        addListener(new ActorGestureListener(){
            @Override
            public void tap (InputEvent event, float x, float y, int count, int button) {
                Gdx.app.log("ActorGestureListener", "tap at " + x + ", " + y + ", count: " + count);

                TiledApp.cameraHelper.setTarget(TiledApp.cameraHelper.hasTarget() ? null :
                       MyActor.this);

                Gdx.app.log(TAG, "Camera follow enabled: " + TiledApp.cameraHelper.hasTarget());

            }
        });

        path = new DefaultGraphPath<Node>();
        brain = new stateOfMind();
    }

    public void setCellPosition (GridPoint2 pos) {
        //set navgraph position
        this.cellPos = pos;

        //set actor position
        setPosition(cellPos.x * NodeFactory.TileMapDetails.tilewidth, cellPos.y * NodeFactory.TileMapDetails.tileheight);

        //z pos used to determine depth/draw order by group
        setZIndex(cellPos.y);
        //set animator position
        sprite.setPosition(getX(),getY());
    }

//    public MyActor (TextureRegion textureRegion, GridPoint2 cellPos, int tileWidth, int tileHeight,   int animationSequences[][]) {
//        this.sprite = new Animator(textureRegion.getTexture(), tileWidth, tileHeight, animationSequences );
//
//        setHeight(textureRegion.getRegionHeight());
//        setWidth(textureRegion.getRegionWidth());
//
//        setCellPosition(cellPos);
//
//        addListener(new ActorGestureListener(){
//            @Override
//            public void tap (InputEvent event, float x, float y, int count, int button) {
//                Gdx.app.log("ActorGestureListener", "tap at " + x + ", " + y + ", count: " + count);
//
//            }
//        });
//
//        path = new DefaultGraphPath<Node>();
//        brain = new stateOfMind();
//    }
    @Override
    public void act(float delta){
        super.act(delta);

        //if state is 'changing' don't want vehicle to move
        //only to animate that changing between states

        if (brain.changingState) {
               //(--brain.changingStateFrameDurationCountdown == 0) hack to hold for 5 frames
            if (brain.previousState == stateOfMind.search && brain.currentState == stateOfMind.attack ) {
                sprite.setState(Moveable.found);

            } else if (brain.previousState == stateOfMind.attack && brain.currentState == stateOfMind.search ) {
                sprite.setState(Moveable.lost);
            }
        } else if(path != null && path.getCount() > 0){
            Node node = path.nodes.removeIndex(0);

            if(!cellPos.equals(node.pos)) {
                //only change direction if on diff node
                sprite.setState(determineMoveDirection(cellPos, node.pos));
            }

            setCellPosition(node.pos);

        }

    }

    public void setPath(DefaultGraphPath<Node> path){

        this.path = path;
    }

    public void draw (Batch batch, float parentAlpha) {
        sprite.draw(batch);

        if(getDebug()){
            //draw path
        }

    }




//    @Override
/*    public Actor hit (float x, float y, boolean touchable) {
        Actor a =  super.hit(x,y,touchable);
        if (a != null) {Gdx.app.log("MyActor","MyActor  hit at " + x + ", " + y   );}
        return a;
    }*/

    public static int determineMoveDirection(GridPoint2 from, GridPoint2 to){
        int x = from.x - to.x;
        int y = from.y - to.y;

        if(x < 0)return Moveable.right;
        if(x > 0)return Moveable.left;
        if(y > 0)return Moveable.down;
        else return Moveable.up;
    }

}
