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
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class EntityConverter {
	private static final NPCConfigs configs =
			FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
	
	public static void convertToFriendly(Entity enemy, Entity player, List<Entity> enemies) {
		AnimationRenderComponent animator = enemy.getComponent(AnimationRenderComponent.class);
		if (animator == null) {
			throw new IllegalStateException("Entity does not have an AnimationRenderComponent. Cannot convert to friendly.");
		}
		
		BaseEntityConfig config = determineConfig(animator);
		config.setFriendly(true);  // Ensure the config is set to friendly
		
		removeEnemyComponents(enemy);
		updateAIBehavior(enemy, player, enemies);
		updateAnimation(animator, config);
		updateComponents(enemy, config);
		adjustMovementSpeed(enemy);
		addSoundEffects(enemy, config);
		
		enemy.getEvents().trigger("onConvertToFriendly");
	}
	
	private static BaseEntityConfig determineConfig(AnimationRenderComponent animator) {
		String currentAnimation = animator.getCurrentAnimation();
		if (currentAnimation == null) {
			return configs.chicken; // Default to chicken if no animation is set
		}
		
		if (currentAnimation.contains("chicken")) {
			return configs.chicken;
		} else if (currentAnimation.contains("frog")) {
			return configs.frog;
		} else if (currentAnimation.contains("monkey")) {
			return configs.monkey;
		} else {
			return configs.chicken; // Default to chicken if type can't be determined
		}
	}
	
	private static void removeEnemyComponents(Entity enemy) {
		enemy.removeComponent(TouchAttackComponent.class);
		enemy.removeComponent(CombatStatsComponent.class);
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
		// Note: We don't have direct access to health in BaseEntityConfig
		// You may need to add a getHealth() method to BaseEntityConfig or handle this differently
		int defaultHealth = 10; // Using a default value; adjust as needed
		enemy.addComponent(new CombatStatsComponent(defaultHealth, 0, 0, 0, 0, 0))
				.addComponent(new FriendlyNPCAnimationController());
	}
	
	private static void adjustMovementSpeed(Entity enemy) {
		PhysicsMovementComponent movement = enemy.getComponent(PhysicsMovementComponent.class);
		if (movement != null) {
			movement.changeMaxSpeed(new Vector2(2f, 2f));
		}
	}
	
	private static void addSoundEffects(Entity enemy, BaseEntityConfig config) {
		String[] animalSoundPaths = config.getSoundPath();
		if (animalSoundPaths != null && animalSoundPaths.length > 0) {
			String eventPausedStart = "PauseStart"; // Using a generic event name
			String eventPausedEnd = "PauseEnd";     // Using a generic event name
			enemy.getEvents().addListener(eventPausedStart, () -> initiateDialogue(animalSoundPaths));
			enemy.getEvents().addListener(eventPausedEnd, EntityConverter::endDialogue);
		}
	}
	
	private static void initiateDialogue(String[] animalSoundPaths) {
		DialogueBoxService chatService = ServiceLocator.getDialogueBoxService();
		for (String soundPath : animalSoundPaths) {
			// Play sound logic here
		}
		// Note: We don't have access to hintText here. You may need to adjust this.
		chatService.updateText(new String[]{"Friendly animal sound!"});
	}
	
	private static void endDialogue() {
		DialogueBoxService chatService = ServiceLocator.getDialogueBoxService();
		chatService.hideCurrentOverlay();
	}
}