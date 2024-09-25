package com.csse3200.game.minigames.maze.entities.factories;


import com.csse3200.game.files.FileLoader;
import com.csse3200.game.minigames.maze.entities.MazePlayer;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;


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

  public MazePlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
