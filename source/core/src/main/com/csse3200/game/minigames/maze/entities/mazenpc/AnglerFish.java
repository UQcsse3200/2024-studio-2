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
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * AnglerFish represents a non-playable character (NPC) in the maze minigame.
 * It is a hostile entity that chases the player and attacks it.
 */
public class AnglerFish extends MazeEntity {

    /**
     * Constructs an AnglerFish entity with the given target and configuration.
     *
     * @param target The entity that this AnglerFish will chase (e.g., the player).
     * @param config The configuration stats for this NPC, such as health and attack power.
     */
    public AnglerFish(Entity target, MazeEntityConfig config) {
        // Add AI tasks specific to AnglerFish
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                .addTask(new MazeChaseTask(target, 10, 2f, 3f));

        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/Angler.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addSound("sounds/minigames/angler-chomp.mp3", "Attack", 4);

        // Set other unique components for AnglerFish

        /* TODO difficulty scaling
        angler should get faster each fish egg that is collected.
        Also, the angler could change colours from
        Color.GREEN -> Color.YELLOW -> new Color(1f, 0.3f, 0.1f, 1f) -> Color.RED
        spawn in more entities over time as well?
        decrease sight radius over time?
         */

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(3f, Color.GREEN)))
                .addComponent(aiComponent);

        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
        this.setScale(.2f, .2f);
        PhysicsUtils.setScaledCollider(this, 1f, 1f);
    }
}
