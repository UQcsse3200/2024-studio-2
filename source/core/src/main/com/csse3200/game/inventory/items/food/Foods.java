package com.csse3200.game.inventory.items.food;

import com.csse3200.game.components.CombatStatsComponent;

public class Foods {
    /**
     * Apple class manages the apple fields inherited from AbstractFood
     */
    public static class Apple extends AbstractFood {
        private final static String path = "images/foodtextures/apple.png";

        /**
         * Constructs an Apple class  while assigning fields with set values.
         */
        public Apple(int quantity, CombatStatsComponent stat) {
            super("Apple", 11, 1, quantity, 2, stat);
            this.setTexturePath(path);
            this.setDescription("This is an apple");
        }
    }

    /**
     * ChickenLeg class manages the apple fields inherited from AbstractFood
     */
    public static class ChickenLeg extends AbstractFood {
        private final static String path = "images/foodtextures/chicken_leg.png";

        /**
         * Constructs a ChickenLeg class with while assigning fields with set values.
         */
        public ChickenLeg(int quantity, CombatStatsComponent stat) {
            super("Chicken_Leg", 12, 1, quantity, 7, stat);
            this.setTexturePath(path);
            this.setDescription("This is a chicken leg");
        }
    }

    /**
     * Meat class manages the apple fields inherited from AbstractFood
     */
    public static class Meat extends AbstractFood {
        private final static String path = "images/foodtextures/meat.png";

        /**
         * Constructs a Meat class with while assigning fields with set values.
         */
        public Meat(int quantity, CombatStatsComponent stat) {
            super("Meat", 13, 1, quantity, 7, stat);
            this.setTexturePath(path);
            this.setDescription("This is meat");
        }
    }

    /**
     * Candy class manages the apple fields inherited from AbstractFood
     */
    public static class Candy extends AbstractFood {
        private final static String path = "images/foodtextures/candy.png";

        /**
         * Constructs a Candy class  while assigning fields with set values.
         */
        public Candy(int quantity, CombatStatsComponent stat) {
            super("Candy", 14, 1, quantity, 10, stat);
            this.setTexturePath(path);
            this.setDescription("This is candy");
        }
    }

    /**
     * Carrot class manages the apple fields inherited from AbstractFood
     */
    public static class Carrot extends AbstractFood {
        private final static String path = "images/foodtextures/carrot.png";

        /**
         * Constructs a Carrot class with while assigning fields with set values.
         */
        public Carrot(int quantity, CombatStatsComponent stat) {
            super("Carrot", 15, 1, quantity, 3, stat);
            this.setTexturePath(path);
            this.setDescription("This is a carrot");
        }
    }
}
