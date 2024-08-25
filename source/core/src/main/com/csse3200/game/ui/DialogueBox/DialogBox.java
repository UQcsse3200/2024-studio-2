package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class DialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final TextButton confirmButton;
    private String[] titles;
    private String[] content;
    private int currentIndex = 0;

    public DialogBox(String[] titles, String[] content, Skin skin, float width, float height, float xPosition, float yPosition) {
        super("", skin);
        this.titles = titles;
        this.content = content;

        // Initialize labels and buttons
        titleLabel = new Label(titles[currentIndex], skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content[currentIndex], skin);
        contentLabel.setWrap(true);

        confirmButton = new TextButton("Confirm Selection", skin);
        addActionListeners();
        createDialogLayout();

        setSize(width, height);
        setPosition(xPosition, yPosition);
    }

    private void addActionListeners() {
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(); // Close dialog
            }
        });
    }

    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);
        contentTable.add(titleLabel).padBottom(15).row();
        contentTable.add(contentLabel).width(400).row();
        contentTable.add(confirmButton).padTop(20);

        getContentTable().add(contentTable).expand().center();
    }

    private void proceedToNext() {
        currentIndex++;
        if (currentIndex < titles.length) {
            titleLabel.setText(titles[currentIndex]);
            contentLabel.setText(content[currentIndex]);
        } else {
            hide();  // Hide dialog when content is done
        }
    }

    public void display(Stage stage) {
        stage.addActor(this);
    }
}
