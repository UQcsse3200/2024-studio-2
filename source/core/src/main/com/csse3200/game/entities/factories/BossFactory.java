package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.combat.move.*;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossFactory {

    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");
    
    
    private static final List<CombatMove> moveSet = new ArrayList<>(
            Arrays.asList(
                    new AttackMove("Enemy Attack", 10),
                    new GuardMove("Enemy Guard", 5),
                    new SleepMove("Enemy Sleep", 0),
                    new SpecialAirMove("Enemy Special", 25)
            )
    );

    /**
     * Creates a Kangaroo Boss entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createKangaBossEntity(Entity target) {
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        Entity kangarooBoss = createBossNPC(target, Entity.EnemyType.KANGAROO, config);

        kangarooBoss
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1))
                .addComponent(new BossAnimationController());

        kangarooBoss.getComponent(AnimationRenderComponent.class).scaleEntity();
        kangarooBoss.scaleHeight(4.0f);

        return kangarooBoss;
    }

    /**
     * Creates a Water Boss entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createWaterBossEntity(Entity target) {
        BaseEnemyEntityConfig config = configs.waterBoss;
        Entity waterBoss = createBossNPC(target, Entity.EnemyType.WATER_BOSS, config);
        

        waterBoss
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1))
                .addComponent(new BossAnimationController());

        waterBoss.getComponent(AnimationRenderComponent.class).scaleEntity();
        waterBoss.scaleHeight(5.0f);

        return waterBoss;
    }

    /**
     * Creates an Air Boss entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createAirBossEntity(Entity target) {
        BaseEnemyEntityConfig config = configs.airBoss;
        Entity airBoss = createBossNPC(target, Entity.EnemyType.AIR_BOSS, config);
        
        airBoss
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1))
                .addComponent(new BossAnimationController());

        airBoss.getComponent(AnimationRenderComponent.class).scaleEntity();
        airBoss.scaleHeight(6.0f);

        return airBoss;
    }

    /**
     * Creates a boss NPC to be used as a boss entity by more specific NPC creation methods.
     *
     * @param target the entity to chase
     * @param type the type of the boss
     * @return entity
     */
    public static Entity createBossNPC(Entity target, Entity.EnemyType type, BaseEnemyEntityConfig config) {
        AITaskComponent aiComponent = new AITaskComponent();

        aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f, true));

        // Add specific tasks for each boss type
        if (type == Entity.EnemyType.KANGAROO) {
            aiComponent.addTask(new ChaseTask(target, 10, 12f, 14f, new Vector2(configs.kangarooBoss.getSpeed(), configs.kangarooBoss.getSpeed()), true))
                    .addTask(new KangaJoeyTask(target, 6f, 2));
        } else if (type == Entity.EnemyType.WATER_BOSS) {
            aiComponent.addTask(new LeviathanTask(target, 10, 10f, 16f, 100f, 300));
        } else if (type == Entity.EnemyType.AIR_BOSS) {
            aiComponent.addTask(new GriffinTask(target, 10, 30f, 300, 100f));
        }
        
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
        animator.addAnimation("wander", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("chase", 0.1f, Animation.PlayMode.LOOP);

        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
                .addComponent(aiComponent)
                .addComponent(animator)
                .addComponent(new CombatMoveComponent(moveSet));
        
        npc.setEnemyType(type);
        
        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }

    /**
     * Creates a Kangaroo Boss entity for combat, without a chase task.
     *
     * @return entity
     */
    public static Entity createKangaBossCombatEntity() {
        Entity kangarooBoss = createCombatBossNPC();
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        kangarooBoss.setEnemyType(Entity.EnemyType.KANGAROO);

        kangarooBoss
                .addComponent(new TextureRenderComponent("images/final_boss_kangaroo_idle.png"))
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1));

        kangarooBoss.scaleHeight(120.0f);

        return kangarooBoss;
    }

    /**
     * Creates a Water Boss entity for combat, without a chase task.
     *
     * @return entity
     */
    public static Entity createWaterBossCombatEntity() {
        Entity waterBoss = createCombatBossNPC();
        BaseEnemyEntityConfig config = configs.waterBoss;
        waterBoss.setEnemyType(Entity.EnemyType.WATER_BOSS);
        waterBoss
                .addComponent(new TextureRenderComponent("images/water_boss_idle.png"))
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1));
        waterBoss.scaleHeight(120.0f);
        return waterBoss;
    }

    /**
     * Creates an Air Boss entity for combat, without a chase task.
     *
     * @return entity
     */
    public static Entity createAirBossCombatEntity() {
        Entity airBoss = createCombatBossNPC();
        BaseEnemyEntityConfig config = configs.airBoss;
        airBoss.setEnemyType(Entity.EnemyType.AIR_BOSS);
        airBoss
                .addComponent(new TextureRenderComponent("images/air_boss_idle.png"))
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1));
        airBoss.scaleHeight(120.0f);
        return airBoss;
    }

    /**
     * Creates a generic combat boss NPC without chase tasks.
     *
     * @return entity
     */
    public static Entity createCombatBossNPC() {
        Entity npc = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER));

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }
    
    //never call
    private BossFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
    
}
