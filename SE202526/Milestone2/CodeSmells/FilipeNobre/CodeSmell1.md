# Code Smell 1 - Inappropriate Intimacy

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