package com.csse3200.game.components.gamestate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Wrapper for FileLoader that has default interactions with various data structs.
Saves and loads specified files.
 */
public class SaveHandler {
    private static final Logger logger = LoggerFactory.getLogger(SaveHandler.class);
    private static final String ROOT_DIR = "CSSE3200Game";
    private static final String FILE_EXTENSION = ".json";

    private static HashMap<String, Object> tracked = new HashMap<>();


    public static void save() {
//        logger.info("Outputting {} {}", tracked.entrySet().stream().toList().get(0).getKey(), tracked.entrySet().stream().toList().get(0).getValue());
//        logger.info("At dir {}", ROOT_DIR + File.separator + tracked.entrySet().stream().toList().get(0).getKey() + FILE_EXTENSION);
        for (Map.Entry<String, Object> entry : tracked.entrySet()) {
            FileLoader.writeClass(entry.getValue(),
                    toPath(entry.getKey()), FileLoader.Location.EXTERNAL);
        }
        logger.info("Game saved");
    }

    public static void loadAll() {
        GameState.env = FileLoader.readClass(EnvironmentVariables.class, toPath("env"),
                FileLoader.Location.EXTERNAL);
        logger.info("Env loaded with test = {}", GameState.env.test);
        //loads all objects automatically and adds them to tracked
    }

    private static String toPath(String string) {
        return ROOT_DIR + File.separator + string + FILE_EXTENSION;
    }

    public static Object getLoaded(String name) {
        return tracked.get(name);
    }

    public static void addTrack(String name, Object object) {
        tracked.put(name, object);
    }

    public static void addTrack(Object object) {
        tracked.put(object.getClass().getName(), object);
    }

    public static void autoAddTrack() {
        addTrack("env", GameState.env);
        //to be populated with all tracked objects and called at runtime
        //optional method, to be discussed with other teams
    }


}
