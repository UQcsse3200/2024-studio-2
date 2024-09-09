package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class CombatTerrainFactory {
    private static final GridPoint2 MAP_SIZE = new GridPoint2(1030, 590);
    private static final int TUFT_TILE_COUNT = 30;
    private static final int ROCK_TILE_COUNT = 30;

    private final OrthographicCamera camera;
    private final TerrainOrientation orientation;
    private GridPoint2 mapSize;
    private final CameraComponent cameraComponent;
    private Table table;

    /**
     * Create a terrain factory with Orthogonal orientation
     *
     * @param cameraComponent Camera to render terrains to. Must be ortographic.
     */
    public CombatTerrainFactory(CameraComponent cameraComponent) {
        this(cameraComponent, TerrainOrientation.ORTHOGONAL);
    }

    /**
     * Create a terrain factory
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     * @param orientation orientation to render terrain at
     */
    public CombatTerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
        this.orientation = orientation;
        this.cameraComponent = cameraComponent;
    }

    /**
     * Retrieve the component to which the camera is attached
     *
     * @return the camera component
     */
    public CameraComponent getCameraComponent() {
        return this.cameraComponent;
    }

    public TerrainComponent createBackgroundTerrain2(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 screenSize) {
        // Initialize ResourceService and load the background texture
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion backgroundTextureRegion;
        float backgroundWidth, backgroundHeight;

        // Determine the background image based on terrain type
        switch (terrainType) {
            case FOREST_DEMO:
                backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
                break;
            default:
                return null; // Return null for unsupported terrain types
        }

        // Get the size of the background texture
        backgroundWidth = backgroundTextureRegion.getRegionWidth();
        backgroundHeight = backgroundTextureRegion.getRegionHeight();

        // Calculate the scale to fit the image to the screen size
//        float scaleX = screenSize.x / backgroundWidth;
//        float scaleY = screenSize.y / backgroundHeight;
//        float scale = Math.min(scaleX, scaleY); // Choose the smaller scale to ensure the image fits within the screen

        // Calculate the scale to fill the image to the screen size
        float scaleX = screenSize.x / backgroundWidth;
        float scaleY = screenSize.y / backgroundHeight;
        float scale = Math.max(scaleX, scaleY); // Choose the larger scale to ensure the image covers the screen

        // Calculate scaled dimensions
        int scaledWidth = (int) (backgroundWidth * scale);
        int scaledHeight = (int) (backgroundHeight * scale);

        // Create a TiledMap with a single layer that covers the screen size
        TiledMap tiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(1, 1, scaledWidth, scaledHeight);

        // Create a tile from the background texture
        TextureRegion scaledTextureRegion = new TextureRegion(backgroundTextureRegion);
        TiledMapTile backgroundTile = new StaticTiledMapTile(scaledTextureRegion);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(backgroundTile);

        // Fill the layer with the single tile
        layer.setCell(0, 0, cell);

        // Add the layer to the map
        tiledMap.getLayers().add(layer);

        // Create a renderer for the tiled map
        TiledMapRenderer renderer = createRenderer(tiledMap, 1f); // 1f scale used for background

        // Adjust camera settings to fit the background
        camera.setToOrtho(false, screenSize.x, screenSize.y);
        camera.position.set(screenSize.x / 2f, screenSize.y / 2f, 0);

        // Print debugging information
        System.out.println("Background Width: " + backgroundWidth);
        System.out.println("Background Height: " + backgroundHeight);
        System.out.println("Scaled Width: " + scaledWidth);
        System.out.println("Scaled Height: " + scaledHeight);
        System.out.println("Screen Size: " + screenSize);

        // Return the TerrainComponent with the background image rendering setup
        return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f); // 1f scale used for background
    }

    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
        switch (orientation) {
            case ORTHOGONAL:
                return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
            case ISOMETRIC:
                return new IsometricTiledMapRenderer(tiledMap, tileScale);
            case HEXAGONAL:
                return new HexagonalTiledMapRenderer(tiledMap, tileScale);
            default:
                return null;
        }
    }

    private TiledMap createForestDemoTiles(
            GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
        TiledMap tiledMap = new TiledMap();
        TerrainTile grassTile = new TerrainTile(grass);
        TerrainTile grassTuftTile = new TerrainTile(grassTuft);
        TerrainTile rockTile = new TerrainTile(rocks);
        TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

        // Create base grass
        fillTiles(layer, MAP_SIZE, grassTile);

        // Add some grass and rocks
        fillTilesAtRandom(layer, MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
        fillTilesAtRandom(layer, MAP_SIZE, rockTile, ROCK_TILE_COUNT);

        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    private static void fillTilesAtRandom(
            TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
        GridPoint2 min = new GridPoint2(0, 0);
        GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

        for (int i = 0; i < amount; i++) {
            GridPoint2 tilePos = RandomUtils.random(min, max);
            Cell cell = layer.getCell(tilePos.x, tilePos.y);
            cell.setTile(tile);
        }
    }

    private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
        for (int x = 0; x < mapSize.x; x++) {
            for (int y = 0; y < mapSize.y; y++) {
                Cell cell = new Cell();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
    }

    /**
     * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
     * the same orientation. But for demonstration purposes, the base code has the same level in 3
     * different orientations.
     */
    public enum TerrainType {
        FOREST_DEMO,
        FOREST_DEMO_ISO,
        FOREST_DEMO_HEX
    }
}
