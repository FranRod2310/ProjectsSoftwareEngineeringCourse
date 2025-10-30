# Smell #1 - Inappropriate Intimacy

`...\core\src\mindustry\game\Team`

## Code Snippet
```java
package mindustry.game;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.logic.Senseable;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

public class Team implements Comparable<Team>, Senseable {
    (...)

    public boolean isAI() {
        return (Vars.state.rules.waves || Vars.state.rules.attackMode || Vars.state.isCampaign()) && this != Vars.state.rules.defaultTeam && !Vars.state.rules.pvp;
    }
    
    (...)
}
```
### Rationale

The `Team` class directly accesses internal details of other classes through the global `Vars` singleton.
This includes nested objects and fields within `Vars.state.rules`, such as `waves`, `attackMode`, `pvp`, and
`defaultTeam`.

This creates **Inappropriate Intimacy** between the `Team` class and the `Rules`/`GameState` classes, since `Team`
now depends on their **internal structure and state** to determine AI behaviour. 

### Suggested Refactoring

To address the **Inappropriate Intimacy** code smell, we should **move the decision logic about AI-controlled
teams from `Team` class to the `Rules` class — which actually owns the relevant data.
This will reduce coupling, respect encapsulation, and make the system easier to maintain and test.

#### Step 1 - Add Behaviour to `Rules`:

```java
package mindustry.game;

import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.graphics.g3d.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.type.Weather.*;
import mindustry.world.*;
import mindustry.world.blocks.*;

/**
 * Defines current rules on how the game should function.
 * Does not store game state, just configuration.
 */
public class Rules{
    (...)

    public boolean isAITeam(Team team) {
        return (waves || attackMode || Vars.state.isCampaign()) && team != defaultTeam && !pvp;
    }
    
    (...)
}
```

#### Step 2 - Simplify `Team`:

```java
package mindustry.game;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.logic.Senseable;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

public class Team implements Comparable<Team>, Senseable {
    (...)

    public boolean isAI() {
        return Vars.state.rules.isAITeam(this);
    }
    
    (...)
}
```

This refactoring delegates the AI-related logic to the `Rules` class — the true owner of that information —
and keeps `Team` focused on team-specific behavior.
As a result, the design becomes more **cohesive, loosely coupled**, and compliant with the **Law of Demeter**
and **Single Responsibility Principle (SRP)**.

# Smell #2 - Shotgun Surgery

`...\core\src\mindustry\world\modules`

## Code Snippet
The `BlockModule` superclass has three subclasses — `ItemModule`, `LiquidModule`, and `PowerModule` —
that all implement a `write(Writes write)` method with **very similar logic**:
```java
// ItemModule
@Override
public void write(Writes write){
    int amount = 0;
    for(int item : items){
        if(item > 0) amount++;
    }

    write.s(amount);

    for(int i = 0; i < items.length; i++){
        if(items[i] > 0){
            write.s(i); //item ID
            write.i(items[i]); //item amount
        }
    }
}

// LiquidModule
@Override
public void write(Writes write){
    int amount = 0;
    for(float liquid : liquids){
        if(liquid > 0) amount++;
    }

    write.s(amount); //amount of liquids

    for(int i = 0; i < liquids.length; i++){
        if(liquids[i] > 0){
            write.s(i); //liquid ID
            write.f(liquids[i]); //liquid amount
        }
    }
}

// PowerModule
@Override
public void write(Writes write){
    write.s(links.size);
    for(int i = 0; i < links.size; i++){
        write.i(links.get(i));
    }
    write.f(status);
}
```
### Rationale
The problem with this design is that each subclass duplicates nearly identical logic. While the type and 
minor details differ, the pattern—count active elements, write the count, loop over elements, serialize index
and value—is repeated in multiple places.

#### This creates several issues:
- **Maintenance difficulty**: Any change to the serialization logic (for example, changing how zeros are handled or how indices are encoded) requires editing each subclass individually. This increases the risk of introducing errors and inconsistencies.
- **High coupling of changes**: A single conceptual modification spreads across multiple classes, forcing developers to touch several files for what should conceptually be a single change.
- **Violation of DRY principle**: "Don't Repeat Yourself" is violated because the same logic is repeated with slight variations across multiple locations.

This scenario is a textbook example of shotgun surgery, where a small change in requirements 
triggers modifications in many places, increasing complexity and risk.

### Suggested Refactoring
To eliminate this problem, we can centralize the repeated logic in a single helper method in the superclass `BlockModule`. 
By making the helper method generic, it can handle arrays or lists of any type and delegate the actual writing of values 
to a function provided by the subclass.
```java
public abstract class BlockModule {
    
    protected <T> void writeArray(Writes write, T[] array, BiConsumer<Writes, T> writer, Predicate<T> isActive){
        int amount = 0;
        for(T element : array){
            if(isActive.test(element)) amount++;
        }
        write.s(amount);

        for(int i = 0; i < array.length; i++){
            if(isActive.test(array[i])){
                write.s(i);
                writer.accept(write, array[i]);
            }
        }
    }

    public abstract void write(Writes write);

    (...)
}
```

### Refactored Subclasses
```java
// ItemModule
@Override
public void write(Writes write){
    writeArray(write, items, Writes::i, item -> item > 0);
}

// LiquidModule
@Override
public void write(Writes write){
    writeArray(write, liquids, Writes::f, liquid -> liquid > 0);
}

// PowerModule
@Override
public void write(Writes write){
    write.f(power);
}
```

### Explanation of the Refactoring
**By extracting the common logic into the generic `writeArray` method**:
- All subclasses reuse the same serialization pattern.
- The subclasses are simplified: each one now only defines what data it has and how to write individual elements.
- Any future change in serialization rules — such as altering how active elements are counted — requires modifying only the generic method, not every subclass.
- The design now adheres more closely to the Single Responsibility Principle (SRP): the superclass handles the serialization pattern, while subclasses focus on their specific data.

### Benefits
**This refactoring improves the code in several ways**:
- **Reduces duplicated code** across multiple classes.
- **Simplifies maintenance**: changes are centralized in a single helper method.
- **Minimizes risk of bugs** from inconsistent changes across subclasses.
- **Improves clarity**: each subclass is easier to read and understand.
- **Supports future extensions**: adding new modules for other resources now requires implementing `write` in terms of `writeArray`, without rewriting the pattern.