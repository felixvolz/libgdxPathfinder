package com.felix;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;


public class PathManager {


	public static final GridPoint2  attackLimit = new GridPoint2(3,3);
	public static final GridPoint2 searchLimit = new GridPoint2(10,10);

    //MyActor baddie;
    Array<MyActor> vehicles;
    MyActor target;
	NavGraph graph;

    GridPoint2 goal = new GridPoint2(7,0);

	//protect (the last coin??)
	//direct chase, flank (left,right), circle behind, fall back (and cover??) guard..
	public PathManager(NavGraph graph,MyActor baddie, MyActor player) {
		this.graph = graph;
		//this.baddie = baddie;
		this.target = player;

        vehicles = new Array<MyActor>();
        vehicles.add(baddie);
	}





//	public boolean setGoal(MyActor v, short goalIndex){
//		return setGoal(v, graph.positionBasedOnIndex(goalIndex));
//	}
	/**
	 * Sets given vehicle a Point to navigate towards
	 * @param goal
	 */
	public boolean setGoal(MyActor actor, GridPoint2 goal){
        actor.path.clear();
		actor.goal = goal;

		return graph.searchMap(actor.cellPos, goal, actor.path);
	}

	public void tick(){
		determineStrategy();
	}

	/*FiniteStateMachine
	 *
	 *'Search' (till locate Player) then 'Surround' (team strat.)
	 *During 'Surround' the opponents are either in 'wait' mode (they've reached their goal)
	 *or in 'gettingToPosition' mode (they are moving to position)
	 *Once 'Surround' is achieved then 'Attack'.
	 *Once 'Attack' fails then 'Search'
	 *
	 */

	public void determineStrategy() {

		int  size = vehicles.size;
		for (byte i = 0; i < size; i++) {

			MyActor v =  vehicles.get(i);

            //distance to target
			GridPoint2 distance = graph.absoluteManhattanDistanceByPoint(v.cellPos, target.cellPos);

			if (distance.x <= attackLimit.x && distance.y <= attackLimit.y) {
				v.brain.currentState(stateOfMind.attack);
			} else {
				v.brain.currentState(stateOfMind.search);
			}

			switch (v.brain.currentState) {
			case stateOfMind.attack:
				attack(v);
				break;
			case stateOfMind.search:
				search(v);
				break;
			case stateOfMind.stuck: // TODO attack(v) gets enemy unstuck
				attack(v);
				break;
			default:
				newSearch(v);
			}
		}
	}

	public void attack(MyActor v){
		if(target.cellPos != v.goal || (v.path.nodes.size == 0) )
		{
					setGoal(v,target.cellPos);
		}
	}

	//can set new search even if edgelist not empty
	public void newSearch(MyActor v){
		GridPoint2 randomPoint;
		do {

            double randomX = Math.random();
            double randomY = Math.random();
            int cellX = (int)Math.abs(randomX * graph.cellsAcross);
            int cellY = (int)Math.abs(randomY * graph.cellsUp);



			randomPoint = new GridPoint2(cellX,cellY);

		} while (!setGoal(v, randomPoint)); //loop till valid random Point found
	}

	//only 'search' if edgelist is empty
	public void search(MyActor v){
		if(v.path.nodes.size == 0)
		{
			newSearch(v);
		}
	}



}
