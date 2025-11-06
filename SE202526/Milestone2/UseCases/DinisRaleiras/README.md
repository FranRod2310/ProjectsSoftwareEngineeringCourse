# Use cases

##  Mindustry – General Gameplay Use Case Model

This unified use case model represents the main gameplay interactions between the player and the core systems of the Mindustry engine.  
All use cases are interconnected as part of the same flow —managing structures to saving/loading progress and controlling units.

---

### **Use Case Name:** Place Block
**Description:** The player places a selected block (e.g., conveyor, turret, drill) in the world at a valid location.  
**Primary Actor:** Player  
**Secondary Actors:** World System  
**Related Use Cases:** Remove Block, Link Blocks, Build Schematic

---

### **Use Case Name:** Remove Block
**Description:** The player removes an existing block from the world, freeing space or recovering resources.  
**Primary Actor:** Player  
**Secondary Actors:** World System  
**Related Use Cases:** Place Block, Build Schematic

---

### **Use Case Name:** Link Blocks
**Description:** The player connects compatible blocks (e.g., conveyor → container, power node → generator) to establish a flow of resources.  
**Primary Actor:** Player  
**Secondary Actors:** World System, Block System

---

### **Use Case Name:** Save Game
**Description:** The player saves the current game state (map, units, buildings, resources) to a file.  
**Primary Actor:** Player  
**Secondary Actors:** Save System, Game System

---

### **Use Case Name:** Load Game
**Description:** The player loads a previously saved game, restoring world and progress.  
**Primary Actor:** Player  
**Secondary Actors:** Save System, Game System

---

## 🧩 **Use Case Summary Table**

| Use Case Name | Description | Primary Actor | Secondary Actors | Related Use Cases |
|----------------|-------------|----------------|------------------|------------------|
| Place Block | The player places a selected block (e.g., conveyor, turret, drill) in the world at a valid location. | Player | World System | Remove Block, Link Blocks, Build Schematic |
| Remove Block | The player removes an existing block from the world, freeing space or recovering resources. | Player | World System | Place Block, Build Schematic |
| Link Blocks | The player connects compatible blocks (e.g., conveyor → container, power node → generator) to establish a flow of resources. | Player | World System, Block System | Place Block, Build Schematic |
| Save Game | The player saves the current game state (map, units, buildings, resources) to a file. | Player | Save System, Game System | Load Game |
| Load Game | The player loads a previously saved game, restoring world and progress. | Player | Save System, Game System | Save Game |


