# Code Metrics Assessment - Lines of Code

The number of lines of code can be used as a metric to analyze project code, being mainly usefull to find overly long classes and methods and identity potential god-class or long method code smelss.

It may not be the best measure of developer productivity or code quality, be we can use it to visualize and detect some specific code that could use improvements.

In this folder, you can find the Excel spreadsheet _`CodeMetrics_LOC.xlsl`_ containing a table with the available information from the Lines of Code Metric Assessment at **class** and **method** level.

We have various metrics available in this table, namely:
- **NCLOC**: Non-comment lines of code
- **LOC**: Lines of code
- **CLOC**: Comment lines of code
- **JLOC**: Javadoc lines of code
- **RLOC**: Relative lines of code

In this report, we will look at 3 different metrics related to the number of lines of code written in Mindustry methods and classes.

## Method level - Non-Comment Lines of Code

First, lets start with the **non-comment lines of code (NCLOC)**. 
In the following graph, we can see the **top 100 methods with highest NCLOC**.

<img width="1386" height="824" alt="image" src="https://github.com/user-attachments/assets/e20dd5fd-3ce2-44cc-9920-e43537fa9cff" />

It's clear to see that this graph very closely resembles a **_Pareto_ distribution**, with the vast majority of methods having lower NCLOC.

Let's take a closer look at the discrepencies. For that, we have a view of the **top 10 methods with most NCLOC**.

<img width="1111" height="661" alt="image" src="https://github.com/user-attachments/assets/100daa9a-7a28-498d-ad6d-92da795a770c" />

We can easily detect two very big outliers, the `mindustry.content.Blocks.load()` and `mindustry.content.UnitTypes.load()` methods which are made up of **more than 3000** lines of code each. Let's take a look at each one.

### `mindustry.content.Blocks.load()`
This method is responsible for **loading ALL block types** in Mindustry. In total we have a method that is **5590** NCLOC-long.
While it does work, its size makes it very hard to read, understand and debug. This is a case of a **Long method code smell**.

To fix this, the load method could be refactured into seperate smaller load methods grouped by block types (environment, ores, walls, these categories are even already commented at the top of the class).

This would vastly improve readibility and make the code more versitile, we could call individual load methods if needed.

### `mindustry.content.UnitTypes.load()`
Like the previous load method, this follows the same design errors. All unit types are loaded in the same method.
A possible refacturing is, again, dividing the loading into smaller methods specific for some unit types (air, mech, naval...).

### `mindustry.service.GameService.registerEvents()`
In the graph above, you can also find highlighted in green the `registerEvents()` method that was addressed as **Code Smell #3**.
An analysis and suggested refacturing can be foudn in the CodeSmells folder!

## Method level - Comment Lines of Code
Now, let's take a look at a different metric, **CLOC**. This metrics analysis the number of **comment lines** in methods, which could be an indicator of overly-complex methods using comments as **deodorant**!

In the following graph, we can see the **top 10 methods** with most comment lines:
<img width="1380" height="829" alt="image" src="https://github.com/user-attachments/assets/4f058d68-2fe3-4ff2-9115-f59cd82d863f" />
We can find some results that we would suspect after analyzing the last metric, namely the `Blocks` and `UnitType` loading methods. Due to their large size, they naturally have lots of comment lines.
However, a more interesting method is the one with the **most** CLOC:

### mindustry.annotations.entity.EntityProcess.process()
After looking through this method, we can see it's very complex and long. The comments themselves do **make sense**, they explain all parts of the method logic. However, even if the comments are justified, the need for so many points to a design issue in the method. 

It's possible to see this same method in the graphs above for NCLOC, which makes it a candidate for a refacture to shorten and simplify the logic. So, we could say that the large number of comments is used as **deodorant** for masking this type of code smell.

## Class level - Lines of Code
Looking now at a **class** level, we can use the general lines of code metric to seek out **potential god-classes** that could be improved. Let's take a look at the metric at a **global level**:
<img width="1579" height="938" alt="image" src="https://github.com/user-attachments/assets/b9d6f4b4-b9f9-476d-9201-9d6567ceb1c3" />
Once again, like at a method level, we can see a case of **_Pareto_ distribution** for lines of code among classes.

In this next graph, we can see the **top 14 classes** with most lines, corresponding to the start of the _Pareto_ distribution:
<img width="1309" height="779" alt="image" src="https://github.com/user-attachments/assets/08a99393-08ee-45d4-8f9f-64331e9a216a" />

As we have observed so far, `Blocks` and `UnitTypes` are huge outliers in most metrics. Here, it's no different. Both of these classes have more than 3000 lines of code, a sign of **God-class code smells**. This makes them hard to read and extend.

To fix these issues, these classes could be refactured into more specific **sub-classes**, dividing work into block and unit types as discussed above.

## Conclusion

While the Lines of Code metric may not always be the best for analyzing productivity, since sometimes methods and classes have to be relatively large, it's still a usefull metric for **identifying potential refacturing opportunities** to imporve code readibility and flexibility.

This metric covers many levels, such as methods, classes and packages, being a powerfull tool for overviewing the project and detecting problems **early** on. 
