//package com.game;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//
//import java.util.Random;
//
//
///**
// * LevelManager controls all game resources
// * Navigation Map
// * Game Item Manager
// * View Manager
// * XML IO
// * Loading Screen
// * Dialogues
// * Films
// *
// * given a level number, it loads that level from xml file WHILE
// * putting up a 'loading..' LoadingScreen in place.
// *
// * The level is made up of (NavMap, GameITemManager,ViewManager,Dialogues and Films)
// *
// * Once level is loaded, the GameCanvas's thread is initialised and starts running.
// *
// * The GameCanvas is shell through which user interacts with game.
// *
// * GameCanvas.tick() passes control to LevelManager (which delegates)
// * GameCanvas.render() passes control to LevelManager (which delegates)
// *
// * @author Felix
// *
// */
//
//
//
//public class LevelManager {
//	//static NavMap graph;
//	//static GameItemManager gameItems;
//	//static ViewManager screenView;
//
//	//Film introFilm;
//
//	//used in 'tick', possible states
//	final LevelIntroState beginLevel = new LevelIntroState();
//	final LevelRunState runLevel = new LevelRunState();
//	final LevelBlankState blank = new LevelBlankState ();
//	final LevelLoadState loadLevel = new LevelLoadState();
//	LevelState currentState = blank;
//
//	boolean running;
//
////	LoadingScreen loadingScreen;
//
////	Alert alert = new Alert("LevelManager");
//
//	static byte currentLevel = 0;
//
//	static final byte lastLevel = 7;
//
////	static final Point topLeft = new Point(5,1);
////	static final Point topRight = new Point(SimpleGameCanvas.screenWidth - 15, 1);
////
//	//static final LevelManager instance = new LevelManager();
////	static Dialogue dialogue = null;
//
//	//static Random random;//not instantiated instantly if 'final' (?)
//
//	//static Thread thread;
//
//	public LevelManager(){
//
//	//	random = new Random();
//
//
//	//	loadLevel(currentLevel);
//	}
//
//	public void start(byte i){
//		currentLevel = i;
//		running = true;
//
//		nextState(loadLevel);
//	}
//
//
//	public void clean() {
//		try {
//
////			if (introFilm != null)
////				introFilm.clean();
////
////			introFilm = null;
////
////			if (graph != null)
////				graph.clean();
////			graph = null;
////
////			if (gameItems != null)
////				gameItems.clean();
////			gameItems = null;
////
////			if (screenView != null)
////				screenView.clean();
////			screenView = null;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			System.gc();
//		}
//	}
//
//	public void cleanNonEssentials(){
//
////		if(xmlLoader != null)
////			xmlLoader.clean();
////		xmlLoader = null;
////
////		if(loadingScreen != null)
////			loadingScreen.clean();
////		loadingScreen = null;
//
//		System.gc();
//	}
//
//	/**
//	 * When gameover resets level to '1'
//	 */
//	public void gameOver(){
//		currentLevel = 1;
//
//		clean();
//		cleanNonEssentials();
//
//		running = false;
//	}
//
//
//	StringBuffer string = new StringBuffer();
//    public void draw(Batch batch) {
//
//		currentState.draw(this,batch);
//	}
//
//
//	//heart of the beast
//	public void tick() {
//		try {
//			currentState.runState(this);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	//runs game
//	public synchronized void run() {
//
//		try {
//			while (running)
//				tick();
//		} catch (Exception e) {
//
//			if (loadingScreen == null)
//				alert.setString(alert.getString() + e.toString()
//						+ "\nloadingscreen run()");
//			// else if(dialogue == null)
//			// alert.setString(alert.getString() + e.toString() + "\ndialogue
//			// fail" );
//			if (gameItems == null)
//				alert.setString(alert.getString() + e.toString()
//						+ "\ngameItems fail");
//			if (graph == null)
//				alert.setString(alert.getString() + e.toString()
//						+ "\ngraph fail");
//			if (screenView == null) {
//				alert.setTitle("screen view failed");
//				alert.setString(xmlLoader.tileIndexs + "");
//
//			} else
//				alert.setString(alert.getString() + e.toString()
//						+ "\nrun()?? fail");
//			// if(xmlLoader == null)
//			// alert.setTitle("xmlload??");
//
//			SimpleGameMidlet.mDisplay.setCurrent(alert);
//			e.printStackTrace();
//		}
//	}
//
//
//	//state change
//	public void nextState(LevelState nextState) {
//
//		System.out.println("Changing states from " + currentState + " to "
//				+ nextState);
//
//		if (currentState != null)
//			currentState.exitState(this);
//
//		if (nextState != null)
//			currentState = nextState;
//		currentState.enterState(this);
//
//	}
//
//
//	public void input(int keyStates){
//		currentState.input(this,keyStates);
//	}
//
//
//
//
//}