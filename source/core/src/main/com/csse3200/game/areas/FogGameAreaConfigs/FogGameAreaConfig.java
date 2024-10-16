package com.csse3200.game.areas.FogGameAreaConfigs;

import com.csse3200.game.files.FileLoader;

public class FogGameAreaConfig {
    public TexturesConfig textures;

    public FogGameAreaConfig() {
        textures = FileLoader.readClass(TexturesConfig.class,
                "configs/FogGameAreaConfigs/textures.json");
    }
}
