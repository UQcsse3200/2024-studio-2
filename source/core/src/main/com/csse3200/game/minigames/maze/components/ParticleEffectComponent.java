package com.csse3200.game.minigames.maze.components;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ParticleEffectComponent extends RenderComponent {
    protected final ParticleEffect effect;
    public static final int PARTICLE_LAYER = 1;
    private float emissionLowMin;
    private float emissionLowMax;
    private float emissionHighMin;
    private float emissionHighMax;
    private Vector2 prevPosition;

    public ParticleEffectComponent(String effectPath) {
        this(ServiceLocator.getResourceService().getAsset(effectPath, ParticleEffect.class));
    }

    public ParticleEffectComponent(ParticleEffect effect) {
        super();
        effect.scaleEffect(0.02f); // reasonable scale from exporting defaults from libgdx particle editor
        this.effect = effect;
        ParticleEmitter emitter = effect.getEmitters().first();
        emissionHighMax = emitter.getEmission().getHighMax();
        emissionHighMin = emitter.getEmission().getHighMin();
        emissionLowMax = emitter.getEmission().getLowMax();
        emissionLowMin = emitter.getEmission().getLowMin();
        stopEmitting();
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public float getZIndex() {
        return super.getZIndex() - 1e6f; // TODO this should really be a layer below sprites, but above tiles.
    }
    @Override
    public int getLayer() {
        return PARTICLE_LAYER;
    }

    public void startEmitting() {
        Array<ParticleEmitter> emitterArray = effect.getEmitters();
        for (ParticleEmitter emitter: emitterArray) {
            emitter.getEmission().setLow(emissionLowMin, emissionLowMax);
            emitter.getEmission().setHigh(emissionHighMin, emissionHighMax);
        }
    }

    public void stopEmitting() {
        Array<ParticleEmitter> emitterArray = effect.getEmitters();
        for (ParticleEmitter emitter: emitterArray) {
            emitter.getEmission().setLow(0);
            emitter.getEmission().setHigh(0);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        if (!entity.getPosition().equals(prevPosition)) {
            prevPosition = entity.getPosition();
            startEmitting();
        } else {
            stopEmitting();
        }

        Vector2 position = entity.getCenterPosition();
        //Setting the position of the ParticleEffect
        effect.setPosition(position.x, position.y);
        //Updating and Drawing the particle effect
        if (effect.isComplete()) {
            effect.reset(); // TODO, make on complete customisable
        }
        effect.draw(batch, ServiceLocator.getTimeSource().getDeltaTime());
    }
}
