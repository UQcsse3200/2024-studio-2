package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A customizable alert box that displays a message and an "OK" button to dismiss.
 * This alert box matches the template of PopupDialogBox for consistency.
 */
public class AlertBox extends Dialog {
    private final Label messageLabel;
    private final TextButton okButton;

    private final float dialogWidth;
    private final float dialogHeight;

    /**
     * Constructs a new AlertBox.
     *
     * @param message       Message to be displayed in the alert box.
     * @param skin          Skin to be used for the UI elements.
     * @param dialogWidth   Width of the dialog box.
     * @param dialogHeight  Height of the dialog box.
     */
    public AlertBox(String message, Skin skin, float dialogWidth, float dialogHeight) {
        super("", skin);

        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;

        // Set the background image for the dialog box
        Texture bgTexture = new Texture(Gdx.files.internal("images/animal/lightblue.png"));
        Image backgroundImage = new Image(bgTexture);
        this.getContentTable().setBackground(backgroundImage.getDrawable());

        // Initialize label and button
        messageLabel = new Label(message, skin);
        messageLabel.setWrap(true);
        messageLabel.setColor(Color.CYAN);

        okButton = new TextButton("OK", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(); // Dismiss the alert box
            }
        });

        createDialogLayout();
    }

    /**
     * Creates and configures the layout of the alert box.
     */
    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);

        // Add message label and OK button to the content table
        contentTable.add(messageLabel).width(dialogWidth * 0.8f).padBottom(20).row();
        contentTable.add(okButton).padTop(10);

        getContentTable().add(contentTable).expand().center();

        // Set the size and position of the dialog box
        setSize(dialogWidth, dialogHeight);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
    }

    /**
     * Displays the alert box on the specified stage.
     *
     * @param stage The stage on which to display the alert box.
     */
    public void display(Stage stage) {
        stage.addActor(this);
    }
}
