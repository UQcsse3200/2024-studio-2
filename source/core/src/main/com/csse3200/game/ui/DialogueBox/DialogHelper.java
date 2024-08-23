package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.ui.DialogueBox.DialogBox;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

import  com.csse3200.game.ui.UIComponent.*;

public class DialogHelper extends Component {
    private Stage stage;
    private DialogBox dialogBox;
    private Skin skin;

    public DialogHelper() {
        this.stage = ServiceLocator.getRenderService().getStage();
    }

    public void displayDialog(String[] titles, String[] content) {
        dialogBox = new DialogBox(titles, content, skin);
        dialogBox.display(stage);
    }
}