package com.csse3200.game.components.quests;

import java.util.ArrayList;
import java.util.List;

public class QuestBuilder {
    String name;
    String description;
    List<Task> tasks = new ArrayList<>();
    List<DialogueKey> dialogueKeys = new ArrayList<>();
    List<String> triggers = new ArrayList<>();
    boolean active = false;
    boolean failed = false;
    int index = 0;
    List<String> follow = new ArrayList<>();

    public QuestBuilder(String name) {
        this.name = name;
    }

    public Quest build() {
        return new Quest(
                name,
                description,
                tasks,
                dialogueKeys,
                triggers.toArray(new String[]{}),
                active,
                failed,
                index,
                follow.toArray(new String[]{})
        );
    }

    public QuestBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QuestBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public QuestBuilder addTask(Task task) {
        this.tasks.add(task);
        return this;
    }

    public QuestBuilder addDialogueKey(DialogueKey dialogueKey) {
        this.dialogueKeys.add(dialogueKey);
        return this;
    }

    public QuestBuilder addTrigger(String trigger) {
        this.triggers.add(trigger);
        return this;
    }

    public QuestBuilder setActive(boolean active) {
        this.active = active;
        return this;
    }

    public QuestBuilder setIndex(int index) {
        this.index = index;
        return this;
    }

    public QuestBuilder addFollowQuest(String follow) {
        this.follow.add(follow);
        return this;
    }

    public QuestBuilder setFailed(boolean failed) {
        this.failed = failed;
        return this;
    }
}
