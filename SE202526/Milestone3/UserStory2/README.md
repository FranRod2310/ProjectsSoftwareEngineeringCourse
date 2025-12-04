# User story 2
### New Turret Defense Mechanic
## Author(s)
 - Dinis Raleiras (67819)
 - Filipe Nobre (67850)
## Reviewer(s)
- Francisco Rodrigues (67753)
- Miguel Rosalino (68210)

## User Story:
*As an experienced player I want to have a greater diversity of towers so that it can help me automate the way I defend waves*
### Review
The use story follows a correct structure and the premise is relevant.

## Use case diagram
![UseCaseDiagram.png](UseCase_Diagram_US2.png)
## Use case textual description

### UC1 – Select Turret
**Name** : Select Turret

**ID**: UC1

**Description**:

The player selects a turret type (attack or support) to deploy.

**Actors**

- **Primary**: Player

- **Secondary**: —

- **Preconditions** :
  - The player is in an active gameplay session.
  - The system is not paused.
   - The player has enough resources to build at least one turret type.
  
 - **Main Scenario**
    - The player opens the build menu.
    - The system displays available turret types.
    - The player selects a turret.
  
  - **Alternative Scenarios**

    - The selected turret is too expensive, system blocks the action and shows a warning.
  
 - **Postconditions**
   - A turret type is selected and ready for deployment.

### UC2 – Deploy Attack Turret
**Name** : Deploy Attack Turret

**ID**: UC2

**Description**:

The player places an attack turret on the map.

**Actors**

- **Primary**: Player

- **Secondary**: —

- **Preconditions** :
    - The player previously selected an attack turret (UC1).
    - The selected tile is valid for placement.
    - The player has enough resources to build the turret.
- **Main Scenario**
    - The player clicks or taps the desired location on the map.
    - The system validates the placement area.
    - The system creates the attack turret at the chosen location.
    - The turret initializes with base stats (health, damage, range).
    - The system adds the turret to the player's list of structures.

- **Alternative Scenarios**

    - The placement location is invalid, the system cancels the action.
    - The player lacks the required resources, the system shows an error.

- **Postconditions**
    - A functional attack turret is deployed and active on the map.

### UC3 – Deploy Support Turret
**Name** : Deploy Support Turret

**ID**: UC3

**Description**:

The player places a support turret that provides buffs to nearby structures.

**Actors**

- **Primary**: Player

- **Secondary**: —

- **Preconditions** :
    - The player previously selected a support turret (UC1).
    - The placement tile is valid.
    - The player has sufficient resources.

- **Main Scenario**
    - The player selects a location on the map.
    - The system checks if the location is valid.
    - The system creates the support turret.
    - The turret initializes with a default buff radius.

- **Alternative Scenarios**

  - The location is invalid, the system refuses the placement.
  - The player does not have enough resources.

- **Postconditions**
    - A support turret is deployed and ready to detect nearby turrets.

### UC4 – Verify Nearby Turrets
**Name** : Verify Nearby Turrets

**ID**: UC4

**Description**:

The support turret scans its surroundings to detect turrets within buff range.


**Actors**

- **Primary**: Support Turret

- **Secondary**: —

- **Preconditions** :
    - A support turret is active on the map.

- **Main Scenario**
    - The support turret periodically scans its buff radius.
    - The system calculates distances to nearby turrets.
    - The system creates a list of eligible turrets within range.

- **Alternative Scenarios**

  - No nearby turrets are detected, resulting in an empty list.

- **Postconditions**
    - A list of turrets within buff range is available for the boosting process.

### UC5 – Boost Nearby Turrets
**Name** : Boost Nearby Turrets

**ID**: UC5

**Description**:

The support turret applies temporary stat boosts to nearby turrets.

**Actors**

- **Primary**: Support Turret

- **Secondary**: Attack Turret

- **Preconditions** :
    - A list of nearby turrets is available (from UC4).

- **Main Scenario**
    - The system evaluates turrets within the buff radius.
    - The system applies temporary boosts (damage, fire rate, etc.).
    - Affected turrets update their stats while remaining in range.

- **Alternative Scenarios**

    - The support turret is destroyed, removing all active buffs.
    - A turret leaves the buff radius, ending its boosted state.

- **Postconditions**
    - Nearby turrets receive temporary enhancements if within range.


### UC6 – Shoot
**Name** : Shoot

**ID**: UC6

**Description**:

Attack turrets automatically fire at enemies within range.

**Actors**

- **Primary**: Attack Turret

- **Secondary**: Enemy

- **Preconditions** :
    - The turret is active and operational.
    - An enemy is within the turret’s shooting range.

- **Main Scenario**
    - The turret detects an enemy target.
    - The turret aims at the closest or highest-priority target.
    - The turret fires projectiles.
    - The system calculates projectile damage and effects.
    - Damage is applied to the enemy.

- **Alternative Scenarios**

    - No enemy is in range, the turret does not shoot.
    - The turret is out of ammo (if applicable).

- **Postconditions**
    - The enemy takes damage or is destroyed.



### Review

The use cases to develop are well chosen considering the user story. The main and alternative flows seem correct, and pre and post conditions too.
The actors described in the use cases however don't match the use case diagram. The Support Turret and Attack Turret actor are not present in the diagram.
The **includes** and **extends** relations make sense, but aren't explained in the use case descriptions. 
Overall, the use case descriptions are correct, but the diagram doesn't fully reflect them.


## Implementation documentation

### SupportBuffTower Class


`...\core\src\mindustry\world\blocks\defense\SuportBuffTower `

In order to implement the new turret defense mechanic, we created a new class called `SupportBuffTower`, which extends the existing `PowerBlock` class which extends the `Block` class. 
This class is responsible for providing buffs to nearby turrets within a specified radius.

#### Code Snippet
```java
package mindustry.world.blocks.defense;


import (...)

import static mindustry.Vars.tilesize;

public class SupportBuffTower extends PowerBlock {
    public final float buffRange = 60f;
    public final float baseDamageMultiplier = 2.5f;

    public SupportBuffTower(String name) {
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        consumePower(1.2f);
        buildTime = 120f;
        health = 140;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.damageMultiplier, baseDamageMultiplier, StatUnit.none);
        stats.add(Stat.range, buffRange / Vars.tilesize, StatUnit.blocks);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, buffRange, Pal.accent);
    }

    public class SupportBuffBuild extends Building {
        float visualTimer = 60f;
        float pulseTimer = 0f;
        final float pulseDuration = 90f;

        @Override
        public void drawSelect() {
            Drawf.dashCircle(x, y, buffRange, Pal.accent);
        }

        @Override
        public void draw() {
            super.draw();
            if (efficiency <= 0f) return;
            float radius = Mathf.lerp(0, buffRange, pulseTimer);
            float alpha = 0.7f * Mathf.curve(pulseTimer, 0f, 0.5f) * (1f - pulseTimer);
            Lines.stroke(3f * (1f - pulseTimer));
            Draw.color(Pal.accent, alpha);
            Lines.circle(x, y, radius);
            Draw.reset();
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if (efficiency <= 0f) return;
            visualTimer += Time.delta;

            pulseTimer += Time.delta / pulseDuration;
            if (pulseTimer >= 1f) {
                pulseTimer = 0f;
            }

            applyDamageBoost();
        }

        void applyDamageBoost() {
            if (efficiency <= 0) return;

            float pulseRadius = Mathf.lerp(0, buffRange, pulseTimer);
            float tolerance = 0.2f;

            Vars.indexer.eachBlock(
                    this.team,
                    this.x,
                    this.y,
                    buffRange,
                    b -> b instanceof Turret.TurretBuild,
                    b -> {
                        Turret.TurretBuild turret = (Turret.TurretBuild) b;

                        // calcula distância do centro do buff até a torre
                        float dist = Mathf.dst(x, y, turret.x, turret.y);

                        // se o pulso "atingiu" a torre
                        if (dist >= pulseRadius - tolerance && dist <= pulseRadius + tolerance) {

                            // Efeito visual
                            Fx.sparkExplosion.at(turret.x, turret.y, 0, Pal.accent);
                        }
                        // Aplica buff
                        turret.supportDamageMultiplier = baseDamageMultiplier;
                    }
            );
        }

    }
}
```
##### Methods

- `setStats()`: Sets the statistics for the support buff tower, including damage multiplier and buff range.
- `drawPlace(int x, int y, int rotation, boolean valid)`: Draws the placement indicator for the tower, including the buff range circle. Use Case: `UC3`
- `SupportBuffBuild` class: Inner class that represents the building instance of the support buff tower.
  - `drawSelect()`: Draws the selection indicator for the tower.
  - `draw()`: Handles the visual effects of the tower, including the pulsing buff effect.
  - `updateTile()`: Updates the tower's state, including applying damage boosts to nearby turrets. Use Case : `UC6`
  - `applyDamageBoost()`: Applies the damage boost to turrets within the buff range when the pulse reaches them. Use Case: `UC4`, `UC5`

### Turret Class

`...\core\src\mindustry\world\blocks\defense\turrets\Turret `

#### Changed `shoot` `UC6` method and `supportDamageMultiplier` variable
```java
(...) 
public float supportDamageMultiplier = 1f;

(...)
BulletType buffedType = type.copy();
buffedType.damage *= supportDamageMultiplier;
(...)
```


This prevent turret damage from scaling permanently, the bullet type is copied during the shooting process.
This ensures that:
 - The original `BulletType` remains unchanged
 - The damage multiplier applies only to the current shot
 - No exponential or persistent damage boosts occur across multiple shots
 - Other turrets using the same bullet type are not affected

### Added resetSupportMultiplier()

``` java
 public void resetSupportMultiplier() {
            supportDamageMultiplier = 1f;
        }
```
This method is called in the end of the `updateTile` method.

Calling resetSupportMultiplier() at the end of updateTile() ensures that each turret:
- Starts every tick with a default multiplier of 1f
- Only receives the buff if a SupportBuffTower is in range during that tick
- Never keeps the buff permanently
- Does not accumulate or stack damage boosts across updates
- This guarantees consistent, non-persistent buff behavior and prevents unintended damage scaling.

### Block Class

`...\core\src\mindustry\content\Block `

#### Initialization of `supportBuffTower`

Use Cases: `UC1`, `UC3`

```java
supportBuffTower = new SupportBuffTower("support-buff-tower") {{
    requirements(Category.turret, with(Items.copper, 200, Items.lead, 150, Items.silicon, 100));
    size = 1;
    update = true;
}};
```
This defines the necessary resources for the construction of the Support Buff Tower.

### Package support-buff-tower

`\...\core\assets-raw\sprites\blocks\turrets\suppport-buff-tower`

(*Sprites and visual assets for the Support Buff Tower.*)
![SupportBuffTowerSprites.png](SupportBuffTowerSprites.png)

**This Represents the image of out tower.**


### Language Descriptions

`...\core\assets\bundles\bundle.properties`

``` properties
(...)
block.support-buff-tower.name = Advance Support Tower
(...)
block.support-buff-tower.description = Boost the damage of the nearby towers.
(...)
```

**This adds the name and description of the tower in the game.**


### Implementation summary
We have made changes throughout the codebase to implement the new turret defense mechanic.
The main addition is the `SupportBuffTower` class, which handles the logic for buffing nearby turrets.
Other changes include modifications to the `Turret` class to accommodate the damage boost functionality.
We also created new assets for the tower and updated the language files to include the tower's name and description.

#### Review
This summary, along with all the code snippets above, explains the process of implementing this new tower type in a very clear and well structured way. The new class is justified and organized, and the changes made to the other classes are well documented. The implementation follows the user story and use cases previously defined very well. Overall, it's a well implemented and well documented story.

### Class diagrams
![Class_Diagram_US2](Class_Diagram.png)

### Review
The class diagram here is clean and readable. The use of generalization and dependency is justified. The information available in each class in relevant. In the SupportBuffField, attributes are missing visibility markers. As for code structure, the right side of the diagram might indicate a very deep inheritance chain, so that might be something worth looking at. Besides that, it' a well structured and informative class diagram for this user story.

### Sequence diagrams
####  Sequence Diagram 1 Tower Selection
In this diagram we represent the selection process of the twoer with the respective classes
![Sequence_Diagram](Sequence_Diagram1.png)

#### Sequence Diagram 2 Tower Deploy

In this diagram we represent the deployment process of the tower with the respective classes
![Sequence_Diagram](Sequence_Diagram2.png)

#### Sequence Diagram 3 Tower Buff
In this diagram we represent the buff process of the tower with the respective classes. This process happens every ticket of the game
![Sequence_Diagram](Sequence_Diagram3.png)

#### Sequence Diagram 4 Tower Shoot
In this diagram we represent the shoot process of the tower with the respective classes. This process happens every ticket of the game
![Sequence_Diagram](Sequence_Diagram4.png)

#### Sequence Diagram 5 Overall
In this diagram we show the order of the processes in our implementation.
![Sequence_Diagram](Sequence_Diagram5.png)
#### Review
*(Please add your sequence diagram review here)*

## Test specifications
`...\tests\src\test\java\mindustry.world.blocks.defense\SupportBuffTowerTest`

### Initialization of the tower
Implemented a setUp method annotated with `@BeforeEach` to properly initialize the testing environment.
This method mocks Vars.content to prevent NullPointerException due to missing global game state.
Additionally, it assigns a unique identifier to each SupportBuffTower instance using System.nanoTime() to
resolve IllegalArgumentException conflicts caused by duplicate content names during sequential test execution.

```java
    @BeforeEach
    void setUp() {
        // 1. Initialize Vars.content (Previous fix)
        if (Vars.content == null) {
            Vars.content = new ContentLoader();
        }

        // for block logic that interacts with the map (like applyDamageBoost -> Vars.indexer.eachBlock).
        if (Vars.indexer == null) {
            Vars.indexer = new BlockIndexer();
            Vars.world = new World(); // Required by BlockIndexer or block methods
            Vars.state = new GameState();
        }
        // Initialize the new tower for each test
        // We add the System.nanoTime() to ensure a unique name for each test instance
        tower = new SupportBuffTower("support-buff-tower-" + System.nanoTime());

        // Initialize the building
        // It is an inner class so we must initialize it manually
        build = tower.new SupportBuffBuild();
    }
```
### Test 1: Base Attributes Verification
This unit test method's main objective is to verify the correct initialization of the base attributes and constants of the SupportBuffTower class.

```java
    @Test
    @DisplayName("Must initialize with the correct base attributes")
    void testBaseStats() {
        assertEquals(2.5f, tower.baseDamageMultiplier, "The multiplier must be 2.5");
        assertEquals(60f, tower.buffRange, "The range buff must be 60f");
        assertEquals(120f, tower.buildTime, "The time to build must be 120f or 2 seconds");
        assertTrue(tower.hasPower, "The block must need power");
        assertTrue(tower.solid, "The block must be solid");
        assertTrue(tower.update, "The block must have an update logic");
    }

```

### Test 2 : Power Logic Verification
```java
    @Test
    @DisplayName("Power Logic: Must return true if efficiency is <= 0")
    void testIsNotPoweredLogic() throws Exception {
        // Access private method isNotPowered() via Reflection
        Method isNotPoweredMethod = SupportBuffBuild.class.getDeclaredMethod("isNotPowered");
        isNotPoweredMethod.setAccessible(true);

        // Case 1: No power (efficiency = 0)
        build.efficiency = 0f;
        boolean resultNoPower = (boolean) isNotPoweredMethod.invoke(build);
        assertTrue(resultNoPower, "Must return true when efficiency is 0");

        // Case 2: With power (efficiency = 1)
        build.efficiency = 1f;
        boolean resultHasPower = (boolean) isNotPoweredMethod.invoke(build);
        assertFalse(resultHasPower, "Must return false when efficiency is 1");
    }
```
This test verifies the internal power-validation logic of `SupportBuffBuild` by invoking the private `isNotPowered() method through reflection. It checks that the method correctly returns true when efficiency is zero (no power) and false when efficiency is positive (powered).
### Test 3 : Buildup Progress Verification
```java
    @Test
    @DisplayName("3. Buildup Progress: Should accumulate pulseTimer when powered and stop when unpowered")
    void testBuildupProgress() throws Exception {
        // Accesses the private pulseTimer field via Reflection to monitor progress
        Field pulseTimerField = SupportBuffBuild.class.getDeclaredField("pulseTimer");
        pulseTimerField.setAccessible(true);

        // Test pulse when tower is powered
        build.efficiency = 1f;
        pulseTimerField.set(build, 0.5f); // Set initial pulseTimer state
        float initialPulseTimer = pulseTimerField.getFloat(build);

        // Simulate tile update
        build.updateTile();
        float pulseTimerAfterUpdate = pulseTimerField.getFloat(build);

        // Check if pulseTimer increased
        assertTrue(pulseTimerAfterUpdate > initialPulseTimer, "The accumulation 'pulseTimer' must increase when powered");

        // Test pulse when tower is unpowered
        build.efficiency = 0f;
        pulseTimerField.set(build, 0.6f); // New initial pulseTimer state
        initialPulseTimer = pulseTimerField.getFloat(build);

        // Simulate update
        build.updateTile();
        float pulseTimerAfterNoPower = pulseTimerField.getFloat(build);

        // Check if pulseTimer did not change (because updateTile() returns immediately if not powered)
        assertEquals(initialPulseTimer, pulseTimerAfterNoPower, 0.001f, "The accumulation 'pulseTimer' must not change when unpowered");
    }
```
This test validates the buildup progression of `SupportBuffBuild` by inspecting the private *pulseTimer* field via *reflection*. It ensures that pulseTimer increases during `updateTile()` when the build is powered, 
and remains unchanged when unpowered, confirming correct pulse accumulation behavior.

### Test 4: Power Consumption Configuration
```java
    @Test
    @DisplayName("Power Consumption: Values configured correctly")
    void testPowerConsumptionConfig() {
        // Verify if consumption was configured (exact value is in consPower.capacity or usage)
        // Since Mindustry uses a complex consumer system, we verify if it exists
        assertNotNull(tower.consumers, "Consumer list must not be null");
        assertTrue(tower.hasConsumers, "Block must have registered consumers");
    }
```
This test checks that the block’s power consumption system is properly configured. 
It verifies that the `consumers` list is initialized and that the block correctly reports having registered 
power consumers.

> NOTE: We also made manual tests in the game to verify the correct functionality of the tower.
### Review
*(Please add your test specification review here)*

### Commits:

| Description                                                                                       | ID | Author |
|:--------------------------------------------------------------------------------------------------|:-----|:-------|
 Support Tower Implementation                                                                      |375796f0431aaf6cb07b04a4a3bcbd15a9d7cc82 | Filipe Nobre 67850
| Fixed bug on damageMultiplier                                                                     | 77cff7f85a0b5a70e2a0ae8a00cfab68cdd38f93 | Dinis Raleiras 67819
| Added the documentation to the User Storie 2.                                                     | 1f755ce15ec0254971d1eeb7d307aaa9bcbb46b2 | Dinis Raleiras 67819
| Added Portuguese Bundle                                                                           | dd34f0f7a932389fce25bfed3c273f34b7cc14b1 | Filipe Nobre 67850
| Commented SupportBuffTower class                                                                  | 4ae7b28eee705632bc45f0014185a0c523600d1c | Dinis Raleiras 67819
| Added auxiliar methods to avoid confuse and duplicated code                                       | bfc9885382a5b8b8958ff8885fa6e3be5e680e13 | Filipe Nobre 67850
| Comented the new methods in SupportBuffTower class                                                | 9fff5b1a087be757440059abe2aacf79fbeedd9a | Dinis Raleiras 67819
| Added Test Class with initial tower setup                                                         | a2355b8cb2d325216ea86bb3ac16771777181ea8 | Filipe Nobre 67850
| Added the second Unit test for the User Story 2 - Milestone 3                                     | b1823d7870d03f9e205290d87bbf99793e2cb35f | Dinis Raleiras 67819
| Added a test for the pulse update                                                                 | 2b636b5561534ca7a19fa81a26be1798c5c0ee0e | Filipe Nobre 67850
| Added test # 4 to the US 2 - Milestone 3 and minor fix to SupportBuffTower class found by testing | 006d592b8c3ef9ec393464f065481398c91ffcd9 | Dinis Raleiras 67819
