package mindustry.ui;

import arc.Core;
import arc.util.Align;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.game.MapObjectives;
import mindustry.net.WorldReloader;
import mindustry.type.Sector;

import java.util.List;

public class TutorialBasicState implements TutorialState {
    private boolean rulesApplied = false;
    private Tutorial context;
    private static final int REQUIRED_COPPER = 85;

    /**
     * we have to use playSector, playMap creates a custom game which messes with the tutorial
     */
    @Override
    public void enter() {
        Vars.ui.announce("Objective: collect " + REQUIRED_COPPER + " copper", 10);
        //TODO
        //sector captured when entered
        //add more copper
        //see techtree 67
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
    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }
}
