package com.csse3200.game.ui.PopUpDialogBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A helper class for displaying popup dialogs.
 * Provides a simple interface for creating and showing a {@link PopupDialogBox} with a single page of content.
 */
public class PopUpHelper {
    private final Skin skin;
    private final Stage stage;

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
     */
    public void displayDialog(String title, String content, String animalImagePath, float width, float height) {
        PopupDialogBox dialogBox = new PopupDialogBox(new String[]{title}, new String[]{content}, animalImagePath, skin, width, height);
        dialogBox.display(stage);
    }
}
