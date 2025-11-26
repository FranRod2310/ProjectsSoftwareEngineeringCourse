package mindustry.ui;

import arc.Core;
import arc.Events;
import mindustry.Vars;
import mindustry.content.SectorPresets;
import mindustry.content.TechTree;
import mindustry.content.TutorialTechTree;
import mindustry.game.EventType;
import mindustry.net.WorldReloader;
import mindustry.type.Sector;

import java.util.List;

public class Tutorial {

    //tutorial order
    private final TutorialState[] state = {new TutorialBasicState(this), new TutorialResearchState(this),new TutorialBuildingState(this), new TutorialDefenseState(this)};
    private static int currentStateIndex;
    private static TutorialState currentState;
    protected static boolean isPlayingTutorial = false;

    // run logic every frame
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (currentState != null) {
                currentState.update();
            }
        });
    }

    public static boolean isPlayingTutorial() {
        return isPlayingTutorial;
    }

    public static void exitTutorial() {
        isPlayingTutorial = false;
        currentStateIndex = 0;
        currentState = null;
    }

    public void enterTutorial() {
        Core.app.post(() -> {
            Vars.ui.showConfirm("Welcome to the Tutorial!\n\nClick 'OK' to begin.", () -> {
                isPlayingTutorial = true;
                currentStateIndex = 0;
                nextState();
                //initialize tutorial
                Sector sector = SectorPresets.tutorial.sector;

                Vars.control.playNewSector(sector, sector, new WorldReloader());
                //remove all tech except core shard
                List<TechTree.TechNode> techNodes = TutorialTechTree.getNodes();
                for (int i = 0; i < techNodes.size() - 1; i++) {
                    TechTree.TechNode node = techNodes.get(i);
                    node.content.clearUnlock();
                    for (int j = 0; j < node.finishedRequirements.length; j++) {
                        node.finishedRequirements[j].amount = 0;
                    }
                }
            });
        });
    }

    public TutorialState getCurrentState() {
        return currentState;
    }

    public void nextState (){
        if (currentState != null)
            currentState.exit();
        if (currentStateIndex >= state.length) {
            exitTutorial();
            return;
        }
        currentState = state[currentStateIndex++];
        currentState.enter();
    }

}
