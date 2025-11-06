# Review

## Design Pattern 1

**Reviewer:** Dinis Raleiras 67819

**Comments/Points to be  corrected/Sugestions:**

The identification of the **Facade Pattern** in the `AsyncCore` class is correct.  
The class provides a unified and simplified interface for managing asynchronous processes while hiding the complexity of Java’s concurrency system.  
It coordinates two subsystems — the internal game processes (`AsyncProcess`) and the Java concurrency API (`ExecutorService`, `Future`) — through clear methods like `begin()` and `end()`.

Overall, this is a **well-applied Facade Pattern** that effectively reduces system complexity, improves decoupling, and enhances maintainability in the Mindustry codebase.

## Design Pattern 2

**Reviewer:** [Miguel Rosalino] [68210]

**Comments/Points to be  corrected/Sugestions:**
The code snippet well illustrates the template design pattern, and exact code locations are provided.
The class diagram is well structured and simple to follow.
The Rationale is a good explanation of the template implementation.
The benefits make sense for the template pattern.
Overall, a solid report.


## Design Pattern 3

**Reviewer:** [Leandro Rodrigues] [nº68211]

**Comments/Points to be  corrected/Sugestions:**

Here is the review structured into the requested sections:

Comments:
- This is an excellent and accurate identification of the Interpreter pattern.
- The rationale correctly pinpoints all the key components of the pattern:
- LAssembler is perfectly identified as the Context, managing the global state and symbol table (vars).
- The l.build(asm) method is the classic interpret(context) operation, where each statement uses the context to produce its result (an LInstruction).

Points to be corrected:
- None. The analysis is very strong, accurate, and well-justified.

Suggestions:
- No suggestions are necessary; the analysis is complete and very well-written.
