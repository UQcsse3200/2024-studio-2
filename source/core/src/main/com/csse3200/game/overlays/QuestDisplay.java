package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.quests.Quest;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.Task;

import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A public class that represents the settings menu display and logic for managing and showing quests onto the screen.
 * This handles the user interface components.
 * It manages the layout and rendering of quest-related information.
 */
public class QuestDisplay extends UIComponent {
    /**
     * The background table for UI elements.
     */
    private Table background;
    /**
     * The root table for UI elements.
     */
    private Table rootTable;
    /**
     * Screen that this
     */
    private PausableScreen screen;
    /** Keeps track of number of quests per page */
    private static final int NUM_QUESTS_PER_PAGE = 3;
    /**Current page tracker */
    private int currPage = 0;
    /**List of quests */
    private List<Quest> listOfQuests = new ArrayList<>();
    /** Makes SonarCloud happy*/
    private static final String TITLE_TEXT = "title";


    /** Comparator to sort quests showing active, completed then failed quests */
    private final Comparator<Quest> questComparator = (q1, q2) -> {
        if (q1.isQuestCompleted() && !q2.isQuestCompleted()) {
            return 1;
        } else if (!q1.isQuestCompleted() && q2.isQuestCompleted()) {
            return -1;
        } else if (q1.isFailed() && !q2.isFailed()) {
            return 1;
        } else if (!q1.isFailed() && q2.isFailed()) {
            return -1;
        } else {
            return 0;
        }
    };

    /** Creates a new quest display and sets the screen that can be paused. */
    public QuestDisplay(PausableScreen screen) {
        super();
        this.screen = screen;
    }

    /**Creates the display */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private Table makeSliders() {
        QuestManager questManager = (QuestManager) ServiceLocator.getEntityService().getSpecificComponent(QuestManager.class);
        Table table = new Table();

        addQuestsCompletedLabel(table);

        if (questManager != null) {
            listOfQuests = questManager.getActiveQuests();
            listOfQuests.sort(questComparator);

            int start = currPage * NUM_QUESTS_PER_PAGE;
            int end = Math.min(start + NUM_QUESTS_PER_PAGE, listOfQuests.size());
            List<Quest> questDisplay = listOfQuests.subList(start, end);

            for (Quest quest : questDisplay) {
                if (!quest.isSecret()) {
                    addQuestComponents(table, quest);
                }
            }

            updateQuestsCompletedLabel(table, listOfQuests);
        }
        return table;
    }

    /**
     * Creates and adds a label to the table that shows the number of completed quests.
     * @param table Table where the label will be added.
     */
    private void addQuestsCompletedLabel(Table table) {
        Label questsCompletedLabel = new Label("Quests Completed: 0", skin, TITLE_TEXT);
        questsCompletedLabel.setColor(Color.BLACK);
        questsCompletedLabel.setFontScale(0.6f);
        questsCompletedLabel.setAlignment(1); // Center alignment

        table.add(questsCompletedLabel).expandX().center().padBottom(10f).row();
    }

    /*
     * Adds quest components such as progress bars, checkboxes, and hints to display.
     * @param table The table to which quest components are added to.
     * @param quest The quest for which components are being added to.
     */
    private void addQuestComponents(Table table, Quest quest) {
        Table questRow = new Table();

        Color questShownActive = determineQuestColor(quest);
        addTitle(questRow, quest, questShownActive);
        addProgressBar(questRow, quest);
        addCheckbox(questRow, quest);

        table.add(questRow).expandX().fillX().padBottom(10f);
        table.row();

        addQuestInfo(table, quest);
    }

    /**
     * Adds the title of the quest.
     * @param questRow      The table representing the row for the quest.
     * @param quest         The quest object containing information about the quest.
     * @param questShownActive The color to display the quest title when it is active.
     */
    private void addTitle(Table questRow, Quest quest, Color questShownActive) {
        Label questTitle = new Label(quest.getQuestName(), skin, TITLE_TEXT, questShownActive);
        questTitle.setFontScaleX(0.5f);
        questRow.add(questTitle).expandX().fillX().padRight(130f).padLeft(10f);
    }

    /**
     * Adds a progress bar for the quests.
     * @param questRow The table representing the row for the quest.
     * @param quest    The quest object containing information about the quest.
     */
    private void addProgressBar(Table questRow, Quest quest) {
        ProgressBar questProgressBar = new ProgressBar(0, quest.getNumQuestTasks(), 1, false, skin);
        questProgressBar.setValue(quest.getProgression());

        questProgressBar.setSize(270, 20);
        questRow.add(questProgressBar).width(150).height(20).padRight(5f);
    }

    /**
     * Adds a checkbox indicating the completion status of the quest .
     * @param questRow The table representing the row for the quest.
     * @param quest    The quest object containing information about the quest.
     */
    private void addCheckbox(Table questRow, Quest quest) {
        CheckBox questCheckbox = new CheckBox("", skin);
        questCheckbox.setChecked(quest.isQuestCompleted());
        questRow.add(questCheckbox).padRight(10f);
    }

    /**
     * Adds quest description and hint labels for each task within quest labels to the table.
     * @param table The table to which task hints are added to.
     * @param quest The quest whose task hints are to be added to.
     */


    private void addQuestInfo(Table table, Quest quest) {
        Label descLabel = new Label(quest.getQuestDescription(), skin, "default");
        descLabel.setColor(Color.GRAY);
        descLabel.setFontScale(0.70f);
        descLabel.setWrap(true);
        descLabel.setWidth(100);


        table.add(descLabel).expandX().fillX().colspan(10).padLeft(10f).padRight(135f); // Match padding with questTitle


        table.row().padTop(1f);

        for (Task task : quest.getTasks()) {
            Label hintLabel = new Label("Hint: " + task.getHint(), skin, "default");
            hintLabel.setColor(Color.GRAY);
            hintLabel.setFontScale(0.70f);
            hintLabel.setWrap(true);
            hintLabel.setWidth(300);



            table.add(hintLabel).expandX().fillX().colspan(10).padLeft(10f).padRight(135f); // Match padding
            table.row().padTop(1f);
        }
    }

    /**
     * Updates and displays the number of quests completed.
     * @param table The table containing the label to update.
     * @param questList The list of quests.
     */
    private void updateQuestsCompletedLabel(Table table, List<Quest> questList) {
        long completedCount = questList.stream().filter(Quest::isQuestCompleted).count();
        Label questsCompletedLabel = (Label) table.getChildren().get(0);
        questsCompletedLabel.setText("Quests Completed: " + completedCount);
    }



    /**
     * Returns the color representing the quests' status.
     * @param quest The quest for which the color is based upon.
     */
    private Color determineQuestColor(Quest quest) {
        if (quest.isQuestCompleted()) {
            return Color.GOLDENROD;
        } else if (quest.isFailed()) {
            return Color.RED;
        } else {
            return Color.BROWN;
        }
    }




    /**
     * Creates and returns a table containing menu buttons for navigating the quest menu.
     * @return A table containing the menu buttons.
     */


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


        nextPage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ((currPage + 1) * NUM_QUESTS_PER_PAGE < listOfQuests.size()) {
                    currPage++;
                    refreshTheUI();
                }
            }
        });


        prevPage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currPage > 0) {
                    currPage--;
                    refreshTheUI();
                }
            }
        });


        Table table = new Table();


        table.add(prevPage).expandX().left().padRight(10f).padTop(-100f);
        table.add(exitBtn).expandX().center().padRight(10f).padTop(-100f);
        table.add(nextPage).expandX().right().padLeft(10f).padTop(-100f);


        table.padTop(10f);
        return table;
    }

    /** Refreshes the UI so quest display components display properly without collision*/
    private void refreshTheUI() {
        rootTable.clearChildren();

        Label title = new Label("QUESTS", skin, TITLE_TEXT);
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        float paddingTop = 28f;
        rootTable.add(title).center().padTop(paddingTop).row();

        Table questsTable = makeSliders();
        if (questsTable.hasChildren()) {
            rootTable.add(questsTable).padBottom(560f - questsTable.getRows() * 40f).padTop(paddingTop).row();
        }

        Table menuBtns = makeMenuBtns();
        rootTable.add(menuBtns).center().padTop(10f);

        stage.addActor(rootTable);
    }




    /**
     * Handles exiting the quest menu
     */

    private void exitMenu() {
        screen.removeOverlay();
    }

    /**
     * Adds actors to the stage for displaying the quest UI components.
     */

    private void addActors() {
        Label title = new Label("QUESTS", skin, TITLE_TEXT);
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        Image questsBackGround = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/Quest_SBG.png", Texture.class));
        background = new Table();
        background.setFillParent(true);
        background.add(questsBackGround).center();
        stage.addActor(background);

        rootTable = new Table();
        rootTable.setSize(background.getWidth(), background.getHeight());
        rootTable.setFillParent(true);

        refreshTheUI();

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