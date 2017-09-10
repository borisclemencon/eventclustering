# eventclustering
Tutorials to understand to fundamental concept of Scala


Scala is a popular functional, object-oriented, compiled, strongly typed (although not always explicitly specified in the code, because infered) langage that run in the JVM. It is the native langage of many frameworks, and espcially used in Big Data and machine learning frameworks such as Spark, Flink and MxNet. 

To produce good scala code, you need to forget how you used to code. The most basic concepts such as loops and if/else are often replaced by (tail) recursivity and pattern matching. Because it is fundamentally a functional langage, Scala allows you to think in an algebra fashion, with functions to apply to each element of a collection. 

This project is a collection of tutorials whose goal is always the same: events clustering. We define a cluster such as: **the time duration between any pair of events from different clusters are always greater than a given threshold**. Then the algorithm is simple, 
  - order the event chronologicaly
  - for each event e,
  - if the time duration between e and the previous event is greater than a given threshold, we insert it in a new set and we save the previous set


We take the example of users visiting some websites. The browsing sequence usually have a common semantic purpose, and given all the visits in a month, we want to identify the sequences of visits (called session). The tutorials are:

In these tutorials, we will go incrementally from a naive Java-like non-functional approach and we will move gradualy to a fully generic and functional code.

  - tuto1-naive: a naive Java-like non-functional approach with while and iterator
  - tuto2-functional: the functional counter part with tail recursivity and pattern matching 
  - tuto3-generic: a fully gemeric code using traits and bounded inheritence allowing a beautiful functional approach