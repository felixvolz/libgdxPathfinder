package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

/**
 * Created by fx on 3/14/2015.
 */
public class PathManagerTest extends TestCase {

    TiledMap tiledMap;
    NavGraph navGraph;
    MyActor player;
    MyActor baddie;
    protected void setUp() throws Exception {
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

        tiledMap = NodeFactory.getTiledMap("assets/data/test_tiled.tmx");
        navGraph = NodeFactory.createGraph(tiledMap);

        player = NodeFactory.getPlayerAgent(tiledMap);
        baddie = NodeFactory.getBaddyAgent(tiledMap);

    }

    protected void tearDown() throws Exception {
    }

    public void testPathManager_setGoal(){
        PathManager pathManager = new PathManager(navGraph,baddie, player);

        assertNotNull(pathManager);

        //loaded from known tile map
        assertEquals(player.cellPos, new GridPoint2(2,0));
        assertEquals(baddie.cellPos, new GridPoint2(9,9));

        assertEquals(player.path.getCount(),0);

        //pathmanager sets path from 2,0 to 9,9
        pathManager.setGoal(player,baddie.cellPos);

        //21 steps from 2,0 to 9,9
        assertEquals(player.path.getCount(),21);

        assertEquals(player.path.get(0).getPos(),new GridPoint2(2,0));
        assertEquals(player.path.get(1).getPos(),new GridPoint2(3,0));
        assertEquals(player.path.get(2).getPos(),new GridPoint2(4,0));
        assertEquals(player.path.get(3).getPos(),new GridPoint2(5,0));
        assertEquals(player.path.get(4).getPos(),new GridPoint2(6,0));
        assertEquals(player.path.get(5).getPos(),new GridPoint2(7,0));
        assertEquals(player.path.get(6).getPos(),new GridPoint2(8,0));

        assertEquals(player.path.get(7).getPos(),new GridPoint2(8,1));
        assertEquals(player.path.get(8).getPos(),new GridPoint2(8,2));
        assertEquals(player.path.get(9).getPos(),new GridPoint2(7,2));
        assertEquals(player.path.get(10).getPos(),new GridPoint2(6,2));
        assertEquals(player.path.get(11).getPos(),new GridPoint2(6,3));
        assertEquals(player.path.get(12).getPos(),new GridPoint2(6,4));

        assertEquals(player.path.get(13).getPos(),new GridPoint2(6,5));
        //cant be bothered doing the rest - looks good so far
    }

    public void testPathManager_newSearch() {
        PathManager pathManager = new PathManager(navGraph, baddie, player);

        assertNotNull(pathManager);

        //loaded from known tile map
        assertEquals(player.cellPos, new GridPoint2(2, 0));
        assertEquals(baddie.cellPos, new GridPoint2(9, 9));

        assertEquals(player.path.getCount(), 0);

        //pathmanager sets path from 2,0 to 9,9
        pathManager.newSearch(player);

        for(int i = 0; i< player.path.getCount(); i++){
            System.out.println(player.path.get(i).getPos().x +", " + player.path.get(i).getPos().y );
        }

    }
}
