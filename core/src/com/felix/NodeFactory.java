package com.felix;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fx on 3/5/2015.
 */
public class NodeFactory {
    static TiledMap tiledMap;

    //TODO is this the best place to capture this info?
    public static class TileMapDetails{
       public static int tilewidth = 0;
       public static int tileheight = 0;

    }



    public static TiledMap getTiledMap(String path){
        return new TmxMapLoader().load(path);
    }

    public static NavGraph  createGraph(TiledMap tiledMap){
        TiledMapTileLayer tiledLayer =(TiledMapTileLayer) tiledMap.getLayers().get("background");

    //    TileMapDetails.tilewidth  =  Integer.parseInt((String)tiledMap.getProperties().get("tilewidth")) ;
    //    TileMapDetails.tileheight  = Integer.parseInt((String)tiledMap.getProperties().get("tileheight")) ;

        TileMapDetails.tilewidth  =   (Integer)tiledMap.getProperties().get("tilewidth")  ;
        TileMapDetails.tileheight  = (Integer)tiledMap.getProperties().get("tileheight")  ;
        return createGraph(tiledLayer);
    }

    public static MyActor getPlayerAgent(TiledMap tiledMap){
        TiledMapTileLayer spriteLayer = (TiledMapTileLayer) tiledMap.getLayers().get("sprite");
        int cellsUp = spriteLayer.getHeight();
        int cellsAcross = spriteLayer.getWidth();

        MyActor actor = null;

        outerloop:
        for(int x = 0; x < cellsAcross; x++) {
            for (int y = 0; y < cellsUp ; y++) {
                TiledMapTileLayer.Cell cell = spriteLayer.getCell(x,y);
                if(cell != null){
                    TiledMapTile tile = cell.getTile();
                    if(tile != null){
                        MapProperties properties = tile.getProperties();
                        String value = (String)properties.get("agent");
                        if( "player".equalsIgnoreCase(value)) {
                            System.out.println(value + "(" + x + "," + y + ")");



                            actor = new MyActor(ImageFactory.getDefaultLRUDAnimator(tile.getTextureRegion()),new GridPoint2(x, y));

                            //actor = new MyActor(tile.getTextureRegion(), new GridPoint2(x, y) , tile.getTextureRegion().getRegionWidth(),  tile.getTextureRegion().getRegionHeight());
                            //now we've found our player sprite, remove him from the tiled map
                            spriteLayer.setCell(x, y, null);
                            break outerloop;
                        }
                    }}
            }
        }
        return actor;
    }

    public static MyActor getBaddyAgent(TiledMap tiledMap){
        TiledMapTileLayer spriteLayer = (TiledMapTileLayer) tiledMap.getLayers().get("sprite");
        int cellsUp = spriteLayer.getHeight();
        int cellsAcross = spriteLayer.getWidth();
        MyActor actor = null;

        outerloop:
        for(int x = 0; x < cellsAcross; x++) {
            for (int y = 0; y < cellsUp ; y++) {
                TiledMapTileLayer.Cell cell = spriteLayer.getCell(x,y);
                if(cell != null){
                    TiledMapTile tile = cell.getTile();
                    if(tile != null){
                        MapProperties properties = tile.getProperties();
                        String value = (String)properties.get("agent");

                        if("baddy".equalsIgnoreCase(value)) {
                            System.out.println(value + "(" + x + "," + y + ")");

                            actor = new MyActor(ImageFactory.getDefaultBaddy(tile.getTextureRegion()),new GridPoint2(x, y));

                            //now we've found our player sprite, remove him from the tiled map
                            spriteLayer.setCell(x, y, null);

                        break outerloop;
                        }
                    }}
            }
        }
        return actor;
    }

    public static NavGraph createGraph(TiledMapTileLayer tiledLayer) {
        int cellsUp = tiledLayer.getHeight();
        int cellsAcross = tiledLayer.getWidth();
        NavGraph graph = new NavGraph(cellsUp, cellsAcross);
        TiledMapTileLayer.Cell cell = null;

        Array<Node> array = new Array<Node>(cellsUp * cellsAcross);
        //first create all the nodes
        int index = 0;
        for (int row = 0; row < cellsUp; ++row) {
            for (int col = 0; col < cellsAcross; ++col) {
                array.add(new Node(index++, new GridPoint2(col,row)));

            }
        }

        //now to calculate the edges. (A position in a 2d array [x][y] is the
        //same as [y*NumCellsX + x] in a 1d array). Each cell has up to four
        //neighbours.
        for (int row = 0; row < cellsUp; ++row) {
            for (int col = 0; col < cellsAcross; ++col) {
                //cell will be null if non-navigable

                cell = tiledLayer.getCell(col,row);

                if(cell != null) {

                    GraphHelper_AddAllNeighboursToGridNode(tiledLayer, array, row, col, cellsAcross, cellsUp);
                }
            }
        }

        graph.setConnections(array);

        return graph;
    }

    /**
     * use to add he four neighboring edges of a graph node that
     * is positioned in a grid layout
     */
    public static void GraphHelper_AddAllNeighboursToGridNode(TiledMapTileLayer tiledLayer, Array<Node> array, int row,
                                                              int col,
                                                              int NumCellsX,
                                                              int NumCellsY) {

        DefaultConnection<Node> connection = null;
        Array<Connection<Node>> connectionArray = new Array<Connection<Node>>();
        TiledMapTileLayer.Cell cell = null;

        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                int nodeX = col + j;
                int nodeY = row + i;

                //skip if equal to this node
                if ((i == 0) && (j == 0)) {
                    continue;
                }

                //skip if a diagonal
                if((java.lang.Math.abs(i) == 1) && (java.lang.Math.abs(j) == 1)){
                    continue;
                }

                //check to see if this is a valid neighbour
                if (ValidNeighbour(nodeX, nodeY, NumCellsX, NumCellsY)) {

                    //cell will be null if non-navigable
                    cell = tiledLayer.getCell(nodeX,nodeY);



                    if(cell != null) {

                        Node node = array.get(row * NumCellsX + col);
                        Node neighbour = array.get(nodeY * NumCellsX + nodeX);

                        connection = new DefaultConnection<Node>(node, neighbour);
                        connectionArray.add(connection);
                    }
                }
            }
        }
        Node node = array.get(row * NumCellsX + col);
        node.setConnections(connectionArray);

    }

    /**
     * @return true if x,y is a valid position in the map
     */
    public static boolean ValidNeighbour(int x, int y, int NumCellsX, int NumCellsY) {
        return !((x < 0) || (x >= NumCellsX) || (y < 0) || (y >= NumCellsY));
    }
}
