package com.csse3200.game.inventory.items.food;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;

public class Foods {
    /**
     * Apple class manages the apple fields inherited from AbstractFood
     */
    public static class Apple extends AbstractFood {
        protected Texture appleTexture;
        private final static String path = "images/foodTexture/apple.png";

        /**
         * Constructs an Apple class  while assigning fields with set values.
         */
        public Apple(Texture foodTexture) {
            super("Apple", 5, 10, 5, new FeedEffect(2));
            this.setTexture(path);
        }
    }

    /**
     * ChickenLeg class manages the apple fields inherited from AbstractFood
     */
    public static class ChickenLeg extends AbstractFood {
        protected Texture meatTexture;
        private final static String path = "images/foodTexture/chicken_leg.png";

        /**
         * Constructs a ChickenLeg class with while assigning fields with set values.
         */
        public ChickenLeg(Texture foodTexture) {
            super("Chicken Leg", 9, 10, 3, new FeedEffect(7));
            this.setTexture(path);
        }
    }

    /**
     * Meat class manages the apple fields inherited from AbstractFood
     */
    public static class Meat extends AbstractFood {
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

    /**
     * Candy class manages the apple fields inherited from AbstractFood
     */
    public static class Candy extends AbstractFood {
        protected Texture candyTexture;
        private final static String path = "images/foodTexture/candy.png";

        /**
         * Constructs a Candy class  while assigning fields with set values.
         */
        public Candy(Texture foodTexture) {
            super("Candy", 7, 10, 1, new FeedEffect(10));
            this.setTexture(path);
        }
    }

    /**
     * Carrot class manages the apple fields inherited from AbstractFood
     */
    public static class Carrot extends AbstractFood {
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
}
