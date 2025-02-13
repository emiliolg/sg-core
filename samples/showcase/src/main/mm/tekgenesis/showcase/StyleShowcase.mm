package tekgenesis.showcase;

form StyleShowcase "Styles"
    on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.StyleShowcase", style "pull-right";
    };

    buttons "Default buttons" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        buttonDefault "Default" : button, tooltip "default";
        buttonPrimary "Primary" : button, style "margin-left-5", content_style "btn-primary", tooltip "content_style \"btn-primary\"";
        buttonInfo    "Info"    : button, style "margin-left-5", content_style "btn-info", tooltip "content_style \"btn-info\"";
        buttonSuccess "Success" : button, style "margin-left-5", content_style "btn-success", tooltip "content_style \"btn-success\"";
        buttonWarning "Warning" : button, style "margin-left-5", content_style "btn-warning", tooltip "content_style \"btn-warning\"";
        buttonDanger  "Danger"  : button, style "margin-left-5", content_style "btn-danger", tooltip "content_style \"btn-danger\"";
        buttonInverse "Inverse" : button, style "margin-left-5", content_style "btn-inverse", tooltip "content_style \"btn-inverse\"";
        buttonLink    "Link"    : button, style "margin-left-5", content_style "btn-link", tooltip "content_style \"btn-link\"";
    };

    buttonSizes "Button sizes" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        buttonLarge    "Large"   : button, content_style "btn-lg", tooltip "content_style \"btn-large\"";
        buttonDefault1 "Default" : button, style "margin-left-5", tooltip "default";
        buttonSmall    "Small"   : button, style "margin-left-5", content_style "btn-sm", tooltip "content_style \"btn-small\"";
        buttonMini     "Mini"    : button, style "margin-left-5", content_style "btn-xs", tooltip "content_style \"btn-mini\"";
    };

    pulls "Pulls: pull-left, pull-right" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        buttonR "Pulled right Button" : button, style "pull-right", tooltip "style \"pull-right\"";
        buttonL "Pulled left Button" : button, style "pull-left", tooltip "style \"pull-left\"";
    };

    inputSizes "Input sizes" : label, style "font-size-20";
    vertical, style "margin-top-10 margin-bottom-20" {
        inputMini "Input mini"       : text_field, style "mini", tooltip "style \"mini\"";
        inputSmall "Input small"     : text_field, style "small", tooltip "style \"small\"";
        inputMedium "Input medium"   : text_field, style "medium", tooltip "style \"medium\"";
        inputLarge "Input large"     : text_field, style "large", tooltip "style \"large\"";
        inputXlarge "Input xlarge"   : text_field, style "xlarge", tooltip "style \"xlarge\"";
        inputXxlarge "Input xxlarge" : text_field, style "xxlarge", tooltip "style \"xxlarge\"";
        inputFull "Input full-width" : text_field, style "full-width", hint "style \"full-width\"";
    };

    noLabel "Form alignment: no-Label / form-align" : label, style "font-size-20";
    vertical, style "margin-top-10 margin-bottom-20" {
        normalInput "Normal input"  : text_field;
        noLabelInput                : text_field, style "no-label", tooltip "style \"no-label\"";
        formAlignedMessage "Form aligned message" : label , style "form-align", tooltip "style \"form-align\"";
        normalInput1 "Normal Input" : text_field;
    };

    formMarginLabel "Form margin: form-margins" : label, style "font-size-20";
    vertical, style "form-margin-1" {
        formMargin1 "Form margin 1"  : text_field, tooltip "inside a vertical group with style \"form-margin-1\"";
    };
    vertical, style "form-margin-2" {
        formMargin2 "Form margin 2"  : text_field, tooltip "inside a vertical group with style \"form-margin-2\"";
    };
    vertical, style "form-margin-3" {
        formMargin3 "Form margin 3"  : text_field, tooltip "inside a vertical group with style \"form-margin-3\"";
    };
    normalMargin "Normal margin" : text_field;

    margins "Margins" : label, style "font-size-20";
    vertical, style "margin-top-10 margin-bottom-20" {
        horizontal, style "margin-top-10 margin-bottom-20" {
            horizontal, style "border-1-dotted-gray" { marginLeft5  "Margin Left 5"  : button, style "margin-bottom-0 margin-left-5", tooltip "style \"margin-left-5\""; };
            horizontal, style "border-1-dotted-gray" { marginLeft10 "Margin Left 10" : button, style "margin-bottom-0 margin-left-10", tooltip "style \"margin-left-10\""; };
            horizontal, style "border-1-dotted-gray" { marginLeft20 "Margin Left 20" : button, style "margin-bottom-0 margin-left-20", tooltip "style \"margin-left-20\""; };
            horizontal, style "border-1-dotted-gray" { marginLeft30 "Margin Left 30" : button, style "margin-bottom-0 margin-left-30", tooltip "style \"margin-left-30\""; };
            horizontal, style "border-1-dotted-gray" { marginLeft40 "Margin Left 40" : button, style "margin-bottom-0 margin-left-40", tooltip "style \"margin-left-40\""; };
        };

        horizontal, style "margin-top-10 margin-bottom-20" {
            horizontal, style "border-1-dotted-gray" { marginRight5  "Margin Right 5"  : button, style "margin-bottom-0 margin-right-5", tooltip "style \"margin-right-5\""; };
            horizontal, style "border-1-dotted-gray" { marginRight10 "Margin Right 10" : button, style "margin-bottom-0 margin-right-10", tooltip "style \"margin-right-10\""; };
            horizontal, style "border-1-dotted-gray" { marginRight20 "Margin Right 20" : button, style "margin-bottom-0 margin-right-20", tooltip "style \"margin-right-20\""; };
            horizontal, style "border-1-dotted-gray" { marginRight30 "Margin Right 30" : button, style "margin-bottom-0 margin-right-30", tooltip "style \"margin-right-30\""; };
            horizontal, style "border-1-dotted-gray" { marginRight40 "Margin Right 40" : button, style "margin-bottom-0 margin-right-40", tooltip "style \"margin-right-40\""; };
        };

        horizontal, style "margin-top-10" {
            vertical {
                horizontal, style "border-1-dotted-gray" { marginTop5  "Margin Top 5"  : button, style "margin-bottom-0 margin-top-5", tooltip "style \"margin-top-5\""; };
                horizontal, style "border-1-dotted-gray" { marginTop10 "Margin Top 10" : button, style "margin-bottom-0 margin-top-10", tooltip "style \"margin-top-10\""; };
                horizontal, style "border-1-dotted-gray" { marginTop20 "Margin Top 20" : button, style "margin-bottom-0 margin-top-20", tooltip "style \"margin-top-20\""; };
                horizontal, style "border-1-dotted-gray" { marginTop30 "Margin Top 30" : button, style "margin-bottom-0 margin-top-30", tooltip "style \"margin-top-30\""; };
                horizontal, style "border-1-dotted-gray" { marginTop40 "Margin Top 40" : button, style "margin-bottom-0 margin-top-40", tooltip "style \"margin-top-40\""; };
            };

            vertical, style "margin-left-40" {
                horizontal, style "border-1-dotted-gray" { marginBottom5  "Margin Bottom 5"  : button, style "margin-bottom-5", tooltip "style \"margin-bottom-5\""; };
                horizontal, style "border-1-dotted-gray" { marginBottom10 "Margin Bottom 10" : button, style "margin-bottom-10", tooltip "style \"margin-bottom-10\""; };
                horizontal, style "border-1-dotted-gray" { marginBottom20 "Margin Bottom 20" : button, style "margin-bottom-20", tooltip "style \"margin-bottom-20\""; };
                horizontal, style "border-1-dotted-gray" { marginBottom30 "Margin Bottom 30" : button, style "margin-bottom-30", tooltip "style \"margin-bottom-30\""; };
                horizontal, style "border-1-dotted-gray" { marginBottom40 "Margin Bottom 40" : button, style "margin-bottom-40", tooltip "style \"margin-bottom-40\""; };
            };

            vertical, style "margin-left-40" {
                horizontal, style "border-1-dotted-gray" { margin5  "Margin 5"  : button, style "margin-5", tooltip "style \"margin-5\""; };
                horizontal, style "border-1-dotted-gray" { margin10 "Margin 10" : button, style "margin-10", tooltip "style \"margin-10\""; };
                horizontal, style "border-1-dotted-gray" { margin20 "Margin 20" : button, style "margin-20", tooltip "style \"margin-20\""; };
                horizontal, style "border-1-dotted-gray" { margin30 "Margin 30" : button, style "margin-30", tooltip "style \"margin-30\""; };
                horizontal, style "border-1-dotted-gray" { margin40 "Margin 40" : button, style "margin-40", tooltip "style \"margin-40\""; };
            };
        };
    };

    condensed "Table (style \"table-condensed\")" : label, style "font-size-20";
    table : table, style "margin-top-10 margin-bottom-20 table-condensed" {
        firstName "First Name" : display;
        lastName "Last Name" : display;
        username "Username" : display;
    };

    noThead "Table (style \"no-thead\")" : label, style "font-size-20";
    table1 : table, style "margin-top-10 margin-bottom-20 no-thead" {
        firstName1 "First Name" : display;
        lastName1 "Last Name" : display;
        username1 "Username" : display;
    };

    horizontal, style "margin-top-10 margin-bottom-20" {
        bold "This text is font size 20, bold." : label, style "font-size-20 bold", tooltip "style \"font-size-20 bold\"";
    };

    horizontal, style "margin-top-10 margin-bottom-20" {
        muted "This text is font size 20, muted." : label, style "font-size-20 muted", tooltip "style \"font-size-20 muted\"";
    };

    grids "Grids (span-*)" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        more "More on this: " : label;
        bootLink "Bootstrap grid system" : label, link "http://getbootstrap.com/2.3.2/scaffolding.html#gridSystem", style "margin-left-5";
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span1 center-middle" { l1 "1" : label; };
        horizontal, style "label span1 center-middle" { l2 "2" : label; };
        horizontal, style "label span1 center-middle" { l3 "3" : label; };
        horizontal, style "label span1 center-middle" { l4 "1" : label; };
        horizontal, style "label span1 center-middle" { l5 "1" : label; };
        horizontal, style "label span1 center-middle" { l6 "1" : label; };
        horizontal, style "label span1 center-middle" { l7 "1" : label; };
        horizontal, style "label span1 center-middle" { l8 "1" : label; };
        horizontal, style "label span1 center-middle" { l9 "1" : label; };
        horizontal, style "label span1 center-middle" { l10 "1" : label; };
        horizontal, style "label span1 center-middle" { l11 "1" : label; };
        horizontal, style "label span1 center-middle" { l12 "1" : label; };
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span2 center-middle" { m1 "2" : label; };
        horizontal, style "label span2 center-middle" { m2 "2" : label; };
        horizontal, style "label span2 center-middle" { m3 "2" : label; };
        horizontal, style "label span2 center-middle" { m4 "2" : label; };
        horizontal, style "label span2 center-middle" { m5 "2" : label; };
        horizontal, style "label span2 center-middle" { m6 "2" : label; };
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span3 center-middle" { n1 "3" : label; };
        horizontal, style "label span3 center-middle" { n2 "3" : label; };
        horizontal, style "label span3 center-middle" { n3 "3" : label; };
        horizontal, style "label span3 center-middle" { n4 "3" : label; };
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span4 center-middle" { o1 "4" : label; };
        horizontal, style "label span4 center-middle" { o2 "4" : label; };
        horizontal, style "label span4 center-middle" { o3 "4" : label; };
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span6 center-middle" { p1 "6" : label; };
        horizontal, style "label span6 center-middle" { p2 "6" : label; };
    };
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "label span12 center-middle" { q1 "12" : label; };
    };

    thumbs "Thumbs (style \"thumb\")" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        image "Image" : image, style "thumb", tooltip "style \"thumb\"";
    };

    palettes "Palettes" : label, style "font-size-20";
    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "rectangle background-palette1-1 margin-right-10" { };
        horizontal, style "rectangle background-palette1-2 margin-right-10" { };
        horizontal, style "rectangle background-palette1-3 margin-right-10" { };
        horizontal, style "rectangle background-palette1-4 margin-right-10" { };
        horizontal, style "rectangle background-palette1-5" { };
    };

    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "rectangle background-palette2-1 margin-right-10" { };
        horizontal, style "rectangle background-palette2-2 margin-right-10" { };
        horizontal, style "rectangle background-palette2-3 margin-right-10" { };
        horizontal, style "rectangle background-palette2-4 margin-right-10" { };
        horizontal, style "rectangle background-palette2-5" { };
    };

    horizontal, style "margin-top-10 margin-bottom-20" {
        horizontal, style "rectangle background-palette3-1 margin-right-10" { };
        horizontal, style "rectangle background-palette3-2 margin-right-10" { };
        horizontal, style "rectangle background-palette3-3 margin-right-10" { };
        horizontal, style "rectangle background-palette3-4 margin-right-10" { };
        horizontal, style "rectangle background-palette3-5" { };
    };
}