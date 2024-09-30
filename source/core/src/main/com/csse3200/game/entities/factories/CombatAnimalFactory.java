package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.combat.CombatAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create display entities in the Combat Screen with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 */
public class CombatAnimalFactory {
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");

    /**
     * Creates a base Combat NPC to be used by more specific NPC creation methods.
     *
     * @return entity
     */
    public static Entity createCombatBaseEnemy() {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());

        PhysicsUtils.setScaledCollider(entity, 0.9f, 0.4f);
        return entity;
    }

    /**
     * Creates chicken enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createChickenCombatEnemy() {
        Entity chickenEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.chicken;
        chickenEnemy.setEnemyType(Entity.EnemyType.CHICKEN);

        TextureAtlas chickenAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(chickenAtlas);

        animator.addAnimation("walk", 0.25f, Animation.PlayMode.LOOP);
        animator.addAnimation("spawn", 1.0f, Animation.PlayMode.NORMAL);

        chickenEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        chickenEnemy.scaleHeight(90.0f);

        return chickenEnemy;
    }

    /**
     * Creates monkey enemy as NPC entity for static combat
     * */
    public static Entity createMonkeyCombatEnemy() {
        Entity monkeyEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.monkey;
        monkeyEnemy.setEnemyType(Entity.EnemyType.MONKEY);

        monkeyEnemy
                .addComponent(new TextureRenderComponent("images/monkey_idle.png"));

        monkeyEnemy.scaleHeight(90.0f);

        return monkeyEnemy;
    }

    /**
     * Creates frog enemy as NPC entity for static combat
     * */
    public static Entity createFrogCombatEnemy() {
        Entity frogEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.frog;
        frogEnemy.setEnemyType(Entity.EnemyType.FROG);

        frogEnemy
                .addComponent(new TextureRenderComponent("images/frog_idle.png"));

        frogEnemy.scaleHeight(150.0f);

        return frogEnemy;
    }

    /**
     * Creates frog enemy as NPC entity for static combat
     * */
    public static Entity createBearCombatEnemy() {
        Entity bearEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.bear;
        bearEnemy.setEnemyType(Entity.EnemyType.BEAR);

        bearEnemy
                .addComponent(new TextureRenderComponent("images/bear_idle.png"));

        bearEnemy.setScale(150f,103.5f);
        //bearEnemy.scaleHeight(150.0f);

        return bearEnemy;
    }

    /**
     * Creates a Kangaroo Boss entity for combat. This functions the same as createKangaBossEntity() however
     * there is no chase task included. This is where abilities components will be added.
     * loaded.
     *
     * @return entity
     */
    public static Entity createKangaBossCombatEntity() {
        Entity kangarooBoss = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        kangarooBoss.setEnemyType(Entity.EnemyType.KANGAROO);

        kangarooBoss
                .addComponent(new TextureRenderComponent("images/final_boss_kangaroo_idle.png"));

        kangarooBoss.scaleHeight(120.0f);

        return kangarooBoss;
    }

    private CombatAnimalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
