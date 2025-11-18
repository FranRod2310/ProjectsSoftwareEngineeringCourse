package mindustry.ui;

import arc.Core;
import mindustry.Vars;
import mindustry.content.SectorPresets;
import mindustry.game.Rules;
import mindustry.type.Sector;

public class TutorialWelcomeState implements TutorialState {
    private Tutorial context;

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }

    @Override
    public void enter() {


        // join tutorial map as a custom game, makes sure map is empty when loading
        Vars.control.playMap(Vars.maps.loadInternalMap("groundZero"), new Rules());

        // welcome msg
        Core.app.post(() -> {
            Vars.ui.showInfo("Welcome to the Tutorial!\n\nClick 'OK' to begin.");
        });

    }

    @Override
    public void update() {}

    @Override
    public void exit() {}
}
