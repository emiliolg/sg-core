package tekgenesis.sales;

entity Articulo1_1 "Catálogo de los Artículos en stock 1_1"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_1;
	familia "Familia del Articulo"     : Familia1_1;
}

entity Familia1_1 "Familia de Artículos 1_1"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_1 "Estado del producto 1_1"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_2 "Catálogo de los Artículos en stock 1_2"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_2;
	familia "Familia del Articulo"     : Familia1_2;
}

entity Familia1_2 "Familia de Artículos 1_2"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_2 "Estado del producto 1_2"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_3 "Catálogo de los Artículos en stock 1_3"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_3;
	familia "Familia del Articulo"     : Familia1_3;
}

entity Familia1_3 "Familia de Artículos 1_3"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_3 "Estado del producto 1_3"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_4 "Catálogo de los Artículos en stock 1_4"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_4;
	familia "Familia del Articulo"     : Familia1_4;
}

entity Familia1_4 "Familia de Artículos 1_4"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_4 "Estado del producto 1_4"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_5 "Catálogo de los Artículos en stock 1_5"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_5;
	familia "Familia del Articulo"     : Familia1_5;
}

entity Familia1_5 "Familia de Artículos 1_5"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_5 "Estado del producto 1_5"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_6 "Catálogo de los Artículos en stock 1_6"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_6;
	familia "Familia del Articulo"     : Familia1_6;
}

entity Familia1_6 "Familia de Artículos 1_6"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_6 "Estado del producto 1_6"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_7 "Catálogo de los Artículos en stock 1_7"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_7;
	familia "Familia del Articulo"     : Familia1_7;
}

entity Familia1_7 "Familia de Artículos 1_7"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_7 "Estado del producto 1_7"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_8 "Catálogo de los Artículos en stock 1_8"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_8;
	familia "Familia del Articulo"     : Familia1_8;
}

entity Familia1_8 "Familia de Artículos 1_8"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_8 "Estado del producto 1_8"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_9 "Catálogo de los Artículos en stock 1_9"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_9;
	familia "Familia del Articulo"     : Familia1_9;
}

entity Familia1_9 "Familia de Artículos 1_9"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_9 "Estado del producto 1_9"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_10 "Catálogo de los Artículos en stock 1_10"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_10;
	familia "Familia del Articulo"     : Familia1_10;
}

entity Familia1_10 "Familia de Artículos 1_10"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_10 "Estado del producto 1_10"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_11 "Catálogo de los Artículos en stock 1_11"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_11;
	familia "Familia del Articulo"     : Familia1_11;
}

entity Familia1_11 "Familia de Artículos 1_11"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_11 "Estado del producto 1_11"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_12 "Catálogo de los Artículos en stock 1_12"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_12;
	familia "Familia del Articulo"     : Familia1_12;
}

entity Familia1_12 "Familia de Artículos 1_12"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_12 "Estado del producto 1_12"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_13 "Catálogo de los Artículos en stock 1_13"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_13;
	familia "Familia del Articulo"     : Familia1_13;
}

entity Familia1_13 "Familia de Artículos 1_13"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_13 "Estado del producto 1_13"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_14 "Catálogo de los Artículos en stock 1_14"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_14;
	familia "Familia del Articulo"     : Familia1_14;
}

entity Familia1_14 "Familia de Artículos 1_14"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_14 "Estado del producto 1_14"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_15 "Catálogo de los Artículos en stock 1_15"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_15;
	familia "Familia del Articulo"     : Familia1_15;
}

entity Familia1_15 "Familia de Artículos 1_15"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_15 "Estado del producto 1_15"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_16 "Catálogo de los Artículos en stock 1_16"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_16;
	familia "Familia del Articulo"     : Familia1_16;
}

entity Familia1_16 "Familia de Artículos 1_16"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_16 "Estado del producto 1_16"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_17 "Catálogo de los Artículos en stock 1_17"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_17;
	familia "Familia del Articulo"     : Familia1_17;
}

entity Familia1_17 "Familia de Artículos 1_17"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_17 "Estado del producto 1_17"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_18 "Catálogo de los Artículos en stock 1_18"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_18;
	familia "Familia del Articulo"     : Familia1_18;
}

entity Familia1_18 "Familia de Artículos 1_18"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_18 "Estado del producto 1_18"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_19 "Catálogo de los Artículos en stock 1_19"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_19;
	familia "Familia del Articulo"     : Familia1_19;
}

entity Familia1_19 "Familia de Artículos 1_19"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_19 "Estado del producto 1_19"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_20 "Catálogo de los Artículos en stock 1_20"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_20;
	familia "Familia del Articulo"     : Familia1_20;
}

entity Familia1_20 "Familia de Artículos 1_20"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_20 "Estado del producto 1_20"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_21 "Catálogo de los Artículos en stock 1_21"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_21;
	familia "Familia del Articulo"     : Familia1_21;
}

entity Familia1_21 "Familia de Artículos 1_21"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_21 "Estado del producto 1_21"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_22 "Catálogo de los Artículos en stock 1_22"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_22;
	familia "Familia del Articulo"     : Familia1_22;
}

entity Familia1_22 "Familia de Artículos 1_22"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_22 "Estado del producto 1_22"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_23 "Catálogo de los Artículos en stock 1_23"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_23;
	familia "Familia del Articulo"     : Familia1_23;
}

entity Familia1_23 "Familia de Artículos 1_23"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_23 "Estado del producto 1_23"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_24 "Catálogo de los Artículos en stock 1_24"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_24;
	familia "Familia del Articulo"     : Familia1_24;
}

entity Familia1_24 "Familia de Artículos 1_24"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_24 "Estado del producto 1_24"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_25 "Catálogo de los Artículos en stock 1_25"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_25;
	familia "Familia del Articulo"     : Familia1_25;
}

entity Familia1_25 "Familia de Artículos 1_25"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_25 "Estado del producto 1_25"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_26 "Catálogo de los Artículos en stock 1_26"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_26;
	familia "Familia del Articulo"     : Familia1_26;
}

entity Familia1_26 "Familia de Artículos 1_26"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_26 "Estado del producto 1_26"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_27 "Catálogo de los Artículos en stock 1_27"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_27;
	familia "Familia del Articulo"     : Familia1_27;
}

entity Familia1_27 "Familia de Artículos 1_27"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_27 "Estado del producto 1_27"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_28 "Catálogo de los Artículos en stock 1_28"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_28;
	familia "Familia del Articulo"     : Familia1_28;
}

entity Familia1_28 "Familia de Artículos 1_28"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_28 "Estado del producto 1_28"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_29 "Catálogo de los Artículos en stock 1_29"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_29;
	familia "Familia del Articulo"     : Familia1_29;
}

entity Familia1_29 "Familia de Artículos 1_29"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_29 "Estado del producto 1_29"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_30 "Catálogo de los Artículos en stock 1_30"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_30;
	familia "Familia del Articulo"     : Familia1_30;
}

entity Familia1_30 "Familia de Artículos 1_30"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_30 "Estado del producto 1_30"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_31 "Catálogo de los Artículos en stock 1_31"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_31;
	familia "Familia del Articulo"     : Familia1_31;
}

entity Familia1_31 "Familia de Artículos 1_31"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_31 "Estado del producto 1_31"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_32 "Catálogo de los Artículos en stock 1_32"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_32;
	familia "Familia del Articulo"     : Familia1_32;
}

entity Familia1_32 "Familia de Artículos 1_32"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_32 "Estado del producto 1_32"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_33 "Catálogo de los Artículos en stock 1_33"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_33;
	familia "Familia del Articulo"     : Familia1_33;
}

entity Familia1_33 "Familia de Artículos 1_33"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_33 "Estado del producto 1_33"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_34 "Catálogo de los Artículos en stock 1_34"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_34;
	familia "Familia del Articulo"     : Familia1_34;
}

entity Familia1_34 "Familia de Artículos 1_34"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_34 "Estado del producto 1_34"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_35 "Catálogo de los Artículos en stock 1_35"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_35;
	familia "Familia del Articulo"     : Familia1_35;
}

entity Familia1_35 "Familia de Artículos 1_35"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_35 "Estado del producto 1_35"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_36 "Catálogo de los Artículos en stock 1_36"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_36;
	familia "Familia del Articulo"     : Familia1_36;
}

entity Familia1_36 "Familia de Artículos 1_36"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_36 "Estado del producto 1_36"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_37 "Catálogo de los Artículos en stock 1_37"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_37;
	familia "Familia del Articulo"     : Familia1_37;
}

entity Familia1_37 "Familia de Artículos 1_37"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_37 "Estado del producto 1_37"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_38 "Catálogo de los Artículos en stock 1_38"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_38;
	familia "Familia del Articulo"     : Familia1_38;
}

entity Familia1_38 "Familia de Artículos 1_38"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_38 "Estado del producto 1_38"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_39 "Catálogo de los Artículos en stock 1_39"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_39;
	familia "Familia del Articulo"     : Familia1_39;
}

entity Familia1_39 "Familia de Artículos 1_39"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_39 "Estado del producto 1_39"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_40 "Catálogo de los Artículos en stock 1_40"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_40;
	familia "Familia del Articulo"     : Familia1_40;
}

entity Familia1_40 "Familia de Artículos 1_40"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_40 "Estado del producto 1_40"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_41 "Catálogo de los Artículos en stock 1_41"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_41;
	familia "Familia del Articulo"     : Familia1_41;
}

entity Familia1_41 "Familia de Artículos 1_41"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_41 "Estado del producto 1_41"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_42 "Catálogo de los Artículos en stock 1_42"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_42;
	familia "Familia del Articulo"     : Familia1_42;
}

entity Familia1_42 "Familia de Artículos 1_42"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_42 "Estado del producto 1_42"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_43 "Catálogo de los Artículos en stock 1_43"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_43;
	familia "Familia del Articulo"     : Familia1_43;
}

entity Familia1_43 "Familia de Artículos 1_43"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_43 "Estado del producto 1_43"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_44 "Catálogo de los Artículos en stock 1_44"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_44;
	familia "Familia del Articulo"     : Familia1_44;
}

entity Familia1_44 "Familia de Artículos 1_44"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_44 "Estado del producto 1_44"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_45 "Catálogo de los Artículos en stock 1_45"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_45;
	familia "Familia del Articulo"     : Familia1_45;
}

entity Familia1_45 "Familia de Artículos 1_45"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_45 "Estado del producto 1_45"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_46 "Catálogo de los Artículos en stock 1_46"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_46;
	familia "Familia del Articulo"     : Familia1_46;
}

entity Familia1_46 "Familia de Artículos 1_46"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_46 "Estado del producto 1_46"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_47 "Catálogo de los Artículos en stock 1_47"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_47;
	familia "Familia del Articulo"     : Familia1_47;
}

entity Familia1_47 "Familia de Artículos 1_47"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_47 "Estado del producto 1_47"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_48 "Catálogo de los Artículos en stock 1_48"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_48;
	familia "Familia del Articulo"     : Familia1_48;
}

entity Familia1_48 "Familia de Artículos 1_48"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_48 "Estado del producto 1_48"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_49 "Catálogo de los Artículos en stock 1_49"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_49;
	familia "Familia del Articulo"     : Familia1_49;
}

entity Familia1_49 "Familia de Artículos 1_49"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_49 "Estado del producto 1_49"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_50 "Catálogo de los Artículos en stock 1_50"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_50;
	familia "Familia del Articulo"     : Familia1_50;
}

entity Familia1_50 "Familia de Artículos 1_50"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_50 "Estado del producto 1_50"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_51 "Catálogo de los Artículos en stock 1_51"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_51;
	familia "Familia del Articulo"     : Familia1_51;
}

entity Familia1_51 "Familia de Artículos 1_51"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_51 "Estado del producto 1_51"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_52 "Catálogo de los Artículos en stock 1_52"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_52;
	familia "Familia del Articulo"     : Familia1_52;
}

entity Familia1_52 "Familia de Artículos 1_52"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_52 "Estado del producto 1_52"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_53 "Catálogo de los Artículos en stock 1_53"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_53;
	familia "Familia del Articulo"     : Familia1_53;
}

entity Familia1_53 "Familia de Artículos 1_53"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_53 "Estado del producto 1_53"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_54 "Catálogo de los Artículos en stock 1_54"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_54;
	familia "Familia del Articulo"     : Familia1_54;
}

entity Familia1_54 "Familia de Artículos 1_54"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_54 "Estado del producto 1_54"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_55 "Catálogo de los Artículos en stock 1_55"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_55;
	familia "Familia del Articulo"     : Familia1_55;
}

entity Familia1_55 "Familia de Artículos 1_55"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_55 "Estado del producto 1_55"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_56 "Catálogo de los Artículos en stock 1_56"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_56;
	familia "Familia del Articulo"     : Familia1_56;
}

entity Familia1_56 "Familia de Artículos 1_56"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_56 "Estado del producto 1_56"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_57 "Catálogo de los Artículos en stock 1_57"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_57;
	familia "Familia del Articulo"     : Familia1_57;
}

entity Familia1_57 "Familia de Artículos 1_57"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_57 "Estado del producto 1_57"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_58 "Catálogo de los Artículos en stock 1_58"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_58;
	familia "Familia del Articulo"     : Familia1_58;
}

entity Familia1_58 "Familia de Artículos 1_58"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_58 "Estado del producto 1_58"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_59 "Catálogo de los Artículos en stock 1_59"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_59;
	familia "Familia del Articulo"     : Familia1_59;
}

entity Familia1_59 "Familia de Artículos 1_59"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_59 "Estado del producto 1_59"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_60 "Catálogo de los Artículos en stock 1_60"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_60;
	familia "Familia del Articulo"     : Familia1_60;
}

entity Familia1_60 "Familia de Artículos 1_60"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_60 "Estado del producto 1_60"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_61 "Catálogo de los Artículos en stock 1_61"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_61;
	familia "Familia del Articulo"     : Familia1_61;
}

entity Familia1_61 "Familia de Artículos 1_61"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_61 "Estado del producto 1_61"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_62 "Catálogo de los Artículos en stock 1_62"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_62;
	familia "Familia del Articulo"     : Familia1_62;
}

entity Familia1_62 "Familia de Artículos 1_62"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_62 "Estado del producto 1_62"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_63 "Catálogo de los Artículos en stock 1_63"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_63;
	familia "Familia del Articulo"     : Familia1_63;
}

entity Familia1_63 "Familia de Artículos 1_63"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_63 "Estado del producto 1_63"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_64 "Catálogo de los Artículos en stock 1_64"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_64;
	familia "Familia del Articulo"     : Familia1_64;
}

entity Familia1_64 "Familia de Artículos 1_64"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_64 "Estado del producto 1_64"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_65 "Catálogo de los Artículos en stock 1_65"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_65;
	familia "Familia del Articulo"     : Familia1_65;
}

entity Familia1_65 "Familia de Artículos 1_65"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_65 "Estado del producto 1_65"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_66 "Catálogo de los Artículos en stock 1_66"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_66;
	familia "Familia del Articulo"     : Familia1_66;
}

entity Familia1_66 "Familia de Artículos 1_66"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_66 "Estado del producto 1_66"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_67 "Catálogo de los Artículos en stock 1_67"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_67;
	familia "Familia del Articulo"     : Familia1_67;
}

entity Familia1_67 "Familia de Artículos 1_67"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_67 "Estado del producto 1_67"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_68 "Catálogo de los Artículos en stock 1_68"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_68;
	familia "Familia del Articulo"     : Familia1_68;
}

entity Familia1_68 "Familia de Artículos 1_68"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_68 "Estado del producto 1_68"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_69 "Catálogo de los Artículos en stock 1_69"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_69;
	familia "Familia del Articulo"     : Familia1_69;
}

entity Familia1_69 "Familia de Artículos 1_69"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_69 "Estado del producto 1_69"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_70 "Catálogo de los Artículos en stock 1_70"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_70;
	familia "Familia del Articulo"     : Familia1_70;
}

entity Familia1_70 "Familia de Artículos 1_70"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_70 "Estado del producto 1_70"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_71 "Catálogo de los Artículos en stock 1_71"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_71;
	familia "Familia del Articulo"     : Familia1_71;
}

entity Familia1_71 "Familia de Artículos 1_71"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_71 "Estado del producto 1_71"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_72 "Catálogo de los Artículos en stock 1_72"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_72;
	familia "Familia del Articulo"     : Familia1_72;
}

entity Familia1_72 "Familia de Artículos 1_72"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_72 "Estado del producto 1_72"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_73 "Catálogo de los Artículos en stock 1_73"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_73;
	familia "Familia del Articulo"     : Familia1_73;
}

entity Familia1_73 "Familia de Artículos 1_73"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_73 "Estado del producto 1_73"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_74 "Catálogo de los Artículos en stock 1_74"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_74;
	familia "Familia del Articulo"     : Familia1_74;
}

entity Familia1_74 "Familia de Artículos 1_74"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_74 "Estado del producto 1_74"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_75 "Catálogo de los Artículos en stock 1_75"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_75;
	familia "Familia del Articulo"     : Familia1_75;
}

entity Familia1_75 "Familia de Artículos 1_75"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_75 "Estado del producto 1_75"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_76 "Catálogo de los Artículos en stock 1_76"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_76;
	familia "Familia del Articulo"     : Familia1_76;
}

entity Familia1_76 "Familia de Artículos 1_76"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_76 "Estado del producto 1_76"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_77 "Catálogo de los Artículos en stock 1_77"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_77;
	familia "Familia del Articulo"     : Familia1_77;
}

entity Familia1_77 "Familia de Artículos 1_77"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_77 "Estado del producto 1_77"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_78 "Catálogo de los Artículos en stock 1_78"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_78;
	familia "Familia del Articulo"     : Familia1_78;
}

entity Familia1_78 "Familia de Artículos 1_78"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_78 "Estado del producto 1_78"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_79 "Catálogo de los Artículos en stock 1_79"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_79;
	familia "Familia del Articulo"     : Familia1_79;
}

entity Familia1_79 "Familia de Artículos 1_79"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_79 "Estado del producto 1_79"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_80 "Catálogo de los Artículos en stock 1_80"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_80;
	familia "Familia del Articulo"     : Familia1_80;
}

entity Familia1_80 "Familia de Artículos 1_80"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_80 "Estado del producto 1_80"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_81 "Catálogo de los Artículos en stock 1_81"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_81;
	familia "Familia del Articulo"     : Familia1_81;
}

entity Familia1_81 "Familia de Artículos 1_81"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_81 "Estado del producto 1_81"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_82 "Catálogo de los Artículos en stock 1_82"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_82;
	familia "Familia del Articulo"     : Familia1_82;
}

entity Familia1_82 "Familia de Artículos 1_82"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_82 "Estado del producto 1_82"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_83 "Catálogo de los Artículos en stock 1_83"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_83;
	familia "Familia del Articulo"     : Familia1_83;
}

entity Familia1_83 "Familia de Artículos 1_83"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_83 "Estado del producto 1_83"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_84 "Catálogo de los Artículos en stock 1_84"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_84;
	familia "Familia del Articulo"     : Familia1_84;
}

entity Familia1_84 "Familia de Artículos 1_84"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_84 "Estado del producto 1_84"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_85 "Catálogo de los Artículos en stock 1_85"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_85;
	familia "Familia del Articulo"     : Familia1_85;
}

entity Familia1_85 "Familia de Artículos 1_85"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_85 "Estado del producto 1_85"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_86 "Catálogo de los Artículos en stock 1_86"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_86;
	familia "Familia del Articulo"     : Familia1_86;
}

entity Familia1_86 "Familia de Artículos 1_86"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_86 "Estado del producto 1_86"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_87 "Catálogo de los Artículos en stock 1_87"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_87;
	familia "Familia del Articulo"     : Familia1_87;
}

entity Familia1_87 "Familia de Artículos 1_87"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_87 "Estado del producto 1_87"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_88 "Catálogo de los Artículos en stock 1_88"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_88;
	familia "Familia del Articulo"     : Familia1_88;
}

entity Familia1_88 "Familia de Artículos 1_88"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_88 "Estado del producto 1_88"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_89 "Catálogo de los Artículos en stock 1_89"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_89;
	familia "Familia del Articulo"     : Familia1_89;
}

entity Familia1_89 "Familia de Artículos 1_89"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_89 "Estado del producto 1_89"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_90 "Catálogo de los Artículos en stock 1_90"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_90;
	familia "Familia del Articulo"     : Familia1_90;
}

entity Familia1_90 "Familia de Artículos 1_90"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_90 "Estado del producto 1_90"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_91 "Catálogo de los Artículos en stock 1_91"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_91;
	familia "Familia del Articulo"     : Familia1_91;
}

entity Familia1_91 "Familia de Artículos 1_91"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_91 "Estado del producto 1_91"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_92 "Catálogo de los Artículos en stock 1_92"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_92;
	familia "Familia del Articulo"     : Familia1_92;
}

entity Familia1_92 "Familia de Artículos 1_92"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_92 "Estado del producto 1_92"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_93 "Catálogo de los Artículos en stock 1_93"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_93;
	familia "Familia del Articulo"     : Familia1_93;
}

entity Familia1_93 "Familia de Artículos 1_93"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_93 "Estado del producto 1_93"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_94 "Catálogo de los Artículos en stock 1_94"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_94;
	familia "Familia del Articulo"     : Familia1_94;
}

entity Familia1_94 "Familia de Artículos 1_94"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_94 "Estado del producto 1_94"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_95 "Catálogo de los Artículos en stock 1_95"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_95;
	familia "Familia del Articulo"     : Familia1_95;
}

entity Familia1_95 "Familia de Artículos 1_95"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_95 "Estado del producto 1_95"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_96 "Catálogo de los Artículos en stock 1_96"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_96;
	familia "Familia del Articulo"     : Familia1_96;
}

entity Familia1_96 "Familia de Artículos 1_96"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_96 "Estado del producto 1_96"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_97 "Catálogo de los Artículos en stock 1_97"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_97;
	familia "Familia del Articulo"     : Familia1_97;
}

entity Familia1_97 "Familia de Artículos 1_97"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_97 "Estado del producto 1_97"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_98 "Catálogo de los Artículos en stock 1_98"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_98;
	familia "Familia del Articulo"     : Familia1_98;
}

entity Familia1_98 "Familia de Artículos 1_98"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_98 "Estado del producto 1_98"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_99 "Catálogo de los Artículos en stock 1_99"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_99;
	familia "Familia del Articulo"     : Familia1_99;
}

entity Familia1_99 "Familia de Artículos 1_99"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_99 "Estado del producto 1_99"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
entity Articulo1_100 "Catálogo de los Artículos en stock 1_100"
    primary_key estado, familia
    described_by des1adm
{
	codadm  "Código de administración" : String(8), custom_mask "XX-XXXX-XX";
	des1adm "1ra descr. del artículo"  : String(20);
	des2adm "2da descr. del artículo"  : String(20), optional;
	estado  "Estado"                  : Estado1_100;
	familia "Familia del Articulo"     : Familia1_100;
}

entity Familia1_100 "Familia de Artículos 1_100"
     primary_key idKey
    described_by descr
{
    idKey "Id" : Int, check idKey > 0 : "Must be positive";
    name "Nombre" : String (30);
    descr "Descripción" : String (120);
}

enum Estado1_100 "Estado del producto 1_100"
{
    ACTIVO       : "Activo";
    DISCONTINUO  : "Discontinuado";
}
