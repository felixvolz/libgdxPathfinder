//package com.game;
//
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//
//public class LevelRunState implements LevelState {
//
//	public LevelRunState() {
//
//	}
//
//	public void enterState(LevelManager l){
////		System.out.println("entered run level");
//
//		//SimpleGameCanvas.instance.resume();
//		GameItemManager.goalAchieved = false;
//		System.out.println("Entering runLevel enterstate " + Thread.currentThread());
//	}
//
//	public void runState(LevelManager l) {
//
//		if (SimpleGameCanvas.instance.active)
//			if (l.gameItems != null)
//				if (!GameItemManager.goalAchieved) {
//
//					l.screenView.tick();
//					l.gameItems.tick();
//
//					SimpleGameCanvas.instance.tick();
//				} else {
//					l.currentLevel++;
//					l.nextState(l.loadLevel);
//				}
//	}
//
//	public void exitState(LevelManager l){
//		System.out.println("runLevel exiting");
//		//l.nextLevel();
//	}
//
//	public void draw(LevelManager l, Batch batch){
//		Font font = Font.getDefaultFont();
//
//
//
//		if(l.screenView != null)
//			l.screenView.Render(g);
//		if(l.gameItems  != null)
//			l.gameItems.Render(g);
//
//		g.setColor(0x000000);
//
//		l.string.delete(0,10);
//		l.string.append(Runtime.getRuntime().totalMemory());
//	//	string.append("Level " + currentLevel);
//		g.drawString(l.string.toString(), l.topLeft.x,l.topLeft.y,Graphics.TOP|Graphics.LEFT);
//
//		l.string.delete(0,10);
//		l.string.append(Runtime.getRuntime().freeMemory());
//		//string.append("Score: " + Player.score);
//		g.drawString(l.string.toString(), l.topRight.x - font.stringWidth(l.string.toString()) ,l.topRight.y,Graphics.TOP|Graphics.LEFT);
////
//	}
//
//	public void input(LevelManager l, int keyStates) {
//		if ((keyStates & GameCanvas.LEFT_PRESSED) != 0) {
//			GameItemManager.player.moveLeft();
//
//		}
//
//		if ((keyStates & GameCanvas.RIGHT_PRESSED) != 0) {
//			GameItemManager.player.moveRight();
//
//		}
//
//		if ((keyStates & GameCanvas.UP_PRESSED) != 0) {
//			GameItemManager.player.moveUp();
//
//
//		}
//
//		if ((keyStates & GameCanvas.DOWN_PRESSED) != 0) {
//
//
//			//current.setGoal(new Point(0,7));
//			GameItemManager.player.moveDown();
//		}
//
//
//		if ((keyStates & GameCanvas.FIRE_PRESSED) != 0) {
//
//			l.gameItems.goalAchieved = true;
//		}
//
////		 short index = LevelManager.graph.depthSearch.searchSpace(LevelManager.gameItems.player.position, GameItem.baddie);
////		Point p = null;
////		 if(index != -1 )
////				p = LevelManager.graph.positionBasedOnIndex(index);
////		 System.out.println(p + " is te nearest baddie");
//	}
//
//}
