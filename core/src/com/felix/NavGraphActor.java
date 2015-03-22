package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.felix.game.TiledApp;

/**
 * Created by fx on 3/9/2015.
 */
public class NavGraphActor extends Group {
        int tileWidth,tileHeight;
        NavGraph navGraph;

        Node startNode, endNode;
        Actor endActor;

        public NavGraphActor(final int tileWidth, final int tileHeight, final NavGraph navGraph) {

            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;

            setHeight(tileWidth);
            setWidth(tileHeight);

            this.navGraph = navGraph;
            addListener(new ActorGestureListener(){
                @Override
                public void tap (InputEvent event, float x, float y, int count, int button) {
                    Gdx.app.log("NavGraphActorGestureListener", "tap at " + x + ", " + y + ", count: " + count);


                    try {
                        startNode = navGraph.getNodeByCoordinates(player.cellPos.x,player.cellPos.y);

                        endNode = navGraph.getNodeByCoordinates(x / 20, y / 20);
                        navGraph.endNode = endNode;


                        Gdx.app.log("NavGraphActorGestureListener", "Node " + endNode.getPos().x + ", " + endNode.getPos().y);

                        //debug on/off hack
                        if(endNode.getPos().x == 1 && endNode.getPos().y == 1){
                            navGraph.setDebug(!navGraph.debug);

                            NavGraphActor.this.getStage().setDebugAll(!NavGraphActor.this.getDebug());
                        }

                        if(endActor != null){
                            endActor.remove();
                        }

                        endActor = new Actor();
                        endActor.setX(endNode.getPos().x * 20);
                        endActor.setY(endNode.getPos().y * 20);
                        endActor.setHeight(20);
                        endActor.setWidth(20);
                        NavGraphActor.this.addActor(endActor);


                        TiledApp.pathManager.setGoal(player,endNode.getPos());

//                        DefaultGraphPath<Node> outpath = new DefaultGraphPath<Node>();
//                        if(navGraph.searchMap(startNode,endNode, outpath)){
//                            player.setPath(outpath);
//                        }

                    }catch(Exception e){Gdx.app.log("NavGraphActorGestureListener", "Exception " + e);}
                }
            });
        }

   MyActor player = null;
   MyActor baddy = null;
    public void addPlayer(MyActor player){
        super.addActor(player);
        this.player = player;

    }

    public void addBaddy(MyActor player){
        super.addActor(player);
        this.baddy = player;

    }


    @Override
    public void act(float delta){
        super.act(delta);




    }

/*    public void draw (Batch batch, float parentAlpha) {
        //batch.draw(textureRegion,0,0);
    }*/

//    @Override
//    public Actor hit (float x, float y, boolean touchable) {
//        Actor a =  super.hit(x,y,touchable);
//        if (a != null) {Gdx.app.log("NavGraphActor","NavGraphActor  hit at " + x + ", " + y   );}
//        return a;
//    }
}
