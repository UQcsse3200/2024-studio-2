package com.csse3200.game.minigames.maze.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.terrain.enums.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory for creating game terrain for the maze mini-game.
 */
public class MazeTerrainFactory extends TerrainFactory {
    public static final GridPoint2 MAP_SIZE = new GridPoint2(13, 13);
    public static final float TILE_SIZE = 1;

    /**
     * Create a terrain factory with Orthogonal orientation
     *
     * @param cameraComponent Camera to render terrains to. Must be ortographic.
     */
    public MazeTerrainFactory(CameraComponent cameraComponent) {
        super(cameraComponent);
    }

    /**
     * Create terrain for the maze mini-game. There is only one Terrain type, the maze background
     * water.
     *
     * @return Terrain component which renders the terrain
     */
    public TerrainComponent createTerrain() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion water =
                new TextureRegion(resourceService.getAsset("images/minigames/water.png", Texture.class));
        return createMazeTerrain(water);
    }

    /**
     * Create terrain component for the water terrain
     * @param water the water texture
     * @return The terrain component to render water
     */
    private TerrainComponent createMazeTerrain(
            TextureRegion water) {
        GridPoint2 tilePixelSize = new GridPoint2(water.getRegionWidth(), water.getRegionHeight());
        TiledMap tiledMap = createWaterTiles(tilePixelSize, water);
        TiledMapRenderer renderer = createRenderer(tiledMap, MazeTerrainFactory.TILE_SIZE / tilePixelSize.x);
        return new TerrainComponent(camera, tiledMap, renderer, TerrainOrientation.ORTHOGONAL, MazeTerrainFactory.TILE_SIZE, MapHandler.MapType.MAZE_MINIGAME);
    }

    /**
     * Creates the water tiles for maze mini-game
     * @param tileSize the size of each water tile
     * @param water the water texture
     * @return the tiles water map
     */
    private TiledMap createWaterTiles(
            GridPoint2 tileSize, TextureRegion water) {
        TiledMap tiledMap = new TiledMap();
        TerrainTile waterTile = new TerrainTile(water);
        TiledMapTileLayer layer = new TiledMapTileLayer(
                MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

        // Create base water
        fillTiles(layer, waterTile);

        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    /**
     * Fills the layer of the map with tiles
     * @param layer the layer to fill with tiles
     * @param tile the tiles to put in the layer
     */
    private static void fillTiles(TiledMapTileLayer layer, TerrainTile tile) {
        for (int x = 0; x < MazeTerrainFactory.MAP_SIZE.x; x++) {
            for (int y = 0; y < MazeTerrainFactory.MAP_SIZE.y; y++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
    }

    /**
     * Gets the maze grid cell containing the given point in world coordinates.
     * @param worldPos vector to represent a position in the world
     * @return the coordinate of the grid cell this point is contained in
     */
    public static GridPoint2 worldPosToGridPos(Vector2 worldPos) {
        return new GridPoint2((int) (worldPos.x / TILE_SIZE), (int) (worldPos.y / TILE_SIZE));
    }
}
