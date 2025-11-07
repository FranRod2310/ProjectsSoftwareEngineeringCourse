# Complexity Metrics


## Explanation of Complexity Metrics

Complexity metrics are quantitative measures used to evaluate how difficult it is to understand, test, and maintain a
program. They help identify areas in the code that may be overly complicated or require refactoring, providing insight into the 
software’s maintainability and reliability.
These metrics are especially useful for detecting methods or classes that have too many branches, nested conditions,
or complex logic, which can make debugging and future modifications harder.




| **Metric** | **Full Name** | **What it Measures** | **Interpretation** |
|------|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| **CogC** | *Cognitive Complexity* | Measures how difficult a method is to understand based on its control structures and nesting. | Higher values indicate code that is harder to read and reason about. |
| **v(G)** | *Cyclomatic Complexity* | Counts the number of independent execution paths through a method. | Higher values mean more possible paths, requiring more tests and increasing risk of errors. |
| **iv(G)** | *Design Complexity* | Measures the complexity introduced by decision structures and interactions between modules.|Higher values indicate more complex decision logic and module interdependence. |
| **ev(G)** | *Essential Complexity* | Indicates how much of the control flow is truly necessary (not due to poor structure or redundancy).| High values suggest tangled or unstructured logic that could be simplified. |
| **OCavg** | *Average Operation Complexity* |Represents the average cognitive or cyclomatic complexity of all methods in a class.| A high value suggests that most methods in the class are complex, making it harder to understand and modify. |
| **OCmax** | *Maximum Operation Complexity* |Indicates the highest complexity value among all methods in a class.| A high OCmax shows that at least one method is particularly complex and may require refactoring or detailed testing. |
| **WMC** | *Weighted Methods per Class* |Sum of the individual complexities of all methods in the class. It reflects the total behavioral complexity of the class.| High WMC means the class is doing too much or has too many complex methods, increasing maintenance cost and reducing reusability. |

### Desired Values

Desired Values
- Low CogC → Code is easier to read, understand, and maintain.
- Low v(G) → Fewer decision paths, easier testing, and lower error probability.
- Low iv(G) → Simpler design with fewer unnecessary interactions between modules.
- Low ev(G) → Indicates clean, structured, and essential logic.
- Low OCavg → Most methods are simple and easy to understand.
- Low OCmax → No single method is excessively complex.
- Low WMC → The class has limited overall complexity, following the Single Responsibility Principle.

**In general:**  
A well-structured codebase aims to minimize all complexity metrics, producing code that is:
- Easier to maintain and extend.
- Simpler to test.
- More robust and less prone to logic errors.

### Method Metric Analysis



The method-level complexity metrics provide insight into the overall complexity and understandability of the system. 
The average values are:

| Metric         | Value | Interpretation |
|----------------|-------|----------------|
| CogC (4.03) |    Moderate   |     The average cognitive complexity per method is 4.03, indicating that most methods require a moderate effort to understand. This suggests that the code is generally readable but some methods may need more attention.          |
| v(G) (3.45)  |    Low   | The average cyclomatic complexity per method is 3.45, showing that most methods have a small number of independent execution paths. This implies that testing is manageable and logic is not overly complex.       |
| iv(G) (2.73)  |Low|        The average design complexity per method is 2.73, indicating that the methods generally have simple decision structures and low interdependence between modules.    |
| ev(G) (1.36) |   Low   |     The average essential complexity per method is 1.36, meaning that most methods have straightforward and well-structured control flow, without unnecessary logic.   |


### Class Metric Analysis



The class-level complexity metrics provide insight into the overall structural complexity of the system at the class level.
The average values are:

| Metric         | Value | Interpretation |
|----------------|-------|----------------|
| OCavg (2.59) |    Low   |     The average operation complexity per class is 2.59, indicating that most methods within classes are simple and easy to understand.          |
| OCmax (5.02)  |   Moderate   | The average maximum complexity per class is 5.02, showing that at least one method in each class may be more complex, requiring additional attention or testing.     |
| WMC (17.75)  |Moderate|    The average weighted methods per class is 17.75, suggesting that classes generally have a moderate total complexity, reflecting manageable responsibilities and maintainability.    |





### Package Metric Analysis



The package-level complexity metrics provide insight into the overall complexity and structure of the system at the package level.
The average values are:

| Metric         | Value | Interpretation |
|----------------|-------|----------------|
| v(G)avg (3.45) |    Low   |    The average cyclomatic complexity per package is 3.45, indicating that packages contain methods with generally low complexity. This suggests that packages are well-structured and maintainable.         |



### Module Metric Analysis



The module-level complexity metrics provide insight into the overall complexity of the system at the module level. 
The average values are:

| Metric         | Value | Interpretation |
|----------------|-------|----------------|
| v(G)avg (3.45) |    Low   |   The average cyclomatic complexity per module is 3.45, indicating that modules contain methods with generally low complexity. This suggests that the system’s modules are well-structured, maintainable, and easy to test.        |



### Project Metric Analysis



The project-level complexity metrics provide insight into the overall complexity of the entire system. The average values are:

| Metric         | Value | Interpretation |
|----------------|-------|----------------|
| v(G)avg (3.45) |    Low   |  The average cyclomatic complexity across the entire project is 3.45, indicating that, overall, methods are relatively simple and easy to understand. This suggests that the system is well-structured, maintainable, and has low risk of overly complex logic.        |





#### sources research: https://www.geeksforgeeks.org/dsa/complexity-metrics/ ,https://blog.codacy.com/code-complexity,https://www.mccabe.com/iq_research_metrics.htm