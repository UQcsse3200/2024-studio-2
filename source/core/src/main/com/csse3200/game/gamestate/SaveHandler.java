package com.csse3200.game.gamestate;

import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;

/**
Wrapper for FileLoader that has default interactions with various data structs.
Saves and loads specified files.
 */
public class SaveHandler {
    private static final Logger logger = LoggerFactory.getLogger(SaveHandler.class);
    private static final String ROOT_DIR = "saves";
    private static final String FILE_EXTENSION = ".json";

    /**
     * Save all fields in the specified class to JSON files.
     *
     * @param add an arbitrary string to be added to file names. Must be an empty string.
     * @param className class to use as saves. For game state, use GameState.class.
     *                  For achievements, use Achievements.class.
     */
    protected static void save(Class<?> className, String add) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                FileLoader.writeClass(member.get(null),
                        toPath(member.getName()+add),
                        FileLoader.Location.LOCAL);
            } catch (IllegalAccessException ignored) {}
        }
        logger.info("All Tracked Objects Saved");
    }

    /**
     * Load all fields to a specified class from JSON files.
     *
     * @param add an arbitrary string to be added to file names. Must be an empty string.
     * @param className class to use as saves. For game state, use GameState.class.
     *      *                  For achievements, use Achievements.class.
     */
    protected static void loadAll(Class<?> className, String add) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                member.set(null,FileLoader.readClass(member.getType(),
                        toPath(member.getName()+add),
                        FileLoader.Location.LOCAL));
            } catch (IllegalAccessException ignored) {}
        }
        logger.info("All Tracked Objects Loaded");
        //loads all objects automatically and adds them to tracked
    }

    /**
     * Clear all data from JSON files.
     *
     * @param add an arbitrary string to be added to file names. Must be an empty string.
     * @param className class to use as saves. For game state, use GameState.class.
     *      *                  For achievements, use Achievements.class.
     */
    protected static void clearAll(Class<?> className, String add) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            FileLoader.deleteJson(toPath(member.getName()+add),
                    FileLoader.Location.LOCAL);
        }
        logger.info("All Tracked Objects Deleted");
        //loads all objects automatically and adds them to tracked
    }

    /**
     * Converts a file name to a full path for JSONs.
     *
     * @param append the file name to be converted to a path
     * @return String the converted path
     */
    private static String toPath(String append) {
        return ROOT_DIR + File.separator + append + FILE_EXTENSION;
    }
}
