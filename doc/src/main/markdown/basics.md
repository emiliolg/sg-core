
# Project Structure
The basic structure for a Sui Generis project is the following:
## src folder
All project files should be stored in the src folder.
The structure for this folder is:

- main
    - mm
    - java
    - resources
    
### mm 
The **mm** folder contains the package and mm models for Sui Generis. All models with package structure should be stored here

### java
The **java** folder contains all java code regarding the project, in particular all classes implementing the mm models are here (not the base generated classes since they are in the src_generated folder)

### resources 
- resources
    - html
    - ...
    - public
        - js
        - css
        - ...

The **resources** folder contains all resources for the project.
Inside the resources folder, a **public** will be available to include all project public data. This includes css, js, audio, img, etc.
Css files to override default ones should be located inside the **public/css** folder. In here you can include the *theme.css* file and the *forms.less* file to override default css.

The **html** folder will contain all project [mustache](mustache.html) or jade files.

## src-generated folder
Automatically generated classes, that should not be modified by the user.  

<!-- ## resources folder -->

