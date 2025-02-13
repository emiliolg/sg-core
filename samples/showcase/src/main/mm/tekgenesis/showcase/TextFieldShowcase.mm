package tekgenesis.showcase schema showcase;

type SignedAmount = Decimal(9,2), signed;

form SignedForm {

    header { message(title); };

    typeSignedAmount : SignedAmount;
    widgetSignedAmount : Decimal(9,2), signed;
}

entity TextShowcase
    primary_key idKey
    searchable
{
    idKey : Int;
    txt : String(20);
    date : Date;
    bool : Boolean;
    option : Options;
    entity : SimpleEntity;

    prop : entity MyProp* {
        type : PropertyType;
        value : String(20);
    };
}

entity SimpleEntity
    primary_key name
    searchable
    described_by name, description
{
    name : String;
    description : String(30);
}


form TestForm "Test Form"
    entity SimpleEntity
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    name        "Name"        : name;
    description "Description" : description;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

type UnsignedInteger = Int(8);
type SignedInteger = Int, signed;

entity Numbers
    primary_key name
    searchable
    described_by name
{
    name : String;

    unsignedInt4 : Int(4);
    signedInt5 : Int(5), signed;
    signedToBeUnsigned: Int, signed;

    unsignedInteger : UnsignedInteger;
    signedInteger : SignedInteger;

    unsignedDecimal52 : Decimal(5,2);
    signedDecimal52 : Decimal(5,2), signed;

    unsignedReal : Real;
    signedReal : Real, signed;
}

form NumbersForm "Numbers"
    entity Numbers
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    name "Name"                  : name;

    unsignedInt4 "Unsigned Int4"         : unsignedInt4;
    signedInt5 "Signed Int5"           : signedInt5;

    unsignedInteger "Unsigned Integer"      : unsignedInteger;
    signedInteger "Signed Integer"        : signedInteger;

    unsignedDecimal52 "Unsigned Decimal(5,2)" : unsignedDecimal52;
    signedDecimal52 "Signed Decimal(5,2)"   : signedDecimal52;

    unsignedReal "Unsigned Real"         : unsignedReal;
    signedReal "Signed Real"           : signedReal;

    signedToBeUnsigned "Unsigned int" : signedToBeUnsigned, unsigned, hint "Signed in the entity, unsigned in form";

    footer {
        save : button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form ListingSimpleEntities "Listing Simple Entities"
{
    header {
        message(title);
    };

    reload "Reload" : button, on_click reload;

    entities    : SimpleEntity, table, on_change saveSimpleEntity, on_load loadSimpleEntities {
        name "Name"        : name;
        desc "Description" : description;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, entities), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, entities), on_click removeSimpleEntity;
    };
}


form SimpleEntityForm "Simple Entity Form" entity SimpleEntity;

form SimpleEntitiesList {

    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SimpleEntities", style "pull-right";
    };

    entities "Simple Entities" : SimpleEntity, table(4), on_load load {
        name, display, link_form SimpleEntityForm, link_pk name;
        description, display;
    };

    add "Add new one" : button, on_click createNewOne;
}

form TextShowcaseForm "Text Showcase Form" entity TextShowcase;

form ViewShowcaseForm "Text View Showcase Form" entity TextShowcase on_load onLoad
{
    header {
        message(title);
        search_box, style "pull-right";
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TextShowcaseForm", style "pull-right";
    };

    id   "Id"   : idKey, display;
    txt "Text" : txt, display;
    date "Date" : date, display;
    bool "Bool" : bool, display;
    lon "Long" : String(20), display(10), default "12345678901234567890";
    option "Option" : option, display;

    disp1 "Display with icon" : display, icon cog;
    disp2 "Display with icon & tooltip" : display, icon cog, tooltip "nice tooltip";
    disp3 "Display with icon" : display, is "some", icon cog;
    disp4 "Display with icon & tooltip" : display, is "some", icon cog, tooltip "nice tooltip";

    entity "Entity" : entity, display, link_form SimpleEntityForm;
    entityWithImageDisplay "Entity with Image Display " : SimpleEntity, display, link_form SimpleEntityForm, with_image;


    multipleReal "Multiple Real" : Real, display, multiple;
    multipleLon "Multiple Long" : String(20), display(10), multiple, draggable;

    multipleEntity "Multiple Entity" : SimpleEntity, display, multiple, link_form SimpleEntityForm;
    multipleEntityWithImage "Multiple Entity with Image" : SimpleEntity, display, multiple, link_form SimpleEntityForm, with_image;

    props "Props": prop, table {
        type "Type 1" : type, on_change render;
        value "Value 1" : dynamic;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, props), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, props);
    };
}

form LinkShowcaseForm "Link Showcase Form"
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.LinkShowcaseForm", style "pull-right";
    };

    linkOption "Label" : vertical {
        labelLink "Eith 'link'" : label, link "http://www.tekgenesis.com";
        labelWithBlank "eith 'link' and 'blank_target'" : label, link "http://www.tekgenesis.com", target_blank;
        labelWithLinkForm "With 'link_form'" : label, link_form ViewShowcaseForm;
        labelWithLinkFormPk "With 'link_form' and 'link_pk' 1" : label, link_form ViewShowcaseForm, link_pk "1";
    };

    displayOption "Display" : vertical {
        displayLink "With 'link'" : display, link "http://www.tekgenesis.com", default "Tekgenesis";
        displayWithBlank "With 'link' and 'blank_target" : display, link "http://www.tekgenesis.com", target_blank, default "Tekgenesis";
        displayWithLinkForm "With 'link_form'" : display, link_form ViewShowcaseForm, default "ViewShowcaseForm";
        displayWithLinkFormPk "With 'link_form' and 'link_pk' 1" : display, link_form ViewShowcaseForm, link_pk "1", default "ViewShowcaseForm/1";
    };

    sugguestOption "Sugguest" : vertical {
        sugguestLink "With 'link'" : SimpleEntity, link "http://www.tekgenesis.com";
        sugguestWithBlank "With 'link' and 'blank_target'" : SimpleEntity, link "http://www.tekgenesis.com", target_blank;
        sugguestWithLinkForm "With 'link_form'" : SimpleEntity, link_form TestForm;
        sugguestWithLinkFormPk "With 'link_form' and 'link_pk' 1" : SimpleEntity, link_form TestForm, link_pk "1";
    };
}

entity TextFieldShowcase "TextFieldShowcase"
    primary_key idKey
    searchable
{
    idKey : Int;
    f1 : String(50), optional;
    f2 : String(50), optional;
    f3 : String(50), optional;
    f4 : String(50), optional;
    patente : String(6), optional;
    money : Decimal(10,2), optional;

    t1: Decimal(4,2), optional;
    t2: Decimal(4,2), optional;
    t3: Decimal(4,2), optional;
    t4: Decimal(4,2), optional;
    a1: Int, optional;
    a2: Int, optional;
    a3: Int, optional;
    a4: Int, optional;
    html: String, optional;
}

type String40 = String(40);

form TextFieldSecret {

    header { message(title); };

    text "Text" : String(35), secret, custom_mask "####";

    unmasked "Unmasked Text" : String(35), default text;

}

form TextFieldShowcaseForm "TextField"
    entity TextFieldShowcase
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.TextFieldShowcaseForm", style "pull-right";
    };

    idKey;
    html : html, rich_text_area;

    textField25 "TextField25" : text_field(25), hint "text_field(25)";
    string40 "type String40 = String(40)" : String40, text_field, hint "type String40 = String(40)";

    "Strings" : vertical {
        f1 : f1, hint "String(50).";
        f2 : f2, text_field, hint "String(50), text_field.";
        f3 : f3, text_field(30), hint "String(50), text_field(30).";
        f4 : f4, text_field(30), style "autoWidth", hint "String(50), text_field(30), style autoWidth.";
    };

    "Decimal" : vertical {
        t1 : t1, hint "Decimal(4,2).";
        t2 : t2, text_field, hint "Decimal(4,2), text_field.";
        t3 : t3, text_field(20), hint "Decimal(4,2), text_field(20).";
        t4 : t4, text_field(20), style "autoWidth", hint "Decimal(4,2), text_field(20), style autoWidth.";
        t5 "Signed Decimal" : Decimal(4,2), signed, hint "Signed Decimal(4,2)";
        t6 "Signed Decimal" : Decimal(4,2), signed, hint "Signed Decimal(4,2)", default -2.5;
    };

    "Int" : vertical {
        a1 : a1, hint "Int";
        a11 "Signed Int" : Int, signed, hint "Signed Int";
        a12 "Signed Int" : Int, signed, hint "Signed Int", default -2;
        a2 : a2, text_field, hint "Int, text_field.", mask raw;
        a3 : a3, text_field(20), hint "Int, text_field(20).";
        a4 : a4, text_field(20), style "autoWidth", hint "Int, text_field(20), style autoWidth.";
    };

    "Masks" : vertical {
        patente, custom_mask "AAA-###";
        m1 "Percent": Decimal(2,1), mask percent;
        m2 "Scientific": Decimal(10,2), mask scientific;
        m3 "Stock Int": Int, signed, mask unit, default -3;
        m4 "Stock Int": Int, signed, mask unit, default -4;
    };

    "Display Masks" : vertical {
        patente2 "Patente": String(6), display, custom_mask "AAA-###", default "ABA541";
        m1d "Percent": Decimal(2,1), display, mask percent, default 0.66;
        m2d "Scientific": Decimal(10,2), display, mask scientific, default 3.2;
        m3d "Stock Int": Int, display, signed, mask unit, default 11;
        m4d "Stock Int": Int, display, signed, mask unit, default -12;
    };

    "Icons" : vertical {
        i1 "Mail": String(20), placeholder "some@example.com", icon envelope;
        i2 "Password": String(20), icon key;
    };

    "Delay and Threshold" : vertical {
        changeText "Change text 1": String, optional , on_change changedText, change_threshold 4, change_delay 0, hint "change_threshold 4, delay 0";
        uiChangeText "UI Change text 1": String, optional , on_ui_change changedText, change_threshold 4, hint "change_threshold 4, no delay set (will use default)";
        changeTextDelay "Change tex 2t": String, optional , on_change changedText, change_delay 4000, hint "No threshold (0) and delay 4 seconds";
        uiChangeTextDelay "UI Change text 2": String, optional , on_ui_change changedText, change_threshold 4, change_delay 4000, hint "change_threshold 4 and delay 4 seconds";
    };

    "Calculator" : vertical {
        c1 "Decimal Signed large": Decimal(10,2), signed, optional, hint "Decimal(10,2) signed, try a large negative decimal";
        c2 "Decimal Signed short": Decimal(4,2), signed, optional, hint "Decimal(4,2) signed, try a large negative decimal and see how its formatted to signed (4,2)";
        c3 "Decimal short": Decimal(4,2), optional, hint "Decimal (4,2), try a large negative decimal and see how its formatted to (4,2)";
        c4 "Int signed": Int, signed, optional, hint "Int signed, try a signed integer, or a decimal and its casted to integer";
        c5 "Int": Int, optional, hint "Int, try signed integer or decimal and its casted to integer without sign";
        c6 "Real signed" : Real, signed, optional, hint "Real signed, try a real number signed";
        c7 "Real" : Real, optional, hint "Real, try a real signed and see how it is unsigned";
        table "Table" : table, on_click createNewRow {
            calc "Open Calculator" : Int, optional, icon minus_square;
        };
        add "Add new row" : button, on_click createNewRow;
    };
}

form CustomMaskForm on_load onLoad {
    patNew "Custom masks (AA-###-AA, AAA-###)": text_field, custom_mask("AA-###-AA", "AAA-###");
    patNew3 "Custom masks (AAA-##-##, AAA-###)": text_field, custom_mask("AAA-##-##", "AAA-###");
    s1: String, internal;
    s2: String, internal;
    lonely "One Custom Mask": text_field, custom_mask("AAA");
    fields "Custom masks bound fields": text_field, custom_mask(s2, s1);
    patNew2 "Custom mask": text_field, custom_mask "AAA-##";
    butt: button(validate), on_click validate;
}

form CustomMaskTestForm "Custom Mask Expressions" on_load onLoad {
    uniqueMask : text_field, custom_mask "AA-##";
    threeMasks   : text_field, custom_mask ("AA-#-##", "AA-##", "A#A-#A#");
    s1: String, internal;
    s2: String, internal;
    fromFieldMask : text_field, custom_mask s1;
    fromFieldMasks : text_field, custom_mask (s1, s2);
}

form PasswordStrengthMeter {

    header { message(title); };

    name "Name" : String(100);
    email "Email" : String(100);
    password "Password" : password_field, hint "This is a hint!", metering 4, check length(trim(password)) > 0 : "Password cannot be empty!";
    confirmPassword "Confirm" : password_field, metering 4, check confirmPassword == password : "Passwords mismatch!";

    validate : button(validate), on_click validated;
}

entity Label
    searchable
{
    withLabel1 "Entity Label" : String;
    withLabel2 "Entity Label" : String;

    noLabel1 : String;
    noLabel2 : String;
    noLabel3 : String;
    noLabel4 : String;

    some "Some" : String;
}


form LabelShowcase "Label Showcase"
    entity Label
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };

    "Id"   : id, internal, optional;

    bindingsWithLabels "Bindings With Labels" : vertical {
		withLabel1, hint "Binding with label and with no form label";
	    noLabel1, hint "Binding with no label and with no form label";
	    "Form Label" : noLabel2, hint "Binding with no label and with form label";
	};

    bindingsNoLabels "Bindings No Labels" : vertical {
		withLabel2, no_label, hint "Binding with label and with no form label & no_label";
	    noLabel3, no_label, hint "Binding with no label entity and with no form label & no_label";
	    "Form Label" : noLabel4, no_label, hint "Binding with no label and with form label & no_label";
	};


    withLabels "With Labels" : vertical {
		formLabel1 "Form Label" : text_field, hint "Form field with label";
	    formLabel3 : text_field, hint "Form field with no explicit label";
	    formLabel5 "" : text_field, hint "Form field with empty string label";
	};

    noLabels "No Labels" : vertical {
		formLabel2 "Form Label" : text_field, no_label, hint "Form field with label & no_label";
	    formLabel4 : text_field, no_label, hint "Form field with no explicit label & no_label";
        formLabel6 "" : text_field, no_label, hint "Form field with empty string label & no_label";
	};


    inputGroupWithLabels "Input Group With Labels" : input_group {
        formLabel12 "Form Label" : text_field, hint "Form field with label";
        formLabel32 : text_field, hint "Form field with no explicit label";
        formLabel52 "" : text_field, hint "Form field with empty string label";
    };

    inputGroupNoLabels "InputGroup No Labels" : input_group {
		formLabel22 "Form Label" : text_field, no_label, hint "Form field with label & no_label";
	    formLabel42 : text_field, no_label, hint "Form field with no explicit label & no_label";
        formLabel62 "" : text_field, hint "Form field with empty string label & no_label";
	};

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity TestProtected {
    name: String;
    bla: String, protected;
    inners: entity InnerTestProtected* {
        desc: String;
        length: Int, protected;
    };
}


form TestProtectedForm "Test Protected Form"
    entity TestProtected
{
    header {
        message(entity), col 12;
    };
    "Id"     : id, internal, optional;
    "Name"   : name;
    "Bla"    : bla;
    "Inners" : inners, table {
        "Desc"   : desc;
        "Length" : length, mask decimal;
    };
    horizontal, style "margin-top-20" {
        button(add_row, inners), style "margin-right-5";
        button(remove_row, inners);
    };
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}


form MailFieldShowcaseForm "Mail field Form" on_load load {
    mail "Mail" : mail_field(syntax), hint "Syntax only";
    blurChange "Mail blur" : mail_field(domain), optional , hint "Domain only";
    mailChange "Mail change" : mail_field(address), on_ui_change changedEmail, hint "Address only (will check domain too)"; //ui_change used in FormTestsTest
    mailSuggest "Mail suggest" : mail_field(domain_address), on_suggest suggestMails, hint "Domain and address";
    mailSuggestSync "Mail suggest sync" : mail_field(syntax_domain), on_suggest_sync suggestSyncMails, hint "Syntax and domain";
    mailOptions "Mail options" : mail_field, hint "All";
    mailTable : table {
        tableChange "Table suggest" : mail_field(syntax), on_change tableMailChange, hint "Syntax only";
        tableSuggest "Table suggest" : mail_field, on_suggest tableMailSuggest, hint "All";
        tableSuggestSync "Table suggest sync" : mail_field, on_suggest_sync tableMailSuggestSync, hint "All";
    };
    uiTable : table {
        name "Name" : String(50), on_ui_change changedNameUpdateNick; //ui_change used in FormTestsTest
        nick "Nick" : String(50) ;
        mailUi "Mail" : String(50), mail_field, hint "All";
        number "Number" : String(10);
    };

   submit "Submit" : button(save);
}

form I18nForm {
    name "Name": String, hint "Write your name";
    statement: String, is "This is Boca Juniors";
    hasName: String, check length(name) <= 0 : "No name", check length(name) >=20 : "Name is too long";
}

form HtmlGeneratorForm {

    header {
        message(title);
        viewSource "View Source" : label, link "/sg/view/source/tekgenesis.showcase.HtmlGeneratorForm", style "pull-right";
    };

    resource "Upload Source" : upload;
    source "Or paste it here" : text_area(20);

    horizontal {
        generateMustache "Generate from Mustache" : button, on_click generateFromMustache;
    };

    result "Result" : text_area(20);
}

form InterpolationForm {
    name "Name" : String, default "Juan";
    lastName "Last name" : String, default "Perez";
    fullName "Full name" : String, is "This man full name is $name $lastName";
}
