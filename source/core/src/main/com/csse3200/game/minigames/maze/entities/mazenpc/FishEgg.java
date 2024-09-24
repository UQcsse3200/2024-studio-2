package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

 // FishEgg represents an entity in the maze minigame that can be collected or interacted with.
 // It does not actively chase or attack the player.

public class FishEgg extends Entity {
    private Texture fishEggTexture;
    private float scaleX; // Scale in the X direction
    private float scaleY; // Scale in the Y direction


     // Constructs a FishEgg entity with the given configuration.

    public FishEgg() {
        // Load the PNG image as a Texture

        // Add lighting component
        this.addComponent(new LightingComponent().attach(LightingComponent.createPointLight(1f, Color.YELLOW)))
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new TextureRenderComponent("images/minigames/fishegg.png"));

        // Initialize scale
        this.scaleX = 0.3f; // Adjust size of FishEgg in X direction
        this.scaleY = 0.3f; // Adjust size of FishEgg in Y direction
        this.setScale(scaleX, scaleY);
    }

    // Optionally, you can add methods to get the scale if needed
    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }
}


