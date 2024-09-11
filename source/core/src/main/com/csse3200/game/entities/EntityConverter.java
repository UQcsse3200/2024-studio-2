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
import com.csse3200.game.entities.configs.BaseFriendlyEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

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
		config.setFriendly(true);
		
		removeEnemyComponents(enemy);
		updateAIBehavior(enemy, player, enemies);
		updateAnimation(animator, config);
		updateComponents(enemy, config);
		adjustMovementSpeed(enemy);
		addEventTriggers(enemy, config);
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
	
	private static void addEventTriggers(Entity entity, BaseEntityConfig config) {
		String[] animalSoundPaths = config.getSoundPath();
		if (animalSoundPaths != null && animalSoundPaths.length > 0) {
			String animalName = (config instanceof BaseFriendlyEntityConfig)
					? ((BaseFriendlyEntityConfig) config).getAnimalName()
					: "Animal";
			
			String eventPausedStart = String.format("PauseStart%s", animalName);
			String eventPausedEnd = String.format("PauseEnd%s", animalName);
			
			entity.getEvents().addListener(eventPausedStart,
					() -> NPCFactory.initiateDialogue(animalSoundPaths, getHintText(config)));
			entity.getEvents().addListener(eventPausedEnd, NPCFactory::endDialogue);
		}
	}
	
	private static String[] getHintText(BaseEntityConfig config) {
		if (config instanceof BaseFriendlyEntityConfig) {
			BaseFriendlyEntityConfig friendlyConfig = (BaseFriendlyEntityConfig) config;
			String[] hintText = friendlyConfig.getStringHintLevel();
			if (hintText == null || hintText.length == 0) {
				hintText = friendlyConfig.getBaseHint();
			}
			if (hintText == null || hintText.length == 0) {
				hintText = new String[]{"A friendly " + friendlyConfig.getAnimalName() + " appears!"};
			}
			return hintText;
		} else {
			return new String[]{"A friendly creature appears!"};
		}
	}
}
