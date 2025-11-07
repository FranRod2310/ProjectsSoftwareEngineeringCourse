# Chidamber-Kemerer Métrics Review

## Reviewer: Filipe Nobre 67850

___

This Chidamber-Kemerer metrics report is very well done — clear, technically accurate, and logically structured. It starts with precise explanations of each metric, follows with desired values, project results, and then interprets outliers and their relation to code smells. The progression is coherent and makes the analysis easy to follow.

The metric definitions are correct, and the interpretations show good understanding of object-oriented design quality. The analysis of averages is balanced, noting good cohesion (low LCOM) but excessive coupling (high CBO and RFC). The discussion of outliers is excellent — it correctly identifies BuildingComp as a “God Class” due to extreme complexity and low cohesion, Vars as a source of global coupling, and deep inheritance problems in other classes. These findings are explained clearly and connected to real design risks.

The link between metrics and code smells is insightful. Connecting high CBO to Feature Envy and high RFC to Message Chaining is accurate and well reasoned, and the recognition of God Classes based on WMC and LCOM adds analytical depth.

The only real improvement would be to give a bit more context on what counts as a “high” or “acceptable” metric value and to integrate visuals more directly.

Overall, this is an excellent and thoughtful CK metrics analysis — technically sound, clearly written, and offering meaningful insights into the project’s design quality