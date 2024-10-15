package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A customizable button class that combines a button with a label and hover/click effects.
 */
public class CustomButton extends Stack{
    private static final Logger logger = LoggerFactory.getLogger(CustomButton.class);

    // Preloaded textures and sound used across all buttons
    private static final Texture NORMAL_BUTTON_TEXTURE = new Texture(Gdx.files.internal("images/ButtonsMain/BlankLarge.png"));
    private static final Texture DIALOGUE_BUTTON_TEXTURE = new Texture(Gdx.files.internal("images/ButtonsMain/BlueBlankLarge.png"));
    private static final Texture WIDE_BUTTON_TEXTURE = new Texture(Gdx.files.internal("images/ButtonsMain/WideButtonBrown.png"));
    private static final Texture SMALL_BUTTON_TEXTURE = new Texture(Gdx.files.internal("images/ButtonsMain/SmallBlankLarge.png"));
    private static final Texture SMALL_DIALOGUE_BUTTON_TEXTURE = new Texture(Gdx.files.internal("images/ButtonsMain/BlueBlankSmall.png"));
    private static final Sound BUTTON_CLICK_SOUND = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
    private static final Sound BUTTON_HOVER_SOUND = Gdx.audio.newSound(Gdx.files.internal("sounds/hoversound2.mp3"));


    private Button button;
    private final Label label;

    private final float hoverMoveBy = 5f;  // Pixels to move button and label on hover
    private final float hoverScaleFactor = 1.05f;  // Scaling factor for hover effect
    private final float hoverDuration = 0.1f;  // Duration for hover effect animation

    /**
     * Constructs a new CustomButton that automatically uses predefined textures and sounds.
     *
     * @param labelText The text to display on the button.
     * @param skin      The skin for styling the label.
     */
    public CustomButton(String labelText, Skin skin) {
        // Create the button using the default texture
        button = new Button(new TextureRegionDrawable(new TextureRegion(NORMAL_BUTTON_TEXTURE)));

        // Create the label
        label = new Label(labelText, skin, "button-red");
        label.setTouchable(Touchable.disabled);
        label.setAlignment(Align.center);

        // Stack button and label to ensure label is centered over the button
        this.add(button);
        this.add(label);

        // Set default button size based on the predefined texture
        this.setSize(NORMAL_BUTTON_TEXTURE.getWidth(), NORMAL_BUTTON_TEXTURE.getHeight());
        label.setSize(this.getWidth(), this.getHeight());

        // Add hover effect (movement, scaling) to both button and label
        addHoverEffect(button, label);
    }

    /**
     * Adds a click listener to the button that triggers a sound effect and a specified action.
     *
     * @param clickAction The action to perform when the button is clicked.
     */
    public void addClickListener(final Runnable clickAction) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (BUTTON_CLICK_SOUND != null) {
                    BUTTON_CLICK_SOUND.play();
                }
                if (clickAction != null) {
                    clickAction.run();
                }
                logger.info("Button clicked: {}", label.getText());
            }
        });
    }

    /**
     * Adds hover effects to both the button and the label, including movement and scaling on hover.
     *
     * @param button The button to add the effect to.
     * @param label  The label to add the effect to.
     */
    private void addHoverEffect(final Button button, final Label label) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Apply movement and scaling actions to both button and label on hover
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, hoverMoveBy, hoverDuration),
                        Actions.scaleTo(hoverScaleFactor, hoverScaleFactor, hoverDuration)
                ));
                label.addAction(Actions.parallel(
                        Actions.moveBy(0, hoverMoveBy, hoverDuration),
                        Actions.scaleTo(hoverScaleFactor, hoverScaleFactor, hoverDuration)
                ));
                // Play the hover sound when the button is hovered over
                if (BUTTON_HOVER_SOUND != null) {
                    BUTTON_HOVER_SOUND.play();
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Return to original position and scale for both button and label when hover ends
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -hoverMoveBy, hoverDuration),
                        Actions.scaleTo(1f, 1f, hoverDuration)
                ));
                label.addAction(Actions.parallel(
                        Actions.moveBy(0, -hoverMoveBy, hoverDuration),
                        Actions.scaleTo(1f, 1f, hoverDuration)
                ));
            }
        });
    }


    /**
     * Updates the button label text.
     *
     * @param newText The new text to display on the button.
     */
    public void setLabelText(String newText) {
        label.setText(newText);
    }

    /**
     * Updates the button size.
     *
     * @param width  The new width of the button.
     * @param height The new height of the button.
     */
    public void setButtonSize(float width, float height) {
        button.setSize(width, height);
        label.setSize(width, height);
        this.setSize(width, height);
    }

    /**
     * Update the button style
     * @param style The style of the button.
     * @param skin The skin for styling the label.
     */
    public void setButtonStyle(Style style, Skin skin) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        switch (style) {
            case NORMAL -> {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(NORMAL_BUTTON_TEXTURE));
                label.setStyle(skin.get("button-red", Label.LabelStyle.class));
            }
            case SMALL -> {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(SMALL_BUTTON_TEXTURE));
                label.setStyle(skin.get("button-red", Label.LabelStyle.class));
            }
            case DIALOGUE -> {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(DIALOGUE_BUTTON_TEXTURE));
                label.setStyle(skin.get("default-white", Label.LabelStyle.class));
            }
            case DIALOGUE_SMALL -> {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(SMALL_DIALOGUE_BUTTON_TEXTURE));
                label.setStyle(skin.get("default-white", Label.LabelStyle.class));
            }
            case BROWN_WIDE -> {
                buttonStyle.up = new TextureRegionDrawable(new TextureRegion(WIDE_BUTTON_TEXTURE));
                label.setStyle(skin.get("default-white", Label.LabelStyle.class));
            }
        }
        button.setStyle(buttonStyle);
    }

    /**
     * Resize the button according to the screen's dimensions and a scaling factor.
     * This allows dynamic resizing based on the screen size.
     *
     * @param screenWidth  The current screen width.
     * @param screenHeight The current screen height.
     * @param scaleFactor  The scale factor for resizing.
     */
    public void resize(float screenWidth, float screenHeight, float scaleFactor) {
        // Calculate new button size based on the scale factor and screen dimensions
        float newWidth = screenWidth * scaleFactor;
        float newHeight = screenHeight * scaleFactor;

        // Update button and label sizes accordingly
        setButtonSize(newWidth, newHeight);

        logger.info("Button resized to: {}x{}", newWidth, newHeight);
    }
    public Button getButton() {
        return button;
    }
    public Label getLabel() {
        return label;
    }

    /**
     * Disposes of any additional resources if needed (textures are static and should not be disposed here).
     */
    public void dispose() {
        logger.info("Disposing button resources");
        // No-op as static resources should not be disposed here
    }

    public enum Style {
        NORMAL, DIALOGUE, SMALL, DIALOGUE_SMALL, BROWN_WIDE
    }
}
