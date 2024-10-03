package com.csse3200.game.minigames.maze.entities.factories;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.entities.configs.MazeNPCConfigs;
import com.csse3200.game.minigames.maze.entities.mazenpc.*;

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

    private MazeNPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates the angler fish NPC
     *
     * @param target entity associated with angular fish
     * @return the angular fish
     */
    public static AnglerFish createAngler(Entity target) {
        MazeEntityConfig config = configs.angler;
        return new AnglerFish(target, config);
    }

    /**
     * Creates the eel npc
     *
     * @param target the entity to be associated with the Eel npc
     * @return the eel npc
     */
    public static ElectricEel createEel(Entity target) {
        MazeEntityConfig config = configs.eel;
        return new ElectricEel(target, config);
    }

    /**
     * Creates the jellyfish npc
     *
     * @return the jellyfish
     */
    public static Jellyfish createJellyfish() {
        MazeEntityConfig config = configs.jellyfish;
        return new Jellyfish(config);
    }

    /**
     * Creates the green jellyfish npc
     *
     * @return the jellyfish
     */
    public static GreenJellyfish createGreenJellyfish() {
        MazeEntityConfig config = configs.jellyfish;
        return new GreenJellyfish(config);
    }


    /**
     * Creates the fish egg npc
     *
     * @return the fish egg npc
     */
    public static FishEgg createFishEgg() {
        return new FishEgg();
    }
}
