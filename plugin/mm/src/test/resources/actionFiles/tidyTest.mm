/*
* FECHA            : 10/06/04 17:13:17
* MODIFICACION     : 10/06/04 17:13:17
* Definicion del esquema de ventas, Entidad Articulo
*/

package catalog;

entity Articulo "Catálogo de los Artículos en stock"
primary_key codadm
    described_by desadm1
{
        codadm  "Código de administración" : Decimal(10);
        desadm1 "1ra descr. del artículo"  : String(20);
        des2adm "2da descr. del artículo"  : String(20), optional;
        estado  "Estado"                   : Estado;
        familia "Familia del Articulos"    : Familia;
}
 entity Familia "Familia de Articulos"
                   primary_key idKey
                   described_by nombre
                    {
                       idKey "Id de Familia" : Int;
                       nombre "Nombre de Familia" : String (30);
                    }

enum Estado "Estado del producto"
{
        ACTIVO       : "Activo";
        DISCONTINUO  : "Discontinuado";
}
form InvoiceForm "Invoice"
      entity Invoice
      primary_key type
 {
     number "#" : number;
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
entity Invoice "Invoice"
    primary_key number
    searchable
    described_by statement
{
	number  "Number"                : Decimal(12);
	statement   "Statement"                       : String(50);
	instructions  "Instructions"    : String(100), optional;
	order  "Order Number"                                            : Int;
	/*date    "Date"                  : Date;*/
	type    "Type"			        : Type;
    items   "Items"                 :               Item*;
}
enum Type "Invoice Type"
{
    STANDARD    : "Standard";
    AUTO_BILL  	: "Auto-Bill";
    PRO_FORMA  	: "Pro-Forma";
    QUICK  	                             : "Quick";
}
entity Product "Product"
    primary_key id
    searchable
    described_by description
{
    id "Id"                     : String(20);
    description "Description"   : String(50);
}

form ProductForm entity Product;

entity Item "Item"
	primary_key slot
	described_by product,qty
{
    slot "Slot"             : Int;
    product "Product"       : Product;
    invoice "Invoice"       : Invoice;
    qty "Quantity"          : Int, default 1;
    unit_price "Unit Price" : Decimal(10,2);
    discount "Discount"     : Decimal(10,2);
}

form ItemForm entity Item;

widget ItemWidget : Item;

type CategoryType widget CategoryTypeWidget  {
    code: Int;
    name: String;
}

widget CategoryTypeWidget : CategoryType;

