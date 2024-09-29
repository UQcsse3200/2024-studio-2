package com.csse3200.game.components.login;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;

import java.awt.*;

/**
 * A UI component for displaying the Main menu.
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
    private TextField confirmPasswordField;
    private TextButton submitButton;
    private TextButton switchButton;
    private Button closeButton;
    private boolean isLoginMode = true;
    private Texture backgroundTexture;
    private Texture closeButtonTexture;

    public LoginRegisterDisplay() {
        super();
    }

    private void loadTextures() {
        backgroundTexture = new Texture("images/SettingBackground.png");
        closeButtonTexture = new Texture("images/CloseButton.png");
    }

    public void initializeTable() {
        table = new Table();
        topTable = new Table();
        contentTable = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        table.setSize(663, 405);
        title = new Label("Login", skin, "title-white");
    }

    public Table makeLoginRegisterTable() {// Create table for layout
        loadTextures();
        initializeTable();
        addInputField();
        addButtons();
        updateUI();

        return table;
    }

    private void addInputField() {
        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
    }

    private void addButtons() {
        closeButton = new Button(new TextureRegionDrawable(new TextureRegion(closeButtonTexture)));
        submitButton = new TextButton("Submit", skin);
        switchButton = new TextButton("Switch to Register", skin);
        switchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Switch button clicked");
                isLoginMode = !isLoginMode;
                topTable.clear();
                contentTable.clear();
                usernameField.clear();
                passwordField.clear();
                updateUI();
            }
        });

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Close button clicked");
                table.setVisible(false);
            }
        });
    }

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
        contentTable.add(new Label("Password:", skin)).padRight(10);
        contentTable.add(passwordField).width(200).padBottom(10);
        contentTable.row();
        // If it's the register screen, add the confirm password field
        if (!isLoginMode) {
            contentTable.add(new Label("Confirm Password:", skin)).padRight(10);
            contentTable.add(confirmPasswordField).width(200).padBottom(10);
            contentTable.row();
        }


        // Add submit and switch buttons
        contentTable.add(submitButton).colspan(2).padBottom(10);
        contentTable.row();
        contentTable.add(switchButton).colspan(2);

        // Update switch button text
        switchButton.setText(isLoginMode ? "Switch to Register" : "Switch to Login");


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
