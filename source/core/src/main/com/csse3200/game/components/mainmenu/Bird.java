package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bird extends Actor {
    private static final int MOVEMENT = 100;
    private Vector3 position;
    private Rectangle bounds;
    private BirdAnimation birdAnimation;
    private Texture texture;
    public Bird(int x, int y){
        position = new Vector3(x, y, 0);
        texture = new Texture("BirdMain.png");
        birdAnimation = new BirdAnimation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
    }

    public void update(float dt){
        birdAnimation.update(dt);
        position.add(MOVEMENT, 0, 0);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        texture.dispose();
    }
}
