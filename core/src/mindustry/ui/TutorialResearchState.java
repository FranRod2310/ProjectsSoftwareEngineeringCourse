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



    @Override
    public void enter() {
        Vars.ui.announce(INSTRUCTIONS, 15);
    }

    @Override
    public void update() {
        List<TechTree.TechNode> techNodes = TutorialTechTree.getNodes();
        for (int i = 0; i < techNodes.size() - 1; i++) {
            TechTree.TechNode node = techNodes.get(i);
            if (!node.content.unlocked()) {
                return;
            }
        }
        context.nextState();
    }

    @Override
    public void exit() {

    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
