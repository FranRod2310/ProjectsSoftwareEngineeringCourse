# Code Smell 2 - Long Method
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

public class Astar {
    (...)

    public static Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHeuristic th, DistanceHeuristic dh, Boolf<Tile> passable) {
        Tiles tiles = world.tiles;

        Tile start = tiles.getn(startX, startY);
        Tile end = tiles.getn(endX, endY);

        GridBits closed = new GridBits(tiles.width, tiles.height);

        if (costs == null || costs.length != tiles.width * tiles.height) {
            costs = new float[tiles.width * tiles.height];
        }

        Arrays.fill(costs, 0);

        queue.clear();
        queue.comparator = Structs.comparingFloat(a -> costs[a.array()] + dh.cost(a.x, a.y, end.x, end.y));
        queue.add(start);
        if (rotations == null || rotations.length != world.width() || rotations[0].length != world.height()) {
            rotations = new byte[world.width()][world.height()];
        }

        boolean found = false;
        while (!queue.empty()) {
            Tile next = queue.poll();
            (...)
            if (next == end) {
                found = true;
                break;
            }
            closed.set(next.x, next.y);
            for (Point2 point : Geometry.d4) {
                (...)
            }
        }

        out.clear();

        if (!found) return out;

        Tile current = end;
        while (current != start) {
            out.add(current);

            byte rot = rotations[current.x][current.y];
            current = tiles.getn(current.x + Geometry.d4x[rot], current.y + Geometry.d4y[rot]);
        }

        out.reverse();

        return out;
    }
}
```
### Rationale
This method is a classic example of a Long Method. While its line count (apprx. 50 lines) is a strong indicator, the primary justification is its low cohesion and violation of the Single Responsibility Principle (SRP). The method is responsible for at least three distinct sequential tasks: **1) Setup and Cache Management**, where it initializes Tile objects from primitive parameters, checks and resizes the static costs and rotations arrays, and prepares the PQueue; **2) The Search (Algorithm)**, wich is the main while(!queue.empty()) loop that executes the AStar search; and **3) Path Reconstruction**, wich is the second while(current !=start) loop that reads the rotations array to build the final path. When a method has such distinct "phases", each phase should be extracted into its own private helper method.

### Suggested Refactoring
The canonical refactoring for a Long Method is "Extract Method".

**Steps**:
- **1.** The "Path Reconstruction" logic (the final while loop) is the cleanest and most obvious candidate for extraction.
- **2.** A new private helper method, reconstructPath, should be created. It will take the state needed to build the path, wich the original pathfind method already calculated: the 'start' and 'end' Tile objects, the rotations array, and the tiles variable.
- **3.** The main pathfind method becomes shorter and more focused on the search, delegating the final step to this new method.

```java
    // The main 'pathfind' method is now shorter.
    public static Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHeuristic th, DistanceHeuristic dh, Boolf<Tile> passable){
    
        // All original setup logic remains exactly as it was.
        Tiles tiles = world.tiles;
        Tile start = tiles.getn(startX, startY);
        Tile end = tiles.getn(endX, endY);
    
        GridBits closed = new GridBits(tiles.width, tiles.height);
    
        if(costs == null || costs.length != tiles.width * tiles.height){
            costs = new float[tiles.width * tiles.height];
        }
    
        Arrays.fill(costs, 0);
    
        queue.clear();
        queue.comparator = Structs.comparingFloat(a -> costs[a.array()] + dh.cost(a.x, a.y, end.x, end.y));
        queue.add(start);
        if(rotations == null || rotations.length != world.width() || rotations[0].length != world.height()){
            rotations = new byte[world.width()][world.height()];
        }
    
        // All original A* search logic remains exactly as it was.
        boolean found = false;
        while(!queue.empty()){
            Tile next = queue.poll();
            float baseCost = costs[next.array()];
            if(next == end){
                found = true;
                break;
            }
            closed.set(next.x, next.y);
            for(Point2 point : Geometry.d4){
                int newx = next.x + point.x, newy = next.y + point.y;
                if(Structs.inBounds(newx, newy, tiles.width, tiles.height)){
                    Tile child = tiles.getn(newx, newy);
                    if(passable.get(child)){
                        float newCost = th.cost(next, child) + baseCost;
                        if(!closed.get(child.x, child.y)){
                            closed.set(child.x, child.y);
                            rotations[child.x][child.y] = child.relativeTo(next.x, next.y);
                            costs[child.array()] = newCost;
                            queue.add(child);
                        }
                    }
                }
            }
        }
    
        // The original path reconstruction logic is now replaced...
        if(!found){
            out.clear();
            return out;
        }
    
        // Step 3
        return reconstructPath(start, end, rotations, tiles);
    }
    
    
    //Step 1 & 2: A new private method is created, containing the
    //extracted "Path Reconstruction" logic.
    private static Seq<Tile> reconstructPath(Tile start, Tile end, byte[][] rotations, Tiles tiles){
        out.clear();
        Tile current = end;
        while(current != start){
            out.add(current);
            byte rot = rotations[current.x][current.y];
            current = tiles.getn(current.x + Geometry.d4x[rot], current.y + Geometry.d4y[rot]);
        }
        out.reverse();
        return out;
    }
```