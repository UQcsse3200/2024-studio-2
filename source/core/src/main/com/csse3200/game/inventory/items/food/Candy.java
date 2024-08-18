package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

/**
 * Candy class manages the apple fields inherited from AbstractFood
 */
public class Candy extends AbstractFood {
    protected Texture candyTexture;

    /**
     * Constructs a Candy class with while assigning fields with set values.
     */
    public Candy(Texture foodTexture) {
        super("Candy", 7, 10, 1, new FeedEffect(10));
        this.candyTexture = new Texture("food/foodTexture/candy.png");
    }
}