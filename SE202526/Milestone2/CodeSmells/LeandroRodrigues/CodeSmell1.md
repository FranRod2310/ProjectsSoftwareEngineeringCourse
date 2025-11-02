# Code Smell 1 - Long Method


`...\core\src\mindustry\game\Universe`

### Code Snippet
```java
public void runTurn(){
    turn++;

    int newSecondsPassed = (int)(turnDuration / 60);
    Planet current = state.getPlanet();

    //update relevant sectors
    for(Planet planet : content.planets()){

        //planets with different wave simulation status are not updated
        if(current != null && current.allowWaveSimulation != planet.allowWaveSimulation){
            continue;
        }

        //don't simulate the planet if there is an in-progress mission on that planet
        if(!planet.allowWaveSimulation && planet.sectors.contains(s -> s.hasBase() && !s.isBeingPlayed() && s.isAttacked())){
            continue;
        }

        if(planet.campaignRules.legacyLaunchPads){
            //first pass: clear import stats
            for(Sector sector : planet.sectors){
                if(sector.hasBase() && !sector.isBeingPlayed()){
                    sector.info.lastImported.clear();
                }
            }

            //second pass: update export & import statistics
            for(Sector sector : planet.sectors){
                if(sector.hasBase() && !sector.isBeingPlayed()){

                    //export to another sector
                    if(sector.info.destination != null){
                        Sector to = sector.info.destination;
                        if(to.hasBase() && to.planet == planet){
                            ItemSeq items = new ItemSeq();
                            //calculated exported items to this sector
                            sector.info.export.each((item, stat) -> items.add(item, (int)(stat.mean * newSecondsPassed * sector.getProductionScale())));
                            to.addItems(items);
                            to.info.lastImported.add(items);
                        }
                    }
                }
            }
        }

        //third pass: everything else
        for(Sector sector : planet.sectors){
            if(sector.hasBase()){
                if(sector.info.importRateCache != null){
                    sector.info.refreshImportRates(planet);
                }

                //if it is being attacked, capture time is 0; otherwise, increment the timer
                if(sector.isAttacked()){
                    sector.info.minutesCaptured = 0;
                }else{
                    sector.info.minutesCaptured += turnDuration / 60 / 60;
                }

                //increment seconds passed for this sector by the time that just passed with this turn
                if(!sector.isBeingPlayed()){

                    //TODO: if a planet has sectors under attack and simulation is OFF, just don't simulate it

                    //increment time if attacked
                    if(sector.isAttacked()){
                        sector.info.secondsPassed += turnDuration/60f;
                    }

                    int wavesPassed = (int)(sector.info.secondsPassed*60f / sector.info.waveSpacing);
                    boolean attacked = sector.info.waves && sector.planet.allowWaveSimulation;

                    if(attacked){
                        sector.info.wavesPassed = wavesPassed;
                    }

                    float damage = attacked ? SectorDamage.getDamage(sector) : 0f;

                    //damage never goes down until the player visits the sector, so use max
                    sector.info.damage = Math.max(sector.info.damage, damage);

                    //check if the sector has been attacked too many times...
                    if(attacked && damage >= 0.999f){
                        //fire event for losing the sector
                        Events.fire(new SectorLoseEvent(sector));

                        //sector is dead.
                        sector.info.items.clear();
                        sector.info.damage = 1f;
                        sector.info.hasCore = false;
                        sector.info.production.clear();
                    }else if(attacked && wavesPassed > 0 && sector.info.winWave > 1 && sector.info.wave + wavesPassed >= sector.info.winWave && !sector.hasEnemyBase()){
                        //autocapture the sector
                        sector.info.waves = false;
                        boolean was = sector.info.wasCaptured;
                        sector.info.wasCaptured = true;

                        //fire the event
                        Events.fire(new SectorCaptureEvent(sector, !was));
                    }

                    float scl = sector.getProductionScale();

                    //add production, making sure that it's capped
                    sector.info.production.each((item, stat) -> sector.info.items.add(item, Math.min((int)(stat.mean * newSecondsPassed * scl), sector.info.storageCapacity - sector.info.items.get(item))));

                    if(planet.campaignRules.legacyLaunchPads){
                        sector.info.export.each((item, stat) -> {
                            if(sector.info.items.get(item) <= 0 && sector.info.production.get(item, ExportStat::new).mean < 0 && stat.mean > 0){
                                //cap export by import when production is negative.
                                //TODO remove
                                stat.mean = Math.min(sector.info.lastImported.get(item) / (float)newSecondsPassed, stat.mean);
                            }
                        });
                    }

                    //prevent negative values with unloaders
                    sector.info.items.checkNegative();

                    sector.saveInfo();
                }

                //queue random invasions
                if(!sector.isAttacked() && sector.planet.campaignRules.sectorInvasion && sector.info.minutesCaptured > invasionGracePeriod && sector.info.hasSpawns){
                    int count = sector.near().count(s -> s.hasEnemyBase() && !s.hasBase() && (s.preset == null || !s.preset.requireUnlock));

                    //invasion chance depends on # of nearby bases
                    if(count > 0 && Mathf.chance(baseInvasionChance * (0.8f + (count - 1) * 0.3f))){
                        int waveMax = Math.max(sector.info.winWave, sector.isBeingPlayed() ? state.wave : sector.info.wave + sector.info.wavesPassed) + Mathf.random(2, 4) * 5;

                        //assign invasion-related things
                        if(sector.isBeingPlayed()){
                            state.rules.winWave = waveMax;
                            state.rules.waves = true;
                            state.rules.attackMode = false;
                            planet.campaignRules.apply(planet, state.rules); //enabling waves may force changes in campaign rules
                            //update rules in multiplayer
                            if(net.server()){
                                Call.setRules(state.rules);
                            }
                        }else{
                            sector.info.winWave = waveMax;
                            sector.info.waves = true;
                            sector.info.attack = false;
                            sector.saveInfo();
                        }

                        Events.fire(new SectorInvasionEvent(sector));
                    }
                }
            }
        }
    }

    Events.fire(new TurnEvent());

    save();
}
```
### Rationale

The runTurn() method from `Universe` class, suffers from a Long Method code smell, as it handles
multiple unrelated tasks — such as planet iteration, sector updates, exports,
and invasions — within a single block. This makes the code hard to read, test,
and maintain. To fix this, the method was refactored into smaller, focused helper
methods, each with a clear responsibility, improving readability, modularity, and maintainability.

### Suggested Refactoring
Refactor the runTurn() method by extracting smaller, well-named private helper methods for each logical task (e.g., planet simulation, sector updates, export/import handling, invasion checks). This modularization reduces method complexity, improves readability, and follows the Single Responsibility Principle, making the code easier to maintain and extend.

```java
   /** Runs possible events. Resets event counter. */
public void runTurn(){
    turn++;

    int newSecondsPassed = (int)(turnDuration / 60);
    Planet current = state.getPlanet();

    //update relevant sectors
    for(Planet planet : content.planets()){

        if(!shouldSimulatePlanet(planet, current)) continue;

        if(planet.campaignRules.legacyLaunchPads){
            clearImportStats(planet);
            updateExports(planet, newSecondsPassed);
        }

        updatePlanetSectors(planet, newSecondsPassed);
    }

    Events.fire(new TurnEvent());
    save();
}

// ---------- Métodos auxiliares ---------- //

private boolean shouldSimulatePlanet(Planet planet, Planet current){
    if(current != null && current.allowWaveSimulation != planet.allowWaveSimulation) return false;
    if(!planet.allowWaveSimulation && planet.sectors.contains(s -> s.hasBase() && !s.isBeingPlayed() && s.isAttacked()))
        return false;
    return true;
}

private void clearImportStats(Planet planet){
    //first pass: clear import stats
    for(Sector sector : planet.sectors){
        if(sector.hasBase() && !sector.isBeingPlayed()){
            sector.info.lastImported.clear();
        }
    }
}

private void updateExports(Planet planet, int newSecondsPassed){
    for(Sector sector : planet.sectors){
        if(sector.hasBase() && !sector.isBeingPlayed()){

            //export to another sector
            if(sector.info.destination != null){
                Sector to = sector.info.destination;
                if(to.hasBase() && to.planet == planet){
                    ItemSeq items = new ItemSeq();
                    //calculated exported items to this sector
                    sector.info.export.each((item, stat) -> items.add(item, (int)(stat.mean * newSecondsPassed * sector.getProductionScale())));
                    to.addItems(items);
                    to.info.lastImported.add(items);
                }
            }
        }
    }
}

private void updatePlanetSectors(Planet planet, int newSecondsPassed){
    for(Sector sector : planet.sectors) {
        if (sector.hasBase()) {
            if (sector.info.importRateCache != null) {
                sector.info.refreshImportRates(planet);
            }

            updateSectorCaptureStatus(sector);
            updateSectorSimulation(planet, sector, newSecondsPassed);
            maybeQueueInvasion(sector);
        }
    }
}

private void updateSectorCaptureStatus(Sector sector){
    if(sector.isAttacked()){
        sector.info.minutesCaptured = 0;
    }else{
        sector.info.minutesCaptured += turnDuration / 60 / 60;
    }
}

private void updateSectorSimulation(Planet planet, Sector sector, int newSecondsPassed){
    if(sector.isBeingPlayed()) return;

    if(sector.isAttacked()){
        sector.info.secondsPassed += turnDuration / 60f;
    }

    int wavesPassed = (int)(sector.info.secondsPassed * 60f / sector.info.waveSpacing);
    boolean attacked = sector.info.waves && sector.planet.allowWaveSimulation;

    if(attacked){
        sector.info.wavesPassed = wavesPassed;
    }

    float damage = attacked ? SectorDamage.getDamage(sector) : 0f;
    sector.info.damage = Math.max(sector.info.damage, damage);

    handleSectorOutcome(sector, attacked, damage, wavesPassed);
    handleProductionAndExports(planet, sector, newSecondsPassed);
    sector.saveInfo();
}

private void handleSectorOutcome(Sector sector, boolean attacked, float damage, int wavesPassed){
    if(attacked && damage >= 0.999f){
        Events.fire(new SectorLoseEvent(sector));
        sector.info.items.clear();
        sector.info.damage = 1f;
        sector.info.hasCore = false;
        sector.info.production.clear();
    }else if(attacked && wavesPassed > 0 && sector.info.winWave > 1 &&
            sector.info.wave + wavesPassed >= sector.info.winWave && !sector.hasEnemyBase()){
        sector.info.waves = false;
        boolean was = sector.info.wasCaptured;
        sector.info.wasCaptured = true;
        Events.fire(new SectorCaptureEvent(sector, !was));
    }
}

private void handleProductionAndExports(Planet planet, Sector sector, int newSecondsPassed){
    float scl = sector.getProductionScale();
    sector.info.production.each((item, stat) ->
            sector.info.items.add(item, Math.min(
                    (int)(stat.mean * newSecondsPassed * scl),
                    sector.info.storageCapacity - sector.info.items.get(item))
            )
    );

    if(planet.campaignRules.legacyLaunchPads){
        sector.info.export.each((item, stat) -> {
            if(sector.info.items.get(item) <= 0 &&
                    sector.info.production.get(item, ExportStat::new).mean < 0 &&
                    stat.mean > 0){
                stat.mean = Math.min(
                        sector.info.lastImported.get(item) / (float)newSecondsPassed,
                        stat.mean
                );
            }
        });
    }

    sector.info.items.checkNegative();
}

private void maybeQueueInvasion(Sector sector){
    if(!sector.isAttacked() && sector.planet.campaignRules.sectorInvasion &&
            sector.info.minutesCaptured > invasionGracePeriod && sector.info.hasSpawns){

        int count = sector.near().count(s -> s.hasEnemyBase() && !s.hasBase() &&
                (s.preset == null || !s.preset.requireUnlock));

        if(count > 0 && Mathf.chance(baseInvasionChance * (0.8f + (count - 1) * 0.3f))){
            queueInvasion(sector);
        }
    }
}

private void queueInvasion(Sector sector){
    int waveMax = Math.max(sector.info.winWave, sector.isBeingPlayed() ? state.wave : sector.info.wave + sector.info.wavesPassed)
            + Mathf.random(2, 4) * 5;

    if(sector.isBeingPlayed()){
        state.rules.winWave = waveMax;
        state.rules.waves = true;
        state.rules.attackMode = false;
        sector.planet.campaignRules.apply(sector.planet, state.rules);
        if(net.server()){
            Call.setRules(state.rules);
        }
    }else{
        sector.info.winWave = waveMax;
        sector.info.waves = true;
        sector.info.attack = false;
        sector.saveInfo();
    }

    Events.fire(new SectorInvasionEvent(sector));
}
```