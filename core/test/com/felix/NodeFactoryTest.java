package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

public class NodeFactoryTest extends TestCase {


    String littleLevelTmxFile = "data/little_level.tmx";
    String bigLevelTmxFile = "data/little_level.tmx";


    public void testLoadTiledMapAndNodeConnections_littleLevel(){
        //Create Gdx.gl

        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

       TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);
       NavGraph graph = NodeFactory.createGraph(t);

        //bottom left node - 2 connections
        Node node = graph.getNodeByCoordinates(0,0);
        assertEquals(0, node.getIndex());
        Array<Connection<Node>> connections = node.getConnections();
        assertEquals(connections.size,2);

        //middle node - 4 connections
        node = graph.getNodeByCoordinates(4,6);
        assertEquals(64, node.getIndex());
        connections = node.getConnections();
        assertEquals(connections.size,4);

        //empty node - 0 connections
        node = graph.getNodeByCoordinates(2,3);
        assertEquals(32, node.getIndex());
        connections = node.getConnections();
        assertEquals(connections.size,0);

        assertNotNull(t);

    }

    public void testAndPrintAllTileMapConnections(){
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

        TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);
        NavGraph graph = NodeFactory.createGraph(t);
        Array<Connection<Node>> connections;
        Node node,neighbour;
        int offset = 10;
        int width = 20;
        int height = 20;
        for(int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                node = graph.getNodeByCoordinates(x, y);
                connections = node.getConnections();

                System.out.println("Node " + node.getIndex() + "("  + x + "," + y + ")");
                if (connections == null) {
                    System.out.println(" -> no connections");
                    continue;
                }
                for (Connection<Node> connection : connections) {
                    neighbour = connection.getToNode();
                    int index = neighbour.getIndex();
                    int neighbourY = (int) Math.floor(index / 10);
                    int neighbourX = index % 10;



                    System.out.println("Connection index:" + index +", (" + neighbourX + "," + neighbourY + ")");
                    System.out.println(" drawing from (" + x * width  + "," + y * height + ") to (" + neighbourX * width  + "," + neighbourY * height + ")") ;


                }

            }
        }
    }

    public void testModulus(){

         assertEquals(2, 32 % 10);
         assertEquals(3, (int)Math.floor(32 / 10));
    }

    public void testCreateActor(){
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);

        TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);
       // NavGraph graph = NodeFactory.createGraph(t);

        TiledMapTileLayer spriteLayer = (TiledMapTileLayer) t.getLayers().get("sprite");
        for(int x = 0; x < 10; x++) {
            for (int y = 0; y < 10 ; y++) {
                TiledMapTileLayer.Cell cell = spriteLayer.getCell(x,y);
                if(cell != null){
               TiledMapTile tile = cell.getTile();
                if(tile != null){
                    MapProperties properties = tile.getProperties();
                    String value = (String)properties.get("agent");
                    System.out.println(value + "("  + x + "," + y + ")");

                    assert(false);
                   // MyActor actor = new MyActor(tile.getTextureRegion(),new GridPoint2(x,y),20,20,20,20, null);


                }}
            }
        }

        assertEquals(true, true);
    }


/*    public void testGraphHelper_AddAllNeighboursToGridNode() throws Exception {

    }*/

  /*  public void testValidNeighbour() throws Exception {

    }*/
}