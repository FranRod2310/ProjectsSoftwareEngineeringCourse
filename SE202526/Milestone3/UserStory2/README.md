# User story 2
### New Turret Defense Mechanic
## Author(s)
 - Dinis Raleiras (67819)
 - Filipe Nobre (67850)
## Reviewer(s)
(*Please add the user story reviewer(s) here, one in each line, providing the authors' name and surname, along with their student number. In the reviews presented in this document, add the corresponding reviewers.*)
## User Story:
*As an experienced player I want to have a greater diversity of towers so that it can help me automate the way I defend waves*
### Review
*(Please add your user story review here)*
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
*(Please add your use case review here)*
## Implementation documentation
(*Please add the class diagram(s) illustrating your code evolution, along with a technical description of the changes made by your team. The description may include code snippets if adequate.*)
### Implementation summary
(*Summary description of the implementation.*)
#### Review
*(Please add your implementation summary review here)*
### Class diagrams
(*Class diagrams and their discussion in natural language.*)
### Review
*(Please add your class diagram review here)*
### Sequence diagrams
(*Sequence diagrams and their discussion in natural language.*)
#### Review
*(Please add your sequence diagram review here)*
## Test specifications
(*Test cases specification and pointers to their implementation, where adequate.*)
### Review
*(Please add your test specification review here)*
