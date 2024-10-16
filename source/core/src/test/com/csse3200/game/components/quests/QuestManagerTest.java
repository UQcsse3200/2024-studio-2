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
        Quest quest = new Quest.QuestBuilder("Test Quest")
                .setDescription("Test Description")
                .setActive(true)
                .build();
//                ,  "Test Description", List.of(), false, null, null, true, false, 0, new String[] {});
        questManager.addQuest(quest);

        assertEquals(quest, questManager.getQuest("Test Quest"));
    }

    @Test
    void GetAllQuests() {

        Quest quest1 = new Quest.QuestBuilder("Quest 1")
                .setDescription("Description 1")
                .setActive(true)
                .build();

        Quest quest2 = new Quest.QuestBuilder("Quest 2")
                .setDescription("Description 2")
                .setActive(true)
                .build();

        questManager.addQuest(quest1);
        questManager.addQuest(quest2);

        List<Quest> quests = questManager.getAllQuests();
        assertTrue(quests.contains(quest1));
        assertTrue(quests.contains(quest2));
    }

    @Test
    void HandleProgressQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        Quest quest = new Quest.QuestBuilder("Test Quest")
                .setDescription("Description")
                .addTask(task)
                .setActive(true)
                .build();

        questManager.addQuest(quest);

        questManager.progressQuest("Test Quest", "testTask");
        assertTrue(quest.isQuestCompleted());
    }

    @Test
    void HandleQuestCompletion() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        Quest quest = new Quest.QuestBuilder("Test Quest")
                .setDescription("Description")
                .addTask(task)
                .setActive(true)
                .build();

        questManager.addQuest(quest);


        questManager.progressQuest("Test Quest", "testTask");
        verify(eventHandler).trigger("questCompleted");
        verify(eventHandler).trigger("Test Quest");
    }

    @Test
    void HandleFailQuest() {
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        Quest quest = new Quest.QuestBuilder("Test Quest")
                .setDescription("Description")
                .addTask(task)
                .setActive(true)
                .build();questManager.addQuest(quest);

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
        Quest quest1 = new Quest.QuestBuilder("Quest 1")
                .setDescription("Description 1")
                .setActive(true)
                .setFailed(true)
                .build();
//                Quest("Quest 1",  "Description 1", List.of(),  false, null, null, true, true, 0, new String[] {});
        Task task = new Task("testTask", "Test Task", "Description", 1, 0, false, false);
        Quest quest2 = new Quest.QuestBuilder("Quest 2")
                .setDescription("Description 2")
                .addTask(task)
                .setActive(true)
                .build();

        GameState.quests.quests.clear();
        GameState.quests.quests.add(quest1);
        GameState.quests.quests.add(quest2);

        SaveHandler.getInstance().save(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);

        GameState.quests.quests.clear();

        SaveHandler.getInstance().load(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);

        assertTrue(GameState.quests.quests.getFirst().isFailed());
        assertEquals("Description 2", GameState.quests.quests.getLast().getQuestDescription());
        assertEquals(1, GameState.quests.quests.getLast().getTasks().size());

        SaveHandler.getInstance().delete(GameState.class, "test/saves/quests", FileLoader.Location.LOCAL);
    }

    @Test
    void HandleInvalidQuestProgression() {
        Quest quest = new Quest.QuestBuilder("Invalid Progression Quest")
                .setDescription("Description")
                .setActive(true)
                .build();

        questManager.addQuest(quest);

        questManager.progressQuest("Invalid Progression Quest", "nonexistentTask");

        verify(eventHandler, never()).trigger(anyString());
    }

    @Test
    void TestFinishingMultipleTasks() {
        Task talkToGuide = new Task("talkToGuide", "Talk to the cow", "Speak with the Guide to start your journey.", 1, 0, false, false);
        Task collectPotions = new Task("collectPotions", "Collect Potions", "Collect 5 potions scattered around the kingdom.", 1, 0, false, false);
        Quest guideQuest = new Quest.QuestBuilder("Guide's Journey")
                .setDescription("Complete various tasks to progress.")
                .setActive(true)
                .addTask(talkToGuide)
                .addTask(collectPotions)
                .build();

        questManager.addQuest(guideQuest);

        questManager.progressQuest("Guide's Journey", "talkToGuide");
        questManager.progressQuest("Guide's Journey", "collectPotions");
        Quest updatedQuest = questManager.getQuest("Guide's Journey");
        assertTrue(updatedQuest.getTasks().get(0).isCompleted());
        assertTrue(updatedQuest.getTasks().get(1).isCompleted());
        assertTrue(updatedQuest.isQuestCompleted());
    }

}