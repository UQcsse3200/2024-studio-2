package com.csse3200.game.components.minigames.birdieDash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.csse3200.game.components.minigames.MinigameRenderable;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;
import com.csse3200.game.components.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;


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
    public void render(){
        renderer.getSb().draw(spikeRegion,
                spike.getPosition().x,
                spike.getPosition().y,
                spike.getWidth(),
                spike.getHeight());
    }

    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.SPIKE});
        ServiceLocator.getResourceService().loadAll();
        spikeTexture = rs.getAsset(AssetPaths.SPIKE, Texture.class);
        spikeRegion = new TextureRegion(spikeTexture, 0, 0, 9, 100);
    }

    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.SPIKE});
    }

    public void dispose() {
        unloadAssets();
        spikeTexture.dispose();

    }
}
