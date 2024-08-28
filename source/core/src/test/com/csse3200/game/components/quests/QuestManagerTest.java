package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class QuestManagerTest {
    private QuestManager questManager;
    private EventHandler eventHandler;
    private Entity player;

    @BeforeEach
    void setUp() {
        EventService eventService = mock(EventService.class);
        eventHandler = mock(EventHandler.class);
        ResourceService resourceService = mock(ResourceService.class);
        player = mock(Entity.class);

        Sound mockSound = mock(Sound.class);
        when(resourceService.getAsset("sounds/QuestComplete.wav", Sound.class)).thenReturn(mockSound);


        when(player.getEvents()).thenReturn(eventHandler);
        ServiceLocator.registerEventService(eventService);
        ServiceLocator.registerResourceService(resourceService);

        questManager = new QuestManager(player);
    }

    @Test
    void AddQuest() {
        QuestBasic quest = new QuestBasic("Test Quest", "Test Description", List.of(), false, null, null, true, false, 0);
        questManager.addQuest(quest);

        assertEquals(quest, questManager.getQuest("Test Quest"));
    }

    @Test
    void AddAchievement() {
        QuestHidden achievement = new QuestHidden("Test Achievement", "Lorem ipsum dolor");
        questManager.addAchievement(achievement);
        assertEquals(achievement, questManager.getAchievement("Test Achievement"));
    }

    @Test
    void GetAllQuests() {
        QuestBasic quest1 = new QuestBasic("Quest 1", "Description 1", List.of(),  false, null, null, true, false, 0);
        QuestBasic quest2 = new QuestBasic("Quest 2", "Description 2", List.of(),  false, null, null, true, false, 0);
        questManager.addQuest(quest1);
        questManager.addQuest(quest2);

        List<QuestBasic> quests = questManager.getAllQuests();
        assertTrue(quests.contains(quest1));
        assertTrue(quests.contains(quest2));
    }

    @Test
    void HandleProgressQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest", "Description", List.of(task),  false, null, null, true, false, 0);
        questManager.addQuest(quest);

        questManager.progressQuest("Test Quest", "testTask");
        assertTrue(quest.isQuestCompleted());
    }

    @Test
    void HandleQuestCompletion() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest", "Description", List.of(task),  false, null, null, true, false, 0);
        questManager.addQuest(quest);


        questManager.progressQuest("Test Quest", "testTask");
        verify(eventHandler).trigger("questCompleted");
        verify(eventHandler).trigger("Test Quest");
    }

    @Test
    void HandleFailQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest", "Description", List.of(task),  false, null, null, true, false, 0);
        questManager.addQuest(quest);

        questManager.failQuest("Test Quest");
        assertTrue(quest.isFailed());
    }

    @Test
    void HandleNoQuestForProgression() {

        questManager.progressQuest("Nonexistent Quest", "taskName");
        verify(eventHandler, never()).trigger(anyString());
    }

    @Test
    void HandleAchievementCompletion() {
        QuestHidden achievement = new QuestHidden("Test Achievement", "Test Description");
        questManager.addAchievement(achievement);
        questManager.completeAchievement("Test Achievement");
        verify(eventHandler).trigger("achievementCompleted");
    }

    @Test
    void shouldSaveLoadQuestProgression() {
        QuestBasic quest1 = new QuestBasic("Quest 1", "Description 1", List.of(),  false, null, null, true, true, 0);
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest2 = new QuestBasic("Quest 2", "Description 2", List.of(task),  false, null, null, true, false, 0);

        GameState.quests.quests.clear();
        GameState.quests.quests.add(quest1);
        GameState.quests.quests.add(quest2);

        SaveHandler.save(GameState.class, "test/saves/quests");

        GameState.quests.quests.clear();

        SaveHandler.load(GameState.class, "test/saves/quests");

        assertTrue(GameState.quests.quests.getFirst().isFailed());
        assertEquals("Description 2", GameState.quests.quests.getLast().getQuestDescription());
        assertEquals(1, GameState.quests.quests.getLast().getTasks().size());

        GameState.quests.quests.clear();

        SaveHandler.delete(GameState.class, "test/saves/quests");
    }
}
