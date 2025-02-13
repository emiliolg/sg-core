#Widget Definition

Custom, user defined, reusable widgets can be modeled using Widget Definitions.
These can be bound to Entities or Type Definitions, or not. And can be used in multiple Forms.
 
If they are bound, they are defined as follows:
@inline-code(samples/showcase/src/main/mm/tekgenesis/showcase/WidgetDefinitionShowcase.mm#addressWidget)

Unbound widgets can also be defined in a manner similar to:

@inline-code(samples/showcase/src/main/mm/tekgenesis/showcase/WidgetDefinitionShowcase.mm#binaryNode)

## Widget Composition.

Since widgets can be composed, within widgets or tables, to be able to refer to a specific widget, notation father.son, and table#row are supported.
To use this, check BaseQualifiedWidget and IndexedWidget classes.
 
##Permissions
Widget do not have permission support. They inherit the permissions of the form they are shown in.
 
 
 