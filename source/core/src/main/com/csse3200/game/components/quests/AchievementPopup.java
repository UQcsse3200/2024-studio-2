package com.csse3200.game.components.quests;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;


/**
 * A popup UI that displays a popup message when a achievement is completed.
 */
public class AchievementPopup extends UIComponent {
    /** Flag to see if popup is displaying. */
    private boolean showing = false;
    /** Label for achievement completion. */
    private Label achievementCompleted;
    /** Scale of font size. */
    private static final float FONTSCALE = 2f;

    /**
     * Adds the listener for the label to trigger the popup.
     */
    private void addActors() {
        this.entity.getEvents().addListener("achievementCompleted", this::showAchievementCompletedPopup);

    }
    /**
     * Initializes the component.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Displays the label popup and draws it.
     */

    private void showAchievementCompletedPopup() {
        showing = true;
        draw(null); // Call draw with null since SpriteBatch is unused
        SaveHandler.save(GameState.class, "saves");
    }

    /**
     * Draws the popup message and creates label.
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if(showing) {
            //create the label
            achievementCompleted = new Label("Achievement Completed!", skin,"title",Color.GOLD);
            achievementCompleted.setFontScale(FONTSCALE);
            stage.addActor(achievementCompleted);

            // Position label and calculates position
            float screenHeight = Gdx.graphics.getHeight();
            float screenWidth = Gdx.graphics.getWidth();
            float displayX = (screenWidth / 2) - (achievementCompleted.getWidth() * FONTSCALE / 2);
            float displayY = (screenHeight / 2) - (achievementCompleted.getHeight() * FONTSCALE / 2);
            achievementCompleted.setPosition( displayX, displayY);

            //defines actions for label created
            achievementCompleted.addAction(Actions.sequence(
                    Actions.fadeOut(1f),
                    Actions.run(this::dispose)
            ));

        }
    }
    /**
     * Disposes of popup message.
     */
    @Override
    public void dispose() {
        if (achievementCompleted != null) {
            achievementCompleted.remove();
            achievementCompleted = null;
        }
        super.dispose();
    }
}
