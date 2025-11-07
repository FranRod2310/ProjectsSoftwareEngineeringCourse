# Review

## Design Pattern 1

**Reviewer:** Francisco Rodrigues 67753

**Comments/Points to be  corrected/Sugestions:**

The template pattern is well identified, and clearly the case. The classes below the abstract class use the template given by the abstract class, adding it's own logic to some cases. The class diagram is also well made, it shows that `DrawCircles`, `DrawFade` and `DrawGlowRegion` depend on the template given by `DrawBlock`. The rationale is also valid, it explains why this pattern can be useful in this case.


## Design Pattern 2

**Reviewer:** Filipe Nobre 67850

**Comments/Points to be  corrected/Sugestions:**
The identification of the Strategy Pattern in this context is correct. The `Weapon` class defines a common interface for different weapon behaviors (`update`, `shoot`, `draw`) while its subclasses such as `BuildWeapon`, `RepairBeamWeapon`, `MineWeapon`, and `PointDefenseWeapon` each implement distinct strategies for specific actions like building, repairing, or defending. This design allows units to change weapon behaviors without modifying their core logic, promoting flexibility and extensibility. However, the base `Weapon` class is quite large and tightly coupled with other game systems, which slightly weakens the clarity of the pattern’s structure. Overall, it’s a good application of the Strategy Pattern, even if the implementation could be more modular.


## Design Pattern 3

**Reviewer:** [nome] [nº]

**Comments/Points to be  corrected/Sugestions:**
