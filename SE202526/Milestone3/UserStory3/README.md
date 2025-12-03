# User story 3

Improved game introduction experience

---

## Author(s)
- Francisco Rodrigues (67753)
- Miguel Rosalino (68210)

---

## Reviewer(s)
- Leandro Rodrigues (68211)
- Luís Muacho (68301)
---

## User Story:
As a new player to Mindustry I want a clear, step-by-step tutorial that introduces the game functionalities in an interactive and intuitive way, so that I can know where and how I can build, research and defend the base.

---

## Review:

This User Story seems well done, as it is focused on the player (the actor) and specifies objectives such as learning how to build, research, and defend your base.
The only point to improve is that "step-by-step tutorial" is too vague; it could be made more specific, for example: "in-game tutorial," "pop-ups," or "mini-missions."

---

## Use case diagram

<img width="1196" height="783" alt="image" src="https://github.com/user-attachments/assets/4880777f-3245-4306-9aba-0d488143e7af" />




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
1. The use case starts when the new player clicks on the tutorial.
2. The system starts the tutorial world.
3. The system starts the basic tutorial.
   3.1 `Include`: Basic tutorial.
4. The system starts the research tutorial.
   4.1 `Include`: Research tutorial.
5. The system starts the building tutorial.
   5.1 `Include`: Building tutorial.
6. The system starts the defense tutorial.
   6.1 `Include`: Defense tutorial.
7. The system shows a pop-up message "Tutorial completed".
8. The system returns to the real world.
9. The use case ends.
   
**Postconditions:**
None

**Alternative flows:**
The new player cancels the tutorial.
(can be activated any time)
1. System starts the `Tutorial Cancelled` use case.



## Use Case: Basic Tutorial

**ID:** 2

**Brief introduction:**
Teach new player how to mine, and gather resources.

**Primary actor:**
New player

**Secondary actors:** 
None

**Preconditions:** 
Player in tutorial world

**Main flow:**
1. Use case starts when the new player enters the tutorial world.
2. System highlights the closest resources.
3. System shows a pop-up message explaining how to mine.
4. The new player select the highlighted area and starts gathering resources.
5. The system shows a pop-up message with gathering successful.
6. The use case ends.

**Postconditions:**
Resources are added to loadout of the new player.

**Alternative flows:**
The new player cancels the tutorial.
(can be activated any time)
1. System starts the `Tutorial Cancelled` use case.

   


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
1. Use case starts when the basic tutorial ends.
2. System highlights Research Tree button on the bottom right interface.
3. Player selects Research Tree menu.
4. System highlights the menu structure and displays an introduction message about the Tree System.
5. System highlights the path to the node the player requires to learn in order to proceed with the tutorial.
6. The player selects the highlighted node.
7. System checks that player has enough resources to complete research.
8. System unlocks the technology and notifies the player.
9. System explains in simple terms what that node is used for.
10. System explains the concept of resource costs and dependencies for learning and building.
11. The use case ends.

**Postconditions:**
Technology becomes unlocked for the player.

**Alternative flows:**

A1 - Not enough resources (can be activated in 7)
    
1. System informs the player that they don't have enough resources yet.
2. System starts `Basic Tutorial`.
3. Use case resumes at main flow step 7 when player gets enough resources.

The new player cancels the tutorial.
(can be activated any time)
1. System starts the `Tutorial Cancelled` use case.





## Use Case: Building tutorial
**ID:** 4

**Brief description:**
Tutorial showing how to build.

**Primary actors:**
New Player 

**Secondary actors:**
None

**Preconditions:**
New player is in the tutorial world.

**Main flow:**
1. The use case starts when the research tutorial ends.
2. The system shows a pop-up message explaining how to build.
3. The new player clicks on a building.
4. The system highlights the area where it can be built.
5. The new player chooses a place to build and clicks on it.
6. The system shows the factory on the position chosen.
7. While the position chosen is invalid:
7.1 The system shows a pop-up message "Invalid area" with specified error message.
7.2 The system changes the color of the factory to red.
8. The player clicks on the verify button.
9. The system shows a pop-up message "Not enough resources".
10. The system alternates the color of the resources between red and white.
11. The system adds enough resources.
12. The system builds a factory on the position chosen.
13. The use case ends.
    
**Postconditions**
1. Factory built.
   
**Alternative flows:**
The new player cancels the tutorial.
(can be activated any time)
1. System starts the `Tutorial Cancelled` use case.




## Use Case: Defense Tutorial

**ID:** 5

**Brief description:**
Teach the player how to defend their base by building a turret, adding basic walls for protection, and supplying ammo before a wave arrives.
Introduce wall placement and enemy path awareness.

**Primary actors:**
Player

**Secondary actors:**
Enemy Spawner

**Preconditions:**
The player is in tutorial world.

**Main flow:**
1. Use case starts when the building tutorial ends.
2. System warms of incoming enemies.
3. System informs player of the use of turrets.
4. Camera moves to suggested turret placement location.
5. System highlights a recommended turret location spot.
7. Player selects a turret from the menu.
8. Player places the turret in the highlighted location.
9. System highlights a short wall segment in front of the turret.
10. System tells player the use of walls.
11. Player places basic walls in the highlighted area.
12. System explains how ammo works.
13. System informs player on how to load ammo into turrets.
14. System highlights the turret.
15. Player supplies the turret with ammo using conveyors.
16. Enemy wave arrives.
17. Camera follows turret actions.
18. Turret fires and the walls absorb damage.
19. System notifies the player that they have successfully defended the base.
20. The use case ends.

**Postconditions:**
The enemy wave is defeated.

**Alternative flows**:

A1 - Player places turret incorrectly (can be activated in 8):

1. System highlights the correct location again.
2. Use case resumes at Main Flow step 8.

The new player cancels the tutorial.
(can be activated any time)
1. System starts the `Tutorial Cancelled` use case.



## Use Case: Tutorial cancelled
**ID:** 6

**Brief description:**
The new player cancelled the tutorial.

**Primary actors:**
New Player 

**Secondary actors:**
None

**Preconditions:**
New player clicked on quit and the player is in the tutorial world.

**Main flow:**
1. The alternative flow can start anywhere.
2. The system returns to main menu.
3. The use case ends.

**Postconditions:**
1. The player is back to the real world
   
**Alternative flows:**

None



### Review
The use case descriptions are well-written, with clear actors, objectives, detailed flows, and inclusion of sub-use-cases.
Postconditions could be improved by specifying the player’s status and the final state of the map. 
Overall, the descriptions are of high quality.
The diagram effectively represents the use cases, correctly identifying the key actors and the different tutorial modules .



## Implementation documentation
(*Please add the class diagram(s) illustrating your code evolution, along with a technical description of the changes made by your team. The description may include code snippets if adequate.*)

**Classes updated so far:**

`mindustry/ui/Tutorial.java` - context class NEW

`mindustry/ui/TutorialState.java` - state interface NEW

`mindustry/ui/TutorialWelcomeState.java` - uc1 state **NEW**

`mindustry/ui/TutorialBuildingState.java` - uc2 state **NEW**

`mindustry/ui/TutorialResearchState.java` - uc3 state **NEW**

`mindustry/ui/TutorialDefenseState.java` - uc4 state **NEW**

`mindustry/ui/fragments/MenuFragment.java` - for adding the tutorial button on main menu

`mindustry/core/UI.java` - for adding tutorial to init

`mindustry/content/Planets.java` - added tutorial planet

`mindustry/core/ContentLoader.java` - added checks for tutorial planet, not drawing button and label in planet menu, load tutorialTechTree

`mindustry/ui/dialogs/ResearchDialog.java` - hiding switch reseratchTree UI for tutorial

`mindustry/content/SectorPresets.java` - added tutorial preset

`mindustry/content/TutorialTechTree.java` - new small tech tree just for tutorial world **NEW**

`mindustry/content/Blocks.java` - added tutorial version of mechanicalDrill, duo, conveyor, copperWall, coreShard

- added sprite files

`mindustry/ui/dialogs/PausedDialog.java` - changed menu ui
### Implementation summary
Our implementation is a Tutorial, which the user can choose to start in the initial menu. When we started the implementation we came across multiple complications. Half of the time needed to implement our tutorial was spent on preparing the world for it. After a few tries we understood than we needed to create a new world, otherwise the sector would appear on the campaign map, on the respective world. For the same reason a new sector was needed, for this new world. A new sector means a new map, so we made a new map too, with the properties we thought relevant. Afterwards some messages and user options were not adequated for our implementation, so that too we needed to dig up and find where it was being done. Even the quit menu (when pressed escape in the tutorial) was modified to our interest, for example, we didn't want it to be able to save the state of the tutorial once exited. Some buttons were also disable like the planet icon on the lower right corner, and the option to change the world's techtree (in the research menu). When we finally were going to start with our implementation, we discovered that modifying the research tree would change in it's world to, so we made our own techtree with our own blocks, with their own images, because every block points to the same, it's not possible to instantiate.
Then the real implementation start, one class that manages the tutorial and multiple tutorial states. The `Tutorial` manages when the multiple states start and finish, and it is the one that comunicates with the other classes.
The `TutorialBasicState` handles the mining of the resources. It explains the user how to gather resources, and marks itself as complete when the user has enough resources to complete the researches.
The `TutorialResearchState` explains to the user how to research blocks. When all 4 blocks are researched, it passes on to the next state.
The `TutorialBuildingState` shows where to build a drill and how it can be transported to the core. This part caused also some struggle as the algorithm to calculate the path to the core wasn´t the easiest. This state ends when the drill and the conveyer have been built in the indicated places. 


#### Review
*(Please add your implementation summary review here)*

### Class diagrams
<img width="2296" height="1127" alt="image" src="https://github.com/user-attachments/assets/9e964cd0-c7d1-407f-b26c-dd7f0b3b2342" />
Despite the large number of classes we changed/edited, we choose to only show the most important ones, that we build from scratch.
We used the state pattern to implement our user story as we though that it would be the perfect case. 
We have a Tutorial class, which is the class that manages the tutorial, and which the other classes in the game can interact with. It stores the order of the multiple states of the tutorial, and has the power to start and end each state.
There is a TutorialState Interface, which has the common methods between the multiple states: enter, update, exit and setContext. All 4 state classes use this interface with their additional logic methods, and have a context variable, that stores the Tutorial. The TutorialResearchState also uses the TutorialTechTree to manage it's state. The TutorialDefenseState also has it's own enum, Step, with the various steps that the tutorial goes through.

### Review
*(Please add your class diagram review here)*

### Sequence diagrams
## Game tutorial
![GAME TUTORIAL](https://github.com/user-attachments/assets/787e38ea-5f65-41be-9e18-04a493cd152c)

Main controller for the tutorial experience. Initializes the tutorial world and then executes the four sub-tutorials (Basic, Research, Building, Defense) in a specific order using ref freames. It uses an alt fragment to handle the player cancelling the tutorial at any time versus completing it successfully.

## Basic tutorial
<img width="882" height="636" alt="BASIC TUTORIAL" src="https://github.com/user-attachments/assets/fdff311e-0d16-4a56-9f06-d71ee7186274" />

Details the process of gathering resources. The loop represents the updates every frame. Inside the loop, the controller continuously instructs the renderer to highlight the closest copper ore. The loop continues until the core reports that the required amount of copper has been collected, moving to the next state.

## Research tutorial
<img width="1140" height="942" alt="RESEARCH TUTORIAL" src="https://github.com/user-attachments/assets/3b530dc0-50e3-42a8-977d-e333511615ed" />

Focus on UI explanation. Models the sequence of the player opening the UI, navigating the Research Dialog, and unlocking technology. It uses an alt fragment to check resource costs: if the player lacks resources, the system shows an error; if they have enough, the system removes items from the core and unlocks the Tech Node.

## Building tutorial
<img width="1515" height="1304" alt="BUILDING TUTORIAL" src="https://github.com/user-attachments/assets/25b0489b-9ec3-4329-b373-54c3d6bb7f9f" />

Models the placement of drills and conveyors. Highlights and checks valid placement.

## Defense tutorial
<img width="1467" height="1798" alt="DEFENSE TUTORIAL" src="https://github.com/user-attachments/assets/22131783-55eb-41d9-bdd3-b711e1a2412a" />

Models defending enemy waves and using turrets. It introduces a secondary actor (Enemy Spawner) that creates Enemy Units. The final section uses a combat loop that represents the turrets destroying enemies.

## Cancel tutorial
<img width="847" height="356" alt="CANCEL TUTORIAL" src="https://github.com/user-attachments/assets/c69f1e35-fe1e-4a92-9874-bbe1405b2443" />

Termination sequence when a player clicks "Quit". It shows the controller performing cleanup like calling exit() on the active state to remove floating arrows or highlights before instructing the Game State to load the Main Menu and return the player to the real world.


#### Review
*(Please add your sequence diagram review here)*

## Test specifications
All the videos corresponding to each test can be found in a folder called `Tests`, in the same folder as this file.

### Basic Tutorial
`Test 1`:
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Leave the tutorial.
   4. Enter again to check if everything resetted. (0 copper)

`Test 2`:
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Wait until you have 85 copper.
   4. Check if a message appeared indicating the next state.

`Test 3`:
   1. Enter the tutorial.
   2. Click on a lead tile.
   3. Wait until you have 85 lead.
   4. Check if no message appeared indicating the next state.
   5. Click on a copper tile.
   6. Wait until you have 85 copper.
   7. Check if a message appeared indicating the next state.

`Test 4`: 
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Go to research tree.
   4. Research the mechanical drill. (costs 10 copper)
   5. Wait until you have 75 copper.
   6. Check if a message appeared indicating the next state.

`Test 5`:
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Wait until you have _ copper.
   4. Go to research tree.
   5. Research the mechanical drill. (costs 10 copper)
   6. Research the conveyer. (costs 5 copper)
   7. Research the copper wall. (costs 20 copper)
   8. Spend _ copper on researching duo. (costs 50)
   9. Wait until you have _ copper.
   10. Check if a message appeared indicating the next state.

`Test 6`:
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Go to research tree.
   4. Research the mechanical drill. (costs 10 copper)
   5. Build a mechanical drill. (costs 12 copper)
   6. Wait until you have 63 copper.
   7. Check if no message appeared indicating the next state.
   8. Wait until you have 75 copper.
   9. Check if a message appeared indicating the next state.

### Research Tutorial
`Test 1`:
   1. Do `Test 2` from `Basic Tutorial`.
   2. Go to the research tree.
   3. Research the copper wall and the conveyer.
   4. Check if no message appeared indicating the next state.
   5. Research copper the mechanical drill and duo.
   6. Check if a message appeared indicating the next state.

`Test 2`:
   1. Do `Test 2` from `Basic Tutorial`.
   2. Go to the research tree.
   3. Research the mechanical drill and the conveyer.
   4. Leave the tutorial.
   5. Enter the tutorial.
   6. Go to the research tree.
   7. Check if everything is locked.

`Test 3`:
   1. Do `Test 5` from `Basic Tutorial`.
   2. Go to the research tree.
   3. Research duo.
   4. Check if a message appeared indicating the next state.

`Test 4`: 
   1. Do `Test 6` from `Basic Tutorial`.
   2. Go to the research tree.
   3. Research the 3 blocks left.
   4. Check if a message appeared indicating the next state.

### Building Tutorial
`Test 1`:
   1. Do `Test 1` from `Research Tutorial`.
   2. Close the research tree.
   3. Check if appeared a highlight indicating where to build the drill.
   4. Build a mechanical drill in the indicated place.
   5. Check if the the highlight indicating where to build the drill disappeared.
   6. Check if appeared a highlight indicating where to build the conveyers.
   7. Build the conveyers on the indicated place.
   8. Check if the the highlight indicating where to build the conveyers disappeared.
   9. Check if a message appeared indicating the next state.

`Test 2`:
   1. Do `Test 1` from `Research Tutorial`.
   2. Close the research tree.
   3. Check if appeared a highlight indicating where to build the drill.
   4. Build a mechanical drill in the with half of it in the indicated place.
   5. Check if the highlight indicating where to build the drill didn't disappear.
   6. Check if the highlight indicating where to build the conveyers didn't appear.
   7. Build a mechanical drill in the with half of it in the other half of the indicated place.
   8. Check if the highlight indicating where to build the drill didn't disappear.
   9. Check if the highlight indicating where to build the conveyers didn't appear.
   10. Remove the 2 mechanical drills.
   11. Build a mechanical drill in the indicated place.
   12. Check if the highlight indicating where to build the drill disappeared.
   13. Check if appeared a highlight indicating where to build the conveyers.
   14. Build the conveyers  one by one on the indicated place, verifying that the highlight doesn't disappear.
   15. Check if the highlight indicating where to build the conveyers disappeared.
   16. Check if a message appeared indicating the next state.

`Test 3`:
   1. Enter the tutorial.
   2. Click on a copper tile.
   3. Go to research tree.
   4. Research the mechanical drill. (costs 10 copper)
   5. Build a mechanical drill on the highlighted copper ore. (costs 12 copper)
   6. Wait until you have 75 copper.
   7. Check if a message appeared indicating the next state.
   8. Go to the research tree.
   9. Research the 3 blocks left.
   10. Check if a message appeared indicating the next state.
   11. Close the research tree.
   12. Check if the indicated place to build the mechanical drill are copper tiles.
   13. Build a mechanical drill in the with half of it in the indicated place.
   14. Check if the highlight indicating where to build the drill didn't disappear.
   15. Check if the highlight indicating where to build the conveyers didn't appear.
   16. Remove the mechanical drill just built.
   17. Build a mechanical drill in the indicated place.
   18. Check if the highlight indicating where to build the drill disappeared.
   19. Check if appeared a highlight indicating where to build the conveyers.
   20. Build the conveyers  one by one on the indicated place, verifying that the highlight doesn't disappear.
   21. Check if the highlight indicating where to build the conveyers disappeared.
   22. Check if a message appeared indicating the next state.

`Test 4`: 
   1. Do `Test 1` from `Research Tutorial`.
   2. Close the research tree.
   3. Check if appeared a highlight indicating where to build the drill.
   4. Leave the tutorial.
   5. Check that the highlight disappeared.
   6. Redo `Test 1` from `Research Tutorial`.
   7. Check that the highlight appeared again.
   8. Build the mechanical drill on the indicated place.
   9. Check if the highlight indicating where to build the drill disappeared.
   10. Check if appeared a highlight indicating where to build the conveyers.
   11. Leave the tutorial.
   12. Check that the highlight disappeared.
   13. Redo `Test 1` from `Research Tutorial`.
   14. Build the mechanical drill on the indicated place.
   15. Check if the highlight indicating where to build the drill disappeared.
   16. Build the conveyers  one by one on the indicated place, verifying that the highlight doesn't disappear.
   17. Check if the highlight indicating where to build the conveyers disappeared.
   18. Check if a message appeared indicating the next state.

### Review
*(Please add your test specification review here)*
