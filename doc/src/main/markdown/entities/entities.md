# Entities

Entities are Sui Generis' way to define and represent real life objects and relationships that are stored in database tables.

Entities are defined in declarative way, they have a name, an optional description for documentation purposes, optional [Model Options](#model-options), attributes, keys and indexes.

Attributes can be :

* A Sui Generis basic type. See: [Types](../language/basicTypes.html#types).
* An enum. See [Enums](../enums.html). 
* A Resource. See [Resource](resources.html#resource). 
* A reference to another entity. See [References](#entity-references) 

Entities may be documented: See [Mm Documentation](documentation/documentation.html)

# Example
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#simpleEntity)
This is a very simple example, where we can find the definition of a basic entity, with some attributes. 
In this particular case primary key is defined. Other options as described_by could be defined with the attributes, for further options see [Model Options](#model-options) below. 

## Attributes

Entities are composed of attributes, these can be as stated before of different types:

* [Basic Types](../language/basicTypes.html#types).
* [Enums](../enums.html). 
* [Reference to Another Entity](#entity-references) (either external or internal)
* Reference to a [Resource](resources.html#resource)

Attributes  may be documented as javadoc : See [Mm Documentation](documentation/documentation.html)

### Entity References
References imply a relationship between the entity that contains the reference and the referenced one. 
If the referenced entity also includes a simple reference to the original Entity then we will have a one to one relationship. A one to many relationship can also be specified adding a "*" character to the name of the referenced entity.

**Entities with one to many relationship with inner entity:**
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#innerMultiple)
see [Update Inner Entities](iterating_entities.html#UpdateInnerEntities) section in Traversing Entities to understand how to update inner entities correctly

**Entities with one to many relationship without inner entity**(The "many" entity must have a reference to the "one" entity.) :
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/EntitySamples.mm#externalMultiple)

The entity referenced **must** have a reference to the other entity, in case it has more than one attribute of the other type, to distinguish the attribute that will be used for matching, ```using <ATTRIBUTE_NAME>´´´ should be used after the Type specification to disambiguate. 


### Attribute Options

Attributes have a name, an optional description, a type and, depending on the type, they may have some options that modify the default attribute behaviour.

|Option|Usage|Description|
|------|-----|-----------|
|**optional** | optional / optional when *expression* |Gives the attribute the ability to be *null*. By default, all attributes are mandatory. If an attribute can be *null* it has to be specified at entity definition level. Attribute can also be optional only on a base condition.|
|**default** | default *expression* |Gives the attribute an initial value. When creating an entity instance, the attribute value is set to *expression*.|
|**mask** | mask *expression* ||
|**check** | check *expression* ||


## Model Options

Model Options are defined at entity level. They affect the way entities behave, how they are stored, hoy they are seen, etc.

|Option|Description|
|------|-----------|
|**described_by** |Defines the fields that will be included by default when entity instances are shown in a standard search box. In the example, the user will see the name and lastName of the entities that match the search criteria the user is entering.|
|**searchable** |Entities marked as searchable triggers indexation of **describe_by** fields to allow entities to be searched on suggest boxes and search APIs.|
|**cache** |Allows entity instance to be hasData through a Data inner class. This cache is shared among threads (see [Cache](cache.html) for further info).|
|**deprecable** |This provides an out of the box mechanism to handle activation and deactivation of entity instances. When an entity is deprecated it will no longer be shown in the searches, and the framework will store the deprecation time and author for that entity. Application permissions consider the deprecated status of an entity to decide if a record can be shown to the current user (A user can have.|
|**auditable** |If an entity is defined as auditable, it will automatically log the username, date and time when the record is created and each time it is updated.|
|**index** |Defines a name for an index and the attributes that are part of the index. This will create an index at database level for the table that represents the entity.|
|**unique** |Defines a name for a unique index and the attributes that are part of the index. This will create a unique constrain at database level for the table that represents the entity.|
|**image** |Defines an attribute to be used as instance image. Attribute must be of type Resource.|
|**table** |Allows sql table name overriding. Typically used when entity name is too long and a custom name is preferred over automatic name truncation.|
|**form** |Defines default form representation for entity.|
|**primary_key** |Defines the fields that will form the primary key. For further details see [Primary Key below](#primary-key)|
|**remotable** |Provides the entity the ability to be synchronized remotely by another Sui Generis Project.(see [Remote entities and views](remote.md))|
    
## Primary Key

There are two ways the entity can handle primary keys.

Explicitly: entity definition specifies which attributes will form the primary key

Implicitly: framework will create an extra database table column called "ID" with an automatic sequence to guarantee unique identification.

