package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

public class Jellyfish extends MazeEntity {

    public Jellyfish(MazeEntityConfig config) {
        // Add AI tasks specific to Jellyfish (no chasing, only wandering)
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false));

        // Add animations specific to Jellyfish in the future please
        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/Angler.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addSound("sounds/minigames/angler-chomp.mp3", "Attack", 4);

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(1f, Color.BLUE)))
                .addComponent(aiComponent);

        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
        this.setScale(.2f, .2f);
        PhysicsUtils.setScaledCollider(this, 1f, 1f);
    }
}

