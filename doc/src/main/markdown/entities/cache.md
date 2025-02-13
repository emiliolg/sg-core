#Entities Cache

When an entity is defined in the mm, the cache option can be specified.
It can be specified alone, or modified by a cache size, to set the cache size. 
Modifier all can also be used, but it must be used carefully, since all instances will be cached, this is only recommended for entities with a limited and defined set of values. Else it will probably trigger memory issues.

**Simple Cache**
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#simpleCache)
**Cache with size**
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#qtyCache)
**Cache All Instances**
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#allCache)

**Console**
Caches can be visualized from the console, to see further details [Console Documentation](../starting/console.html)).


##Configuring through Properties

Cache can also be modified by properties, overriding the ones set in the MetaModel. See [tekgenesis.cache.CacheProperties javadoc](../javadoc/index.html) for further information.
- *maxIdleTime*: Max idle time in seconds an instance will be in the cache. -1 means infinite , 0 means do not override
- *lifespan*: Seconds an instance will be in the cache. -1 means infinite. 0 means do not use default
- *disable*: Disables the cache for the entity 

Cache can be set as **synchronic** or not. By default caches are not synchronic.  When a cache is synchronic, each entity modification, or new entity cached, will be updated on all nodes synchronously. This shouldn't be abused, since it will provoke overheads.
Since these properties apply to particular entities, to set them use: <entityName>.cacheoverride.<propertyName>. If <entityName> is not specified, it will apply for all entities.

#User Cache

User can also define caches for objects other than entities, for further detail, see [Memos and Caches](../memos.html)