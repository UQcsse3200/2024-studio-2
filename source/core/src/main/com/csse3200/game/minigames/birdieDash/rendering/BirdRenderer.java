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
    private boolean isFlapping; // To track if the bird is flapping
    private float flapTimeRemaining; // Time left for flapping
    private final float flapDuration = 0.45f; // Flap duration (1 seconds)

    public BirdRenderer(Bird bird, MinigameRenderer renderer) {
        this.bird = bird;
        this.spriteBatch = renderer.getSb();
        loadAssets();
        stateTime = 0f;
    }

    /**
     * To animate the bird when flapped
     */
    public void flap() {
        isFlapping = true;
        flapTimeRemaining = flapDuration;
        stateTime = 0f;
    }

    /**
     * Renders the bird with animation.
     */
    @Override
    public void render() {
        if (isFlapping) {
            flapTimeRemaining -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();

            // Update animation time while flapping
            stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

            // Stop flapping when time runs out
            if (flapTimeRemaining <= 0) {
                isFlapping = false;
                flapTimeRemaining = 0f;
            }
        }

        // Get the current frame (if flapping, use animation, otherwise, use idle frame)
        TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, false);

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
