package com.csse3200.game.minigames.birdieDash.rendering;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdieDash.entities.Bird;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class for rendering the bird in the bordie dash mini-game
 */
public class BirdRenderer implements MinigameRenderable {

    private final Bird bird;
    private final SpriteBatch spriteBatch;
    private Texture birdTexture;

    public BirdRenderer(Bird bird, MinigameRenderer renderer) {
        this.bird = bird;
        this.spriteBatch = renderer.getSb();
        loadAssets();
    }

    /**
     * renders the bird
     */
    @Override
    public void render() {

        spriteBatch.draw(birdTexture,
                bird.getPosition().x,
                bird.getPosition().y,
                bird.getBoundingBox().getWidth(),
                bird.getBoundingBox().getHeight());
    }

    /**
     * laods the assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.BIRD});
        rs.loadAll();
        birdTexture = rs.getAsset(AssetPaths.BIRD, Texture.class);
    }

    /**
     * unloads assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.BIRD});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        if (birdTexture != null) {
            birdTexture.dispose();
        }
    }
}