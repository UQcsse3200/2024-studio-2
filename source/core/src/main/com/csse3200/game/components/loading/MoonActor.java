package com.csse3200.game.components.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An actor representing the moon, which fills up as the loading progresses.
 */
public class MoonActor extends Actor {
    private final Texture moonTexture;
    private float progress;
    private float opacity = 0.5f;  // Default opacity (set to 0.5 for blending)

    public MoonActor() {
        moonTexture = new Texture("images/animal/moon.png");  // Load the moon texture
        setSize(300, 300);  // Fixed size for the moon
        // Position the moon to be centered on the screen
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        setPosition((screenWidth - getWidth()) / 2, (screenHeight - getHeight()) / 2);
    }

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
     * Set the moon's progress (from 0 to 1) to adjust how much of the moon is visible.
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
    /**
     * Set the opacity for the moon. Values should be between 0 (completely transparent) and 1 (fully opaque).
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void dispose() {
        moonTexture.dispose();  // Dispose of the moon texture when done
    }
}
