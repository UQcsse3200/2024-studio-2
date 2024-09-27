package com.csse3200.game.minigames.maze.components.npc;

import box2dLight.Light;
import com.csse3200.game.components.Component;

public class EelLightingEffect extends Component {
    Light light;


    public EelLightingEffect(Light light) {
        this.light = light;
        light.setActive(false);
    }

    @Override
    public void create() {
        entity.getEvents().addListener("wanderStart", this::stopEffect);
        entity.getEvents().addListener("chaseStart", this::startEffect);
    }

    private void startEffect() {
        light.setActive(true);
    }

    private void stopEffect() {
        light.setActive(false);
    }

    @Override
    public void update() {
        light.setDistance((float) Math.random() * .2f + .3f);
    }
}
