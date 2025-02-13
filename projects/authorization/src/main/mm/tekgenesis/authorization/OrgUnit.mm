package tekgenesis.authorization schema authorization;

entity OrgUnit "Organizational Unit"
    primary_key name
    described_by description
    cache
    searchable by {
        name;
        description;
    }
{
    name        : String(10);
    description : String(60);
    parent      : OrgUnit, optional;
    children    : OrgUnit*;
    props : entity OrgUnitProperties* {
        property : Property;
        value       : String(20);
    };
}
form OrgUnitForm "Organizational Unit"
    entity OrgUnit
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    edit : Boolean, internal;

    horizontal {
        vertical, col 4 {
            horizontal, style "pull-right margin5" {
                addChild "+": button, on_click addChild;
                delete "-": button, on_click deleteChild;
            };
            treeView : OrgUnit, tree_view, on_change nodeSelected, optional, style "well tree no-label";
        };

        vertical, col 8 {
            messageTitle: message(plain), is "New Organizational Unit ...", style "form-align message-title", hide when edit;
            name;
            description;
            validParent : Boolean, internal, default true;
            parent "Parent" : parent, required, on_change validateParent, check validParent : "I can't be my parent!";
            company "Company" : display, hide when !edit, optional;
            "Properties" : props, table, hide when count(property) == 0 {
                "Property" : property, display;
                type "Type"     : PropertyType, display;
                required : Boolean, internal;
                value "Value" : dynamic, optional when !required;
            };

            footer {
                save "Save" : button(save);
                button(cancel);
            };
        };
    };
}


