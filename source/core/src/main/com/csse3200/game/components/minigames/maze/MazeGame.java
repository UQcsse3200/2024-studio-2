package com.csse3200.game.components.minigames.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.maze.mazegrid.MazeCell;
import com.csse3200.game.components.minigames.maze.mazegrid.MazeGrid;
import com.csse3200.game.components.minigames.maze.mazegrid.Wall;
import com.csse3200.game.components.minigames.maze.rendering.MazeGridRenderer;
import box2dLight.RayHandler;
import box2dLight.PointLight;

/**
 * CLass for the underwater maze game functionality
 */
public class MazeGame {

    private final MazeGrid grid;
    private final MinigameRenderer renderer;

    private final RayHandler rayHandler;

    private final Box2DDebugRenderer b2dr;
    private final World world;
    private Body player;
    private final PointLight pl;
    private final PointLight pl2;

    public MazeGame() {
        this.grid = new MazeGrid(12, 6);
        this.renderer = new MinigameRenderer();
        initRenderers();
        world = new World(new Vector2(0,0),false);
        rayHandler = new RayHandler(world);
        Color lightColor = new Color(0.55f, 0.45f, 0.75f, 1);
        pl = new PointLight(rayHandler,180, lightColor,350,1920/2,1200/2);
        pl2 = new PointLight(rayHandler,180, lightColor,180,1920/2,1200/2);
        RayHandler.useDiffuseLight(true);
        pl.setPosition(800, 500);
        pl2.setPosition(800, 500);
        pl2.setXray(true);
        //rayHandler.setAmbientLight(lightColor);
        b2dr = new Box2DDebugRenderer();
        //player = createPlayer();
        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize(); col++) {
                MazeCell cell = grid.getCell(row, col);
                if (cell instanceof Wall) {
                    createPlayer(cell.getPosition().x, cell.getPosition().y, cell.getSize(), cell.getSize());
                }
            }
        }
    }

    /**
     * Renders the maze
     */
    private void initRenderers() {
        renderer.addRenderable(new MazeGridRenderer(grid, renderer));
    }

    /**
     * Updates the maze rendering
     */
    public void render() {
        renderer.render();
        float x = pl.getPosition().x, y = pl.getPosition().y;

        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += 4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= 4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= 4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += 4;
        }

        pl.setPosition(x, y);
        pl2.setPosition(x, y);

        //update(Gdx.graphics.getDeltaTime());

        //b2dr.render(world, renderer.getCam().combined);
        rayHandler.setCombinedMatrix(renderer.getCam().combined);
        rayHandler.updateAndRender();
    }

    /**
     * Disposes
     */
    public void dispose() {
        renderer.dispose();
        rayHandler.dispose();
        b2dr.dispose();
        world.dispose();
    }

    /**
     * Uses the mini-game renderer
     * @return the mini-game renderer
     */
    public MinigameRenderer getRenderer() {
        return renderer;
    }

    /**
     * Creates a player in the maze
     * @param x start x coordinate
     * @param y start y coordinate
     * @param width player width
     * @param height player height
     * @return the players body
     */
    private Body createPlayer(float x, float y, float width, float height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x,y);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();



        return pBody;
    }
}

