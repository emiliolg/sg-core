#Traversing Entities

##Iterating Through Entities Instance

The recommended way to iterate an entity's records, is the following:

**forEach**

```java
Entity.forEach{ entity -> ...}    
```

It is not recommended to use 

```java
foreach(Entity.list()){...}
```
Since the list() method only frees the resources used for database whe the iteration is finished, if iterator is interrupted either intentionally or by exception, the database resources will not be freed.

For the same reasons it is not recommended to use:

```java
foreach(Entity.selectFrom(ENTITY)){...}
```

Also the use of  

```java
Entity.list().list()
```
is discouraged since all instances are loaded to memory.


If an iteration may be interrupted based on a particular condition and no return value is expected, use *forEachWhile* specifying a Predicate
**forEachWhile**

```java
Entity.forEachWhile( entity -> ...) using a java 1.8 predicate
```


If an iteration may be interrupted based on a particular condition and an optional return value is expected, use *forEachReturning* specifying a StepResult

**forEachReturning**

```java
Entity.forEachReturning( entity -> {...
    if(shouldReturnSomething){
        return StepResult.done(someValue);
    }

    if(shouldJustReturn){
        return StepResult.done()
    }
     //should continue
    return StepResult.next()
})
```

##Updating Inner Entities

To help update inner entities in a simple and performant manner, there are two available methods for them. Through the usage of this methods, the user can update all of the instances available for an inner entity with a very simple code.

These methods are: 

**Merge**
Merge the Instances in the Inner with the new ones. Matching sequentially. 

@inline-code(db/entity/src/main/java/tekgenesis/persistence/InnerEntitySeq.java?InnerEntitySeq<E> merge)

*A sample usage:*
 
@inline-code(samples/basic/src/main/java/tekgenesis/sales/basic/ProductForm.java?public void copyTo)

Using **merge** the new values are matched in order with the existing ones, and the consumer is invoked for each pair. 
If the original set exceeds in size the new values, all remaining elements are removed. If otherwise, the newValues exceeds in size the original values, a new instance is created for each “new value” and the consumer is invoked with that new instance.

**MergeMatching **
Merge the Instances in the Inner with the new ones, matching using the specified Predicate.

@inline-code(db/entity/src/main/java/tekgenesis/persistence/InnerEntitySeq.java#fullMerge)

*A sample usage:*
@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/InnerMultiplePersonForm.java?doMergeData)


Here the matching is done though the use of the matchPredicate predicate. 
In this case, for each of the original values, the "match" is searched using the match predicate. If no match is found, the deleteAction predicate is used to check if the original value should be removed.
Again, for each newValue that had no match in the original set, a new instance is created, and the createAction consumer is used to initialize that new value.

Also a simple **MergeMatching** version is provided, as follows:
@inline-code(db/entity/src/main/java/tekgenesis/persistence/InnerEntitySeq.java#simpleMerge)

In this case, as shown in the snippet. The deletePredicate is allways true, therefore if no match is found, values will be deleted, and the same consumer is being used both for matched values or new ones.

**_Even if predicate is asserting on an attribute, that attribute should be copied in the consumer, since new (empty) instances are created when no match is found._**

