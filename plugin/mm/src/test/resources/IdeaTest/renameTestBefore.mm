  package catalog;

                       entity Jonny "Catálogo de los Artículos en stock"
                       primary_key codadm
                           described_by desadm1
                       {
                               codadm  "Código de administración" : Decimal(10);
                               desadm1 "1ra descr. del artículo"  : String(20);
                               des2adm "2da descr. del artículo"  : String(20), optional;
                               status  "Status"                   : String(50);
                               familia "Familia del Articulos"    : sales.Familia;
                       }


                       enum <caret>Status "Estado del producto"
                       {
                               ACTIVO       : "Activo";
                               DISCONTINUO  : "Discontinuado";
                       }


