package mindustry.ui;
import mindustry.Vars;

public class TutorialDefenseState implements TutorialState {
    private Tutorial context;

    @Override
    public void enter() {
        Vars.ui.announce("Objective: Defend your base against incoming enemies");
    }

    @Override
    public void update() {
        //when finished call:
        //context.nextState();
    }

    @Override
    public void exit() {

    }

    @Override
    public void setContext(Tutorial context) {
        this.context = context;
    }

}
