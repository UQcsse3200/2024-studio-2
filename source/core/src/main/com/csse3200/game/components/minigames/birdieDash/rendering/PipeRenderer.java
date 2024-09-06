package com.csse3200.game.components.minigames.birdieDash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.minigames.MinigameRenderable;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

// very rough class for now. will refactor later
public class PipeRenderer implements MinigameRenderable {

    private Texture obstacleTexture;
    private final List<Pipe> pipes;
    private final MinigameRenderer renderer;
    public PipeRenderer(List<Pipe> pipes, MinigameRenderer renderer) {
        this.pipes = pipes;
        this.renderer = renderer;
        this.renderer.addRenderable(this);
        loadAssets();
    }
    public void render(){
        for (Pipe pipe : this.pipes) {
            renderer.getSb().draw(obstacleTexture,
                    pipe.getPositionBottom().x,
                    pipe.getPositionBottom().y,
                    pipe.getWidth(),
                    pipe.getHeightBottom());

            renderer.getSb().draw(obstacleTexture,
                    pipe.getPositionTop().x,
                    pipe.getPositionTop().y,
                    pipe.getWidth(),
                    pipe.getHeightTop());
        }
    }

    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.PIPE});
        ServiceLocator.getResourceService().loadAll();
        obstacleTexture = rs.getAsset(AssetPaths.PIPE, Texture.class);
    }

    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.PIPE});
    }

    public void dispose() {
        unloadAssets();
        obstacleTexture.dispose();
    }
}
