
# Basic Syntax

## Metamodel File

On each metamodel file, a package decalartion and multiple different metamodels can be included.
@inlineGrammar(metModelFile)


## Defining a package
Package specification should be at the top of the source file:

@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/FormSnippets.mm#package)
@inlineGrammar(packageDefinition)


It is required to match directories and packages: source files can not be placed arbitrarily in the file system.

## Defining Metamodels

This is a sample declaration for an entity:
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/FormSnippets.mm#basicEntity)

## Entity MetaModel declaration 
MetaModel declaration should be of the form: 
@inlineGrammar(entity)

And each Entity Field Declaration should be of the form: 
@inlineGrammar(entityField)

See [Metamodels](metamodels.md) for metamodel type full listing.

## Full Grammar

see Full [Grammar](../MetaModel.xhtml)

