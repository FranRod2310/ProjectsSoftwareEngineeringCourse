# Code Smell 3

## Feature Envy

`...\core\src\mindustry\game\FogControl`

### Code Snippet
```java
private void registerTileChangeEvent() {
    Events.on(TileChangeEvent.class, event -> {
        if (state.rules.fog
                && event.tile.build != null
                && event.tile.isCenter()
                && !event.tile.build.team.isOnlyAI()
                && event.tile.block().flags.contains(BlockFlag.hasFogRadius)) {

            var data = data(event.tile.team());
            if (data != null) {
                data.dynamicUpdated = true;
            }

            if (state.rules.staticFog) {
                synchronized (staticEvents) {
                    pushEvent(FogEvent.get(
                        event.tile.x,
                        event.tile.y,
                        Mathf.round(event.tile.build.fogRadius()),
                        event.tile.build.team.id
                    ), false);
                }
            }
        }
    });
}
```

### Rationale

This section of code demonstrates a *Feature Envy* code smell.
The method shows an excessive interest in the data and behavior of foreign classes, especially the Class `Tile`.
It frequently accesses properties and methods like `event.tile.build`, `event.tile.block()`, `event.tile.isCenter()`, and `event.tile.build.team.isOnlyAI()`.
Instead of the logic being encapsulated inside those classes, `FogControl` is directly manipulating their data.

### Suggested Refactoring

```java
// In Tile.java
public boolean hasFogAffectingBuild() {
return build != null
&& isCenter()
&& !build.team.isOnlyAI()
&& block().flags.contains(BlockFlag.hasFogRadius);
}
```
Then, simplify the original method in `FogControl`:
```java
private void registerTileChangeEvent() {
    Events.on(TileChangeEvent.class, event -> {
        if (state.rules.fog && event.tile.hasFogAffectingBuild()) {

            var data = data(event.tile.team());
            if (data != null) data.dynamicUpdated = true;

            if (state.rules.staticFog) {
                synchronized (staticEvents) {
                    pushEvent(FogEvent.get(
                        event.tile.x,
                        event.tile.y,
                        Mathf.round(event.tile.build.fogRadius()),
                        event.tile.build.team.id
                    ), false);
                }
            }
        }
    });
}
```
This refactor improves **encapsulation and cohesion**, ensuring that each class is responsible for its own data and logic.
As a result, `FogControl` becomes simpler and less dependent on the internal structure of other classes.
