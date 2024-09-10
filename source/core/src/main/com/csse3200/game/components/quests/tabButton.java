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

public class tabButton extends ImageButton {
    private final Label label;
    private final Image backgroundImage;

    public tabButton(String text, Skin skin, Drawable backgroundDrawable) {
        this(text, skin, backgroundDrawable, backgroundDrawable);
    }

    public tabButton(String text, Skin skin, Drawable backgroundUpDrawable, Drawable backgroundDownDrawable) {
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

    public tabButton(String text, Skin skin, Texture backgroundTexture) {
        this(text, skin, new TextureRegionDrawable(backgroundTexture));
    }

    public Label getLabel() {
        return label;
    }

    public void setText(String text) {
        label.setText(text);
    }
}
