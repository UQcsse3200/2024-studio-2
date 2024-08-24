package com.csse3200.game.Overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.quests.AbstractQuest;
import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.Task;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Settings menu display and logic.
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

    //makes sliders for progression
    private Table makeSliders() {
        QuestManager questManager = (QuestManager) ServiceLocator.getEntityService().getSpecificComponent(QuestManager.class);
        Table table = new Table();

        addQuestsCompletedLabel(table);

        if (questManager != null) {
            List<QuestBasic> questList = questManager.getAllQuests();
            questList.sort(questComparator);

            for (AbstractQuest quest : questList) {
                if (!quest.isAchievement() && !quest.isSecret()) {
                    addQuestComponents(table, quest);
                }
            }

            updateQuestsCompletedLabel(table, questList);
        }
        return table;
    }

    //creates quest completed label
    private void addQuestsCompletedLabel(Table table) {
        Label questsCompletedLabel = new Label("Quests Completed: 0", skin, "title");
        questsCompletedLabel.setColor(Color.BLACK);
        questsCompletedLabel.setFontScale(0.6f);
        table.add(questsCompletedLabel).colspan(2).center().padBottom(10f).row();
    }

    //handles all components such as progress bar, checkbox etc
    private void addQuestComponents(Table table, AbstractQuest quest) {
        Color questShownActive = determineQuestColor(quest);

        Label questTitle = new Label(quest.getQuestName(), skin, "title", questShownActive);
        ProgressBar questProgressBar = new ProgressBar(0, quest.getNumQuestTasks(), 1, false, skin);
        questProgressBar.setValue(quest.getProgression());
        CheckBox questCheckbox = new CheckBox("", skin);
        questCheckbox.setChecked(quest.isQuestCompleted());

        table.add(questTitle).expandX().fillX().padRight(10f);
        table.add(questProgressBar).expandX().fillX().padRight(10f);
        table.add(questCheckbox).padRight(10f);
        table.row().padTop(5f);

        addTaskHints(table, quest);

        table.row().padTop(10f);
    }

    //sorts color
    private Color determineQuestColor(AbstractQuest quest) {
        if (quest.isQuestCompleted()) {
            return Color.GOLD;
        } else if (quest.isFailed()) {
            return Color.RED;
        } else {
            return Color.BROWN;
        }
    }

    //handles hints for each task
    private void addTaskHints(Table table, AbstractQuest quest) {
        for (Task task : quest.getTasks()) {
            Label hintLabel = new Label("Hint: " + task.getHint(), skin, "default");
            hintLabel.setColor(Color.GRAY);

            table.add(hintLabel).expandX().fillX().colspan(3);
            table.row().padTop(5f);
        }
    }

    //updates quests count
    private void updateQuestsCompletedLabel(Table table, List<QuestBasic> questList) {
        long completedCount = questList.stream().filter(AbstractQuest::isQuestCompleted).count();
        Label questsCompletedLabel = (Label) table.getChildren().get(0);
        questsCompletedLabel.setText("Quests Completed: " + completedCount);
    }

    //menu button
    private Table makeMenuBtns() {

        TextButton exitBtn = new TextButton("Leave Menu", skin);
        TextButton nextPage = new TextButton("Next Page", skin);
        TextButton prevPage = new TextButton("Prev Page", skin);


        exitBtn.getLabel().setFontScale(0.8f);
        nextPage.getLabel().setFontScale(0.8f);
        prevPage.getLabel().setFontScale(0.8f);


        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitMenu();
            }
        });


        Table table = new Table();
        table.add(prevPage).expandX().left().padRight(10f);
        table.add(exitBtn).expandX().center().padRight(10f);
        table.add(nextPage).expandX().right().padLeft(10f);

        table.padTop(10f);

        return table;
    }

    private void exitMenu() {
        eventService.globalEventHandler.trigger("removeOverlay");
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


        Table menuBtns = makeMenuBtns();
        Table questsTable = makeSliders();

        rootTable = new Table();
        rootTable.setSize(background.getWidth(), background.getHeight());
        rootTable.setFillParent(true);
        rootTable.add(title).center();

        if (questsTable.hasChildren()) {
            rootTable.row();
            rootTable.add(questsTable).padBottom(560f - questsTable.getRows() * 40f);
        }


        rootTable.row();
        rootTable.add(menuBtns).center().padTop(10f);


        stage.addActor(rootTable);
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
