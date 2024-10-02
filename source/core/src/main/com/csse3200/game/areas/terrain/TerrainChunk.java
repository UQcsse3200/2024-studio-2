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
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainResource;
import com.csse3200.game.areas.terrain.TerrainComponent.Tile;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.mapped.MappedType;

import java.util.BitSet;
import java.util.Arrays;

/**
 * A chunk of terrain in the game world.
 * This class is responsible for generating and rendering the terrain.
 */
public class TerrainChunk {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private GridPoint2 position;
  private TiledMap tiledMap;

  public Array<BitSet> grid;
  private BitSet collapsedTiles;

  public HashMap<String, Integer> tileTypeCount;
  public int totalTiles = 0;
  public MapType inArea; 
 
  TerrainChunk(GridPoint2 position, TiledMap map) {
    this.position = position;
    this.tiledMap = map;
    this.tileTypeCount = new HashMap<String, Integer>();
    this.grid = new Array<BitSet>(256);
    this.collapsedTiles = new BitSet(256);
  }

  /**
   * Generate the tiles for this chunk of terrain.
   * 
   * @param chunkPos        The position of this chunk in the world
   * @param loadedChunks    The chunks of terrain that are currently loaded
   * @param terrainResource The terrain resource to use for generating the terrain
   */
  public void generateTiles(GridPoint2 chunkPos, Map<GridPoint2, TerrainChunk> loadedChunks,
      TerrainResource terrainResource) {
    int cPosX = chunkPos.x * CHUNK_SIZE;
    int cPosY = chunkPos.y * CHUNK_SIZE;

    // if chunk is in another area, then terrainResource load assest for that area
    // INFO: The Map is equally divied into three areaas. Each area is 16x10 tiles wide.
    inArea = checkAreaType(position);
    if (inArea == MapType.FOREST) {
      totalTiles = TerrainResource.FOREST_SIZE;
    } else if (inArea == MapType.WATER) {
      totalTiles = TerrainResource.WATER_SIZE;
    } else {
      totalTiles = TerrainResource.AIR_SIZE;
    }

    for (int i = 0; i < 256; ++i) {
      BitSet bitset = new BitSet(totalTiles);
      bitset.set(0, totalTiles, true);
      this.grid.add(bitset);
    }

    updateGrid(terrainResource);

    while (true)
      if (collapseAll(cPosX, cPosY, terrainResource, inArea))
        break;
  }

  private MapType checkAreaType(GridPoint2 chunkPos) {
    if (chunkPos.y < (int)(ForestGameArea.MAP_SIZE.y / 16) / 3) {
      return MapType.FOREST;
    } else if (chunkPos.y < (int)(ForestGameArea.MAP_SIZE.y / 16) / 3 * 2) {
      return MapType.WATER;
    } else {
      // TODO: change to air 
      return MapType.WATER;
    }
  }

  /**
   * Fille all tile in the chunk return true if all tiles are collapsed, false
   * otherwise
   *
   * @param cPosX           x position of the chunk
   * @param cPosY           y position of the chunk
   * @param terrainResource Terrain resource to use for generating the terrain
   *
   * @return true if all tiles are collapsed, false otherwise
   */
  private boolean collapseAll(int cPosX, int cPosY, TerrainResource terrainResource, MapType type) {
    boolean allCollapsed = true;
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

      if (minentropyTiles.size == 0)
        break;

      Integer randomTile = minentropyTiles.random();

      // ranodm pick a tile
      int numTrueBits = grid.get(randomTile).cardinality();
      int randomTrueBitIndex = 0;
      if (numTrueBits > 0) {
        int randomIndex = ((int) (Math.random() * numTrueBits));
        randomTrueBitIndex = grid.get(randomTile).nextSetBit(0);
        for (int i = 0; i < randomIndex; i++)
          randomTrueBitIndex = grid.get(randomTile).nextSetBit(randomTrueBitIndex + 1);
      }

      // clear all bit of the picked cell as filled tile
      grid.get(randomTile).clear(); // collapsed

      collapseTile(cPosX + randomTile % 16, cPosY + randomTile / 16, terrainResource, randomTrueBitIndex);
      collapsedTiles.set(randomTile);

      updateGrid(terrainResource);
    }
    // set the rest of the empty tiles
    int currentBit = 0;
    for (int i = 0; i < collapsedTiles.size() - collapsedTiles.cardinality(); ++i) {
      currentBit = collapsedTiles.nextClearBit(currentBit);
      collapseTile(cPosX + currentBit % 16, cPosY + currentBit / 16, terrainResource, 4);
      currentBit++;
    }

    return allCollapsed;
  }

  /**
   *  Set the selected tile on the map.
   *  Also strores the type of the tile infomation 
   *
   *  @param x               x position of the tile
   *  @param y               y position of the tile
   *  @param terrainResource Terrain resource to use for generating the terrain
   *  @param tileIndex       The index of the tile to set
   */
  private void collapseTile(int x, int y, TerrainResource terrainResource, int tileIndex) {
    CCell cell = new CCell();
    Tile tile = terrainResource.getMapTilebyIndex(tileIndex, inArea);
    //Tile tile = terrainResource.getTilebyIndex(tileIndex);
    cell.setTile(tile, terrainResource);
    ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(x, y, cell);

    if (tileTypeCount.containsKey(tile.getCentre())) {
      tileTypeCount.put(tile.getCentre(), tileTypeCount.get(tile.getCentre()) + 1);
    } else {
      tileTypeCount.put(tile.getCentre(), 1);
    }
  }

  /**
   * Get the tile type count for a given tile type. 
   * Returns null if the tile type does not exist.
   * 
   * @param tileType The tile type to get the count for
   * @return The count of the tile type
   */
  public Integer getTileTypeCount(String tileType) {
    return tileTypeCount.get(tileType);
  }


  /**
   * Update the grid of possible tiles for each cell in the chunk.
   */
  private void updateGrid(TerrainResource terrainResource) {
    for (int i = 0; i < grid.size; ++i) {
      int x = position.x * 16 + (i % 16);
      int y = position.y * 16 + (i / 16);
      CCell thisCell = ((CCell) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(x, y));
      if (thisCell != null)
        continue;
      int tSize = terrainResource.getMapTiles(inArea).size();
      BitSet up = new BitSet(tSize);
      BitSet down = new BitSet(tSize);
      BitSet left = new BitSet(tSize);
      BitSet right = new BitSet(tSize);
      up.set(0, tSize, true);
      down.set(0, tSize, true);
      left.set(0, tSize, true);
      right.set(0, tSize, true);

      // position is the chunk position, needs to be converted to world position
      // System.out.println("position: " + x + " " + y + " i: " + i);
      CCell upcell = (CCell) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(x, y + 1);
      if (upcell != null)
        up = upcell.getDown();

      CCell downcell = (CCell) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(x, y - 1);
      if (downcell != null)
        down = downcell.getUp();

      CCell leftcell = (CCell) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(x - 1, y);
      if (leftcell != null)
        left = leftcell.getRight();

      CCell rightcell = (CCell) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(x + 1, y);
      if (rightcell != null)
        right = rightcell.getLeft();

      // System.out.println("up: " + up + " down: " + down + " left: " + left + "
      // right: " + right);
      grid.set(i, analyseTile(up, down, left, right, grid.get(i)));
    }
  }

  /**
   * Analyse the possible tiles for a given cell based on the surrounding cells.
   * 
   * @param up             The cell above this cell
   * @param down           The cell below this cell
   * @param left           The cell to the left of this cell
   * @param right          The cell to the right of this cell
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

    public boolean isCollapsed;
    //private BitSet options;

    CCell() {
      super();
      this.possibleUp = new BitSet();
      this.possibleDown = new BitSet();
      this.possibleLeft = new BitSet();
      this.possibleRight = new BitSet();
      this.isCollapsed = false;

      //this.options = new BitSet(TerrainResource.TILE_SIZE);
      //this.options.set(0, TerrainResource.TILE_SIZE, true);
    }

    /**
     * Get the possible tiles that can join with the top of this cell.
     * 
     * @return The possible tiles for the cell
     */
    private BitSet getUp() {
      return this.possibleUp;
    }

    /**
     * Get the possible tiles that can join with the bottom of this cell.
     * 
     * @return The possible tiles for the cell
     */
    private BitSet getDown() {
      return this.possibleDown;
    }

    /**
     * Get the possible tiles that can join with the left of this cell.
     * 
     * @return The possible tiles for the cell
     */
    private BitSet getLeft() {
      return this.possibleLeft;
    }

    /**
     * Get the possible tiles that can join with the right of this cell.
     * 
     * @return The possible tiles for the cell
     */
    private BitSet getRight() {
      return this.possibleRight;
    }

    /**
     * Set the tile and also mean the cell is collapsed and confirm all possible
     * tiles.
     * 
     * @param tile            The tile to set
     * @param terrainResource The terrain resource to use for setting the tile
     * @return this cell object
     */
    public CCell setTile(Tile tile, TerrainResource terrainResource) {
      super.setTile(new TerrainTile(tile.getTexture()));
      //terrainResource.getTilebyName(tile.getName());

      this.possibleUp = tile.getUp();
      this.possibleDown = tile.getDown();
      this.possibleLeft = tile.getLeft();
      this.possibleRight = tile.getRight();

      this.isCollapsed = true;
      return this;
    }

  }

  public enum TileType {
    GRASS, WATER, SAND, NONE
  }
}
