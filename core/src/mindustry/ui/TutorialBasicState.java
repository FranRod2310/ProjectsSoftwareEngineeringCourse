package mindustry.ui;

import arc.Events;
import arc.func.Cons;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.world.Tile;
import static mindustry.Vars.tilesize;

public class TutorialBasicState implements TutorialState {
    private Tutorial context;
    private int reqCopper;
    private Cons<EventType.DrawEvent> drawConsumer;
    private Cons<EventType.ResearchEvent> researchConsumer;
    private final String INSTRUCTIONS =
            "Welcome to Mindustry! In this tutorial, you'll learn the basics of resource gathering, base building and defending.\n\n" +
                    "Your first objective is to collect " + reqCopper + " copper to get started. Copper is a fundamental resource used for constructing buildings and crafting items.\n\n" +
                    "To collect copper, you'll need to click on the highlighted area.\n" +
                    "Once collected enough, you will move on to the next stage\n" +
                    "Good luck, and have fun!";

    /**
     * Show instructions and set up drawing for copper ore location.
     */
    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
        calculateRequiredCopper();
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

    /**
     * Calculate the total required copper for all unresearched tech nodes.
     */
    private void calculateRequiredCopper() {
        int total = 0;
        for (TechTree.TechNode node : TutorialTechTree.getNodes()) {
            for (int i = 0; i < node.requirements.length; i++) {
                ItemStack stack = node.requirements[i];
                ItemStack finished = node.finishedRequirements[i];
                if (stack.item == Items.copper && !node.content.unlocked()) {
                    total += stack.amount - finished.amount;
                }
            }
        }
         reqCopper = total;
    }

    /**
     * Check if the player has enough copper in their core.
     */
    private boolean hasEnoughCopper() {
        return Vars.player.team().core().items.get(Items.copper) >= reqCopper;
    }

    /**
     * Update method called every frame.
     * Checks if the player has collected enough copper to proceed.
     */
    @Override
    public void update() {
        calculateRequiredCopper();
        if (hasEnoughCopper())
            context.nextState();
    }

    /**
     * Remove event listeners when exiting the state.
     */
    @Override
    public void exit() {
        Events.remove(EventType.DrawEvent.class, drawConsumer);
    }

    /**
     * Set the tutorial context for this state.
     *
     * @param context The tutorial context.
     */
    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
