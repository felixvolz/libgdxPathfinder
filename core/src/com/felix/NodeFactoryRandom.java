package com.felix;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
* Created by fx on 3/5/2015.
*/
public class NodeFactoryRandom {

    static final String MyActivity = NodeFactoryRandom.class.toString();

    static Random rand = new Random();

    //interface for extending enum
    public static interface TileType {


        public String getTileName();

        public TileType tileTypeFor(String tileType);

    }

    public enum Tiles implements TileType{
        openDoor,
        //closedDoor,
        floor,
        wall,
        path,
        player,
        baddy;

        Tiles(){}


        public String getTileName(){
            return this.name();
        }

        public TileType tileTypeFor(String tileType){
            for(Tiles t : Tiles.values()){
                if(t.name().equalsIgnoreCase(tileType)){
                    return t;
                }
            }
            return null;
        }


    }


    public enum Direction {
        N(0,1),
        E(1,0),
        S(0,1),
        W(-1,0);

        Vector2 vec;
        Direction(int x, int y) {
            this.vec = new Vector2(x,y);
        }


    }


    public static int randInt(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    // Returns `true` if a random int chosen between 1 and chance was 1.
    public static boolean oneIn(int chance) {
       return rand.nextInt(chance) == 0;
    }

    /// Gets a random [Vec] within the given [Rect] (half-inclusive).
    Vector2 getRandomVecInRect(Rectangle rect) {
        return new Vector2(randInt((int) rect.x, (int) (rect.x + rect.width)), randInt((int)rect.y,(int)(rect.y + rect.height)));
    }

    Array<Rectangle> rooms = new Array<Rectangle>();

    /// The index of the current region being carved.
    int currentRegion = -1;
    int windingPercent = 20;

     void generate() {

        fill(Tiles.wall);
        addRooms();

         debug_renderMaze();

        // Fill in all of the empty space with mazes.
        for (int y = 1; y < boundWidth; y += 2) {
            for (int x = 1; x < boundHeight; x += 2) {
                Vector2 pos = new Vector2(x, y);

               Tiles tile = getTile(pos);

               if (tile != Tiles.wall) {
                   continue;
               }


                 growMaze(pos);

            //    debug_renderMaze();
            }
        }


        connectRegions();

    }



    public void debug_renderMaze(){
        System.out.println("\n\n");


            for (int y = 0; y < boundHeight; y++){
             for (int x = 0; x < boundWidth; x++){

                Tiles t = getTile(new Vector2(x, y));

                if(t != null){
                    switch (t) {
                        case floor:
                            System.out.print('f');

                            break;
                        case wall:
                            System.out.print('w');

                            break;
                        case openDoor:
                            System.out.print('o');

                            break;
                    /*    case closedDoor:
                            System.out.print('c');
                            break;*/
                        case path:
                            System.out.print('P');
                            break;

                    }
                }else {
                    System.out.print('N');
                }

            }
            System.out.println();
        }

        System.out.println();
    }

    /// Implementation of the "growing tree" algorithm from here:
    /// http://www.astrolog.org/labyrnth/algrithm.htm.
    public void growMaze(final Vector2 start) {
        System.out.println("growMaze " + start.toString());

        Array<Vector2> cells = new Array<Vector2>();
        Direction lastDir = Direction.E;

        startRegion();
        carve(start, Tiles.path);



        cells.add(start);
        while (cells.size != 0) {
            final Vector2 cell = cells.get(cells.size-1);
            // See which adjacent cells are open.
            Array<Direction> unmadeCells = new Array<Direction>();

            for(Direction d : Direction.values()){
                if(canCarve(cell, d)){
                    unmadeCells.add(d);
                }


            }

      /*      for (var dir in Direction.CARDINAL) {
                if (_canCarve(cell, dir)) unmadeCells.add(dir);
            }*/

            if (unmadeCells.size > 0) {
                // Based on how "windy" passages are, try to prefer carving in the
                // same direction.
                Direction dir;

                if (unmadeCells.contains(lastDir, true) && rand.nextInt(100) > windingPercent) {
                    dir = lastDir;
                } else {

                   dir = unmadeCells.get(rand.nextInt(unmadeCells.size));
                   // dir = rng.item(unmadeCells);
                }

                Vector2 dirCopy = new Vector2(dir.vec);
                Vector2 cellCopy = new Vector2(cell);

                carve(cellCopy.add(dirCopy), Tiles.path); //plus directoin
                carve(cellCopy.add(dirCopy), Tiles.path); //plus direction * 2
                cells.add(cellCopy);//cellCopy still twice as big

               // cells.add(cell + dir * 2);
                lastDir = dir;
            } else {
                // No adjacent uncarved cells.
                //cells.remvoeLast
                cells.removeIndex(cells.size-1);

                // This path has ended.
                lastDir = null;
            }
        }
    }

    Rectangle bounds;
    /// Places rooms ignoring the existing maze corridors.
    public  void addRooms() {
        int numRoomTries = 100;
        int roomExtraSize = 0;

        //Rectangle bounds;
        bounds = new Rectangle();
        bounds.width = boundWidth;
        bounds.height = boundHeight;

        //testing
       /*  int ex = 3;
        int ey = 3;
        int w = 5;
        int h = 5;
        Rectangle one = new Rectangle(ex,ey,w,h);

        rooms.add(one);

        startRegion();
        for(int xi = ex; xi < ex+w;xi++){
            for(int yi = ey; yi < ey+h;yi++){
                carve(new Vector2(xi,yi));
            }
        }

         ex = 10;
          ey = 10;
          w = 5;
          h = 5;
         Rectangle two = new Rectangle(ex,ey,w,h);

        rooms.add(two);

        startRegion();
        for(int xi = ex; xi < ex+w;xi++){
            for(int yi = ey; yi < ey+h;yi++){
                carve(new Vector2(xi,yi));
            }
        }
      return;*/


        //end testing

      for (int i = 0; i < numRoomTries; i++) {

            // Pick a random room size. The funny math here does two things:
            // - It makes sure rooms are odd-sized to line up with maze.
            // - It avoids creating rooms that are too rectangular: too tall and
            //   narrow or too wide and flat.
            // TODO: This isn't very flexible or tunable. Do something better here.
            int size = randInt(1, boundWidth/2) ;
            int rectangularity = randInt(0, 1 +  size / 2 );// * 2;
            int width = size;
            int height = size;
            if (oneIn(2)) {
                width += rectangularity;

               width = (int)Math.min(bounds.width -1 ,width);
            } else {
                height += rectangularity;

                height = (int) Math.min(bounds.height - 1,height);
            }

         // System.out.println(size+ ", " + roomExtraSize);
        // System.out.println(bounds.width + ", " + width + ", " + bounds.height + ", " + height );

          //System.out.println((bounds.width - width) / 2);
            int x = rand.nextInt(Math.max(1,(int)(bounds.width - width) / 2)) * 2 + 1;

         // System.out.println((bounds.height - height) / 2);
            int y = rand.nextInt(Math.max(1,(int) (bounds.height - height) / 2)) * 2 + 1;

            Rectangle room = new Rectangle(x, y, width, height);

            boolean overlaps = false;
            for (Rectangle other : rooms) {
                if (room.overlaps(other)) {
                    overlaps = true;
                    break;
                }
            }

            if (overlaps) continue;

            rooms.add(room);

            startRegion();

            for(int xi = x; xi < x+width;xi++){
                for(int yi = y; yi < y+height;yi++){
                    carve(new Vector2(xi,yi));
                }
            }
        }
    }

   void connectRegions() {
       System.out.println("connect regions - start");
       // Find all of the tiles that can connect two (or more) regions.
       // var connectorRegions = <Vec, Set <int>>{};
       ArrayMap<Vector2, Array<Integer>> connectorRegions = new ArrayMap<Vector2, Array<Integer>>();


       for (int x = 0; x < boundWidth; x++) {
           for (int y = 0; y < boundHeight; y++) {
               Vector2 pos = new Vector2(x, y);

               //for (var pos in bounds.inflate(-1)) {
               // Can't already be part of a region.
               if (getTile(pos) != Tiles.wall) continue;

               //var regions = new Set<int>();
               Array<Integer> localRegions = new Array();
               for (Direction dir : Direction.values()) {
                   Vector2 vecDir = new Vector2(dir.vec);
                   vecDir.add(pos);
                   Integer region = regions.get(vecDir);
                   if (region != null) {
                       if(!localRegions.contains(region,true)){
                           localRegions.add(region);
                       }

                       }
               }

               if (localRegions.size < 2) continue;

               connectorRegions.put(pos, localRegions);
           }
       }


       System.out.println("connect regions - connectorRegions - start" + connectorRegions.size);
           Array<Vector2> connectors = connectorRegions.keys().toArray();

           // Keep track of which regions have been merged. This maps an original
           // region index to the one it has been merged to.
           ArrayList<Integer> merged = new ArrayList<Integer>();
           Array openRegions = new Array();
           for (int i = 0; i <= currentRegion; i++) {
               merged.add(i, i);

               if(!openRegions.contains(i,true)) {
                   openRegions.add(i);
               }
           }

       System.out.println("connect regions - openRegions - start " + openRegions.size);
           // Keep connecting regions until we're down to one.

       long startTime = System.currentTimeMillis(); //fetch starting time
       while (openRegions.size > 1 && (System.currentTimeMillis()-startTime)<5000) {

              // System.out.println(" - openRegions - " + openRegions.size);
               //var connector = rng.item(connectors);
               Vector2 connector = connectors.get(rand.nextInt(connectors.size));
               // Carve the connection.
               addJunction(connector);

             //  System.out.println(" - openRegions - connector " + connector);
               // Merge the connected regions. We'll pick one region (arbitrarily) and
               // map all of the other regions to its index.
               Array<Integer> regions = connectorRegions.get(connector);

               for(int i = 0; i < regions.size; i++){
                   merged.add(regions.get(i),regions.get(i));
               }

               //.map((region) => merged[region]);
               //Integer dest = regions.first();
               if(regions == null || regions.size == 0){
                   System.out.print("test " +  connector);
                   continue;
               }
               Integer dest = regions.get(0);
               //Array<Integer> sources = regions.skip(1).toList();

               Array<Integer> sources = new Array<Integer>( Arrays.copyOfRange(regions.items,1,regions.size));
              // Array<Integer> sources = regions.g;

               // Merge all of the affected regions. We have to look at *all* of the
               // regions because other regions may have previously been merged with
               // some of the ones we're merging now.
               for (int i = 0; i <= currentRegion; i++) {
                   if (sources.contains(merged.get(i), true)) {
                       //if (sources.contains(merged[i])) {
                       //merged[i] = dest;
                       merged.set(i, dest);
                   }
               }

               // The sources are no longer in use.

               openRegions.removeAll(sources, true);

               // Remove any connectors that aren't needed anymore.
           /* connectors.removeWhere((pos) {
                    // Don't allow connectors right next to each other.
            if (connector - pos < 2) return true;

            // If the connector no long spans different regions, we don't need it.
            var regions = connectorRegions[pos].map((region) => merged[region])
            .toSet();

            if (regions.length > 1) return false;

            // This connecter isn't needed, but connect it occasionally so that the
            // dungeon isn't singly-connected.
            if (rng.oneIn(extraConnectorChance)) _addJunction(pos);

            return true;
            });*/
           }
       }


    void addJunction(Vector2 pos) {
        if (oneIn(4)) {
            setTile(pos, oneIn(3) ? Tiles.openDoor : Tiles.floor);
        } else {
            setTile(pos, Tiles.openDoor);
        }
    }

    /// Gets whether or not an opening can be carved from the given starting
    /// [Cell] at [pos] to the adjacent Cell facing [direction]. Returns `true`
    /// if the starting Cell is in bounds and the destination Cell is filled
    /// (or out of bounds).</returns>
    boolean canCarve(final Vector2 pos, Direction direction) {
        // Must end in bounds.
        //if (!bounds.contains(pos + direction * 3)) return false;

        Vector2 step3 = new Vector2(direction.vec).scl(3).add(pos);
        Vector2 step2 = new  Vector2(direction.vec).scl(2).add(pos);
        if (!bounds.contains(step3)) return false;

        Tiles t = getTile(step2);
        // Destination must not be open.
        //return getTile(pos + direction * 2) == Tiles.wall;
        return getTile(step2) == Tiles.wall;
    }

    void startRegion() {
        currentRegion++;
    }

    ArrayMap<Vector2,Integer> regions = new ArrayMap<Vector2,Integer>();
    ArrayMap<Vector2,Tiles> tiles = new ArrayMap<Vector2,Tiles>();

    void carve(Vector2 pos) {
       carve(pos,null);
    }

    void carve(Vector2 pos, Tiles tiles) {
        // int type = Tiles.floor;

        if(tiles == null){tiles = Tiles.floor;}
        setTile(pos, tiles);
        regions.put(pos,currentRegion);
    }

    void setTile(Vector2 pos, Tiles type) {
        tiles.put(new Vector2(pos),type);
    }

    Tiles getTile(Vector2 pos){
        return tiles.get(pos);
    }

    public int boundWidth;
    public int boundHeight;


    public NodeFactoryRandom(int boundWidth, int boundHeight){
        this.boundWidth = boundWidth;
        this.boundHeight = boundHeight;
    }

    public void fill(Tiles t){
        for (int x = 0; x < boundWidth; x++) {
            for (int y = 0; y < boundHeight; y++) {

                Vector2 pos = new Vector2(x, y);
                setTile(pos, t);
            }
        }
    }


    void removeDeadEnds() {
        boolean done = false;

        while (!done) {
            done = true;


            for (int x = 0; x < boundWidth; x++) {
                for (int y = 0; y < boundHeight; y++) {

                    Vector2 pos = new Vector2(x, y);
                    // for (var pos in bounds.inflate(-1)) {
                    if (getTile(pos) == Tiles.wall) continue;

                    // If it only has one exit, it's a dead end.
                    int exits = 0;
                    for (Direction dir : Direction.values()) {
                        Vector2 newDir = new Vector2(pos);
                        newDir.add(dir.vec);

                        if (getTile(newDir) != Tiles.wall) exits++;
                    }

                    if (exits != 1) continue;

                    done = false;
                    setTile(pos, Tiles.wall);
                }
            }
        }
    }


    /**
     * use to add he four neighboring edges of a graph node that
     * is positioned in a grid layout
     */
/*    public static void GraphHelper_AddAllNeighboursToGridNode(Array<Node> array, int row,
                                                              int col,
                                                              int NumCellsX,
                                                              int NumCellsY) {

        DefaultConnection<Node> connection = null;
        Array<Connection<Node>> connectionArray = new Array<Connection<Node>>();
        TiledMapTileLayer.Cell cell = null;

        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                int nodeX = col + j;
                int nodeY = row + i;

                //skip if equal to this node
                if ((i == 0) && (j == 0)) {
                    continue;
                }

                //skip if a diagonal
                if((Math.abs(i) == 1) && (Math.abs(j) == 1)){
                    continue;
                }

                //check to see if this is a valid neighbour
                if (ValidNeighbour(nodeX, nodeY, NumCellsX, NumCellsY)) {

                    //cell will be null if non-navigable
                   // cell = tiledLayer.getCell(nodeX,nodeY);



                  //  if(cell != null) {

                        Node node = array.get(row * NumCellsX + col);
                        Node neighbour = array.get(nodeY * NumCellsX + nodeX);

                        connection = new DefaultConnection<Node>(node, neighbour);
                        connectionArray.add(connection);
                   // }
                }
            }
        }
        Node node = array.get(row * NumCellsX + col);
        node.setConnections(connectionArray);

    }*/


    /**
     * @return true if x,y is a valid position in the map
     */
   /* public static boolean ValidNeighbour(int x, int y, int NumCellsX, int NumCellsY) {
        return !((x < 0) || (x >= NumCellsX) || (y < 0) || (y >= NumCellsY));
    }*/

    public static void main(String arg[]){

        int boundWidth = 20;
        int boundHeight = 20;

        NodeFactoryRandom s = new NodeFactoryRandom(boundWidth, boundHeight);
        s.generate();
        s.removeDeadEnds();

        for (int x = 0; x < s.boundWidth; x++){

            for (int y = 0; y < s.boundHeight; y ++){
                boolean intersect = false;
                int i = 0;

                for(; i < s.rooms.size; i++){
                    Rectangle r = s.rooms.get(i);
                    if(r.contains(new Vector2(x,y))){
                       intersect = true;
                        break;
                    }
                }

                if(intersect){
                    System.out.print(i);
                }else{
                    System.out.print("o");
                }


            }
            System.out.println();
        }



        //tiles - ArrayMap<Vector2,Tiles> tiles = new ArrayMap<Vector2,Tiles>();
        for (int x = 0; x < s.boundWidth; x++){

            for (int y = 0; y < s.boundHeight; y ++){
               Tiles t = s.getTile(new Vector2(x,y));

               if(t != null){
                    switch (t) {
                        case floor:
                            System.out.print('X');
                            break;
                        case wall:
                            System.out.print('|');
                            break;
                        case openDoor:
                            System.out.print('o');
                            break;
//                        case closedDoor:
//                            System.out.print('c');
//                            break;
                        case path:
                            System.out.print('-');
                            break;

                    }
                }else {System.out.print('N');}

            }
            System.out.println();
        }


    }
}
