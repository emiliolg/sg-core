package resources.catalog;

entity Simi "Catálogo de los Artículos en stock"
    primary_key codadm
    described_by desadm1
{
       codadm  "Código de administración" : Decimal(10);
       desadm1 "1ra descr. del artículo"  : String(20);
       des2adm "2da descr. del artículo"  : String(20), optional;
       status  "Status"                   : String(32);
       familia "Familia del Articulos"    : sales.Familia;
}


enum GORBEL "Estado del producto"
{
       ACTIVO       : "Activo";
       DISCONTINUO  : "Discontinuado";
}

form MarioForm "Invoice"
    entity Invoice
{
    number "#" : <caret>;
    statement;
    instructions, required;
    "Order" : horizontal {
                         "#" : order;
                         "Random" : button, on_click randomOrder;
             };
     holliday "Holliday" : check_box;
     type;
     "Shortname" : horizontal {
                                 shortname : text_field, on_change checkShortname;
                                 available : text_field;
                               };
     other "Other" : String(20), internal;
     "Total" : Decimal(10,2), disable;
 }
