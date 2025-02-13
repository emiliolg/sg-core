# Form Extensions

Form extensions enable you to pragmatically modify a form.
The use cases for this are very specific, such as:

- From module B, you wish to extend a form in module A to add a certain logic.
NOTE: To preserve the forms typing checking, its recommended to only use extensions to only add a subform to the original form.
- There are certain fields you wish to add dynamically, for example, new columns to a table.


## Having a Form in module A

```mm
    form MyForm "My Form"
    {
        textField "Text Field" : String, on_ui_change textFieldChanged;
        navigate "Navigate" : button;
    }
```

## Create a FormExtension class


### Basic extend

The `extend` method enables you to programmatically add new fields, or modify exisintg ones. You can also add new click and change listeners:  

```java
    public class MyFormExtension extends FormExtension<MyForm, Void> {
    
        @Override public Class<MyForm> getFormType() { return MyForm.class; }
    
        @Override public void extend(final Extender<MyFormExtension, Void> extender)
            throws BuilderException
        {
            // modify an existing field option
            extender.findWidget(TEXT_FIELD).defaultValue("New default value");                                
        }            
    }
```


### Intercepting FormInstance methods from original class

A form extension class is actually a FormInstance, so you can override the default methods to intercept and add some logic (you can also user form listeners if this is the only thing you need).

```java        
    @NotNull @Override public Action create() {
        // before create
        return super.create();
       // after create
    }
```

### Adding new widgets

Adding a new field will return a FormFieldRef, that can latter be used to retrive or the the model value.

```java
    private FormFieldRef dynamicTextField = null;
    
    @Override public void extend(final Extender<MyFormExtension, Void> extender)
        throws BuilderException
    {
        // add a new field on top
        dynamicTextField = extender.addBefore(TEXT_FIELD, field("Extended Field!").id("dynamicTextField"));
    }

    @Override public void load() { f.set(dynamicTextField, "onLoad value for dynamic field"); }
```

### Adding listeners

You can add on_change or on_click listeners, to existing or new fields.

```java
    private FormFieldRef dynamicTextField = null;

    @Override public void extend(final Extender<MyFormExtension, Void> extender)
        throws BuilderException
    {
        // add a new field on top
        dynamicTextField = extender.addBefore(TEXT_FIELD, field("Extended Field!").id("dynamicTextField"));
        
        // add an on_change listener
        extender.onChange(dynamicTextField, (Function<MyFormExtension, Action>) MyFormExtension::onDynamicFieldChange);
        
        // add on click listener
        extender.onClick(NAVIGATE, (Function<MyFormExtension, Action>) MyFormExtension::navigate);
    }

    /** Invoked on navigate click. */
    @NotNull public Action navigate() { return actions.navigate(MyFormExtension.class); }

    /** Invoked on dynamicTextFieldChange. */
    public Action onDynamicFieldChange() {
        // set a new value
        f.set(dynamicTextField, "onChange value for dynamic field");
        return actions.getDefault();
    }
```



## Declare your service extension on the META-INF folder

Under `/resources/META-INF/services`, create a file named `tekgenesis.form.extension.FormExtension`. 

```
    tekgenesis.demo.MyFormExtension
```