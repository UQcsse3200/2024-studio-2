package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.areas.FogGameAreaConfigs.FogMapTiles;
import com.csse3200.game.areas.FogGameAreaConfigs.FogTileConfig;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanMapTiles;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanTileConfig;
import com.csse3200.game.areas.terrain.tiles.ForestTileConfig;
import com.csse3200.game.areas.terrain.tiles.Tile;
import com.csse3200.game.areas.terrain.tiles.TileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * TerrainResource class to store all possible tiles and their edge tiles.
 */
public class TerrainResource {
    private List<Tile> forestTiles;
    private List<Tile> waterTiles;
    private List<Tile> airTiles;
    private List<Tile> fogTiles;

    // total number of each tile
    public static int FOREST_SIZE = 0;
    public static int WATER_SIZE = 0;
    public static int AIR_SIZE = 0;
    public static int FOG_SIZE = 0;
    private boolean unlockedWater;

    public TerrainResource(MapHandler.MapType mapType) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        forestTiles = new ArrayList<>();
        waterTiles = new ArrayList<>();
        airTiles = new ArrayList<>();
        fogTiles = new ArrayList<>();
        this.unlockedWater = false;
        switch (mapType) {
            case FOREST:
                // load forest tiles
                for (TileConfig tile : ForestTileConfig.getForestMapTiles()) {
                    // edge: TOP, RIGHT, BOTTOM, LEFT
                    // A: sand, B: grass, C: water
                    // =======================
                    forestTiles.add(
                            new Tile(tile.id,
                                    new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
                                    tile.edges, tile.centre));
                }
                FOREST_SIZE = forestTiles.size();

                // load water tiles
                OceanMapTiles oceanTileConfig;
                FogMapTiles fogTileConfig;
                oceanTileConfig = FileLoader.readClass(OceanMapTiles.class, "configs/OceanGameAreaConfigs/waterTiles.json");
                fogTileConfig = FileLoader.readClass(FogMapTiles.class, "configs/FogGameAreaConfigs/fogTiles.json");

                for (OceanTileConfig tile : oceanTileConfig.waterMapTiles) {
                    waterTiles.add(new Tile(tile.id,
                            new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
                            tile.edges,
                            tile.centre));
                }
                WATER_SIZE = waterTiles.size();

                // load fog tiles
                for (FogTileConfig tile : fogTileConfig.fogTiles) {
                    fogTiles.add(new Tile(tile.id,
                            new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
                            tile.edges,
                            tile.centre));
                }
                FOG_SIZE = fogTiles.size();

                break;
            case COMBAT:
                break;
            case MAZE_MINIGAME:
                break;
            default:
                throw new IllegalArgumentException("Map type not supported: " + mapType);
        }

        this.setPossibleTiles();
    }

    public List<Tile> getMapTiles(MapHandler.MapType mapType) {
        switch (mapType) {
            case FOREST:
                return forestTiles;
            case WATER:
                return waterTiles;
            case AIR:
                return airTiles;
            case FOG:
                return fogTiles;
            case COMBAT:
                return null;
            default:
                throw new IllegalArgumentException("No such map type:" + mapType);
        }
    }

    /**
     * Set all possible tiles for each tile for all directions.
     */
    public void setPossibleTiles() {
        for (int i = 0; i < this.forestTiles.size(); i++) {
            setPossibleUp(this.forestTiles.get(i), this.forestTiles);
            setPossibleRight(this.forestTiles.get(i), this.forestTiles);
            setPossibleDown(this.forestTiles.get(i), this.forestTiles);
            setPossibleLeft(this.forestTiles.get(i), this.forestTiles);
        }

        for (int i = 0; i < this.waterTiles.size(); i++) {
            setPossibleUp(this.waterTiles.get(i), this.waterTiles);
            setPossibleRight(this.waterTiles.get(i), this.waterTiles);
            setPossibleDown(this.waterTiles.get(i), this.waterTiles);
            setPossibleLeft(this.waterTiles.get(i), this.waterTiles);
        }

        for (int i = 0; i < this.fogTiles.size(); i++) {
            setPossibleUp(this.fogTiles.get(i), this.fogTiles);
            setPossibleRight(this.fogTiles.get(i), this.fogTiles);
            setPossibleDown(this.fogTiles.get(i), this.fogTiles);
            setPossibleLeft(this.fogTiles.get(i), this.fogTiles);
        }
    }

    /**
     * Set possible tiles for up direction.
     *
     * @param tile The tile to set possible tiles
     */
    public void setPossibleUp(Tile tile, List<Tile> areaTiles) {
        BitSet up = new BitSet(areaTiles.size());
        for (int i = 0; i < areaTiles.size(); i++) {
            if (areaTiles.get(i).getEdgeTiles().get(2).equals(tile.getEdgeTiles().get(0))) {
                up.set(i, true);
            }
        }
        tile.setPossibleUp(up);
    }

    /**
     * Set possible tiles for right direction.
     *
     * @param tile The tile to set possible tiles
     */
    public void setPossibleRight(Tile tile, List<Tile> areaTiles) {
        BitSet right = new BitSet(areaTiles.size());
        for (int i = 0; i < areaTiles.size(); i++) {
            if (areaTiles.get(i).getEdgeTiles().get(3).equals(tile.getEdgeTiles().get(1))) {
                right.set(i, true);
            }
        }
        tile.setPossibleRight(right);
    }

    /**
     * Set possible tiles for down direction.
     *
     * @param tile The tile to set possible tiles
     */
    public void setPossibleDown(Tile tile, List<Tile> areaTiles) {
        BitSet down = new BitSet(areaTiles.size());
        for (int i = 0; i < areaTiles.size(); i++) {
            if (areaTiles.get(i).getEdgeTiles().get(0).equals(tile.getEdgeTiles().get(2))) {
                down.set(i, true);
            }
        }
        tile.setPossibleDown(down);
    }

    /**
     * Set possible tiles for left direction.
     *
     * @param tile The tile to set possible tiles
     */
    public void setPossibleLeft(Tile tile, List<Tile> areaTiles) {
        BitSet left = new BitSet(areaTiles.size());
        for (int i = 0; i < areaTiles.size(); i++) {
            if (areaTiles.get(i).getEdgeTiles().get(1).equals(tile.getEdgeTiles().get(3))) {
                left.set(i, true);
            }
        }
        tile.setPossibleLeft(left);
    }

    /**
     * Get a tile by index and map type.
     *
     * @param index   The index of the tile
     * @param mapType The map type of the tile
     * @return The tile with the given index and map type
     */
    public Tile getMapTilebyIndex(int index, MapHandler.MapType mapType) {
        switch (mapType) {
            case FOREST:
                return forestTiles.get(index);
            case FOG:
                return fogTiles.get(index);
            case WATER:
                return waterTiles.get(index);
            case AIR:
                return airTiles.get(index);
            case COMBAT:
                return null;
            default:
                throw new IllegalArgumentException("No such map type:" + mapType);
        }
    }
}
