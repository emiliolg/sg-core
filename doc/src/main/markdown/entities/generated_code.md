#Generated Code
As mentioned in the [Metamodel Language Section](../language/metamodels.html#code-generation), for each Entity a base class and a user class are generated.
The user class will be named as the Entity, and the base class will have the same name with the "base" suffix.
The user class will be created in the same package specified in the mm, but the Base class will be generated in a packaged called the same way, adding a "g" (as in generated)  suffix. 
Also for each entity a Table class is generated, this class will be named as the entity, with the "Table" suffix, and placed in the "g" package.

## Table Class
Table class, located in package <entityPackage>.g, and named <EntityName>Table provides an entry point for Sui Generis SQL.
The table defines attributes of TableField types for each of the defined columns.
See the following example:
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/Person.mm#address)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressTable.java?Int PERSON_SSN)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressTable.java?Str STREET)


For further detail see [Sql Statements (DSL)](sqldsl/sqldsl.html)

## Base Class
Generated code for any entity base class includes:

- Getter and Setter methods for each attribute (setter methods return the entity to enforce a fluent API)
- [Find Methods](#find-methods)
- [List Methods](#list-methods)
- [Iteration Method](#iteration-method)
- [Create Method](#create-method)
- Add/Remove Listener methods (see [Listeners] (listeners.html))

 
### Find Methods
Various find methods are also generated for the entities, to provide different functionality.

|Method|Usage|
|------|-----|
|**find** (primary Key)| Searches by primary key and returns null if not found|
|**findWhere** (using a criteria)|Same as before but with a criteria|
|**findOrFail** |Throws exception instead of returning null|
|**findPersisted**| Goes straight to the database instead of accessing caches and returns null if not found|
|**findPersistedOrFail**|Goes straight to the database instead of accessing caches and throws exception if not found|
for more cache info see [caches](cache.html)
some samples: 
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?find)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?findOrFail)

### List Methods
List methods provide either a Select instance or an immutable list. 

|Method|Usage|
|------|-----|
|**list** |Returns a Select instance, is lazy, and can iterate through all of the persisted instances of this entity|
|**listWhere(Criteria)** |also lazy, and returns a Select instance, that can be iterated.|
|**list(Set<Integer> keys)** **list(Iterable<String> keys)**|Thesee list methods will return an ImmutableList containing all entity instances with those keys|

some samples:
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?Select<Address> list)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?listWhere)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?ImmutableList<Address> list)

### Iteration Method
For each methods is provided, this method is the recommend way to iterated through instances of an entity. Please see [Traversing Entities] (iterating_entities.html)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?forEach)

### Create Method 
Create method for instantiation takes as arguments the primary key attributes for the entity.
#### Entity with no primary Key defined
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/Person.mm#address)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/AddressBase.java?static Address create)

#### Entity with primary Key defined
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/Person.mm#person)
@inline-code(doc/samples/snippets/src_managed/main/mm/tekgenesis/snippets/g/PersonBase.java?static Person create)