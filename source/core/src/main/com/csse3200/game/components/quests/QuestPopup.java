package com.csse3200.game.components.quests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;


/**
 * A popup UI that displays a popup message when a quest is completed.
 */
public class QuestPopup extends UIComponent {
    /** Flag to see if popup is displaying. */
    private boolean showing = false;
    /** Label for quest completion. */
    private Label questCompleted;
    /** Event service for UI. */
    private final EventService eventService = ServiceLocator.getEventService();
    /** Scale of font size. */
    private static final float FONTSCALE = 2f;

    /**
     * Adds the listener for the label to trigger the popup.
     */
    private void addActors() {
        eventService.getGlobalEventHandler().addListener("questCompleted", this::showQuestCompletedPopup);
    }

    /**
     * Initializes the component and adds event listener.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Displays the label popup and draws it.
     */

    private void showQuestCompletedPopup() {
        showing = true;
        draw(null); // Call draw with null since SpriteBatch is unused
        GameState.env.text = "two";
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
            questCompleted = new Label("Quest Completed!", skin,"title",Color.GOLD);
            questCompleted.setFontScale(FONTSCALE);
            stage.addActor(questCompleted);
            questCompleted.getWidth();

            // Position label and calculates position
            float screenHeight = Gdx.graphics.getHeight();
            float screenWidth = Gdx.graphics.getWidth();
            float displayX = (screenWidth / 2) - (questCompleted.getWidth() * FONTSCALE / 2);
            float displayY = (screenHeight / 2) - (questCompleted.getHeight() * FONTSCALE / 2);
            questCompleted.setPosition( displayX, displayY);

            //defines actions for label created
            questCompleted.addAction(Actions.sequence(
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
        if (questCompleted != null) {
            questCompleted.remove();
            questCompleted = null;
        }
        super.dispose();
    }
}
