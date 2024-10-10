package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.areas.FogGameAreaConfigs.FogMapTiles;
import com.csse3200.game.areas.FogGameAreaConfigs.FogTileConfig;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanMapTiles;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanTileConfig;
import com.csse3200.game.areas.terrain.tiles.ForestTileConfig;
import com.csse3200.game.areas.terrain.tiles.TileConfig;
import com.csse3200.game.areas.terrain.tiles.Tile;
import com.csse3200.game.areas.terrain.tiles.TileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.enums.*;

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
    private int forestSize;
    private int waterSize;
    private int airSize;
    private int fogSize;
    public static int forestSize = 0;
    public static int waterSize = 0;
    public static int airSize = 0;
    public static int fogSize = 0;

    private boolean unlockedWater;

    public TerrainResource(MapHandler.MapType mapType) {
        forestTiles = new ArrayList<>();
        waterTiles = new ArrayList<>();
        airTiles = new ArrayList<>();
        fogTiles = new ArrayList<>();

        ResourceService resourceService = ServiceLocator.getResourceService();
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
                forestSize = forestTiles.size();

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
                waterSize = waterTiles.size();

                // load fog tiles
                for (FogTileConfig tile : fogTileConfig.fogTiles) {
                    fogTiles.add(new Tile(tile.id,
                            new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
                            tile.edges,
                            tile.centre));
                }
                fogSize = fogTiles.size();

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

    public int getForestSize() {
        return forestSize;
    }

    public int getWaterSize() {
        return waterSize;
    }

    public int getAirSize() {
        return airSize;
    }

    public int getFogSize() {
        return fogSize;
    }

    public List<Tile> getMapTiles(MapHandler.MapType mapType) {
        return switch (mapType) {
            case FOREST -> forestTiles;
            case WATER -> waterTiles;
            case AIR -> airTiles;
            case FOG -> fogTiles;
            case COMBAT -> null;
            default -> throw new IllegalArgumentException("No such map type:" + mapType);
        };
    }

    /**
     * Set all possible tiles for each tile for all directions.
     */
    public void setPossibleTiles() {
        for (int i = 0; i < forestTiles.size(); i++) {
            setPossibleUp(forestTiles.get(i), forestTiles);
            setPossibleRight(forestTiles.get(i), forestTiles);
            setPossibleDown(forestTiles.get(i), forestTiles);
            setPossibleLeft(forestTiles.get(i), forestTiles);
        }

        for (int i = 0; i < waterTiles.size(); i++) {
            setPossibleUp(waterTiles.get(i), waterTiles);
            setPossibleRight(waterTiles.get(i), waterTiles);
            setPossibleDown(waterTiles.get(i), waterTiles);
            setPossibleLeft(waterTiles.get(i), waterTiles);
        }

        for (int i = 0; i < fogTiles.size(); i++) {
            setPossibleUp(fogTiles.get(i), fogTiles);
            setPossibleRight(fogTiles.get(i), fogTiles);
            setPossibleDown(fogTiles.get(i), fogTiles);
            setPossibleLeft(fogTiles.get(i), fogTiles);
        }
    }

    public static int getTileSize(TileLocation location) {
        switch (location) {
            case FOREST -> {return forestSize;}
            case WATER -> {return waterSize;}
            case AIR -> {return airSize;}
            case FOG -> {return fogSize;}
            default -> throw new IllegalArgumentException("Given tile location doesn't exist!");
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
            if (areaTiles.get(i).getEdgeTiles().get(2).equals(tile.getEdgeTiles().getFirst())) {
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
            if (areaTiles.get(i).getEdgeTiles().getFirst().equals(tile.getEdgeTiles().get(2))) {
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
        return switch (mapType) {
            case FOREST -> forestTiles.get(index);
            case FOG -> fogTiles.get(index);
            case WATER -> waterTiles.get(index);
            case AIR -> airTiles.get(index);
            case COMBAT -> null;
            default -> throw new IllegalArgumentException("No such map type:" + mapType);
        };
    }

    public boolean hasUnlockedWater() {return unlockedWater;}
}
