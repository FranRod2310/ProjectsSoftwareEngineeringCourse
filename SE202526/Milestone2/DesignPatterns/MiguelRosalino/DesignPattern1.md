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

**Originator**
The object whose internal state is being captured and later restored. It creates a memento representing its current state and can restore itself from one.
Here, the originator is the game world, represented by classes like `World`, `Sector` and `GameState`.

**Memento**

