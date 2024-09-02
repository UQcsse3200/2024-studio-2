package com.csse3200.game.components.minigame.flappybird.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.minigame.MinigameRenderable;
import com.csse3200.game.components.minigame.MinigameRenderer;
import com.csse3200.game.components.minigame.flappybird.entities.Obstacle;
import com.csse3200.game.components.minigame.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class ObstacleRenderer implements MinigameRenderable {

    private Texture obstacleTexture;
    private final Obstacle obstacle;
    private final MinigameRenderer renderer;
    public ObstacleRenderer(Obstacle obstacle, MinigameRenderer renderer) {
        this.obstacle = obstacle;
        this.renderer = renderer;
        this.renderer.addRenderable(this);
        loadAssets();
    }
    public void render(){
        renderer.getSb().draw(obstacleTexture,
                obstacle.getPositionBottom().x,
                obstacle.getPositionBottom().y,
                obstacle.getWidth(),
                obstacle.getHeightBottom());

        renderer.getSb().draw(obstacleTexture,
                obstacle.getPositionTop().x,
                obstacle.getPositionTop().y,
                obstacle.getWidth(),
                obstacle.getHeightTop());
    }

    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.SNAKE_BODY_VERTICAL_IMAGE});
        ServiceLocator.getResourceService().loadAll();
        obstacleTexture = rs.getAsset(AssetPaths.SNAKE_BODY_VERTICAL_IMAGE, Texture.class);
    }

    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.SNAKE_BODY_VERTICAL_IMAGE});
    }

    public void dispose() {
        unloadAssets();
        obstacleTexture.dispose();
    }
}
