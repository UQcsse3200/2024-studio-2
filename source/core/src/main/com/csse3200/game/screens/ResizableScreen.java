package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResizableScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ResizableScreen.class);
    protected final Renderer renderer;
    
    public ResizableScreen() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        
        renderer = RenderFactory.createRenderer();
    }
    
    
    /**
     * Render the Screen.
     */
    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }
    
    /**
     * Resize the screen to match window.
     * @param width The width of the new screen.
     * @param height The height of the new screen.
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }
    
    /**
     * Pause the current screen.
     */
    @Override
    public void pause() {
        logger.debug("This screen should not be paused");
    }
    
    /**
     * Resume the current screen.
     */
    @Override
    public void resume() {
        logger.debug("This screen should not be resumed");
    }
    
    /**
     * Dispose of current screen, only handles Render and Entity service.
     */
    @Override
    public void dispose() {
        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        
        ServiceLocator.clear();
    }
    
    
}
