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
