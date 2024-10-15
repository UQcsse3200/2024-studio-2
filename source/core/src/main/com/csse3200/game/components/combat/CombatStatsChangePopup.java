package com.csse3200.game.components.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A popup UI that displays multiple popup messages when stats change.
 */
public class CombatStatsChangePopup extends UIComponent {
    /** List of currently displayed stat difference labels. */
    private List<Label> statsChangePopups = new ArrayList<>();

    /** Popup display boundaries for player */
    private static final float PLAYER_MIN_X = 0.2f;
    private static final float PLAYER_MAX_X = 0.3f;

    /** Popup display boundaries for enemy */
    private static final float ENEMY_MIN_X = 0.7f;
    private static final float ENEMY_MAX_X = 0.8f;

    private static final float MIN_Y = 0.5f;
    private static final float MAX_Y = 0.6f;

    /**
     * Adds the listener for the label to trigger the popup.
     */
    private void addActors() {
        entity.getEvents().addListener("playerHealthStatsChangePopup", statsDiff -> createHealthStatsChangePopup((int) statsDiff, true));
        entity.getEvents().addListener("enemyHealthStatsChangePopup", statsDiff -> createHealthStatsChangePopup((int) statsDiff, false));
        entity.getEvents().addListener("playerHungerStatsChangePopup", statsDiff -> createHungerStatsChangePopup((int) statsDiff, true));
        entity.getEvents().addListener("enemyHungerStatsChangePopup", statsDiff -> createHungerStatsChangePopup((int) statsDiff, false));
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
     * Convert numeric health stats changes to string with polarity and select the text color
     *
     * @param statsDiff the numeric change in stats
     * @param isPlayer whether to display the stats changes around the player or enemy
     */
    public void createHealthStatsChangePopup(int statsDiff, boolean isPlayer) {
        if (statsDiff == 0) return;
        String statsDiffText = (statsDiff > 0) ? "+" + statsDiff : Integer.toString(statsDiff);
        CharSequence popupText = String.format("HP%s", statsDiffText);
        // Swap health change isPlayer because target applies damage to itself
        createStatsChangePopup(popupText, (statsDiff > 0) ? Color.FOREST : Color.RED, (statsDiff > 0) == isPlayer);
    }

    /**
     * Convert numeric hunger stats changes to string with polarity and select the text color
     * @param statsDiff the numeric change in stats
     * @param isPlayer whether to display the stats changes around the player or enemy
     */
    private void createHungerStatsChangePopup(int statsDiff, boolean isPlayer) {
        if (statsDiff == 0) return;
        String statsDiffText = (statsDiff > 0) ? "+" + statsDiff : Integer.toString(statsDiff);
        CharSequence popupText = String.format("HGR%s", statsDiffText);
        createStatsChangePopup(popupText, Color.ORANGE, isPlayer);
    }

    /**
     * Creates and displays a new stats difference popup label.
     * @param popupText The text to display in the popup.
     * @param textColor The text color to display
     * @param isPlayer whether to display the stats changes around the player or enemy
     */
    private void createStatsChangePopup(CharSequence popupText, Color textColor, boolean isPlayer) {
        // Create the label
        Label statsChangePopup = new Label(popupText, skin, "title", textColor);
        statsChangePopup.setFontScale(Math.max(Gdx.graphics.getHeight() / 1000f, 1f));
        statsChangePopups.add(statsChangePopup);
        stage.addActor(statsChangePopup);

        // Randomize position within defined boundaries
        float randomX, randomY;
        randomY = MathUtils.random(MIN_Y, MAX_Y);
        if (isPlayer) {
            randomX = MathUtils.random(PLAYER_MIN_X, PLAYER_MAX_X);
        } else {
            randomX = MathUtils.random(ENEMY_MIN_X, ENEMY_MAX_X);
        }
        statsChangePopup.setPosition(randomX * Gdx.graphics.getWidth(), randomY * Gdx.graphics.getHeight());

        float staggerDelay = 0.5f * statsChangePopups.size();

        // Label animations
        statsChangePopup.addAction(Actions.sequence(
                Actions.hide(),
                Actions.delay(staggerDelay),
                Actions.show(),
                Actions.moveBy(0, 50, 1f),
                Actions.fadeOut(1f),
                Actions.run(() -> disposePopup(statsChangePopup))
        ));
    }

    /**
     * Disposes of the specified popup message.
     * @param statsChangePopup The popup to dispose.
     */
    private void disposePopup(Label statsChangePopup) {
        statsChangePopup.remove();
        statsChangePopups.remove(statsChangePopup);
    }

    /**
     * Draws the popups if any are displayed.
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // handled by the stage
    }

    /**
     * Disposes of all remaining popup messages.
     */
    @Override
    public void dispose() {
        for (Label popup : statsChangePopups) {
            popup.remove();
        }
        statsChangePopups.clear();
        super.dispose();
    }
}
