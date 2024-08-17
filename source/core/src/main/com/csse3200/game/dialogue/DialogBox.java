package com.csse3200.game.dialogue;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class used to represent the DialogBox displayed on screen
 *
 * The DialogBox will be called upon certain events triggers with NPCs and other mobs,
 * illustrating information for game players to learn
 *
 * @author GPA Reapers (Team 11 UI(Stats))
 */
public class DialogBox extends Dialog {
    private Skin skin;
    public DialogBox(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
    }

    /**
     * Adds the dialog box entity to the stage to be rendered
     *
     * @param stage - The stage on which the dialog box will be rendered onto
     */
    public void showDialogBox(Stage stage) {

    }

    /**
     * Inputs the string to be outputted into the dialog box into the dialog class
     *
     * @param dialog - The string to be outputted into the dialog box
     */
    public void addText(String dialog) {

    }

}
