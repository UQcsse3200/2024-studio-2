package com.csse3200.game.areas.ForestGameAreaConfigs;

import com.csse3200.game.files.FileLoader;

public class ForestGameAreaConfig {
    public ForestTexturesConfig textures;

    public ForestGameAreaConfig() {
        textures = FileLoader.readClass(ForestTexturesConfig.class,
                "configs/ForestGameAreaConfigs/textures.json");
    }
}
