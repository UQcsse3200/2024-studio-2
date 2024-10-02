package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class QuestManagerTest {
    private QuestManager questManager;
    private EventHandler eventHandler;
    private Entity player;

    @BeforeEach
    void setUp() {
        eventHandler = mock(EventHandler.class);
        ResourceService resourceService = mock(ResourceService.class);
        player = mock(Entity.class);

        Sound mockSound = mock(Sound.class);
        when(resourceService.getAsset("sounds/QuestComplete.wav", Sound.class)).thenReturn(mockSound);


        when(player.getEvents()).thenReturn(eventHandler);
        ServiceLocator.registerResourceService(resourceService);

        questManager = new QuestManager(player);
    }

    @Test
    void AddQuest() {
        QuestBasic quest = new QuestBasic("Test Quest",  "Test Description", List.of(), false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest);

        assertEquals(quest, questManager.getQuest("Test Quest"));
    }

    @Test
    void GetAllQuests() {
        QuestBasic quest1 = new QuestBasic("Quest 1",  "Description 1", List.of(),  false, null, null, true, false, 0, new String[] {});
        QuestBasic quest2 = new QuestBasic("Quest 2",  "Description 2", List.of(),  false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest1);
        questManager.addQuest(quest2);

        List<QuestBasic> quests = questManager.getAllQuests();
        assertTrue(quests.contains(quest1));
        assertTrue(quests.contains(quest2));
    }

    @Test
    void HandleProgressQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest", "Description", List.of(task),  false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest);

        questManager.progressQuest("Test Quest", "testTask");
        assertTrue(quest.isQuestCompleted());
    }

    @Test
    void HandleQuestCompletion() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest",  "Description", List.of(task),  false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest);


        questManager.progressQuest("Test Quest", "testTask");
        verify(eventHandler).trigger("questCompleted");
        verify(eventHandler).trigger("Test Quest");
    }

    @Test
    void HandleFailQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest = new QuestBasic("Test Quest",  "Description", List.of(task),  false, null, null, true, false, 0, new String[] {});
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
    void shouldSaveLoadQuestProgression() {
        QuestBasic quest1 = new QuestBasic("Quest 1",  "Description 1", List.of(),  false, null, null, true, true, 0, new String[] {});
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        QuestBasic quest2 = new QuestBasic("Quest 2",  "Description 2", List.of(task),  false, null, null, true, false, 0, new String[] {});

        GameState.quests.quests.clear();
        GameState.quests.quests.add(quest1);
        GameState.quests.quests.add(quest2);

        SaveHandler.save(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);

        GameState.quests.quests.clear();

        SaveHandler.load(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);

        assertTrue(GameState.quests.quests.getFirst().isFailed());
        assertEquals("Description 2", GameState.quests.quests.getLast().getQuestDescription());
        assertEquals(1, GameState.quests.quests.getLast().getTasks().size());

        GameState.quests.quests.clear();

        SaveHandler.delete(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);
    }

    @Test
    void HandleInvalidQuestProgression() {
        QuestBasic quest = new QuestBasic("Invalid Progression Quest",  "Description", List.of(), false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest);

        questManager.progressQuest("Invalid Progression Quest", "nonexistentTask");

        verify(eventHandler, never()).trigger(anyString());
    }

    @Test
    void TestFinishingMultipleTasks() {
        Task talkToGuide = new Task("talkToGuide", "Talk to the cow", "Speak with the Guide to start your journey.", 1, 0, false, false);
        Task collectPotions = new Task("collectPotions", "Collect Potions", "Collect 5 potions scattered around the kingdom.", 1, 0, false, false);
        QuestBasic guideQuest = new QuestBasic("Guide's Journey",  "Complete various tasks to progress.", List.of(talkToGuide, collectPotions), false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(guideQuest);

        questManager.progressQuest("Guide's Journey", "talkToGuide");
        questManager.progressQuest("Guide's Journey", "collectPotions");
        QuestBasic updatedQuest = questManager.getQuest("Guide's Journey");
        assertTrue(updatedQuest.getTasks().get(0).isCompleted());
        assertTrue(updatedQuest.getTasks().get(1).isCompleted());
        assertTrue(updatedQuest.isQuestCompleted());
    }

}