package tekgenesis.sales.basic;
enum DocumentType "Document Type"
with { local: Boolean; desc: String(4), optional; ord: Int;}
{
    LONG  : "LONG", true,"MuuyLargo",2;
    DNI  : "DNI", true,null,"badType";
    PASS : "Passport", false, null,1, "extra";
    SUBE : "SUBE";
}
