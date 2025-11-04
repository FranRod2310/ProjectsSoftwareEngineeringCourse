# Code Metrics Assessment - Lines of Code

The number of lines of code can be used as a metric to analyze project code, being mainly usefull to find overly long methods.
It may not be the best measure of developer productivity or code quality, be we can use it to visualize and detect some specific methods that may constitue code smells.

Using the `MetricsReforged` extension, we can observe the following:

<img width="1359" height="1102" alt="image" src="https://github.com/user-attachments/assets/2e6d098a-fb88-4bec-99aa-a29663528024" />

<img width="1168" height="1138" alt="image" src="https://github.com/user-attachments/assets/15b4a28e-a2c5-4b7f-b4eb-07d6e10bfa89" />


We have various metrics availably in this table, namely:
- **NCLOC**: Non-comment lines of code
- **LOC**: Lines of code
- **CLOC**: Comment lines of code
- **JLOC**: Javadoc lines of code
- **RLOC**: Relative lines of code

In this sample of the analysis table, we can see the methods that are made up of **more than 200** non-comment lines of code.

We can easily see that `mindustry.content.Blocks.load()` and `mindustry.content.UnitTypes.load()` are excessivly long, measuring in more than 3000 lines of code each.

While the methods do work, this is clearly a **Long Method** code smell. 
In such methods, code can easily get confusing and hard do debug. Most if not all of these overly long methods can and should be re-written into multiple **smaller** complimentary methods. For example, we could organize the Block Loading into smaller block types, making code more readable.

An example of this is addressed in my **Code Smell #3**, where the `mindustry.service.GameService.registerEvents()` method _(highlighted in dark green)_ is refactured.

In this folder, you can also find the full metrics spreadsheet, where multiple other LOC-related metrics are also displayed.

