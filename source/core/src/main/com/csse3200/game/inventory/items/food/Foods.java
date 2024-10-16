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
            this.setDescription("This is an apple. Increases Hunger by 2.");
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
            this.setDescription("This is a chicken leg. Increases Hunger by 7.");
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
            this.setDescription("This is meat. Increases Hunger by 7.");
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
            this.setDescription("This is candy. Increases Hunger by 20.");
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
            this.setDescription("This is a carrot. Increases Hunger by 3.");
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
            this.setDescription("This is milk. Increases Hunger by 20.");
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
            this.setDescription("This is Sushi. Increases Hunger by 40.");
        }
    }

    /**
     * FriedFish class manages the fried fish fields inherited from AbstractFood
     */
    public static class FriedFish extends AbstractFood {
        private static final String PATH = "images/foodtextures/oceanfoodtextures/fried-fish.png";

        /**
         * Constructs a FriedFish class with while assigning fields with set values.
         */
        public FriedFish(int quantity) {
            super("FriedFish", 18, 1, quantity, 10);
            this.setTexturePath(PATH);
            this.setDescription("This is FriedFish");
        }
    }

    /**
     * Shrimp class manages the shrimp fields inherited from AbstractFood
     */
    public static class Shrimp extends AbstractFood {
        private static final String PATH = "images/foodtextures/oceanfoodtextures/shrimp.png";

        /**
         * Constructs a Sushi class with while assigning fields with set values.
         */
        public Shrimp(int quantity) {
            super("Shrimp", 19, 1, quantity, 10);
            this.setTexturePath(PATH);
            this.setDescription("This is Shrimp");
        }
    }

    /**
     * CloudCookie class manages the cloud cookies fields inherited from AbstractFood
     */
    public static class CloudCookie extends AbstractFood {
        private static final String PATH = "images/foodtextures/airfoodtextures/cloud-cookie.png";

        /**
         * Constructs a Sushi class with while assigning fields with set values.
         */
        public CloudCookie(int quantity) {
            super("CloudCookie", 10, 1, quantity, 10);
            this.setTexturePath(PATH);
            this.setDescription("This is CloudCookie");
        }
    }

    /**
     * CloudCupcake class manages the cloud cookies fields inherited from AbstractFood
     */
    public static class CloudCupcake extends AbstractFood {
        private static final String PATH = "images/foodtextures/airfoodtextures/cloudCupcake.png";

        /**
         * Constructs a Sushi class with while assigning fields with set values.
         */
        public CloudCupcake(int quantity) {
            super("CloudCupcake", 21, 1, quantity, 20);
            this.setTexturePath(PATH);
            this.setDescription("This is CloudCupcake");
        }
    }

    /**
     * CottonCloud class manages the cloud cookies fields inherited from AbstractFood
     */
    public static class CottonCloud extends AbstractFood {
        private static final String PATH = "images/foodtextures/airfoodtextures/cotton-cloud.png";

        /**
         * Constructs a Sushi class with while assigning fields with set values.
         */
        public CottonCloud(int quantity) {
            super("CottonCloud", 22, 1, quantity, 10);
            this.setTexturePath(PATH);
            this.setDescription("This is CottonCloud");
        }
    }






}
