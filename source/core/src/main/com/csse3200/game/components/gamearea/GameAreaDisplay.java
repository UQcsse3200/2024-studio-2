package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.factories.PlayerFactory;

/**
 * Displays the player icon based on the player's image corresponding to its kingdom.
 */
public class GameAreaDisplay extends UIComponent {
    private final String gameAreaName;
    private Label title;
    private Texture playerIconTexture;

    /**
     * Constructs a GameAreaDisplay component.
     *
     * @param gameAreaName the name of the current game area to be displayed
     */
    public GameAreaDisplay(String gameAreaName) {
        this.gameAreaName = gameAreaName;
    }


    /**
     * Initializes the GameAreaDisplay component and calls addactors() to add actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds the title label and player icon to the UI using a table layout.
     * The player icon is determined based on the selected player's image
     * path with help of switch casewhich corresponds to its kingdom
     */
    private void addActors() {
        title = new Label(this.gameAreaName, skin, "large");

        // Get the player image path from PlayerFactory
        String playerImagePath = PlayerFactory.getSelectedAnimalImagePath();

        // Determine the player icon texture based on the player image path
        switch (playerImagePath) {
            case "images/dog.png" ->
                    playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png"));
            case "images/croc.png" -> playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sea.png"));
            case "images/bird.png" -> playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sky.png"));
            default ->
                    playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png")); // Default icon
        }

        Image playerIcon = new Image(playerIconTexture);
        // Set the size of the icon to match the label's height
        float titleHeight = title.getPrefHeight();
        float scaleFactor = 5f;
        playerIcon.setSize(titleHeight * scaleFactor, titleHeight * scaleFactor);

        // Create a table for the top UI
        Table topTable = new Table();
        topTable.setFillParent(true);

        topTable.top().left();

        // Add the player icon to the right side of the table
        topTable.add(playerIcon).size(titleHeight * scaleFactor, titleHeight * scaleFactor).align(Align.left | Align.top).pad(10); // Padding from the edges

        // Add space to push the icon to the right
        topTable.add().expandX();
        // This expands the space between the title and the icon

        topTable.add(title).align(Align.center | Align.top).pad(10); // Padding from the edges

        // Add the table to the stage
        stage.addActor(topTable);
    }

    /**
     * Draws the UI component on the screen.
     *
     * @param batch the SpriteBatch used to draw the component
     */
    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        float offsetX = 10f;
        float offsetY = 30f;

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
        title.remove();
    }
}
