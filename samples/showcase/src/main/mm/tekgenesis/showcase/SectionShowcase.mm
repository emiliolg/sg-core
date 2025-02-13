package tekgenesis.showcase;

form SectionForm "Sections"
    on_load load
{
    header {
            message(title);
            "View Source" : label, link "/sg/view/source/tekgenesis.showcase.SectionForm", style "pull-right";
    };

    rooms "Rooms" : section {
        horizontal {
            adults "Adults" : Int, default 2, on_change changed;
            children "Children" : Int, default 0, on_change changed;
        };
        amenities "Category" : SectionCategory, default STANDARD;
    };

    horizontal {
        more "More" : button, on_click more;
        clear "Clear" : button, on_click clear;
        remove "Remove First" : button, on_click removeFirstRoom;
    };

    path "Complex Path" : section, style "form-inline margin-5" {
        horizontal {
            fqn : internal;
            display : display, on_click link, col 8;
            ">" : label, col 4;
        };
    };

    sec "Inline Style, Custom Width": section, row_inline_style "width:"+width+"%; display:inline-block"{
        width : Int, internal;
        lab : display;
    };


    horizontal {

        cells "Cells" : section, row_style style, col 6 {
            value : display, style "no-label btn";
            style : internal;
        };

        some "Cols" : section, col 6{
            vertical, col 4 {
                colImg : String, image, style "no-label";
                displayText : display, style "no-label";
            };
        };

    };

    scroll "Scroll Section" :  section, scrollable {
        img : image, style "no-label";
        name "The Shoes": label;
    };
}

enum SectionCategory {
    STANDARD : "Standard";
    MODERATE : "Moderate";
    SUPERIOR : "Superior";
    DELUXE : "Deluxe";
}