package mindustry.ui;

import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
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
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.Turret;
import static mindustry.Vars.tilesize;

public class TutorialDefenseState implements TutorialState {
    private Tutorial context;

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

    private Cons<EventType.DrawEvent> drawTurretPlace;
    private Cons<EventType.DrawEvent> drawWallPlace;
    private Cons<EventType.DrawEvent> drawAmmoSupply;
    private Cons<EventType.DrawEvent> drawDefend;

    private static final int turretX = 50;
    private static final int turretY = 57;
    private static final int enemySpawnX = 49;
    private static final int enemySpawnY = 76;

    private Tile enemySpawnTile;
    private Tile turretTargetTile;

    private float timer = 0f;
    private boolean waveSpawned = false;
    private boolean textShown = false;

    private Unit unit1;
    private Unit unit2;

    private static final int AMMO_TARGET = 10;

    @Override
    public void enter() {

        currentStep = Step.SHOW_PATH;
        timer = 0f;
        waveSpawned = false;
        textShown = false;
        unit1 = null;
        unit2 = null;

        enemySpawnTile = Vars.world.tile(enemySpawnX, enemySpawnY);
        turretTargetTile = Vars.world.tile(turretX, turretY);

        //DEBUG
        //Vars.player.team().core().items.set(Items.copper, 1000);

        drawEvents();
    }

    @Override
    public void update() {
        if (!textShown)
            timer += Time.delta;
        forceSafeRules();

        switch (currentStep) {

            case SHOW_PATH:
                if (!textShown) {
                    textShown = true;
                    Vars.ui.showInfoOnHidden("CAUTION! Enemies are about to attack your base!\n\n" +
                            "It looks like they're coming from the north...", () -> {

                        currentStep = Step.PLACE_TURRET;

                        Vars.ui.showInfoOnHidden("First, build a DUO TURRET " + Blocks.duo.emoji() + " in the highlighted area!", () -> {
                            textShown = false;
                        });
                        Events.on(EventType.DrawEvent.class, drawTurretPlace);
                    });
                }
                break;

            case PLACE_TURRET:
                if (turretTargetTile.block() == Blocks.tutorialDuo && !textShown) {
                    textShown = true;
                    Vars.ui.showInfoOnHidden("Great! The turret is in place.\n\n" +
                            "Now, protect the turret with COPPER WALLs.  " + Blocks.copperWall.emoji(), () -> {

                        Events.remove(EventType.DrawEvent.class, drawTurretPlace);
                        Events.on(EventType.DrawEvent.class, drawWallPlace);

                        currentStep = Step.PLACE_WALL;

                        textShown = false;
                    });
                }
                break;

            case PLACE_WALL:
                int wallsBuilt = 0;

                for (int xOffset = -2; xOffset <= 2; xOffset++) {

                    Tile checkTile = turretTargetTile.nearby(xOffset, 1);

                    if (checkTile != null && checkTile.block() == Blocks.tutorialCopperWall) {
                        wallsBuilt++;
                    }
                }

                if (wallsBuilt >= 5 && !textShown) {
                    Events.remove(EventType.DrawEvent.class, drawWallPlace);
                    currentStep = Step.EXPLAIN_AMMO;
                }
                break;

            case EXPLAIN_AMMO:
                if (!textShown) {

                    textShown = true;
                    Vars.ui.showInfoOnHidden("The turret has no ammo!\n\n" +
                            "You need to supply it with COPPER " + Blocks.oreCopper.emoji() + " to defend against enemies.\n\n" +
                            "Use CONVEYOR BELTs " + Blocks.conveyor.emoji() + " to transport Copper to the turret.", () -> {

                        Events.on(EventType.DrawEvent.class, drawAmmoSupply);

                        currentStep = Step.SUPPLY_AMMO;

                        textShown = false;
                    });
                }
                break;

            case SUPPLY_AMMO:

                Building build = Vars.world.build(turretTargetTile.x, turretTargetTile.y);

                if (build instanceof Turret.TurretBuild tb) {
                    if (tb.totalAmmo >= AMMO_TARGET && !textShown) {

                        textShown = true;
                        Vars.ui.showInfoOnHidden("Great work! The turret is now loaded and ready to fire!\n\n" +
                                "Watch out, enemies " + UnitTypes.dagger.emoji() + " are arriving!", () -> {

                            Events.remove(EventType.DrawEvent.class, drawAmmoSupply);
                            Events.on(EventType.DrawEvent.class, drawDefend);

                            currentStep = Step.DEFEND_WAVE;

                            textShown = false;
                            timer = 0;
                        });
                    }
                }
                break;

            case DEFEND_WAVE:
                if (!waveSpawned && timer > 60f * 2f) {
                    float spawnX = enemySpawnTile.worldx();
                    float spawnY = enemySpawnTile.worldy();

                    unit1 = UnitTypes.dagger.create(Team.green);
                    unit1.health = 50;
                    unit1.set(spawnX, spawnY);
                    unit1.add();

                    unit2 = UnitTypes.dagger.create(Team.green);
                    unit2.health = 60;
                    unit2.set(spawnX + 10f, spawnY - 10f);
                    unit2.add();

                    waveSpawned = true;
                }

                boolean u1Dead = (unit1 == null || unit1.dead || !unit1.isAdded());
                boolean u2Dead = (unit2 == null || unit2.dead || !unit2.isAdded());

                if (waveSpawned && u1Dead && u2Dead && !textShown) {
                    textShown = true;
                    Vars.ui.showInfoOnHidden("Great work! Enemy wave DEFEATED!\n\n" +
                            "Your core is safe... for now.", () -> {

                        Events.remove(EventType.DrawEvent.class, drawDefend);

                        currentStep = Step.FINISHED;

                        textShown = false;
                    });
                }
                break;

            case FINISHED:
                if (!textShown) {

                    textShown = true;
                    Vars.ui.showInfoOnHidden("TUTORIAL COMPLETE!\n\n" +
                            "These are the basic of MINDUSTRY, but there's much more to explore!\n\n" +
                            "Jump into the campaign and HAVE FUN!", () -> {
                        textShown = false;
                    });
                }
                context.nextState();
                break;
        }
    }

    private void forceSafeRules() {
        Vars.state.rules.unitCap = 9999;
        Vars.state.rules.waves = true;
        Vars.state.rules.attackMode = true;
        Vars.state.rules.unitCrashDamageMultiplier = 0;
    }

    private void drawEvents() {

        drawTurretPlace = (e) -> {
            Drawf.square(turretTargetTile.worldx(), turretTargetTile.worldy(), tilesize * 2f, Pal.accent);

            Drawf.arrow(turretTargetTile.worldx(), turretTargetTile.worldy() + 40f,
                    turretTargetTile.worldx(), turretTargetTile.worldy() + 8f,
                    tilesize, 4f, Pal.accent);

            mindustry.ui.Fonts.outline.draw(
                    "Place DUO TURRET " + Blocks.duo.emoji() + " here!",
                    turretTargetTile.worldx(),
                    turretTargetTile.worldy() + (tilesize * 2.5f),
                    Pal.accent, 0.30f, false, arc.util.Align.center
            );
        };


        drawWallPlace = (e) -> {
            float recWidth = tilesize * 5f;
            float recHeight = tilesize;
            float recX = turretTargetTile.worldx() - (tilesize * 2.5f);
            float recY = turretTargetTile.worldy() + (tilesize * 0.5f);

            Drawf.dashRect(Color.gold, recX, recY, recWidth, recHeight);

            mindustry.ui.Fonts.outline.draw(
                    "Place COPPER WALLs " + Blocks.copperWall.emoji(),
                    turretTargetTile.worldx(),
                    turretTargetTile.worldy() + (tilesize * 2.5f),
                    Color.gold, 0.30f, false, arc.util.Align.center
            );
        };


        drawAmmoSupply = (e) -> {
            Drawf.square(turretTargetTile.worldx(), turretTargetTile.worldy(), tilesize * 1.5f, Color.orange);

            float startX = turretTargetTile.worldx();
            float startY = turretTargetTile.worldy() - (tilesize * 3f);
            Drawf.arrow(startX, startY, turretTargetTile.worldx(), turretTargetTile.worldy(), tilesize * 2f, 4f, Color.orange);

            Building build = Vars.world.build(turretTargetTile.x, turretTargetTile.y);
            int currentAmmo = 0;
            if (build instanceof Turret.TurretBuild tb) {
                currentAmmo = (int) tb.totalAmmo;
            }
            int displayAmmo = Math.min(currentAmmo, AMMO_TARGET);

            mindustry.ui.Fonts.outline.draw(
                    displayAmmo + " / " + AMMO_TARGET + " COPPER " + Blocks.oreCopper.emoji(),
                    turretTargetTile.worldx(),
                    turretTargetTile.worldy() + (tilesize * 1.5f),
                    Pal.accent, 0.50f, false, arc.util.Align.center
            );
        };


        drawDefend = (e) -> {
            if (enemySpawnTile != null) {
                Drawf.target(enemySpawnTile.worldx(), enemySpawnTile.worldy(), tilesize * 1f, Color.scarlet);
            }
        };
    }

    @Override
    public void exit() {
        Events.remove(EventType.DrawEvent.class, drawTurretPlace);
        Events.remove(EventType.DrawEvent.class, drawWallPlace);
        Events.remove(EventType.DrawEvent.class, drawAmmoSupply);
        Events.remove(EventType.DrawEvent.class, drawDefend);
    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}