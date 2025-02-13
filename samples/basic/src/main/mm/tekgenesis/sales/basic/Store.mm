package tekgenesis.sales.basic;

//#storeEntity
entity Store
    searchable
    described_by name
{
    name : String(50);
    address : String(150);
    lat: Real, signed;
    lng: Real, signed;
}
//#storeEntity

//#storeForm
form StoreForm "Store"
    entity Store
    unrestricted
{
    header {
        message(entity), col 8;
    };
    id, internal, optional;
    name;
    address;
    lat, signed;
    lng, signed;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
//#storeForm
//#storeType
final type StoreType (
    name : String(50);
    address : String(150);
    latitude: Real, signed;
    longitude: Real, signed;
)
//#storeType
//#storeServiceHandler
remote handler StoreServiceHandler
    on_route "/service/store"
    unrestricted
{
    "/$key" : StoreType, get;
}
//#storeServiceHandler
//#storeHtmlHandler
handler StoreHtmlHandler
    on_route "/stores"
    unrestricted
{
    "/" : Html, home;
}
//#storeHtmlHandler
