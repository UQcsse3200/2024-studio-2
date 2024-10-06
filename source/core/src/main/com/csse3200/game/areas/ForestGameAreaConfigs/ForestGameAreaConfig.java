package com.csse3200.game.areas.ForestGameAreaConfigs;

import com.csse3200.game.files.FileLoader;

public class ForestGameAreaConfig {
    public SoundsConfig sounds;
    public TexturesConfig textures;

    public ForestGameAreaConfig() {
        textures = FileLoader.readClass(TexturesConfig.class,
                "configs/ForestGameAreaConfigs/textures.json");
        sounds = FileLoader.readClass(SoundsConfig.class,
                "configs/ForestGameAreaConfigs/sounds.json");
    }
}
