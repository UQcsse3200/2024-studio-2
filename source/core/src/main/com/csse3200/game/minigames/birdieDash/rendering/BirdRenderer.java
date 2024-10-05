package com.csse3200.game.minigames.birdieDash.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdieDash.entities.Bird;

/**
 * Class for rendering the bird with animation in the birdie dash mini-game.
 */
public class BirdRenderer implements MinigameRenderable {

    private final Bird bird;
    private final SpriteBatch spriteBatch;
    private Animation<TextureRegion> birdAnimation;
    private float stateTime; // To track animation time

    public BirdRenderer(Bird bird, MinigameRenderer renderer) {
        this.bird = bird;
        this.spriteBatch = renderer.getSb();
        loadAssets();
        stateTime = 0f;
    }

    /**
     * Renders the bird with animation.
     */
    @Override
    public void render() {
        stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame,
                bird.getPosition().x,
                bird.getPosition().y,
                bird.getBoundingBox().getWidth(),
                bird.getBoundingBox().getHeight());
    }

    /**
     * Loads the bird animation frames.
     */
    private void loadAssets() {
        TextureAtlas birdAtlas = new TextureAtlas("spriteSheets/BirdMain.atlas");
        Array<TextureRegion> birdTextures = new Array<>(3);
        for (int frameBird = 1; frameBird <= 3; frameBird++) {
           TextureRegion region = birdAtlas.findRegion("fly" + frameBird);
           region.flip(true, false);
           birdTextures.add(region);
        }
        birdAnimation = new Animation<>(0.1f, birdTextures, Animation.PlayMode.LOOP);
    }

    /**
     * Disposes the bird texture.
     */
    public void dispose() {
        // Did not need a dispose method for the animations
    }
}
