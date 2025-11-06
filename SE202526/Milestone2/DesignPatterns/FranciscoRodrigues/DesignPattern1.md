## Facade Pattern 

`core/src/mindustry/core/ContentLoader.java`

### Code Snippet

```Java
(...)

public void createBaseContent(){
        UnitCommand.loadAll();
        TeamEntries.load();
        Items.load();
        UnitStance.loadAll(); //needs to access items
        StatusEffects.load();
        Liquids.load();
        Bullets.load();
        UnitTypes.load();
        Blocks.load();
        Loadouts.load();
        Weathers.load();
        Planets.load();
        SectorPresets.load();
        SerpuloTechTree.load();
        ErekirTechTree.load();
    }

(...)
```


`tools/src/mindustry/tools/ImagePacker.java`

### Code Snippet

```Java

(...)

public class ImagePacker{
    static ObjectMap<String, PackIndex> cache = new ObjectMap<>();

    public static void main(String[] args) throws Exception{
        Vars.headless = true;
        //makes PNG loading slightly faster
        ArcNativesLoader.load();

        fixSubdirectory("blocks/environment/character-overlay");
        fixSubdirectory("blocks/environment/rune-overlay");

        Core.settings = new MockSettings();
        Log.logger = new NoopLogHandler();
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();
        Vars.content.init();
        Log.logger = new DefaultLogHandler();

(...)

```

### Class Diagram

![DesignPatternFacade](https://github.com/user-attachments/assets/5c1a16fa-4b6d-48f5-939c-e3377ec4dc93)



### Rationale

The Facade Pattern is used to show a easier interface for the client to use, without needing to know the complexity below it.

In this case the class `core/src/mindustry/core/ContentLoader.java` is the **Facade**, which keeps the other classes without knowing what is really being done when called.
It loads a lot of classes, when it's instanciated.
For the class `tools/src/mindustry/tools/ImagePacker.java` the only thing that is being done is loading the content which might or might not be a simple function.

**Benefits**
- Easier to change the classes below the Facade because the classes using it, don't know what is being done.
- The classes using the Facade have less complexibility and are easier to understand 
