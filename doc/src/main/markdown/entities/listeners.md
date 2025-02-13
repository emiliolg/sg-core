#Listeners

There are two different kinds of listeners that can be used with entities. There are EntityListeners and TransacationListeners.
EntityListeners follow the lifecycle of an entity, and Transaction listeners the different transaction phases.
Both listeners are invoked in the same thread as the transaction, so the have a veto capacity. In case of transactions, if an exception is thrown, the transaction will be rolled back.
Also return type for pre-delete will be used for veto.

##Observers
Sui Generis also provides an Observer Framework. To register to this framework, the Observer clases must be registered in the MetaInf file.

