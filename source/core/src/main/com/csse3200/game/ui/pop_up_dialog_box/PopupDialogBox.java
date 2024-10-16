package com.csse3200.game.ui.pop_up_dialog_box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.ui.CustomButton;

public class PopupDialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final Image animalImage;
    private Table statsTable;
    private Runnable callback;
    private final float dialogWidth;
    private final float dialogHeight;
    private final CustomButton enterKingdomButton;
    private final CustomButton backButton;
    private int animalIndex = 0;

    private final String[] kingdomButtonTexts = {
            "Enter Land Kingdom",    // Dog
            "Enter Water Kingdom",   // Croc
            "Enter Air Kingdom"      // Bird
    };

    public PopupDialogBox(String title, String content, String animalImagePath, Skin skin,
                          float dialogWidth, float dialogHeight,
                          int[] speedStats, int[] defenseStats, int[] strengthStats) {
        super("", skin);

        Texture bgTexture = new Texture(Gdx.files.internal("images/animal/lightblue.png"));
        Image backgroundImage = new Image(bgTexture);
        this.getContentTable().setBackground(backgroundImage.getDrawable());

        this.dialogWidth = dialogWidth + 200;
        this.dialogHeight = dialogHeight + 150;

        Texture animalTexture = new Texture(Gdx.files.internal(animalImagePath));
        animalImage = new Image(animalTexture);

        titleLabel = new Label(title, skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content, skin);
        contentLabel.setWrap(true);

        enterKingdomButton = new CustomButton(kingdomButtonTexts[0], skin);
        backButton = new CustomButton("Back", skin);

        addActionListeners();
        createDialogLayout(speedStats, defenseStats, strengthStats);
    }

    private void addActionListeners() {
        enterKingdomButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (callback != null) {
                    callback.run();
                }
                hide();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
    }

    private void createDialogLayout(int[] speedStats, int[] defenseStats, int[] strengthStats) {
        Table contentTable = new Table();
        contentTable.pad(20);

        Table rightTable = new Table();
        Table textTable = new Table();
        textTable.add(contentLabel).width(dialogWidth * 0.5f).padTop(30).top().expandY();
        rightTable.add(textTable).width(dialogWidth * 0.5f).expandX().row();

        Table statsContainer = new Table();
        statsTable = new Table();
        updateStatsTable(speedStats, defenseStats, strengthStats);
        statsContainer.add(statsTable).expand().top().row();
        rightTable.add(statsContainer).width(dialogWidth * 0.5f).expandX().fillY().top();

        Table innerTable = new Table();
        innerTable.add(animalImage).width(dialogWidth * 0.4f).height(dialogHeight * 0.8f).padRight(20);
        innerTable.add(rightTable).width(dialogWidth * 0.6f).expandY().top();

        contentTable.add(innerTable).expandX().center().row();

        Table buttonTable = new Table();
        buttonTable.add(backButton).width(200).height(50).padRight(10);
        buttonTable.add(enterKingdomButton).width(200).height(50);

        contentTable.add(buttonTable).padTop(20).colspan(2).center().expandX();

        getContentTable().add(contentTable).expand().center();

        setSize(dialogWidth, dialogHeight);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f,
                (Gdx.graphics.getHeight() - getHeight()) / 2f);
    }

    private void updateStatsTable(int[] speedStats, int[] defenseStats, int[] strengthStats) {
        statsTable.clear();
        statsTable.add(new Label("STATS", getSkin())).colspan(2).padBottom(10).row();
        statsTable.add(new Label("SPEED:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(speedStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("DEFENSE:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(defenseStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("STRENGTH:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(strengthStats[animalIndex]), getSkin())).right().row();
    }

    public void setAnimalIndex(int animalIndex) {
        this.animalIndex = animalIndex;
        enterKingdomButton.setLabelText(kingdomButtonTexts[animalIndex]);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void display(Stage stage) {
        stage.addActor(this);
    }
}