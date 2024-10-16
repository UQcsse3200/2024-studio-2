package com.csse3200.game.ui.pop_up_dialog_box;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A helper class for displaying popup dialogs.
 * Provides a simple interface for creating and showing a {@link PopupDialogBox} with a single page of content.
 */
public class PopUpHelper {
    private final Skin skin;
    private final Stage stage;

    private int[] speedStats = {100, 40, 80}; // Speed stats for bird, croc, dog
    private int[] defenseStats = {80, 30, 60}; // Defense stats for bird, croc, dog
    private int[] strengthStats = {40, 80, 60}; // Strength stats for bird, croc, dog

    /**
     * Constructs a new PopUpHelper.
     *
     * @param skin  The skin to be used for the UI elements in the dialog.
     * @param stage The stage on which to display the dialog.
     */
    public PopUpHelper(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
    }

    /**
     * Displays a popup dialog with the specified title, content, animal image, and dimensions.
     *
     * @param title           The title of the dialog.
     * @param content         The content to be displayed in the dialog.
     * @param animalImagePath Path to the image of the animal to be displayed.
     * @param width           The width of the dialog box.
     * @param height          The height of the dialog box.
     * @param animalIndex     The index of the selected animal (0 for bird, 1 for croc, 2 for dog).
     * @param callback        A Runnable to be executed when the dialog is closed.
     */
    public void displayDialog(String title, String content, String animalImagePath, float width, float height, int animalIndex, Runnable callback) {
        PopupDialogBox dialogBox = new PopupDialogBox(
                title,
                content,
                animalImagePath,
                skin,
                width,
                height,
                speedStats,
                defenseStats,
                strengthStats
        );
        dialogBox.setAnimalIndex(animalIndex); // Set the correct animal index
        dialogBox.setCallback(callback); // Set the callback to be executed when the dialog is closed
        dialogBox.display(stage);
    }

    // Keep the old method for backward compatibility
    public void displayDialog(String title, String content, String animalImagePath, float width, float height, int animalIndex) {
        displayDialog(title, content, animalImagePath, width, height, animalIndex, null);
    }
}