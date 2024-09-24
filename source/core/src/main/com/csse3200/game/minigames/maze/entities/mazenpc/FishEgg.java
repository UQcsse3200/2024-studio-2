/*package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.lighting.components.LightingComponent;
//import com.csse3200.game.minigames.maze.entities.MazeEntity;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.services.ServiceLocator;

 // FishEgg represents an entity in the maze minigame that can be collected or interacted with.
 // It does not actively chase or attack the player.

public class FishEgg extends MazeEntity {
    private Texture fishEggTexture;
    private float scaleX; // Scale in the X direction
    private float scaleY; // Scale in the Y direction


     // Constructs a FishEgg entity with the given configuration.

    public FishEgg() {
        // Load the PNG image as a Texture
        fishEggTexture = ServiceLocator.getResourceService().getAsset("images/minigames/fishegg.png", Texture.class);

        // Add lighting component
        this.addComponent(new LightingComponent().attach(LightingComponent.createPointLight(1f, Color.YELLOW)));

        // Initialize scale
        this.scaleX = 0.5f; // Adjust size of FishEgg in X direction
        this.scaleY = 0.5f; // Adjust size of FishEgg in Y direction
        PhysicsUtils.setScaledCollider(this, scaleX, scaleY); // Adjust collider size
    }


     // Renders the FishEgg texture.

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(fishEggTexture, getPosition().x, getPosition().y,
                fishEggTexture.getWidth() * scaleX,
                fishEggTexture.getHeight() * scaleY);
    }

    @Override
    public void dispose() {
        fishEggTexture.dispose(); // Dispose of the texture when no longer needed
    }

    // Optionally, you can add methods to get the scale if needed
    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }
}
*/

