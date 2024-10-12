package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.Background;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Rendering for the background of birdie dash mini-game
 */
public class BackgroundRenderer implements MinigameRenderable {

    private Texture backGroundTexture;
    private TextureRegion backgroundRegion;
    private final MinigameRenderer renderer;
    private final float GAME_WIDTH = 1920;
    private final float GAME_HEIGHT = 1200;
    Background background;

    public BackgroundRenderer(Background background, MinigameRenderer renderer) {
        this.renderer = renderer;
        loadAssets();
        this.background = background;
    }

    /**
     * render the background
     */
    public void render() {
        renderer.getSb().draw(backgroundRegion,
                background.xBG1,
                0,
                GAME_WIDTH,
                GAME_HEIGHT);
        renderer.getSb().draw(backgroundRegion,
                background.xBG2,
                0,
                GAME_WIDTH,
                GAME_HEIGHT);
    }

    /**
     * Loads assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.BACKGROUND});
        ServiceLocator.getResourceService().loadAll();
        backGroundTexture = rs.getAsset(AssetPaths.BACKGROUND, Texture.class);
        // Region is used to get the particular part of the background desired
        // The last two numbers, e.g. 900 and 430 mean i only want the parts of the image in
        // 900x430 from the background image.
        backgroundRegion = new TextureRegion(backGroundTexture,
                0,
                0,
                900,
                420);
    }

    /**
     * unload assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.BACKGROUND});
    }

    /**
     * dispose assets
     */
    public void dispose() {
        unloadAssets();
        backGroundTexture.dispose();
    }
}
