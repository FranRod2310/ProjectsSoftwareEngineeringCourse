# Design Pattern #1 - Memento

`core/src/mindustry/game/Saves.SaveSlot`

## Code snippet:

```java
public class SaveSlot {
    public Fi file;
    public SaveMeta meta;

    (...)

    public void save() {
        long prev = totalPlaytime;
        SaveIO.save(file);                  // Capture current world state to file
        meta = SaveIO.getMeta(file);        // Store snapshot metadata
        totalPlaytime = prev;
        savePreview();
    }

    (...)

    public void load(WorldContext context) throws SaveException {
        SaveIO.load(file, context);         // Restore world state from file
        meta = SaveIO.getMeta(file);        // Restore snapshot metadata
        current = this;
        totalPlaytime = meta.timePlayed;
    }
}
```

## Rationale

The `Saves` class captures and stores the current info and state of the game without requiring any other component to access or manipulate that state directly.
This is a clear use of the **Memento** design pattern. It includes the following roles:

**Originator**:

Can produce snapshots of its own state, as well as restore its state from snapshots when needed.
Here, the originator is the game world, represented by classes like `World`, `Sector`, `GameState` and `Rules`.

**Memento**:

A value object that acts as a snapshot of the originator’s state. 
Here, the memento is `SaveSlot`.

**Caretaker**:

Knows not only “when” and “why” to capture the originator’s state, but also when the state should be restored.
Here, the caretaker is `Saves`.

<img width="2137" height="1192" alt="image" src="https://github.com/user-attachments/assets/36377583-ef85-4a9d-b940-a641b9baf14d" />

**Advantages**:
- Preserves encapsulation of the object’s state.
- Makes undo or restore operations easy to implement.
- Separates state storage from state management logic.


