package mindustry.ui;

import arc.Core;
import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.SectorPresets;
import mindustry.content.TechTree;
import mindustry.game.MapObjectives;
import mindustry.game.Rules;
import mindustry.type.ItemStack;
import mindustry.type.Sector;
import mindustry.ui.dialogs.FullTextDialog;
import mindustry.ui.dialogs.ResearchDialog;

public class TutorialWelcomeState implements TutorialState {
    private Tutorial context;
    private boolean rulesApplied = false;

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }


    /**
     * we have to use playSector, playMap creates a custom game which messes with the tutorial
     */
    @Override
    public void enter() {
        Core.app.post(() -> {
            Vars.ui.showConfirm("Welcome to the Tutorial!\n\nClick 'OK' to begin.", () -> {

                Sector sector = SectorPresets.tutorial.sector;

                // reset save data for tutorial
                // otherwise might load groundZero progress from main game
                /*if (sector.save != null) {
                    sector.save.delete();
                    sector.save = null;
                }
                Rules rules = new Rules();
                //no starting resources
                rules.loadout = ItemStack.list();
                Vars.control.playMap(Vars.maps.loadInternalMap("groundZero"), rules);
                // lock research - list of blocks to unlock in tutorial
                // don't unlock the whole tree, since it deletes game progress from other saves
                // instead, just unlock the blocks we need for the tutorial since they'll be unlocked by the end anyway
                Blocks.mechanicalDrill.clearUnlock();
                Blocks.duo.clearUnlock();
                Blocks.copperWall.clearUnlock();*/
                Rules rules = new Rules();
                //no starting resources
                rules.loadout = ItemStack.list();
                Blocks.mechanicalDrill.clearUnlock();
                Blocks.duo.clearUnlock();
                Blocks.copperWall.clearUnlock();
                Vars.control.playSector(sector);

                //material and research reset
            });
        });
    }

    @Override
    public void update() {
        // wait for tutorial start
        if (Vars.state.isGame()) {

            // custom rules for tutorial:
            if (!rulesApplied) {

                // remove wave timer
                Vars.state.rules.waveTimer = false;

                // remove enemy spawning
                Vars.state.rules.waves = false;

                // remove original tutorial objectives in groundZero ONLY for tutorial mode
                Vars.state.rules.objectives = new MapObjectives();
            }

            // wait for any info box to close
            //if (!Core.scene.hasDialog()) {
                context.changeState(new TutorialBuildingState());
            //}
        }
    }

    @Override
    public void exit() {}
}
