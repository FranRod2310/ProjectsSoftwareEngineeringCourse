package mindustry.ui;

import arc.Events;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.actions.RelativeTemporalAction;
import arc.scene.ui.layout.Scl;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.content.TutorialTechTree;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.ResearchDialog;
import mindustry.world.Tile;

import java.util.List;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.ui;


public class TutorialResearchState implements TutorialState {
    private Tutorial context;
    private boolean test = true;
    private Cons<EventType.DrawEvent> drawConsumer;
    private static final String INSTRUCTIONS =
            "Great job collecting copper! Now, let's move on to research.\n\n" +
                    "Research allows you to unlock new technologies and buildings that are essential for your progress in the game.\n\n" +
                    "To begin researching, open the research menu and select a technology to unlock.\n\n" +
                    "Once you've researched the 4 available items, you'll be ready to advance further in Mindustry!\n\n" +
                    "Good luck!";


    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
    }

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

    @Override
    public void exit() {

    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
