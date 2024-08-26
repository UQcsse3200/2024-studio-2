package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DialogHelper {
    private final Skin skin;
    private final Stage stage;

    public DialogHelper(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
    }

    public void displayDialog(String title, String content, String animalImagePath, float width, float height) {
        PopupDialogBox dialogBox = new PopupDialogBox(new String[]{title}, new String[]{content}, animalImagePath, skin, width, height);
        dialogBox.display(stage);
    }
}
