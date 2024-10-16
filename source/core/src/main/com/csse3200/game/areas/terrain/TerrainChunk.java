package com.csse3200.game.areas.terrain;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.forest.ForestGameArea;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.terrain.tiles.Tile;
import com.csse3200.game.areas.terrain.enums.*;

/**
 * A chunk of terrain in the game world.
 * This class is responsible for generating and rendering the terrain.
 */
public class TerrainChunk {
  public static final int CHUNK_SIZE = 16;

  private GridPoint2 position;
  private TiledMap tiledMap;

  public Array<BitSet> grid;
  private BitSet collapsedTiles;

  private Map<String, Integer> tileTypeCount;
  int totalTiles = 0;
  private MapType inArea;
 
  TerrainChunk(GridPoint2 position, TiledMap map) {
    this.position = position;
    this.tiledMap = map;
    this.tileTypeCount = new HashMap<>();
    this.grid = new Array<>(256);
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

    // if chunk is in another area, then terrainResource load asset for that area
    // INFO: The Map is equally divided into three areas. Each area is 16x10 tiles wide.
    inArea = checkAreaType(position);
    switch (inArea) {
      case MapType.FOREST -> totalTiles = terrainResource.getTileSize(TileLocation.FOREST);
      case MapType.WATER -> totalTiles = terrainResource.getTileSize(TileLocation.WATER);
      case MapType.FOG -> totalTiles = terrainResource.getTileSize(TileLocation.FOG);
      default -> totalTiles = terrainResource.getTileSize(TileLocation.AIR);
    }

    for (int i = 0; i < 256; ++i) {
      BitSet bitset = new BitSet(totalTiles);
      bitset.set(0, totalTiles, true);
      this.grid.add(bitset);
    }

    updateGrid(terrainResource);

    while (true)
      if (collapseAll(cPosX, cPosY, terrainResource))
        break;
  }

  private MapType checkAreaType(GridPoint2 chunkPos) {
    if (chunkPos.y < (ForestGameArea.MAP_SIZE.y / 16) / 3) {
      return MapType.FOREST;
    } else if (chunkPos.y < (ForestGameArea.MAP_SIZE.y / 16) / 3 * 2) {
        return MapType.WATER;
    } else {
      return MapType.AIR;
    }
  }

  public Map<String, Integer> getTileTypeCount() {return tileTypeCount;}

  /**
   * Fill all tile in the chunk return true if all tiles are collapsed, false
   * otherwise
   *
   * @param cPosX           x position of the chunk
   * @param cPosY           y position of the chunk
   * @param terrainResource Terrain resource to use for generating the terrain
   *
   * @return true if all tiles are collapsed, false otherwise
   */
  private boolean collapseAll(int cPosX, int cPosY, TerrainResource terrainResource) {
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
      Array<Integer> minentropyTiles = new Array<>();
      for (int i = 0; i < grid.size; ++i) {
        if (grid.get(i).cardinality() == minentropy) {
          minentropyTiles.add(i);
        }
      }

      if (minentropyTiles.size == 0)
        break;

      Integer randomTile = minentropyTiles.random();

      // ranodm pick a tile
      int randomTrueBitIndex = randomPickTile(grid.get(randomTile));


      // clear all bit of the picked cell as filled tile
      grid.get(randomTile).clear(); // collapsed

      collapseTile(cPosX + randomTile % 16, cPosY + randomTile / 16, terrainResource, randomTrueBitIndex);
      collapsedTiles.set(randomTile);

      updateGrid(terrainResource);
    }
    // set the rest of the empty tiles
    setDefaultTiles(cPosX, cPosY, terrainResource);

    return allCollapsed;
  }

  /**
   * random pick a tile from the possible tiles
   *
   * @param bitSet The possible tiles
   * @return the index of the picked tile
   */
  private int randomPickTile(BitSet bitSet) {
    int numTrueBits = bitSet.cardinality();
    int randomTrueBitIndex = 0;
    if (numTrueBits > 0) {
      int randomIndex = ((int) (MathUtils.random() * numTrueBits));
      randomTrueBitIndex = bitSet.nextSetBit(0);
      for (int i = 0; i < randomIndex; i++)
        randomTrueBitIndex = bitSet.nextSetBit(randomTrueBitIndex + 1);
    }
    return randomTrueBitIndex;
  }

  /**
   * Set the rest of the empty tiles to a default tile.
   *
   * @param cPosX           x position of the chunk
   * @param cPosY           y position of the chunk
   * @param terrainResource Terrain resource to use for generating the terrain
   */
  private void setDefaultTiles(int cPosX, int cPosY, TerrainResource terrainResource) {
    int currentBit = 0;
    for (int i = 0; i < collapsedTiles.size() - collapsedTiles.cardinality(); ++i) {
      currentBit = collapsedTiles.nextClearBit(currentBit);
      collapseTile(cPosX + currentBit % 16, cPosY + currentBit / 16, terrainResource, 4);
      currentBit++;
    }
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
    cell.setTile(tile, terrainResource);
    ((TiledMapTileLayer) tiledMap.getLayers().get(0)).setCell(x, y, cell);

    if (inArea == MapType.WATER) {
      CCell fogCell = new CCell();
      Tile fogTile = terrainResource.getMapTilebyIndex(0, MapType.FOG);
      fogCell.setTile(fogTile, terrainResource);
      ((TiledMapTileLayer) tiledMap.getLayers().get(1)).setCell(x, y, fogCell);
    } else if (inArea == MapType.AIR) {
      CCell fogCell = new CCell();
      Tile fogTile = terrainResource.getMapTilebyIndex(0, MapType.FOG);
      fogCell.setTile(fogTile, terrainResource);
      ((TiledMapTileLayer) tiledMap.getLayers().get(2)).setCell(x, y, fogCell);
    }


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
}
