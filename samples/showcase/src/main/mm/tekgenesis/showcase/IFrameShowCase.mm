package tekgenesis.showcase;


form IFrameShowcase "IFrames"{
    header {
        message(title);
    };

    urlText "URL To display": text_field, on_change urlTextChanged, tooltip "Using onChange";
    display "Displayed URL": String,disable, tooltip "updated via frame on change";

    //#iframe
    test "Frame": iframe,on_change testChanged, default "http://apps.tekgenesis.com", tooltip "Defaults to apps.tekgenesis.com";
    //#iframe
    button(save);
}
