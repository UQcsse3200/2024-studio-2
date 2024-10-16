package com.csse3200.game.entities.factories;

import box2dLight.PositionalLight;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CameraZoomComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.move.*;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.lootboxview.LootBoxOverlayComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.components.quests.AchievementPopup;
import com.csse3200.game.components.stats.StatManager;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.components.FadeLightsDayTimeComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */

public class PlayerFactory {

    public static Entity createPlayer(GdxGame game) {
        String imagePath = GameState.player.selectedAnimalPath;

        Entity player =
                new Entity() // Set that this entity is the player
                        .addComponent(new TextureRenderComponent(imagePath))
                        .addComponent(new CameraZoomComponent())
                        .addComponent(new PhysicsComponent(true))
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        List<CombatMove> moveSet = new ArrayList<>();
        moveSet.add(new AttackMove("Player Attack", 10));
        moveSet.add(new GuardMove("Player Guard", 5));
        moveSet.add(new SleepMove("Player Sleep", 0));
        moveSet.add(new ItemMove("Player Item", 0));

        player.addComponent(new CombatMoveComponent(moveSet));
        player.addComponent(new PlayerActions(game, player, imagePath));

        CombatStatsComponent combatComponent = new CombatStatsComponent(
                GameState.player.health,
                GameState.player.hunger,
                GameState.player.strength,
                GameState.player.defense,
                GameState.player.speed,
                GameState.player.exp,
                true,
                false,
                GameState.player.level,
                GameState.player.currentHealth,
                GameState.player.currentHunger);

        player.addComponent(combatComponent);



        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForPlayer();
        
        player.addComponent(inputComponent)
                .addComponent(new PlayerStatsDisplay())
                .addComponent(new QuestManager(player))
                .addComponent(new QuestPopup())
                .addComponent((new StatManager(player)));

        // Add inventory from player (in future this will provide shared interface for memory
        InventoryComponent inventoryComponent = new InventoryComponent(50);
        player.addComponent(inventoryComponent)

                .addComponent(new PlayerInventoryDisplay(inventoryComponent.getInventory(), 5, 5,game))

                .addComponent(new LootBoxOverlayComponent());
        player.addComponent(new AchievementPopup());

        // Add QuestManager to player
        player.addComponent(new QuestManager(player));

        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.getComponent(ColliderComponent.class).setDensity(1.5f);
        player.getComponent(TextureRenderComponent.class).scaleEntity();


        PositionalLight light = LightingComponent.createPointLight(4f, Color.GOLDENROD);
        player.addComponent(new LightingComponent().attach(light))
              .addComponent(new FadeLightsDayTimeComponent());

        player.setIsPlayer(true);

        return player;
    }


    /**
     * Create a player NPC to spawn in Combat
     */

    public static Entity createCombatPlayer(String imagePath) {
        Entity combatPlayer = createCombatPlayerStatic();

        CombatStatsComponent combatComponent = new CombatStatsComponent(
                GameState.player.health,
                GameState.player.hunger,
                GameState.player.strength,
                GameState.player.defense,
                GameState.player.speed,
                GameState.player.exp,
                true,
                false,
                GameState.player.level,
                GameState.player.currentHealth,
                GameState.player.currentHunger);

        combatPlayer
                .addComponent(new TextureRenderComponent(imagePath))
                .addComponent(combatComponent);

        combatPlayer.scaleHeight(90.0f);

        return combatPlayer;
    }

    public static String getSelectedAnimalImagePath() {
        return GameState.player.selectedAnimalPath;
    }

    /**
     * Creates a boss NPC to be used as a boss entity by more specific NPC creation methods.
     *
     * @return entity
     */
    public static Entity createCombatPlayerStatic() {
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER));


        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }


    private PlayerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}