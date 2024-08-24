package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.potions.healingpotion.HealingPotion;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;


import static com.csse3200.game.physics.PhysicsLayer.OBSTACLE;


public class ItemFactory {


    private static Entity createItem(Entity target, AbstractItem item, String texturePath) {
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new ItemProximityTask(target,20, 1f) );

        Entity itemEntity = new  Entity().addComponent(new TextureRenderComponent(texturePath))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(OBSTACLE))
                .addComponent(aiComponent);
        itemEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        return itemEntity;
    }

    public static Entity createHealthPotion(Entity target, HealingPotion healthPotion) {
        return createItem(target, healthPotion, "images/Healthpotion.png");
    }

}
