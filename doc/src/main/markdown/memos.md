#Memos and Caches 
##Memos

Memos are useful to cache costly calculations. They cache the result, and recalculate after a given interval.
Memos are not replicated among nodes, they are cached on each node.


To implement a Memo, you should extend either SingletonMemo or MemoMap classes.
You should use the SingletonMemo when a single instance of the calculation will be stored, and MemoMap if you wish to have multiple instances, with different keys.

To create a memoizer of type T, a class extending SingletonMemo should be declarend and method T calculate(..) should be implemented.
The Memo will calculate it's value on first invocation, and then again after the specified time duration has passed.
If multiple calculations will be needed, MapMemo class should be extended instead. The K type for the Key should be specified, and the T calculate(K ..) method should be implemented.
In both cases, since Memo instances are constructed using reflection, they should have a single constructor with no arguments.
The defaultDuration must be defined in the default contructor.

**Example of a SingletonMemo:**
@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/memo/SampleSingletonMemo.java)
**Example of a MemoMap:**
@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/memo/SampleMapMemo.java)


Memos can be forced to calculate on next get() invocation from the Admin Console (For further info, view [Console Documentation](starting/console.html)).
They can be also forced to calculate across the cluster using method tekgenesis.cluster.jmx.Memos.forceCluster() 

###Useful methods for memos are:
-``` Memo.force()``` to force the calculation of the Memo on next invocation. 
-``` Memo.clean()``` to clear the value of the Memo.


>It is important to note, that in case of concurrence, to avoid unnecessary delays, if there is a previously calculated value, it will be returned, even if it is expired, the thread thas asks for the new value second, will not start a new calculation, nor wait for the first to finish, it will returne the previous cached value. If there is no previous value (this is the first calculation), it will wait for the first thread to finish.



##User Caches.