package com.felix;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fx on 3/8/2015.
 */
public class Node implements IndexedNode<Node> {
    public int index;
    public GridPoint2 pos;
    public Array<Connection<Node>> connections;

    public Node (int index, GridPoint2 pos){
        this.index = index;
        this.pos = pos;
    }


    public void setConnections(Array<Connection<Node>> connections) {
        this.connections = connections;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    public GridPoint2 getPos(){return this.pos;}
    @Override
    public Array<Connection<Node>> getConnections() {
        return connections;
    }
}
