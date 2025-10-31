# Code Smell 2
## Long Method

`...\core\src\mindustry\game\FogControl`

### Code Snippet
```java
public FogControl(){
        Events.on(ResetEvent.class, e -> {
            stop();
        });

        Events.on(WorldLoadEvent.class, e -> {
            stop();

            loadedStatic = false;
            justLoaded = true;
            ww = world.width();
            wh = world.height();

            //all old buildings have static light scheduled around them
            if(state.rules.fog && state.rules.staticFog){
                pushStaticBlocks(true);
                //force draw all static stuff immediately
                updateStatic();

                loadedStatic = true;
            }
        });

        Events.on(TileChangeEvent.class, event -> {
            if(state.rules.fog && event.tile.build != null && event.tile.isCenter() && !event.tile.build.team.isOnlyAI() && event.tile.block().flags.contains(BlockFlag.hasFogRadius)){
                var data = data(event.tile.team());
                if(data != null){
                    data.dynamicUpdated = true;
                }

                if(state.rules.staticFog){
                    synchronized(staticEvents){
                        //TODO event per team?
                        pushEvent(FogEvent.get(event.tile.x, event.tile.y, Mathf.round(event.tile.build.fogRadius()), event.tile.build.team.id), false);
                    }
                }
            }
        });

        //on tile removed, dynamic fog goes away
        Events.on(TilePreChangeEvent.class, e -> {
            if(state.rules.fog && e.tile.build != null && !e.tile.build.team.isOnlyAI() && e.tile.block().flags.contains(BlockFlag.hasFogRadius)){
                var data = data(e.tile.team());
                if(data != null){
                    data.dynamicUpdated = true;
                }
            }
        });

        //unit dead -> fog updates
        Events.on(UnitDestroyEvent.class, e -> {
            if(state.rules.fog && fog[e.unit.team.id] != null){
                fog[e.unit.team.id].dynamicUpdated = true;
            }
        });

        SaveVersion.addCustomChunk("static-fog-data", this);
    }
```
### Rationale
The constructor of the `FogControl` class exhibits a **Long Method** code smell.  
It contains multiple event registrations (`ResetEvent`, `WorldLoadEvent`, `TileChangeEvent`, `TilePreChangeEvent`, `UnitDestroyEvent`) all defined within a single block.  
This results in a method that performs too many responsibilities and becomes difficult to read, maintain, and extend.
In short, this design tightly couples object construction with behavior registration, making the code less maintainable and less understandable.


### Suggested Refactoring
```java
public class Host {

    private final String name;
    private final String address;
    private final String mapName;
    private final String description;
    private final int wave;
    private final int players;
    private final int playerLimit;
    private final int version;
    private final String versionType;
    private final Gamemode mode;
    private final String modeName;
    private int ping;
    private int port;

    public Host(String name, String address, String mapName, int wave, int players, int playerLimit, 
                int version, String versionType, Gamemode mode, String description, String modeName, int port, int ping) {
        this.name = name;
        this.address = address;
        this.mapName = mapName;
        this.wave = wave;
        this.players = players;
        this.playerLimit = playerLimit;
        this.version = version;
        this.versionType = versionType;
        this.mode = mode;
        this.description = description;
        this.modeName = modeName;
        this.port = port;
        this.ping = ping;
    }

    // ---- Added behavior (instead of only getters) ----

    /** Checks if the server is full. */
    public boolean isFull() {
        return players >= playerLimit;
    }

    /** Returns the server occupancy percentage. */
    public double occupancyRate() {
        return (playerLimit == 0) ? 0.0 : (double) players / playerLimit;
    }

    /** Checks if the server version matches the client version. */
    public boolean isCompatible(int clientVersion) {
        return this.version == clientVersion;
    }

    /** Returns a formatted string summarizing the host info. */
    public String getSummary() {
        return String.format("%s (%s) - %d/%d players, wave %d, map: %s", 
                             name, address, players, playerLimit, wave, mapName);
    }

    // Example of encapsulation: setters only if necessary
    public void updatePing(int newPing) {
        if (newPing >= 0) this.ping = newPing;
    }

    public int getPing() {
        return ping;
    }

    public String getName() {
        return name;
    }
}
```
