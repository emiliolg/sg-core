package tekgenesis.snippets;

//#FromScratch
type SampleType{
    a "a": String;
}
//#FromScratch

//#OptionalChildType
type OptionalChildSample extends SampleType{
    b "b":String, optional;
}
//#OptionalChildType
//#ChildType
type ChildSample extends SampleType{
    b "b":String;
}
//#ChildType
//#FinalChildType
final type FinalChildSample extends SampleType{
    b "b":String;
}
//#FinalChildType

type PersonType (
    dni: Int;
) {
    name: String;
}

type Customer (
    cuit: String;
) {
    person: PersonType;
}


enum LinkType {
    CATEGORY;
    LIST;
    LANDING;
    PRODUCT;
    URL;
}
final type LinkDef {
    id: String;
    type: LinkType, default URL;
}

final type MenuNodeDef {
    name : String, optional;
    nodes : MenuNodeDef*;
    bannerId: String, optional;
    linkValue: LinkDef, optional;

    // ToDo: Remove when no longer needed
    link : String, optional;
}

final type MenuDef {
    root : MenuNodeDef;
    dummy : Boolean; // to leave the object created for future fields
}


