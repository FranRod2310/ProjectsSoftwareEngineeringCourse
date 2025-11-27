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
            float[] closestPosToCore = getClosestPosToCore();
            Seq<Point2> points = Placement.pathfindLine(true, (int)(closestPosToCore[0]), (int)(closestPosToCore[1]), (int)(closestPosToCore[2]), (int)(closestPosToCore[3]));
            float width = 0;
            float height = 0;
            Point2 point = null;
            if (points.size > 0)
                point = points.get(0);
            for(int i = 0; i < points.size - 1; i++) {
                Point2 next = points.get(i + 1);
                if (point.y == next.y) {
                    width += 1;
                }
                else if (point.x == next.x) {
                    height += 1;
                } else {
                    point = next;
                }
            }
            if (width == 0 && height > 0)
                width = tilesize;
            else if (height == 0 && width> tilesize)
                height = tilesize;
            Drawf.dashRect(Color.gold, closestPosToCore[0], closestPosToCore[1], width, height);
            /*float xDiff = closestPosToCore[2] - closestPosToCore[0];
            float yDiff = closestPosToCore[3] - closestPosToCore[1];
            Drawf.dashRect(Color.gold, closestPosToCore[0], closestPosToCore[1], xDiff, tilesize);
            Drawf.dashRect(Color.gold, closestPosToCore[0] + xDiff, closestPosToCore[1], tilesize, yDiff);*/
        };
    }


    private float[] getClosestPosToCore() {
        CoreBlock.CoreBuild core = Vars.player.team().core();
        float xCore = core.x;
        float yCore = core.y;
        float xCop = closestOre.worldx();
        float yCop = closestOre.worldy();
        float halfXToAdd = 0.5f * tilesize;
        float halfYToAdd = 0.5f * tilesize;
        if (xCore <= xCop) {
            xCore += Blocks.tutorialCoreShard.size * tilesize * 0.5f;
        } else {
            xCore -= Blocks.tutorialCoreShard.size * tilesize * 0.5f;
        }
        if (yCore <= yCop) {
            yCore += Blocks.tutorialCoreShard.size * tilesize * 0.5f + tilesize * 0.5f;
        } else {
            yCore -= Blocks.tutorialCoreShard.size * tilesize * 0.5f + tilesize * 0.5f;
        }

        float drillSize = Blocks.tutorialMechanicalDrill.size;
        float[] closestCoor = {xCop, yCop, xCore, yCore};
        float bestDistance = Mathf.dst2(xCore, yCore, closestCoor[0], closestCoor[1]);
        for (float i = 0; i < drillSize * tilesize + tilesize; i+= tilesize) {
            for (int j = 0; j < drillSize * tilesize + tilesize; j++) {
                Tile tile = Vars.world.tileWorld(xCop - tilesize + (i * tilesize), yCop - tilesize + (j * tilesize));
                if (tile == null || tile.block() != Blocks.tutorialMechanicalDrill) continue;
                float currentDistance = Mathf.dst2(xCore, yCore, xCop - tilesize + (i * tilesize), yCop - tilesize + (j * tilesize));
                if (currentDistance < bestDistance) {
                    closestCoor[0] = xCop - tilesize + (i * tilesize);
                    closestCoor[1] = yCop - tilesize + (j * tilesize);
                }
            }
        }
        closestCoor[0] += halfXToAdd + tilesize;
        closestCoor[1] += halfYToAdd;
        closestCoor[2] += tilesize;

        return closestCoor;
    }

    /**
     * Check if the conveyer belt is built correctly.
     */
    private boolean checkConveyerBuilt() {
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
