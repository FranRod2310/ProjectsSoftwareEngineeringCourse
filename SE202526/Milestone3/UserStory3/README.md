# User story 3
Introduction to the game

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
(*Please add the use case diagram here.*)

---

## Use case textual description

### Use Case: Tutorial
ID: 1
Brief description:
Start a tutorial explaining how building, researching and defending works.
Primary actors:
New Player
Secondary actors:
None
Preconditions:
None
Main flow:
1. The use case starts when the new player clicks on the help icon.
2. The system starts the tutorial world.
3. The system starts the building tutorial.
  3.1 Include: Building tutorial.
5. The system starts the defense tutorial.
   5.1 Include: Defence tutorial.
6. The system shows a pop-up message "Tutorial completed".
7. The system returns to the real world.
8. The use case ends.
Postconditions
None
Alternative flows:
The new player cancels the tutorial.

### Use Case: Building tutorial
ID: 2
Brief description:
Tutorial showing how to build.
Primary actors:
New Player 
Secondary actors:
None
Preconditions:
New player is in the tutorial world.
Main flow:
1. The use case starts when the new player arrives at the tutorial world.
2. The system shows a pop-up message explaining what to do.
3. The player clicks on the research tree icon.
4. The system starts the research tutorial.
   4.1 Include: Research Tutorial.
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
Postconditions
1. Factory built.
Alternative flows:
The new player cancels the tutorial.



### Use Case: Tutorial cancelled
ID: 5
Brief description:
The new player cancelled the tutorial.
Primary actors:
New Player 
Secondary actors:
None
Preconditions:
New player clicked on cancel and the player is in the tutorial world.
Main flow:
1. The alternative flow can start anywhere.
2. The system shows a pop-up message "Tutorial ended".
3. The system returns to the real world.
4. The use case ends.
Postconditions
1. The player is back to the real world
Alternative flows:
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
