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
    private static final TutorialState[] state = {new TutorialBasicState(), new TutorialResearchState(),new TutorialBuildingState(), new TutorialDefenseState()};
    private static int currentStateIndex;
    private static TutorialState currentState;
    protected static boolean isPlayingTutorial = false;

    /**
     * Runs every frame, calls current state update
     */
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (currentState != null) {
                currentState.update();
            }
        });
    }

    /**
     * @return true if playing tutorial
     */
    public static boolean isPlayingTutorial() {
        return isPlayingTutorial;
    }

    public static void exitTutorial() {
        if (currentState != null)
            currentState.exit();
        isPlayingTutorial = false;
        currentStateIndex = 0;
        currentState = null;
    }

    /**
     * Start the tutorial, show initial message, initialize the world and reset techtree
     */
    public void enterTutorial() {
        Core.app.post(() -> {
            Vars.ui.showConfirm("Welcome to the Tutorial!\n\nClick 'OK' to begin.", () -> {
                isPlayingTutorial = true;
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
                for (TutorialState tutorialState : state)
                    tutorialState.setContext(this);
            });
        });
    }

    /**
     * @return current state
     */
    public TutorialState getCurrentState() {
        return currentState;
    }

    /**
     * @return true if in research state
     */
    public static boolean isResearchTutorial(){
        return currentState instanceof TutorialResearchState;
    }

    /**
     * Start tutorial only when landed
     */
    public static void arrivedAtSector(){
        currentStateIndex = 0;
        nextState();
    }

    /**
     * Call the next state
     */
    public static void nextState (){
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
