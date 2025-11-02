# Code Smell 1 - Data Clumps

`...\core\src\mindustry\ai\Astar`

## Code Snippet
```java
package mindustry.ai;

import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.world.*;

import java.util.*;

import static mindustry.Vars.*;

public class Astar{
    (...)

    public static Seq<Tile> pathfind(Tile from, Tile to, TileHeuristic th, Boolf<Tile> passable){
        return pathfind(from.x, from.y, to.x, to.y, th, manhattan, passable);
    }

    public static Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHeuristic th, Boolf<Tile> passable){
        return pathfind(startX, startY, endX, endY, th, manhattan, passable);
    }

    public static Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHeuristic th, DistanceHeuristic dh, Boolf<Tile> passable){
        Tiles tiles = world.tiles;

        Tile start = tiles.getn(startX, startY);
        Tile end = tiles.getn(endX, endY);
        (...)
    }
    (...)
}
```
### Rationale
The Data Clumps code smell arises when groups of variables, such as (int startX, int startY) and (int endX, int endY), consistently appear together in multiple locations. The justification for identifying this smell is an inversion of responsibility: the core A* logic resides in a method that accepts these data clumps. This design forces the "good" methods (the overloads accepting Tile objects) to decompose those objects (e.g., from.x, from.y) merely to call the main implementation. Instead of preserving the domain objects (Tile), the code actively breaks them down, creating clumps of primitives just to satisfy the method's signature. This pattern of unnecessary decomposition to pass parameters is a clear symptom of Data Clumps, affecting the design of all pathfind overloads.
### Suggested Refactoring
The canonical refactoring for Data Clumps is "Introduce Parameter Object" or, in this specific case, "Preserve Whole Object", since the required object (Tile) already exists. The strategy involves inverting the current logic flow.
Steps:
- **1. Move the Core Logic**: The AStar implementation ( the while(!queue.empty()) loop, etc.), wich currently resides in the pathfind(int startX, ...) method, must be moved to the overload that accepts the Tile objects.
- **2. Update the Main Signature**: The method containing the core logic should now become: ```java public static Seq<Tile> pathfind(Tile Start, Tile end, TileHeuristic th, Distance dh, Boolf<Tile> passable)```
- **3. Refactor Delegations**: The other methods (including the one accepting 4 ints) now become simple helpers. Their responsibility is to convert their parameters into the correct Tile objects and then call the new main method.

```java
//Step 3
//The method that previously accepted "clumps" now becomes a simple helper.
//Its only job is to convert primitives to objects and delegate.
public static Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHeuristic th, DistanceHeuristic dh, Boolf<Tile> passable){
    
    // 1. Re-assembles the clumps into their domain objects
    Tile start = world.tiles.getn(startX, startY);
    Tile end = world.tiles.getn(endX, endY);

    // 2. Calls the main method, "preserving the whole object"
    return pathfind(start, end, th, dh, passable); // Calls the new main implementation
}

// Step 3
//this "clean" overload now points to the new main implementation.
public static Seq<Tile> pathfind(Tile from, Tile to, TileHeuristic th, Boolf<Tile> passable){
    return pathfind(from, to, th, manhattan, passable); // Calls the new main implementation
}

// Step 2
// this is now the main implementation, accepting "whole objects" (Tile).
public static Seq<Tile> pathfind(Tile start, Tile end, TileHeuristic th, DistanceHeuristic dh, Boolf<Tile> passable){
    //Step 1
    //all Astar implementation logic is moved here.
    Tiles tiles = world.tiles;
    GridBits closed = new GridBits(tiles.width, tiles.height);
    (...)
    queue.add(start);
    (...)
    if(next == end){ /* ... */ }
    (...)
}
```