package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.files.FileLoader;
import java.util.ArrayList;
import java.util.List;

public class Slide extends Table {
    private final String name;

    public Slide(String name, Skin skin, String title, String contents) {
        this.name = name;
        Label titleLabel = new Label(title, skin, "title");
        Label contentLabel = new Label(contents, skin);
        add(titleLabel).padTop(20f).expandX().center().row();
        add(contentLabel).padTop(20f).expandX().center().row();
    }

    public String getName() {
        return this.name;
    }
}
