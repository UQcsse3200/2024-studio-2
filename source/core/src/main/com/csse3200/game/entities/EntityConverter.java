package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.PauseTask;
import com.csse3200.game.components.tasks.AvoidTask;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class EntityConverter {
	
	public static void convertToFriendly(Entity enemy, Entity player, List<Entity> enemies) {
		// Remove enemy-specific components
		enemy.removeComponent(TouchAttackComponent.class);
		enemy.removeComponent(CombatStatsComponent.class);
		
		// Ensure the entity has an AnimationRenderComponent
		AnimationRenderComponent animator = enemy.getComponent(AnimationRenderComponent.class);
		if (animator == null) {
			System.err.println("Error: Entity does not have an AnimationRenderComponent. Cannot convert to friendly.");
			return;
		}
		
		// Determine the config based on the enemy's current animation
		BaseEntityConfig config = determineConfig(animator);
		
		// Update AI behavior
		updateAIBehavior(enemy, player, enemies);
		
		// Update animation
		updateAnimation(animator, config);
		
		// Update components
		updateComponents(enemy, config);
		
		// Adjust movement speed
		adjustMovementSpeed(enemy);
		
		// Add sound effects
		addSoundEffects(enemy, config);
		
		// Trigger conversion event
		enemy.getEvents().trigger("onConvertToFriendly");
	}
	
	private static BaseEntityConfig determineConfig(AnimationRenderComponent animator) {
		String currentAnimation = animator.getCurrentAnimation();
		if (currentAnimation != null && currentAnimation.contains("chicken")) {
			return NPCConfigs.chicken;
		} else if (currentAnimation != null && currentAnimation.contains("frog")) {
			return NPCConfigs.frog;
		} else if (currentAnimation != null && currentAnimation.contains("monkey")) {
			return NPCConfigs.monkey;
		} else {
			return NPCConfigs.chicken; // Default to chicken if type can't be determined
		}
	}
	
	private static void updateAIBehavior(Entity enemy, Entity player, List<Entity> enemies) {
		enemy.removeComponent(AITaskComponent.class);
		
		AITaskComponent aiComponent = new AITaskComponent()
				.addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
				.addTask(new PauseTask(player, 10, 2f, 1f, false));
		
		for (Entity otherEnemy : enemies) {
			aiComponent.addTask(new AvoidTask(otherEnemy, 10, 3f, 3f, false));
		}
		
		enemy.addComponent(aiComponent);
	}
	
	private static void updateAnimation(AnimationRenderComponent animator, BaseEntityConfig config) {
		animator.stopAnimation();
		animator.addAnimation("float", config.getAnimationSpeed(), Animation.PlayMode.LOOP);
		animator.startAnimation("float");
		animator.scaleEntity();
	}
	
	private static void updateComponents(Entity enemy, BaseEntityConfig config) {
		enemy.addComponent(new CombatStatsComponent(config.health, 0, 0, 0, 0, 0))
				.addComponent(new FriendlyNPCAnimationController());
	}
	
	private static void adjustMovementSpeed(Entity enemy) {
		PhysicsMovementComponent movement = enemy.getComponent(PhysicsMovementComponent.class);
		if (movement != null) {
			movement.changeMaxSpeed(new Vector2(2f, 2f)); // Set to a friendlier, slower speed
		}
	}
	
	private static void addSoundEffects(Entity enemy, BaseEntityConfig config) {
		String[] animalSoundPaths = config.getSoundPath();
		if (animalSoundPaths != null && animalSoundPaths.length > 0) {
			String eventPausedStart = String.format("PauseStart%s", config.getAnimalName());
			String eventPausedEnd = String.format("PauseEnd%s", config.getAnimalName());
			enemy.getEvents().addListener(eventPausedStart, (String[] hintText) -> initiateDialogue(animalSoundPaths, hintText));
			enemy.getEvents().addListener(eventPausedEnd, () -> endDialogue());
		}
	}
	
	private static void initiateDialogue(String[] animalSoundPaths, String[] hintText) {
		EntityChatService chatService = ServiceLocator.getEntityChatService();
		for (String soundPath : animalSoundPaths) {
			// Play sound logic here
		}
		chatService.updateText(hintText);
	}
	
	private static void endDialogue() {
		EntityChatService chatService = ServiceLocator.getEntityChatService();
		chatService.disposeCurrentOverlay();
	}
}