package com.csse3200.game.components.quests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;



class QuestManagerTest {

    private QuestManager questManager;

    @BeforeEach
    void setUp() {
        questManager = new QuestManager();
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1);
        //add tasks
        List<Task> tasks = List.of(stepsTask);
        QuestBasic quest = new QuestBasic("First Steps", "Take your first steps in this world!", tasks, false, false, null);
        questManager.addQuest(quest);
    }







}
