package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.*;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * Pipe render for birdie dash mini-game
 */
public class PipeRenderer implements MinigameRenderable {

    private Texture obstacleTexture;
    private TextureRegion pipeHead;
    private TextureRegion pipeBody;
    private final List<Pipe> pipes;
    private final MinigameRenderer renderer;
    private final float pipeHeadHeight = 200;
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
            // Draw bottom pipe
            renderer.getSb().draw(pipeHead,
                    pipe.getPositionBottom().x - pipe.getWidth()/2,
                    pipe.getPositionBottom().y + pipe.getHeightBottom() - pipeHeadHeight,
                    pipe.getWidth(),
                    pipeHeadHeight); // Render head without stretching

            renderer.getSb().draw(pipeBody,
                    pipe.getPositionBottom().x - pipe.getWidth()/2,
                    pipe.getPositionBottom().y,
                    pipe.getWidth(),
                    pipe.getHeightBottom() - pipeHeadHeight); // Stretch the body

            pipeHead.flip(false, true);
            // Draw top pipe and flip the head
            renderer.getSb().draw(pipeHead,
                    pipe.getPositionTop().x - pipe.getWidth()/2,
                    pipe.getPositionTop().y,
                    pipe.getWidth(),
                    pipeHeadHeight); // Render head without stretching

            renderer.getSb().draw(pipeBody,
                    pipe.getPositionTop().x - pipe.getWidth()/2,
                    pipe.getPositionTop().y + pipeHeadHeight,
                    pipe.getWidth(),
                    pipe.getHeightTop() - pipeHeadHeight); // Stretch the body

            // flip the head back
            pipeHead.flip(false, true);
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
        pipeHead = new TextureRegion(obstacleTexture, 0, 0, 32,40);
        pipeBody = new TextureRegion(obstacleTexture, 0, 20, 32,30);
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
        pipeBody.getTexture().dispose();
        pipeHead.getTexture().dispose();
    }
}
