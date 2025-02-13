/*
* Invoice sample
*/
package tekgenesis.test.basic schema BasicTest;

entity SeqInvoice "Invoice"
    primary_key number
    described_by statement
{
    number "Number"             : Decimal(12);
    statement "Statement"       : String(50);
    instructions "Instructions" : String(100), optional;
    date "Date"                 : Date;
}

entity SeqProduct "Product"
    primary_key id
    described_by description
    searchable
{
    id "Id"                     : String(20);
    description "Description"   : String(50);
}

entity SeqItem "Item"
	described_by product,qty
{
    product "Product"       : SeqProduct;
    invoice "Invoice"       : SeqInvoice;
    qty "Quantity"          : Int, default 1;
}
