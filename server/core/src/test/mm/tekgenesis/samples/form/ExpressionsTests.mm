package tekgenesis.samples.form;

form UpdateExpressionTest
    entity Some
{
    id;
    some;
    update : Boolean, is isUpdate();
}

form FormExpressionsTestDefaults "Simple Expressions"
{
    ignored : Int, default -1, disable;

    item1   : Int, default 10;
    item2   : Int;
    item3   : Int, default 3 * item1, on_change setItem2Value;

    total   : Int, is item1 + item2 + item3;
}

form FormExpressionsTestTables "Table Expressions"
{
    prev : Int, default 10, disable;

    table : table {
        col1    : Int, default 10;
        col2    : Int;
        col3    : Int, default 3 * col1, on_change setCol2Value;
        total   : Int, is col2 + col3;
    };

    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, table), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, table);
    };

    post : Int, is sum(total);
}

form FormExpressionsTestRelatedTables "Related Tables Expressions"
{
    first : table {
        a   : Int, is b + c;
        b   : Int, default 10;
        c   : Int;
    };

    ftot    : Int, is sum(a);

    second : table {
        x   : Int, is y + z;
        y   : Int, default 10;
        z   : Int, is sum(a);
    };

    stot    : Int, is sum(x);
}

form FormExpressionsTestRecursion "On Change Recursion Expressions"
{
    count "Count OnChanges calls" : Int, default 0;

    a : Int, default 10;
    b : Int, is a * 2;
    c : Int, is b * 2, on_change setATo20;

}

form FormExpressionsTestDefaultAndOnLoad "Default and OnLoad Expressions"
{
    credentials : String(10), on_change updateAvailability;
    available   : Int;
    reserved    : Int, default 2;
    valid       : Boolean, is reserved <= available;
}

form DependeciesForm
    primary_key product
{
    product : String;
    dependencies : table {
        dependency : String;
    };
    total : Int, internal, is rows(dependency);
}

form SubformsExpressions "Subforms Expressions"
{
    products : table {
        product : String, is dependencies.product;
        deps : Int, is dependencies.total;
        dependencies : subform(DependeciesForm), display "Edit " + dependencies.product;
    };

    total : Int, internal, is rows(product);
}

form DateExpressions {
    dateTime "DateTime" : DateTime, default today();
    date "Date" : Date, default now();
}

widget EmployeeSimpleWidget {
    first   : String, default "Juana I";
    last    : String, default "de Castilla";
    full    : String, is first + " " + last;
    legacy  : String;
}

form EmployeeSimpleProfile {
    name    : String, is employee.full;
    employee : widget(EmployeeSimpleWidget);
}

widget EmployeeRecursiveWidget {
    first   : String, default "Juana I";
    last    : String, default "de Castilla";
    full    : String, is first + " " + last;
    legacy  : String;
    manager : widget(EmployeeRecursiveWidget), optional;
}

form EmployeeRecursiveProfile {
    name    : String, is employee.full;
    manager : String, is employee.manager.full;
    employee : widget(EmployeeRecursiveWidget);
}

abstract widget OnChangeRecursiveWidget {
    value   : String, on_change valueChanged;
    child   : widget(OnChangeRecursiveWidget), optional;
}

form OnChangeRecursiveForm {
    value1  : String, is child.value, on_change value1Changed;
    value2  : String, is child.child.value, on_change value2Changed;
    value3  : String, is child.child.child.value, on_change value3Changed;
    value4  : String, is child.child.child.child.value, on_change value4Changed;
    child   : widget(OnChangeRecursiveWidget);
}

form OnChangeWidgetRecursiveForm {
    child   : widget(OnChangeRecursiveWidget), on_change rootChanged;
}

abstract widget OnChangeRecursiveWidgetWithMultiple {
    values  : table {
        value : String, on_change valueChanged;
    };
    child   : widget(OnChangeRecursiveWidgetWithMultiple), optional;
}

form OnChangeRecursiveWidgetWithMultipleForm {
    child   : widget(OnChangeRecursiveWidgetWithMultiple), on_change rootChanged;
}

abstract widget OnChangeRecursiveWidgetWithMultipleAndDefault {
    values  : table {
        value : String, on_change valueChanged, default "v";
    };
    child   : widget(OnChangeRecursiveWidgetWithMultipleAndDefault), optional;
}

form OnChangeRecursiveWidgetWithMultipleAndDefaultForm {
    child   : widget(OnChangeRecursiveWidgetWithMultipleAndDefault), on_change rootChanged;
}

abstract widget RecursiveWidgetWithinMultiple {
    widgets : table {
        inner : widget(RecursiveWidgetWithinMultiple);
    };
    value   : String, on_change valueChanged, default "v";
}

form RecursiveWidgetWithinMultipleForm {
    child   : widget(RecursiveWidgetWithinMultiple);
}