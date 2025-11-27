package mindustry.ui;


import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;


public class TutorialBuildingState implements TutorialState {
    private Tile closestOre;
    private Tutorial context;
    private boolean drillBuilt;
    private boolean justBuiltDrill;
    private boolean convBuilt;
    private Cons<EventType.DrawEvent> drawDrill;
    private Cons<EventType.DrawEvent> drawConveyer;

    private static final String INSTRUCTIONS =
            "Great! Now that you've researched some buildings, it's time to build your first structures.\n\n" +
                    "First, build a Mechanical Drill on the highlighted copper ore tiles to start extracting copper.\n\n" +
                    "Next, construct a Conveyor Belt leading from the drill to your core to transport the extracted copper.\n\n" +
                    "Hold ctrl while building conveyors for an automatic route.\n\n" +
                    "Once both buildings are constructed, you'll be ready to move on to the next stage of the tutorial!\n\n" +
                    "Good luck!";

    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
        drillBuilt = false;
        justBuiltDrill = true;
        convBuilt = false;
        closestOre = Vars.indexer.findClosestOre(Vars.player.x, Vars.player.y, Items.copper);;
        drawDrill = (e) -> {
            if (closestOre != null) {
                Drawf.square(closestOre.worldx() + (int) (tilesize / 2), closestOre.worldy() + (int) (tilesize / 2),
                        (float) (tilesize * 1.5), 0, Pal.accent);

                Drawf.arrow(closestOre.worldx() + (int) (tilesize / 2), closestOre.worldy() + 40 + (int) (tilesize / 2),
                        closestOre.worldx() + (int) (tilesize / 2), closestOre.worldy() + 8 + (int) (tilesize / 2),
                        tilesize, 4, Pal.accent);
            }
        };
        Events.on(EventType.DrawEvent.class, drawDrill);

        drawConveyer = (e) -> {
            Drawf.dashRect(Color.gold, closestOre.worldx() + (tilesize * 1.5f), closestOre.worldy() - (tilesize * 0.5f), tilesize * 4f, tilesize);
            Drawf.dashRect(Color.gold, closestOre.worldx() + (tilesize * 4.5f), closestOre.worldy() - (tilesize * 0.5f), tilesize, tilesize * 2f);
        };
    }


    private boolean checkConveyerBuilt(){
        float x = closestOre.worldx();
        float y = closestOre.worldy();
        for (int i = 0; i < 4; i++) {
            Tile tile = Vars.world.tileWorld(x + (tilesize * 1.5f) + (i * tilesize), y);
            if (tile == null || tile.block() != Blocks.tutorialConveyor) {
                return false;
            }
        }
        for (int i = 0; i < 2; i++) {
            Tile tile = Vars.world.tileWorld(x + (tilesize * 4.5f), y + (i * tilesize));
            if (tile == null || tile.block() != Blocks.tutorialConveyor) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update() {
        if (closestOre == null) return;
        drillBuilt = closestOre.block() == Blocks.tutorialMechanicalDrill;
        convBuilt = checkConveyerBuilt();
        if (drillBuilt && justBuiltDrill) {
            justBuiltDrill = false;
            Events.remove(EventType.DrawEvent.class, drawDrill);
            Events.on(EventType.DrawEvent.class, drawConveyer);
        }

        if (drillBuilt && convBuilt)
            context.nextState();
    }

    @Override
    public void exit() {
        Events.remove(EventType.DrawEvent.class, drawDrill);
        Events.remove(EventType.DrawEvent.class, drawConveyer);
    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
