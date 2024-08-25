package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.inventory.items.potions.healingpotion.HealingPotion;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import static com.csse3200.game.physics.PhysicsLayer.OBSTACLE;

public class ItemFactory {
    private static Entity createItem(Entity target, AbstractItem item) {
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new ItemProximityTask(target,20, 1f, item));

        Entity itemEntity = new  Entity().addComponent(new TextureRenderComponent(item.getTexturePath()))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(OBSTACLE))
                .addComponent(aiComponent);
        itemEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        return itemEntity;
    }

    public static Entity createHealthPotion(Entity target, HealingPotion healthPotion) {
        return createItem(target, healthPotion);
    }

    public static Entity createApple(Entity target, Foods.Apple apple) {
        return createItem(target, apple);
    }
}
