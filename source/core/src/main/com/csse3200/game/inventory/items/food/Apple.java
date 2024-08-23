package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * Apple class manages the apple fields inherited from AbstractFood
 */
public class Apple extends AbstractFood {
    protected Texture appleTexture;
    private final static String path = "images/foodTexture/apple.png";

    /**
     * Constructs an Apple class with while assigning fields with set values.
     */
    public Apple(Texture foodTexture) {
        super("Apple", 5, 10, 5, new FeedEffect(2));
        this.setTexture(path);
    }
}
