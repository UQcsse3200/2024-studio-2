package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PopupDialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final TextButton nextButton;
    private final Image animalImage;
    private final Image healthBarImage1;
    private final Image healthBarImage2;

    private final float dialogWidth;
    private final float dialogHeight;

    private String[] titles;
    private String[] content;
    private int currentIndex = 0;

    public PopupDialogBox(String[] titles, String[] content, String animalImagePath, Skin skin, float dialogWidth, float dialogHeight) {
        super("", skin);
        this.titles = titles;
        this.content = content;
        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;

        // Load the animal image
        Texture animalTexture = new Texture(Gdx.files.internal(animalImagePath));
        animalImage = new Image(animalTexture);

        // Load the health bar images
        Texture healthTexture = new Texture(Gdx.files.internal("images/health_bar_x1.png"));
        healthBarImage1 = new Image(healthTexture);
        healthBarImage2 = new Image(healthTexture);

        // Initialize labels and buttons
        titleLabel = new Label(titles[currentIndex], skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content[currentIndex], skin);
        contentLabel.setWrap(true);

        nextButton = new TextButton("Confirm", skin);
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

        // Layout: Image on the left, text and health bars on the right
        Table rightTable = new Table();

        // Text on top 1/3 of the right side
        Table textTable = new Table();
        textTable.add(contentLabel).width(dialogWidth * 0.3f).top().expandY();
        rightTable.add(textTable).width(dialogWidth * 0.3f).expandX().row();

        // Health bars on bottom 2/3 of the right side
        Table healthTable = new Table();
        healthTable.add(healthBarImage1).width(dialogWidth * 0.3f).height(dialogHeight * 0.2f).padTop(10).row();
        healthTable.add(healthBarImage2).width(dialogWidth * 0.3f).height(dialogHeight * 0.2f).padTop(10);
        rightTable.add(healthTable).expandX().fillY().top();

        // Add image and right table to the content layout
        Table innerTable = new Table();
        innerTable.add(animalImage).width(dialogWidth * 0.4f).height(dialogHeight * 0.8f).padRight(20);
        innerTable.add(rightTable).width(dialogWidth * 0.6f).expandY().top();

        // Add inner table and next button to contentTable
        contentTable.add(innerTable).expandX().center().row();
        contentTable.add(nextButton).padTop(20);

        getContentTable().add(contentTable).expand().center();

        // Set the size of the dialog box
        setSize(dialogWidth, dialogHeight);
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
