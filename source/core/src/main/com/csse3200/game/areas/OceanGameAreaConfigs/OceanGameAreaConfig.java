package com.csse3200.game.areas.OceanGameAreaConfigs;

import com.csse3200.game.areas.ForestGameAreaConfigs.EntitySpawnConfig;
import com.csse3200.game.files.FileLoader;

public class OceanGameAreaConfig {public SoundsConfig sounds;
    public EntitySpawnConfig spawns;
    public TexturesConfig textures;
    public TexturesConfig spawnsUnlocked;

    public OceanGameAreaConfig() {
        textures = FileLoader.readClass(TexturesConfig.class,
                "configs/OceanGameAreaConfigs/textures.json");
        spawns = FileLoader.readClass(EntitySpawnConfig.class,
                "configs/OceanGameAreaConfigs/entitySpawn.json");
        spawnsUnlocked = FileLoader.readClass(TexturesConfig.class,
                "configs/OceanGameAreaConfigs/texturesLocked.json");
        sounds = FileLoader.readClass(SoundsConfig.class,
                "configs/OceanGameAreaConfigs/sounds.json");
    }
}
