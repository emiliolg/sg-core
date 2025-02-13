//#package
package tekgenesis.snippets;
//#package
//#basicEntity
entity BasicEntity
    primary_key attribute{
        attribute : String;
}
//#basicEntity
form SimpleForm{
    formElement "label": label;
    formElement1 "another" : text_area;
}

form SimpleFormWithInnerAndMethods{
    formElement "label": label;
    formElement1 "another" : text_area, on_change anotherChange;
    tableId "tableId"  : table {
        group           : Boolean, internal;
        featureName     : String, display;
        featureValue    : String, display;
    };
}

form SimpleEntityBasedForm entity BasicEntity{
    formElement "label": label;
    attribute;
}
