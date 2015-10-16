package com.felix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.felix.MyActor;
import com.felix.NavGraph;
import com.felix.NavGraphActor;
import com.felix.NodeFactory;
import com.felix.PathManager;

/*
TODO 14 March - 5 tasks
DONE 14 March - PathManager.newSearch fix: Fix random directional defect and unit test
DONE 5 March - l,r,u,d, search, lose single frame working correctly

TODO Smooth out the rendering and the sleep thing Gdx.graphics.getDeltaTime()
TODO Directional animation (up, down, left, right etc)
    - Implement Drawable (hold Sprite and array of animations)
    - Implement ImageFactory to load Drawable
    - Implement ImageFactoryTest
    - implement Animator
TODO Brain State - use in combo with directioanl aniation
    - l,r,u,d, search, lose multi frame animation
    - search, lose animations trigger back to back - can be mis leading
    -

TODO fix how to centre the Map and Camera and Zoom to nice level of detail
TODO build debug log class that captures useful debug log info and displays - turn on/off dynamicall
  --add debug table tto each actor t show its relevant ifno (eg. stateof mind attack)
  --MyActor debug - draw path
TODO tumblr account - list game
     Done -  make animated gif of gameplay
TODO make HTML5 app of game
    - March 16/17 having grief twwith
TODO reveiw the TiledApp..Sleep functionality and use Gdx.graphics.getDeltaTime()
*/
public class TiledApp_old extends ApplicationAdapter    {
    Texture img;
    TiledMap tiledMap;
    //OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    CameraController controller;
    Stage stage;
    NavGraph navGraph;
    PathManager pathManager = null;

    Label label;
    String labelText = "Debug Log:\nNew Line:";

    boolean debug = false;
    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        stage = new Stage();
        /*camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();*/
        try {
            TmxMapLoader.Parameters t = new TmxMapLoader.Parameters();
            t.convertObjectToTileSpace = true;


            tiledMap = new TmxMapLoader().load("data/test_tiled.tmx");
        }catch (Exception e){
            //hack to run DesktopLauncher from Android Studio

            TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
            parameters.generateMipMaps = true;

            tiledMap = new TmxMapLoader().load("C:/Users/fx/Downloads/libGDX - pathfinder/android/assets/data/test_tiled.tmx",parameters );

        }
        navGraph = NodeFactory.createGraph(tiledMap);
        NavGraphActor navgraphActor = new NavGraphActor(200,200,navGraph);
        MyActor playerAgent = NodeFactory.getPlayerAgent(tiledMap);
        MyActor baddyAgent = NodeFactory.getBaddyAgent(tiledMap);
        navgraphActor.addPlayer(playerAgent);
        navgraphActor.addActor(baddyAgent);

        pathManager = new PathManager(navGraph,baddyAgent,playerAgent);


        stage.addActor(navgraphActor);
        stage.setDebugAll(debug);

        ((OrthographicCamera) stage.getCamera()).zoom = 0.8f;// 0.2f;

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
        controller = new CameraController();

        //Gdx.input.setInputProcessor( new GestureDetector(20, 0.5f, 2, 0.15f, controller));
        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(20, 0.5f, 2, 0.15f, controller);
        im.addProcessor(gd);
        im.addProcessor(stage);

        Gdx.input.setInputProcessor(im);
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        ((OrthographicCamera) stage.getCamera()).position.set(new Vector2(200,100), 0);
        //table
//        Table table = new Table();
//        stage.addActor(table);
//        table.setPosition(200, 0);

        Skin skin = null;
        try {
            skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        }catch (Exception e){
            Gdx.app.log(e.toString(),e.getMessage());
           skin = new Skin(Gdx.files.internal("C:/Users/fx/Downloads/libGDX - pathfinder/android/assets/data/uiskin.json"));
        }
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
       // skin.getFont("default-font").setMarkupEnabled(true);

//        table.debug();
//        label = new Label(labelText, skin);
//        table.add(label);
//        table.row();


 //       table.pack();
    }



    int fps = 15;

    private long diff, start = System.currentTimeMillis();

    public void sleep(int fps) {
        if(fps>0){
            diff = System.currentTimeMillis() - start;
            long targetDelay = 1000/fps;
            if (diff < targetDelay) {
                try{
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {}
            }
            start = System.currentTimeMillis();
        }
    }
    byte tickCount = 0;
    byte vehicleUpdateDelay = 4;
    public void render () {
        sleep(fps);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controller.update();
       // camera.update();

      //  int index = (int) (System.currentTimeMillis() % 5);
      //  tiledMap.getLayers().get(index).setVisible(!tiledMap.getLayers().get(index).isVisible());


        tiledMapRenderer.setView((OrthographicCamera)stage.getCamera());
       tiledMapRenderer.render();
        if (tickCount++ % vehicleUpdateDelay == 0) {
            pathManager.tick();
            stage.act(Gdx.graphics.getDeltaTime());

        }


        stage.draw();
        //debugging only
        //navGraph.render(camera);
        if(debug)
         navGraph.render((OrthographicCamera) stage.getCamera());
    }

    class CameraController implements GestureListener {
        float velX, velY;
        boolean flinging = false;
        float initialScale = 1;

        public boolean touchDown (float x, float y, int pointer, int button) {
            flinging = false;
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

            initialScale = camera.zoom;
            return false;
        }

        @Override
        public boolean tap (float x, float y, int count, int button) {
            Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);


            return false;
        }
        @Override
        public boolean longPress (float x, float y) {
            Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
            flinging = true;
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
            velX = camera.zoom * velocityX * 0.5f;
            velY = camera.zoom * velocityY * 0.5f;
            return false;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            // Gdx.app.log("GestureDetectorTest", "pan at " + x + ", " + y);
            //camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
            camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
            return false;
        }

        @Override
        public boolean panStop (float x, float y, int pointer, int button) {
            Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
            return false;
        }

        @Override
        public boolean zoom (float originalDistance, float currentDistance) {
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
            float ratio = originalDistance / currentDistance;
            camera.zoom = initialScale * ratio;

            labelText = "Zoom : " + camera.zoom;
            label.setText(labelText);
            return false;
        }

        @Override
        public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
            return false;
        }

        public void update () {
            if (flinging) {
                OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
                velX *= 0.98f;
                velY *= 0.98f;
                camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
                if (Math.abs(velX) < 0.01f) velX = 0;
                if (Math.abs(velY) < 0.01f) velY = 0;
            }
        }
    }
/*    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
      return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchDown (" + screenX + ", " + screenY + ")" );
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp (" + screenX + ", " + screenY + ")" );
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        System.out.println("tap (" + x + ", " + y + ")" );
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        System.out.println("longPress (" + x + ", " + y + ")" );
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {

        System.out.println("zoom (" + initialDistance + ", " + distance + ")" );
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        System.out.println("pinch (" + initialPointer1 + ", " + initialPointer2 + ") (" + pointer1 + ", " + pointer2 + ")" );
        return false;
    }*/
}