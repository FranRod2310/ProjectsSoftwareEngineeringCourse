package mindustry.ui;


import mindustry.Vars;


public class TutorialBuildingState implements TutorialState {

    private Tutorial context;
    private boolean drillBuilt;
    private boolean convBuilt;

    @Override
    public void enter() {
        Vars.ui.announce("Objective: Build a Mechanical Drill");
        drillBuilt = false;
        convBuilt = false;
        // get tile with copper ore

    }



    @Override
    public void update() {
        if(drillBuilt && convBuilt)
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
