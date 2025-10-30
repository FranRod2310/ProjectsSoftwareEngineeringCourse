# Smell #1 - Data clumps
 `...\core\src\mindustry\entities\units\Damage`

 ### Code snippet

 ```java
public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, Effect explosionFx){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, Effect explosionFx, float baseShake){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx, float baseShake){

```

### Rationale
These methods take the same set of parameters for their funcionality. This is a case of a **Data Clumps** code smell.
If it is needed to change some parameter, it is necessary to do it in each one of them.

### Suggested Refactoring
Create a record, `ExplosionParams` to pass as argument for those methods.

## New ExplosionParams record
```java
// record for parameters dynamicExplosion
public record ExplosionParams(
    float x,
    float y,
    float flammability,
    float explosiveness,
    float power,
    float radius,
    boolean damage
) {}
```
This now simplifies the parameter list for many methods inside the Damage class:
```java

// BEFORE:
public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, Effect explosionFx){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, Effect explosionFx, float baseShake){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx){

public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx, float baseShake){

// AFTER:
public static void dynamicExplosion(ExplosionParams params){

public static void dynamicExplosion(ExplosionParams params, Effect explosionFx){

public static void dynamicExplosion(ExplosionParams params, Effect explosionFx, float baseShake){

public static void dynamicExplosion(ExplosionParams params, boolean fire, @Nullable Team ignoreTeam){

public static void dynamicExplosion(ExplosionParams params, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx){

public static void dynamicExplosion(ExplosionParams params, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx, float baseShake){

```

# Smell #2 - Long Parameter List
 `...\core\src\mindustry\entities\units\EntityCollisions`

## Code Snippet

```java
public static boolean collide(float x1, float y1, float w1, float h1, float vx1, float vy1,
                            float x2, float y2, float w2, float h2, float vx2, float vy2, Vec2 out){...}
```

### Rationale
In the `collide(...)` method, we can find the **Long Parameter List** code smell.
A lot of parameters, which makes it easy to change the order of passing it into the function.

### Suggested Refactoring
Create a record, `HitBox` to pass as argument to this method.

## Update `EntityCollisions`
```java
// record for hitbox parameters 
record Hitbox(
  float x,
  float y,
  float width,
  float height,
  float vx,
  float vy
) {}

```

## Update `EntityCollisions`
```java
// BEFORE:
public static boolean collide(float x1, float y1, float w1, float h1, float vx1, float vy1,
                            float x2, float y2, float w2, float h2, float vx2, float vy2, Vec2 out){...}
// AFTER:
public static boolean collide(HitBox hb1, HitBox hb2, Vec2 out){...}
```



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




