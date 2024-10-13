package com.csse3200.game.components.login;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents the login and registration display for the game.
 * It allows users to either login or register by using the PlayFab service.
 */
public class LoginRegisterDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoginRegisterDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table topTable;
    private Table contentTable;
    public ProgressBar progressBar;
    private Label title;
    private TextField usernameField;
    private TextField passwordField;
    private TextField emailField;
    private CustomButton submitButton;
    private CustomButton switchButton;
    private Button closeButton;
    private boolean isLoginMode = true;
    private Texture backgroundTexture;
    private Texture closeButtonTexture;
    private PlayFab playFab;
    private MainMenuDisplay mainMenuDisplay;
    /**
     * Constructor for LoginRegisterDisplay. Initializes PlayFab settings with the TitleId
     * and prepares the display for user interaction.
     */
    public LoginRegisterDisplay(MainMenuDisplay mainMenuDisplay) {
        super();
        playFab = new PlayFab("DBB26");
        this.mainMenuDisplay = mainMenuDisplay;
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
        title = new Label("Login", skin, "title-white");
    }
    /**
     * Constructs and returns the layout table containing all UI components, including input fields,
     * buttons, and dynamic mode switching for login and registration.
     *
     * @return Table containing the login or registration form.
     */
    public Table makeLoginRegisterTable() {// Create table for layout
        loadTextures();
        initializeTable();
        addInputField();
        addButtons();
        updateUI();

        return table;
    }

    /**
     * Adds the input fields for username, password, and email to the UI.
     */
    private void addInputField() {
        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        emailField = new TextField("", skin);
    }

    /**
     * Adds buttons for form submission, switching between login/register modes, and closing the display.
     */
    private void addButtons() {
        closeButton = new Button(new TextureRegionDrawable(new TextureRegion(closeButtonTexture)));

        submitButton = new CustomButton("Submit", skin);
        switchButton = new CustomButton("Switch to Register", skin);

        switchButton.addClickListener(() -> {
                logger.info("Switch button clicked");
                isLoginMode = !isLoginMode;
                topTable.clear();
                contentTable.clear();
                updateUI();
        });

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Close button clicked");
                table.setVisible(false);
                mainMenuDisplay.setMenuTouchable();
            }
        });
    }

    /**
     * Updates the UI elements to reflect the current mode (login or register).
     */
    private void updateUI() {
        table.clear();  // Clear the table to re-add elements

        // Update title
        if (isLoginMode) {
            title.setText("Login");
        } else {
            title.setText("Register");
        }

        topTable.top().padTop(10);
        topTable.add(title).expandX().center().padTop(5);
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        // Add title, username, and password fields
        contentTable.add(new Label("Username:", skin)).padRight(10);
        contentTable.add(usernameField).width(200).padBottom(10);
        contentTable.row();
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                PlayFab.Response response = playFab.loginUser(usernameField.getText(), passwordField.getText());
                NotifManager.displayNotif(response.getResult(), response.getIsSucceed());
                if (response.getIsSucceed()) {
                    table.setVisible(false);
                }

            }
        });
        // If it's the register screen, add the confirm password field
        if (!isLoginMode) {
            contentTable.add(new Label("Email:", skin)).padRight(10);
            contentTable.add(emailField).width(200).padBottom(10);
            contentTable.row();
            submitButton.addClickListener(() -> {
                PlayFab.Response response = playFab.registerUser(usernameField.getText(), emailField.getText(), passwordField.getText());
                NotifManager.displayNotif(response.getResult(), response.getIsSucceed());
            });
        }


        contentTable.add(new Label("Password:", skin)).padRight(10);
        contentTable.add(passwordField).width(200).padBottom(10);
        contentTable.row();

        // Add submit and switch buttons
        contentTable.add(submitButton).size(150, 50).colspan(2).padTop(10);
        contentTable.row();
        contentTable.add(switchButton).size(300, 50).colspan(2).padTop(10);

        // Update switch button text
        switchButton.setLabelText(isLoginMode ? "Switch to Register" : "Switch to Login");


        table.add(topTable).expandX().fillX(); // Top-right table
        table.row().padTop(30f);
        table.add(contentTable).expandX().expandY().padLeft(20);
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
