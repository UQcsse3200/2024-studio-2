package com.csse3200.game.overlays;

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

import java.util.Comparator;
import java.util.List;


/**
 * A public class that represents the settings menu display and logic for managing and showing quests onto the screen.
 * This handles the user interface components.
 * It manages the layout and rendering of quest-related information.
 */
public class QuestDisplay extends UIComponent {
    /**
     * The event service used to handle events related to quest display.
     */
    EventService eventService = ServiceLocator.getEventService();
    /**
     * The background table for UI elements.
     */
    private Table background;
    /**
     * The root table for UI elements.
     */
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
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates and updates a table with sliders to display quest progression.
     * @return A table containing sliders for each quest's progression.
     */
    private Table makeSliders() {
        QuestManager questManager = (QuestManager) ServiceLocator.getEntityService().getSpecificComponent(QuestManager.class);
        Table table = new Table();

        addQuestsCompletedLabel(table);

        if (questManager != null) {
            List<QuestBasic> questList = questManager.getAllQuests();
            questList.sort(questComparator);

            for (AbstractQuest quest : questList) {
                if (!quest.isSecret()) {
                    addQuestComponents(table, quest);
                }
            }

            updateQuestsCompletedLabel(table, questList);
        }
        return table;
    }

    /**
     * Creates and adds a label to the table that shows the number of completed quests.
     * @param table Table where the label will be added.
     */
    private void addQuestsCompletedLabel(Table table) {
        Label questsCompletedLabel = new Label("Quests Completed: 0", skin, "title");
        questsCompletedLabel.setColor(Color.BLACK);
        questsCompletedLabel.setFontScale(0.6f);
        table.add(questsCompletedLabel).colspan(2).center().padBottom(10f).row();
    }

    /**
     * Adds quest components such as progress bars, checkboxes, and hints to display.
     * @param table The table to which quest components are added to.
     * @param quest The quest for which components are being added to.
     */
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

        addQuestInfo(table, quest);

        table.row().padTop(10f);
    }

    /**
     * Returns the color representing the quests' status.
     * @param quest The quest for which the color is based upon.
     */
    private Color determineQuestColor(AbstractQuest quest) {
        if (quest.isQuestCompleted()) {
            return Color.GOLDENROD;
        } else if (quest.isFailed()) {
            return Color.RED;
        } else {
            return Color.BROWN;
        }
    }

    /**
     * Adds quest description and hint labels for each task within quest labels to the table.
     * @param table The table to which task hints are added to.
     * @param quest The quest whose task hints are to be added to.
     */
    private void addQuestInfo(Table table, AbstractQuest quest) {
        Label descLabel = new Label(quest.getQuestDescription(), skin, "default");
        descLabel.setColor(Color.GRAY);

        table.add(descLabel).expandX().fillX().colspan(3);
        table.row().padTop(5f);

        for (Task task : quest.getTasks()) {
            Label hintLabel = new Label("Hint: " + task.getHint(), skin, "default");
            hintLabel.setColor(Color.GRAY);

            table.add(hintLabel).expandX().fillX().colspan(3);
            table.row().padTop(5f);
        }
    }

    /**
     * Updates and displays the number of quests completed.
     * @param table The table containing the label to update.
     * @param questList The list of quests.
     */
    private void updateQuestsCompletedLabel(Table table, List<QuestBasic> questList) {
        long completedCount = questList.stream().filter(AbstractQuest::isQuestCompleted).count();
        Label questsCompletedLabel = (Label) table.getChildren().get(0);
        questsCompletedLabel.setText("Quests Completed: " + completedCount);
    }

    /**
     * Creates and returns a table containing menu buttons for navigating the quest menu.
     * @return A table containing the menu buttons.
     */

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

    /**
     * Handles exiting the quest menu
     */

    private void exitMenu() {
        eventService.getGlobalEventHandler().trigger("removeOverlay");
    }

    /**
     * Adds actors to the stage for displaying the quest UI components.
     */

    private void addActors() {
        Label title = new Label("QUESTS", skin, "title");
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        Image questsBackGround = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/Quest_SBG.png", Texture.class));
        background = new Table();
        background.setFillParent(true);
        background.add(questsBackGround).center();
        stage.addActor(background);

        Table menuBtns = makeMenuBtns();
        Table questsTable = makeSliders();

        rootTable = new Table();
        rootTable.setSize(background.getWidth(), background.getHeight());
        rootTable.setFillParent(true);


        float paddingTop = 28f;

        rootTable.add(title).center().padTop(paddingTop);


        if (questsTable.hasChildren()) {
            rootTable.row();
            rootTable.add(questsTable).padBottom(560f - questsTable.getRows() * 40f).padTop(paddingTop);
        }


        rootTable.row();
        rootTable.add(menuBtns).center().padTop(10f);

        stage.addActor(rootTable);
    }

    /**
     * Draws the quest UI onto the screen.
     * @param batch The sprite batch used for drawing.
     */


    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Updates the quest UI based on time.
     */
    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    /**
     * Disposes of assets used by the quest display.
     */

    @Override
    public void dispose() {
        background.clear();
        rootTable.clear();
        super.dispose();
    }
}
