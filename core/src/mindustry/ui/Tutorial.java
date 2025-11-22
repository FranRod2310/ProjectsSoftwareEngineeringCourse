package mindustry.ui;

import arc.Events;
import mindustry.game.EventType;

public class Tutorial {

    private TutorialState currentState;
    protected static boolean isPlayingTutorial = false;

    // run logic every frame
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (currentState != null) {
                currentState.update();
            }
        });
    }

    public static boolean isPlayingTutorial() {
        return isPlayingTutorial;
    }

    public static void exitTutorial() {
        isPlayingTutorial = false;
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
