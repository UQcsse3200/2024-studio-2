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
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.minigames.maze.entities.MazePlayer;
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

  public static MazePlayer createPlayer() {
    return new MazePlayer(stats);
  }

  private MazePlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
