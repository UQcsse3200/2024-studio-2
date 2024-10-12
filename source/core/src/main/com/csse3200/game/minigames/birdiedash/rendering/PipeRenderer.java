package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.Pipe;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * Pipe render for birdie dash mini-game
 */
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

    /**
     * Render the pipes in the birdie dash mini-game
     */
    public void render(){
        for (Pipe pipe : this.pipes) {
            renderer.getSb().draw(obstacleTexture,
                    pipe.getPositionBottom().x - pipe.getWidth()/2,
                    pipe.getPositionBottom().y,
                    pipe.getWidth(),
                    pipe.getHeightBottom());

            renderer.getSb().draw(obstacleTexture,
                    pipe.getPositionTop().x - pipe.getWidth()/2,
                    pipe.getPositionTop().y,
                    pipe.getWidth(),
                    pipe.getHeightTop());
        }
    }

    /**
     * load assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.PIPE});
        ServiceLocator.getResourceService().loadAll();
        obstacleTexture = rs.getAsset(AssetPaths.PIPE, Texture.class);
    }

    /**
     * unload assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.PIPE});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        obstacleTexture.dispose();
    }
}
