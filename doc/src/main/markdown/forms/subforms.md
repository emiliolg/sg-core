# Subforms

They are widgets that represent re-utilization in Sui Generis forms. They allow including a full scale form inside another one. There are three ways of inclusion:

- Inline
- Dialog
- Anchor
 
# Example

In this example AddressForm is being included on ClientForm using the default inclusion method, Dialog. A handler will be rendered on ClientForm that upon clicking it, will open AddressForm within a modal dialog. 

```
form ClientForm "Client Form"
{
    header { message(title); };
    
    firstName "First Name" : text_field;
    lastName "Last Name" : text_field;
    
    address, subform(AddressForm);

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form AddressForm "Address Form"
{
    header { message(title); };
  
    street  "Street"      : text_field;
    city    "City"        : combo_box;
    state   "State"       : combo_box;
    country "Country"     : combo_box;
    zip     "Postal Code" : text_field;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
```

# Code Generation

Subforms generates the following code on the base class of the parent form:

```
    /** Create and set a new AddressForm instance */
    @NotNull public AddressForm createAddress() { return f.init(Field.ADDRESS, AddressForm.class); }

    /** Create and populates set a new AddressForm instance with a pk */
    @NotNull public AddressForm createAddress(@NotNull String key) { return f.init(Field.ADDRESS, AddressForm.class, key); }

    /** Get the AddressForm if defined, or null otherwise.*/
    @Nullable public AddressForm getAddress() { return f.subform(Field.ADDRESS, AddressForm.class); }
```

# Inline

You specify a subform to be inline by adding the 'inline' field option to it. 
The subform will be rendered inline where the subform widget is located. Inline subforms support multiple levels of nesting.
  
```
    address, subform(AddressForm), inline; 
```

>The only caveat here is that the subform instance must be created in the load method.

```
public class ClientFormImpl extends ClientForm{
...
    public void load(){
        createAddress();
    }
...   
}
```
 


# Dialog

This is the default inclusion method. The child subform will be rendered on a modal dialog that the user can open by clicking on the handler located on the parent form. If the child form has a footer group, it will be striped and by default Ok & Cancel buttons will be rendered instead. Ok button will let user close the dialog approving the changes and the Cancel button will cancel any edition that is made to the fields in the dialog. This can be customized using different values as the second argument.

```
    address, subform(AddressForm, ok_cancel); //the default, ok_cancel can be omitted.
    address, subform(AddressForm, close); //only a close button will be added.
    address, subform(AddressForm, ok_cancel_validate); //like the default, but will validate upon on exit.
    address, subform(AddressForm, none); //no buttons will be added.
```

# Anchor

Using anchor is pretty much the same as dialog, but instead of a dialog, the subform will be rendered on the selected anchor when the user clicks on the handler.

```
form ClientForm "Client Form"
{
    header { message(title); };

    firstName "First Name" : text_field;
    lastName "Last Name" : text_field;
    
    address "Address" : subform(AddressForm), display address.street, on placeholder;

    placeholder    : anchor;
    
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
```

# Field option display

On both, dialog and anchor use cases, display field option lets customize the handler's text. It can reference subform's fields and parent form ones. To reference subform's fields you must use dot notation starting with the id of the subform.


# Subform Configuration

Subforms can be configured using the forms configure method. Right now, only visibility can be changed. More on this [here](widgets_configuration.html)  
