package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * TO DO
 */
public class Apple extends AbstractFood {
    protected Texture appleTexture;

    /**
     * Apple constructor
     */
    public Apple(Texture foodTexture) {
        super("Apple", 5, 10, 5, new FeedEffect(2));
        this.appleTexture = new Texture("food/foodTexture/apple.png");
    }
}
