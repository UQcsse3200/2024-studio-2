package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;g
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
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
    private SettingsMenuDisplay settingsMenuDisplay;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        Table settingMenu = new Table();
        Image title = new Image(ServiceLocator.getResourceService().getAsset("images/box_boy_title.png", Texture.class));

        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton minigamesBtn = new TextButton("Minigames", skin); // New Minigames button
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton helpBtn = new TextButton("Help", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        Label versionLabel = new Label("Version 1.0", skin);

        addButtonElevationEffect(startBtn);
        addButtonElevationEffect(loadBtn);
        addButtonElevationEffect(minigamesBtn); // Apply the elevation effect to Minigames button
        addButtonElevationEffect(settingsBtn);
        addButtonElevationEffect(helpBtn);
        addButtonElevationEffect(exitBtn);

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

        minigamesBtn.addListener(new ChangeListener() { // Listener for Minigames button
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Minigames button clicked");
                entity.getEvents().trigger("minigames"); // Trigger minigames event
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Settings button clicked");
                settingMenu.setVisible(!settingMenu.isVisible());
                table.setTouchable(Touchable.disabled);
            }
        });

        helpBtn.addListener(new ChangeListener() {
            @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Help button clicked");
                entity.getEvents().trigger("help");
                showHelpDialog();
            }
        });

        addExitConfirmation(exitBtn);

        table.add(title).padTop(50f).padBottom(50f);
        table.row();
        table.add(startBtn).padTop(30f).width(200f).height(60f);
        table.row();
        table.add(loadBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(minigamesBtn).padTop(15f).width(200f).height(60f); // Add the Minigames button to the layout
        table.row();
        table.add(settingsBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(helpBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(exitBtn).padTop(15f).width(200f).height(60f);
        table.row();
        table.add(versionLabel).padTop(20f);

        stage.addActor(table);

        // Add the minimize button to the top-right corner
        addMinimizeButton();

        makeSettingMenu(settingMenu);
        }
        //method for help window
        private void showHelpDialog() {
            final int NUM_SLIDES = 6;
            final float DIALOG_WIDTH = 1100f;
            final float DIALOG_HEIGHT = 700f;

            // Create the dialog with a larger size
            final Dialog helpDialog = new Dialog("Help", skin) {
                @Override
                public void result(Object obj) {
                    // Optional: handle result if needed
                }
            };

            // Set the dialog size
            helpDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

            // Create a table to hold all slides
            final Table slideTable = new Table();
            slideTable.setFillParent(true);

            Table[] slides = new Table[NUM_SLIDES];
            for (int i = 0; i < NUM_SLIDES; i++) {
                slides[i] = new Table();
                Label titleLabel = new Label("Title " + (i + 1), skin, "title");
                Label contentLabel = new Label("Content for slide " + (i + 1), skin);

                slides[i].add(titleLabel).padTop(20f).expandX().center().row();
                slides[i].add(contentLabel).padTop(20f).expandX().center().row();

                // Add an exit button on the last slide
                if (i == NUM_SLIDES - 1) {
                    TextButton exitButton = new TextButton("Exit to Main Menu", skin);
                    exitButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            helpDialog.hide(); // Hide the dialog
                            entity.getEvents().trigger("showMainMenu"); // Trigger the event to show the main menu
                        }
                    });

                    slides[i].add(exitButton).padTop(30f).expandX().center();
                }
            }

            // Add the first slide to the slideTable
            slideTable.add(slides[0]).expand().fill();
            helpDialog.add(slideTable).expand().fill();

            // Create a table for navigation buttons
            Table navigationTable = new Table();
            TextButton previousButton = new TextButton("Previous", skin);
            TextButton nextButton = new TextButton("Next", skin);

            navigationTable.add(previousButton).padRight(10);
            navigationTable.add(nextButton);

            // Add the navigation buttons to the dialog's content
            helpDialog.add(navigationTable).padTop(10).expandX().bottom().right();

            final int[] currentSlide = {0}; // Use an array to modify value in lambda expressions

            previousButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (currentSlide[0] > 0) {
                        slides[currentSlide[0]].setVisible(false);
                        currentSlide[0]--;
                        slides[currentSlide[0]].setVisible(true);
                    }
                }
            });

            nextButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (currentSlide[0] < NUM_SLIDES - 1) {
                        slides[currentSlide[0]].setVisible(false);
                        currentSlide[0]++;
                        slides[currentSlide[0]].setVisible(true);
                    }
                }
            });

            // Initially hide all slides except the first
            for (int i = 1; i < NUM_SLIDES; i++) {
                slides[i].setVisible(false);
            }

            // Center the dialog on the stage
            helpDialog.setPosition(
                    (stage.getWidth() - helpDialog.getWidth()) / 2,
                    (stage.getHeight() - helpDialog.getHeight()) / 2
            );

            // Show the dialog
            helpDialog.show(stage);

            // Force layout update
            helpDialog.layout();
        }






    private void addMinimizeButton() {
        TextButton minimizeBtn = new TextButton("-", skin);
        minimizeBtn.addListener(new ChangeListener() {
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

        Table topRightTable = new Table();
        topRightTable.top().right();
        topRightTable.setFillParent(true);
        topRightTable.add(minimizeBtn).size(40, 40).padTop(10).padRight(10);

        stage.addActor(topRightTable);
    }

    private void makeSettingMenu(Table settingMenu) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); // Set color to white
        pixmap.fill();

        // Create a Drawable from the Pixmap
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        // Dispose of the Pixmap after creating the texture
        pixmap.dispose();

        int screenWidth = (int) stage.getWidth();
        int screenHeight = (int) stage.getHeight();

        settingMenu.setSize(550, 350);
        settingMenu.setBackground(backgroundDrawable);
        settingMenu.setVisible(false);

        Table topTable = new Table();
        topTable.top().padTop(10);

        Label title = new Label("Settings", skin, "title");

        topTable.add(title).expandX().center();
        topTable.row();

        TextButton closeButton = new TextButton("X", skin);
        topTable.add(closeButton).size(40, 40).right().padRight(10).padTop(-40);

        settingsMenuDisplay = new SettingsMenuDisplay();
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create a table for the "Apply" button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom(); // Align contents to bottom-right

        TextButton applyButton = new TextButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(10f).padRight(10f);

        settingMenu.add(topTable).expandX().fillX(); // Top-right table
        settingMenu.row().padTop(30f);
        settingMenu.add(contentTable).expandX().expandY().padLeft(50);
        settingMenu.row().padTop(30f);
        settingMenu.add(bottomRightTable).expandX().right().padLeft(100); // Bottom-right table

        // Center the menu on the screen
        settingMenu.setPosition(
                (screenWidth - settingMenu.getWidth()) / 2,
                (screenHeight - settingMenu.getHeight()) / 2
        );

        stage.addActor(settingMenu);

        closeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        // Toggle the visibility of the small menu
                        settingMenu.setVisible(!settingMenu.isVisible());
                        table.setTouchable(Touchable.enabled);
                    }
                });

        // Add event listener for the "Apply" button
        applyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Apply button clicked");
                        settingsMenuDisplay.applyChanges(); // Apply the settings when clicked
                        settingMenu.setVisible(false); // Optionally hide the settings menu
                        table.setTouchable(Touchable.enabled);
                    }
                });
    }

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
