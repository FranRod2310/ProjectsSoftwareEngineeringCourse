package mindustry.ui;

import mindustry.Vars;

public class TutorialResearchState implements TutorialState {
    private Tutorial context;



    @Override
    public void enter() {
        Vars.ui.announce("Objective: Research the Mechanical Drill");
    }

    @Override
    public void update() {
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
