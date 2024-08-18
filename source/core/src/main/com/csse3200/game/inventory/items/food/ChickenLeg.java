package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * ChickenLeg class manages the apple fields inherited from AbstractFood
 */
public class ChickenLeg extends AbstractFood {
    protected Texture meatTexture;

    /**
     * Constructs a ChickenLeg class with while assigning fields with set values.
     */
    public ChickenLeg(Texture foodTexture) {
        super("Chicken Leg", 9, 10, 3, new FeedEffect(7));
        this.meatTexture = new Texture("food/foodTexture/chicken_leg.png");
    }
}
