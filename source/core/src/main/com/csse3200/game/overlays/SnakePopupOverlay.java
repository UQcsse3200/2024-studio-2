package com.csse3200.game.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakePopupOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(SnakePopupOverlay.class);
    private final Stage stage;
    private final Skin skin;
    private final Window window;
    private boolean isVisible = false;
    private final float WIDTH = 800;
    private final float HEIGHT = 600;

    public SnakePopupOverlay(GdxGame game) {
        super(OverlayType.SNAKE_POPUP_OVERLAY);

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));


        window = new Window("Help", skin);
        window.setSize(800, 600);
        window.setModal(true);
        window.setPosition((Gdx.graphics.getWidth() -WIDTH) / 2,
                (Gdx.graphics.getHeight() - HEIGHT) / 2);

        stage.addActor(window);
        Entity ui = new Entity();
        super.add(ui);
        ServiceLocator.getEntityService().register(ui);
    }

    public void show() {
        isVisible = true;
        window.setVisible(true);
        Gdx.input.setInputProcessor(stage);
        logger.info("Showing SnakePopupOverlay");
    }

    public void hide() {
        isVisible = false;
        window.setVisible(false);
        Gdx.input.setInputProcessor(null);
        logger.info("Hiding SnakePopupOverlay");
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        window.setPosition((width - window.getWidth()) / 2, (height - window.getHeight()) / 2);
    }


    public void render(float delta) {
        if (isVisible) {
            stage.act(delta);
            stage.draw();
        }
    }


    @Override
    public void remove() {
        hide();
        stage.dispose();
        skin.dispose();
    }
}
