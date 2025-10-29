# Data clumps
 `...\core\src\mindustry\entities\units\Units`

 # Code snippet

 ```java
public static boolean invalidateTarget(Posc target, Team team, float x, float y) {...}

public static boolean anyEntities(float x, float y, float size) {...}

public static boolean anyEntities(float x, float y, float width, float height) {...}

public static boolean anyEntities(float x, float y, float width, float height, boolean ground) {...}

```

# Rationale
These methods take the same set of parameters for their funcionality. This is a case of a **Data Clumps** code smell.

# Suggested Refactoring
Create new objects, `Area` and `TargetQuery` to pass as arguments for such methods.

# New Area parameter object
```java
// Object for rectangular areas (replaces x,y,width,height)
public final class Area {
    public final float x, y, width, height;

    public Area(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    (...)

    public static Area fromCenter(float cx, float cy, float size){
        return new Area(cx - size/2f, cy - size/2f, size, size);
    }

    public Rect toRect(){
        return new Rect(x, y, width, height);
    }
}
```
# New TargetQuery object
```java
// Object for target-query info (replaces team/x/y/range/flags)
public final class TargetQuery {
    public final Team team;
    public final float x, y, range;
    public final boolean allowAir, allowGround;
    public final Boolf<Unit> unitPredicate; 

    public TargetQuery(Team team, float x, float y, float range, boolean allowAir, boolean allowGround, Boolf<Unit> unitPredicate){
        this.team = team;
        this.x = x;
        this.y = y;
        this.range = range;
        this.allowAir = allowAir;
        this.allowGround = allowGround;
        this.unitPredicate = unitPredicate != null ? unitPredicate : u -> true;
    }

    public TargetQuery(Team team, float x, float y, float range, boolean allowAir, boolean allowGround){
        this(team, x, y, range, allowAir, allowGround, u -> true);
    }

    public Rect bounds(){
        return new Rect(x - range, y - range, range * 2f, range * 2f);
    }

    /** Return whether the target is invalid according to this query. */
    public boolean invalidates(Posc target){
        if(target == null) return true;
        float extra = (target instanceof mindustry.entities.Sized hb ? hb.hitSize()/2f : 0f);
        if(range != Float.MAX_VALUE && !target.within(x, y, range + extra)) return true;
        if(target instanceof mindustry.game.Teamc tc && tc.team() == team) return true;
        if(target instanceof mindustry.world.blocks.ProductionBlock.Healthc hc && !hc.isValid()) return true; // defensive check if Healthc exists
        if(target instanceof Unit u && !u.targetable(team)) return true;
        return false;
    }
}
```

This now simplifies the parameter list for many methods inside the Units class:
```java

// BEFORE:
public static boolean invalidateTarget(Posc target, Team team, float x, float y)
// AFTER:
public static boolean invalidateTarget(Posc target, TargetQuery q)

// BEFORE:
public static boolean anyEntities(float x, float y, float width, float height, boolean ground)
// AFTER:
public static boolean anyEntities(Area area, boolean ground)

// BEFORE:
public static boolean anyEntities(float x, float y, float size)
// AFTER:
public static boolean anyEntities(Area area)
```
