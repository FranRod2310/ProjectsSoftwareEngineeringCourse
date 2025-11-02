# Review

## Code Smell 1
**Reviewer**: [Miguel Rosalino] nº [68210]

**Comments/Points to be corrected/Suggestions:**

The code smell is well identified and presented.
Suggested refactoring is well implemented and makes sense.

## Code Smell 2
**Reviewer**: [Francisco Rodrigues] nº [67753]

**Comments/Points to be corrected/Suggestions:**

The code smell is there and it is well defined. The suggested refactoring (adding a class) is a good option, so that some responsibilities can pass to this new class.


## Code Smell 3
**Reviewer** [Filipe Nobre] nº [67850]:

**Comments/Points to be corrected/Suggestions:**
- **Accurate Identification**: The Feature Envy smell is correctly spotted — the method heavily depends on data and behavior from the `Tile` class.
- **Clear Explanation**: The rationale effectively explains how `FogControl` violates encapsulation by directly accessing multiple `Tile` internals.
- **Refactoring Proposal**: The suggested method `hasFogAffectingBuild()` neatly encapsulates related checks inside `Tile`, restoring proper object responsibility.
- **Design Improvement**: The refactor enhances **cohesion** and **encapsulation**, reducing cross-class coupling and improving code readability.
- **Overall**: Strong and precise analysis with a practical, minimal-impact refactoring that aligns well with clean code principles.


# Log
## Date [30/10/25]
 - Reviewed code smell 1

## Date [31/10/25]
 - Reviewed code smell 2

## Date [02/11/25]
 - Reviewed code smell 3
