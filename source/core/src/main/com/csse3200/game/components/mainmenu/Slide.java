package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.files.FileLoader;
import java.util.ArrayList;
import java.util.List;

public class Slide extends Table {
    private final String name;

    private Slide(String name, Skin skin, String title, String contents) {
        this.name = name;
        Label titleLabel = new Label(title, skin, "title");
        Label contentLabel = new Label(contents, skin);
        add(titleLabel).padTop(20f).expandX().center().row();
        add(contentLabel).padTop(20f).expandX().center().row();
    }

    public String getName() {
        return this.name;
    }

    public static List<Slide> getAllSlides(Skin skin) {
        return loadSlidesFromJson(skin);
    }

    private static List<Slide> loadSlidesFromJson(Skin skin) {
        List<Slide> slides = new ArrayList<>();

        // Read array of SlideData from JSON
        SlideData[] slidesData = FileLoader.readClass(SlideData[].class, "configs/HelpSlides.json");

        if (slidesData != null) {
            for (SlideData data : slidesData) {
                Slide slide = new Slide(data.name, skin, data.title, data.content);
                slides.add(slide);
            }
        } else {
            throw new RuntimeException("Error: Slide data could not be loaded from slides.json.");
        }

        return slides;
    }

    // Helper class to represent slide data loaded from JSON
    private static class SlideData {
        public String name;
        public String title;
        public String content;
    }

}
