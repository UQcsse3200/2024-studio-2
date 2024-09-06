package com.csse3200.game.components.minigames.birdieDash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.components.minigames.MinigameRenderable;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class BackgroundRenderer implements MinigameRenderable {

    private Texture backGroundTexture;
    private TextureRegion backgroundRegion;
    private final MinigameRenderer renderer;
    private final float GAME_WIDTH = 1920;
    private final float GAME_HEIGHT = 1200;

    public BackgroundRenderer(MinigameRenderer renderer) {
        this.renderer = renderer;
        loadAssets();
    }

    public void render() {
        renderer.getSb().draw(backgroundRegion,
                0,
                0,
                GAME_WIDTH,
                GAME_HEIGHT);
    }

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

    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.BACKGROUND});
    }

    public void dispose() {
        unloadAssets();
        backGroundTexture.dispose();
    }
}
