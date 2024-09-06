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
    private static final GridPoint2 MAP_SIZE = new GridPoint2(7000, 7000);
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

    /**
     * Create a terrain of the given type, using the orientation of the factory. This can be extended
     * to add additional game terrains.
     *
     * @param terrainType Terrain to create
     * @return Terrain component which renders the terrain
     */
    public TerrainComponent createTerrain(TerrainType terrainType) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        switch (terrainType) {
            case FOREST_DEMO:
                TextureRegion orthoGrass =
                        new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
                TextureRegion orthoTuft =
                        new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
                TextureRegion orthoRocks =
                        new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
                return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);
            case FOREST_DEMO_ISO:
                TextureRegion isoGrass =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
                TextureRegion isoTuft =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
                TextureRegion isoRocks =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
                return createForestDemoTerrain(1f, isoGrass, isoTuft, isoRocks);
            case FOREST_DEMO_HEX:
                TextureRegion hexGrass =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
                TextureRegion hexTuft =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
                TextureRegion hexRocks =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
                return createForestDemoTerrain(1f, hexGrass, hexTuft, hexRocks);
            default:
                return null;
        }
    }

    public TerrainComponent createFullTerrain(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 mapSize) {
        this.mapSize = mapSize;
        float tileWorldSize = 1.f;

        TiledMap tiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(this.mapSize.x, this.mapSize.y, 500, 500);
        tiledMap.getLayers().add(layer);

        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / 500);
        return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
    }

    public TerrainComponent createTiledTerrain(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 mapSize) {
        // Initialize ResourceService and variables for the terrain
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion grass, tuft, rocks;
        float tileWorldSize;

        // Determine the textures and scale based on terrain type
        switch (terrainType) {
            case FOREST_DEMO:
                grass = new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
                tuft = new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
                rocks = new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
                tileWorldSize = 0.5f; // Assuming tile size, adjust as necessary
                break;
            case FOREST_DEMO_ISO:
                grass = new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
                tuft = new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
                rocks = new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
                tileWorldSize = 1f;
                break;
            case FOREST_DEMO_HEX:
                grass = new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
                tuft = new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
                rocks = new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
                tileWorldSize = 1f;
                break;
            default:
                return null;
        }

        // Create a TiledMap with layers based on map size
        this.mapSize = mapSize;
        TiledMap tiledMap = new TiledMap();
        int tilePixelWidth = grass.getRegionWidth();  // Assuming all tiles have the same size
        int tilePixelHeight = grass.getRegionHeight();
        TiledMapTileLayer layer = new TiledMapTileLayer(this.mapSize.x, this.mapSize.y, tilePixelWidth, tilePixelHeight);

        // Fill the layer with terrain tiles
        for (int x = 0; x < mapSize.x; x++) { // changing to < 1 makes background disappear, keeps kanga boss
            // mapSize.x
            for (int y = 0; y < mapSize.y; y++) { // changing to < 1 makes background disappear, keeps kanga boss
                // Randomly pick a texture for variety (you can adjust logic as needed)
                TextureRegion region = (x + y) % 3 == 0 ? grass : ((x + y) % 3 == 1 ? tuft : rocks);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(region));
                layer.setCell(x, y, cell);
            }
        }

        // Add the layer to the map
        tiledMap.getLayers().add(layer);

        // Create a renderer for the tiled map
        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelWidth);

        // Check and adjust camera settings to ensure correct alignment
        camera.update();
        camera.position.set(mapSize.x * tileWorldSize / 2f, mapSize.y * tileWorldSize / 2f, 0);

//        // new code
//        table = new Table();
//        table.setFillParent(true);
//        // Import image.
//        Texture texture = ServiceLocator.getResourceService().getAsset("images/combat_background_one.png", Texture.class);
//        Image bg = new Image(texture);
//        Stage stage = ServiceLocator.getRenderService().getStage();
//        // Full stage.
//        bg.setSize(stage.getWidth(), stage.getHeight());
//        table.add(bg).expand().fill();
//        stage.addActor(table);
//        //

        // Return the fully configured TerrainComponent
        return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
    }

    public TerrainComponent createBackgroundTerrain(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 mapSize) {
        // Initialize ResourceService and load the background texture
        ResourceService resourceService = ServiceLocator.getResourceService();
        TextureRegion backgroundTextureRegion;
        float backgroundWidth, backgroundHeight;

        // Determine the background image based on terrain type
        switch (terrainType) {
            case FOREST_DEMO:
                backgroundTextureRegion = new TextureRegion(resourceService.getAsset("images/combat_background_one.png", Texture.class));
                break;
            default:
                return null;
        }

        // Get the size of the background texture
        backgroundWidth = backgroundTextureRegion.getRegionWidth();
        backgroundHeight = backgroundTextureRegion.getRegionHeight();

        // Ensure the background texture covers the map size
        // Create a TiledMap with a single layer and one tile that covers the entire map
        this.mapSize = mapSize;
        TiledMap tiledMap = new TiledMap();
        int tilePixelWidth = backgroundTextureRegion.getRegionWidth();  // Assuming all tiles have the same size
        int tilePixelHeight = backgroundTextureRegion.getRegionHeight();
        TiledMapTileLayer layer = new TiledMapTileLayer(mapSize.x, mapSize.y, (int)backgroundWidth, (int)backgroundHeight);

        // Create a single large tile from the background texture
        TiledMapTile backgroundTile = new StaticTiledMapTile(backgroundTextureRegion);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(backgroundTile);

        // Fill the entire layer with the single large tile
        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < 1; y++) {
                layer.setCell(x, y, cell);
            }
        }

        // Add the layer to the map
        tiledMap.getLayers().add(layer);

        // Create a renderer for the tiled map
        TiledMapRenderer renderer = createRenderer(tiledMap, backgroundWidth / (int)backgroundWidth);

        // Adjust camera settings to fit the background
        camera.update();
        camera.position.set(mapSize.x / 2f, mapSize.y / 2f, 0);

        // Return the TerrainComponent with the background image rendering setup
        return new TerrainComponent(camera, tiledMap, renderer, orientation, backgroundWidth);
    }



    private TerrainComponent createForestDemoTerrain(
            float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
        GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
        TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
        return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
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
