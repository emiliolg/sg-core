#Type Definition

Type Definitions are used when a model is needed simply to hold data, and have some standard functionality that is derivable from the data itself.

@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/TypeSamples.mm#FromScratch)

For this type definition, some out of the box functionality such as:

* setters and getters
* *toString*: based on the json representation
* *equals* and *hashcode* methods.


##inheritance
Sui Generis Type Definitions support inheritance. Providing Type Definitions the capability to be derived from other Type Definitions, thereby inheriting fields and methods from that Type Definition.
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/TypeSamples.mm#ChildType)

###final Type Definitions
You can declare a Type Definition final. You use the final keyword in Type Definitions if you do not want your Type Definition to be extended by any other Type Definition.
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/TypeSamples.mm#FinalChildType)

###abstract Type Definitions
Sui Generis supports making the generated Type Definition class abstract, all though it does not support the abstract keyword at a metamodel level.
Making a Type Definition abstract is useful either when you do not want that Type Definition to be instantiable for some design reason, or you define some abstract methods in that Type, that will be present in the Type Definition's heirs.

##optional fields
Optional Fields are also supported in Type Definitions.
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/TypeSamples.mm#OptionalChildType)

<!-- compound types ver person. y tema constructories-->
<!-- fields can be optional ver nested types con optionals.-->
<!-- kotlin data class -->
<!-- scala case class -->
