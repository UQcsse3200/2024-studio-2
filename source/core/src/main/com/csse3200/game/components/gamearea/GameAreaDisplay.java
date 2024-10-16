package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.ui.UIComponent;
<<<<<<< HEAD
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;
=======
import com.csse3200.game.entities.factories.PlayerFactory;
>>>>>>> 324047075cc0262f5f8a3003fd5f0243072a2e95

/**
 * Displays the player icon and a larger minimap frame based on the player's image corresponding to its kingdom.
 */
public class GameAreaDisplay extends UIComponent {
    private final String gameAreaName;
    private Label title;
    private Texture playerIconTexture;
    private Texture minimapFrameTexture;
<<<<<<< HEAD
    private PopUpHelper popUpHelper;  // Add PopUpHelper
=======
>>>>>>> 324047075cc0262f5f8a3003fd5f0243072a2e95

    /**
     * Constructs a GameAreaDisplay component.
     *
     * @param gameAreaName the name of the current game area to be displayed
     */
    public GameAreaDisplay(String gameAreaName) {
        this.gameAreaName = gameAreaName;
    }

    /**
     * Initializes the GameAreaDisplay component and calls addActors() to add actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        popUpHelper = new PopUpHelper(skin, stage);  // Initialize PopUpHelper
        addActors();
    }

    /**
     * Adds the title label, player icon, and minimap frame to the UI using a table layout.
     * The player icon and minimap frame are determined based on the selected player's image path,
     * which corresponds to its kingdom.
     */
    private void addActors() {
        title = new Label(this.gameAreaName, skin, "large");

        // Get the player image path from PlayerFactory
        String playerImagePath = GameState.player.selectedAnimalPath;

        // Determine the player icon texture and minimap frame texture based on the player image path
        switch (playerImagePath) {
            case "images/dog.png" -> {
                playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png"));
                minimapFrameTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png"));
            }
            case "images/croc.png" -> {
                playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sea.png"));
                minimapFrameTexture = new Texture(Gdx.files.internal("images/player_icon_sea.png"));
            }
            case "images/bird.png" -> {
                playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sky.png"));
                minimapFrameTexture = new Texture(Gdx.files.internal("images/player_icon_sky.png"));
            }
            default -> {
                playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png")); // Default icon
                minimapFrameTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png")); // Default for minimap
            }
        }

        // Create images for the player icon and the minimap frame
        Image playerIcon = new Image(playerIconTexture);
        Image minimapFrame = new Image(minimapFrameTexture);

        // Add ClickListener to playerIcon
        playerIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalStats();
            }
        });

        // Set the size of the icons
        float titleHeight = title.getPrefHeight();
        float playerIconScaleFactor = 5.5f;   // Size for the player icon
        float minimapScaleFactor = 10f;      // Larger size for the minimap frame

        playerIcon.setSize(titleHeight * playerIconScaleFactor, titleHeight * playerIconScaleFactor);
        minimapFrame.setSize(titleHeight * minimapScaleFactor, titleHeight * minimapScaleFactor);

        // Create a table for the top UI
        Table topTable = new Table();
<<<<<<< HEAD
        Table tableTwo = new Table();
        topTable.setFillParent(true);
        tableTwo.setFillParent(true);

        topTable.top().left();
        tableTwo.bottom().left();
=======
        Table Tabletwo = new Table();
        topTable.setFillParent(true);
        Tabletwo.setFillParent(true);

        // Align the table to the top left corner
//        topTable.top().left();
>>>>>>> 324047075cc0262f5f8a3003fd5f0243072a2e95

        // Add the player icon to the left side of the table
        topTable.add(playerIcon)
                .size(titleHeight * playerIconScaleFactor, titleHeight * playerIconScaleFactor)
                .align(Align.left | Align.top)
                .pad(5); // Padding from the edges
<<<<<<< HEAD

        // Add the minimap frame to the right side of the table
        tableTwo.add(minimapFrame)
                .size(titleHeight * minimapScaleFactor, titleHeight * minimapScaleFactor)
                .align(Align.left | Align.bottom)
                .pad(5); // Padding from the edges

        // Add the table to the stage
        stage.addActor(topTable);
        stage.addActor(tableTwo);
    }

    /**
     * Shows the animal stats popup using PopUpHelper.
     */
    private void showAnimalStats() {
        String playerImagePath = GameState.player.selectedAnimalPath;

        String[] waterAnimalNames = {"Dog", "Crocodile", "Bird"};
        String[] waterAnimalDescriptions = {
                "Dog is loyal and protective. It's great in a forest setting.",
                "Crocodile is strong and stealthy. It thrives in water environments.",
                "Bird is agile and quick, perfect for sky adventures."
        };

        String selectedAnimalName;
        String selectedAnimalDescription;
        String selectedAnimalImage;

        switch (playerImagePath) {
            case "images/dog.png" -> {
                selectedAnimalName = waterAnimalNames[0];
                selectedAnimalDescription = waterAnimalDescriptions[0];
                selectedAnimalImage = "images/dog.png";
            }
            case "images/croc.png" -> {
                selectedAnimalName = waterAnimalNames[1];
                selectedAnimalDescription = waterAnimalDescriptions[1];
                selectedAnimalImage = "images/croc.png";
            }
            case "images/bird.png" -> {
                selectedAnimalName = waterAnimalNames[2];
                selectedAnimalDescription = waterAnimalDescriptions[2];
                selectedAnimalImage = "images/bird.png";
            }
            default -> {
                selectedAnimalName = "Unknown";
                selectedAnimalDescription = "No description available.";
                selectedAnimalImage = "images/dog.png"; // Default image
            }
        }

        // Display the popup with animal stats
        popUpHelper.displayDialog(selectedAnimalName, selectedAnimalDescription, selectedAnimalImage, 600, 400, 0, () -> {
            // Close or change screen if needed after the popup
        });
    }

    /**
=======

        topTable.top().left();

        // Add space to push the title and minimap frame to their respective locations
//        topTable.add().expandX();

        // Add the title in the center
//        topTable.add(title).align(Align.center | Align.top).pad(10);

        // Add space between the title and the minimap frame
//        topTable.add().expandX();

        // Add the minimap frame to the right side of the table
        Tabletwo.add(minimapFrame)
                .size(titleHeight * minimapScaleFactor, titleHeight * minimapScaleFactor)
                .align(Align.left | Align.bottom)
                .pad(5); // Padding from the edges

        Tabletwo.bottom().left();

        // Add the table to the stage
        stage.addActor(topTable);
        stage.addActor(Tabletwo);
    }

    /**
>>>>>>> 324047075cc0262f5f8a3003fd5f0243072a2e95
     * Draws the UI component on the screen.
     *
     * @param batch the SpriteBatch used to draw the component
     */
    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        float offsetX = 1f;
        float offsetY = 1f;

        title.setPosition(offsetX, screenHeight - offsetY);
    }

    /**
     * Disposes of assets used by this component, including the player icon texture and the title label.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (playerIconTexture != null) {
            playerIconTexture.dispose();
        }
        if (minimapFrameTexture != null) {
            minimapFrameTexture.dispose();
        }
        title.remove();
    }
}
