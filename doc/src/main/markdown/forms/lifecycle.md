# States a Form passes through

A form normally passes through five states during its lifetime: 

1. [Initialized](#initialized) (expressions are computed) 
2. [Parameterized](#parameterized) (optional parameters are applied)
3. [Loaded](#loaded) (load() method is called)
4. [Populated](#populated) (populate method is invoked if primary key was specified)
5. [Terminated](#terminated) (create, update, or deleted is successfully invoked)

This topic describes these states, and the transitions between them.

## Initialized

In this state, the form is initialized and model expressions are evaluated (e.g. defaults are computed). After this state, forms are initialized and field values matches 'default' and 'is' expressions.
@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#sampleForm)

In the above example, after form is initialized, values will assert:

* getName() returns a not null String value "Ruby"
* getCreated() returns a not null Integer value '2014' 
* getExpiration() returns a not null Integer value '2015'
* getEmail() returns null

After initialization is completed, the form will be parameterized, which is the next state. 

## Parameterized

In this state, the form is parameterized with optional arguments if defined. In order to allow form parameterization, parameters should be specified on definition.

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/parametrized/Samples.mm#sampleForm)

```
form SampleForm
    parameters name, created
{
    name : String, default "Ruby";
    created : Int, default 2014;
    expiration : Int, is created + 1;
    email : String, optional;
}
```

Form parameters are optional. Unlike a primary key, specified parameters can be fully or partially fulfilled, meaning: *name* and *created* can be both specified, or just one, or none of them.

In the above example, after form is initialized with *created* parameter set to* 2010*, values will assert:

* getName() returns a not null String value "Ruby"
* getCreated() returns a not null Integer value '2010' (specified parameter overwrites default value)
* getExpiration() returns a not null Integer value '2011'
* getEmail() returns null

After parameterization is completed, the form will be loaded, which is the next state. 

## Loaded

In this state, the form calls the *load()* method, if not overwritten the base empty method will be called. Load methods are useful to contextualize field values or specify appropriate field options.

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#sampleForm)
@inline-code(doc/samples/doc-project/src/main/java/tekgenesis/doc/SampleForm.java)

In the above example, after form is loaded, values will assert:

* getName() returns a not null String value "Ruby"
* getCreated() returns a not null Integer value '2014'
* getExpiration() returns a not null Integer value '2015'
* getEmail() returns a not null String value specified with the current logged user email

After load is completed, the form will be populated, which is the next state. 

*Note: If a primary key is defined on form, the populate method will be invoked on the next state, although primary key values will be available on this load state, before populate is performed. Sometimes primary key values can be convenient during load method as well*. 

## Populated

In this state, the form calls the *populate* method if defined. Populate method is only invoked if form is initialized with a primary key.  In order to be able to initialize a form with a given primary key, *primary_key* should be specified on definition, or form should be bind to an entity,
if neither of these is done, method will throw and exception.
If the Form is bound to an entity, the populate method should return such entity, if only primary_key is specified, that primary key could be returned. 
@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/populated/Samples.mm#sampleForm)

@inline-code(doc/samples/doc-project/src/main/java/tekgenesis/doc/populated/SampleForm.java)

Populate method is only invoked if form is initialized with a primary key, otherwise, nothing happens on this state.

In the above example, after form is populated with the specified primary key, values will assert:

* getName() returns a not null String value with the specified key.
* getCreated() returns a not null Integer value '2014'
* getExpiration() returns a not null Integer value '2015'
* getEmail() returns a nullable String value specified with the return call of the method *findEmailForUsername* on sample utility class *Users*.

After populate is completed, the form will eventually finalize after a terminate state is reached.

## Terminated

In this state, the form instance if discarded after one of the three terminate methods is reached. Terminate methods are *create*, *update*, and *delete*.

Terminated state and terminate methods are of special consideration on navigation, see [Navigation](navigation/navigation.html) topic.
