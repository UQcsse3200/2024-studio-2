package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class DialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final TextButton nextButton;
    private final Image dialogImage;
    private final float imageWidth;
    private final float imageHeight;
    private final float imageX;
    private final float imageY;

    private String[] titles;
    private String[] content;
    private int currentIndex = 0;

    // Updated constructor to include image size and position
    public DialogBox(String[] titles, String[] content, Skin skin, float imageWidth, float imageHeight, float imageX, float imageY) {
        super("", skin);
        this.titles = titles;
        this.content = content;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageX = imageX;
        this.imageY = imageY;

        // Load and initialize the image
        Texture texture = new Texture(Gdx.files.internal("images/health_bar_x1.png"));
        dialogImage = new Image(texture);

        // Initialize labels and buttons
        titleLabel = new Label(titles[currentIndex], skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content[currentIndex], skin);
        contentLabel.setWrap(true);

        nextButton = new TextButton("Continue", skin);
        addActionListeners();
        createDialogLayout();
    }

    private void addActionListeners() {
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                proceedToNext();
            }
        });
    }

    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);

        // Add the image
        dialogImage.setSize(imageWidth, imageHeight); // Set image size
        dialogImage.setPosition(imageX, imageY);       // Set image position

        contentTable.add(dialogImage).expandX().center().row(); // Add image to table
        contentTable.add(titleLabel).padBottom(15).row();
        contentTable.add(contentLabel).width(400).row();

        // Add next button at the bottom
        contentTable.add(nextButton).padTop(20);

        getContentTable().add(contentTable).expand().center();
        setSize(500, 300);  // Adjust size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
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
