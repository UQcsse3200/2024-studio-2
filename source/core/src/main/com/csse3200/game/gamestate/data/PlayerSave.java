package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerSave implements Json.Serializable {
    public String selectedAnimalPath = null;
    public int health = 0;
    public int currentHealth = 0;
    public int hunger = 0;
    public int currentHunger = 0;
    public int strength = 0;
    public int defense = 0;
    public int speed = 0;
    public int level = 0;
    public int gold = 0;
    public String favouriteColour = "peach";
    public int exp = 0;

    @Override
    public void write(Json json) {
        json.writeValue("health", health);
        json.writeValue("hunger", hunger);
        json.writeValue("currentHealth", currentHealth);
        json.writeValue("currentHunger", currentHunger);
        json.writeValue("strength", strength);
        json.writeValue("defense", defense);
        json.writeValue("speed", speed);
        json.writeValue("level", level);
        json.writeValue("gold", gold);
        json.writeValue("favouriteColour", favouriteColour);
        json.writeValue("exp", exp);
        json.writeValue("selectedAnimalPath", selectedAnimalPath);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        selectedAnimalPath = jsonData.getString("selectedAnimalPath");
        health = jsonData.getInt("health");
        currentHealth = jsonData.getInt("currentHealth");
        hunger = jsonData.getInt("hunger");
        currentHunger = jsonData.getInt("currentHunger");
        strength = jsonData.getInt("strength");
        defense = jsonData.getInt("defense");
        speed = jsonData.getInt("speed");
        level = jsonData.getInt("level");
        gold = jsonData.getInt("gold");
        favouriteColour = jsonData.getString("favouriteColour");
        exp = jsonData.getInt("exp");
    }
}
