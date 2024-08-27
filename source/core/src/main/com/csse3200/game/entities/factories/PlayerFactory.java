package com.csse3200.game.entities.factories;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.animal.AnimalSelectionActions;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer(GdxGame game) {
    String imagePath = AnimalSelectionActions.getSelectedAnimalImagePath();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();


    Entity player =
        new Entity()
            .addComponent(new TextureRenderComponent(imagePath))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions(game));
            if (imagePath.equals("images/dog.png")) {
              player.addComponent(new CombatStatsComponent(70, 100, 70, 50, 50, 0));

            } else if (imagePath.equals("images/croc.png")) {
              player.addComponent(new CombatStatsComponent(90, 100, 90, 70, 30, 0));
            } else if (imagePath.equals("images/bird.png")) {
              player.addComponent(new CombatStatsComponent(60, 100, 40, 60, 100, 0));
            }
            player.addComponent(new CombatStatsComponent(stats.health, stats.hunger, stats.strength, stats.defense, stats.speed, stats.experience));

            player.addComponent(new InventoryComponent(stats.gold));
            player.addComponent(inputComponent);
            player.addComponent(new PlayerStatsDisplay());
            player.addComponent(new QuestManager());



    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
