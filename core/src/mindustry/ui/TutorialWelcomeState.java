package mindustry.ui;

import arc.Core;
import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.SectorPresets;
import mindustry.content.TechTree;
import mindustry.game.Rules;
import mindustry.type.ItemStack;
import mindustry.type.Sector;
import mindustry.ui.dialogs.FullTextDialog;
import mindustry.ui.dialogs.ResearchDialog;

public class TutorialWelcomeState implements TutorialState {
    private Tutorial context;

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }

    @Override
    public void enter() {
        // welcome msg
        Core.app.post(() -> {
            Vars.ui.showConfirm("Welcome to the Tutorial!\n\nClick 'OK' to begin.", () -> {
                // when confirmed, join tutorial map as a custom game, makes sure map is empty when loading
                Rules rules = new Rules();
                //no starting resources
                rules.loadout = ItemStack.list();
                Vars.control.playMap(Vars.maps.loadInternalMap("groundZero"), rules);
            });
        });
    }

    @Override
    public void update() {}

    @Override
    public void exit() {}
}
