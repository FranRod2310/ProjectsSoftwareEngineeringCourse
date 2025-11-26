package mindustry.ui;

import arc.Events;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;

public class TutorialBuildingState implements TutorialState {

    private Tutorial context;
    private Boolean active = false;


    public TutorialBuildingState(Tutorial context) {
        this.context = context;
    }

    @Override
    public void enter() {
        active = true;
        Vars.ui.announce("Objective: Build a Mechanical Drill");

        // get tile with copper ore
        Tile closestOre = Vars.indexer.findClosestOre(Vars.player.x, Vars.player.y, Items.copper);

        Events.run(EventType.Trigger.draw, () -> {
            if (active && closestOre != null) {
                // draw a pulsing square around the target
                Drawf.square(closestOre.worldx(), closestOre.worldy(),
                        tilesize * 1.5f + Mathf.absin(10f, 2f), Pal.accent);

                // draw an arrow pointing down to it
                Drawf.arrow(closestOre.worldx(), closestOre.worldy() + 40f,
                        closestOre.worldx(), closestOre.worldy() + 8f,
                        tilesize + Mathf.absin(10f, 2f), 4f, Pal.accent);
            }
        });
    }



    @Override
    public void update() {
        context.nextState();
    }

    @Override
    public void exit() {

    }
}
