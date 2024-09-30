package com.csse3200.game.components.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An actor representing the moon, which visually fills up as the loading progresses.
 * The moon is centered on the screen and its visibility changes based on the loading progress.
 */
public class MoonActor extends Actor {
    private final Texture moonTexture;  // The texture representing the moon image
    private float progress;  // Loading progress, ranges from 0 (empty) to 1 (full)
    private float opacity = 0.5f;  // Default opacity (transparency level)


    /**
     * Creates a new MoonActor, loading the moon texture and positioning the moon
     * in the center of the screen with a fixed size.
     */
    public MoonActor() {
        moonTexture = new Texture("images/animal/moon.png");  // Load the moon texture
        setSize(300, 300);  // Fixed size for the moon
        // Position the moon to be centered on the screen
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        setPosition((screenWidth - getWidth()) / 2, (screenHeight - getHeight()) / 2);
    }

    /**
     * Draws the moon on the screen, displaying only a portion of it based on the loading progress.
     * The moon becomes more visible as progress increases, drawn from the bottom up.
     *
     * @param batch The Batch used for drawing textures.
     * @param parentAlpha The parent alpha, which can be used to combine transparency.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Calculate the visible part of the moon based on progress
        float visibleHeight = getHeight() * progress;  // Height to draw based on progress

        // Set color with lower opacity for blending (white with a specified alpha)
        batch.setColor(1f, 1f, 1f, opacity);

        // Draw the visible portion of the moon, from bottom to top
        batch.draw(
                moonTexture,
                getX(), getY(),  // Destination position (static in the center)
                getWidth(), visibleHeight,  // Draw full width, but height changes with progress
                0, (int) (moonTexture.getHeight() * (1 - progress)),  // Source x,y: start at increasing y as progress increases
                moonTexture.getWidth(), (int) (moonTexture.getHeight() * progress),  // Source width and height
                false, false  // No flipping
        );
    }

    /**
     * Sets the loading progress for the moon, which determines how much of the moon is visible.
     * The value should be between 0 (no progress) and 1 (fully visible).
     *
     * @param progress The progress of the loading, ranging from 0 to 1.
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**
     * Updates the moon actor each frame.
     *
     * @param delta Time elapsed since the last frame.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Sets the opacity (transparency) of the moon.
     * Values should range from 0 (completely transparent) to 1 (fully opaque).
     *
     * @param opacity The opacity value for the moon, between 0 and 1.
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * Returns the current opacity of the moon.
     *
     * @return The opacity value of the moon, between 0 and 1.
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Returns the current loading progress of the moon.
     *
     * @return The loading progress, a value between 0 and 1.
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Disposes of the resources used by the MoonActor, specifically the moon texture.
     * This method should be called when the moon is no longer needed to free up memory.
     */
    public void dispose() {
        moonTexture.dispose();  // Dispose of the moon texture when done
    }
}
