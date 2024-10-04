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

        animator.addAnimation("combat_idle", 1.0f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.1f, Animation.PlayMode.LOOP);

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

        TextureAtlas monkeyAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(monkeyAtlas);

        animator.addAnimation("combat_idle", 0.3f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        monkeyEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

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

        TextureAtlas frogAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(frogAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        frogEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        frogEnemy.setScale(120.0f, 90.0f);

        return frogEnemy;
    }

    /**
     * Creates bear enemy as NPC entity for static combat
     * */
    public static Entity createBearCombatEnemy() {
        Entity bearEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.bear;
        bearEnemy.setEnemyType(Entity.EnemyType.BEAR);

        TextureAtlas bearAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(bearAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        bearEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        bearEnemy.setScale(150f,103.5f);
        //bearEnemy.scaleHeight(150.0f);

        return bearEnemy;
    }
    /**
     * Creates bee enemy as NPC entity for static combat
     * */
    public static Entity createBeeCombatEnemy() {
        Entity beeEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.bee;
        beeEnemy.setEnemyType(Entity.EnemyType.BEE);

        TextureAtlas beeAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(beeAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        beeEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        beeEnemy.setScale(90f, 103.5f);

        return beeEnemy;
    }

    /**
     * Creates big saw fish enemy as NPC entity for static combat
     * */
    public static Entity createBigsawfishCombatEnemy() {
        Entity bigsawfishEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.bigsawfish;
        bigsawfishEnemy.setEnemyType(Entity.EnemyType.BIGSAWFISH);

        TextureAtlas bigsawfishAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(bigsawfishAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        bigsawfishEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        bigsawfishEnemy.scaleHeight(90.0f);

        return bigsawfishEnemy;
    }
    
    /**
     * Creates big saw fish enemy as NPC entity for static combat
     * */
    public static Entity createOctopusCombatEnemy() {
        Entity octopus = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.octopus;
        octopus.setEnemyType(Entity.EnemyType.OCTOPUS);
        
        TextureAtlas octopusAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(octopusAtlas);
        
        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);
        
        octopus
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        octopus.scaleHeight(90.0f);
        
        return octopus;
    }

    /**
     * Creates macaw enemy as NPC entity for static combat
     * */
    public static Entity createMacawCombatEnemy() {
        Entity macawEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.macaw;
        macawEnemy.setEnemyType(Entity.EnemyType.MACAW);

        TextureAtlas macawAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(macawAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        macawEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        macawEnemy.scaleHeight(90.0f);

        return macawEnemy;
    }

    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createPigeonCombatEnemy() {
        Entity pigeonEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.pigeon;
        pigeonEnemy.setEnemyType(Entity.EnemyType.PIGEON);

        TextureAtlas pigeonAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(pigeonAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        pigeonEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        pigeonEnemy.setScale(100f,70f);

        return pigeonEnemy;
    }

    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createEelCombatEnemy() {
        Entity eelEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.eel;
        eelEnemy.setEnemyType(Entity.EnemyType.EEL);

        TextureAtlas eelAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(eelAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        eelEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        eelEnemy.setScale(100f,70f);

        return eelEnemy;
    }

    /**
     * Creates joey enemy as NPC entity for static combat
     * */
    public static Entity createJoeyCombatEnemy() {
        Entity joeyEnemy = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.joey;
        joeyEnemy.setEnemyType(Entity.EnemyType.JOEY);

        TextureAtlas joeyAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(joeyAtlas);

        animator.addAnimation("combat_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.5f, Animation.PlayMode.LOOP);

        joeyEnemy
                .addComponent(animator)
                .addComponent(new CombatAnimationController());
        joeyEnemy.scaleHeight(90.0f);

        return joeyEnemy;
    }

    /**
     * Creates kangaroo boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createKangaBossCombatEntity() {
        Entity kangarooBoss = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        kangarooBoss.setEnemyType(Entity.EnemyType.KANGAROO);

        TextureAtlas kangarooAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(kangarooAtlas);

        animator.addAnimation("combat_idle", 1.0f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.1f, Animation.PlayMode.LOOP);

        kangarooBoss
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        kangarooBoss.scaleHeight(120.0f);

        return kangarooBoss;
    }

    /**
     * Creates water boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createWaterBossCombatEntity() {
        Entity waterBoss = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.waterBoss;
        waterBoss.setEnemyType(Entity.EnemyType.WATER_BOSS);

        TextureAtlas waterBossAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(waterBossAtlas);

        animator.addAnimation("combat_idle", 1.0f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.1f, Animation.PlayMode.LOOP);

        waterBoss
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        waterBoss.scaleHeight(120.0f);

        return waterBoss;
    }

    /**
     * Creates air boss enemy as entity for combat display
     *
     * @return entity
     * */
    public static Entity createAirBossCombatEntity() {
        Entity airBoss = createCombatBaseEnemy();
        BaseEnemyEntityConfig config = configs.airBoss;
        airBoss.setEnemyType(Entity.EnemyType.AIR_BOSS);

        TextureAtlas airBossAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);
        AnimationRenderComponent animator = new AnimationRenderComponent(airBossAtlas);

        animator.addAnimation("combat_idle", 1.0f, Animation.PlayMode.LOOP);
        animator.addAnimation("combat_move", 0.1f, Animation.PlayMode.LOOP);

        airBoss
                .addComponent(animator)
                .addComponent(new CombatAnimationController());

        airBoss.scaleHeight(120.0f);

        return airBoss;
    }

    private CombatAnimalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
