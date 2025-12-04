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
**Classes updated/created:**
Every time we added something to a class we commented with "//US3" except the classes that we added. (By using the universal search and typing "//US3", the modified classes will be easy to find).

`mindustry/ui/Tutorial.java` - context class **NEW**

`mindustry/ui/TutorialState.java` - state interface **NEW**

`mindustry/ui/TutorialBasicState.java` - uc1 state **NEW**

`mindustry/ui/TutorialBuildingState.java` - uc3 state **NEW**

`mindustry/ui/TutorialResearchState.java` - uc2 state **NEW**

`mindustry/ui/TutorialDefenseState.java` - uc4 state **NEW**

`mindustry/content/TutorialTechTree.java` - new small tech tree just for tutorial world **NEW**

`mindustry/ui/fragments/MenuFragment.java` - for adding the tutorial button on main menu

`mindustry/core/UI.java` - for adding tutorial to init

`mindustry/content/Planets.java` - added tutorial planet

`mindustry/core/ContentLoader.java` - load tutorialTechTree

`mindustry/ui/dialogs/ResearchDialog.java` - hiding switch researchTree UI for tutorial, changing the color of the block buttons in researchTree for a highlight,  

`mindustry/content/SectorPresets.java` - added tutorial sector preset

`mindustry/content/Blocks.java` - added tutorial version of mechanicalDrill, duo, conveyor, copperWall, coreShard

- added sprite files for the blocks added

`mindustry/ui/dialogs/PausedDialog.java` - changed menu ui and guarantee that it doesn't save.

`mindustry/ui/dialogs/DatabaseDialog.java` - Hide tutorial items from the database

`mindustry/ui/fragments/HudFragment.java` - Remove the initial message "Sector captured"

`mindustry/world/blocks/storage/CoreBlock.java` - Notify the tutorial when the core is landed.

`mindustry/input/DesktopInput.java` - Removed the planet button while in the tutorial, so the user doesn't go to another world.

We didn't add any code snippet because otherwise this segment would become very long, so we added a brief explanation of each change. As it's possible to see, we have worked with many classes, making the time needed to implement our user story longer, but it also helped understanding better the project itself.   
  
### Implementation summary
Our implementation is a new **Tutorial**, which the user can choose to start from the initial game menu. 

When we started the implementation we came across multiple complications. Half of the time needed to implement our tutorial was spent on preparing the world for it. After a few tries we understood than we needed to create a new world, otherwise the sector would appear on the campaign map, on the respective world. For the same reason a new sector was needed for our tutorial. A new sector means a new map, so we made a new map too, with the properties we thought relevant. 

Afterwards some messages and user options were not adequate for our implementation, so that too we needed to dig up and find where it was being done. Even the quit menu (when pressed escape in the tutorial) was modified to our interest, for example, we didn't want it to be able to save the state of the tutorial once exited. We also disabled some buttons like the planet icon on the lower right corner, and the option to change the worlds TechTree (in the research menu). 

When we were finally going to start with our implementation, we discovered that modifying the research tree would also affect it in it's orignal world too. So, we made our own new techtree with our own blocks, with their own images. Since every tech node points to the same block, it's not possible to instantiate.


Then, the real implementation started. One class that manages the tutorial and multiple tutorial states. The `Tutorial` manages when the multiple states start and finish, and is the one that comunicates with the other classes.

The `TutorialBasicState` handles the mining of resources. It explains the user how to gather resources, and marks itself as complete when the user has enough resources to complete the researches.

The `TutorialResearchState` explains to the user how to research blocks. When all 4 available blocks are researched, it passes on to the next state.

The `TutorialBuildingState` shows where to build a drill and how it can be transported to the core. This part caused also some struggle as the algorithm to calculate the path to the core wasn´t the easiest. This state ends when the drill and the conveyer have been built in the indicated places. 

Finally, the `TutorialDefenseState` serves as an introduction to enemies and how to defend the base against attacks. It begins by explaining the use of turrets to the player, and then building one. After a turret is correctly placed, the tutorial then explains building walls for defense. After placing 5 walls, we move on to explaining the concept of ammo. The player is then asked to place Conveyors into the highlighted turret, supplying it with ammo. When enough ammo is supplied, the tutorial begins the enemy wave. Two simple enemies spawn, and the player watches the turret defend the base. With both enemies defeated, the tutorial is complete.

The Defense tutorial was particularly challenging to implement due to problems with spawning the enemies. At first, the enemies kept dying instantly when spawned, and it took a long time diving into the enemy logic to understand the problem. Our new sector had to update it's rules to allow conditions that permit enemy spawning. Also, the unit type and specific health pools were fine-tuned after many tests to provide the most realistic combat experience in the tutorial, using only one turret.



#### Review
*(Please add your implementation summary review here)*

### Class diagrams
<img width="2296" height="1127" alt="image" src="https://github.com/user-attachments/assets/9e964cd0-c7d1-407f-b26c-dd7f0b3b2342" />

Despite the large number of classes we changed/edited, we chose to only show the most important ones that we built from scratch. We used the State design pattern to implement our user story as we thought that it would be the perfect case.

We have a `Tutorial` class, which is the class that manages the tutorial, and which the other classes in the game can interact with. It stores the order of the multiple states of the tutorial, and has the power to start and end each state.

There is a `TutorialState` Interface, which has the common methods between the multiple states: `enter()`, `update()`, `exit()` and `setContext()`. All 4 state classes use this interface with their additional logic methods, and have a `context` variable that stores the Tutorial. 

The `TutorialResearchState` also uses the `TutorialTechTree` to manage it's state. The `TutorialDefenseState` also has it's own enum, `Step`, with the various steps that the tutorial goes through.

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
All the videos corresponding to the tests can be found in the link mentioned in eacht tutorial state. All the videos are in 2x speed.
We decided not to make unit tests and instead make them in videos, because our tests depend very much on what the user does, and that would be difficult to test using unit tests.  

### Basic Tutorial
https://youtu.be/w6NLhfivpiI

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
   3. Wait until you have 48 copper.
   4. Go to research tree.
   5. Research the mechanical drill. (costs 10 copper)
   6. Research the conveyer. (costs 5 copper)
   7. Research the copper wall. (costs 20 copper)
   8. Spend 13 copper on researching duo. (costs 50)
   9. Wait until you have 37 copper.
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
https://youtu.be/Op-GLzRgD38

`Test 1`:
   1. Do `Test 2` from `Basic Tutorial`.
   2. Go to the research tree.
   3. Research the copper wall and the conveyer.
   4. Check if no message appeared indicating the next state.
   5. Research the mechanical drill and duo.
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
https://youtu.be/c_JxVis2XMc

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


### Defense Tutorial
[https://www.youtube.com/watch?v=Wup_agyguAA&list=PL5TJMXb9bNplq7qxV_UZ3g34t7o1HH03_](https://youtube.com/playlist?list=PL5TJMXb9bNplq7qxV_UZ3g34t7o1HH03_&si=piAa3nC0yk9fFRes)

`Test 1`:
   1. Check if defense intro messages appear.
   2. Place duo turret in highlighted placement.
   3. Check if wall placement message appears.
   4. Place 5 walls in highlighted area.
   5. Check if ammo explanation message appears.
   6. Check if ammo count is working above turret.
   7. Place conveyors from drill into turret.
   8. Wait until ammo count check done.
   9. Check if enemy spawning message appears.
   10. Check if enemies spawn.
   11. Check if turret can eliminate both enemies before destroying base.
   12. Check if tutorial complete message appears after clearing wave.

`Test 2`:
   1. Place turret in another spot other than the highlight.
   2. Check if tutorial does not advance.
   3. Place turret in correct spot.
   4. Check if tutorial advances as intended.
   5. Place walls in spots other than highlighted ones.
   6. Check if tutorial does not advance.
   7. Place walls correctly.
   8. Check if tutorial advances as intended.

`Teste 3`:
   1. Place turret in highlithed spot.
   2. Check if tutorial advances as inteded.
   3. Quit the tutorial.
   4. Join tutorial.
   5. Check if turret and highlight has been removed aas intended.
   6. Advance until wall placement.
   7. Place walls in highlihted spot.
   8. Quit the tutorial.
   9. Join tutorial.
   10. Check if walls and hightlights have been removed as intended.

`Teste 4`:
   1. Proceed until enemies spawn.
   2. Move player ship into the enemies.
   3. Take damage from enemies.
   4. Get ship destroyed.
   5. Check if tutorial proceeds as intended.
   6. Respawn.
   7. Check if tutorial finished as intended.
       
### Review
*(Please add your test specification review here)*


### Commits:

| Description                                                                                       | ID | Author |
|:--------------------------------------------------------------------------------------------------|:-----|:-------|
 US3 - added tutorial button on the main menu|0dfc5a86e62a94a3800cd6f84df7dc10ecabc911 | Miguel Rosalino 68210 |
 US3 - created tutorial class in mindustry.ui, added tutorial var to core.ui class| 0c608f528f62438bc8b89e67bf36618fdc790743 | Miguel Rosalino 68210|
 US3 - created TutorialState interface and respective state classes.| 0e48320d13f93bef82fa57fe543ebc620d2c8ad5 | Miguel Rosalino 68210|
 changed tutorial to start after confirmed | f19db1fb5b5cb1ef55476c303fa5657ba869e56c | Francisco Rodrigues 67753|
 US3 - updated WelcomeState with copper ore location, removed original tutorial objectives, started BuildingTutorial | dc3417b4bf9e298e277bd7356a7686bf664f905e | Miguel Rosalino 68210|
 added a tutorial map | ba06163daeec349544a44a6a8d6a0195413cb066 | Francisco Rodrigues 67753|
 First draft tutorial with own world | fe8a37d40bb8aba0fa302b894ccd27a0a167d4f7 | Francisco Rodrigues 67753|
 ui planets for tutorial removed | 7af17e08b0e35ad3f58776d2903455ad0bf794ad | Francisco Rodrigues 67753|
 added tutorial research tree | 3410ad385e42c8374791181a289eab07c58dcc5f | Francisco Rodrigues 67753|
 tutorial conveyors 3-4 | 668d73c8cf43d732217913e1fd91a5430e75624c | Miguel Rosalino 68210|
 added tutorial sprite files | c2a748abea7ffc1006e762f6565485e746534c09 | Francisco Rodrigues 67753 |
 Merge remote-tracking branch 'origin/Tutorial' into Tutorial | f1fc431a518ff2e01cf37ea22a4671d6241e149c | Francisco Rodrigues 67753 |
 Add tutorial state and update UI for tutorial mode | 445b4fe80fdfa5ef1062533bdffeae31280f52d1 | Francisco Rodrigues 67753|
 fixed all problems, is ready to start tutorial | cef8bd890380861de6fa88d4c739b9931381edea | Francisco Rodrigues 67753|
 removed TechTree switch in tutorial | 527c0470bf58231b834ff5549643bbb5e3e661f8 | Francisco Rodrigues 67753|
 added base for changing tutorial states | 0249d7c05aaf7d51e01284433b8eb8ab60ba623a | Francisco Rodrigues 67753|
 Changed tutorial map and fixed some tutorial related bugs | e0bb8eb77f20475cc000d247e9610379a106b39e | Francisco Rodrigues 67753|
 basic tutorial done, added events fire and added initial conditions for Research and Building states | bbc4e201bfbeb9a64c680c663f984608adcdfb85 | Francisco Rodrigues 67753|
 implemented defense tutorial but need to fix enemy spawn bug... | 633a815080b9b67bffc69f9845d4a5c69048ba02 | Miguel Rosalino 68210|
 fixed enemy spawn in defense tutorial | 678f5113bd624208e03f536b6b65a166e28a7866 | Miguel Rosalino 68210|
 Added completed Research Tutorial | 1e81be8f69acd5552d6e9db9c0d7e1c443a25054 | Francisco Rodrigues 67753| 
 Merge remote-tracking branch 'origin/Tutorial' into Tutorial | 53220c1ba8f77d5306a0421b1f19d55082270550 | Francisco Rodrigues 67753| 
 Added completed Research Tutorial | 84c90078e8047035a94b5fdb0eb912361fd1865e | Francisco Rodrigues 67753|
 working version tutorial from basic to building | 07718748eeb332490224414721a5edabe370a961 | Francisco Rodrigues 67753| 
 defense tutorial refactured and functional | 72cd72356829a2de78fde968f2efca86629264c6 | Miguel Rosalino 68210| 
 enemy hp adjustment | c322d50b77a5234ee9b3dbeacfc893d2c63f5c47 | Miguel Rosalino 68210|
 fixed overlapping "sector cleared" text from appearing | 70e9af45fa357c43ee76fcb0610b030068069276 | Miguel Rosalino 68210| 
 added some comments and first draft of finding path build tutorial but still with bugs | fb4ca13df2d3f3841bb6fc9700bd288aecba54ba | Francisco Rodrigues 67753| 
 Merge remote-tracking branch 'origin/Tutorial' into Tutorial | a9c760090c4ffbe1ce6983b6402a41290c313fec | Francisco Rodrigues 67753| 
 Final changes to build tutorial | c31a037f1c6422023a945e8e186061026a7d77a7 | Francisco Rodrigues 67753|
 Merge remote-tracking branch 'refs/remotes/origin/Tutorial' | 51aa9666ec8d48e299ff92ba60abbbe6b8297e37 | Francisco Rodrigues 67753| 
 fixed minor bug in TutorialBuildingState, and added final comments | b335934b023723ae4461a89c2494d84cb0b6b418 | Francisco Rodrigues 67753| 
 Merge remote-tracking branch 'origin/main' | d9ccd85b1c17704a6873238ff773973f1fc6aebd | Francisco Rodrigues 67753| 
 fixed small bug in TutorialBuildingState | 8efd3d3ae41c5cdb6eb2008728b3e37c9c239835 | Francisco Rodrigues 67753| 
 Added //US3 comments to modified US3 codes | 736b753fb515335d8599eacf9f02937d88ca1958 | Francisco Rodrigues 67753
