package com.csse3200.game.components.combatButtons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CombatButtonDisplay {
    private final Stage stage;
    private final Skin skin;
    private final TextButton[] CombatButtons;
    private final TextButton AttackButton;
    private final TextButton BoostButton;

    public CombatButtonDisplay(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        // Initialize arrays to hold images and buttons for the animals
        this.CombatButtons = new TextButton[2];

        // Initialize the select and back buttons
        this.AttackButton= new TextButton("Attack", skin);
        this.BoostButton = new TextButton("Boost", skin);

        // Set up the display of the selection screen
        initializeDisplay();
    }


    /**
     * Initializes the display by setting up the layout of images and buttons on the stage.
     */
    private void initializeDisplay() {
        // Create the main table layout for positioning combat buttons
        Table CombatButtonTable = new Table();
        CombatButtonTable.setFillParent(false); // Make the table not fill the entire stage very mindful
        CombatButtonTable.top().padTop(80); // Align the table to the top with some padding
        stage.addActor(CombatButtonTable); // Add the table to the stage

        //add animation or customize buttons here

        // Add space between the animal selection and buttons
        CombatButtonTable.row();
        CombatButtonTable.add().expandY();

        // Create a table for the select and back buttons
        Table buttonTable = new Table();
        buttonTable.add(AttackButton).padBottom(10).width(300).height(60).padRight(250); // Position the select button
        buttonTable.add(BoostButton).padBottom(10).width(300).height(60).padRight(380); // Position the back button

        // Add the button table to the main table
        CombatButtonTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();

    }
    // Getters for accessing the UI elements

    public TextButton[] getCombatButtons() {
        return CombatButtons;
    }

    public TextButton getAttackButton() {
        return AttackButton;
    }

    public TextButton getBoostButton() {
        return BoostButton;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }

}
