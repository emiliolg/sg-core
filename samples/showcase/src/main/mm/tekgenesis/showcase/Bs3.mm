package tekgenesis.showcase;

form Bs3 "Grid"
on_load load
{

    header {
        message(title);
    };


    "String(20)" : String(20);
    "Width 30" : text_field, width 30;
    "Width 5" : text_field, width 5;
    "Default t_f" : text_field;

    tab : table {
        "Short String" : text_field, width 5;

        "Long Group" : horizontal {
            String(20), expand;
            String(20), expand;
        };
        "Short Fixed" : Options, width 5;
        "Long Fixed" : Options, width 45;
    };


}


form AddressShowcaseForm "Address"
on_load load
{
    header {
        message(title);
        "View Source" : label, link "/view/source/tekgenesis.showcase.TextFieldShowcaseForm", style "pull-right";
    };

    horizontal {
        vertical, col 8 {
            horizontal, label_col 4 {
                "Pais" : text_field;
                "Provincia" : text_field;
            };
            "Ciudad" : input_group {
                "Text Field 1" : text_field;
                "Text Field 2" : text_field, hint "Codigo postal";
            };
            "Direccion" : input_group {
                "Text Field 1" : text_field, placeholder "Buscar...", hint "Nombre de la calle", col 4;
                "Text Field 2" : text_field, hint "Numero", col 2;
                "Text Field 2" : text_field, hint "Piso", col 2;
                "Text Field 2" : text_field, hint "Depto/Oficina", col 2;
            };

            "Otros" : text_field;

            "Telefono" : text_field;

            "Entre" : input_group {
                "Text Field 1" : text_field, hint "Calle 1";
                "Text Field 2" : text_field, hint "Calle 2";
            };
        };

        vertical, col 4{
            map : map, style "no-label";
        };

    };
}


form GroupShowcaseForm "Groups"
 on_load load
 {

    header {
        message(title);
    };

    "Input group " : input_group {
        "Ignored" : String (20), col 4;
        "Ignored" : String (20);
        "Ignored" : String (20);
    };

    "Text" : String(20);

    "Label col 4" : horizontal, label_col 4 {
        "Text" : String(20);
        "Text" : String(20);
    };

    "Buttons" : horizontal {
        "Click me!" : button;
        "Button" : button;
        "Click me!" : button;
        "Button" : button;
        "Click me!" : button;
        "Button" : button;
        "Click me!" : button;
        "Button" : button;
    };

    "Labels" : horizontal {
        label, icon android;
        label, icon apple;
        label, icon windows;
        label, icon beer;
        label, icon ambulance;
        label, icon rocket;
        label, icon automobile;
        "text" : label;
    };

    "Custom label col" : horizontal, label_col 6 {
        "Label": String(20), label_col 2;
        "Really Long Label label_col 6": String(20);
    };


    "Tabs" : tabs {

        "Vertical" : vertical {
            "Text" : String(20);
            "Text" : String(20);
        };

        "Horizontal" : horizontal {
            "Text" : String(20);
            "Text" : String(20);
        };

    };


    table "Table" : table {
        disp "#" : display;
        "Input" : String (20);
        "Select" : Options;
    };

    bigTable "Big Table" : table {
        disp2 "#" : display;
        "Input" : String (20);
        "Select" : Options;
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
    };


    sub "Resize SubForm" : subform(ResizeSubForm), display "Click me !";
    "Display Widget" : display, is "Some text", on_click doStuff ;

}


form ResizeSubForm
    entity Address
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.AddressForm", style "pull-right";
    };
    id, internal, optional;
    street  "Street"      : street, text_field;
    city    "City"        : city;
    state   "State"       : state, hide when city == "CABA", optional when city == "CABA", reset;
    zip     "Postal Code" : zip, optional;
    country "Country"     : country;

    enlarge : toggle_button, icon plus;

    vertical, hide when !enlarge {

        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);
        "Input" : String (20);

    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };

}

form HideShowcaseForm "Hide" {

    header {
        message(title);
    };

    but "Hide" : toggle_button;

    "Default " : horizontal {
        String(20), placeholder "Input 1";
        String(20), placeholder "Input 2", hide when but;
        String(20), placeholder "Input 3";
    };

    "Pulled-Right " : horizontal {
        String(20), placeholder "Input 1";
        String(20), placeholder "Input 2", hide when but;
        String(20), placeholder "Input 3", style "pull-right";
    };

    horizontal {
        "Text" : String(20), col 8;
        "Pulled-Right" : button, style "pull-right";
    };


    horizontal {
        horizontal {
            "Btn 1" : button;
            "Btn 2" : button;
        };

        horizontal {
            "Btn 3" : button, style "pull-right";
            "Btn 4" : button, style "pull-right";
        };
    };

}


form ClientShowcase "Client"
on_load load
{
    header{
        message(title);
    };

    message(warning), default "Confirmar la direccion del cliente en el mapa";

    horizontal , col 6, offset_col 3 , hide when true{
        indicators : section{
                indicatorIcon : internal, optional;  // Indicator icon
                indicator : label, icon_expr indicatorIcon;
        };

        reference : vertical, style "indicatorReferences", col  3, offset_col 2{
            goldIcon " " : label, icon certificate, tooltip "High", style "gold";
            silverIcon " " : label, icon certificate, tooltip "Medium", style "silver";
            bronzeIcon " " : label, icon certificate, tooltip "Low", style "bronze";
        };
    };

    horizontal{
        vertical, col 10 {

            "Identificacion" : input_group {
                combo_box;
                String(10);
            };

            "Nombre" : input_group {
                String(10), hint "Nombre de pila";
                String(10), hint "Apellido";
            };

            "Contacto" : input_group {
                String(10), hint "E-Mail", placeholder "email@ejemplo.com";
                String(10), hint "Telefono";
            };

            "Otros" : input_group {
                combo_box, hint "Sexo", col 2;
                Date, hint "Fecha de Nacimiento", col 3;
                String(10), hint "CUIL/CUIT", col 5;
            };
        };

        vertical, col 2 {
            display, is "Codigo Unix";
            display, is "37.091.682";
        };
	};

    horizontal{
        addresses "Direcciones"  : section, style "cards", placeholder "None", col 10 {
            horizontal, style "alert alert-success", col 4{
                vertical, col 9 {
                    addressName : display;
                    addressLine : display, optional, hide when addressLine == null;
                    addressCity : display, style "no-label", optional;
                    addressStateCountry : display, style "no-label", optional;
                };

                vertical, col 3 {
                    mainAddress : toggle_button, icon star_o, icon_selected star;
                    deprecateAddress : toggle_button, icon trash_o;
                };
            };
        };
        addAddressButton "Add" : label, icon plus, col 2;
    };


}





