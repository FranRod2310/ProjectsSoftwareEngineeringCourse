# Smell #1 - Data clumps
 `...\core\src\mindustry\entities\units\Units`

 ### Code snippet

 ```java
public static boolean invalidateTarget(Posc target, Team team, float x, float y) {...}

public static boolean anyEntities(float x, float y, float size) {...}

public static boolean anyEntities(float x, float y, float width, float height) {...}

public static boolean anyEntities(float x, float y, float width, float height, boolean ground) {...}

```

### Rationale
These methods take the same set of parameters for their funcionality. This is a case of a **Data Clumps** code smell.

### Suggested Refactoring
Create new objects, `Area` and `TargetQuery` to pass as arguments for such methods.

## New Area parameter object
```java
// Object for rectangular areas (replaces x,y,width,height)
public final class Area {
    public final float x, y, width, height;

    public Area(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    (...)

    public static Area fromCenter(float cx, float cy, float size){
        return new Area(cx - size/2f, cy - size/2f, size, size);
    }

    public Rect toRect(){
        return new Rect(x, y, width, height);
    }
}
```
## New TargetQuery object
```java
// Object for target-query info (replaces team/x/y/range/flags)
public final class TargetQuery {
    public final Team team;
    public final float x, y, range;
    public final boolean allowAir, allowGround;
    public final Boolf<Unit> unitPredicate; 

    public TargetQuery(Team team, float x, float y, float range, boolean allowAir, boolean allowGround, Boolf<Unit> unitPredicate){
        this.team = team;
        this.x = x;
        this.y = y;
        this.range = range;
        this.allowAir = allowAir;
        this.allowGround = allowGround;
        this.unitPredicate = unitPredicate != null ? unitPredicate : u -> true;
    }

    public TargetQuery(Team team, float x, float y, float range, boolean allowAir, boolean allowGround){
        this(team, x, y, range, allowAir, allowGround, u -> true);
    }

    public Rect bounds(){
        return new Rect(x - range, y - range, range * 2f, range * 2f);
    }

    /** Return whether the target is invalid according to this query. */
    public boolean invalidates(Posc target){
        if(target == null) return true;
        float extra = (target instanceof mindustry.entities.Sized hb ? hb.hitSize()/2f : 0f);
        if(range != Float.MAX_VALUE && !target.within(x, y, range + extra)) return true;
        if(target instanceof mindustry.game.Teamc tc && tc.team() == team) return true;
        if(target instanceof mindustry.world.blocks.ProductionBlock.Healthc hc && !hc.isValid()) return true; // defensive check if Healthc exists
        if(target instanceof Unit u && !u.targetable(team)) return true;
        return false;
    }
}
```

This now simplifies the parameter list for many methods inside the Units class:
```java

// BEFORE:
public static boolean invalidateTarget(Posc target, Team team, float x, float y)
// AFTER:
public static boolean invalidateTarget(Posc target, TargetQuery q)

// BEFORE:
public static boolean anyEntities(float x, float y, float width, float height, boolean ground)
// AFTER:
public static boolean anyEntities(Area area, boolean ground)

// BEFORE:
public static boolean anyEntities(float x, float y, float size)
// AFTER:
public static boolean anyEntities(Area area)
```

# Smell #2 - Message Chaining
`...\core\src\mindustry\type\Planet`

## Code Snippet

```java
    public void drawSelection(VertexBatch3D batch, Sector sector, Color color, float stroke, float length){
        float arad = (outlineRad + length) * radius;

        for(int i = 0; i < sector.tile.corners.length; i++){
            Corner next = sector.tile.corners[(i + 1) % sector.tile.corners.length];
            Corner curr = sector.tile.corners[i];

            next.v.scl(arad);
            curr.v.scl(arad);
            sector.tile.v.scl(arad);

            Tmp.v31.set(curr.v).sub(sector.tile.v).setLength(curr.v.dst(sector.tile.v) - stroke).add(sector.tile.v);
            Tmp.v32.set(next.v).sub(sector.tile.v).setLength(next.v.dst(sector.tile.v) - stroke).add(sector.tile.v);

            batch.tri(curr.v, next.v, Tmp.v31, color);
            batch.tri(Tmp.v31, next.v, Tmp.v32, color);

            sector.tile.v.scl(1f / arad);
            next.v.scl(1f / arad);
            curr.v.scl(1f /arad);
        }
    }
```

### Rationale

In the `drawSelection(...)` method, we can find multiple cases of the **Message Chaining** code smell.
For example here:
```java
Tmp.v31.set(curr.v).sub(sector.tile.v).setLength(curr.v.dst(sector.tile.v) - stroke).add(sector.tile.v);
```
This chain exposes deep object structures in `Sector`, becoming hard to read and overly-complex.

### Suggested Refactoring
Add method in `Sector` to simplify `drawSelection(...)` logic, getting rid of large foreign object chains in `Planet`.

## Update `Sector`
```java
public class Sector {

(...)

    public void drawSelection(VertexBatch3D batch, Color color, float stroke, float arad){
        for(int i = 0; i < tile.corners.length; i++){
            Corner next = tile.corners[(i + 1) % tile.corners.length];
            Corner curr = tile.corners[i];

            Vec3 currS = Tmp.v31.set(curr.v).scl(arad);
            Vec3 nextS = Tmp.v32.set(next.v).scl(arad);
            Vec3 tileS = Tmp.v33.set(tile.v).scl(arad);

            Vec3 insetCurr = Tmp.v34.set(currS).sub(tileS).setLength(currS.dst(tileS) - stroke).add(tileS);
            Vec3 insetNext = Tmp.v35.set(nextS).sub(tileS).setLength(nextS.dst(tileS) - stroke).add(tileS);

            batch.tri(currS, nextS, insetCurr, color);
            batch.tri(insetCurr, nextS, insetNext, color);
        }
    }
}
```

## Update `Planet`
```java
public void drawSelection(VertexBatch3D batch, Sector sector, Color color, float stroke, float length){
    float arad = (outlineRad + length) * radius;
    sector.drawSelection(batch, color, stroke, arad);
}
```





# Smell #3 - Long Method

`...\core\src\mindustry\service\GameService`

## Code Snippet
Get ready, let's take a look at a method that has 439 lines of code.

```java
private void registerEvents(){
        allTransportSerpulo = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
        allTransportErekir = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.erekir) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);

        //cores are ignored since they're upgrades and can be skipped
        allSerpuloBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
        allErekirBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.erekir) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);

        unitsBuilt = Core.settings.getJson("units-built" , ObjectSet.class, String.class, ObjectSet::new);
        blocksBuilt = Core.settings.getJson("blocks-built" , ObjectSet.class, String.class, ObjectSet::new);
        t5s = ObjectSet.with(UnitTypes.omura, UnitTypes.reign, UnitTypes.toxopid, UnitTypes.eclipse, UnitTypes.oct, UnitTypes.corvus);

        checkAllBlocks(allBlocksErekir, allErekirBlocks);
        checkAllBlocks(allBlocksSerpulo, allSerpuloBlocks);

        //periodically check for various conditions
        float updateInterval = 2f;
        Timer.schedule(this::checkUpdate, updateInterval, updateInterval);

        if(Items.thorium.unlocked()) obtainThorium.complete();
        if(Items.titanium.unlocked()) obtainTitanium.complete();

        if(SectorPresets.origin.sector.isCaptured()){
            completeErekir.complete();
        }

        if(SectorPresets.planetaryTerminal.sector.isCaptured()){
            completeSerpulo.complete();
        }

        if(mods != null && mods.list().size > 0){
            installMod.complete();
        }

        if(Core.bundle.get("yes").equals("router")){
            routerLanguage.complete();
        }

        if(!Planets.serpulo.sectors.contains(s -> !s.isCaptured())){
            captureAllSectors.complete();
        }

        Events.run(Trigger.openConsole, () -> openConsole.complete());

        Events.run(Trigger.unitCommandAttack, () -> {
            if(campaign()){
                issueAttackCommand.complete();
            }
        });

        Events.on(UnitDestroyEvent.class, e -> {
            if(campaign()){
                if(e.unit.team != Vars.player.team()){
                    SStat.unitsDestroyed.add();

                    if(e.unit.isBoss()){
                        SStat.bossesDefeated.add();
                    }
                }
            }
        });

        Events.on(TurnEvent.class, e -> {
            float total = 0;
            for(Planet planet : content.planets()){
                for(Sector sec : planet.sectors){
                    if(sec.hasBase()){
                        for(ExportStat v : sec.info.production.values()){
                            if(v.mean > 0) total += v.mean * 60;
                        }
                    }
                }
            }

            SStat.maxProduction.max(Math.round(total));
        });

        Events.run(Trigger.update, () -> {
            //extremely lazy timer, I just don't care
            if(campaign() && !hoverUnitLiquid.isAchieved() && Core.graphics.getFrameId() % 20 == 0){
                var units = state.rules.defaultTeam.data().getUnits(UnitTypes.elude);
                if(units != null){
                    for(var unit : units){
                        if(unit.floorOn().isLiquid){
                            hoverUnitLiquid.complete();
                            break;
                        }
                    }
                }
            }

            if(campaign() && !player.dead() && player.unit().type.canBoost && player.unit().elevation >= 0.25f){
                boostUnit.complete();
            }
        });

        Events.run(Trigger.newGame, () -> Core.app.post(() -> {
            if(campaign() && player.core() != null && player.core().items.total() >= 10 * 1000){
                drop10kitems.complete();
            }
        }));

        Events.on(BuildingBulletDestroyEvent.class, e -> {
            if(campaign() && e.build.block == Blocks.scatter && e.build.team == state.rules.waveTeam && e.bullet.owner instanceof Unit u && u.type == UnitTypes.flare && u.team == player.team()){
                destroyScatterFlare.complete();
            }
        });

        Events.on(BlockBuildEndEvent.class, e -> {
            if(campaign() && state.rules.sector == SectorPresets.groundZero.sector && e.tile.block() == Blocks.coreNucleus){
                nucleusGroundZero.complete();
            }
        });

        Events.on(BlockBuildEndEvent.class, e -> {
            if(campaign() && e.unit != null && e.unit.isLocal() && !e.breaking){
                SStat.blocksBuilt.add();

                if(e.tile.block() == Blocks.router && e.tile.build.proximity.contains(t -> t.block == Blocks.router)){
                    chainRouters.complete();
                }

                if(e.tile.block() == Blocks.groundFactory){
                    buildGroundFactory.complete();
                }

                if((e.tile.build instanceof AttributeCrafterBuild a && a.attrsum > 0) || (e.tile.build instanceof SolidPumpBuild sp && sp.boost > 0)){
                    boostBuildingFloor.complete();
                }

                if(!allTransportOneMap.isAchieved()){
                    Block[] allTransports = state.rules.sector.planet == Planets.erekir ? allTransportErekir : allTransportSerpulo;
                    boolean all = true;
                    for(var block : allTransports){
                        if(state.rules.defaultTeam.data().getCount(block) == 0){
                            all = false;
                            break;
                        }
                    }
                    if(all){
                        allTransportOneMap.complete();
                    }
                }

                if(e.tile.block() == Blocks.mendProjector) buildMendProjector.complete();
                if(e.tile.block() == Blocks.overdriveProjector) buildOverdriveProjector.complete();

                if(e.tile.block() == Blocks.waterExtractor){
                    if(e.tile.getLinkedTiles(tmpTiles).contains(t -> t.floor().liquidDrop == Liquids.water)){
                        buildWexWater.complete();
                    }
                }

                if(blocksBuilt.add(e.tile.block().name)){
                    if(state.rules.sector.planet == Planets.erekir){
                        checkAllBlocks(allBlocksErekir, allErekirBlocks);
                    }else{
                        checkAllBlocks(allBlocksSerpulo, allSerpuloBlocks);
                    }

                    if(blocksBuilt.contains("meltdown") && blocksBuilt.contains("spectre") && blocksBuilt.contains("foreshadow")){
                        buildMeltdownSpectre.complete();
                    }

                    save();
                }

                if(!circleConveyor.isAchieved() && e.tile.block() instanceof Conveyor){
                    checked.clear();
                    check: {
                        Tile current = e.tile;
                        for(int i = 0; i < 4; i++){
                            checked.add(current.pos());
                            if(current.build == null) break check;
                            Tile next = current.nearby(current.build.rotation);
                            if(next != null && next.block() instanceof Conveyor){
                                current = next;
                            }else{
                                break check;
                            }
                        }

                        if(current == e.tile && checked.size == 4){
                            circleConveyor.complete();
                        }
                    }
                }
            }

            if(campaign() && e.unit != null && e.unit.isLocal() && e.breaking){
                //hacky way of testing for boulders without string contains/endsWith
                if(e.tile.block().breakSound == Sounds.rockBreak){
                    SStat.bouldersDeconstructed.add();
                }
            }
        });

        Events.on(TurnEvent.class, e -> {
            int total = 0;
            for(var planet : content.planets()){
                for(var sector : planet.sectors){
                    if(sector.hasBase()){
                        total += sector.items().total;
                    }
                }
            }

            SStat.totalCampaignItems.max(total);
        });

        Events.on(SectorLaunchLoadoutEvent.class, e -> {
            if(e.sector.planet == Planets.serpulo && !schematics.isDefaultLoadout(e.loadout)){
                launchCoreSchematic.complete();
            }
        });

        Events.on(UnitCreateEvent.class, e -> {
            if(campaign()){
                if(unitsBuilt.add(e.unit.type.name)){
                    SStat.unitTypesBuilt.max(content.units().count(u -> unitsBuilt.contains(u.name) && !u.isHidden()));
                    save();
                }

                if(t5s.contains(e.unit.type)){
                    buildT5.complete();
                }
            }
        });

        Events.on(UnitControlEvent.class, e -> {
            if(e.unit instanceof BlockUnitc unit && unit.tile().block == Blocks.router){
                becomeRouter.complete();
            }

            if(e.unit instanceof BlockUnitc unit && unit.tile() instanceof TurretBuild){
                controlTurret.complete();
            }
        });

        Events.on(SchematicCreateEvent.class, e -> {
            SStat.schematicsCreated.add();
        });

        Events.on(BlockDestroyEvent.class, e -> {
            if(campaign() && e.tile.team() != player.team()){
                SStat.blocksDestroyed.add();
            }
        });

        Events.on(MapMakeEvent.class, e -> SStat.mapsMade.add());

        Events.on(MapPublishEvent.class, e -> SStat.mapsPublished.add());

        Events.on(UnlockEvent.class, e -> {
            if(e.content == Items.thorium) obtainThorium.complete();
            if(e.content == Items.titanium) obtainTitanium.complete();
        });

        Events.run(Trigger.openWiki, openWiki::complete);

        Events.run(Trigger.importMod, installMod::complete);

        Events.run(Trigger.exclusionDeath, dieExclusion::complete);

        Events.on(UnitDrownEvent.class, e -> {
            if(campaign() && e.unit.isPlayer()){
                drown.complete();
            }
        });

        trigger(Trigger.impactPower, powerupImpactReactor);

        trigger(Trigger.flameAmmo, useFlameAmmo);

        trigger(Trigger.turretCool, coolTurret);

        trigger(Trigger.suicideBomb, suicideBomb);

        trigger(Trigger.blastGenerator, blastGenerator);

        trigger(Trigger.forceProjectorBreak, breakForceProjector);

        trigger(Trigger.neoplasmReact, neoplasmWater);

        trigger(Trigger.shockwaveTowerUse, shockwaveTowerUse);

        Events.run(Trigger.enablePixelation, enablePixelation::complete);

        Events.run(Trigger.thoriumReactorOverheat, () -> {
            if(campaign()){
                SStat.reactorsOverheated.add();
            }
        });

        Events.on(GeneratorPressureExplodeEvent.class, e -> {
            if(campaign() && e.build.block == Blocks.neoplasiaReactor){
                neoplasiaExplosion.complete();
            }
        });

        trigger(Trigger.shock, shockWetEnemy);

        trigger(Trigger.blastFreeze, blastFrozenUnit);

        Events.on(UnitBulletDestroyEvent.class, e -> {
            if(state.isCampaign() && player != null && player.team() == e.bullet.team){

                if(e.bullet.owner instanceof WallBuild){
                    killEnemyPhaseWall.complete();
                }

                if(e.unit.type == UnitTypes.eclipse && e.bullet.owner instanceof TurretBuild turret && turret.block == Blocks.duo){
                    killEclipseDuo.complete();
                }

                if(e.bullet.type instanceof MassDriverBolt){
                    killMassDriver.complete();
                }
            }
        });

        Events.on(LaunchItemEvent.class, e -> {
            if(campaign()){
                launchItemPad.complete();
            }
        });

        Events.on(PickupEvent.class, e -> {
            if(e.carrier.isPlayer() && campaign() && e.unit != null && t5s.contains(e.unit.type)){
                pickupT5.complete();
            }
        });

        Events.on(UnitCreateEvent.class, e -> {
            if(campaign() && e.unit.team() == player.team()){
                SStat.unitsBuilt.add();
            }
        });

        Events.on(SectorLaunchEvent.class, e -> {
            SStat.timesLaunched.add();
        });

        Events.on(LaunchItemEvent.class, e -> {
            SStat.itemsLaunched.add(e.stack.amount);
        });

        Events.on(WaveEvent.class, e -> {
            if(campaign()){
                SStat.maxWavesSurvived.max(Vars.state.wave);

                if(state.stats.buildingsBuilt == 0 && state.wave >= 10){
                    survive10WavesNoBlocks.complete();
                }
            }
        });

        Events.on(PlayerJoin.class, e -> {
            if(Vars.net.server()){
                SStat.maxPlayersServer.max(Groups.player.size());
            }
        });

        Runnable checkUnlocks = () -> {
            if(Blocks.router.unlocked()) researchRouter.complete();

            if(!TechTree.all.contains(t -> t.content.locked())){
                researchAll.complete();
            }

            if(Blocks.microProcessor.unlocked()) researchLogic.complete();
        };

        //check unlocked stuff on load as well
        Events.on(ResearchEvent.class, e -> checkUnlocks.run());
        Events.on(UnlockEvent.class, e -> checkUnlocks.run());

        checkUnlocks.run();

        Events.on(WinEvent.class, e -> {
            if(state.rules.pvp){
                SStat.pvpsWon.add();
            }
        });

        Events.on(ClientPreConnectEvent.class, e -> {
            if(e.host != null && !e.host.address.startsWith("steam:") && !e.host.address.startsWith("192.")){
                joinCommunityServer.complete();
            }
        });

        Events.on(SectorCaptureEvent.class, e -> {
            if(e.sector.isBeingPlayed() || net.client()){
                if(Vars.state.wave <= 5 && state.rules.attackMode){
                    defeatAttack5Waves.complete();
                }

                if(state.stats.buildingsDestroyed == 0){
                    captureNoBlocksBroken.complete();
                }
            }

            if(Vars.state.rules.attackMode){
                SStat.attacksWon.add();
            }

            if(!e.sector.isBeingPlayed() && !net.client()){
                captureBackground.complete();
            }

            if(e.sector.planet == Planets.serpulo && !e.sector.planet.sectors.contains(s -> !s.hasBase())){
                captureAllSectors.complete();
            }

            if(e.sector.planet == Planets.erekir && e.sector.preset != null && e.sector.preset.isLastSector){
                completeErekir.complete();
            }

            if(e.sector.planet == Planets.serpulo && e.sector.preset != null && e.sector.preset.isLastSector){
                completeSerpulo.complete();
            }

            //TODO wrong
            if(e.sector.planet == Planets.serpulo){
                SStat.sectorsControlled.set(e.sector.planet.sectors.count(Sector::hasBase));
            }
        });

        Events.on(PayloadDropEvent.class, e -> {
            if(campaign() && e.unit != null && e.carrier.team == state.rules.defaultTeam && state.rules.waveTeam.cores().contains(c -> c.within(e.unit, state.rules.enemyCoreBuildRadius))){
                dropUnitsCoreZone.complete();
            }
        });

        Events.on(ClientChatEvent.class, e -> {
            if(e.message.contains(Iconc.alphaaaa + "")){
                useAnimdustryEmoji.complete();
            }
        });
    }
```

### Rationale

The `registerEvents()` method is very clearly a case of **Long Method** code smell.
Long methods like this can be hard to understand and hard to debug.

### Suggested Refactoring

To fix this, we can try to break down portions of code into seperate **Extract Methods**.

### Extract Methods to add

```java
private void registerBasicTriggers(){
    Events.run(Trigger.openConsole, () -> openConsole.complete());
    Events.run(Trigger.unitCommandAttack, () -> { if(campaign()) issueAttackCommand.complete(); });

    trigger(Trigger.impactPower, powerupImpactReactor);
    trigger(Trigger.flameAmmo, useFlameAmmo);
    trigger(Trigger.turretCool, coolTurret);
    trigger(Trigger.suicideBomb, suicideBomb);
    trigger(Trigger.blastGenerator, blastGenerator);
    trigger(Trigger.forceProjectorBreak, breakForceProjector);
    trigger(Trigger.neoplasmReact, neoplasmWater);
    trigger(Trigger.shockwaveTowerUse, shockwaveTowerUse);
    trigger(Trigger.shock, shockWetEnemy);
    trigger(Trigger.blastFreeze, blastFrozenUnit);

    Events.run(Trigger.enablePixelation, enablePixelation::complete);
    Events.run(Trigger.openWiki, openWiki::complete);
    Events.run(Trigger.importMod, installMod::complete);
    Events.run(Trigger.exclusionDeath, dieExclusion::complete);
    Events.run(Trigger.thoriumReactorOverheat, () -> { if(campaign()) SStat.reactorsOverheated.add(); });
}
```

```java
private void registerUnitHandlers(){
    Events.on(UnitDestroyEvent.class, e -> {
        if(!campaign()) return;
        if(e.unit.team != Vars.player.team()){
            SStat.unitsDestroyed.add();
            if(e.unit.isBoss()) SStat.bossesDefeated.add();
        }
    });

    Events.on(UnitCreateEvent.class, e -> {
        if(!campaign()) return;
        if(unitsBuilt.add(e.unit.type.name)){
            SStat.unitTypesBuilt.max(content.units().count(u -> unitsBuilt.contains(u.name) && !u.isHidden()));
            save();
        }
        if(t5s.contains(e.unit.type)) buildT5.complete();
    });

    Events.on(UnitControlEvent.class, e -> {
        if(e.unit instanceof BlockUnitc unit && unit.tile().block == Blocks.router) becomeRouter.complete();
        if(e.unit instanceof BlockUnitc unit2 && unit2.tile() instanceof TurretBuild) controlTurret.complete();
    });

    Events.on(UnitDrownEvent.class, e -> {
        if(campaign() && e.unit.isPlayer()) drown.complete();
    });

    Events.on(UnitCreateEvent.class, e -> {
        if(campaign() && e.unit.team() == player.team()) SStat.unitsBuilt.add();
    });
}
```

```java
private void registerBuildHandlers() {
    Events.on(BuildingBulletDestroyEvent.class, this::onBuildingBulletDestroy);
    Events.on(BlockBuildEndEvent.class, this::onBlockBuildEnd);
    Events.on(TurnEvent.class, this::onTurnEventTotalCampaignItems);
    Events.on(SectorLaunchLoadoutEvent.class, this::onSectorLaunchLoadout);
    Events.on(BlockDestroyEvent.class, this::onBlockDestroy);
    Events.on(MapMakeEvent.class, e -> SStat.mapsMade.add());
    Events.on(MapPublishEvent.class, e -> SStat.mapsPublished.add());
    Events.on(UnlockEvent.class, this::onUnlockEvent);
}
```
```java
private void onBuildingBulletDestroy(BuildingBulletDestroyEvent e) {
    if (!campaign()) return;

    if (e.build.block == Blocks.scatter
            && e.build.team == state.rules.waveTeam
            && e.bullet.owner instanceof Unit u
            && u.type == UnitTypes.flare
            && u.team == player.team()) {
        destroyScatterFlare.complete();
    }
}
```
```java
private void onBlockBuildEnd(BlockBuildEndEvent e) {
    // ground zero special case
    if (campaign() && state.rules.sector == SectorPresets.groundZero.sector && e.tile.block() == Blocks.coreNucleus) {
        nucleusGroundZero.complete();
    }

    if (!campaign()) return;

    if (e.unit != null && e.unit.isLocal() && !e.breaking) {
        SStat.blocksBuilt.add();

        if (e.tile.block() == Blocks.router && e.tile.build.proximity.contains(t -> t.block == Blocks.router)) {
            chainRouters.complete();
        }

        if (e.tile.block() == Blocks.groundFactory) {
            buildGroundFactory.complete();
        }

        if ((e.tile.build instanceof AttributeCrafterBuild a && a.attrsum > 0)
                || (e.tile.build instanceof SolidPumpBuild sp && sp.boost > 0)) {
            boostBuildingFloor.complete();
        }

        if (!allTransportOneMap.isAchieved()) {
            Block[] allTransports = state.rules.sector.planet == Planets.erekir ? allTransportErekir : allTransportSerpulo;
            boolean all = true;
            for (var block : allTransports) {
                if (state.rules.defaultTeam.data().getCount(block) == 0) {
                    all = false;
                    break;
                }
            }
            if (all) allTransportOneMap.complete();
        }

        if (e.tile.block() == Blocks.mendProjector) buildMendProjector.complete();
        if (e.tile.block() == Blocks.overdriveProjector) buildOverdriveProjector.complete();

        if (e.tile.block() == Blocks.waterExtractor) {
            if (e.tile.getLinkedTiles(tmpTiles).contains(t -> t.floor().liquidDrop == Liquids.water)) {
                buildWexWater.complete();
            }
        }

        if (blocksBuilt.add(e.tile.block().name)) {
            if (state.rules.sector.planet == Planets.erekir) {
                checkAllBlocks(allBlocksErekir, allErekirBlocks);
            } else {
                checkAllBlocks(allBlocksSerpulo, allSerpuloBlocks);
            }

            if (blocksBuilt.contains("meltdown") && blocksBuilt.contains("spectre") && blocksBuilt.contains("foreshadow")) {
                buildMeltdownSpectre.complete();
            }

            save();
        }

        if (!circleConveyor.isAchieved() && e.tile.block() instanceof Conveyor) {
            if (isCircleConveyor(e.tile)) {
                circleConveyor.complete();
            }
        }
    }

    if (campaign() && e.unit != null && e.unit.isLocal() && e.breaking) {
        // hacky way of testing for boulders without string contains/endsWith
        if (e.tile.block().breakSound == Sounds.rockBreak) {
            SStat.bouldersDeconstructed.add();
        }
    }
}
```
```java
private boolean isCircleConveyor(Tile start) {
    checked.clear();
    Tile current = start;
    for (int i = 0; i < 4; i++) {
        checked.add(current.pos());
        if (current.build == null) return false;
        Tile next = current.nearby(current.build.rotation);
        if (next != null && next.block() instanceof Conveyor) {
            current = next;
        } else {
            return false;
        }
    }
    return current == start && checked.size == 4;
}
```
```java
private void onTurnEventTotalCampaignItems(TurnEvent e) {
    int total = 0;
    for (Planet planet : content.planets()) {
        for (Sector sector : planet.sectors) {
            if (sector.hasBase()) {
                total += sector.items().total;
            }
        }
    }
    SStat.totalCampaignItems.max(total);
}
```
```java
private void onSectorLaunchLoadout(SectorLaunchLoadoutEvent e) {
    if (e.sector.planet == Planets.serpulo && !schematics.isDefaultLoadout(e.loadout)) {
        launchCoreSchematic.complete();
    }
}
```
```java
private void onBlockDestroy(BlockDestroyEvent e) {
    if (campaign() && e.tile.team() != player.team()) {
        SStat.blocksDestroyed.add();
    }
}
```
```java
private void onUnlockEvent(UnlockEvent e) {
    if (e.content == Items.thorium) obtainThorium.complete();
    if (e.content == Items.titanium) obtainTitanium.complete();
}
```
```java
private void registerPeriodicRuns(){
    Events.run(Trigger.update, () -> {
        if(!campaign()) return;

        // hover unit liquid check (throttled)
        if(!hoverUnitLiquid.isAchieved() && Core.graphics.getFrameId() % 20 == 0){
            var units = state.rules.defaultTeam.data().getUnits(UnitTypes.elude);
            if(units != null){
                for(var unit : units){
                    if(unit.floorOn().isLiquid){
                        hoverUnitLiquid.complete();
                        break;
                    }
                }
            }
        }

        SStat.maxUnitActive.max(Groups.unit.count(t -> t.team == player.team()));

        if(Groups.unit.count(u -> u.type == UnitTypes.poly && u.team == player.team()) >= 10) active10Polys.complete();

        for(Building entity : player.team().cores()){
            if(!content.items().contains(i -> i.isOnPlanet(state.getPlanet()) && entity.items.get(i) < entity.block.itemCapacity)){
                fillCoreAllCampaign.complete();
                break;
            }
        }

        for(var up : Groups.powerGraph){
            var graph = up.graph();
            if(graph.all.size > 1 && graph.all.first().team == player.team() && graph.hasPowerBalanceSamples()){
                float balance = graph.getPowerBalance() * 60f;
                if(balance < -10_000) negative10kPower.complete();
                if(balance > 100_000) positive100kPower.complete();
                if(graph.getBatteryStored() > 1_000_000) store1milPower.complete();
            }
        }
    });

    Events.run(Trigger.newGame, () -> Core.app.post(() -> {
        if(campaign() && player.core() != null && player.core().items.total() >= 10 * 1000) drop10kitems.complete();
    }));

    Events.on(WaveEvent.class, e -> {
        if(!campaign()) return;
        SStat.maxWavesSurvived.max(Vars.state.wave);
        if(state.stats.buildingsBuilt == 0 && state.wave >= 10) survive10WavesNoBlocks.complete();
    });

    Events.on(LaunchItemEvent.class, e -> SStat.itemsLaunched.add(e.stack.amount));
    Events.on(SectorLaunchEvent.class, e -> SStat.timesLaunched.add());
    Events.on(MapMakeEvent.class, e -> SStat.mapsMade.add());
    Events.on(MapPublishEvent.class, e -> SStat.mapsPublished.add());
    Events.on(SchematicCreateEvent.class, e -> SStat.schematicsCreated.add());
}
```

```java
private void registerMiscHandlers(){
    Events.on(SectorLaunchLoadoutEvent.class, e -> {
        if(e.sector.planet == Planets.serpulo && !schematics.isDefaultLoadout(e.loadout)) launchCoreSchematic.complete();
    });

    Events.on(UnitBulletDestroyEvent.class, e -> {
        if(!state.isCampaign() || player == null || player.team() != e.bullet.team) return;

        if(e.bullet.owner instanceof WallBuild) killEnemyPhaseWall.complete();
        if(e.unit.type == UnitTypes.eclipse && e.bullet.owner instanceof TurretBuild turret && turret.block == Blocks.duo) killEclipseDuo.complete();
        if(e.bullet.type instanceof MassDriverBolt) killMassDriver.complete();
    });

    Events.on(LaunchItemEvent.class, e -> { if(campaign()) launchItemPad.complete(); });
    Events.on(PickupEvent.class, e -> { if(e.carrier.isPlayer() && campaign() && e.unit != null && t5s.contains(e.unit.type)) pickupT5.complete(); });

    Events.on(PlayerJoin.class, e -> { if(Vars.net.server()) SStat.maxPlayersServer.max(Groups.player.size()); });

    Events.on(ClientPreConnectEvent.class, e -> {
        if(e.host != null && !e.host.address.startsWith("steam:") && !e.host.address.startsWith("192.")) joinCommunityServer.complete();
    });

    Events.on(SectorCaptureEvent.class, e -> {
        if(e.sector.isBeingPlayed() || net.client()){
            if(Vars.state.wave <= 5 && state.rules.attackMode) defeatAttack5Waves.complete();
            if(state.stats.buildingsDestroyed == 0) captureNoBlocksBroken.complete();
        }
        if(Vars.state.rules.attackMode) SStat.attacksWon.add();
        if(!e.sector.isBeingPlayed() && !net.client()) captureBackground.complete();

        if(e.sector.planet == Planets.serpulo && !e.sector.planet.sectors.contains(s -> !s.hasBase())) captureAllSectors.complete();
        if(e.sector.planet == Planets.erekir && e.sector.preset != null && e.sector.preset.isLastSector) completeErekir.complete();
        if(e.sector.planet == Planets.serpulo && e.sector.preset != null && e.sector.preset.isLastSector) completeSerpulo.complete();

        if(e.sector.planet == Planets.serpulo){
            SStat.sectorsControlled.set(e.sector.planet.sectors.count(Sector::hasBase));
        }
    });

    Events.on(PayloadDropEvent.class, e -> {
        if(!campaign() || e.unit == null) return;
        if(e.carrier.team == state.rules.defaultTeam && state.rules.waveTeam.cores().contains(c -> c.within(e.unit, state.rules.enemyCoreBuildRadius))) dropUnitsCoreZone.complete();
    });

    Events.on(ClientChatEvent.class, e -> { if(e.message.contains(Iconc.alphaaaa + "")) useAnimdustryEmoji.complete(); });

    Events.on(UnlockEvent.class, e -> {
        if(e.content == Items.thorium) obtainThorium.complete();
        if(e.content == Items.titanium) obtainTitanium.complete();
    });

    Events.on(WinEvent.class, e -> { if(state.rules.pvp) SStat.pvpsWon.add(); });

    Events.on(PickupEvent.class, e -> { /* some pickups already handled above; keep other pickup-related handlers here as needed */ });
}
```
```java
private void checkUnlocks(){
    if(Blocks.router.unlocked()) researchRouter.complete();
    if(!TechTree.all.contains(t -> t.content.locked())) researchAll.complete();
    if(Blocks.microProcessor.unlocked()) researchLogic.complete();
}
```

### Refactured, smaller `registerEvents()`

```java
private void registerEvents(){
    // initialize caches / persisted sets (kept from original)
    allTransportSerpulo = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
    allTransportErekir = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.erekir) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);

    allSerpuloBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
    allErekirBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.erekir) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);

    unitsBuilt = Core.settings.getJson("units-built" , ObjectSet.class, String.class, ObjectSet::new);
    blocksBuilt = Core.settings.getJson("blocks-built" , ObjectSet.class, String.class, ObjectSet::new);
    t5s = ObjectSet.with(UnitTypes.omura, UnitTypes.reign, UnitTypes.toxopid, UnitTypes.eclipse, UnitTypes.oct, UnitTypes.corvus);

    checkAllBlocks(allBlocksErekir, allErekirBlocks);
    checkAllBlocks(allBlocksSerpulo, allSerpuloBlocks);

    Timer.schedule(this::checkUpdate, 2f, 2f);

    // small immediate checks kept here
    if(Items.thorium.unlocked()) obtainThorium.complete();
    if(Items.titanium.unlocked()) obtainTitanium.complete();
    if(SectorPresets.origin.sector.isCaptured()) completeErekir.complete();
    if(SectorPresets.planetaryTerminal.sector.isCaptured()) completeSerpulo.complete();
    if(mods != null && mods.list().size > 0) installMod.complete();
    if(Core.bundle.get("yes").equals("router")) routerLanguage.complete();
    if(!Planets.serpulo.sectors.contains(s -> !s.isCaptured())) captureAllSectors.complete();

    // delegate grouped registrations
    registerBasicTriggers();
    registerUnitHandlers();
    registerBuildHandlers();
    registerPeriodicRuns();
    registerMiscHandlers();

    // check unlocks on load and bind to events
    Runnable checkUnlocks = this::checkUnlocks;
    Events.on(ResearchEvent.class, e -> checkUnlocks.run());
    Events.on(UnlockEvent.class, e -> checkUnlocks.run());
    checkUnlocks.run();
}
```

## From 439 to 38 lines!




