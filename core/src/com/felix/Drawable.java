package com.felix;


import com.badlogic.gdx.graphics.g2d.Sprite;

public class Drawable {

    static class Moveable{
        public static final byte left = 0;
        public static final byte right = 1;
        public static final byte up = 2;
        public static final byte down = 3;
    }

	Sprite character;
	int currentDirection = -1;
	int animationSequences[][];
	
	public Drawable(Drawable d){
		this(new Sprite(d.character),d.animationSequences);
	}
	/**ImageFactory.java
	 * Given a Sprite and a multidimensional array of animation sequences
	 * that tie in with 4 directions (i.e. [0][..] will be character walking left)
	 * @param character Sprite to be drawn
	 * @param animationSequences array of framenumbers (for when animating)
	 */
	
	public Drawable(Sprite character, int animationSequences[][]) {
		this.character = character;
		this.animationSequences = animationSequences;
		

		setState(Moveable.left);
		
		//System.out.println(character.getFrameSequenceLength());
	}
	

	public void setState(byte state){
		try{
		//	currentDirection = state;
//			for(int i = 0; i < animationSequences[direction].length; i++){
//				System.out.println(animationSequences[direction][i]);
//			}
		if(animationSequences != null && animationSequences[state]!= null)	{}
			//character.setFrameSequence(animationSequences[state]);
            currentDirection = animationSequences[state][0];
            System.out.print("Current Direction" + currentDirection + ", state: " + state);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
//	//hmm sets animation state of vehicle todo
//	public void setState(int[] state){
//		try{
//		if(state != null)	
//			character.setFrameSequence(state);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * sets nextFrame() of current animation sequence
	 */
	public void tick(){
		//this.character.nextFrame();
	}
}
