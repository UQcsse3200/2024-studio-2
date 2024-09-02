package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.configs.*;



public class EntityConverter {
	
	/**
	 * Converts an EnemyNPC to a FriendlyNPC.
	 *
	 * @param enemy The enemy NPC entity to be converted.
	 */
	public static void convertToFriendly(Entity enemy) {
		// Set the NPC state to friendly
		EnemyFactory.FRIENDLY = true;
		
		// Change properties or textures
		AnimationRenderComponent animator =
				new AnimationRenderComponent(
						ServiceLocator.getResourceService().getAsset("images/chicken.atlas", TextureAtlas.class));
		animator.addAnimation("float", 10, Animation.PlayMode.LOOP);
		
		// Modify behavior or components to make the NPC friendly
		enemy.removeComponent(CombatStatsComponent.class); // Example: Remove combat capabilities
		enemy.addComponent(new Component()); // Add friendly behavior component
		
		// Register friendly NPC using NPCFactory
		NPCFactory.registerFriendlyNPC(enemy);
	}
}
