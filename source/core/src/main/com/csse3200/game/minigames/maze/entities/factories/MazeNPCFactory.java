package com.csse3200.game.minigames.maze.entities.factories;


import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.entities.configs.MazeNPCConfigs;
import com.csse3200.game.minigames.maze.entities.mazenpc.AnglerFish;
import com.csse3200.game.minigames.maze.entities.mazenpc.Jellyfish;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class MazeNPCFactory {
  private static final MazeNPCConfigs configs =
      FileLoader.readClass(MazeNPCConfigs.class, "configs/minigames/maze/NPCs.json");

  public static AnglerFish createAngler(Entity target) {
    MazeEntityConfig config = configs.angler;
    return new AnglerFish(target, config);
  }

  public static Jellyfish createJellyfish() {
    MazeEntityConfig config = configs.jellyfish;
    return new Jellyfish(config);
  }

  private MazeNPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
