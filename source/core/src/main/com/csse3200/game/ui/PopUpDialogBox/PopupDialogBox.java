package com.csse3200.game.ui.PopUpDialogBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A customizable popup dialog box that displays animal information and health bars.
 * This dialog box allows navigation through multiple pages of content using a confirm button.
 */
public class PopupDialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final TextButton nextButton;
    private final Image animalImage;
//    private final Image healthBarImage1;
//    private final Image healthBarImage2;

    private final float dialogWidth;
    private final float dialogHeight;

    private String[] titles;
    private String[] content;
    private int currentIndex = 0;

    /**
     * Constructs a new PopupDialogBox.
     *
     * @param titles        Array of titles for each page of content.
     * @param content       Array of content for each page.
     * @param animalImagePath Path to the image of the animal to be displayed.
     * @param skin          Skin to be used for the UI elements.
     * @param dialogWidth   Width of the dialog box.
     * @param dialogHeight  Height of the dialog box.
     */
    public PopupDialogBox(String[] titles, String[] content, String animalImagePath, Skin skin, float dialogWidth, float dialogHeight) {
        super("", skin);
        Texture bgTexture = new Texture(Gdx.files.internal("images/animal/lightblue.png")); // Load the background image
        Image backgroundImage = new Image(bgTexture); // Create an Image from the texture
        this.getContentTable().setBackground(backgroundImage.getDrawable());
        this.titles = titles;
        this.content = content;
        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;

        // Load the animal image
        Texture animalTexture = new Texture(Gdx.files.internal(animalImagePath));
        animalImage = new Image(animalTexture);

//        // Load the health bar images
//        Texture healthTexture = new Texture(Gdx.files.internal("images/health_bar_x1.png"));
//        healthBarImage1 = new Image(healthTexture);
//        healthBarImage2 = new Image(healthTexture);

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

    /**
     * Adds action listeners to the buttons in the dialog.
     */
    private void addActionListeners() {
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                proceedToNext();
            }
        });
    }

    /**
     * Creates and configures the layout of the dialog box.
     */
    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);

        // Layout: Image on the left, text and health bars on the right
        Table rightTable = new Table();

        // Text on top 1/3 of the right side
        Table textTable = new Table();
        textTable.add(contentLabel).width(dialogWidth * 0.5f)
                .padTop(30)
                .top().expandY();
        rightTable.add(textTable).width(dialogWidth * 0.5f).expandX().row();


//        // Health bars on bottom 2/3 of the right side
//        Table healthTable = new Table();
//        healthTable.add(healthBarImage1).width(dialogWidth * 0.3f).height(dialogHeight * 0.2f).padTop(10).row();
//        healthTable.add(healthBarImage2).width(dialogWidth * 0.3f).height(dialogHeight * 0.2f).padTop(10);
//        rightTable.add(healthTable).expandX().fillY().top();

        // Add image and right table to the content layout
        Table innerTable = new Table();
        innerTable.add(animalImage).width(dialogWidth * 0.4f).height(dialogHeight * 0.8f).padRight(20);
        innerTable.add(rightTable).width(dialogWidth * 0.6f).expandY().top();

        // Add inner table and next button to contentTable
        contentTable.add(innerTable).expandX().center().row();
        contentTable.add(nextButton).padTop(20);

        getContentTable().add(contentTable).expand().center();

        // Set the size and position of the dialog box
        setSize(dialogWidth, dialogHeight);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);
    }

    /**
     * Moves to the next page of content in the dialog box. If all pages are shown, hides the dialog.
     */
    private void proceedToNext() {
        currentIndex++;
        if (currentIndex < titles.length) {
            titleLabel.setText(titles[currentIndex]);
            contentLabel.setText(content[currentIndex]);
        } else {
            hide();  // Hide dialog when content is done
        }
    }

    /**
     * Displays the dialog box on the specified stage.
     *
     * @param stage The stage on which to display the dialog box.
     */
    public void display(Stage stage) {
        stage.addActor(this);
    }
}
