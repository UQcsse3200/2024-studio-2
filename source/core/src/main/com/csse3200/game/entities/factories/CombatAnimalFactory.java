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
    public static Entity createCombatBaseEnemy(BaseEnemyEntityConfig config, Entity.EnemyType type) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());

        PhysicsUtils.setScaledCollider(entity, 0.9f, 0.4f);
        
        entity.setEnemyType(type);
        
        TextureAtlas textureAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(textureAtlas);
        
        animator.addAnimation("combat_idle", 0.8f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.2f, Animation.PlayMode.LOOP);
        
        entity
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        
        return entity;
    }

    /**
     * Creates chicken enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createChickenCombatEnemy() {
        BaseEnemyEntityConfig config = configs.chicken;
        Entity chickenEnemy = createCombatBaseEnemy(config, Entity.EnemyType.CHICKEN);
        chickenEnemy.scaleHeight(110.0f);
        return chickenEnemy;
    }

    /**
     * Creates monkey enemy as NPC entity for static combat
     * */
    public static Entity createMonkeyCombatEnemy() {
        BaseEnemyEntityConfig config = configs.monkey;
        Entity monkeyEnemy = createCombatBaseEnemy(config, Entity.EnemyType.MONKEY);
        monkeyEnemy.scaleHeight(110.0f); // 90.0
        return monkeyEnemy;
    }

    /**
     * Creates frog enemy as NPC entity for static combat
     * */
    public static Entity createFrogCombatEnemy() {
        BaseEnemyEntityConfig config = configs.frog;
        Entity frogEnemy = createCombatBaseEnemy(config, Entity.EnemyType.FROG);
        frogEnemy.setScale(120.0f, 90.0f);
        // frogEnemy.scaleHeight(80f);
        return frogEnemy;
    }

    /**
     * Creates bear enemy as NPC entity for static combat
     * */
    public static Entity createBearCombatEnemy() {
        BaseEnemyEntityConfig config = configs.bear;
        Entity bearEnemy = createCombatBaseEnemy(config, Entity.EnemyType.BEAR);
        bearEnemy.setScale(150f,110.5f);
        return bearEnemy;
    }
    /**
     * Creates bee enemy as NPC entity for static combat
     * */
    public static Entity createBeeCombatEnemy() {
        BaseEnemyEntityConfig config = configs.bee;
        Entity beeEnemy = createCombatBaseEnemy(config, Entity.EnemyType.BEE);
        beeEnemy.setScale(90f, 103.5f);
        return beeEnemy;
    }

    /**
     * Creates big saw fish enemy as NPC entity for static combat
     * */
    public static Entity createBigsawfishCombatEnemy() {
        BaseEnemyEntityConfig config = configs.bigsawfish;
        Entity bigsawfishEnemy = createCombatBaseEnemy(config, Entity.EnemyType.BIGSAWFISH);
        bigsawfishEnemy.scaleHeight(90.0f);
        return bigsawfishEnemy;
    }
    
    /**
     * Creates big saw fish enemy as NPC entity for static combat
     * */
    public static Entity createOctopusCombatEnemy() {
        BaseEnemyEntityConfig config = configs.octopus;
        Entity octopus = createCombatBaseEnemy(config, Entity.EnemyType.OCTOPUS);
        octopus.scaleHeight(90.0f);
        return octopus;
    }

    /**
     * Creates macaw enemy as NPC entity for static combat
     * */
    public static Entity createMacawCombatEnemy() {
        BaseEnemyEntityConfig config = configs.macaw;
        Entity macawEnemy = createCombatBaseEnemy(config, Entity.EnemyType.MACAW);
        macawEnemy.scaleHeight(90.0f);
        return macawEnemy;
    }

    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createPigeonCombatEnemy() {
        BaseEnemyEntityConfig config = configs.pigeon;
        Entity pigeonEnemy = createCombatBaseEnemy(config, Entity.EnemyType.PIGEON);
        pigeonEnemy.setScale(100f,70f);
        return pigeonEnemy;
    }

    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createEelCombatEnemy() {
        BaseEnemyEntityConfig config = configs.eel;
        Entity eelEnemy = createCombatBaseEnemy(config, Entity.EnemyType.EEL);
        eelEnemy.setScale(100f,70f);
        eelEnemy.scaleHeight(80f);
        return eelEnemy;
    }

    /**
     * Creates joey enemy as NPC entity for static combat
     * */
    public static Entity createJoeyCombatEnemy() {
        BaseEnemyEntityConfig config = configs.joey;
        Entity joeyEnemy = createCombatBaseEnemy(config, Entity.EnemyType.JOEY);
        joeyEnemy.scaleHeight(90.0f);
        return joeyEnemy;
    }

    /**
     * Creates kangaroo boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createKangaBossCombatEntity() {
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        Entity kangarooBoss = createCombatBaseEnemy(config, Entity.EnemyType.KANGAROO);
        kangarooBoss.scaleHeight(120.0f);
        return kangarooBoss;
    }

    /**
     * Creates water boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createWaterBossCombatEntity() {
        BaseEnemyEntityConfig config = configs.waterBoss;
        Entity waterBoss = createCombatBaseEnemy(config, Entity.EnemyType.WATER_BOSS);
        waterBoss.scaleHeight(120.0f);
        return waterBoss;
    }

    /**
     * Creates air boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createAirBossCombatEntity() {
        BaseEnemyEntityConfig config = configs.airBoss;
        Entity airBoss = createCombatBaseEnemy(config, Entity.EnemyType.AIR_BOSS);
        airBoss.scaleHeight(120.0f);
        return airBoss;
    }

    private CombatAnimalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
