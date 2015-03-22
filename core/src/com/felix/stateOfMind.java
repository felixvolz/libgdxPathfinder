package com.felix;


public class stateOfMind {
	
	public static final byte stuck = 0;
	public static final byte attack = 1;
	public static final byte search = 2;
	
	public static final byte wait = 3; 
	public static final byte getInToPosition = 4;

	public byte previousState = search;
	public byte currentState = search;

	public boolean changingState = false;

    public int changingStateFrameDurationCountdown = 0;

	public stateOfMind(){
		currentState = search;
	}
	
	public stateOfMind(byte task) {
		currentState = task;
	}
	
	public void currentState(byte newState){
		
		//if changing between states want state change animation
		//only have attack/search to worry about
		if(currentState != newState){
			changingState = true;
            changingStateFrameDurationCountdown = 3;
		}else{
            //forces changingState to be true for a count of 5
            if(changingStateFrameDurationCountdown-- < 0) {
                changingStateFrameDurationCountdown = 5;
                changingState = false;
            }
		}
		
		previousState = currentState;
		currentState = newState;
	}
	


}
