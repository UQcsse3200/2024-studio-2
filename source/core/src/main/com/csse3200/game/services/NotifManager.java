package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ui.UIComponent;

import java.util.Objects;

public class NotifManager extends UIComponent {

    private static Texture notifNormalBackground;
    private static Texture notifErrorBackground;
    private static Texture notifSuccessBackground;
    private static Label notifLabel;

    private static Table notifTable;
    private static float dropDuration = 1f;


    public static Table addNotificationTable() {
        notifTable = new Table();
        notifNormalBackground = new Texture("images/notification/BlueNotif.png");
        notifErrorBackground = new Texture("images/notification/RedNotif.png");
        notifSuccessBackground = new Texture("images/notification/GreenNotif.png");
        notifLabel = new Label("Contents", skin, "default-white");
        notifTable.clear();
        notifTable.setBackground(new TextureRegionDrawable(new TextureRegion(notifNormalBackground)));
        notifTable.setSize(600, 65);
        notifTable.setPosition(
                (Gdx.graphics.getWidth()- notifTable.getWidth())/2 ,
                Gdx.graphics.getHeight()/2 + 450);
        notifTable.setVisible(false);
        notifTable.add(notifLabel).top().right().padTop(-5);
        return notifTable;
    }
    public static void displayNotif(String contents, Boolean isSucceed) {
        if (isSucceed) {
            notifTable.setBackground(new TextureRegionDrawable(new TextureRegion(notifSuccessBackground)));
        } else {
            notifTable.setBackground(new TextureRegionDrawable(new TextureRegion(notifErrorBackground)));
        }
        notifTable.setVisible(true);
        notifLabel.setText(contents);
        notifTable.addAction(Actions.moveTo(
                (Gdx.graphics.getWidth()- notifTable.getWidth())/2,
                Gdx.graphics.getHeight() / 2 + 300,
                dropDuration
        ));
        // Schedule the notification to disappear after 2 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hideNotif();
            }
        }, dropDuration + 2);  // +2 seconds after the drop
    }

    //Overload method with one parameter
    public static void displayNotif(String contents) {
        notifTable.setBackground(new TextureRegionDrawable(new TextureRegion(notifNormalBackground)));
        notifTable.setVisible(true);
        notifLabel.setText(contents);
        notifTable.addAction(Actions.moveTo(
                (Gdx.graphics.getWidth()- notifTable.getWidth())/2,
                Gdx.graphics.getHeight() / 2 + 300,
                dropDuration
        ));
        // Schedule the notification to disappear after 2 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hideNotif();
            }
        }, dropDuration + 2);  // +2 seconds after the drop
    }

    public static void hideNotif() {
        notifTable.setVisible(false);
        notifTable.addAction(Actions.moveTo(
                (Gdx.graphics.getWidth()- notifTable.getWidth())/2,
                Gdx.graphics.getHeight() / 2 + 450,
                0
        ));
    }
    @Override
    public void update() {

    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
