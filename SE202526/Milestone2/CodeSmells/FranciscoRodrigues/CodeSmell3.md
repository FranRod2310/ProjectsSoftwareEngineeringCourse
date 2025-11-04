# Smell #3 - Data Class
 `...\core\src\mindustry\entities\units\LegDestroyData`

## Code Snippet
```java
package mindustry.entities;

import arc.graphics.g2d.*;
import arc.math.geom.*;

public class LegDestroyData{
    public Vec2 a, b;
    public TextureRegion region;

    public LegDestroyData(Vec2 a, Vec2 b, TextureRegion region){
        this.a = a;
        this.b = b;
        this.region = region;
    }
}
```

### Rationale
The `class LegDestroyData` is clearly a case of a **Data Class** code smell.
Doesn't have any logic, only data.

### Suggested Refactoring
To fix this, after trying to add new logic functions to the class, I concluded that the best option was to remove the Data Class, and store it as a record.

### Extract Methods to add
```java
// record for LegDestroyData 
public record LegDestroyData(
 Vec2 a,
 Vec2 b,
 TextureRegion region
) { }
```

## Update `LegDestroyData`

```java
package mindustry.entities;

import arc.graphics.g2d.*;
import arc.math.geom.*;

public record LegDestroyData(Vec2 a, Vec2 b, TextureRegion region) { }
```
