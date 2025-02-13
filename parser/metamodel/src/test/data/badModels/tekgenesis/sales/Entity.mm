/*
* FECHA            : 10/06/04 17:13:17
* MODIFICACION     : 10/06/04 17:13:17
* Definicion del esquema de ventas, Entidad Articulo
*/

package mon  sales;

entity Articulo "Catálogo de los Artículos en stock"
    primary_koy codadm
    described_by des1adm
{
	codadm  "Código de administración" : Strg(8), mask "XX-XXXX-XX",optional;
	asaa : ;
        frula  "Frula"			   : enumo ?!?!?;
	ass "asda" 			   : Into;
	des1adm "1ra descr. del artículo"  : String(20)
	des2adm "2da descr. del artículo"  : String(20), optionaxl;
	estado  "Estado"                   : Estado;
	familia "Familia del Articulo"     : Familia;
}

entity Familia "Familia de Artiículos"
    primary_key id2
    described_by descr /** doc1 */
{
    id "Id de Familia" : Int;
    name "Nombre de Familia" : String (30);
}

/** doc2 */
/** doc3 */
entity Categoria "Categoria"
    primary_key attr1,attr1
{
    attr1 "Id de Categoria" : Int;
    name "Nombre de Categoria" : String (30);
}

entity Super1 "Super"
    primary_key attr1,attr2
{
    attr1 "Id int" : Int;
    attr2 "If string" : String (30);
    attr3 "wrong default" : String (30), default attr1;
}

entity Aug "Augmented" augments Super1
    primary_key attr1,attr2
    described_by attr1, attr3, attr2
    searchable
    index attr1
    unique attr2
{
    attr3 "bool" : Boolean;
    attr4 "If string" : Int;
}

entity lowercase "LowerCase"
{
   name : String (20);
}

entity Class "ReserverdKeyword"
{
   name : String (20);
}

/** documentation to show import error */

import tekgenesis.sales.DocumentType;
