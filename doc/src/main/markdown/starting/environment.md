# Development Environment

Sui Generis works seamless with Intellij Idea IDE and Gradle build system. However the user can develop using any other Java IDE and build system.

## Intellij Idea plugin

Sui Generis is integrated with IDEA to make the definition of metamodels easier. This integration is made through an IDEA Plugin.

Some plugin features include:

- *Metamodel files creation and edition*: create the '.mm' files where metamodels are defined.
- *Application run and debug*
- *Syntax highlighting*
- *Reference navigation*
- *Refactoring and usages*
- *Code completion*
- *Code generation*: Code is automatically generated when metamodel is saved.
- *Inspection warnings*
- *Internationalization and Localization integration*
- *Sql Generation*

### Run configurations

Two types of Run/Debug configurations are provided:

* Sui Generis Configuration: Allows to run a Sui Generis Server from a project module
* Sui Generis Task Configuration: Allows to run a Sui Generis Task.

## Gradle integration

Sui Generis development using Gradle build system is very easy. The following is a build.gradle sample to start developing with Sui Generis and Gradle. Gradle 2.x or higher is required.

```gradle
plugins {
  id "tekgenesis.mm" version "0.3.8"
}
apply plugin: 'idea'
idea {
  project {
    //if you want to set specific jdk and language level
    jdkName = '1.8'
    languageLevel = '1.8'
  }
}

tekgenesis {
        token = '.........'
       
        dependencies {
                suigeneris  'com.tekgenesis:suigeneris:3.0.8977-master'
        }
}
subprojects {
        apply plugin: 'java'
        apply plugin: 'idea'
        apply plugin: 'tekgenesis.mm'
}
```

The gradle plugin adds the following tasks:

* *mm*: Generate java classes and .sql files from the .mm files.
* *metamodelsJar*: Generates a jar with the metamodels from the project. Useful to use in other projects.
* *servicesJar*: Generates a jar with the remote services and associated types and enum. Useful to invoke services from other project.
* *solveDistributions*: Update dependencies.
