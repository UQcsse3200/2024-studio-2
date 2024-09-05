package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.inventory.items.potions.HealingPotion;
import com.csse3200.game.inventory.items.potions.SpeedPotion;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameTime;

/**
 * A factory class for creating various item entities in the game.
 * This class provides methods to create different items like health potions and apples.
 * with specific behaviours such proximity detection using AI tasks and physics components
 */
public class ItemFactory {

    /**
     * Creates an item entity with the specified target and item.
     * The entity is configured with a texture, physics and AI components
     * @param target The entity that the item will interact with (e.g., the player).
     * @param item The specific item to create an entity of (e.g., potions, food items).
     * @return The created item entity, fulled configured with its components
     */
    private static Entity createItem(Entity target, AbstractItem item) {
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new ItemProximityTask(target,20, 1f, item));

        Entity itemEntity = new  Entity().addComponent(new TextureRenderComponent(item.getTexturePath()))
                .addComponent(new PhysicsComponent())
                .addComponent(aiComponent);
        itemEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        return itemEntity;
    }

    /**
     * Creates a health potion entity that interacts with the specified target.
     *
     * @param target The entity the health potion will interact with
     * @return The created health potion entity.
     */
    public static Entity createHealthPotion(Entity target) {
        return createItem(target, new HealingPotion(1));
    }

    /**
     * Creates a defense potion entity that interacts with the specified target.
     *
     * @param target The entity the defense potion will interact with
     * @return The created health potion entity.
     */
    public static Entity createDefensePotion(Entity target) {
        GameTime gameTime = new GameTime();
        return createItem(target, new DefensePotion(1, gameTime));
    }

    public static Entity createSpeedPotion(Entity target) {
        GameTime gameTime = new GameTime();
        return createItem(target, new SpeedPotion(1, gameTime));
    }

    /**
     * Creates an attack potion entity that interacts with the specified target.
     *
     * @param target The entity the attack potion will interact with
     * @return The created attack potion entity
     */
    public static Entity createAttackPotion(Entity target) {
        GameTime gameTime = new GameTime();
        return createItem(target, new AttackPotion(1, gameTime));
    }

    /**
     * Creates an apple entity that interacts with the specified target.
     * @param target The entity the apple with interact with
     * @return The created apple entity.
     */
    public static Entity createApple(Entity target) {
        return createItem(target, new Foods.Apple(1));
    }

    /**
     * Creates a carrot entity that interacts with the specifc target
     * @param target The entity the apple with interact with
     * @return the created carrot entity
     */
    public static Entity createCarrot(Entity target) {
        return createItem(target, new Foods.Carrot(1));
    }

    public static Entity createCandy(Entity target) {
        return createItem(target, new Foods.Candy(1));
    }

    public static Entity createChickenLeg(Entity target) {
        return createItem(target, new Foods.ChickenLeg(1));
    }

    public static Entity createMeat(Entity target) {
        return createItem(target, new Foods.Meat(1));
    }
}
