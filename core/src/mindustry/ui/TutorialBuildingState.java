package mindustry.ui;


import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.Placement;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

import static mindustry.Vars.tilesize;


public class TutorialBuildingState implements TutorialState {
    private Tile closestOre;
    private Tutorial context;
    private boolean drillBuilt;
    private boolean justBuiltDrill;
    private boolean convBuilt;
    private Cons<EventType.DrawEvent> drawDrill;
    private Cons<EventType.DrawEvent> drawConveyer;
    private int convWidth;
    private int convHeight;

    private static final String INSTRUCTIONS =
            "Great! Now that you've researched some buildings, it's time to build your first structures.\n\n" +
                    "First, build a Mechanical Drill on the highlighted copper ore tiles to start extracting copper.\n\n" +
                    "Next, construct a Conveyor Belt leading from the drill to your core to transport the extracted copper.\n\n" +
                    "Hold ctrl while building conveyors for an automatic route. If needed, use your mouse wheel to rotate segments.\n\n" +
                    "Once both buildings are constructed, you'll be ready to move on to the next stage of the tutorial!\n\n" +
                    "Good luck!";


    /**
     * Show building instructions and highlight where to build.
     */
    @Override
    public void enter() {
        Vars.ui.showInfo(INSTRUCTIONS);
        drillBuilt = false;
        justBuiltDrill = true;
        convBuilt = false;
        convHeight = tilesize;
        convWidth = tilesize;
        closestOre = Vars.indexer.findClosestOre(Vars.player.x, Vars.player.y, Items.copper);
        // Draw highlight for drill placement
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
        // Draw highlight for conveyer placement
        drawConveyer = (e) -> {
            CoreBlock.CoreBuild core = Vars.player.team().core();
            int halfTileSize = tilesize / 2;
            Seq<Point2> horPoints = Placement.pathfindLine(true, (int) (closestOre.worldx()) + halfTileSize + tilesize, (int) (closestOre.worldy()) + halfTileSize, (int) (core.x) + halfTileSize, (int) (closestOre.worldy()));
            convWidth = horPoints.size - horPoints.size%tilesize;
            //if the path is horizontal
            boolean isHorizontalPath = (Math.abs(closestOre.worldy() + halfTileSize - core.y) <= tilesize);
            if (isHorizontalPath)
                convWidth -= 2*tilesize;
            Drawf.dashRect(Color.gold, closestOre.worldx() + halfTileSize + tilesize, closestOre.worldy() + halfTileSize, convWidth, tilesize);
            if (!isHorizontalPath) {
                Seq<Point2> verPoints = Placement.pathfindLine(true, (int) (core.x), (int) (closestOre.worldy()) + halfTileSize, (int) (core.x), (int) (core.y) - halfTileSize);
                convHeight = verPoints.size - verPoints.size%tilesize - tilesize;
                Drawf.dashRect(Color.gold, core.x - halfTileSize, closestOre.worldy() + halfTileSize, tilesize, convHeight);
            }
        };
    }

    /**
     * Check if the conveyer belt is built correctly.
     */
    private boolean checkConveyerBuilt() {
        int halfTileSize = tilesize / 2;
        float x = closestOre.worldx() + 2 * tilesize;
        float lastX = x;
        float y = closestOre.worldy() + tilesize;
        for (int i = 0; i < convWidth / tilesize; i++) {
            Tile tile = Vars.world.tileWorld(x + (i * tilesize), y);
            if (tile == null || tile.block() != Blocks.tutorialConveyor) {
                return false;
            }
            lastX = x + (i * tilesize);
        }
        for (int i = 0; i < convHeight / tilesize; i++) {
            Tile tile = Vars.world.tileWorld(lastX, y + (i * tilesize));
            if (tile == null || tile.block() != Blocks.tutorialConveyor) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if both buildings are constructed to proceed.
     */
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

    /**
     * Clean up event listeners when exiting the state.
     */
    @Override
    public void exit() {
        Events.remove(EventType.DrawEvent.class, drawDrill);
        Events.remove(EventType.DrawEvent.class, drawConveyer);
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
