package com.felix;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fx on 3/8/2015.
 */

/*
TODO remove the hardcoded 10 in render
TODO add the cell width, cell height info?

 */
public class NavGraph extends DefaultIndexedGraph<Node> {
    int cellsAcross, cellsUp;
    ShapeRenderer sr;
    IndexedAStarPathFinder<Node> pathFinder;

    Node startNode, endNode;
    boolean debug;

    public NavGraph(int cellsAcross, int cellsUp){
        super();
        this.cellsAcross =cellsAcross;
        this.cellsUp = cellsUp;

        sr = new ShapeRenderer();
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    public void setConnections(Array<Node> nodes){
        this.nodes = nodes;
        pathFinder = new IndexedAStarPathFinder<Node>(this);

        //testing connectoins
        Array<Connection<Node>> connections;
        Node node,neighbour;
        int offset = 10;
        int width = 20;
        int height = 20;
        for(int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                node = getNodeByCoordinates(x, y);
                connections = node.getConnections();

                System.out.println("Node " + node.getIndex() + "(" + x + "," + y + ")");
                if (connections == null) {
                    System.out.println(" -> no connections");
                    continue;
                }
                for (Connection<Node> connection : connections) {
                    neighbour = connection.getToNode();
                    int index = neighbour.getIndex();
                    int neighbourY = (int) Math.floor(index / 10);
                    int neighbourX = index % 10;


                    System.out.println("Connection index:" + index + ", (" + neighbourX + "," + neighbourY + ")");
                    System.out.println(" drawing from (" + x * width + "," + y * height + ") to (" + neighbourX * width + "," + neighbourY * height + ")");


                }

            }
        }
    }
   /* public NavGraph(Array<Node> nodes) {
        super(nodes);
    }*/


    public Node getNode(int index){
        return this.nodes.get(index);
    }

    public Node getNodeByCoordinates(float x, float y){
        return getNodeByCoordinates((int) x, (int) y);

    }

    public Node getNodeByCoordinates(int x, int y){
        //libgdx defaults to  y-up coordinates (i.e. 0,0 in bottom left corner, 9,9 in top hand right)
        //tiled default to y-down coordinates (i.e. 0,0, in top LC, 9,9, in bottom RC)

        //  return this.nodes.get(y * cellsAcross + x);// we are reading a Tiled tmx, this wont work
        return this.nodes.get(y * cellsAcross + x);

    }
    public Node getNodeByTranslatedCoordinates(float x, float y){
        return getNodeByTranslatedCoordinates((int) x, (int) y);

    }
    public Node getNodeByTranslatedCoordinates(int x, int y){
        //libgdx defaults to  y-up coordinates (i.e. 0,0 in bottom left corner, 9,9 in top hand right)
        //tiled default to y-down coordinates (i.e. 0,0, in top LC, 9,9, in bottom RC)
            y = 9 - y;
            x = x;
      //  return this.nodes.get(y * cellsAcross + x);// we are reading a Tiled tmx, this wont work
      return this.nodes.get(y * cellsAcross + x);

    }

    //render assistanct
    int offset = 10;
    int width = 20;
    int height = 20;
    Array<Connection<Node>> connections;
    Node node,neighbour;
    public void render(OrthographicCamera camera){
        if(!debug)return;

        sr.setColor(Color.RED);
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);



        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                node = getNodeByCoordinates(x, y);
                connections = node.getConnections();
                if(connections == null){continue;}
                for(Connection<Node> connection: connections){
                    neighbour = connection.getToNode();
                    int index = neighbour.getIndex();
                    int neighbourY =  (int)Math.floor(index / 10);
                    int neighbourX = index % 10;

                    sr.rectLine(x * width + offset, y * height + offset, neighbourX * width + offset, neighbourY * height + offset, 1);
                }

            }
        }

        if(outpath != null){
            sr.setColor(Color.BLUE);
           for(int i = 0; i < outpath.getCount();i++){
               Node n = outpath.get(i);
               int x,y;
               x = n.getPos().x;
               y = n.getPos().y;
               connections = node.getConnections();
               if(connections == null){continue;}
               for(Connection<Node> connection: connections){
                   neighbour = connection.getToNode();
                   int index = neighbour.getIndex();
                   int neighbourY =  (int)Math.floor(index / 10);
                   int neighbourX = index % 10;

                   sr.rectLine(x * width , y * height , x * width + 2 , y * height + 2, 4);
               }
              // Gdx.app.log("NavGraph outpath output", "Node " + n.getPos().x + ", " + n.getPos().y);

           }
        }


        sr.end();

    }

    public boolean searchMap(GridPoint2 start, GridPoint2 finish, DefaultGraphPath<Node> outpath) {
        Node s = getNodeByCoordinates(start.x, start.y);
        Node f = getNodeByCoordinates(finish.x, finish.y);

        return searchMap( s,  f, outpath);
    }
    DefaultGraphPath<Node> outpath;


    public boolean searchMap(Node s, Node f) {
        outpath = new DefaultGraphPath<Node>();
        return searchMap(s,f,outpath);
    }


    public boolean searchMap(Node s, Node f,DefaultGraphPath<Node> outpath) {
       // outpath = new DefaultGraphPath<Node>();

       return pathFinder.searchNodePath(s,f,new Heuristic<Node>() {
            @Override
            public float estimate(Node node, Node endNode) {
                GridPoint2 current = node.getPos();
                GridPoint2 goal = endNode.getPos();

                //System.out.println("manhattan from " + current.x +"," + current.y + " to " + goal.x +"," + goal.y  + " " + (Math.abs(current.x - goal.x)+(Math.abs(current.y-goal.y))));
                return manhattanDistanceByShort(current,goal);
            }
        },outpath);
    }

    //expected cost Point(cost.x, cost.y) from current indexBasedPosition to goal indexBasedPosition
    public GridPoint2 absoluteManhattanDistanceByPoint(GridPoint2 current, GridPoint2 goal){
        return new GridPoint2(Math.abs(current.x - goal.x),(Math.abs(current.y-goal.y)));
    }

    //returns directional Point to goal.
    //(i.e.) point(5,-1) is 5 to right and 1 up
    public GridPoint2 manhattanDistanceByPoint(GridPoint2 current, GridPoint2 goal){
        return new GridPoint2((goal.x - current.x),((goal.y - current.y)));
    }

    public short manhattanDistanceByShort(GridPoint2 current, GridPoint2 goal){
        return (short)(Math.abs(current.x - goal.x)+(Math.abs(current.y-goal.y)));
    }
}
