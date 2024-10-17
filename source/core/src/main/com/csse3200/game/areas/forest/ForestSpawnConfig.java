package com.csse3200.game.areas.forest;

public class ForestSpawnConfig {
    // Map Obstacles
    public static final int NUM_TREES = 7;
    public static final int NUM_CLOUDS = 7;
    public static final int NUM_SEAWEED = 7;
    public static final int NUM_STARFISH = 7;

    // Items
    public static final int NUM_APPLES = 2;
    public static final int NUM_CARROTS = 3;
    public static final int NUM_CHICKEN_LEGS = 2;
    public static final int NUM_MEAT = 2;
    public static final int NUM_CANDY = 2;
    public static final int NUM_HEALTH_POTIONS = 2;
    public static final int NUM_DEFENSE_POTIONS = 2;
    public static final int NUM_ATTACK_POTIONS = 2;
    public static final int NUM_SPEED_POTIONS = 4;

    // Enemies
    public static final int NUM_CHICKENS = 10;
    public static final int NUM_FROGS = 6;
    public static final int NUM_MONKEYS = 7;
    public static final int NUM_BEARS = 3;
    public static final int NUM_PIGEONS = 5;
    public static final int NUM_BEES = 10;
    public static final int NUM_EELS = 4;
    public static final int NUM_OCTOPUS = 5;
    public static final int NUM_BIGSAWFISH = 5;
    public static final int NUM_MACAW = 5;
    public static final int NUM_HIVES = 3;

    // NPCs
    public static final int NUM_COWS = 5;
    public static final int NUM_FISH = 5;
    public static final int NUM_LIONS = 5;
    public static final int NUM_TURTLES = 5;
    public static final int NUM_EAGLES = 5;
    public static final int NUM_SNAKES = 5;
    public static final int NUM_MAGPIES = 5;
    public static final int NUM_FIREFLIES = 5;

    private ForestSpawnConfig() {
        throw new IllegalArgumentException("Do not instantiate static util class!");
    }
}
