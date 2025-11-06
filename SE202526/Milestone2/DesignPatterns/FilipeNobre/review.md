# Review

## Design Pattern 1

**Reviewer:** Dinis Raleiras 67819

**Comments/Points to be  corrected/Sugestions:**

The identification of the **Factory Method** pattern in the `Blocks` class is accurate and well justified.
The explanation correctly highlights how the `load()` method centralizes the creation and initialization of all block objects in the game, such as conveyors, drills, cores, and turrets.
Each block type inherits from the abstract `Block` class, which defines the base structure and shared behavior, fitting the intent of the **Factory Method** pattern.

## Design Pattern 2

**Reviewer:** Luís Muacho 68301

**Comments/Points to be  corrected/Sugestions:**
The Rationale for this pattern is correct, it precisely identifies the class `Vars` as a Singleton container, which is an exact description of its function.
The justification correctly points to the `static` members as the main evidence. This guarantees that there is just one instance of each critical subsystem, which is managed by the `Vars` class.
The benefits are precisely the results of this type of pattern, so they are correctly identified. 
Finally, the diagram is simply but represents what we need to see about this class with this pattern.
So overall, this identification seems correct, meets all the bullet points, and is well done.

## Design Pattern 3

**Reviewer:** Francisco Rodrigues 67753

**Comments/Points to be  corrected/Sugestions:**

The Pattern is correct since the algorithm of the `BulletType` can be changed without modifying it's connection with `PowerTurret`. The `BulletType` has different classes implementing multiple algorithms, so it can change internally without the `PowerTurret` class noticing. The class diagram showed is also correct, it show the interaction between the 4 classes. The Rationale explained after is also correct. The reason for this pattern is the flexibility in the code, which is mentioned, and the reusability. It helps separating and organizing different logic methods, and makes it easy to change between classes implementing it.
