package mindustry.ui;

import arc.Core;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.game.MapObjectives;
import mindustry.net.WorldReloader;
import mindustry.type.Sector;

import java.util.List;

public class TutorialBasicState implements TutorialState {
    private boolean rulesApplied = false;
    private Tutorial context;

    public TutorialBasicState(Tutorial context) {
        this.context = context;
    }

    /**
     * we have to use playSector, playMap creates a custom game which messes with the tutorial
     */
    @Override
    public void enter() {

        //TODO
        //sector captured when entered
        //add more copper
        //see techtree 67



    }

    private boolean hasEnoughCopper() {
        return Vars.player.team().core().items.get(Items.copper) >= 85;
    }

    @Override
    public void update() {
      // if (hasEnoughCopper())
           context.nextState();
    }

    @Override
    public void exit() {
    }

}
