# Design Pattern #3 - State

`core/src/mindustry/core/GameState`

```java
public class GameState{
(...)
  public void set(State astate){
        //nothing to change.
        if(state == astate) return;

        Events.fire(new StateChangeEvent(state, astate));
        state = astate;
    }
  public boolean isCampaign(){
        return rules.sector != null;
    }

  public boolean isEditor(){
        return rules.editor;
    }

  public boolean isPaused(){
        return state == State.paused;
    }

    /** @return whether there is an unpaused game in progress. */
  public boolean isPlaying(){
        return state == State.playing;
    }

    /** @return whether the current state is *not* the menu. */
  public boolean isGame(){
        return state != State.menu;
    }

  public boolean isMenu(){
        return state == State.menu;
    }

  public boolean is(State astate){
        return state == astate;
    }

  public State getState(){
        return state;
    }

  public enum State{
        paused, playing, menu
    }
}
```

Example usage:
`core/src/mindustry/async/AsyncCore`

```java
public class AsyncCore{
(...)
public void begin(){
    if(state.isPlaying()){
        // for sync
        for(AsyncProcess p : processes){
            p.begin();
        }
      (...)
    }
}
(...)
```

## Rationale

The State pattern allows Mindustry’s core systems to change behavior depending on the current game state (menu, playing, paused, etc.) without scattering conditional logic across the code.
`GameState` acts as the **Context**, while the nested `State enum` represents possible **States**, enabling clean and centralized state management.

<img width="1415" height="844" alt="image" src="https://github.com/user-attachments/assets/84383bad-a6bb-450b-94fa-bf718494b872" />

**Advantages:**
- Simplifies control flow by centralizing all state logic in one place (GameState).
- Makes behavior easier to manage and extend when adding new game states.


