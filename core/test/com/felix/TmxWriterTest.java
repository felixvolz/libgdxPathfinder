package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;
import tiled.io.TMXMapWriter;
import tiled.util.BasicTileCutter;

import static org.mockito.Mockito.mock;

public class TmxWriterTest extends TestCase {



    public enum BackgroundTiles implements NodeFactoryRandom.TileType{
        //tiles for sheet_6.png
        path(45),
        wall(7),
        openDoor(31),
        floor(44);

        int[] ids;
        BackgroundTiles(int ...id) {
            ids =  id;
        }

        int getRandomTileId(){
            return this.ids[randInt(0, this.ids.length-1)];
        }

        public String getTileName(){
            return this.name();
        }

        public NodeFactoryRandom.TileType tileTypeFor(String tileType){
            for(BackgroundTiles t : BackgroundTiles.values()){
                if(t.name().equalsIgnoreCase(tileType)){
                    return t;
                }
            }
            return null;
        }



    }

    static Random rand = new Random();

    public static int randInt(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    String littleLevelTmxFile = "data/little_level.tmx";
    String bigLevelTmxFile = "data/little_level.tmx";

    public void testTMXMapReader_little_level() throws Exception{
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
        //Gdx.graphics = mock(Graphics.class);

        // TiledMap t = NodeFactory.getTiledMap(littleLevelTmxFile);

        TMXMapReader mapReader = new TMXMapReader();
       Map map = mapReader.readMap("C:\\Users\\fx\\Downloads\\libGDX - pathfinder\\android\\assets\\data\\little_level.tmx");
        assertNotNull(map);
    }

    public void testTMXMapWriter() throws Exception{
        int tileWidth = 20,  tileHeight = 20, tileSpacing = 0, tileMargin = 0;
        int width = 40, height = 40;



        Map map = new Map(width, height);

        //backgroup
        TileLayer background =  createTileLayer("background", width, height);
        TileLayer collision = createTileLayer("collision", width, height);

        //sprite layer
        TileLayer sprite = createTileLayer("sprite",width, height);
        Properties playerProperties = new Properties();
        playerProperties.setProperty("agent","player");

        Properties baddyProperties = new Properties();
        baddyProperties.setProperty("agent","baddy");

        String baseDir = "C:/Users/fx/Downloads/libGDX - pathfinder/android/assets/data/";
        String backgroundTilesImage= baseDir + "sheet_6.png";

        String playerImage = baseDir + "gotenku.png";
        String baddyImage = baseDir + "p2_walk.png";

        TileSet playerTileSet = createTileSet(playerImage,20,30,tileSpacing, tileMargin, playerProperties);

        TileSet baddyTileSet = createTileSet(baddyImage,70,94,tileSpacing, tileMargin, baddyProperties);




        //map.setLayer(0,background);

        TileSet backgroundTileSet = createTileSet(backgroundTilesImage,tileWidth,tileHeight,tileSpacing, tileMargin, null);


        NodeFactoryRandom nodeFactoryRandom = new NodeFactoryRandom(width,height);
        nodeFactoryRandom.generate();
        nodeFactoryRandom.debug_renderMaze();

        for (int x = 0; x < nodeFactoryRandom.boundWidth; x++){
            for (int y = 0; y < nodeFactoryRandom.boundHeight; y ++){
                NodeFactoryRandom.Tiles t = nodeFactoryRandom.getTile(new Vector2(x,y));

                //convert from tile to tile
                BackgroundTiles bTile = null;
                for(BackgroundTiles tile : BackgroundTiles.values()){
                    if(tile.name().equalsIgnoreCase(t.getTileName())){
                        bTile = tile;
                        break;
                    }
                }


                Tile setTile = backgroundTileSet.getTile(bTile.getRandomTileId());

                switch(t){
                    case wall: collision.setTileAt(x,y,setTile);break;
                    default: background.setTileAt(x,y,setTile);break; //path, floor, door
                }

               // background.setTileAt(x,y,setTile);

            }
           // System.out.println();
        }

        //player placement
        Rectangle playerRoom = nodeFactoryRandom.rooms.get(0);
        Rectangle baddyRoom = nodeFactoryRandom.rooms.get(nodeFactoryRandom.rooms.size-1);

        sprite.setTileAt((int)playerRoom.x,(int)playerRoom.y,playerTileSet.getFirstTile());
        sprite.setTileAt((int)baddyRoom.x,(int)baddyRoom.y,baddyTileSet.getFirstTile());
        //end player placement

    /*    for(int x = 0; x < width; x++){
            for (int y = 0; y < height ; y++){
                //Tile tile = new Tile(backgroundTileSet);
                 Tile t = backgroundTileSet.getTile(46);
                 background.setTileAt(x,y,t);
            }
        }*/


        assertNotNull(backgroundTileSet);


        map.addLayer(background);
        map.addLayer(collision);
        map.addLayer(sprite);
        map.addTileset(backgroundTileSet);
        map.addTileset(playerTileSet);
        map.addTileset(baddyTileSet);

        //set by TMXMapWriter
       // Properties p = new Properties();
        //p.setProperty("tilewidth",Integer.toString(tileWidth) );
       // p.setProperty("tileheight", Integer.toString(tileHeight));
        //map.setProperties(p);

        TMXMapWriter writer = new TMXMapWriter();

        writer.writeMap(map,baseDir + "TMXMapWriterTest.tmx");
       // writer.writeMap(map,baseDir + "TMXMapWriterTest.tmx");
    }

    public TileLayer createTileLayer(String name, int width, int height){

        TileLayer layer = new TileLayer(width, height);
        layer.setName(name);

        return layer;
    }

    public Tile createTile(){
       Tile tile = new Tile();


        return tile;
    }

    public TileSet createTileSet(String imgFilename, int tileWidth, int tileHeight, int tileSpacing,
                                 int tileMargin, Properties properties) throws IOException {
        TileSet tileSet = new TileSet();
        tileSet.setName(imgFilename);
        tileSet.importTileBitmap(imgFilename,new BasicTileCutter(tileWidth, tileHeight, tileSpacing, tileMargin));

        if(properties != null){
            tileSet.getFirstTile().setProperties(properties);
        }

        return tileSet;
    }


}

