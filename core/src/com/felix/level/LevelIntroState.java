//package com.game;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//
//import javax.microedition.lcdui.Graphics;
//import javax.microedition.lcdui.game.GameCanvas;
//
///**
// * LevelIntroState is the 'second' possible state that the LEvelManager can be in.
// *
// * At the beginning of the level we can see the character on the level map.
// * If there is an intro 'Film' this will run (it might show the layout of the
// * level or a new type of baddie/goal).
// *
// * On completion of this 'Film' instance the state will evolve to runLevelState
// * @author Felix
// *
// */
//public class LevelIntroState implements LevelState{
//
//	public LevelIntroState() {
//
//	}
//
//
//	public void enterState(LevelManager l){
//
//		//testing if introFilm loaded, not always the case
//		if (l.introFilm != null) {
//		//	System.out.println("the films still got stuf  to show " + Thread.currentThread());
//			l.introFilm.next();
//
//		} else {
//		//System.out.println("nothing to see here, films noexistanet " + Thread.currentThread());
//			l.nextState(l.runLevel);
//		}
//	}
//
//	//runs intro film while introFilm.tick() returns 'True'
//	//will return 'false' when finished running.
//	public void runState(LevelManager l) {
//		try {
//			SimpleGameCanvas.instance.tick();
//
////			System.out.println("intro level .runstate " + l
////					+ Thread.currentThread());
//			if (l.introFilm != null) {
//				if (l.introFilm.tick()) {
//					if (l.introFilm.getPosition() != null)
//						l.screenView.setViewWindow(l.introFilm.getPosition());
//				} else {
//					l.nextState(l.runLevel);
//				}
//			}else{
//				l.nextState(l.runLevel);
//			}
//		//		System.out.println("aww " + Thread.currentThread());
//
//			l.screenView.updateVisuals();
//			l.gameItems.updateVisuals();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public void exitState(LevelManager l){
//
//		if(l.introFilm != null)
//			l.introFilm.clean();
//	}
//
//    public void draw(LevelManager l, Batch batch) {
//		if(l.screenView != null)
//			l.screenView.Render(g);
//		if(l.gameItems  != null)
//			l.gameItems.Render(g);
//
//		if(l.introFilm != null)
//			l.introFilm.render(g);
//	}
//
//	public void input(LevelManager l, int keyStates) {
//		if(keyStates != 0)
//			l.introFilm.current.skip();
//
//	}
//
//
//
//}
