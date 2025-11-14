# User story 3

Improved game introduction experience

---

## Author(s)
- Francisco Rodrigues (67753)
- Miguel Rosalino (68210)

---

## Reviewer(s)
(*Please add the user story reviewer(s) here, one in each line, providing the authors' name and surname, along with their student number. In the reviews presented in this document, add the corresponding reviewers.*)

---

## User Story:
As a new player to Mindustry I want a clear, step-by-step tutorial that introduces the game functionalities in an interactive and intuitive way, so that I can know where and how I can build, research and defend the base.

---

### Review
*(Please add your user story review here)*

---

## Use case diagram

<img width="1043" height="548" alt="image" src="https://github.com/user-attachments/assets/26f1b7fa-1ad5-4b55-941b-605d18c51fd2" />


---

## Use case textual description

## Use Case: Game tutorial
**ID**: 1

**Brief description:**
Start a tutorial explaining how building, researching and defending works.

**Primary actors:**
New Player

**Secondary actors:**
None

**Preconditions:**
None

**Main flow:**
1. The use case starts when the new player clicks on the help icon.
2. The system starts the tutorial world.
3. The system starts the building tutorial.
3.1 `Include`: Building tutorial.
4. The system starts the defense tutorial.
4.1 `Include`: Defense tutorial.
5. The system shows a pop-up message "Tutorial completed".
6. The system returns to the real world.
7. The use case ends.
   
**Postconditions:**
None

**Alternative flows:**
The new player cancels the tutorial.

## Use Case: Building tutorial
**ID:** 2

**Brief description:**
Tutorial showing how to build.

**Primary actors:**
New Player 

**Secondary actors:**
None

**Preconditions:**
New player is in the tutorial world.

**Main flow:**
1. The use case starts when the new player arrives at the tutorial world.
2. The system shows a pop-up message explaining what to do.
3. The player clicks on the research tree icon.
4. The system starts the research tutorial.
4.1 `Include`: Research Tutorial.
5. The system shows a pop-up message explaining how to build.
6. The new player clicks on a building.
7. The system highlights the area where it can be built.
8. The new player chooses a place to build and clicks on it.
9. The system shows the factory on the position chosen.
10. While the position chosen is invalid:
10.1 The system shows a pop-up message "Invalid area" with specified error message.
10.2 The system changes the color of the factory to red.
11. The player clicks on the verify button.
12. The system shows a pop-up message "Not enough resources".
13. The system alternates the color of the resources between red and white.
14. The system adds enough resources.
15. The system builds a factory on the position chosen.
16. The use case ends.
    
**Postconditions**
1. Factory built.
   
**Alternative flows:**

The new player cancels the tutorial.

## Use Case: Research Tutorial

**ID:** 3

**Brief introduction:**
Teach new player how to navigate, understand and unlock items in the Research Tree menu.

**Primary actor:**
New player

**Secondary actors:** 
None

**Preconditions:** 
Player in tutorial world

**Main flow:**
1. Use case starts when the player opens Research Tree menu.
2. System highlights Research Tree button on the bottom right interface.
3. Player selects Research Tree menu.
4. System highlights the menu structure and displays an introduction message about the Tree System.
5. System highlights the path to the node the player requires to learn in order to proceed with the tutorial.
6. The player selects the highlighted node.
7. System explains in simple terms what that node is used for.
8. System explains the concept of resource costs and dependencies for learning and building.
9. System highlights "Research" button.
10. Player presses "Research".
11. System checks that player has enough resources to complete research.
12. System unlocks the technology and notifies the player.

**Postconditions:**
Technology becomes unlocked for the player.


**Alternative flows:**

A1 - Not enough resources
    
1. System informs the player that they don't have enough resources yet.
2. System tells the player how to gather resources.
3. Use case resumes at main flow step 7 when player gets enough resources.

A2 - Player closes menu too soon
    
1. System pauses tutorial.
2. When player opens menu, resume main flow from last incomplete step.

## Use Case: Defense Tutorial

**ID:** 4

**Brief description:**
Teach the player how to defend their base by building a turret, adding basic walls for protection, and supplying ammo before a wave arrives.
Introduce wall placement and enemy path awareness.

**Primary actors:**
Player

**Secondary actors:**
Enemy Spawner

**Preconditions:**
The player has unlocked basic ores for fuel and is in tutorial world.

**Main flow:**

1. Use case starts when the building tutorial ends.
2. System shows the path of incoming enemies.
3. System informs player of the use of turrets.
4. Camera moves to suggested turret placement location.
5. System highlights a recommended turret location spot.
6. Systems highlights Research Tree.
6.1 `Include`: Research tutorial
7. Player selects and unlocks a turret from the menu.
8. Player places the turret in the highlighted location.
9. System highlights a short wall segment in front of the turret.
10. System highlights wall item location in the menus.
11. Player places basic walls in the highlighted area.
12. System highlights the ammo type required by the turret.
13. System informs player on how to load ammo into turrets.
14. System highlights a suggested path for conveyor placement into turrets.
15. Player supplies the turret with ammo using conveyors.
16. Camera moves to enemy spawn location.
17. Enemy wave arrives.
18. Camera follows turret actions.
19. Turret fires and the walls absorb damage.
20. System notifies the player that they have successfully defended the base.

**Postconditions:**
The enemy wave is defeated.

**Alternative flows**:

A1 - Player places turret incorrectly:

1. System highlights the correct location again.
2. Use case resumes at Main Flow step 8.

A2 - Turret receives no ammo:

1. The system highlights the ammo source and the turret’s ammo bar.
2. The use case resumes at Main Flow step 15.



## Use Case: Tutorial cancelled
**ID:** 5

**Brief description:**
The new player cancelled the tutorial.

**Primary actors:**
New Player 

**Secondary actors:**
None

**Preconditions:**
New player clicked on cancel and the player is in the tutorial world.

**Main flow:**
1. The alternative flow can start anywhere.
2. The system shows a pop-up message "Tutorial ended".
3. The system returns to the real world.
4. The use case ends.

**Postconditions:**
1. The player is back to the real world
   
**Alternative flows:**

None

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
