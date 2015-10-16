package com.felix.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.felix.game.TiledApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
 		 new LwjglApplication(new TiledApp(), config);
//        /new LwjglApplication(new RandomTiledGeneratorApp(), config);

    }
}
