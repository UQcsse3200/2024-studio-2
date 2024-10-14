package com.csse3200.game.areas.combat;

/**
 * This is a config class for the CombatArea.
 * All member fields have package access (this can be changed in future if necessary).
 * This exists merely as a storage for resources related to the CombatArea
 */
public class CombatAreaConfig {
    static final String[] combatTexture = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/final_boss_kangaroo_idle.png",
            "images/friendly_npcs/friendly-npcs.png",
            "images/water_boss_idle.png",
            "images/air_boss_idle.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/gt.png",
            "images/top_left_grass.png",
            "images/top_middle_grass.png",
            "images/top_right_grass.png",
            "images/middle_left_grass.png",
            "images/middle_grass.png",
            "images/middle_right_grass.png",
            "images/lower_left_grass.png",
            "images/lower_middle_grass.png",
            "images/lower_right_grass.png",
            "images/full_sand_tile.png",
            "images/bird.png",
            "images/Healthpotion.png",
            "images/foodtextures/apple.png",
            "images/combat_background_one.png",
            "images/combat_background.png",
            "images/chicken_idle.png",
            "images/bear_idle.png",
            "images/bee_idle.png",
            "images/eel_idle.png",
            "images/pigeon_idle.png",
            "images/monkey_idle.png",
            "images/frog_idle.png",
            "images/octopus_idle.png",
            "images/bear_idle.png",
            "images/macaw_idle.png",
            "images/bigsawfish_idle.png",
            "images/bee.png",
            "images/eel.png",
            "images/pigeon.png",
            "images/monkey.png",
            "images/macaw.png",
            "images/bigsawfish.png",
            "images/joey_idle.png",
            "images/dog.png",
            "images/croc.png",
            "images/bird.png",
            "images/zzz.png",
            "images/shield.png",
            "images/shield_flipped.png",
            "images/single_fireball.png",
            "images/flipped_fireball.png",
            "images/enemy-chicken.png",
            "images/frog.png",
            "images/bear.png",
            "images/air_background.png",
            "images/land_background.png",
            "images/water_background.png",
            "images/rock.png",
    };

    static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/chicken.atlas", "images/frog.atlas", "images/octopus.atlas",
            "images/monkey.atlas", "images/friendly_npcs/Cow.atlas", "images/snake.atlas", "images/friendly_npcs/lion.atlas",
            "images/eagle.atlas", "images/turtle.atlas", "images/final_boss_kangaroo.atlas",
            "images/monkey.atlas", "images/Cow.atlas", "images/snake.atlas", "images/lion.atlas",
            "images/eagle.atlas", "images/turtle.atlas", "images/final_boss_kangaroo.atlas",
            "images/water_boss.atlas", "images/air_boss.atlas", "images/joey.atlas",
            "images/bigsawfish.atlas", "images/macaw.atlas","images/enemy-chicken.atlas",
            "images/enemy-frog.atlas", "images/enemy-monkey.atlas", "images/bear.atlas", "images/bee.atlas",
            "images/bigsawfish.atlas", "images/macaw.atlas", "images/eel.atlas", "images/pigeon.atlas", "images/enemy-bear.atlas",
    };

    static final String[] questSounds = {"sounds/QuestComplete.wav"};
    static final String[] forestSounds = {"sounds/Impact4.ogg"};
    static final String HEARTBEAT = "sounds/heartbeat.mp3";
    static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
    static final String[] forestMusic = {BACKGROUND_MUSIC};
    static final String[] combatBackgroundMusic = {"sounds/combat_track1.mp3"};
    static final String COMBATBACKGROUND_MUSIC = "sounds/combat_track1.mp3";
    static final String[] combatSounds = {"sounds/combat/attack start.wav", "sounds/combat/attack hit.wav",
            "sounds/combat/attack blocked.wav", "sounds/combat/sleep.wav", "sounds/combat/guard.wav"};

    private CombatAreaConfig() {
        throw new IllegalArgumentException("Do not instantiate static util class!");
    }
}
