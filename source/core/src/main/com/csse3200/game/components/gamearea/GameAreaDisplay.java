package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;
/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
    private String gameAreaName = "";
    private Label title;
    private Texture playerIconTexture;
    private Image playerIcon;
    private static Integer iconScale = 3;

    public GameAreaDisplay(String gameAreaName) {
        this.gameAreaName = gameAreaName;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        title = new Label(this.gameAreaName, skin, "large");
        if (gameAreaName.equals("Sky Kingdom")) {
            playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sky.png"));
        } else if (gameAreaName.equals("Sea Kingdom")) {
            playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sea.png"));
        } else {
            playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png"));
        }
        playerIcon = new Image(playerIconTexture);
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

    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        float offsetX = 10f;
        float offsetY = 30f;

        title.setPosition(offsetX, screenHeight - offsetY);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (playerIconTexture != null)
        {
            playerIconTexture.dispose();
        }
        title.remove();
    }
}
