package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.combat.move.*;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.components.npc.PigeonAnimationController;
import com.csse3200.game.components.npc.BigsawfishAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.components.FadeLightsDayTimeComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class EnemyFactory {
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemyNPCs.json");
    private static final List<CombatMove> moveSet = new ArrayList<>(
            Arrays.asList(
                    new AttackMove("Enemy Attack", 10),
                    new GuardMove("Enemy Guard", 5),
                    new SleepMove("Enemy Sleep", 0)
            )
    );
    private static final String SPAWN = "spawn";
    private static final String FLOAT = "float";

    /**
     * types of enemies
     */
    private enum EnemyType {
        FROG,
        CHICKEN,
        MONKEY,
        BEAR,
        BEE,
        EEL,
        OCTOPUS,
        PIGEON,
        BIGSAWFISH,
        MACAW,
        JOEY,
        HIVE;
    }

    /**
     * Creates a chicken enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy chicken entity
     */
    public static Entity createChicken(Entity target) {
        BaseEnemyEntityConfig config = configs.chicken;
        Entity chicken = createBaseEnemy(target, EnemyType.CHICKEN, config);
        chicken.setEnemyType(Entity.EnemyType.CHICKEN);

        TextureAtlas chickenAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(chickenAtlas);

        animator.addAnimation(SPAWN, 1.0f, Animation.PlayMode.NORMAL);
        animator.addAnimation("walk", 0.25f, Animation.PlayMode.LOOP);


        chicken
                .addComponent(animator)
                .addComponent(new ChickenAnimationController());

        chicken.getComponent(AnimationRenderComponent.class).scaleEntity();

        return chicken;
    }

    /**
     * Creates a bear enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy bear entity
     */
    public static Entity createBear(Entity target) {
        BaseEnemyEntityConfig config = configs.bear;
        Entity bear = createBaseEnemy(target, EnemyType.BEAR, config);
        bear.setEnemyType(Entity.EnemyType.BEAR);

        TextureAtlas bearAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(bearAtlas);

        animator.addAnimation("chase", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(FLOAT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(SPAWN, 1.0f, Animation.PlayMode.NORMAL);

        bear
                .addComponent(animator)
                .addComponent(new BearAnimationController());


        bear.setScale(2f,1.38f);

        return bear;
    }

    /**
     * Creates a big saw fish enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy bear entity
     */
    public static Entity createBigsawfish(Entity target) {
        BaseEnemyEntityConfig config = configs.bigsawfish;
        Entity bigsawfish = createBaseEnemy(target, EnemyType.BIGSAWFISH, config);
        bigsawfish.setEnemyType(Entity.EnemyType.BIGSAWFISH);

        TextureAtlas bigsawfishAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(bigsawfishAtlas);

        animator.addAnimation("chase", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("float", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("spawn", 1.0f, Animation.PlayMode.NORMAL);

        bigsawfish
                .addComponent(animator)
                .addComponent(new BigsawfishAnimationController());


        bigsawfish.setScale(2f,1.38f);

        return bigsawfish;
    }

    /**
     * Creates a green macaw enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy macaw entity
     */
    public static Entity createMacaw(Entity target) {
        BaseEnemyEntityConfig config = configs.macaw;
        Entity macaw = createBaseEnemy(target, EnemyType.MACAW, config);
        macaw.setEnemyType(Entity.EnemyType.MACAW);

        TextureAtlas macawAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(macawAtlas);

        animator.addAnimation("chase", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("spawn", 1.0f, Animation.PlayMode.NORMAL);

        macaw
                .addComponent(animator)
                .addComponent(new MacawAnimationController());


        macaw.setScale(2f,1.38f);

        return macaw;
    }

    /**
     * Creates an undersea octopus enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy octopus entity
     */
    public static Entity createOctopus(Entity target) {
        BaseEnemyEntityConfig config = configs.octopus;
        Entity octopus = createBaseEnemy(target, EnemyType.OCTOPUS, config);
        octopus.setEnemyType(Entity.EnemyType.OCTOPUS);

        TextureAtlas octopusAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(octopusAtlas);

        animator.addAnimation("chase", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("float", 0.5f, Animation.PlayMode.LOOP);

        octopus
                .addComponent(animator)
                .addComponent(new OctopusAnimationController());


        octopus.setScale(1.3f,1.0f);

        return octopus;
    }

    /**
     * Creates a bee enemy.
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy bee entity
     */
    public static Entity createBee(Entity target) {
        BaseEnemyEntityConfig config = configs.bee;
        Entity bee = createBaseEnemy(target, EnemyType.BEE, config);
        bee.setEnemyType(Entity.EnemyType.BEE);

        TextureAtlas beeAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(beeAtlas);

        animator.addAnimation("float", 1.0f, Animation.PlayMode.LOOP);
        animator.addAnimation("chase", 1.0f,Animation.PlayMode.LOOP);
        animator.addAnimation("alert", 1.0f, Animation.PlayMode.NORMAL);

        bee
                .addComponent(animator)
                .addComponent(new BeeAnimationController());

        bee.setScale(0.542f,0.35f);

        return bee;
    }

    /**
     * Creates a pigeon enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy pigeon entity
     */
    public static Entity createPigeon(Entity target) {
        BaseEnemyEntityConfig config = configs.pigeon;
        Entity pigeon = createBaseEnemy(target, EnemyType.PIGEON, config);
        pigeon.setEnemyType(Entity.EnemyType.PIGEON);

        TextureAtlas pigeonAtlas = ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class);

        AnimationRenderComponent animator = new AnimationRenderComponent(pigeonAtlas);

        animator.addAnimation(FLOAT, 0.06f, Animation.PlayMode.LOOP);
        animator.addAnimation(SPAWN, 1.0f, Animation.PlayMode.NORMAL);

        pigeon
                .addComponent(animator)
                .addComponent(new PigeonAnimationController());

        pigeon.setScale(0.842f,0.55f);

        return pigeon;
    }

    /**
     * Creates a frog enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy frog entity
     */
    public static Entity createFrog(Entity target) {
        BaseEnemyEntityConfig config = configs.frog;
        Entity frog = createBaseEnemy(target, EnemyType.FROG, config);
        frog.setEnemyType(Entity.EnemyType.FROG);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
        animator.addAnimation("jump", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("still", 0.1f, Animation.PlayMode.LOOP);

        frog
                .addComponent(animator)
                .addComponent(new FrogAnimationController());

        frog.getComponent(AnimationRenderComponent.class).scaleEntity();

        return frog;
    }

    /**
     * Creates a eel enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy frog entity
     */
    public static Entity createEel(Entity target) {
        BaseEnemyEntityConfig config = configs.eel;
        Entity eel = createBaseEnemy(target, EnemyType.EEL, config);
        eel.setEnemyType(Entity.EnemyType.EEL);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
        animator.addAnimation("swim_down", 0.25f, Animation.PlayMode.LOOP);
        animator.addAnimation("swim_down_right", 0.25f, Animation.PlayMode.LOOP);
        animator.addAnimation("swim_right", 0.25f, Animation.PlayMode.LOOP);
        animator.addAnimation("swim_up_right", 0.25f, Animation.PlayMode.LOOP);
        animator.addAnimation("swim_up", 0.25f, Animation.PlayMode.LOOP);

        eel
                .addComponent(animator)
                .addComponent(new EelAnimationController());

        eel.getComponent(AnimationRenderComponent.class).scaleEntity();

        return eel;
    }

    /**
     * Creates a monkey enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy monkey entity
     */
    public static Entity createMonkey(Entity target) {
        BaseEnemyEntityConfig config = configs.monkey;
        Entity monkey = createBaseEnemy(target, EnemyType.MONKEY, config);
        monkey.setEnemyType(Entity.EnemyType.MONKEY);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
        animator.addAnimation("run_down", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_up", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_left_down", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_right_down", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_left_up", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_right_up", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("wait", 0.1f, Animation.PlayMode.LOOP);

        monkey
                .addComponent(animator)
                .addComponent(new MonkeyAnimationController());

        monkey.getComponent(AnimationRenderComponent.class).scaleEntity();

        return monkey;
    }

    /**
     * Creates a joey enemy.
     *
     * @param target entity to chase (player in most cases, but does not have to be)
     * @return enemy joey entity
     */
    public static Entity createJoey(Entity target) {
        BaseEnemyEntityConfig config = configs.joey;
        Entity joey = createBaseEnemy(target, EnemyType.JOEY, config);
        joey.setEnemyType(Entity.EnemyType.JOEY);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.getSpritePath(), TextureAtlas.class));
        animator.addAnimation("wander", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("chase", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("spawn", 1.0f, Animation.PlayMode.NORMAL);

        joey
                .addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(), config.getDefense(), config.getSpeed(), config.getExperience(), 100, false, false, 1))
                .addComponent(new CombatMoveComponent(moveSet))
                .addComponent(animator)
                .addComponent(new JoeyAnimationController());

        joey.getComponent(AnimationRenderComponent.class).scaleEntity();
        joey.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(new Vector2(config.getSpeed(), config.getSpeed()));

        return joey;
    }


    /**
     * Creates a generic Enemy with specific tasks depending on the enemy type.
     *
     * @param target the enemy target
     * @param type the enemy type
     * @return entity
     */
    private static Entity createBaseEnemy(Entity target, EnemyType type, BaseEnemyEntityConfig config) {
        AITaskComponent aiComponent = new AITaskComponent();

        BaseEnemyEntityConfig configStats = switch (type) { // assign config variable depending on enemy to inherit speed stat
            case FROG -> configs.frog;
            case CHICKEN -> configs.chicken;
            case MONKEY -> configs.monkey;
            case BEAR -> configs.bear;
            case BEE -> configs.bee;
            case EEL -> configs.eel;
            case OCTOPUS -> configs.octopus;
            case PIGEON -> configs.pigeon;
            case BIGSAWFISH -> configs.bigsawfish;
            case MACAW -> configs.macaw;
            case JOEY -> configs.joey;
            case HIVE -> configs.hive;
        };

        switch (type) {
            case EnemyType.MONKEY -> {
                // Adding SpecialWanderTask with correct entity speed, changes all animal movement speed
                aiComponent.addTask(new SpecialWanderTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 2f));
                aiComponent.addTask(new RunTask(target, 10, 3f));
                aiComponent.addTask(new ShootTask(1000, target, 5f));
            }
            case EnemyType.BEAR -> {
                //BlindBear makes bears wonder away from the player when the player isn't moving
                aiComponent.addTask(new BlindBearTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 1f, 3, target, 6f));
                aiComponent.addTask(new WanderTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 2f, false));
                aiComponent.addTask(new ChaseTask(target, 2, 6f, 7f, new Vector2((float) configStats.getSpeed() / 100 * 4, (float) configStats.getSpeed() / 100 * 4), false));
            }
            case EnemyType.EEL -> {
                aiComponent.addTask(new SpecialWanderTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 2f));
                aiComponent.addTask(new ChaseTask(target, 4, 10f, 8f, new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), false));
                aiComponent.addTask(new ShootTask(5000, target, 10f));
            }
            case EnemyType.PIGEON -> {
                aiComponent.addTask(new StealTask(((ForestGameArea)MapHandler.getCurrentMap()).getDynamicItems(), 2f));
            }
            case EnemyType.MACAW -> {
                aiComponent.addTask(new SpecialWanderTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 2f));
                aiComponent.addTask(new ChaseTask(target, 10, 3f, 4f, new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), false));
                aiComponent.addTask(new ShootTask(5000, target, 6f));
            }
            case EnemyType.JOEY -> {
                aiComponent.addTask(new SpecialWanderTask(new Vector2(configStats.getSpeed(), configStats.getSpeed()), 2f));
                aiComponent.addTask(new ChaseTask(target, 10, 10f, 12f, new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), false));
            }
            default -> {
                // Adding SpecialWanderTask with correct entity speed, changes all animal movement speed
                aiComponent.addTask(new WanderTask(new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), 2f, false));
                aiComponent.addTask(new ChaseTask(target, 10, 3f, 4f, new Vector2((float) configStats.getSpeed() / 100, (float) configStats.getSpeed() / 100), false));
            }
        }

        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
                        .addComponent(new ConfigComponent<>(configStats))
                        .addComponent(aiComponent)
                        .addComponent(new CombatStatsComponent(config.getHealth() + (int)(Math.random() * 2) - 1,
                                config.getHunger(), Math.max(0, config.getBaseAttack() + (int)(Math.random() * 5) - 2),
                                config.getDefense() + (int)(Math.random() * 2), config.getSpeed(),
                                config.getExperience(), 100, false, false, 1))
                        .addComponent(new CombatMoveComponent(moveSet))
                        .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(2f, Color.SCARLET)))
                        .addComponent(new FadeLightsDayTimeComponent());

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }

    /**
     * Creates a boss NPC to be used as a boss entity by more specific NPC creation methods.
     *
     * @return entity
     */
    public static Entity createCombatNPC(BaseEnemyEntityConfig config) {
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER));
        
        
        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        npc.addComponent(new CombatStatsComponent(config.getHealth(), config.getHunger(), config.getBaseAttack(),
                config.getDefense(), config.getSpeed(), config.getExperience(), 100, false, false, 1));

        return npc;
    }
    
    
    /**
     * Creates chicken enemy as NPC entity for static combat.
     * */
    public static Entity createChickenCombatEnemy() {
        BaseEnemyEntityConfig config = configs.chicken;
        Entity chickenEnemy = createCombatNPC(config);
        chickenEnemy.setEnemyType(Entity.EnemyType.CHICKEN);
        
        chickenEnemy
                .addComponent(new TextureRenderComponent("images/chicken_idle.png"));
        chickenEnemy.scaleHeight(90.0f);
        
        return chickenEnemy;
    }
    
    /**
     * Creates monkey enemy as NPC entity for static combat
     * */
    public static Entity createMonkeyCombatEnemy() {
        BaseEnemyEntityConfig config = configs.monkey;
        Entity monkeyEnemy = createCombatNPC(config);
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
        BaseEnemyEntityConfig config = configs.frog;
        Entity frogEnemy = createCombatNPC(config);
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
        BaseEnemyEntityConfig config = configs.bear;
        Entity bearEnemy = createCombatNPC(config);
        bearEnemy.setEnemyType(Entity.EnemyType.BEAR);
        
        bearEnemy
                .addComponent(new TextureRenderComponent("images/bear_idle.png"));
        
        bearEnemy.setScale(150f,103.5f);
        
        return bearEnemy;
    }
    
    /**
     * Creates bee enemy as NPC entity for static combat
     * */
    public static Entity createBeeCombatEnemy() {
        BaseEnemyEntityConfig config = configs.bee;
        Entity beeEnemy = createCombatNPC(config);
        beeEnemy.setEnemyType(Entity.EnemyType.BEE);
        
        beeEnemy
                .addComponent(new TextureRenderComponent("images/bee_idle.png"));
        
        beeEnemy.setScale(90f, 103.5f);
        
        return beeEnemy;
    }

    /**
     * Creates octopus enemy as NPC entity for static combat
     * */
    public static Entity createOctopusCombatEnemy() {
        BaseEnemyEntityConfig config = configs.octopus;
        Entity octopusEnemy = createCombatNPC(config);
        octopusEnemy.setEnemyType(Entity.EnemyType.OCTOPUS);

        octopusEnemy
                .addComponent(new TextureRenderComponent("images/octopus_idle.png"));
        octopusEnemy.scaleHeight(90.0f);

        return octopusEnemy;
    }


    /**
     * Creates big saw fish enemy as NPC entity for static combat
     * */
    public static Entity createBigsawfishCombatEnemy() {
        BaseEnemyEntityConfig config = configs.bigsawfish;
        Entity bigsawfishEnemy = createCombatNPC(config);
        bigsawfishEnemy.setEnemyType(Entity.EnemyType.BIGSAWFISH);
        
        bigsawfishEnemy
                .addComponent(new TextureRenderComponent("images/bigsawfish_idle.png"));
        bigsawfishEnemy.scaleHeight(90.0f);
        
        return bigsawfishEnemy;
    }
    
    /**
     * Creates macaw enemy as NPC entity for static combat
     * */
    public static Entity createMacawCombatEnemy() {
        BaseEnemyEntityConfig config = configs.macaw;
        Entity macawEnemy = createCombatNPC(config);
        macawEnemy.setEnemyType(Entity.EnemyType.MACAW);
        
        macawEnemy
                .addComponent(new TextureRenderComponent("images/macaw_idle.png"));
        macawEnemy.scaleHeight(90.0f);
        
        return macawEnemy;
    }
    
    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createPigeonCombatEnemy() {
        BaseEnemyEntityConfig config = configs.pigeon;
        Entity pigeonEnemy = createCombatNPC(config);
        pigeonEnemy.setEnemyType(Entity.EnemyType.PIGEON);
        
        pigeonEnemy
                .addComponent(new TextureRenderComponent("images/pigeon_idle.png"));
        pigeonEnemy.setScale(100f,70f);
        
        return pigeonEnemy;
    }
    
    /**
     * Creates pigeon enemy as NPC entity for static combat
     * */
    public static Entity createEelCombatEnemy() {
        BaseEnemyEntityConfig config = configs.eel;
        Entity eelEnemy = createCombatNPC(config);
        eelEnemy.setEnemyType(Entity.EnemyType.EEL);
        
        eelEnemy
                .addComponent(new TextureRenderComponent("images/eel_idle.png"));
        eelEnemy.setScale(100f,70f);
        
        return eelEnemy;
    }

  /**
   * Creates joey enemy as NPC entity for static combat
   * */
  public static Entity createJoeyCombatEnemy() {
      BaseEnemyEntityConfig config = configs.joey;
    Entity joeyEnemy = createCombatNPC(config);
    joeyEnemy.setEnemyType(Entity.EnemyType.JOEY);

    joeyEnemy
            .addComponent(new TextureRenderComponent("images/joey_idle.png"));
    joeyEnemy.scaleHeight(90.0f);

    return joeyEnemy;
  }

  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
