package com.csse3200.game.entities.factories;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CameraZoomComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerInventoryDisplay;

import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.components.stats.Stat;
import com.csse3200.game.components.stats.StatManager;
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
                        .addComponent(new CameraZoomComponent())
                        // Notify terrain component when moving
                        .addComponent(new PhysicsComponent(true))
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        player.addComponent(new PlayerActions(game, player));
        if (imagePath.equals("images/dog.png")) {
          player.addComponent(new CombatStatsComponent(70, 100, 70, 50, 50, 20));
        } else if (imagePath.equals("images/croc.png")) {
          player.addComponent(new CombatStatsComponent(100, 100, 90, 70, 30, 100));
        } else if (imagePath.equals("images/bird.png")) {
          player.addComponent(new CombatStatsComponent(60, 100, 40, 60, 100, 100));
        }
        else {
          player.addComponent(new CombatStatsComponent(stats.getHealth(), stats.getHunger(), stats.getStrength(), stats.getDefense(), stats.getSpeed(), stats.getExperience()));
        }

        player.addComponent(new PlayerInventoryDisplay(45, 9))

              .addComponent(inputComponent)
              .addComponent(new PlayerStatsDisplay())
              .addComponent(new QuestManager(player))
              .addComponent(new QuestPopup());
            player.addComponent((new StatManager()));



    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    player.getComponent(StatManager.class).addStat(new Stat("KangaDefeated", "Kangaroos Defeated", 1));
    player.getComponent(QuestManager.class).loadQuests();
    return player;
  }




    private PlayerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    public static String getSelectedAnimalImagePath() {return AnimalSelectionActions.getSelectedAnimalImagePath();
    }
}