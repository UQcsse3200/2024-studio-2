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
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossFactory {
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");

    /**
     * Creates a Kangaroo Boss entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createKangaBossEntity(Entity target) {
        BaseEnemyEntityConfig config = configs.kangarooBoss;
        Entity kangarooBoss = createBossNPC(target, Entity.EnemyType.KANGAROO, config);

        List<CombatMove> moveSet = new ArrayList<>(
                Arrays.asList(
                        new AttackMove("Jumping Jab", 10),
                        new GuardMove("Defensive Stance", 5),
                        new SleepMove("Rest and Recover", 0),
                        new SpecialKangaMove("Ground Slam", 25)
                )
        );

        kangarooBoss.addComponent(new CombatMoveComponent(moveSet));

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

        List<CombatMove> moveSet = new ArrayList<>(
                Arrays.asList(
                        new AttackMove("Tidal Strike", 5),
                        new GuardMove("Ocean's Shield", 15),
                        new SleepMove("Deep Rest", 0),
                        new SpecialWaterMove("Maelstrom Fury", 15)
                )
        );

        waterBoss.addComponent(new CombatMoveComponent(moveSet));

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

        List<CombatMove> moveSet = new ArrayList<>(
                Arrays.asList(
                        new AttackMove("Wind Slash", 5),
                        new GuardMove("Feather Guard", 5),
                        new SleepMove("Soaring Rest", 0),
                        new SpecialAirMove("Hurricane Dive", 20)
                )
        );

        airBoss.addComponent(new CombatMoveComponent(moveSet));

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
                    .addTask(new KangaJoeyTask(target, 9f, 2));
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
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), false, true, 1))
                .addComponent(new BossAnimationController());
        
        npc.setEnemyType(type);
        
        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }
    
    //never call
    private BossFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
    
}
