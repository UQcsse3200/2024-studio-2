package com.csse3200.game.components.quests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/**
 * A custom tab button class that extends {@link ImageButton} and provides additional
 * functionality to display a label with a customizable background. This class can be used
 * in a tab-like UI component where each tab has a text label and a background image.
 */
public class TabButton extends ImageButton {
    private final Label label;
    private final Image backgroundImage;

    /**
     * Constructs a new tabButton with the specified text, skin, and background image.
     * The same drawable is used for both the button's up and down states.
     * @param text              The text to display on the button's label.
     * @param skin              The {@link Skin} used for the button's label.
     * @param backgroundDrawable The {@link Drawable} used as the background image for the button.
     */
    public TabButton(String text, Skin skin, Drawable backgroundDrawable) {
        this(text, skin, backgroundDrawable, backgroundDrawable);
    }

    /**
     * Constructs a new tabButton with the specified text, skin, and separate background images
     * for the up and down states.
     * @param text                  The text to display on the button's label.
     * @param skin                  The {@link Skin} used for the button's label.
     * @param backgroundUpDrawable  The {@link Drawable} used as the background image when the button is not pressed.
     * @param backgroundDownDrawable The {@link Drawable} used as the background image when the button is pressed.
     *                               If null, the up drawable is used for the down state as well.
     */
    public TabButton(String text, Skin skin, Drawable backgroundUpDrawable, Drawable backgroundDownDrawable) {
        super(new ImageButtonStyle());
        ImageButtonStyle style = getStyle();

        style.up = backgroundUpDrawable;
        style.down = backgroundDownDrawable != null ? backgroundDownDrawable : backgroundUpDrawable; // Button's pressed state

        Stack stack = new Stack();

        backgroundImage = new Image(backgroundUpDrawable);
        backgroundImage.setFillParent(true);
        stack.add(backgroundImage);

        // Set the label
        label = new Label(text, skin);
        label.setAlignment(Align.center);
        stack.add(label);

        add(stack).expand().fill();
    }

    /**
     * Constructs a new tabButton with the specified text, skin, and background texture.
     * The same texture is used for both the button's up and down states.
     * @param text             The text to display on the button's label.
     * @param skin             The {@link Skin} used for the button's label.
     * @param backgroundTexture The {@link Texture} used as the background image for the button.
     */
    public TabButton(String text, Skin skin, Texture backgroundTexture) {
        this(text, skin, new TextureRegionDrawable(backgroundTexture));
    }

    /**
     * Gets the label associated with this tabButton.
     * @return The {@link Label} displayed on the button.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Sets the text of the label displayed on this tabButton.
     * @param text The new text to display on the button's label.
     */
    public void setText(String text) {
        label.setText(text);
    }
}
