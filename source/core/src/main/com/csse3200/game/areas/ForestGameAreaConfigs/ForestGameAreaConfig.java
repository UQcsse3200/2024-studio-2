package com.csse3200.game.areas.ForestGameAreaConfigs;

import com.csse3200.game.files.FileLoader;

public class ForestGameAreaConfig {
    public SoundsConfig sounds;
    public EntitySpawnConfig spawns;
    public TexturesConfig textures;

    public ForestGameAreaConfig() {
        textures = FileLoader.readClass(TexturesConfig.class,
                "configs/ForestGameAreaConfigs/textures.json");
        spawns = FileLoader.readClass(EntitySpawnConfig.class,
                "configs/ForestGameAreaConfigs/entitySpawn.json");
        sounds = FileLoader.readClass(SoundsConfig.class,
                "configs/ForestGameAreaConfigs/sounds.json");
    }
}
