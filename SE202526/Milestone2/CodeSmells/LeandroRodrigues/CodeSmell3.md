# Smell #3 - Message Chaining
`...\core\src\mindustry\service\GameService`

## Code Snippet

```java
   private void registerEvents() {
    allTransportSerpulo = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
    allTransportErekir = content.blocks().select(b -> b.category == Category.distribution && b.isOnPlanet(Planets.erekir) && b.isVanilla() && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);

    //cores are ignored since they're upgrades and can be skipped
    allSerpuloBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.serpulo) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
    allErekirBlocks = content.blocks().select(b -> b.synthetic() && b.isOnPlanet(Planets.erekir) && b.isVanilla() && !(b instanceof CoreBlock) && b.buildVisibility == BuildVisibility.shown).toArray(Block.class);
    (...)
}

```

### Rationale

In registerEvents(), long chains like content.blocks().select(...).toArray() expose internal structures of content, creating tight coupling and reducing readability. Encapsulating these calls in helper
methods simplifies the code and improves maintainability.

### Suggested Refactoring
Introduce helper methods in ContentLoader to encapsulate the selection logic:

## Update `ContentLoader`
```java
public class Content {
 (...)
    public Block[] selectBlocks(... filter){
        return blocks().select(filter).toArray(Block.class);
    }
}
(...)

```

## Update `GameService`
```java
private void registerEvents() {
    allTransportSerpulo = content.selectBlocks(b ->
            b.category == Category.distribution &&
                    b.isOnPlanet(Planets.serpulo) &&
                    b.isVanilla() &&
                    b.buildVisibility == BuildVisibility.shown
    );

    allTransportErekir = content.selectBlocks(b ->
            b.category == Category.distribution &&
                    b.isOnPlanet(Planets.erekir) &&
                    b.isVanilla() &&
                    b.buildVisibility == BuildVisibility.shown
    );

    allSerpuloBlocks = content.selectBlocks(b ->
            b.synthetic() &&
                    b.isOnPlanet(Planets.serpulo) &&
                    b.isVanilla() &&
                    !(b instanceof CoreBlock) &&
                    b.buildVisibility == BuildVisibility.shown
    );

    allErekirBlocks = content.selectBlocks(b ->
            b.synthetic() &&
                    b.isOnPlanet(Planets.erekir) &&
                    b.isVanilla() &&
                    !(b instanceof CoreBlock) &&
                    b.buildVisibility == BuildVisibility.shown
    );

    unitsBuilt = Core.settings.getJson("units-built", ObjectSet.class, String.class, ObjectSet::new);
    blocksBuilt = Core.settings.getJson("blocks-built", ObjectSet.class, String.class, ObjectSet::new);

    (...)
}

}
```
