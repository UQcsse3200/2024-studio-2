package com.csse3200.game.dialogue;

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
    public DialogBox(String title, Skin skin) {
        super(title, skin);
    }
}
