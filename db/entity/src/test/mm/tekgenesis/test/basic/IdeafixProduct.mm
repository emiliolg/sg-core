package tekgenesis.test.basic  schema BasicTest;

// Just as a help when mapping to an existing IdeaFix product
entity IdeafixProduct
   primary_key company, codadm
   described_by codadm, codName, brand
   searchable
{
   company : String(30);
   codadm  : Int;
   codflia : Int;
   codName : String(25);
   brand   : String(25);
   ctaord "Sold on behalf of" : Int, optional;
   directCommission "Direct commission" : Decimal(5,2), optional;
   classification   "Classification" : String(2), optional;
   cost "costorep" : Decimal(9,2), optional;
}