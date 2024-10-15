package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
// import com.csse3200.game.overlays.Overlay; // Commented out for now

public class SnakePopup {

    private final Stage stage;
    private final Skin skin;
    private final Window window;
    private final Texture texture;
    private final GdxGame game;
    private final String gameType;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    private boolean isVisible = false;
    // private final Entity ui; // Commented out for now

    public SnakePopup(GdxGame game, String texturePath, String gameType) {
        this.game = game;
        this.gameType = gameType;

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        texture = new Texture(Gdx.files.internal(texturePath));

        window = new Window("Help", skin);
        window.setSize(windowWidth, windowHeight);
        window.setModal(true);
        window.setPosition((Gdx.graphics.getWidth() - windowWidth) / 2, (Gdx.graphics.getHeight() - windowHeight) / 2);

        Image image = new Image(texture);
        window.add(image).fill();

        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                switch (gameType) {
                    case "Snake":
                        game.enterSnakeScreen();
                        break;
                    case "Maze":
                        game.enterMazeGameScreen();
                        break;
                    case "Birdie":
                        game.enterBirdieDashScreen();
                        break;
                }
            }
        });

        window.row();
        window.add(continueButton).padTop(10);

        stage.addActor(window);

        // Initialize the UI Entity for overlay management
        // ui = new Entity();
        // ui.getEvents().addListener("addOverlay", this::show);
        // ui.getEvents().addListener("removeOverlay", this::hide);
        // ServiceLocator.getEntityService().register(ui);
    }

    public void show() {
        isVisible = true;
        window.setVisible(true);
        Gdx.input.setInputProcessor(stage);
        // ui.getEvents().trigger("addOverlay", Overlay.OverlayType.PAUSE_OVERLAY);
    }

    public void hide() {
        isVisible = false;
        window.setVisible(false);
        Gdx.input.setInputProcessor(null);
        // ui.getEvents().trigger("removeOverlay", Overlay.OverlayType.PAUSE_OVERLAY);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        window.setPosition((width - windowWidth) / 2, (height - windowHeight) / 2); // Adjust window position accordingly
    }

    public void render() {
        if (isVisible) { // Only render if visible
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }
}
