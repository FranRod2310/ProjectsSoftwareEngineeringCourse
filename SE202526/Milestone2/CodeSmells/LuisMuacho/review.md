# Review

## Code Smell 1
**Reviewer**: [Filipe Nobre] nº [67850]

**Comments/Points to be corrected/Suggestions:**
- **Accurate Identification**: The Data Clumps smell is correctly recognized — repeated primitive groups `(startX, startY)` and `(endX, endY)` appear across multiple overloads.
- **Clear Justification**: Excellent explanation of how the current design decomposes `Tile` objects unnecessarily, showing an inversion of responsibility.
- **Refactoring Strategy**: The proposed “Preserve Whole Object” solution is well aligned with best practices. Steps are logically structured and easy to follow.
- **Technical Soundness**: The suggested new method signatures and delegation flow are consistent with object-oriented principles and improve maintainability.
- **Overall**: Strong analysis and well-documented refactoring plan — concise, technically correct, and clearly articulated.

## Code Smell 2
**Reviewer**: [Leandro Rodrigues] nº[68211]:

**Comments/Points to be corrected/Suggestions:**
The code smell was well identified: pathfind clearly suffers from being a Long Method. The suggested refactoring to extract reconstructPath is a good first step,
but it only addresses the path reconstruction phase. The setup and search logic remain in the main method, so further extraction could improve cohesion, readability,
and testability. Overall, the code smell is correctly spotted, but the refactoring could be more comprehensive.


## Code Smell 3
**Reviewer**: [Leandro Rodrigues] nº[68211]:

**Comments/Points to be corrected/Suggestions:**
The smell was accurately identified, the rationale is clear, and the refactoring is well-implemented. Overall, a solid and correct solution.



# Log
## Date [02/11/25]
- Reviewed code smell 1

## Date [02/11/25]
- Reviewed code smell 2

## Date [02/11/25]
- Reviewed code smell 3