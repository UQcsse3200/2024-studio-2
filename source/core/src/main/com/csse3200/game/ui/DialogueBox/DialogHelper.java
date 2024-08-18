package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.components.Component;

public class DialogHelper extends Component {
    private Stage stage;
    private DialogBox dialogBox;
    private Skin skin;

    public DialogHelper(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void displayDialog(String[] titles, String[] content) {
        dialogBox = new DialogBox(titles, content, skin);
        dialogBox.display(stage);
    }
}