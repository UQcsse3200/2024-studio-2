package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryTest {

    @Test
    void itemCollectionSuccessful() {
        ResourceService resourceService = mock(ResourceService.class);
        Entity player = new Entity();

        Sound mockSound = mock(Sound.class);

        when(resourceService.getAsset("sounds/QuestComplete.wav", Sound.class)).thenReturn(mockSound);

        ServiceLocator.registerResourceService(resourceService);

        QuestManager quest = new QuestManager(player);

        Task task = new Task("collectPotionTest", "", "", 2, 0, false, false);
        quest.addQuest(new Quest("Guide's Request test",
        "Follow the cow's request and collect items.", List.of(task),
        false, new ArrayList<>(), null, true, false, 0, null));

        player.getEvents().trigger("collectPotionTest");
        assertFalse(quest.getQuest("Guide's Request test").isQuestCompleted());

        player.getEvents().trigger("collectPotionTest");
        assertTrue(quest.getQuest("Guide's Request test").isQuestCompleted());

    }
}