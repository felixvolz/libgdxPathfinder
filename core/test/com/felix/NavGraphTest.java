package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

public class NavGraphTest extends TestCase {

    String littleLevelTmxFile = "data/little_level.tmx";
    String bigLevelTmxFile = "data/little_level.tmx";

    public void testNavGraphSearchMap_little_level(){
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
        //Gdx.graphics = mock(Graphics.class);

        TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);
        NavGraph graph = NodeFactory.createGraph(t);
        Array<Connection<Node>> connections;

        //straight line
        Node start = graph.getNodeByCoordinates(2,9);
        Node finish = graph.getNodeByCoordinates(7,9);
        DefaultGraphPath<Node> outpath = new DefaultGraphPath<Node>();
        graph.searchMap(start,finish,outpath);

        assertEquals(outpath.getCount(),6);
        assertEquals(outpath.get(0).getPos(),new GridPoint2(2,9));
        assertEquals(outpath.get(1).getPos(),new GridPoint2(3,9));
        assertEquals(outpath.get(2).getPos(),new GridPoint2(4,9));
        assertEquals(outpath.get(3).getPos(),new GridPoint2(5,9));
        assertEquals(outpath.get(4).getPos(),new GridPoint2(6,9));
        assertEquals(outpath.get(5).getPos(),new GridPoint2(7,9));

//        for(Node n: outpath.nodes){
//            System.out.println(n.getPos().x + ", " + n.getPos().y);
//        }
    }
    public void testNavGraphSearchMap_little_level_2(){
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
        //Gdx.graphics = mock(Graphics.class);

        TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);
        NavGraph graph = NodeFactory.createGraph(t);
        Array<Connection<Node>> connections;

        //there is a fence ruunign from 1,8 to 7,8
        Node start = graph.getNodeByCoordinates(2,9);
        Node finish = graph.getNodeByCoordinates(2,7);
        DefaultGraphPath<Node> outpath = new DefaultGraphPath<Node>();
        graph.searchMap(start,finish,outpath);

//        assertEquals(outpath.getCount(),6);
//        assertEquals(outpath.get(0).getPos(),new GridPoint2(2,9));
//        assertEquals(outpath.get(1).getPos(),new GridPoint2(3,9));
//        assertEquals(outpath.get(2).getPos(),new GridPoint2(4,9));
//        assertEquals(outpath.get(3).getPos(),new GridPoint2(5,9));
//        assertEquals(outpath.get(4).getPos(),new GridPoint2(6,9));
//        assertEquals(outpath.get(5).getPos(),new GridPoint2(7,9));

        for(Node n: outpath.nodes){
            System.out.println(n.getPos().x + ", " + n.getPos().y);
        }
    }

    public void testNavGraphSearchMap_big_level(){
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
        //Gdx.graphics = mock(Graphics.class);

        TiledMap t = NodeFactory.getTiledMap(bigLevelTmxFile);
        NavGraph graph = NodeFactory.createGraph(t);
        Array<Connection<Node>> connections;

        //straight line
        Node start = graph.getNodeByCoordinates(2,9);
        Node finish = graph.getNodeByCoordinates(7,9);
        DefaultGraphPath<Node> outpath = new DefaultGraphPath<Node>();
        graph.searchMap(start,finish,outpath);

        assertEquals(outpath.getCount(),6);
        assertEquals(outpath.get(0).getPos(),new GridPoint2(2,9));
        assertEquals(outpath.get(1).getPos(),new GridPoint2(3,9));
        assertEquals(outpath.get(2).getPos(),new GridPoint2(4,9));
        assertEquals(outpath.get(3).getPos(),new GridPoint2(5,9));
        assertEquals(outpath.get(4).getPos(),new GridPoint2(6,9));
        assertEquals(outpath.get(5).getPos(),new GridPoint2(7,9));

//        for(Node n: outpath.nodes){
//            System.out.println(n.getPos().x + ", " + n.getPos().y);
//        }
    }

}