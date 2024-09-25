package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.tasks.MazeChaseTask;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;


public class ElectricEel extends MazeEntity {

    public ElectricEel(Entity target, MazeEntityConfig config) {

        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                .addTask(new MazeChaseTask(target, 10, 2f, 3f, null));


        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/eels.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(.5f, new Color(1f, .85f, .7f, 1f))))
                .addComponent(aiComponent);

        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
        this.setScale(.3f, .3f);
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.8f, 0.25f);
    }
}

