package essos;

type Weapon = westeros.Weapon;

entity Targaryen "Targaryen"
	described_by name
{
	name "Name" : String(20);
}

entity Danerys "Danerys"
{
    name "Danerys" : String(50);
    dragon "dragon" : Dragons, default Viserion;
    weapon "weapon" : Weapon;

}


form TargaryenForm "Targaryen Form"
    entity Targaryen
{
    header {
        message(title);
    };
    "Id"   : id, internal, optional;
    "Name" : name;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

enum Dragons "Dragons"
{
   Viserion: "VISERION";
   Rhaegal: "RHAEGAL";
   Drogon : "DROGON";

}


