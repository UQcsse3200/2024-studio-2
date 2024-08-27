package com.csse3200.game.gamestate;

import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;

/**
Wrapper for FileLoader that has default interactions with various data structs.
Saves and loads specified files.
    @see GameState
    @see Achievements
 */
public class SaveHandler {

    private SaveHandler() {}
    private static final Logger logger = LoggerFactory.getLogger(SaveHandler.class);
    private static final String FILE_EXTENSION = ".json";

    /**
     * Save all fields in the specified class to JSON files.
     *
     * @param dir the directory in assets for the saves to be located in.
     *            For game state (not shared between new games), use "saves".
     *            For achievements (shared between new games), use "saves/achievements"
     * @param className class to use as saves.
     *                  For game state (not shared between new games), use GameState.class.
     *                  For achievements (shared between new games), use Achievements.class.
     */
    public static void save(Class<?> className, String dir) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                FileLoader.writeClass(member.get(null),
                        toPath(member.getName(), dir),
                        FileLoader.Location.LOCAL);
            } catch (IllegalAccessException ignored) {}
        }
        logger.info("Objects from {} class saved to {}", className.getSimpleName(), dir);
    }

    /**
     * Load all fields to a specified class from JSON files.
     *
     * @param dir the directory in assets for the saves to be located in.
     *            For game state (not shared between new games), use "saves".
     *            For achievements (shared between new games), use "saves/achievements"
     * @param className class to use as saves.
     *                  For game state (not shared between new games), use GameState.class.
     *                  For achievements (shared between new games), use Achievements.class.
     */
    public static void load(Class<?> className, String dir) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                member.set(null,FileLoader.readClass(member.getType(),
                        toPath(member.getName(), dir),
                        FileLoader.Location.LOCAL));
            } catch (IllegalAccessException ignored) {}
        }
        logger.info("Objects from {} class loaded from {}", className.getSimpleName(), dir);
        //loads all objects automatically and adds them to tracked
    }

    /**
     * Clear all data from JSON files.
     *
     * @param dir the directory in assets for the saves to be located in.
     *            For game state (not shared between new games), use "saves".
     *            For achievements (shared between new games), use "saves/achievements"
     * @param className class to use as saves.
     *                  For game state (not shared between new games), use GameState.class.
     *                  For achievements (shared between new games), use Achievements.class.
     */
    public static void delete(Class<?> className, String dir) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            FileLoader.deleteJson(toPath(member.getName(), dir),
                    FileLoader.Location.LOCAL);
        }
        logger.info("Objects from {} class deleted from {}", className.getSimpleName(), dir);
        //loads all objects automatically and adds them to tracked
    }

    /**
     * Converts a file name to a full path for JSONs.
     *
     * @param append the file name to be converted to a path
     * @param dir the directory from assets for the path to be toward
     * @return String the converted path
     */
    private static String toPath(String append, String dir) {
        return dir + File.separator + append + FILE_EXTENSION;
    }
}
