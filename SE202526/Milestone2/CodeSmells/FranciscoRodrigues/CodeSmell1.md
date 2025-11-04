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
