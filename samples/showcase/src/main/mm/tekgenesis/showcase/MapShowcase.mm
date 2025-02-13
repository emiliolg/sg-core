package tekgenesis.showcase;


form MapForm "Maps" on_load load
{
        header {
            message(title);
            "View Source" : label, link "/sg/view/source/tekgenesis.showcase.MapForm", style "pull-right";
        };

        places : table {
            latitud "Latitud" : Real, display;
            longitud "Longitud" : Real, display;
            name "Name" : String(15), display;
            "Go" : button, on_click go;
        };

        map "Map" : map {
            lat : Real;
            lng : Real;
            vertical {
                title : String(20), display;
                img : image;
                horizontal {
					button "Show": button, on_click show;
					button1 "Show": button, on_click show;
					button3 "Show": button, on_click show;
					button4 "Show": button, on_click show;
					button5 "Show": button, on_click show;
					button6 "Show": button, on_click show;
				};
            };
        };

        vertical{
            map2 "Map" : map {
                lat2 : Real;
                lng2 : Real;
                vertical {
                    title2 : String(20), display;
                    img2 : image;
                    button2 "Show": button, on_click show;
                };
            };
        };

        clicked : message(info);

        // Fakes getting map info from service and populating a map on a sub-form
        bt "Fake service" : button, on_click doStuff, shortcut "cmd + shift + y";
        locate "SubForm Map": subform(MapSubSubForm), display "view map", shortcut "meta +u";
}

form MapDragForm "Maps Drag & Drop" on_load load
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.MapDragForm", style "pull-right";
    };

    map "Map" : map, draggable, on_change updateCoords, on_new_location locate {
        lat : Real;
        lng : Real;
    };

    coords "Coords" : String(200);
}

form MapConfigurationForm "Maps Configurations"
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.MapConfigurationForm", style "pull-right";
    };

    markerSize "Marker size" : Decimal(4,2), hint "this number must be between 0 and 5", default 1;
    color "Color" : combo_box, default "Blue";
    "Change size" : button, on_click changeSize;

    argentina "Argentina" : map {
        argLat : Real;
        argLng : Real;
    };

    ethiopia "Etiopia" : map {
        etiLat : Real;
        etiLng : Real;
    };

    pilar "Pilar" : map {
        pilLat : Real;
        pilLng : Real;
    };

    horizontal, style "navbar-fixed-bottom" {
        zoomIn "Zoom In" : button, style "pull-right marginRight10", on_click zoomIn, shortcut "z";
        zoomOut "Zoom Out" : button, style "pull-right", on_click zoomOut, shortcut "x";

        resolution3 "1280x720" : button, style "pull-right marginRight10", on_click size1280;
        resolution2 "854x480" : button, style "pull-right", on_click size854;
        resolution1 "640x480" : button, style "pull-right", on_click size640;
    };
}

form MapSubSubForm "Map subform Showcase"
{
// this map is populated on sub-form creation, there is no MapConfiguration(default)
        map "Map" : map(UNIGIS) {
            lat : Real;
            lng : Real;
        };
}


enum MarkerOptions
{
    OPTION1     : "Marker 1";
    OPTION2     : "Marker 2";
    OPTION3     : "Marker 3";
}
