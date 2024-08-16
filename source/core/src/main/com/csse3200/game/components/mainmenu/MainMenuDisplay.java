package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        Image title = new Image(ServiceLocator.getResourceService().getAsset("images/box_boy_title.png", Texture.class));

        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton fullscreenToggleBtn = new TextButton("Minimize", skin);
        Label versionLabel = new Label("Version 1.0", skin);

        addButtonElevationEffect(startBtn);
        addButtonElevationEffect(loadBtn);
        addButtonElevationEffect(settingsBtn);
        addButtonElevationEffect(exitBtn);
        addButtonElevationEffect(fullscreenToggleBtn);

        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Start button clicked");
                entity.getEvents().trigger("start");
            }
        });

        loadBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Load button clicked");
                entity.getEvents().trigger("load");
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Settings button clicked");
                entity.getEvents().trigger("settings");
            }
        });

        fullscreenToggleBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isFullscreen = Gdx.graphics.isFullscreen();
                if (isFullscreen) {
                    Gdx.graphics.setWindowedMode(800, 600);
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                logger.debug("Fullscreen toggled: " + !isFullscreen);
            }
        });

        addExitConfirmation(exitBtn);

        table.add(title).padTop(50f).padBottom(50f);
        table.row();
        table.add(startBtn).padTop(30f).width(200f).height(60f);
        table.row();
        table.add(loadBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(settingsBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(fullscreenToggleBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(exitBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(versionLabel).padTop(20f);

        stage.addActor(table);
    }

    /**
     * Adds an elevation effect to buttons when hovered over.
     */
    private void addButtonElevationEffect(TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }

    /**
     * Adds an exit confirmation dialog when the exit button is clicked.
     */
    private void addExitConfirmation(TextButton exitBtn) {
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog dialog = new Dialog("Exit", skin) {
                    public void result(Object obj) {
                        if ((Boolean) obj) {
                            Gdx.app.exit(); // Exit the game
                        }
                    }
                };
                dialog.text("Exit the game?");
                dialog.button("Yes", true);
                dialog.button("No", false);
                dialog.show(stage);
            }
        });
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
//
//
//package com.csse3200.game.components.mainmenu;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.csse3200.game.services.ServiceLocator;
//import com.csse3200.game.ui.UIComponent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * A ui component for displaying the Main menu.
// */
//public class MainMenuDisplay extends UIComponent {
//    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
//    private static final float Z_INDEX = 2f;
//    private Table table;
//
//    @Override
//    public void create() {
//        super.create();
//        addActors();
//    }
//
//    private void addActors() {
//        table = new Table();
//        table.setFillParent(true);
//        Image title =
//                new Image(
//                        ServiceLocator.getResourceService()
//                                .getAsset("images/box_boy_title.png", Texture.class));
//
//        TextButton startBtn = new TextButton("Start", skin);
//        TextButton loadBtn = new TextButton("Load", skin);
//        TextButton settingsBtn = new TextButton("Settings", skin);
//        TextButton exitBtn = new TextButton("Exit", skin);
//
//        // Add elevation effect to buttons
//        addButtonElevationEffect(startBtn);
//        addButtonElevationEffect(loadBtn);
//        addButtonElevationEffect(settingsBtn);
//        addButtonElevationEffect(exitBtn);
//
//        // Triggers an event when the button is pressed
//        startBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Start button clicked");
//                        entity.getEvents().trigger("start");
//                    }
//                });
//
//        loadBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Load button clicked");
//                        entity.getEvents().trigger("load");
//                    }
//                });
//
//        settingsBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Settings button clicked");
//                        entity.getEvents().trigger("settings");
//                    }
//                });
//
//        exitBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Exit button clicked");
//                        entity.getEvents().trigger("exit");
//                    }
//                });
//
//        table.add(title);
//        table.row();
//        table.add(startBtn).padTop(30f);
//        table.row();
//        table.add(loadBtn).padTop(15f);
//        table.row();
//        table.add(settingsBtn).padTop(15f);
//        table.row();
//        table.add(exitBtn).padTop(15f);
//
//        stage.addActor(table);
//    }
//
//    /**
//     * An elevation effect on hovering over the button.
//     */
//    private void addButtonElevationEffect(TextButton button) {
//        button.addListener(new ClickListener() {
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                button.addAction(Actions.parallel(
//                        Actions.moveBy(0, 5, 0.1f),
//                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
//                ));
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                button.addAction(Actions.parallel(
//                        Actions.moveBy(0, -5, 0.1f),
//                        Actions.scaleTo(1f, 1f, 0.1f)
//                ));
//            }
//        });
//    }
//
//    @Override
//    public void draw(SpriteBatch batch) {
//        // draw is handled by the stage
//    }
//
//    @Override
//    public float getZIndex() {
//        return Z_INDEX;
//    }
//
//    @Override
//    public void dispose() {
//        table.clear();
//        super.dispose();
//    }
//}
