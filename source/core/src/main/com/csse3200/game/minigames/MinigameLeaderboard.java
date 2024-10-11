package com.csse3200.game.minigames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.login.PlayFab;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.ui.UIComponent;
import org.lwjgl.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * This class represents the login and registration display for the game.
 * It allows users to either login or register by using the PlayFab service.
 */
public class MinigameLeaderboard extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MinigameLeaderboard.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table topTable;
    private Table contentTable;
    private Label title;

    String[] playerNames;
    String[] playerScores;

    private ArrayList<Label> usersname;
    private ArrayList<Label> highscores;
    private Button closeButton;
    private Texture backgroundTexture;
    private Texture closeButtonTexture;
    private PlayFab playFab;

    /**
     * Constructor for LoginRegisterDisplay. Initializes PlayFab settings with the TitleId
     * and prepares the display for user interaction.
     */
    public MinigameLeaderboard() {
        super();
        playFab = new PlayFab("DBB26");
    }



    /**
     * Loads the necessary textures for the UI components.
     */
    private void loadTextures() {
        backgroundTexture = new Texture("images/SettingBackground.png");
        closeButtonTexture = new Texture("images/CloseButton.png");
    }

    /**
     * Initializes the layout of the table.
     * This includes setting up the background, size, and title label.
     */
    public void initializeTable() {
        table = new Table();
        topTable = new Table();
        contentTable = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        table.setSize(663, 405);
        title = new Label("Leaderboard", skin, "title-white");
        usersname = new ArrayList<>();
        highscores = new ArrayList<>();
    }
    /**
     * Constructs and returns the layout table containing all UI components, including input fields,
     * buttons, and dynamic mode switching for login and registration.
     *
     * @return Table containing the login or registration form.
     */
    public Table makeLeaderboardTable() {// Create table for layout
        loadTextures();
        initializeTable();

        playerNames = playFab.getUsernames();
        playerScores = playFab.getHighscores();


        for (int i = 0; i < playerNames.length; i++) {
            Label newPlayerName = new Label(playerNames[i], skin, "default");
            usersname.add(newPlayerName);
        }

        for (int i = 0; i < playerScores.length; i++) {
            Label newPlayerScore= new Label(playerScores[i], skin, "default");
            highscores.add(newPlayerScore);
        }
        System.out.println(playerNames[0]);
        addButtons();
        updateUI();


        return table;
    }

    /**
     * Adds buttons for form submission, switching between login/register modes, and closing the display.
     */
    private void addButtons() {
        closeButton = new Button(new TextureRegionDrawable(new TextureRegion(closeButtonTexture)));

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Close button clicked");
                table.setVisible(false);
            }
        });
    }

    /**
     * Updates the UI elements to reflect the current mode (login or register).
     */
    private void updateUI() {
        table.clear();  // Clear the table to re-add elements

        topTable.top().padTop(10);
        topTable.add(title).expandX().center().padTop(5);
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        for (int i = 0; i < usersname.size(); i++) {
            contentTable.row();
            contentTable.add(usersname.get(i));
        }

        table.add(topTable).expandX().fillX(); // Top-right table
        table.row().padTop(30f);
        table.add(contentTable).expandX().expandY().padLeft(50);
        table.row().padTop(30f);
    }
    @Override
    public void update() {
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
