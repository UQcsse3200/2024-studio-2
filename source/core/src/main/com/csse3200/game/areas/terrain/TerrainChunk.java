package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainResource;
import com.csse3200.game.areas.terrain.TerrainComponent.Tile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

import java.util.HashMap;
import java.util.Map;
import java.util.BitSet;
import java.util.Arrays;

public class TerrainChunk {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private GridPoint2 position;
  private TiledMap tiledMap;
  private TiledMapTileLayer layer;
  private boolean loaded = false;

  //private TextureRegion grass, grassTuft, rocks;
  public Array<Tile> tiles;
  public Array<BitSet> grid;

  TerrainChunk(GridPoint2 position, TiledMap map) {
    this.position = position;
    this.tiledMap = map;
    this.layer = (TiledMapTileLayer) map.getLayers().get(TERRAIN_LAYER);
    //this.tiles = new Array<Tile>(256);   
    //for (int i = 0; i < 256; ++i) {
    //  this.tiles.add(null);
    //}
    //BitSet[] in = new BitSet[256];
    this.grid = new Array<BitSet>(256);
    for (int i = 0; i < 256; ++i) {
      BitSet bitset = new BitSet(TerrainResource.TILE_SIZE);
      bitset.set(0, TerrainResource.TILE_SIZE, true);
      this.grid.add(bitset);
      //in[i] = new BitSet(TerrainResource.TILE_SIZE);
      //in[i].set(0, TerrainResource.TILE_SIZE, true);
    }

    //System.out.println("this printed----------------------------------------");
    for (int i = 0; i < 256; ++i) {
      // look up right down left
      BitSet up = new BitSet(TerrainResource.TILE_SIZE);
      BitSet down = new BitSet(TerrainResource.TILE_SIZE);
      BitSet left = new BitSet(TerrainResource.TILE_SIZE);
      BitSet right = new BitSet(TerrainResource.TILE_SIZE);

      up.set(0, TerrainResource.TILE_SIZE, true);
      down.set(0, TerrainResource.TILE_SIZE, true);
      left.set(0, TerrainResource.TILE_SIZE, true);
      right.set(0, TerrainResource.TILE_SIZE, true);

      CCell upcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x, position.y + 1);
      if (upcell != null)
        up = upcell.getDown();

      CCell downcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x, position.y - 1);
      if (downcell != null)
        down = downcell.getUp();

      CCell leftcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x - 1, position.y);
      if (leftcell != null)
        left = leftcell.getRight();

      CCell rightcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x + 1, position.y);
      if (rightcell != null)
        right = rightcell.getLeft();

      System.out.print(upcell + " " + downcell + " " + leftcell + " " + rightcell + " " + grid.get(i) + "\n");
      //BitSet gridCell = new BitSet(256);
      //gridCell.set(0, 256, true);
      //grid.set(i, analyseTile(up, down, left, right, gridCell));
      
    //System.out.println("error from here----------------------------------------");
    System.out.println(up.toString());
      

      grid.set(i, analyseTile(up, down, left, right, grid.get(i)));
    }
  }

  public void generateTiles(GridPoint2 chunkPos, Map<GridPoint2, TerrainChunk> loadedChunks,
      TerrainResource terrainResource) {
    //System.out.println("Generating tiles for chunk at " + chunkPos);
    int cPosX = chunkPos.x * CHUNK_SIZE;
    int cPosY = chunkPos.y * CHUNK_SIZE;

    //for (int x = chunkPos.x * CHUNK_SIZE; x < (chunkPos.x + 1) * CHUNK_SIZE; x++)
    //  for (int y = chunkPos.y * CHUNK_SIZE; y < (chunkPos.y + 1) * CHUNK_SIZE; y++) {
    //    CCell cell = new CCell();
    //    //cell.setTile(terrainResource.random_pick(), terrainResource);
    //    cell.setTile(terrainResource.getGrassTL(), terrainResource);
    //    ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(x, y, cell);
    //}
    
    System.out.println("-----------------");
    System.out.println(grid.get(0).cardinality());
    System.out.println(grid.toString());


    System.out.println("oops here----------------------------------------");
    for (int t = 0; t < 256; ++t) {

    System.out.println("-----------------");
      int minentropy = Integer.MAX_VALUE;
      for (int i = 0; i < grid.size; ++i) {
        int entropy = grid.get(i).cardinality();
        if (entropy < minentropy && entropy > 0) {
          minentropy = entropy;
        }
      }

      // get all minentropy tiles to an array and random pick one of them
      Array<Integer> minentropyTiles = new Array<Integer>();
      for (int i = 0; i < grid.size; ++i) {
        if (grid.get(i).cardinality() == minentropy) {
          minentropyTiles.add(i);
        }
      }

      //System.out.println(minentropyTiles.toString());
      int randomTile = minentropyTiles.random();

      //System.out.println("Random tile: " + randomTile);
      //System.out.println("random tile bitset: " + grid.get(randomTile).toString());

      GridPoint2 toGridpos = new GridPoint2(randomTile % 16, randomTile / 16);

      // ranodm pick a tile
      int numTrueBits = grid.get(randomTile).cardinality();
      int randomTrueBitIndex = -1;
      if (numTrueBits > 0) {
        int randomIndex = (int) (Math.random() * numTrueBits);
        randomTrueBitIndex = grid.get(randomTile).nextSetBit(0);
        for (int i = 0; i < randomIndex; i++)
            randomTrueBitIndex = grid.get(randomTile).nextSetBit(randomTrueBitIndex + 1);
        //System.out.println("Random true bit index: " + randomTrueBitIndex);
      }
      //System.out.println(grid.get(randomTile).get(randomTrueBitIndex));

      // clear all bit of the picked cell as filled tile 
      grid.get(randomTile).clear(); // collapsed

      //System.out.println(grid.toString());

      CCell cell = new CCell();
      cell.setTile(terrainResource.getTilebyIndex(randomTrueBitIndex), terrainResource);
      ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(cPosX + toGridpos.x, cPosY + toGridpos.y, cell);

      //// update grid
      for (int i = 0; i < grid.size; ++i) {
        // look up right down left
        BitSet up = new BitSet(TerrainResource.TILE_SIZE);
        BitSet down = new BitSet(TerrainResource.TILE_SIZE);
        BitSet left = new BitSet(TerrainResource.TILE_SIZE);
        BitSet right = new BitSet(TerrainResource.TILE_SIZE);
        up.set(0, TerrainResource.TILE_SIZE, true);
        down.set(0, TerrainResource.TILE_SIZE, true);
        left.set(0, TerrainResource.TILE_SIZE, true);
        right.set(0, TerrainResource.TILE_SIZE, true);



        CCell upcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x, position.y + 1);
        if (upcell != null)
          up = upcell.getDown();


        CCell downcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x, position.y - 1);
        if (downcell != null)
          down = downcell.getUp();

        CCell leftcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x - 1, position.y);
        if (leftcell != null)
          left = leftcell.getRight();

        CCell rightcell = (CCell)((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(position.x + 1, position.y);
        if (rightcell != null)
          right = rightcell.getLeft();


        grid.set(i, analyseTile(up, down, left, right, grid.get(i)));
      }

    }
  }


  private BitSet analyseTile(BitSet up, BitSet down, BitSet left, BitSet right,
      BitSet currentBitCell) {
    BitSet gridCell = currentBitCell;
    //System.out.println("got here----------------------------------------");
    //System.out.print(up.toString() + down.toString() + left.toString() + right.toString() + currentBitCell.toString() + "\n");

    if (up != null)
      currentBitCell.and(up);

    if (down != null)
      currentBitCell.and(down);

    if (left != null)
      currentBitCell.and(left);
    
    if (right != null)
      currentBitCell.and(right);

    return gridCell;
  }


  public class CCell extends Cell {
    //private transient TerrainResource terrainResource;
    private BitSet possibleUp;
    private BitSet possibleDown;
    private BitSet possibleLeft;
    private BitSet possibleRight;

    private boolean isCollapsed;
    private BitSet options;

    CCell() {
      super();

      this.possibleUp = new BitSet(TerrainResource.TILE_SIZE);
      this.possibleDown = new BitSet(TerrainResource.TILE_SIZE);
      this.possibleLeft = new BitSet(TerrainResource.TILE_SIZE);
      this.possibleRight = new BitSet(TerrainResource.TILE_SIZE);
      this.isCollapsed = false;
      
      this.options = new BitSet(TerrainResource.TILE_SIZE);
      this.options.set(0, TerrainResource.TILE_SIZE, true);
    }

    private BitSet getUp() {
      return this.possibleUp;
    }

    private BitSet getDown() {
      return this.possibleDown;
    }

    private BitSet getLeft() {
      return this.possibleLeft;
    }

    private BitSet getRight() {
      return this.possibleRight;
    }

    //private void setpossibleUp(Tile up) {
    //  this.possibleUp = up.getDown();
    //}

    public CCell setTile(Tile tile, TerrainResource terrainResource) {
      super.setTile(new TerrainTile(tile.getTexture()));
      terrainResource.getTilebyName(tile.getName());

      this.possibleUp = tile.getUp();
      this.possibleDown = tile.getDown();
      this.possibleLeft = tile.getLeft();
      this.possibleRight = tile.getRight();

      this.isCollapsed = true;
      return this;
    }

  }
}
