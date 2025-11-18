package mindustry.ui;

import arc.Events;
import mindustry.game.EventType;

public class Tutorial {

    private TutorialState currentState;


    // run logic every frame
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (currentState != null) {
                currentState.update();
            }
        });
    }


    // initial state
    public void show() {
        changeState(new TutorialWelcomeState());
    }


    public void changeState(TutorialState newState) {
        if (currentState != null) {
            currentState.exit();
        }

        this.currentState = newState;

        if (currentState != null) {
            currentState.setContext(this);
            currentState.enter();
        }
    }

}
