# Review

## Design Pattern 1

**Reviewer:** Dinis Raleiras 67819

**Comments/Points to be  corrected/Sugestions:**

The identification of the **Memento** pattern in the `SaveSlot` class is correct and well explained.
The mapping of the roles — **Originator**, **Memento**, and **Caretaker** — matches how the saving system in `Mindustry` works.
`SaveSlot` acts as the memento, storing the game’s snapshot data, while the originator is the game world that generates and restores its state.
The caretaker, represented by the `Saves` class, controls when the game state should be saved or loaded.
This design correctly applies the **Memento** pattern because it allows the game to save and restore its state without exposing internal details.

## Design Pattern 2

**Reviewer:** Luís Muacho 68301

**Comments/Points to be  corrected/Sugestions:**

The Rationale precisely identifies the Factory Method pattern. It correctly points to the `UnitType` as the Creator and the method `create(Team)` as the main factory method that encapsulates the creation logic.
The Identification of "Concrete Creators" is well-done. It demonstrates a good understanding of the pattern's flexibility by showing how `MissileUnitType` changes the type of the created object, while `ErekirUnitType` personalizes the properties of the default object.
The diagram clearly shows the Creator hierarchy and its dependency relationship with the product interface.
To summarize, it seems that this pattern identification is precise and correct.

## Design Pattern 3

**Reviewer:** Francisco Rodrigues 67753

**Comments/Points to be  corrected/Sugestions:**

The design pattern is well chosen, it has a abstract class, `SaveFileReader`, which is used as a template for the class below, `SaveVersion`. It is also explained why it is a template pattern, and what for it can be used. The class diagram showed below the code is also correct, as it's possible to see that the `SaveVersion` class depends on the `SaveFileReader` class. The types "Abstract class" and "Concrete class" are also well identified. 
The advantages listed are also common characteristics of this pattern. 
