# Cache

_tekgenesis.admin_

## Overview

In the cache section, all configured cached are shown, and the quantity of element in each cache.

There are two different types of caches:

- Entity Cache 
Each one of these is related to a specific entity.

- User Cache
These caches are created programmatically by Sui Generis developers.

![Database](/img/cache.png)

The Cache Table can be filtered using the search field. 
It shows the following information:

**Cache Name**
For entity cache: the name of the entity the cache is bound to.
For user caches: the name by which the cache will be referenced.

**Mode** 
The mode shows how the value changes will be replicated onto other nodes. The available options are: 

- Invalidation Async
There is no replication, only asynchronous data invalidation.

- Local
The cache is local, and it remotely invalidates other nodes.

- Invalidation Sync
There is no replication, but there is a synchronous invalidation of other caches.



The clear(![clear](/img/delete.png)) button forces a cache cleanup.
