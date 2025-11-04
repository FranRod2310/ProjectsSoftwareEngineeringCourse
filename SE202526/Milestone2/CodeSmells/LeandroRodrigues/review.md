# Review

## Code Smell 1
**Reviewer**: [Luís Muacho] nº [68301]

**Comments/Points to be corrected/Suggestions:**
The code smell was perfectly identified and the rationale is correct.
The original runTurn() method had too many responsibilities, and the refactoring fixes this effectively. This division of logic follows the SRP and improves the code's readability and maintainability

## Code Smell 2
**Reviewer**: [Luís Muacho] nº[68301]:

**Comments/Points to be corrected/Suggestions:**
The code smell was perfectly identified and the rationale is correct. 
The suggested refactoring is the ideal solution to this problem. The introduction of the MenuDisplay interface breaks that coupling and correctly applies the DIP. With this change, Menus now depends only on the MenuDisplay abstraction, and not on the concrete details of the UI, making the code easier to maintain and more modular.
## Code Smell 3
**Reviewer**: [Luís Muacho] nº[68301]:

**Comments/Points to be corrected/Suggestions:**
The code smell was perfectly identified and the rationale is correct.
The chain content.blocks().select(...).toArray() caused the GameService class to be strongly coupled to the internal structure of the Content class. The refactoring is the ideal solution. By creating a helper method inside Content, the details of that chain are encapsulated and the coupling is removed, making the code cleaner and easier to maintain.

# Log
## Date [02/11/25]
- Reviewed code smell 1
- Reviewed code smell 2
- Reviewed code smell 3
