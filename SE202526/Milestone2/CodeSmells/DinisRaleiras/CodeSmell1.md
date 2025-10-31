# Code Smell 1
## Data Class

`...\core\src\mindustry\net\Host`

### Code Snippet
```java
package mindustry.net;

import arc.util.*;
import mindustry.*;
import mindustry.game.*;

public class Host{
    public final String name;
    public final String address;
    public final String mapname, description;
    public final int wave;
    public final int players, playerLimit;
    public final int version;
    public final String versionType;
    public final Gamemode mode;
    public final @Nullable String modeName;
    public int ping, port;

    public Host(int ping, String name, String address, int port, String mapname, int wave, int players, int version, String versionType, Gamemode mode, int playerLimit, String description, String modeName){
        this.ping = ping;
        this.name = name;
        this.address = address;
        this.port = port;
        this.mapname = mapname;
        this.wave = wave;
        this.players = players;
        this.version = version;
        this.versionType = versionType;
        this.mode = mode;
        this.playerLimit = playerLimit;
        this.description = description;
        this.modeName = modeName;
    }

    public Host(int ping, String name, String address, String mapname, int wave, int players, int version, String versionType, Gamemode mode, int playerLimit, String description, String modeName){
        this(ping, name, address, Vars.port, mapname, wave, players, version, versionType, mode, playerLimit, description, modeName);
    }
}
```
### Rationale

The `Host` class represents a **Data Class** code smell.  
It only contains fields and constructors without any meaningful behavior or methods that operate on its data.  
This design violates the principle of **encapsulation**, as the logic that should belong to this class is likely implemented elsewhere in the system.  
This is an indicator that the class maybe not not be a good abstraction.


### Suggested Refactoring
To adress que *Data Class* code smell we need to introduce behavior to the `Host` class.

1. **Add Behaviour**: Implement methods that operate on the data within the `Host` class.

```java
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
    
    (...)
```
This way we have more functionalities related to the data encapsulated within the `Host` class itself, improving cohesion and maintainability.






