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
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
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
    private Label gameTitle;

    private ArrayList<String> userNames;
    private ArrayList<String> highScores;

    private ArrayList<Label> userNameLabels;
    private ArrayList<Label> highScoreLabel;
    private Button closeButton;
    private CustomButton refreshButton;
    Texture backgroundTexture;
    Texture closeButtonTexture;
    PlayFab playFab;
    ArrayList<String> gameName;
    int currentIdx;

    private CustomButton snakeButton;
    private CustomButton birdButton;
    private CustomButton fishButton;
    private MainMenuDisplay mainMenuDisplay;
    /**
     * Constructor for LoginRegisterDisplay. Initializes PlayFab settings with the TitleId
     * and prepares the display for user interaction.
     */
    public MinigameLeaderboard(MainMenuDisplay mainMenuDisplay) {
        super();
        playFab = new PlayFab("DBB26");
        this.mainMenuDisplay = mainMenuDisplay;
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
        refreshLeaderboard("Current");
        return table;
    }

    /**
     * Loads the necessary textures for the UI components.
     */
    void loadTextures() {
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
        userNameLabels = new ArrayList<>();
        highScoreLabel = new ArrayList<>();
    }
    public void updateLeaderboard() {
        userNames = playFab.getUsernames();
        highScores = playFab.getHighscores();

        userNameLabels.clear();
        highScoreLabel.clear();


        for (int i = 0; i < userNames.size(); i++) {
            Label newPlayerName = new Label(userNames.get(i), skin, "default");
            userNameLabels.add(newPlayerName);
        }

        for (int i = 0; i < highScores.size(); i++) {
            Label newPlayerScore= new Label(highScores.get(i), skin, "default");
            highScoreLabel.add(newPlayerScore);
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
                mainMenuDisplay.setMenuTouchable();
            }
        });

        refreshButton = new CustomButton("Refresh", skin);
        refreshButton.addClickListener(() -> refreshLeaderboard("Current"));

        snakeButton = new CustomButton("Snake", skin);
        snakeButton.addClickListener(() -> refreshLeaderboard("Snake"));

        birdButton = new CustomButton("Bird", skin);
        birdButton.addClickListener(() -> refreshLeaderboard("Bird"));

        fishButton = new CustomButton("Fish", skin);
        fishButton.addClickListener(() -> refreshLeaderboard("Fish"));
    }

    /**
     *
     */
    public void refreshLeaderboard(String name) {
        if (!name.equals("Current")) {
            currentIdx = gameName.indexOf(name);
        }
        PlayFab.submitScore(gameName.get(currentIdx), 0);
        PlayFab.updateLeaderboard(gameName.get(currentIdx));
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
        topTable.add(title).expandX().center().padTop(5).top();
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        if (PlayFab.isLogin) {
            // Add table headers for username and score
            contentTable.add(new Label("Username", skin, "large-white")).padRight(30f).left().top();
            contentTable.add(new Label("Score", skin, "large-white")).padLeft(30f).right().top();

            contentTable.row();
            for (int i = 0; i < userNameLabels.size(); i++) {
                contentTable.add(userNameLabels.get(i)).padRight(30f).expandX().left();  // Username in left column
                contentTable.add(highScoreLabel.get(i)).padLeft(30f).expandX().right();  // Score in right column
                contentTable.row();
            }

            buttonRow.add(snakeButton).size(150, 50).padRight(10);  // Add snake button
            buttonRow.add(birdButton).size(150, 50).padRight(10);   // Add bird button
            buttonRow.add(fishButton).size(150, 50);                // Add fish button

            table.add(topTable).expandX().fillX().padTop(-10).top();
            table.row();
            table.add(contentTable).expandX().expandY().padLeft(30f).padRight(30f).top().padTop(20);
            table.row();
            table.add(buttonRow).expandX().bottom().padBottom(20); // Add buttons row
            table.row();
            table.add(refreshButton).size(200, 40).expandX().bottom().padBottom(20);
        } else {
            contentTable.add(warningLabel).expandX().width(450);

            table.add(topTable).expandX().fillX().padTop(-5).top();
            table.row();
            table.add(contentTable).expandX().expandY().padLeft(30f).padRight(30f).padTop(20);
            table.row();
            table.add(refreshButton).size(200, 40).expandX().bottom().padBottom(50);
        }
    }

    public ArrayList<String> getUsernames() {
        return userNames;
    }

    public ArrayList<String> getHighscores() {
        return highScores;
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
