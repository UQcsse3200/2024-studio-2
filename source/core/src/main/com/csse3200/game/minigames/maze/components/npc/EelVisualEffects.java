package com.csse3200.game.minigames.maze.components.npc;

import box2dLight.Light;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.components.Component;
import com.csse3200.game.minigames.maze.components.ParticleEffectComponent;

public class EelVisualEffects extends Component {
    Light light;
    ParticleEffectComponent particleEffect;

    public EelVisualEffects(Light light) {
        this.light = light;
        light.setActive(false);
    }

    @Override
    public void create() {
        particleEffect = entity.getComponent(ParticleEffectComponent.class);
        entity.getEvents().addListener("wanderStart", this::stopEffect);
        entity.getEvents().addListener("chaseStart", this::startEffect);
        particleEffect.stopEmitting();
    }

    private void startEffect() {
        light.setActive(true);
        particleEffect.startEmitting();
    }

    private void stopEffect() {
        light.setActive(false);
        particleEffect.stopEmitting();
    }

    @Override
    public void update() {
        light.setDistance(MathUtils.random() * .2f + .3f);
    }
}
