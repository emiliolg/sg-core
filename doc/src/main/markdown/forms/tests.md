# FormTests

The form must be **initialized** with *FormTests.initialize(class)* method, and from there we will have in a variable the user class generated for that form as an instance to make what's necessary to test it.
The fields are set with the "set" generated methods of the Base class.
In order to save, update, or click a button, the method that the mm calls must be called: create, update, onclickMethod. 
**NOTE**: when the create or update method are called it is **NOT necessary to write the line "Databases.commitTransaction()"** because those methods make the transactions for you.

# Initialize

This is a simple create and then update on a simple entity bound form:

```

form CategoryForm "Category" : Category
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    id       "Id #"        : idKey, check id > 0 : "Must be positive";
    name     "Name"        : name;
    descr    "Description" : descr, text_area(10, 80), default name;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

@Test public void categoryFormTest() {
        final CategoryForm form = initialize(CategoryForm.class);

        final int categoryId = 32;
        form.setId(categoryId);
        final String createName = "test";
        form.setName(createName);
        final String createDescr = "test description";
        form.setDescr(createDescr);
        form.create();

        // check there is no need to use Databases.commitTransaction() to see db changes
        final Category category = Category.findPersisted(categoryId);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isNotNull();
        assertThat(category.getName()).isEqualTo(createName);
        assertThat(category.getDescr()).isNotNull();
        assertThat(category.getDescr()).isEqualTo(createDescr);

        final CategoryForm updateForm = initialize(CategoryForm.class, category.keyAsString());
        final String updateName = "test";
        updateForm.setName(updateName);
        final String updateDescr = "updated description";
        updateForm.setDescr(updateDescr);
        updateForm.update();

        // check there is no need to use Databases.commitTransaction() to see db changes
        final Category upCat = Category.findPersisted(categoryId);
        assertThat(upCat).isNotNull();
        assertThat(upCat.getName()).isNotNull();
        assertThat(upCat.getName()).isEqualTo(updateName);
        assertThat(upCat.getDescr()).isNotNull();
        assertThat(upCat.getDescr()).isEqualTo(updateDescr);

    }  // end method categoryFormTest


```

# Util methods
## Hide, disable, Action messages, etc

This one shows how different util methods can be used to test, such as getFieldExpressions to check if the field is disabled, hidden, etc, getFieldOptions, getActionMessage.

```

form DisplayShowcase "Displays"

{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.DisplayShowcase", style "pull-right";
    };

    hidePanel "Hide Panel" : check_box;

    hiddenPanel "Hidden Panel" : horizontal, hide when hidePanel == true {

        displayCombo "Display" : DisplayOptions;

        vertical, hide when displayCombo == HIDE {
            text1 "Input #1" : String(20), default "disable", disable when displayCombo == DISABLE;
            text2 "Input #2" : String(20), hint "Change Input #1", disable when (displayCombo == DISABLE || text1=="disable"), placeholder "look up!";
        };
    };

    vertical {

        horizontal{
            disableCheck "Disable All": check_box;
            hideCheck "Hide Some": check_box;
        };

        input "Text Field": text_field, disable when disableCheck == true;
        combo "ComboBox": Options, disable when disableCheck == true, hide when hideCheck == true;
        radiogroup "RadioGroup": Options, radio_group , disable when disableCheck == true, hide when hideCheck == true;
        button "Button" : button, disable when disableCheck == true, on_click addRow;
        checkbox "Checkbox": check_box, disable when disableCheck == true, hide when hideCheck == true;

    };

    items :  table {
        checkItem "Disable" : Boolean;
        numberItem "#" : Int, disable when checkItem, check numberItem > 3 : "Must be greater than 3", hint checkItem ? "" : "##";
    };
}

    @Test public void formTests() {
        final DisplayShowcase form = initialize(DisplayShowcase.class);

        // test initial expressions;
        final Expressions textExpr = getFieldExpressions(form, DisplayShowcaseBase.Field.TEXT2);

        assertThat(textExpr.isDisabled()).isTrue();
        assertThat(textExpr.isHidden()).isFalse();
        assertThat(textExpr.getHint()).isEqualTo("Change Input #1");
        assertThat(textExpr.getPlaceholder()).isEqualTo("look up!");

        // test that combo_box options are the ones expected
        final KeyMap options = getFieldOptions(form, DisplayShowcaseBase.Field.DISPLAY_COMBO);
        assertThat(options.size()).isEqualTo(3);

        // see that message of adding a row's action is fine
        assertThat(getActionMessage(form.addRow())).isEqualTo("Row added!");

        // check changing disable and hide expressions;
        final Expressions comboExpr = getFieldExpressions(form, DisplayShowcaseBase.Field.COMBO);

        assertThat(comboExpr.isDisabled()).isFalse();
        assertThat(comboExpr.isHidden()).isFalse();

        form.setDisableCheck(true);

        assertThat(comboExpr.isDisabled()).isTrue();
        assertThat(comboExpr.isHidden()).isFalse();

        form.setHideCheck(true);

        assertThat(comboExpr.isDisabled()).isTrue();
        assertThat(comboExpr.isHidden()).isTrue();

        // check table expressions;
        final ItemsRow    row  = form.getItems().get(0);
        final Expressions expr = getFieldExpressions(form, DisplayShowcaseBase.Field.NUMBER_ITEM, 0);

        assertThat(expr.getHint()).isEqualTo("##");
        assertThat(expr.getPlaceholder()).isEmpty();
        assertThat(expr.isDisabled()).isFalse();
        assertThat(expr.isHidden()).isFalse();

        row.setCheckItem(true);

        assertThat(expr.getHint()).isEmpty();
        assertThat(expr.getPlaceholder()).isEmpty();
        assertThat(expr.isDisabled()).isTrue();
        assertThat(expr.isHidden()).isFalse();
    }  // end method formTests


```

## UIChange 

There is also the option to emulate a ui_change with setFieldCallingUiChange :

```
@Test public void uiChangeFormTest() {
        final MailFieldShowcaseForm form = initialize(MailFieldShowcaseForm.class);

        setFieldCallingUiChange(form, MailFieldShowcaseFormBase.Field.MAIL_CHANGE, "asdas@gmail.com");
        assertThat(form.getMail()).isEqualTo("whut@hotmail.com");
    }  // end method uiChangeFormTest
```

## I18n Tests

There is also the option to obatain a message localized, from some fields that are not posible to extract to enums such as hints, checks, etc.
It is important to specify really well the key that wants to be localized, this means that if it is a hint write "fieldname.hint":

```
@Test public void i18nFormTest() {
        final I18nForm form = initialize(I18nForm.class);

        final Locale es = new Locale("es");
        assertThat(getLocalizedMessage(form, es, "name")).isEqualTo("Nombre");
        assertThat(getLocalizedMessage(form, es, "name.hint")).isEqualTo("Escribí tu nombre");
        assertThat(getLocalizedMessage(form, es, "statement.is")).isEqualTo("Esto es Boca Juniors");
        assertThat(getLocalizedMessage(form, es, "hasName.check")).isEqualTo("No hay nombre");
    }  // end method i18nFormTest
```