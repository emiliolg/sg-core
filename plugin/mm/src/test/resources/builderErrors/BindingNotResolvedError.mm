 /*
                       * FECHA            : 10/06/04 17:13:17
                       * MODIFICACION     : 10/06/04 17:13:17
                       * Definicion del esquema de ventas, Entidad Articulo

                       */


          package sales;


          entity Mono "El Mono"
                   primary_key idKey
                   described_by nombre
                    {
                       idKey "Id de Mono" : Int;
                       nombre "Nombre de Mono" : String (30);
                       banana "banana" : Int;
                       banana2 "B2" : Int;
                    }

           form MonoForm "Mono"
                 entity Mono
            {
            	banan;
            	banana2;
            	sales.Tortuga2.banax;
                name		: String(50);
                surname     : String(100);

             }

