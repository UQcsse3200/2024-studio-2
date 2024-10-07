package com.csse3200.game.areas.OceanGameAreaConfigs;

import com.csse3200.game.areas.forest.ForestSpawnConfig;
import com.csse3200.game.files.FileLoader;

public class OceanGameAreaConfig {public SoundsConfig sounds;
    public ForestSpawnConfig spawns;
    public TexturesConfig textures;

    public OceanGameAreaConfig() {
        textures = FileLoader.readClass(TexturesConfig.class,
                "configs/OceanGameAreaConfigs/textures.json");
        spawns = FileLoader.readClass(ForestSpawnConfig.class,
                "configs/OceanGameAreaConfigs/entitySpawn.json");
        sounds = FileLoader.readClass(SoundsConfig.class,
                "configs/OceanGameAreaConfigs/sounds.json");
    }
}
