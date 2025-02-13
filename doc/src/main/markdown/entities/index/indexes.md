# Indexes

## Searchable syntax

An entity can be set as searchable so that it is indexed for search in search boxes and search APIs. This will generate a base class (entityFqn + 'SearcherBase') and a user class for the user to override default search behaviours.
Every search of an entity should be managed by the entity's Searcher.

'searchable' meta model option can be set in two ways:

* **'searchable by { entityField1; entityField2; ...}** : Define entity fields to be indexed in a document. Values will be taken from entity instance. If the field is abstract it will call the entity's user class getter method.
* **'searchable'** : Standalone defaults to describe_by fields.

Each searchable by field can have field options:

- **boost intValue** : Boost field at index time.
- **filter_only** : Field will be indexed, but will not be searched for in the default query. It is intended to use in filters where you can set the field to filter for.
- **result_only** : Field will be returned in SearchResult as an extra field.

### Example

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#searchable)

For this entity, the following base class is generated, where you can see the declaration of the searchable fields, and the searcher singleton that will be used for filtering.

@inline-code(doc/samples/doc-project/src_managed/main/mm/tekgenesis/doc/g/SearchablePersonSearcherBase.java)

## Filter expression

Graphic representation of how to write a filter expression in a mm file:
**Filter Expression:**
@inlineGrammar(doc/src/main/markdown/entities/index/filter.xhtml,FilterExpression)

A sequence of filter fields.

**Filter Field:**
@inlineGrammar(doc/src/main/markdown/entities/index/filter.xhtml,FilterField)

**'searchableField'** : Name of the index field referenced (for example _deprecated). This field must be one of the searchable fields.
**'='** : makes the filter field a MUST condition (for example ```+fieldName:fieldValue```).
**'!='** : makes the filter field a MUST_NOT condition (for example ```-fieldName:fieldValue```).
**'expression'** : can be a single value, meaning the field must or must not be that value to match; or a list of values, meaning that the field must or must not be any of the list values to match.
**'when boolExpr'** : if it evaluates to false, the field will be ignored, if true, the rest filter will be evaluated. It is optional, and behaves as set to true when not present.

This allows defining an expression to match any of the searchable field, with a value of set of values. In the case of a single value, the match can be by equal or not equal, in the case of multiple values, the match is of type IN, or NOT_IN set.
The boolean expression is used to avoid filtering in some cases, it is optional, and considered as "true" when not set. 

### Example

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#filter)

## Programmatic filter expression

Basic filters can be done through mm filter expression, but if a more complex filter structure is needed, it can be achieved through Java. This could be used, for example, inside on_suggest methods.

To use filtering from Java a Fluent API is provided. 
For this purpose, Searcher classes are generated (a Searcher and a SearcherBase). 
The Searcher class, provides search methods to start the search, inherited from tekgenesis.index.InstanceSearcher - see [javadoc](../../javadoc/index.html). 
The list of all the searchable fields will be available in the SearcherBase class, so filtering can be specified through them.


###Specifying the filter.
*eq,neq,in,notIn : specify that the field is or not equal to a value or inluded or not in a set of values.


    
### Example
@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#complexFilter)
@inline-code(doc/samples/doc-project/src/main/java/tekgenesis/doc/Samples.java#complexFilter)

Of course this example should statically import AssignmentType's static methods.

