/*
                       * FECHA            : 10/06/04 17:13:17
                       * MODIFICACION     : 10/06/04 17:13:17
                       * Definicion del esquema de ventas, Entidad Articulo
                       */

                       package catalog;

                       entity Tortuga "Raphael"
                       primary_key name
                           described_by color
                       {
                               name  "name" : String(50),optional;
                               color "color"  : String(20);
                               banaq "banaq" : String(20);
                       }


                       enum Arma "Estado del producto"
                       {
                               ESPADA  : "Espada";
                               BASTON  : "Baston";
                       }

                       form TortugaForm "TMNT"
                             entity Tortuga
                        {
                           name;
                           color;

                     }
