package mindustry.ui;

import arc.Core;
import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.core.GameState;
import mindustry.game.MapObjectives;
import mindustry.game.Objectives;
import mindustry.game.Rules;
import mindustry.net.WorldReloader;
import mindustry.type.ItemStack;
import mindustry.type.Sector;
import mindustry.ui.dialogs.FullTextDialog;
import mindustry.ui.dialogs.ResearchDialog;

import java.util.LinkedList;
import java.util.List;

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

        //TODO
        //sector captured when entered
        //add more copper
        //see techtree 67

        Tutorial.isPlayingTutorial = true;
        Core.app.post(() -> {
            Vars.ui.showConfirm("Welcome to the Tutorial!\n\nClick 'OK' to begin.", () -> {
                Sector sector = SectorPresets.tutorial.sector;

                // reset save data for tutorial
                // otherwise might load groundZero progress from main game

                // lock research - list of blocks to unlock in tutorial
                // don't unlock the whole tree, since it deletes game progress from other saves
                // instead, just unlock the blocks we need for the tutorial since they'll be unlocked by the end anyway

                //no starting resources
                Vars.control.playNewSector(sector, sector, new WorldReloader());
                List <TechTree.TechNode> techNodes = TutorialTechTree.getNodes();
                for (int i = 0; i < techNodes.size()-1; i++) {
                    TechTree.TechNode node = techNodes.get(i);
                    node.content.clearUnlock();
                    for (int j = 0; j < node.finishedRequirements.length; j++) {
                        node.finishedRequirements[j].amount = 0;
                    }
                }
            });
        });
    }

    @Override
    public void update() {
        // wait for tutorial start
        if (Tutorial.isPlayingTutorial()) {

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
                //context.changeState(new TutorialBuildingState());
            //}
        }
    }

    @Override
    public void exit() {
    }

}
