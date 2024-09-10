package com.csse3200.game.components.quests;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import javax.sound.midi.Sequence;


/**
 * A popup UI that displays a popup message when a achievement is completed.
 */
public class AchievementPopup extends UIComponent {
    /** Flag to see if popup is displaying. */
    private boolean showing;
    /** Label for achievement completion. */
    private Image popup;

    /** Scale of font size. */
    private static final float FONTSCALE = 2f;

    /**
     * Adds the listener for the label to trigger the popup.
     */

    public AchievementPopup() {
        this.showing = false;
    }

    /**
     * Initializes the component.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("achievementCompleted", this::showPopup);
    }

    /**
     * Displays the label popup and draws it.
     */

    private void showPopup() {
        showing = true;
        generate();
        SaveHandler.save(GameState.class, "saves");
    }

    /**
     * Draws the popup message and creates label.
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
       // handled by the stage
    }

    public void generate() {

        if(showing) {
            popup = new Image(new Texture(Gdx.files.internal("images/logbook-popup.png")));
            //create the label

            // Position label and calculates position
            float screenHeight = Gdx.graphics.getHeight();
            float screenWidth = Gdx.graphics.getWidth();
            float displayX = screenWidth * 0.8f;
            float displayY = screenHeight * 0.8f;
            popup.setPosition(displayX, displayY);
            //defines actions for label created

            SequenceAction sequence = new SequenceAction();
            sequence.addAction(Actions.fadeOut(1f));
            //sequence.addAction(Actions.run(this::dispose));
            popup.addAction(sequence);
            stage.addActor(popup);
        }
    }
    /**
     * Disposes of popup message.
     */
    @Override
    public void dispose() {
        if (popup != null) {
            popup.clear();
            popup.remove();
            popup = null;
        }
        super.dispose();
    }
}
