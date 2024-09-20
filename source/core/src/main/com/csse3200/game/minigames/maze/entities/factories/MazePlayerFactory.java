package com.csse3200.game.minigames.maze.entities.factories;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class MazePlayerFactory {
  private static final MazePlayerConfig stats =
      FileLoader.readClass(MazePlayerConfig.class, "configs/minigames/maze/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    Entity player =
        new Entity()
            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new MazePlayerActions())
            .addComponent(new MazeCombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(inputComponent)
            .addComponent(new MazePlayerStatsDisplay());

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    player.setScale(player.getScale().scl(0.3f));
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);

    Color lightColor = new Color(0.55f, 0.45f, 0.75f, 1);
    RayHandler rayHandler = ServiceLocator.getLightingService().getLighting().getRayHandler();
    PointLight pl1 = new PointLight(rayHandler, 1000, lightColor, 4f, 0, 0);
    PointLight pl2 = new PointLight(rayHandler, 1000, lightColor, 4f, 0, 0);
    player.addComponent(new LightingComponent().attach(pl1).attach(pl2));
    pl1.setSoftnessLength(0f);
    pl1.setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.OBSTACLE);
    pl2.setXray(true);
    return player;
  }

  private MazePlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
