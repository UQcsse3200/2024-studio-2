package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.Spike;
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
    public void render() {
        float amount = spike.getHeight() / 15; // The height of each spike segment

        // Iterate 20 times to draw 20 spikes
        for (int i = 0; i < 15; i++) {
            renderer.getSb().draw(spikeRegion,
                    spike.getPosition().x - 20,                      // x position (same for all
                    // spikes)
                    spike.getPosition().y + i * amount,         // y position increases with each spike
                    spike.getWidth() / 2,                       // x origin of rotation (center of the spike)
                    amount / 2,                                 // y origin of rotation (middle of the spike segment)
                    spike.getWidth(),                           // Width of the spike
                    amount + 22,                                     // Height of one spike segment
                    1,                                          // Scale x
                    1,                                          // Scale y
                    -90);                                        // Rotation angle (90 degrees)
        }
    }


    /**
     * Load assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.SPIKE});
        ServiceLocator.getResourceService().loadAll();
        spikeTexture = rs.getAsset(AssetPaths.SPIKE, Texture.class);
        spikeRegion = new TextureRegion(spikeTexture, 0, 2, 16, 14);
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
