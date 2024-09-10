package com.csse3200.game.services;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.entities.DialogueBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A container class that stores all of the services for a screen. This can be used to switch between screens without
 * loosing any of the services that are loaded.
 */
public class ServiceContainer {
    private static final Logger logger = LoggerFactory.getLogger(ServiceContainer.class);
    private final DialogueBoxService dialogueBoxService;
    private EntityService entityService;
    private RenderService renderService;
    private PhysicsService physicsService;
    private GameTime timeSource;
    private InputService inputService;
    private ResourceService resourceService;

    public ServiceContainer() {
        this.entityService = ServiceLocator.getEntityService();
        this.renderService = ServiceLocator.getRenderService();
        this.physicsService = ServiceLocator.getPhysicsService();
        this.timeSource = ServiceLocator.getTimeSource();
        this.inputService = ServiceLocator.getInputService();
        this.resourceService = ServiceLocator.getResourceService();
        this.dialogueBoxService = ServiceLocator.getDialogueBoxService();
        logger.debug("Services stored");
    }

    /**
     * Gets the EntityService stored in this container
     * @return the EntityService stored in this container
     */
    public EntityService getEntityService() {return entityService;}
    /**
     * Gets the RenderService stored in this container
     * @return the RenderService stored in this container
     */
    public RenderService getRenderService() {return renderService;}
    /**
     * Gets the PhysicsService stored in this container
     * @return the PhysicsService stored in this container
     */
    public PhysicsService getPhysicsService() {return physicsService;}
    /**
     * Gets the TimeService stored in this container
     * @return the TimeService stored in this container
     */
    public GameTime getTimeSource() {return timeSource;}
    /**
     * Gets the InputService stored in this container
     * @return the InputService stored in this container
     */
    public InputService getInputService() {return inputService;}
    /**
     * Gets the ResourceService stored in this container
     * @return the ResourceService stored in this container
     */
    public ResourceService getResourceService() {return resourceService;}
    /**
     * Gets the EntityService stored in this container
     * @return the EntityService stored in this container
     */
    public DialogueBoxService getDialogueBoxService() {
        return dialogueBoxService;
    }
}
