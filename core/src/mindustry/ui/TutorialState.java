package mindustry.ui;

public interface TutorialState {

    /**
     * Initializes the tutorial state and displays instructions.
     */
    void enter();

    /**
     * Updates the tutorial state logic.
     */
    void update();

    /**
     * Cleans up any event listeners or resources.
     */
    void exit();

    /**
     * Sets the tutorial context.
     * @param context The tutorial context.
     */
    void setContext(Tutorial context);
}
