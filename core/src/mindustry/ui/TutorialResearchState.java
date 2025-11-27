package mindustry.ui;

import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.content.TutorialTechTree;
import java.util.List;

public class TutorialResearchState implements TutorialState {
    private Tutorial context;
    private static final String INSTRUCTIONS =
            "Great job collecting copper! Now, let's move on to research.\n\n" +
                    "Research allows you to unlock new technologies and buildings that are essential for your progress in the game.\n\n" +
                    "To begin researching, open the research menu and select a technology to unlock.\n\n" +
                    "Once you've researched the 4 available items, you'll be ready to advance further in Mindustry!\n\n" +
                    "Good luck!";


    /**
     * Show research instructions.
     */
    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
    }

    /**
     * Check if all required tech nodes have been researched.
     */
    @Override
    public void update() {
        List<TechTree.TechNode> nodes = TutorialTechTree.getNodes();
        for (int i = 0; i < nodes.size() - 1; i++) {
            TechTree.TechNode node = nodes.get(i);
            if (!node.content.unlocked())
                return;
        }
        context.nextState();
    }

    /**
     * Clean up any event listeners or resources.
     * (nothing to clean up in this state)
     */
    @Override
    public void exit() {

    }

    /**
     * Set the tutorial context.
     *
     * @param context the tutorial instance
     */
    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
