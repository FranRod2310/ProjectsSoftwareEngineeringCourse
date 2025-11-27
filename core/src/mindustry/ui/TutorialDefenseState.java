package mindustry.ui;

import arc.Core; // Added this import
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.Turret;

import static mindustry.Vars.tilesize;

public class TutorialDefenseState implements TutorialState {
    private Tutorial context;

    // Internal steps for the defense sequence
    private enum Step {
        SHOW_PATH,
        PLACE_TURRET,
        PLACE_WALL,
        EXPLAIN_AMMO,
        SUPPLY_AMMO,
        DEFEND_WAVE,
        FINISHED
    }

    private Step currentStep = Step.SHOW_PATH;
    private Tile coreTile;
    private Tile enemySpawnTile;
    private Tile turretTargetTile;
    private float timer = seconds(0);
    private boolean waveSpawned = false;

    private final int targetX = 50;
    private final int targetY = 57;

    @Override
    public void enter() {
        if (Vars.player.team().core() != null) {
            coreTile = Vars.player.team().core().tile;
            Vars.player.team().core().items.set(Items.copper, 1000); //testing only
        }

        enemySpawnTile = Vars.world.tile(49, 76);

        // Fallback
        if (enemySpawnTile == null) {
            enemySpawnTile = Vars.player.tileOn();
        }

        turretTargetTile = Vars.world.tile(targetX, targetY);
        if(turretTargetTile == null && Vars.player != null) turretTargetTile = Vars.player.tileOn();

        Events.run(EventType.Trigger.draw, this::onDraw);
    }

    @Override
    public void update() {
        timer += Time.delta;
        forceSafeRules();

        switch (currentStep) {
            //show enemy spawn path
            case SHOW_PATH:

                if (timer < Time.delta * 2) {
                    Vars.ui.announce("Enemies will approach from the red circle.", 4);
                }

                float targetX, targetY;

                // camera at enemy spawn for 4s
                if (timer < seconds(4) && enemySpawnTile != null) {
                    targetX = enemySpawnTile.worldx();
                    targetY = enemySpawnTile.worldy();

                } else {
                    // return camera to player
                    targetX = Vars.player.x;
                    targetY = Vars.player.y;
                }

                // move camera smoothly
                Core.camera.position.lerp(new arc.math.geom.Vec2(targetX, targetY), 0.08f);

                // next step after 7s
                if (timer > seconds(7)) {
                    currentStep = Step.PLACE_TURRET;
                    timer = 0;
                    Vars.ui.announce("Build a Duo Turret in the highlighted area!", 5);
                }
                break;

            case PLACE_TURRET:
                // check if duo is in right place
                if (turretTargetTile.block() == Blocks.tutorialDuo) {
                    currentStep = Step.PLACE_WALL;
                    Vars.ui.announce("Now, protect the turret with Copper Walls.", 5);
                }
                break;

            case PLACE_WALL:
                int wallsBuilt = 0;
                int requiredWalls = 5;

                for (int xOffset = -2; xOffset <= 2; xOffset++) {
                    // row above turret
                    Tile checkTile = turretTargetTile.nearby(xOffset, 1);

                    if (checkTile != null && checkTile.block() == Blocks.tutorialCopperWall) {
                        wallsBuilt++;
                    }
                }

                // if all walls built
                if (wallsBuilt >= requiredWalls) {
                    currentStep = Step.EXPLAIN_AMMO;
                    timer = 0;
                }
                break;

            case EXPLAIN_AMMO:

                if (timer < Time.delta * 2) {
                    Vars.ui.announce("The turret has no ammo!", 3);
                }

                if (timer > seconds(3) && timer < seconds(3) + Time.delta * 2) {
                    Vars.ui.announce("It requires Copper to fire.", 3);
                }

                if (timer > seconds(6)) {
                    currentStep = Step.SUPPLY_AMMO;
                    timer = 0;
                    Vars.ui.announce("Objective: Supply Copper to the Turret using Conveyors");
                }
                break;


            case SUPPLY_AMMO:
                // get turret
                Building build = Vars.world.build(turretTargetTile.x, turretTargetTile.y);

                if (build != null) {
                    boolean hasAmmo = false;


                    if (build instanceof Turret.TurretBuild tb) {
                        if (tb.totalAmmo > 0) hasAmmo = true;
                    }

                    if (build.items != null && build.items.total() > 0)
                        hasAmmo = true;

                    if (hasAmmo) {
                        currentStep = Step.DEFEND_WAVE;
                        timer = 0;
                        Vars.ui.announce("Enemy approaching. Defend the base!");
                    }
                }
                break;

            //TODO: enemies keep exploding on spawn, WHY?????
            //not working
            case DEFEND_WAVE:
                if (!waveSpawned && timer > seconds(2)) {
                    float spawnX = (enemySpawnTile != null) ? enemySpawnTile.worldx() : Vars.player.x;
                    float spawnY = (enemySpawnTile != null) ? enemySpawnTile.worldy() : Vars.player.y;

                    Unit u1 = UnitTypes.flare.create(Team.green);
                    u1.set(spawnX, spawnY);
                    u1.health = 100000f;
                    u1.add();

                    Unit u2 = UnitTypes.flare.create(Team.green);
                    u2.set(spawnX + 10f, spawnY + 5f);
                    u2.health = 100000f;
                    u2.add();

                    waveSpawned = true;
                    Vars.ui.announce("Targets detected...", 3);
                }

                //TODO: just send final messages, maybe go to a TutorialCompleteState?
            case FINISHED:
                if (timer > seconds(4)) {
                    context.nextState();
                }
                break;
        }
    }

    // desperate try to fix enemy spawning... not working
    private void forceSafeRules() {
        Vars.state.rules.unitCap = 9999;
        Vars.state.rules.limitMapArea = false;
        Vars.state.rules.dropZoneRadius = 0;
        Vars.state.rules.waves = false;
        Vars.state.rules.canGameOver = false;
        Vars.state.rules.polygonCoreProtection = false;
        Vars.state.rules.enemyCoreBuildRadius = 0;
    }

    private void onDraw() {
        // Drawing logic based on current step
        if (currentStep == Step.SHOW_PATH && enemySpawnTile != null) {
            Drawf.circles(enemySpawnTile.worldx(), enemySpawnTile.worldy(), tilesize * 3f + Mathf.absin(10f, 5f), Pal.health);
            Drawf.arrow(enemySpawnTile.worldx(), enemySpawnTile.worldy(), coreTile.worldx(), coreTile.worldy(),
                    tilesize * 2f, 4f, Pal.health);
        }

        if (currentStep == Step.PLACE_TURRET && turretTargetTile != null) {
            Drawf.square(turretTargetTile.worldx(), turretTargetTile.worldy(),
                    tilesize * 2f + Mathf.absin(10f, 2f), Pal.accent);
            Drawf.arrow(turretTargetTile.worldx(), turretTargetTile.worldy() + 40f,
                    turretTargetTile.worldx(), turretTargetTile.worldy() + 8f,
                    tilesize, 4f, Pal.accent);
        }

        if (currentStep == Step.PLACE_WALL && turretTargetTile != null) {
            float recWidth = tilesize * 5f;
            float recHeight = tilesize;
            float recX = turretTargetTile.worldx() - (tilesize * 2.5f);
            float recY = turretTargetTile.worldy() + (tilesize * 0.5f);
            Drawf.dashRect(Color.gold, recX, recY, recWidth, recHeight);
        }

        if (currentStep == Step.SUPPLY_AMMO && turretTargetTile != null) {
            Drawf.square(turretTargetTile.worldx(), turretTargetTile.worldy(), tilesize * 1.5f, Color.orange);
        }
        if (currentStep == Step.SUPPLY_AMMO && turretTargetTile != null) {
            float startX = turretTargetTile.worldx();
            float startY = turretTargetTile.worldy() - (tilesize * 3f);
            Drawf.arrow(startX, startY, turretTargetTile.worldx(), turretTargetTile.worldy(), tilesize * 2f, 4f, Color.orange);

        }

        if (currentStep == Step.DEFEND_WAVE && enemySpawnTile != null) {
            Drawf.target(enemySpawnTile.worldx(), enemySpawnTile.worldy(), tilesize * 1f, Color.scarlet);
        }


    }

    @Override
    public void exit() {
        // Cleanup if necessary
    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }

    // converts seconds into game ticks (60 ticks = 1 second)
    private float seconds(float sec) {
        return sec * 60f;
    }
}