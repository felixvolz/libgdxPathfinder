//package com.game;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.felix.ImageFactory;
//
//import javax.microedition.lcdui.Graphics;
//
//public class LevelLoadState implements LevelState {
//
//	public LevelLoadState() {
//
//	}
//
//	/**
//	 * On entry put 'loading..' screen in place
//	 * @param l
//	 */
//	public void enterState(LevelManager l) {
//
//		System.out
//				.println("entering load level state" + Thread.currentThread());
//		l.loadingScreen = new LoadingScreen();
//		l.loadingScreen.start();
//
//		SimpleGameMidlet.mDisplay.setCurrent(l.loadingScreen);
//
//		l.thread = new Thread(l);
//		l.thread.start();
//
//	}
//
//	public void runState(LevelManager l) {
//
//		l.xmlLoader = new Kxml2IO();
//		l.xmlLoader.setCurrentLevel(l.currentLevel);
//
//		l.xmlLoader.initGameItems();
//
//		l.xmlLoader.initNavMap();
//		l.graph = new NavMap(l.xmlLoader.mapDetails, l.xmlLoader.edgeIndexs);
//		l.xmlLoader.cleanNavMap();
//
//		//requires navmap to be made prior
//		l.gameItems = new GameItemManager(l.xmlLoader.numberBaddies,
//				l.xmlLoader.gameElements);
//
//		System.gc();
//		l.xmlLoader.initView();
//		l.screenView = new ViewManager(ImageFactory.bg(),
//				l.xmlLoader.tileIndexs);
//
//		l.xmlLoader.cleanView();
//
//		l.introFilm = l.xmlLoader.loadFilm();//hmm
//
//		l.xmlLoader.clean();
//		l.xmlLoader = null;
//		System.gc();
//
//		l.loadingScreen.stop();
//		l.cleanNonEssentials();
//
//		l.nextState(l.beginLevel);
//
//	}
//
//	public void exitState(LevelManager l) {
//		//just update all gameitems' screen based position;
//		l.gameItems.updateVisuals();
//		//on exiting load state I want the gamecanvas to load
//		SimpleGameMidlet.mDisplay.setCurrent(SimpleGameCanvas.instance);
//	}
//
//    public void draw(LevelManager l, Batch batch) {
//
//	}
//
//	public void input(LevelManager l, int keyStates) {
//
//	}
//
//}
