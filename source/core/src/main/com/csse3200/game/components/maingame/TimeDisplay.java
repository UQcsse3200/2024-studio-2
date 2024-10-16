package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.InGameTime;

/**
 * Displays the current in-game time on the screen with a toggle between 24-hour and 12-hour format.
 */
public class TimeDisplay extends UIComponent {
    private static final float Z_INDEX = 2f;
    private Table table;
    private CustomButton timeButton;
    private final InGameTime inGameTime;
    private boolean is24HourFormat = true; // Tracks whether the display is in 24-hour format

    public TimeDisplay() {
        this.inGameTime = ServiceLocator.getInGameTime();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();  // Position the table in the top right
        table.setFillParent(true);  // Make sure the table takes up the entire screen space

        // Initialize the time button with default text
        timeButton = new CustomButton("Time: 00:00", skin);
        timeButton.setButtonSize(220f, 50f); // Resize the button to a smaller size that fits the UI

        // Update the button text with the current time
        updateTimeText();

        // Add the time button to the table and add padding
        table.add(timeButton).padTop(10f).padRight(100f).width(220f).height(50f);

        // Add click listener to toggle between 24-hour and 12-hour format
        timeButton.addClickListener(() -> {
            is24HourFormat = !is24HourFormat; // Toggle the format
            updateTimeText(); // Update the text after the format change
        });

        // Add the table to the stage
        stage.addActor(table);
    }

    /**
     * Updates the time displayed on the button based on the current format (12-hour or 24-hour).
     */
    private void updateTimeText() {
        long currentTime = inGameTime.getTime();
        float timeOfDay = ((float) (currentTime % DayNightCycle.DAY_LENGTH)) / DayNightCycle.DAY_LENGTH;
        int hour24 = (int) (timeOfDay * 24); // 24-hour format

        String timeText;
        if (is24HourFormat) {
            timeText = String.format("Time: %02d:00", hour24); // 24-hour format
        } else {
            // Convert to 12-hour format with AM/PM
            int hour12 = hour24 % 12 == 0 ? 12 : hour24 % 12;
            String period = hour24 < 12 ? "AM" : "PM";
            timeText = String.format("Time: %02d:00 %s", hour12, period); // 12-hour format
        }

        timeButton.setLabelText(timeText); // Update the button label text
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Update the time every frame
        updateTimeText();
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
