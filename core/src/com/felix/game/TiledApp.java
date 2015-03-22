package com.felix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.felix.MyActor;
import com.felix.NavGraph;
import com.felix.NavGraphActor;
import com.felix.NodeFactory;
import com.felix.PathManager;
import com.felix.component.CameraHelper;


/*
TODO 14 March - 5 tasks
DONE 14 March - PathManager.newSearch fix: Fix random directional defect and unit test
DONE 15 March - l,r,u,d, search, lose single frame working correctl
March 18
MyActor has component Animator - shouldnt care about its implementation
Animator built by ImageFactory where possible
Default AnimatoinSequence LRUD created
Rewrote gotenku,baddy png to be read from top to bottom

March 19 - played around with Camera Helper - dont really have a good grasp on where I'm heading with this
  - heavier solution in SmoothedCamera - havent used
  - like the idea of a 'smooth' bounding box that causes my camera to shift
  - need to have 'smooth' camera that operates seperately from my clunky 'step' updates


TODO Smooth out the rendering and the sleep thing Gdx.graphics.getDeltaTime()
Done Directional animation (up, down, left, right etc)
    - Implement ImageFactory to load Animator
    - Implement ImageFactoryTest
    - implement Animator (hold Sprite and array of animations)
TODO Brain State - use in combo with directioanl aniation
    - l,r,u,d, search, lose multi frame animation
    - search, lose animations trigger back to back - can be mis leading
    -

TODO fix how to centre the Map and Camera and Zoom to nice level of detail
TODO build debug log class that captures useful debug log info and displays - turn on/off dynamicall
  --add debug table tto each actor t show its relevant ifno (eg. stateof mind attack)
  --MyActor debug - draw path
DONE tumblr account - list game
     Done -  make animated gif of gameplay
TODO make HTML5 app of game
    - March 16/17 having grief twwith
TODO reveiw the TiledApp..Sleep functionality and use Gdx.graphics.getDeltaTime()

TODO review LevelState stuff - is it worth implementing?
    - I thiknk I need to look at how levels progress in Androi (via Screens paerhaps)
TODO - find hardcoded numbers and tidyup

TODO - GitHub
TODO - ViewPort
*/

public class TiledApp extends ApplicationAdapter    {
    private static final String TAG = TiledApp.class.getName();
    Texture img;
    TiledMap tiledMap;
    //OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    CameraController controller;
    Stage stage;
    NavGraph navGraph;
    public static PathManager pathManager = null;

    public static CameraHelper cameraHelper;

//    Label label;
//    String labelText = "Debug Log:\nNew Line:";

    boolean debug = true;
    Array<Viewport> viewports;
    Array<String> names;


    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();




        stage = new Stage();
         TmxMapLoader.Parameters t = new TmxMapLoader.Parameters();
         tiledMap = new TmxMapLoader().load("data/little_level.tmx");

        navGraph = NodeFactory.createGraph(tiledMap);
        navGraph.setDebug(debug);
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
        im.addProcessor(controller);
        im.addProcessor(stage);

        viewports = getViewports(stage.getCamera());
        names = getViewportNames();

        stage.setViewport(viewports.first());

        Gdx.input.setInputProcessor(im);
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        ((OrthographicCamera) stage.getCamera()).position.set(new Vector2(200,100), 0);

//        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
//            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
//
//
//                //tiled
////                int index = (viewports.indexOf(stage.getViewport(), true) + 1) % viewports.size;
////                Gdx.app.log(TAG, names.get(index));
////                Viewport viewport = viewports.get(index);
////                stage.setViewport(viewport);
////                resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//                return false;
//            }
//
//        }, stage));

        cameraHelper = new CameraHelper();

        //table
//        Table table = new Table();
//        stage.addActor(table);
//        table.setPosition(200, 0);

//        Skin skin = null;
//        try {
//            skin = new Skin(Gdx.files.internal("data/uiskin.json"));
//        }catch (Exception e){
//            Gdx.app.log(e.toString(),e.getMessage());
//           skin = new Skin(Gdx.files.internal("C:/Users/fx/Downloads/libGDX - pathfinder/android/assets/data/uiskin.json"));
//        }
//        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
//        skin.getFont("default-font").setMarkupEnabled(true);

//        table.debug();
//        label = new Label(labelText, skin);
//        table.add(label);
//        table.row();


 //       table.pack();


    }



    int fps = 4;

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

    public boolean act(){
        diff = System.currentTimeMillis() - start;
        long targetDelay = 1000/fps;

        if (diff < targetDelay) {
            return false;
        }

        start = System.currentTimeMillis();
        return true;
    }

    byte tickCount = 0;
    byte vehicleUpdateDelay = 4;
    public void render () {

        //TODO restrict framerate but smooth camera
//       /sleep(fps);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controller.update();
       // camera.update();

      //  int index = (int) (System.currentTimeMillis() % 5);
      //  tiledMap.getLayers().get(index).setVisible(!tiledMap.getLayers().get(index).isVisible());


       tiledMapRenderer.setView((OrthographicCamera)stage.getCamera());

        //draw layers UNDER our players
        ((BatchTiledMapRenderer)tiledMapRenderer).render(new int[]{0,1});
       // if (tickCount++ % vehicleUpdateDelay == 0) {
        if (act()){
            pathManager.tick(); //TODO turned off baddy while figuring out camera smoothing
            stage.act(Gdx.graphics.getDeltaTime());


        }

        if(debug) {
            navGraph.render((OrthographicCamera) stage.getCamera());
            cameraHelper.applyTo((OrthographicCamera) stage.getCamera());
             cameraHelper.update(Gdx.graphics.getDeltaTime());
        }
        stage.draw();

        //draw layers OVER our players (e.g. tree tops/clouds)
        ((BatchTiledMapRenderer)tiledMapRenderer).render(new int[]{2,3,4});
        //debugging only
        //navGraph.render(camera);


    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
    }

    static public Array<String> getViewportNames () {
        Array<String> names = new Array();
        names.add("StretchViewport");
        names.add("FillViewport");
        names.add("FitViewport");
        names.add("ExtendViewport: no max");
        names.add("ExtendViewport: max");
        names.add("ScreenViewport: 1:1");
        names.add("ScreenViewport: 0.75:1");
        names.add("ScalingViewport: none");
        return names;
    }

    static public Array<Viewport> getViewports (Camera camera) {
        int minWorldWidth = 640;
        int minWorldHeight = 480;
        int maxWorldWidth = 800;
        int maxWorldHeight = 480;

        Array<Viewport> viewports = new Array();
        viewports.add(new StretchViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new FillViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new FitViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
        viewports.add(new ScreenViewport(camera));

        ScreenViewport screenViewport = new ScreenViewport(camera);
        screenViewport.setUnitsPerPixel(0.75f);
        viewports.add(screenViewport);

        viewports.add(new ScalingViewport(Scaling.none, minWorldWidth, minWorldHeight, camera));
        return viewports;
    }

    class CameraController implements GestureListener,InputProcessor {
        private final String TAG = CameraController.class.getName();
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
            Gdx.app.log(TAG, "tap at " + x + ", " + y + ", count: " + count);


            return false;
        }
        @Override
        public boolean longPress (float x, float y) {
            Gdx.app.log(TAG, "long press at " + x + ", " + y);
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            Gdx.app.log(TAG, "fling " + velocityX + ", " + velocityY);
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
            Gdx.app.log(TAG, "pan stop at " + x + ", " + y);
            return false;
        }

        @Override
        public boolean zoom (float originalDistance, float currentDistance) {
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
            float ratio = originalDistance / currentDistance;
            camera.zoom = initialScale * ratio;

            cameraHelper.setZoom(camera.zoom);
//            labelText = "Zoom : " + camera.zoom;
//            label.setText(labelText);
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

        @Override
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
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

        float zoom;

        @Override
        public boolean scrolled(int amount) {
            //Zoom out
            if (amount > 0 && zoom < 1) {
                zoom += 0.1f;
            }

            //Zoom in
            if (amount < 0 && zoom > 0.1) {
                zoom -= 0.1f;
            }

            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

            camera.zoom = zoom;

            cameraHelper.setZoom(camera.zoom);
//            labelText = "Zoom : " + camera.zoom;
//            label.setText(labelText);
            return false;
        }
    }

}