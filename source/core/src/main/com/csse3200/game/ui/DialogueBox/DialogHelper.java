package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DialogHelper {
    private final Skin skin;
    private final Stage stage;

    public DialogHelper(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
    }

    public void displayDialog(String title, String content, float width, float height, float xPosition, float yPosition) {
        DialogBox dialogBox = new DialogBox(new String[]{title}, new String[]{content}, skin, width, height, xPosition, yPosition);
        dialogBox.display(stage);
    }
}
