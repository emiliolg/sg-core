package sales;

entity Tortuga2 "MiguelAngelo"
    described_by color
{
name  "name" : String(50);
color "color"  : String(20);
banax "String" : String(30);
}

enum Banana "armadillio"
{
CASCARON  : "CASCARON";
LOW  : "low";
}

form TortugaForm2 "TMNT"
    entity Tortuga2
{
    name;
    color;
    banax;
}
