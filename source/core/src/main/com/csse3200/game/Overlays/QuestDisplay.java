package com.csse3200.game.Overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.quests.AbstractQuest;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class QuestDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuestDisplay.class);
    EventService eventService = ServiceLocator.getEventService();
    private Table background;
    private Table rootTable;

    /** Comparator to sort quests showing active, completed then failed quests */
    private final Comparator<AbstractQuest> questComparator = (q1, q2) -> {
        if (q1.isActive() && !q2.isActive()) {
            return -1;
        } else if (!q1.isActive() && q2.isActive()) {
            return 1;
        } else if (q1.isQuestCompleted() && !q2.isQuestCompleted()) {
            return -1;
        } else if (!q1.isQuestCompleted() && q2.isQuestCompleted()) {
            return 1;
        } else if (q1.isFailed() && !q2.isFailed()) {
            return -1;
        } else if (!q1.isFailed() && q2.isFailed()) {
            return 1;
        } else {
            return 0;
        }
    };

    public QuestDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        Label title = new Label("Quests", skin, "title");
        Image questsBackGround = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/QuestsBG.png", Texture.class));
        background = new Table();
        background.setFillParent(true);
        background.setDebug(true);
        background.add(questsBackGround).center();
        stage.addActor(background);
        // Create tables for buttons and quests
        Table menuBtns = makeMenuBtns();
        Table quests = makeSliders();
        rootTable = new Table();
        rootTable.setSize(background.getWidth(),background.getHeight());
        rootTable.setFillParent(true);
        //rootTable.setDebug(true);
        rootTable.add(title).center();
        if (quests.hasChildren()) {
            rootTable.row();
            rootTable.add(quests).padBottom(560f-quests.getRows()*40f);
        }
        rootTable.row();
        rootTable.add(menuBtns).center();

        // Add the root table to the stage
        stage.addActor(rootTable);
    }

    private Table makeSliders() {
        QuestManager questManager = (QuestManager) ServiceLocator.getEntityService().getSpecificComponent(QuestManager.class);
        Table table = new Table();
        if (questManager != null) {
            List<AbstractQuest> questList = questManager.getAllQuests();

            // Sort questList to display active, then completed, then failed quests.
            questList.sort(questComparator);

            for (AbstractQuest quest : questList) {
                // Exclude Achievements and secret quests
                if (!quest.isAchievement() && !quest.isSecret()) {

                    // Change Display elements based on if quest is completed or failed
                    Color questShownActive = Color.BROWN;
                    if (quest.isQuestCompleted()) {
                        questShownActive = Color.GOLD;
                    }
                    else if (quest.isFailed()) {
                        questShownActive = Color.RED;
                    }
                    ProgressBar questProgressBar = new ProgressBar(0, quest.getNumQuestTasks(), 1, false, skin);
                    questProgressBar.setValue(quest.getProgression());

                    // Eventually will be ImageButton / TextButton that leads to more info on the quest
                    Label questTitle = new Label(quest.getQuestName(), skin, "title", questShownActive);

                    table.add(questTitle);
                    table.add(questProgressBar).spaceLeft(20f);
                    table.row().padTop(10f);
                }
            }
        }
        return table;
    }

    private Table makeMenuBtns() {
        // Create buttons
        TextButton exitBtn = new TextButton("Leave Menu", skin);

        // Placeholder nextPage and previous Page button will be ImageButton as well
        TextButton nextPage = new TextButton("Next Page", skin);
        TextButton prevPage = new TextButton("Prev Page", skin);

        // Add listeners for button
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitMenu();
            }
        });

        // Layout buttons in a table
        Table table = new Table();
        table.add(prevPage).expandX().left().padRight(20f);
        table.add(exitBtn).expandX().center();
        table.add(nextPage).expandX().right().padLeft(20f);

        return table;
    }

    private void exitMenu() {
        eventService.globalEventHandler.trigger("removeOverlay");
    }


    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        background.clear();
        rootTable.clear();
        super.dispose();
    }
}
