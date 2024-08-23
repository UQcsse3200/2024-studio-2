package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * Carrot class manages the apple fields inherited from AbstractFood
 */
public class Carrot extends AbstractFood {
    protected Texture carrotTexture;
    private final static String path = "images/foodTexture/carrot.png";

    /**
     * Constructs a Carrot class with while assigning fields with set values.
     */
    public Carrot(Texture foodTexture) {
        super("Carrot", 6, 10, 5, new FeedEffect(3));
        this.setTexture(path);
    }
}