package com.csse3200.game.components.gamestate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/*
Wrapper for FileLoader that has default interactions with various data structs.
Saves and loads specified files.
 */
public class SaveHandler {
    private static final Logger logger = LoggerFactory.getLogger(SaveHandler.class);
    private static final String ROOT_DIR = "CSSE3200Game";
    private static final String FILE_EXTENSION = ".json";


    public static void save() {
        save("", GameState.class);
    }

    protected static void save(String add, Class<?> className) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                FileLoader.writeClass(member.get(null),
                        toPath(member.getName() + add), FileLoader.Location.EXTERNAL);
            } catch (IllegalAccessException e) {
                logger.debug("Unable to access {} field in GameState", member.getName());
            }
        }
        logger.info("All Tracked Objects Saved");
    }

    public static void loadAll() {
        loadAll("", GameState.class);
    }

    protected static void loadAll(String add, Class<?> className) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            try {
                member.set(null,FileLoader.readClass(member.getType(),
                        toPath(member.getName()+add), FileLoader.Location.EXTERNAL));
            } catch (IllegalAccessException e) {
                logger.debug("Unable to access {} field in GameState", member.getName());
            }
        }
        logger.info("All Tracked Objects Loaded");
        //loads all objects automatically and adds them to tracked
    }

    public static void clearAll() {
        clearAll("", GameState.class);
    }

    protected static void clearAll(String add, Class<?> className) {
        Field[] members = className.getDeclaredFields();
        for (Field member : members) {
            FileLoader.deleteJson(toPath(member.getName()+add), FileLoader.Location.EXTERNAL);
        }
        logger.info("All Tracked Objects Deleted");
        //loads all objects automatically and adds them to tracked
    }

    private static String toPath(String string) {
        return ROOT_DIR + File.separator + string + FILE_EXTENSION;
    }
}
