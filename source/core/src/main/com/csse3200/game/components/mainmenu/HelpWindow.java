package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ui.CustomButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HelpWindow manages the display of the help screen with multiple slides
 * and handles navigation between the slides.
 */
public class HelpWindow {
    private static final Logger logger = LoggerFactory.getLogger(HelpWindow.class);
    private static final int NUM_SLIDES = 7;

    private final Skin skin;
    private final Stage stage;
    private final Drawable settingDrawable;
    private final float windowWidth;
    private final float windowHeight;
    private final Table helpWindowTable;

    private int currentSlide;
    private Table slideTable;
    private Table[] slideInstances;
    private Runnable onClose;  // For handling the close logic

    public HelpWindow(Skin skin, Stage stage, Drawable settingDrawable) {
        this.skin = skin;
        this.stage = stage;
        this.settingDrawable = settingDrawable;

        // Set the window size based on the screen size
        windowWidth = Math.min(1000f, Gdx.graphics.getWidth() - 100f);
        windowHeight = Math.min(600f, Gdx.graphics.getHeight() - 100f);
        currentSlide = 0;

        // Initialize the help window and slides
        helpWindowTable = createHelpWindow();
        initializeSlides();
    }

    /**
     * Initializes the help window layout, navigation buttons, and event listeners.
     */
    private Table createHelpWindow() {
        // Create a Window for the help screen
        Table helpWindow = new Table();
        helpWindow.setSize(windowWidth, windowHeight);
        helpWindow.setBackground(settingDrawable);

        // Title
        Label title = new Label("Help", skin, "title-white");

        // Create the slide table
        slideTable = new Table();
        slideTable.setFillParent(true);

        // Create navigation buttons
        float buttonWidth = windowWidth * 0.25f; // Make buttons take 25% of the window width
        float buttonHeight = 50f; // Consistent height for all buttons

        CustomButton previousButton = new CustomButton("Previous", skin);
        previousButton.setButtonSize(buttonWidth, buttonHeight); // Set size of previous button
        CustomButton nextButton = new CustomButton("Next", skin);
        nextButton.setButtonSize(buttonWidth, buttonHeight); // Set size of next button

        // Create the navigation table to hold buttons
        Table navigationTable = new Table();
        navigationTable.add(previousButton).size(buttonWidth, buttonHeight).padRight(20); // Add padding between buttons
        navigationTable.add(nextButton).size(buttonWidth, buttonHeight);

        // Create a close button
        Button closeButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/CloseButton.png"))));

        // Top Table with title and close button
        Table topTable = new Table();
        topTable.top().padTop(10);
        topTable.add(title).expandX().center().padTop(20);
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        // Add top table, slides, and navigation to the help window
        helpWindow.add(topTable).expandX().fillX();
        helpWindow.row();
        helpWindow.add(slideTable).expand().fill().row();
        helpWindow.add(navigationTable).bottom().expandX().fillX().pad(30).row();

        // Add listeners for navigation and close button
        addNavigationListeners(previousButton, nextButton);
        addCloseButtonListener(closeButton, helpWindow);

        return helpWindow;
    }

    /**
     * Initializes all the slides to be displayed in the help window.
     */
    private void initializeSlides() {
        slideInstances = new Table[NUM_SLIDES];
        slideInstances[0] = new Slides.MovementSlide(skin);
        slideInstances[1] = new Slides.CombatSlide(skin);
        slideInstances[2] = new Slides.StorylineSlide(skin);
        slideInstances[3] = new Slides.MinigamesSlide(skin);
        slideInstances[4] = new Slides.Minigames1Slide(skin);
        slideInstances[5] = new Slides.Minigames2Slide(skin);
        slideInstances[6] = new Slides.StatsSlide(skin);

        // Initially show only the first slide
        for (int i = 1; i < NUM_SLIDES; i++) {
            slideInstances[i].setVisible(false);
        }
    }

    /**
     * Adds navigation listeners to the previous and next buttons.
     */
    private void addNavigationListeners(CustomButton previousButton, CustomButton nextButton) {
        previousButton.addClickListener(() -> {
            if (currentSlide > 0) {
                slideInstances[currentSlide].setVisible(false);
                currentSlide--;
                updateSlide();
                logger.info("Slide changed to: " + (currentSlide + 1));
            }
        });

        nextButton.addClickListener(() -> {
            if (currentSlide < NUM_SLIDES - 1) {
                slideInstances[currentSlide].setVisible(false);
                currentSlide++;
                updateSlide();
                logger.info("Slide changed to: " + (currentSlide + 1));
            }
        });
    }

    /**
     * Adds a listener for the close button to close the help window.
     */
    private void addCloseButtonListener(Button closeButton, Table helpWindow) {
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                helpWindow.remove();
                if (onClose != null) {
                    onClose.run();
                }
                logger.info("Help window closed");
            }
        });
    }

    /**
     * Updates the currently visible slide.
     */
    private void updateSlide() {
        slideTable.clear(); // Clear existing slide
        slideTable.add(slideInstances[currentSlide]).expand().fill(); // Add the new slide
        slideInstances[currentSlide].setVisible(true);
    }

    /**
     * Displays the help window by adding it to the stage and sets keyboard focus.
     */
    public void show() {
        updateSlide(); // Set the first slide as visible
        helpWindowTable.setPosition(
                (stage.getWidth() - helpWindowTable.getWidth()) / 2,
                (stage.getHeight() - helpWindowTable.getHeight()) / 2
        );
        stage.setKeyboardFocus(helpWindowTable);

        // Add an InputListener to handle keyboard input (LEFT/RIGHT keys for slide navigation)
        helpWindowTable.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.LEFT:
                        if (currentSlide > 0) {
                            slideInstances[currentSlide].setVisible(false);
                            currentSlide--;
                            updateSlide();
                            logger.info("Slide changed to: {}", (currentSlide + 1) + " (via LEFT key)");
                        }
                        return true;
                    case Input.Keys.RIGHT:
                        if (currentSlide < NUM_SLIDES - 1) {
                            slideInstances[currentSlide].setVisible(false);
                            currentSlide++;
                            updateSlide();
                            logger.info("Slide changed to: {}", (currentSlide + 1) + " (via RIGHT key)");
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        stage.addActor(helpWindowTable);
    }

    /**
     * Sets the logic to be executed when the help window is closed.
     */
    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}
