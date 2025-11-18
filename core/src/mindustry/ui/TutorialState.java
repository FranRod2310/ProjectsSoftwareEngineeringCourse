package mindustry.ui;

public interface TutorialState {

    void setContext(Tutorial context);

    // state begins
    void enter();

    // runs 60 times per second
    void update();

    // state ends
    void exit();
}
