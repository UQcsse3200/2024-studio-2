package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.terrain.enums.*;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;


/** Factory for creating game terrains. */
public class CombatTerrainFactory {

    private final OrthographicCamera camera;
    private final TerrainOrientation orientation;
    private final CameraComponent cameraComponent;

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


    public TerrainComponent createBackgroundTerrainAir(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 screenSize) {
        // Initialize ResourceService and load the background texture
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion backgroundTextureRegion;
        float backgroundWidth;
        float backgroundHeight;

        // Determine the background image based on terrain type
        if (terrainType.equals(TerrainType.FOREST_DEMO)) {
            // backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
            backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/air_background.png", Texture.class));
        } else {
            return null;
        }

        // Get the size of the background texture
        backgroundWidth = backgroundTextureRegion.getRegionWidth();
        backgroundHeight = backgroundTextureRegion.getRegionHeight();

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

        // Return the TerrainComponent with the background image rendering setup
        return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f, MapType.COMBAT); // 1f scale used for background
    }

    public TerrainComponent createBackgroundTerrainLand(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 screenSize) {
        // Initialize ResourceService and load the background texture
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion backgroundTextureRegion;
        float backgroundWidth;
        float backgroundHeight;

        // Determine the background image based on terrain type
        if (terrainType.equals(TerrainType.FOREST_DEMO)) {
            // backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
            backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/land_background.png", Texture.class));
        } else {
            return null;
        }

        // Get the size of the background texture
        backgroundWidth = backgroundTextureRegion.getRegionWidth();
        backgroundHeight = backgroundTextureRegion.getRegionHeight();

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

        // Return the TerrainComponent with the background image rendering setup
        return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f, MapType.COMBAT); // 1f scale used for background
    }

    /**
     * Creates the background terrain for the combat screen
     * @param terrainType
     * @param playerPosition the position of the player for camera positioning against background
     * @param screenSize size of screen for relevant background scaling
     * @return a background terrain
     */
//    public TerrainComponent createBackgroundTerrainWater(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 screenSize) {
//        // Initialize ResourceService and load the background texture
//        ResourceService resourceService = ServiceLocator.getResourceService();
//        TextureRegion backgroundTextureRegion;
//        float backgroundWidth;
//        float backgroundHeight;
//
//        // Determine the background image based on terrain type
//        if (terrainType.equals(TerrainType.FOREST_DEMO)) {
//            // backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
//            backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/water_background.png", Texture.class));
//        } else {
//            return null;
//        }
//
//        // Get the size of the background texture
//        backgroundWidth = backgroundTextureRegion.getRegionWidth();
//        backgroundHeight = backgroundTextureRegion.getRegionHeight();
//
//        // Calculate the scale to fill the image to the screen size
//        float scaleX = screenSize.x / backgroundWidth;
//        float scaleY = screenSize.y / backgroundHeight;
//        float scale = Math.max(scaleX, scaleY); // Choose the larger scale to ensure the image covers the screen
//
//        // Calculate scaled dimensions
//        int scaledWidth = (int) (backgroundWidth * scale);
//        int scaledHeight = (int) (backgroundHeight * scale);
//
//        // Create a TiledMap with a single layer that covers the screen size
//        TiledMap tiledMap = new TiledMap();
//        TiledMapTileLayer layer = new TiledMapTileLayer(1, 1, scaledWidth, scaledHeight);
//
//        // Create a tile from the background texture
//        TextureRegion scaledTextureRegion = new TextureRegion(backgroundTextureRegion);
//        TiledMapTile backgroundTile = new StaticTiledMapTile(scaledTextureRegion);
//        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
//        cell.setTile(backgroundTile);
//
//        // Fill the layer with the single tile
//        layer.setCell(0, 0, cell);
//
//        // Add the layer to the map
//        tiledMap.getLayers().add(layer);
//
//        // Create a renderer for the tiled map
//        TiledMapRenderer renderer = createRenderer(tiledMap, 1f); // 1f scale used for background
//
//        // Adjust camera settings to fit the background
//        camera.setToOrtho(false, screenSize.x, screenSize.y);
//        camera.position.set(screenSize.x / 2f, screenSize.y / 2f, 0);
//
//        // Return the TerrainComponent with the background image rendering setup
//        return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f, MapType.COMBAT); // 1f scale used for background
//    }

    public TerrainComponent createBackgroundTerrainWater(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 screenSize) {
        // Initialize ResourceService and load the background texture
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion backgroundTextureRegion;
        float backgroundWidth;
        float backgroundHeight;

        // Determine the background image based on terrain type
        if (terrainType.equals(TerrainType.FOREST_DEMO)) {
            // backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
            backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background.png", Texture.class));
        } else {
            return null;
        }

        // Get the size of the background texture
        backgroundWidth = backgroundTextureRegion.getRegionWidth();
        backgroundHeight = backgroundTextureRegion.getRegionHeight();

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

        // Return the TerrainComponent with the background image rendering setup
        return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f, MapType.COMBAT); // 1f scale used for background
    }

    /**
     * Creates tiled terrain area
     * @param tiledMap
     * @param tileScale size of map
     * @return new tiled and rendered map
     */
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
