# Metamodels

Sui Generis main artifact is the metamodel definition file: *.mm

Metamodel files contain definitions for [Entities](../entities/entities.html), [Views](../entities/views.html), [Enumerations](../enums.html), [Forms](../forms/forms.html), [Tasks](../tasks.html), [Types](../types.html), [Handlers](../handlers.html), and [Menus](../menus.html).

### Scoping 

All definitions have a domain scope defined by the package statement at the beginning of the file. Import statements are also allowed to avoid full qualified references on definitions.

### Code Generation

For each metamodel a base class and an optional user class are generated. Class structure varies depending on the related metamodel. The base class is abstract and it is overwritten each time the model is updated, it should never be modified by users. The user class extends the base class and is only generated if it does not exist, and it is intended to be changed by the user to extend the model's behaviour.

### I18N

Metamodels internationalization includes full development and runtime integration, offering plugin mechanisms for localizing messages, code APIs for retrieving localized messages, and automatic request Locale binding for out of the box localization of metamodels.
