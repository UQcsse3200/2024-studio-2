package com.csse3200.game.gamestate;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.QuestBasic;

import java.util.ArrayList;
import java.util.List;

public class Quests {
    public List<QuestBasic> quests = new ArrayList<>();

//    @Override
//    public void write(Json json) {
//        for (QuestBasic quest : quests) {
//
//        }
//    }

//    @Override
//    public void read(Json json, JsonValue jsonData) {
//        Quests quests = new Quests();
//        quests.quests = jsonData.get("jsonFieldName");
//    }
}
