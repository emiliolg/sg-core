package tekgenesis.sales.basic;

widget CoordinateWidget {
    "Lat" : Int;
    "Lng" : Int;
    subf: subform(CoordinatesForm);
}

form CoordinatesForm {
    from "From" : widget(CoordinateWidget);
    "To" : widget(CoordinateWidget);
    widget(CoordinateWidget);
}

type CoordinateType {
    lat: Int;
    lng: Int;
    name: RandomEnt;
}

entity RandomEnt {
    name: String;
}

form OtherForm : CoordinateType {
    name: String;
}