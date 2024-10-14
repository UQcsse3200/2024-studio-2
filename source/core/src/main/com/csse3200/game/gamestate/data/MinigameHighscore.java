package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.minigames.MiniGameNames;

import java.util.HashMap;
import java.util.Map;

import static com.csse3200.game.minigames.MiniGameNames.*;

public class MinigameHighscore implements Json.Serializable {

    // Store high scores in a map with the minigame name as the key
    private final Map<String, Integer> highscores = new HashMap<>();

    /**
     * Add or update the high score for a specific minigame.
     *
     * @param minigameName The name of the minigame.
     * @param score The score to be saved.
     */
    public void addHighScore(String minigameName, int score) {
        // Only update the score if it's higher than the current high score
        if (!highscores.containsKey(minigameName) || highscores.get(minigameName) < score) {
            highscores.put(minigameName, score);
        }
    }

    /**
     * Get the high score for a specific minigame.
     *
     * @param minigameName The name of the minigame.
     * @return The high score for the minigame, or 0 if no score is available.
     */
    public int getHighScore(MiniGameNames minigameName) {

        return highscores.getOrDefault(gameToString(minigameName), 0);
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart("highScores");
        for (Map.Entry<String, Integer> entry : highscores.entrySet()) {
            json.writeValue(entry.getKey(), entry.getValue());
        }
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue highScoresData = jsonData.get("highScores");
        if (highScoresData != null) {
            for (JsonValue scoreEntry : highScoresData) {
                String minigameName = scoreEntry.name;
                int score = scoreEntry.asInt();
                highscores.put(minigameName, score);
            }
        }
    }

    /**
     * Get all the high scores.
     *
     * @return A map containing all the high scores.
     */
    public Map<String, Integer> getAllHighScores() {
        return highscores;
    }

    /**
     * Converts the MiniGameName to Stirng
     * @param gameName the MiniGameName to be converted
     * @return the minigame name as a string
     */
    private String gameToString(MiniGameNames gameName) {
        String result = "";
        switch (gameName) {
            case SNAKE -> result = "snake";
            case MAZE -> result = "maze";
            case BIRD -> result = "bird";
        }
        return result;
    }

    /**
     * Takes a game name as a string and converts it to a MiniGameName COnstant
     * @param gameName the string to eb converted
     * @return the MiniGameName constant
     */
    private MiniGameNames stringtoGame(String gameName) {
        MiniGameNames result = null;
        switch (gameName) {
            case "snake" -> result = SNAKE;
            case "maze" -> result = MAZE;
            case "bird" -> result = BIRD;
        }
        return result;
    }
}