/**
                       * FECHA            : 10/06/04 17:13:17
                       * MODIFICACION     : 10/06/04 17:13:17
                       * Definicion del esquema de ventas, Entidad Articulo
                       */

                       package catalog;

                       entity Articulo "Catálogo de los Artículos en stock"
                       primary_key codadm
                           described_by desadm1
                       {
                               product "Producto"                 : tekgenesis.sales.basic.Product;
                               codadm  "Código de administración" : Decimal(10);
                               desadm1 "1ra descr. del artículo"  : String(20);
                               des2adm "2da descr. del artículo"  : String(20), optional;
                               estado  "Estado"                   : Estado;
                               familia "Familia del Articulos"    : sales.Familia;
                       }


                       enum Estado "Estado del producto"
                       {
                               ACTIVO       : "Activo";
                               DISCONTINUO  : "Discontinuado";
                       }


