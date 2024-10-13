package com.csse3200.game.inventory.items.food;

public class Foods {
    private Foods() throws InstantiationException {
        throw new InstantiationException("Do not instantiate Foods class directly!");
    }

    /**
     * Apple class manages the apple fields inherited from AbstractFood
     */
    public static class Apple extends AbstractFood {
        private static final String PATH = "images/foodtextures/apple.png";

        /**
         * Constructs an Apple class while assigning fields with set values.
         */
        public Apple(int quantity) {
            super("Apple", 11, 1, quantity, 2);
            this.setTexturePath(PATH);
            this.setDescription("This is an apple. Increases Health by 2.");
        }
    }

    /**
     * ChickenLeg class manages the apple fields inherited from AbstractFood
     */
    public static class ChickenLeg extends AbstractFood {
        private static final String PATH = "images/foodtextures/chicken_leg.png";

        /**
         * Constructs a ChickenLeg class with while assigning fields with set values.
         */
        public ChickenLeg(int quantity) {
            super("Chicken_Leg", 12, 1, quantity, 7);
            this.setTexturePath(PATH);
            this.setDescription("This is a chicken leg. Increases Health by 7.");
        }
    }

    /**
     * Meat class manages the apple fields inherited from AbstractFood
     */
    public static class Meat extends AbstractFood {
        private static final String PATH = "images/foodtextures/meat.png";

        /**
         * Constructs a Meat class with while assigning fields with set values.
         */
        public Meat(int quantity) {
            super("Meat", 13, 1, quantity, 7);
            this.setTexturePath(PATH);
            this.setDescription("This is meat. Increases Health by 7.");
        }
    }

    /**
     * Candy class manages the apple fields inherited from AbstractFood
     */
    public static class Candy extends AbstractFood {
        private static final String PATH = "images/foodtextures/candy.png";

        /**
         * Constructs a Candy class  while assigning fields with set values.
         */
        public Candy(int quantity) {
            super("Candy", 14, 1, quantity, 10);
            this.setTexturePath(PATH);
            this.setDescription("This is candy. Increases Health by 20.");
        }
    }

    /**
     * Carrot class manages the apple fields inherited from AbstractFood
     */
    public static class Carrot extends AbstractFood {
        private static final String PATH = "images/foodtextures/carrot.png";

        /**
         * Constructs a Carrot class with while assigning fields with set values.
         */
        public Carrot(int quantity) {
            super("Carrot", 15, 1, quantity, 3);
            this.setTexturePath(PATH);
            this.setDescription("This is a carrot. Increases Health by 3.");
        }
    }

    /**
     * Milk class manages the milk fields inherited from AbstractFood
     */
    public static class Milk extends AbstractFood {
        private static final String PATH = "images/foodtextures/Milk.png";

        /**
         * Constructs a Milk class with while assigning fields with set values.
         */
        public Milk(int quantity) {
            super("Milk", 16, 1, quantity, 20);
            this.setTexturePath(PATH);
            this.setDescription("This is milk. Increases Health by 20.");
        }
    }

    /**
     * Sushi class manages the caviar fields inherited from AbstractFood
     */
    public static class Sushi extends AbstractFood {
        private static final String PATH = "images/foodtextures/Sushi.png";

        /**
         * Constructs a Sushi class with while assigning fields with set values.
         */
        public Sushi(int quantity) {
            super("Caviar", 17, 1, quantity, 40);
            this.setTexturePath(PATH);
            this.setDescription("This is Sushi. Increases Health by 40.");
        }
    }
}
