package tekgenesis.showcase;



form OptionWidgetsForm on_load load {
    // combo
    "Combo" : horizontal {
        simpleE "Simple entity" : SimpleEntity, combo_box, hint "Simple entity";
        simpleButton "Set Simple" : button, content_style "btn-default", on_click setSimpleE;
        simpleOptionButton "Set Simple Options" : button, content_style "btn-default", on_click setSimpleEOptions, hint "This options contains the invalid value";
    };
    // tags combo
    "Tags" : horizontal {
        tagsSimpE "Simple tags" : SimpleEntity, tags_combo_box, hint "Simple entity tags";
        tagsButton "Set Tags" : button, content_style "btn-default", on_click setTagsE;
        tagsOptionButton "Set Tags Options" : button, content_style "btn-default", on_click setTagsEOptions, hint "This options contains the invalid value";
    };
    // pick list
    "PickList" : horizontal {
        pickSimpE "Simple Picklist " : SimpleEntity, pick_list, hint "Simple entity picklist";
        pickButton "Set Picklist " : button, content_style "btn-default", on_click setPicklistE;
        pickOptionButton "Set Picklist Options" : button, content_style "btn-default", on_click setPicklistEOptions, hint "This options contains the invalid value";
    };
    // breadcrumb
    "Breadcrumb" : horizontal {
        breadcrumbSimpE "Simple Breadcrumb " : SimpleEntity, breadcrumb, hint "Simple entity Breadcrumb";
        breadcrumbButton "Set Breadcrumb " : button, content_style "btn-default", on_click setBreadcrumbE;
        breadcrumbOptionButton "Set Breadcrumb Options" : button, content_style "btn-default", on_click setBreadcrumbEOptions, hint "This options contains the invalid value";
    };
    // checkbox group
    "CheckGroup" : horizontal {
        checkSimpE "Simple CheckGroup " : SimpleEntity, check_box_group, hint "Simple entity CheckGroup";
        checkButton "Set CheckGroup " : button, content_style "btn-default", on_click setCheckGroupE;
        checkOptionButton "Set CheckGroup Options" : button, content_style "btn-default", on_click setCheckGroupEOptions, hint "This options contains the invalid value";
    };
    // radio group
    "RadioGroup" : horizontal {
        radioSimpE "Simple RadioGroup " : SimpleEntity, radio_group, hint "Simple entity RadioGroup";
        radioSButton "Set RadioGroup " : button, content_style "btn-default", on_click setRadioGroupE;
        radioOptionButton "Set RadioGroup Options" : button, content_style "btn-default", on_click setRadioGroupEOptions, hint "This options contains the invalid value";
    };

    button "Validate": button(validate), on_click validate;
}

enum OrgType {
    RETAIL; LOGISTIC;
}

enum Org with {
    id : Int;
    type : OrgType;
    show : Boolean;
}
{
    GARBARINO : "Garbarino", 0, RETAIL, true;
    COMPUMUNDO : "Compumundo", 1, RETAIL, true;
    LOGISTICS : "Logistics", 2, LOGISTIC, false;
}

form Orgs {

    header { message(title); };

    allOrg "All Orgs" : Org;

    filteredOrgsByType "Filtered by type" : Org, filter (type != LOGISTIC), hint "Only RETAIL";

    filteredOrgsByShow "Filtered by show" : Org, filter (show = true), hint "Only show = true";
}
