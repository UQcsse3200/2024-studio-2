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

/**
 * A chunk of terrain in the game world.
 * This class is responsible for generating and rendering the terrain.
 * */
public class TerrainChunk {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private GridPoint2 position;
  private TiledMap tiledMap;

  public Array<Tile> tiles;
  public Array<BitSet> grid;

  TerrainChunk(GridPoint2 position, TiledMap map) {
    this.position = position;
    this.tiledMap = map;
    this.grid = new Array<BitSet>(256);

    for (int i = 0; i < 256; ++i) {
      BitSet bitset = new BitSet(TerrainResource.TILE_SIZE);
      bitset.set(0, TerrainResource.TILE_SIZE, true);
      this.grid.add(bitset);
    }

    // initialise the grid
    for (int i = 0; i < 256; ++i) {
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

  /**
   * Generate the tiles for this chunk of terrain.
   * @param chunkPos The position of this chunk in the world
   * @param loadedChunks The chunks of terrain that are currently loaded
   * @param terrainResource The terrain resource to use for generating the terrain
   */
  public void generateTiles(GridPoint2 chunkPos, Map<GridPoint2, TerrainChunk> loadedChunks,
                            TerrainResource terrainResource) {
    int cPosX = chunkPos.x * CHUNK_SIZE;
    int cPosY = chunkPos.y * CHUNK_SIZE;

    for (int t = 0; t < 256; ++t) {

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

      int randomTile = minentropyTiles.random();

      GridPoint2 toGridpos = new GridPoint2(randomTile % 16, randomTile / 16);

      // ranodm pick a tile
      int numTrueBits = grid.get(randomTile).cardinality();
      int randomTrueBitIndex = -1;
      if (numTrueBits > 0) {
        int randomIndex = (int) (Math.random() * numTrueBits);
        randomTrueBitIndex = grid.get(randomTile).nextSetBit(0);
        for (int i = 0; i < randomIndex; i++)
          randomTrueBitIndex = grid.get(randomTile).nextSetBit(randomTrueBitIndex + 1);
      }

      // clear all bit of the picked cell as filled tile
      grid.get(randomTile).clear(); // collapsed


      CCell cell = new CCell();
      cell.setTile(terrainResource.getTilebyIndex(randomTrueBitIndex), terrainResource);
      ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(cPosX + toGridpos.x, cPosY + toGridpos.y, cell);

      //// update grid
      for (int i = 0; i < grid.size; ++i) {
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


  /**
   * Analyse the possible tiles for a given cell based on the surrounding cells.
   * @param up The cell above this cell
   * @param down The cell below this cell
   * @param left The cell to the left of this cell
   * @param right The cell to the right of this cell
   * @param currentBitCell The current bitset for this cell
   *
   * @return The updated bitset for this cell
   */
  private BitSet analyseTile(BitSet up, BitSet down, BitSet left, BitSet right,
                             BitSet currentBitCell) {
    BitSet gridCell = currentBitCell;
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


  /**
   * The CCell class holds the possible tiles for the cell.
   */
  public class CCell extends Cell {
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

    /**
     * Get the possible tiles that can join with the top of this cell.
     * @return The possible tiles for the cell
     */
    private BitSet getUp() {
      return this.possibleUp;
    }

    /**
     * Get the possible tiles that can join with the bottom of this cell.
     * @return The possible tiles for the cell
     */
    private BitSet getDown() {
      return this.possibleDown;
    }

    /**
     * Get the possible tiles that can join with the left of this cell.
     * @return The possible tiles for the cell
     */
    private BitSet getLeft() {
      return this.possibleLeft;
    }

    /**
     * Get the possible tiles that can join with the right of this cell.
     * @return The possible tiles for the cell
     */
    private BitSet getRight() {
      return this.possibleRight;
    }

    /**
     * Set the tile and also mean the cell is collapsed and confirm all possible tiles.
     * @param tile The tile to set
     * @param terrainResource The terrain resource to use for setting the tile
     * @return this cell object
     */
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
