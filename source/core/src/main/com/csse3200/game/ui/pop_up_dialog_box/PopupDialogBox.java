package com.csse3200.game.ui.pop_up_dialog_box;

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
    private final TextButton backButton;
    private final Image animalImage;
    private Table statsTable;
    private Runnable callback;

    private final float dialogWidth;
    private final float dialogHeight;

    private String[] titles;
    private String[] content;
    private int currentIndex = 0;

    private int[] speedStats;
    private int[] defenseStats;
    private int[] strengthStats;

    private int animalIndex = 0; // Default to bird stats, should be updated based on selection

    /**
     * Constructs a new PopupDialogBox.
     *
     * @param titles         Array of titles for each page of content.
     * @param content        Array of content for each page.
     * @param animalImagePath Path to the image of the animal to be displayed.
     * @param skin           Skin to be used for the UI elements.
     * @param dialogWidth    Width of the dialog box.
     * @param dialogHeight   Height of the dialog box.
     * @param speedStats     Array of speed stats for different animals.
     * @param defenseStats   Array of defense stats for different animals.
     * @param strengthStats  Array of strength stats for different animals.
     */
    public PopupDialogBox(String[] titles, String[] content, String animalImagePath, Skin skin, float dialogWidth, float dialogHeight,
                          int[] speedStats, int[] defenseStats, int[] strengthStats) {
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

        // Initialize stats arrays
        this.speedStats = speedStats;
        this.defenseStats = defenseStats;
        this.strengthStats = strengthStats;

        // Initialize labels and buttons
        titleLabel = new Label(titles[currentIndex], skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content[currentIndex], skin);
        contentLabel.setWrap(true);

        nextButton = new TextButton("Confirm and Start game", skin);
        backButton = new TextButton("Back", skin);
        addActionListeners();
        createDialogLayout();
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    /**
     * Adds action listeners to the buttons in the dialog.
     */
    private void addActionListeners() {
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
                if (callback != null) {
                    callback.run();
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(); // Hide the dialog
                // You can add additional logic here if needed when going back
            }
        });
    }

    /**
     * Creates and configures the layout of the dialog box.
     */
    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);

        // Layout: Image on the left, text and stats table on the right
        Table rightTable = new Table();

        // Text on top 1/3 of the right side
        Table textTable = new Table();
        textTable.add(contentLabel).width(dialogWidth * 0.5f)
                .padTop(30)
                .top().expandY();
        rightTable.add(textTable).width(dialogWidth * 0.5f).expandX().row();

        // Stats table under text
        Table statsContainer = new Table();
        statsTable = new Table();
        statsTable.add(new Label("STATS", getSkin())).colspan(2).padBottom(10).row();
        statsContainer.add(statsTable).expand().top().row();
        rightTable.add(statsContainer).width(dialogWidth * 0.5f).expandX().fillY().top();

        // Add image and right table to the content layout
        Table innerTable = new Table();
        innerTable.add(animalImage).width(dialogWidth * 0.4f).height(dialogHeight * 0.8f).padRight(20);
        innerTable.add(rightTable).width(dialogWidth * 0.6f).expandY().top();

        // Add inner table and next button to contentTable
        contentTable.add(innerTable).expandX().center().row();
        contentTable.add(nextButton).padTop(20);
        contentTable.add(backButton).padTop(10);

        getContentTable().add(contentTable).expand().center();

        // Set the size and position of the dialog box
        setSize(dialogWidth, dialogHeight);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);

        updateStatsTable(); // Update stats table with the current animal's stats
    }

    /**
     * Updates the stats table with the current animal's stats.
     */
    private void updateStatsTable() {
        // Clear previous stats
        statsTable.clear();
        statsTable.add(new Label("STATS", getSkin())).colspan(2).padBottom(10).row();
        statsTable.add(new Label("SPEED:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(speedStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("DEFENSE:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(defenseStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("STRENGTH:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(strengthStats[animalIndex]), getSkin())).right().row();
    }

    /**
     * Moves to the next page of content in the dialog box. If all pages are shown, hides the dialog.
     */
    private void proceedToNext() {
        currentIndex++;
        if (currentIndex < titles.length) {
            titleLabel.setText(titles[currentIndex]);
            contentLabel.setText(content[currentIndex]);
            updateStatsTable(); // Update stats table when changing pages
        } else {
            hide();  // Hide dialog when content is done
            if (callback != null) {
                callback.run();  // Execute the callback when dialog is closed
            }
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

    /**
     * Sets the index of the animal for which stats should be displayed.
     *
     * @param animalIndex Index of the selected animal (0 for bird, 1 for croc, 2 for dog).
     */
    public void setAnimalIndex(int animalIndex) {
        this.animalIndex = animalIndex;
        updateStatsTable(); // Update stats immediately with new animal stats
    }
}
