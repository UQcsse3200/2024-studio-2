package com.csse3200.game.minigames.birdieDash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdieDash.entities.Spike;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Render for the spikes in the birdie dash mini-game
 */
public class SpikeRenderer implements MinigameRenderable {
    private final Spike spike;
    private final MinigameRenderer renderer;
    private Texture spikeTexture;
    private TextureRegion spikeRegion;

    public SpikeRenderer(Spike spike, MinigameRenderer renderer) {
        this.spike = spike;
        this.renderer = renderer;
        this.renderer.addRenderable(this);
        loadAssets();
    }

    /**
     * Render the spikes
     */
    public void render(){
        renderer.getSb().draw(spikeRegion,
                spike.getPosition().x,
                spike.getPosition().y,
                spike.getWidth(),
                spike.getHeight());
    }

    /**
     * Load assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.SPIKE});
        ServiceLocator.getResourceService().loadAll();
        spikeTexture = rs.getAsset(AssetPaths.SPIKE, Texture.class);
        spikeRegion = new TextureRegion(spikeTexture, 0, 0, 9, 100);
    }

    /**
     * unload assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.SPIKE});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        spikeTexture.dispose();
    }
}
