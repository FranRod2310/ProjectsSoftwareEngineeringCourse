# Code Metrics Assessment - Martin Packaging Metrics

The Martin Packaging Metrics can be used to detect code smells and identify potential design issues within a software system.
They are particularly useful for understanding the dependencies between classes and how stable or fragile those classes and packages are.

Using the `MetricsReloaded` extension, we can observe these 5 metrics:
- **A**: Abstractness
- **Ca**: Afferent Coupling
- **Ce**: Efferent Coupling
- **D**: Distance from the Main Sequence
- **I**: Instability

Each one with a different purpose.


**Abstractness**

This metric is used to measure how abstract a package is.
Here we can see the graphic that relates the packages with abstractness.
1 means that package is completely abstract, 0 means that the package is not abstract.

<img width="2427" height="749" alt="image" src="https://github.com/user-attachments/assets/3eb979ea-4112-476e-8fe8-3d737b0b44b4" />

In this chart we can see that at the left are the most abstract packages. 
This means that this packages are easier to extend and to add new features. It has more flexibility. They have normally more base code and less implementation.
On the other hand, the packages close to 0 have no abstraction, this means that they implement the logic and are not prepared for features. 


**Afferent Coupling**

Afferent Coupling measures how many other packages depend on a given package. More specifically,
Ca = Number of classes outside the package that depend on classes inside the package.

<img width="2364" height="768" alt="image" src="https://github.com/user-attachments/assets/1eed0380-f52c-43d3-ab5b-0429c9c5a0fa" />

This charts shows on the left, that the package "mindustry.world" has the most classes depending on its package. 
This is normal since it's a package with a lot of important information for the rest of the classes.
On the right side we can see a similarity with the chart showed before. 
The classes that are not abstract, and implement more logical methods, have also less classes depending on them since it is difficult to have features.

**Efferent Coupling**

Efferent Coupling is the opposite of Afferent Coupling, it measures how many classes inside a package depend on classes outside that package.
Ce = Number of classes inside the package that depend on classes outside the package.

<img width="2439" height="923" alt="image" src="https://github.com/user-attachments/assets/71bdad86-1565-4478-aad5-7b5822a4d5f8" />

We can see a clear difference between the packages. "mindustry.content" depends a lot on the classes outside its package. 
This could be seen as a possible code smell, feature envy. It is more interested in other classes than in it's own methods.

<img width="312" height="656" alt="image" src="https://github.com/user-attachments/assets/0a46b073-af42-45c8-bf43-bc7a121151d8" />

This is the import list of one of the classes inside the package "mindustry.content".


**Instability**

This is useful to measure how easily a package can change, how flexible it is.
This value is calculated based on the values of Ca and Ce.
I = Ce/(Ca + Ce)
A low value means it's very stable and a high value means it's unstable

<img width="2208" height="1039" alt="image" src="https://github.com/user-attachments/assets/83f3b8c7-a111-4271-8345-31378f039810" />

On the chart we can see that the most of the packets have a high value, this means that they are unstable. 
This can be problematic because if there is something that needs to be changed in the way things are done, you need to pay extra attention to how other classes use this packet.

**Distance**

This is the distance from the main sequence, in other words, it measures how far a package’s design is from the ideal balance between Abstractness and Instability.
If the value is close to 0, it means that the package has the ideal balance between the abstractness and instability.
If the value is closer to 1, it means that it could easily lead to problems later, since the balance isn't great.
The formula to calculate the value is: D = | A + I - 1|.
So if the abstracness is 0.8 but the instability is also 0.8, the result will be 0.6, which means that it has a useless abstraction, which is unnecessary.
On the other side if we have 2 low values like 0.2 for each, it means that it's stable but not abstract, which makes it hard to extend and add depending classes.


<img width="2338" height="922" alt="image" src="https://github.com/user-attachments/assets/9ab69dde-a50a-4528-8552-90762e9f8c4a" />

On the last chart we can see that there are a lot of high values, this means that these packages are not well-balanced. 
The architectural design quality is not the best and so it should be changed.

