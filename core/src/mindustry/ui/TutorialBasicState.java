package mindustry.ui;

import arc.Events;
import arc.func.Cons;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import static mindustry.Vars.tilesize;

public class TutorialBasicState implements TutorialState {
    private Tutorial context;
    private static final int REQUIRED_COPPER = 85;
    private Cons<EventType.DrawEvent> drawConsumer;
    private static final String INSTRUCTIONS =
            "Welcome to Mindustry! In this tutorial, you'll learn the basics of resource gathering, base building and defending.\n\n" +
            "Your first objective is to collect " + REQUIRED_COPPER + " copper to get started. Copper is a fundamental resource used for constructing buildings and crafting items.\n\n" +
            "To collect copper, you'll need to click on the highlighted area.\n" +
            "Once collected enough, you will move on to the next stage\n" +
            "Good luck, and have fun!";

    /**
     * we have to use playSector, playMap creates a custom game which messes with the tutorial
     */
    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
        drawConsumer = (e) -> {
                Tile closestOre = Vars.indexer.findClosestOre(Vars.player.x, Vars.player.y, Items.copper);
                if (closestOre != null) {
                    Drawf.square(closestOre.worldx(), closestOre.worldy(),
                            tilesize, Pal.accent);

                    Drawf.arrow(closestOre.worldx(), closestOre.worldy() + 40,
                            closestOre.worldx(), closestOre.worldy() + 8,
                            tilesize, 4, Pal.accent);
                }
        };

            Events.on(EventType.DrawEvent.class, drawConsumer);
    }

    private boolean hasEnoughCopper() {
        return Vars.player.team().core().items.get(Items.copper) >= REQUIRED_COPPER;
    }

    @Override
    public void update() {
        if (hasEnoughCopper())
           context.nextState();
    }

    @Override
    public void exit() {
        Events.remove(EventType.DrawEvent.class, drawConsumer);
    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
