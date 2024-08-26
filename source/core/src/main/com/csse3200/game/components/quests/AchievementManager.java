package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class AchievementManager extends Component {
    private final HashMap<String, QuestHidden> achievements;
    private final EventService eventService = ServiceLocator.getEventService();
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);

    public AchievementManager() {
        this.achievements = new HashMap<>();

        // Manual test achievements
        QuestHidden tempAchievement = new QuestHidden("Test Achievement","This is a test achievement");
        addAchievement(tempAchievement);

    }

    private void subscribeToAchievementEvents(QuestHidden achievement) {
        eventService.globalEventHandler.addListener(achievement.getQuestName(), () -> this.completeAchievement(achievement.getQuestName()));
    }

    public void addAchievement(QuestHidden achievement) {
        achievements.put(achievement.getQuestName(), achievement);
        subscribeToAchievementEvents(achievement);
    }

    public QuestHidden getAchievement(String achievementName) {
        return achievements.get(achievementName);
    }

    public void progressAchievement(String achievementName) {
        QuestHidden achievement = achievements.get(achievementName);
        if (achievement != null && !achievement.isCompleted()) {

            questComplete.play();
            eventService.globalEventHandler.trigger(achievementName);
            logger.info("{} Completed!", achievement.getQuestName());

        }
    }

    public void completeAchievement(String achievementName) {

    }


}

