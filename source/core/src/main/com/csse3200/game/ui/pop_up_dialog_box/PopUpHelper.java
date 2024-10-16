package com.csse3200.game.ui.pop_up_dialog_box;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PopUpHelper {
    private final Skin skin;
    private final Stage stage;

    private int[] speedStats = {100, 40, 80}; // Speed stats for bird, croc, dog
    private int[] defenseStats = {80, 30, 60}; // Defense stats for bird, croc, dog
    private int[] strengthStats = {40, 80, 60}; // Strength stats for bird, croc, dog

    public PopUpHelper(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
    }

    public void displayDialog(String title, String content, String animalImagePath, float width, float height, int animalIndex, String confirmButtonText, Runnable callback) {
        PopupDialogBox dialogBox = new PopupDialogBox(
                new String[]{title},
                new String[]{content},
                animalImagePath,
                skin,
                width,
                height,
                speedStats,
                defenseStats,
                strengthStats,
                confirmButtonText
        );
        dialogBox.setAnimalIndex(animalIndex);
        dialogBox.setCallback(callback);
        dialogBox.display(stage);
    }
}