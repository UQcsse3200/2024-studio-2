package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.PauseTask;
import com.csse3200.game.components.tasks.AvoidTask;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class EntityConverter {
	
	public static void convertToFriendly(Entity enemy, Entity player, List<Entity> enemies) {
		// Remove enemy-specific components
		enemy.removeComponent(CombatStatsComponent.class);
		enemy.removeComponent(TouchAttackComponent.class);
		
		// Update AI behavior
		AITaskComponent aiComponent = enemy.getComponent(AITaskComponent.class);
		if (aiComponent != null) {
			aiComponent.dispose(); // This will stop the current task
			enemy.removeComponent(AITaskComponent.class);
			
			AITaskComponent newAIComponent = new AITaskComponent()
					.addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
					.addTask(new PauseTask(player, 10, 2f, 1f, false));
			
			for (Entity otherEnemy : enemies) {
				newAIComponent.addTask(new AvoidTask(otherEnemy, 10, 3f, 3f, false));
			}
			
			enemy.addComponent(newAIComponent);
		}
		
		// Update animation
		AnimationRenderComponent animator = enemy.getComponent(AnimationRenderComponent.class);
		if (animator != null) {
			//TextureAtlas atlas = ServiceLocator.getResourceService().getAsset("images/chicken.atlas", TextureAtlas.class);
			animator.stopAnimation();
			animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
			enemy.addComponent(new FriendlyNPCAnimationController());
		}
		
		// Adjust movement speed
		PhysicsMovementComponent movement = enemy.getComponent(PhysicsMovementComponent.class);
		if (movement != null) {
			movement.changeMaxSpeed(new Vector2(2f, 2f)); // Set to a friendlier, slower speed
		}
		
		// Add friendly NPC specific components
		BaseEntityConfig config = new BaseEntityConfig(); // Might want to create a specific config for converted entities
		enemy.addComponent(new CombatStatsComponent(config.health, 0, 0, 0, 0, 0));
		
		// Register as a friendly NPC
		NPCFactory.registerFriendlyNPC(enemy);
		
		// Trigger conversion event
		enemy.getEvents().trigger("onConvertToFriendly");
	}
}