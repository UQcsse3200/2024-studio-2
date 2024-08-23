package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * Meat class manages the apple fields inherited from AbstractFood
 */
public class Meat extends AbstractFood {
    protected Texture meatTexture;
    private final static String path = "images/foodTexture/meat.png";

    /**
     * Constructs a Meat class with while assigning fields with set values.
     */
    public Meat(Texture foodTexture) {
        super("Carrot", 8, 10, 3, new FeedEffect(7));
        this.setTexture(path);
    }
}