package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.Achievement;
import com.csse3200.game.components.quests.Quest;
import com.csse3200.game.gamestate.Achievements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AchievementSave implements Json.Serializable {
    public List<Achievement> achievementList = new ArrayList<Achievement>();

    @Override
    public void write(Json json) {
        json.writeArrayStart("achievements");
        for(Achievement element : achievementList) {
            json.writeObjectStart();
            json.writeValue("questName", element.getQuestName());
            json.writeValue("questDescription", element.getQuestDescription());
            json.writeValue("completed", element.isCompleted());
            json.writeValue("seen", element.isSeen());
            json.writeValue("type", element.getType().name());
            json.writeValue("iconPath", element.getPath());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Logger logger = LoggerFactory.getLogger(AchievementSave.class);
        List<Achievement> newAchievements = new ArrayList<>();

        JsonValue.JsonIterator achievements = jsonData.child.iterator();

        while(achievements.hasNext()) {
            JsonValue nextAchievement = achievements.next();
            Achievement newAchievement = new Achievement(
                    nextAchievement.getString("questName"),
                    nextAchievement.getString("questDescription"),
                    nextAchievement.getBoolean("completed"),
                    nextAchievement.getBoolean("seen"),
                    Achievement.AchievementType.valueOf(nextAchievement.getString("type")),
                    nextAchievement.getString("iconPath")
            );
            newAchievements.add(newAchievement);
        }

        achievementList = newAchievements;
    }
}
