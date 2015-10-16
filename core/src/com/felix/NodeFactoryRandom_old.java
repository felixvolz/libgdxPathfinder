package com.felix;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fx on 3/5/2015.
 */
public class NodeFactoryRandom_old {

    public static NavGraph createGraph(int cellsUp, int cellsAcross) {
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
                GraphHelper_AddAllNeighboursToGridNode(array, row, col, cellsAcross, cellsUp);

            }
        }

        graph.setConnections(array);

        return graph;
    }



    /**
     * use to add he four neighboring edges of a graph node that
     * is positioned in a grid layout
     */
    public static void GraphHelper_AddAllNeighboursToGridNode(Array<Node> array, int row,
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
                if((Math.abs(i) == 1) && (Math.abs(j) == 1)){
                    continue;
                }

                //check to see if this is a valid neighbour
                if (ValidNeighbour(nodeX, nodeY, NumCellsX, NumCellsY)) {

                    //cell will be null if non-navigable
                   // cell = tiledLayer.getCell(nodeX,nodeY);



                  //  if(cell != null) {

                        Node node = array.get(row * NumCellsX + col);
                        Node neighbour = array.get(nodeY * NumCellsX + nodeX);

                        connection = new DefaultConnection<Node>(node, neighbour);
                        connectionArray.add(connection);
                   // }
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
