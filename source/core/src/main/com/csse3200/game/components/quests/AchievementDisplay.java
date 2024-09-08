package com.csse3200.game.components.quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.csse3200.game.components.quests.AchievementManager.saveAchievements;

public class AchievementDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(AchievementDisplay.class);
    private final GdxGame game;
    private Table rootTable;
    /** Array to store achievements. */
    private final Array<Achievement> achievements;

    public AchievementDisplay(GdxGame game) {
        super();
        this.game = game;
        AchievementManager achievementManager = new AchievementManager();
        this.achievements =  achievementManager.getAchievements();
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
        Table table = new Table();
        for (Achievement achievement : achievements){
            if (achievement.isCompleted()) {
                TextButton achievementButton = new TextButton(achievement.getQuestName(), skin);
                achievementButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Help button clicked");
                        achievement.setSeen();
                        if(achievement.isSeen()){
                            achievementButton.setColor(Color.GREEN);
                        }
                    }

                });
                addButtonElevationEffect(achievementButton);
                table.add(achievementButton);
                if (!achievement.isSeen()) {
                    logger.info("GOLD");
                    achievementButton.setColor(Color.GOLD);
                }
                table.row();
            }
        }
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
        saveAchievements(achievements);
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(new Texture("images/BackgroundSplash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    /**
     * Clear the UI elements on the display
     */
    @Override
    public void dispose() {
        saveAchievements(achievements);
        rootTable.clear();
        super.dispose();
    }

    /**
     * Adds an elevation effect to buttons when hovered.
     */
    private void addButtonElevationEffect(TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
                //logger.info("Hover feature activated"); uncomment this if you want to check hover feature
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }
}
