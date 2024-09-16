package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.TimedUseItemTask;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerItemInUseDisplay extends UIComponent {
    private final Texture indicatebox = new Texture("Inventory/item-indication.png");
    private final Table box = new Table();
    private boolean speedExpired;
    private boolean attackExpired;
    private boolean defenseExpired;
    private final int ITEM_BOX_WIDTH = 72;
    private final int ITEM_BOX_HEIGHT = 91;

    public PlayerItemInUseDisplay(boolean speedExpired, boolean attackExpired, boolean defenseExpired) {
        this.speedExpired = speedExpired;
        this.attackExpired = attackExpired;
        this.defenseExpired = defenseExpired;
        create();
        createIndicationBox();
    }

    /**
     * Set the defense expired bool
     * @param defenseExpired boolean indicator if the defence is expired or not
     */
    public void setDefenseExpired(boolean defenseExpired) {
        this.defenseExpired = defenseExpired;
    }

    /**
     * To initialise the stage
     */
    public void create() {super.create();}

    /**
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    @Override
    public void dispose() {
        box.clear();
        box.remove();
        super.dispose();
    }

    /**
     * create item usage indication box to show which timed item is currently in use
     */
    public void createIndicationBox() {
        box.setBackground(new TextureRegionDrawable(indicatebox));
        box.setSize(ITEM_BOX_WIDTH * 3, ITEM_BOX_HEIGHT);

        if (!speedExpired) {
            box.add(createColoredBox(Color.GRAY)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        } else {
            box.add(createColoredBox(Color.PURPLE)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        }

        if (!attackExpired) {
            box.add(createColoredBox(Color.GRAY)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        } else {
            box.add(createColoredBox(Color.YELLOW)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        }

        if (!defenseExpired) {
            box.add(createColoredBox(Color.GRAY)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        } else {
            box.add(createColoredBox(Color.BLUE)).size(ITEM_BOX_WIDTH, ITEM_BOX_HEIGHT);
        }


        box.row();

        float gap = 10;
        float tableX = 20;
        float tableY = 50;
        box.setPosition(tableX, tableY);
        stage.addActor(box);
    }

    /**
     * Creates a colored box for the indication
     */
    private Actor createColoredBox(Color color) {
        Image image = new Image(new Texture("Inventory/white_pixel.png"));
        image.setColor(color);
        return image;
    }

    void disposeBox() {
        box.clear();
        box.removeActor(box);
    }

}
