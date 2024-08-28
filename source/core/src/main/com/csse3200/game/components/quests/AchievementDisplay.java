package com.csse3200.game.components.quests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(AchievementDisplay.class);
    private final GdxGame game;
    private Table rootTable;

    public AchievementDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    /**
     * Creates and populates the UI with UI elements.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Add UI elements to the display. Elements will be populated in their own Tables, before being added to a rootTable.
     */
    private void addActors() {
        Label title = new Label("Achievements", skin, "title");
        Table menuBtns = makeMenuBtns();
        Table achievementsTable = makeAchievementsTable();

        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.add(title).expandX().top().padTop(20f);
        rootTable.row().padTop(30f);

        rootTable.add(achievementsTable).expandX().expandY();

        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    /**
     * Creates the Table for the visual representation of completed achievements.
     * @return The Table showing achievements.
     */
    private Table makeAchievementsTable() {

        Label achievementName = new Label("Test Achievement", skin);
        Label achievementDescription = new Label("Lorem ipsum", skin);

        Table table = new Table();

        table.add(achievementName);
        table.row();
        table.add(achievementDescription);

        return table;
    }

    /**
     * Creates the table for the exit button.
     * @return The Table showing the exit button.
     */
    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });


        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        return table;
    }

    /**
     * Sets the current game screen back to the main menu.
     */
    private void exitMenu() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Clear the UI elements on the display
     */
    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}
