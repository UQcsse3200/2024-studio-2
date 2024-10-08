package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the current in-game time on the screen.
 */
public class TimeDisplay extends UIComponent {
    private static final float Z_INDEX = 2f;
    private Table table;
    private Label timeLabel;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

        // Create the label to display time
        timeLabel = new Label("", skin);

        // Scale the font size of the label to make it bigger
        timeLabel.setFontScale(1.5f); // 1.5 times bigger

        // Add the time label to the table and adjust the padding
        table.add(timeLabel).padTop(5f).padRight(100f); // Reduce padRight to move it more left

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Get the current time of day from the DayNightCycle and update the label
        float timeOfDay = DayNightCycle.getTimeOfDay();
        int hour = (int) (timeOfDay * 24);
        String timeText = String.format("Time: %02d:00", hour);
        timeLabel.setText(timeText);
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
