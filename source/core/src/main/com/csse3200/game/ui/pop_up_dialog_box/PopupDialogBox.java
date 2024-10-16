package com.csse3200.game.ui.pop_up_dialog_box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.ui.CustomButton;

public class PopupDialogBox extends Dialog {
    private final Label titleLabel;
    private final Label contentLabel;
    private final CustomButton confirmButton;
    private final CustomButton backButton;
    private final Image animalImage;
    private Table statsTable;
    private Runnable callback;

    private final float dialogWidth;
    private final float dialogHeight;

    private int animalIndex = 0;

    private int[] speedStats;
    private int[] defenseStats;
    private int[] strengthStats;

    public PopupDialogBox(String[] titles, String[] content, String animalImagePath, Skin skin, float dialogWidth, float dialogHeight,
                          int[] speedStats, int[] defenseStats, int[] strengthStats, String confirmButtonText) {
        super("", skin);
        Texture bgTexture = new Texture(Gdx.files.internal("images/animal/lightblue.png"));
        Image backgroundImage = new Image(bgTexture);
        this.getContentTable().setBackground(backgroundImage.getDrawable());

        this.dialogWidth = dialogWidth + 200;
        this.dialogHeight = dialogHeight + 150;

        Texture animalTexture = new Texture(Gdx.files.internal(animalImagePath));
        animalImage = new Image(animalTexture);

        this.speedStats = speedStats;
        this.defenseStats = defenseStats;
        this.strengthStats = strengthStats;

        titleLabel = new Label(titles[0], skin);
        titleLabel.setFontScale(0.4f);
        titleLabel.setColor(Color.CYAN);

        contentLabel = new Label(content[0], skin);
        contentLabel.setWrap(true);

        confirmButton = new CustomButton(confirmButtonText, skin);
        backButton = new CustomButton("Back", skin);

        confirmButton.setSize(200, 50);
        backButton.setSize(200, 50);

        addActionListeners();
        createDialogLayout();
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void addActionListeners() {
        confirmButton.addClickListener(() -> {
            hide();
            if (callback != null) {
                callback.run();
            }
        });

        backButton.addClickListener(() -> hide());
    }

    private void createDialogLayout() {
        Table contentTable = new Table();
        contentTable.pad(20);

        Table rightTable = new Table();
        Table textTable = new Table();
        textTable.add(contentLabel).width(dialogWidth * 0.5f).padTop(30).top().expandY();
        rightTable.add(textTable).width(dialogWidth * 0.5f).expandX().row();

        Table statsContainer = new Table();
        statsTable = new Table();
        statsTable.add(new Label("STATS", getSkin())).colspan(2).padBottom(10).row();
        statsContainer.add(statsTable).expand().top().row();
        rightTable.add(statsContainer).width(dialogWidth * 0.5f).expandX().fillY().top();

        Table innerTable = new Table();
        innerTable.add(animalImage).width(dialogWidth * 0.4f).height(dialogHeight * 0.8f).padRight(20);
        innerTable.add(rightTable).width(dialogWidth * 0.6f).expandY().top();

        contentTable.add(innerTable).expandX().center().row();

        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(200).height(50).padRight(10);
        buttonTable.add(backButton).width(200).height(50);

        contentTable.add(buttonTable).padTop(10).expandX().center();

        getContentTable().add(contentTable).expand().center();

        setSize(dialogWidth, dialogHeight);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2f, (Gdx.graphics.getHeight() - getHeight()) / 2f);

        updateStatsTable();
    }

    private void updateStatsTable() {
        statsTable.clear();
        statsTable.add(new Label("STATS", getSkin())).colspan(2).padBottom(10).row();
        statsTable.add(new Label("SPEED:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(speedStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("DEFENSE:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(defenseStats[animalIndex]), getSkin())).right().row();
        statsTable.add(new Label("STRENGTH:", getSkin())).left();
        statsTable.add(new Label(String.valueOf(strengthStats[animalIndex]), getSkin())).right().row();
    }

    public void display(Stage stage) {
        stage.addActor(this);
    }

    public void setAnimalIndex(int animalIndex) {
        this.animalIndex = animalIndex;
        updateStatsTable();
    }
}