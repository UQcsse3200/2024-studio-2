package com.csse3200.game.minigames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.login.PlayFab;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.ui.CustomButton;
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
    private Table buttonRow;
    private Label title;
    private Label warningLabel;

    private ArrayList<String> playerNames;
    private ArrayList<String> playerScores;

    private ArrayList<Label> usernames;
    private ArrayList<Label> highscores;
    private Button closeButton;
    private CustomButton refreshButton;
    private Texture backgroundTexture;
    private Texture closeButtonTexture;
    private PlayFab playFab;
    private ArrayList<String> gameName;

    private CustomButton snakeButton;
    private CustomButton birdButton;
    private CustomButton fishButton;
    private int currentIdx;
    /**
     * Constructor for LoginRegisterDisplay. Initializes PlayFab settings with the TitleId
     * and prepares the display for user interaction.
     */
    public MinigameLeaderboard() {
        super();
        playFab = new PlayFab("DBB26");
        gameName = new ArrayList<>();
        gameName.add("Snake");
        gameName.add("Bird");
        gameName.add("Fish");
        currentIdx = 0;
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
        addButtons();
        refreshLeaderboard();

        return table;
    }


    /**
     * Loads the necessary textures for the UI components.
     */
    private void loadTextures() {
        backgroundTexture = new Texture("images/backgrounds/LeaderboardBackground.png");
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
        buttonRow = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        table.setSize(300, 450);
        title = new Label("Leaderboard", skin, "title-white");
        warningLabel = new Label("You need to login to see the leaderboard", skin, "large-white");
        warningLabel.setWrap(true);
        warningLabel.setAlignment(Align.center);
        usernames = new ArrayList<>();
        highscores = new ArrayList<>();
    }

    public void refreshLeaderboard() {
        PlayFab.submitScore(gameName.get(currentIdx), 0);
        PlayFab.getLeaderboard(gameName.get(currentIdx));
        updateLeaderboard();
        updateUI();
    }
    private void updateLeaderboard() {
        playerNames = playFab.getUsernames();
        playerScores = playFab.getHighscores();
        usernames.clear();
        highscores.clear();


        for (int i = 0; i < playerNames.size(); i++) {
            Label newPlayerName = new Label(playerNames.get(i), skin, "default");
            usernames.add(newPlayerName);
        }

        for (int i = 0; i < playerScores.size(); i++) {
            Label newPlayerScore= new Label(playerScores.get(i), skin, "default");
            highscores.add(newPlayerScore);
        }
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

        refreshButton = new CustomButton("Refresh", skin);
        refreshButton.addClickListener(this::refreshLeaderboard);

        snakeButton = new CustomButton("Snake", skin);
        snakeButton.addClickListener(() -> moveToLeaderboard("Snake"));

        birdButton = new CustomButton("Bird", skin);
        birdButton.addClickListener(() -> moveToLeaderboard("Bird"));

        fishButton = new CustomButton("Fish", skin);
        fishButton.addClickListener(() -> moveToLeaderboard("Fish"));
    }

    private void moveToLeaderboard(String name) {
        PlayFab.getLeaderboard(name);
        PlayFab.submitScore(gameName.get(currentIdx), 0);
        currentIdx = gameName.indexOf(name);
        updateLeaderboard();
        updateUI();
    }

    /**
     * Updates the UI elements to reflect the current mode (login or register).
     */
    private void updateUI() {
        topTable.clear();
        contentTable.clear();
        table.clear();  // Clear the table to re-add elements

        topTable.top().padTop(10);
        topTable.add(title).expandX().center().padTop(5);
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        if (PlayFab.isLogin) {
            // Add table headers for username and score
            contentTable.add(new Label("Username", skin, "large-white")).padRight(30f).left();
            contentTable.add(new Label("Score", skin, "large-white")).padLeft(30f).right();

            contentTable.row();
            for (int i = 0; i < usernames.size(); i++) {
                contentTable.add(usernames.get(i)).padRight(30f).expandX().left();  // Username in left column
                contentTable.add(highscores.get(i)).padLeft(30f).expandX().right();  // Score in right column
                contentTable.row();
            }

            buttonRow.add(snakeButton).size(150, 50).padRight(10);  // Add snake button
            buttonRow.add(birdButton).size(150, 50).padRight(10);   // Add bird button
            buttonRow.add(fishButton).size(150, 50);                // Add fish button

            table.add(topTable).expandX().fillX().padTop(-10);
            table.row();
            table.add(contentTable).expandX().expandY().padLeft(30f).padRight(30f).padTop(-100);
            table.row();
            table.add(buttonRow).expandX().bottom().padBottom(20); // Add buttons row
            table.row();
            table.add(refreshButton).size(200, 40).expandX().bottom().padBottom(20);
        } else {
            contentTable.add(warningLabel).expandX().width(450);

            table.add(topTable).expandX().fillX().padTop(-5);
            table.row();
            table.add(contentTable).expandX().expandY().padLeft(30f).padRight(30f).padTop(20);
            table.row();
            table.add(refreshButton).size(200, 40).expandX().bottom().padBottom(50);
        }
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
