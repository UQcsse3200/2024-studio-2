package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

public class Foods {
    /**
     * Apple class manages the apple fields inherited from AbstractFood
     */
    public static class Apple extends AbstractFood {
        protected Texture appleTexture;
        private final static String path = "images/foodtextures/apple.png";

        /**
         * Constructs an Apple class  while assigning fields with set values.
         */
        public Apple(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity, new FeedEffect(2));
            this.setTexturePath(path);
            this.setDescription("This is an apple, press P to pick up");
        }
    }

    /**
     * ChickenLeg class manages the apple fields inherited from AbstractFood
     */
    public static class ChickenLeg extends AbstractFood {
        protected Texture meatTexture;
        private final static String path = "images/foodtexture/chicken_leg.png";

        /**
         * Constructs a ChickenLeg class with while assigning fields with set values.
         */
        public ChickenLeg(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity, new FeedEffect(7));
            this.setTexturePath(path);
            this.setDescription("This is an chicken leg");
        }
    }

    /**
     * Meat class manages the apple fields inherited from AbstractFood
     */
    public static class Meat extends AbstractFood {
        protected Texture meatTexture;
        private final static String path = "images/foodtexture/meat.png";

        /**
         * Constructs a Meat class with while assigning fields with set values.
         */
        public Meat(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity, new FeedEffect(7));
            this.setTexturePath(path);
            this.setDescription("This is meat");
        }
    }

    /**
     * Candy class manages the apple fields inherited from AbstractFood
     */
    public static class Candy extends AbstractFood {
        protected Texture candyTexture;
        private final static String path = "images/foodtexture/candy.png";

        /**
         * Constructs a Candy class  while assigning fields with set values.
         */
        public Candy(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity, new FeedEffect(10));
            this.setTexturePath(path);
            this.setDescription("This is candy");
        }
    }

    /**
     * Carrot class manages the apple fields inherited from AbstractFood
     */
    public static class Carrot extends AbstractFood {
        protected Texture carrotTexture;
        private final static String path = "images/foodtexture/carrot.png";

        /**
         * Constructs a Carrot class with while assigning fields with set values.
         */
        public Carrot(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity, new FeedEffect(3));
            this.setTexturePath(path);
            this.setDescription("This is a carrot");
        }
    }
}
