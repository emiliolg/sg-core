 /**
                       * FECHA            : 10/06/04 17:13:17
                       * MODIFICACION     : 10/06/04 17:13:17
                       * Definicion del esquema de ventas, Entidad Articulo

                       */


          package sales;


          entity Familia "Familia de Articulos"
                   primary_key id
                   described_by nombre
                    {
                       id "Id de Familia" : Int;
                       nombre "Nombre de Familia" : String (30);
                    }

           form CustomerForm "Customer"
                 entity Customer
            {
            	documentId	: Decimal(10);
                name		: String(50);
                surname     : String(100);
                sex		    : Sex;
             }

