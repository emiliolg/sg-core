package westeros;

entity Arya "Arya"
{
    name : String(50);
    weapon : Weapon;//Needle

}

form AryaForm "Arya Form"
    entity Arya
{
    header {
        message(title);
    };
    "Id"     : id, internal, optional;
    "Name"   : name;
    "Weapon" : weapon;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity Weapon "Weapon"
    searchable
{
    name: String(50);
    type : String(50);
    damage : Int;
}
entity Salty "Salty"
{
    name : String(50);
    weapon: Weapon;
}
entity Weasel "Weasel"
{
     name : String(50);
     weapon: Weapon;//weaselSoup

}
entity NoOne "No One"
{
     name : String(50);

}

enum KillList "KillList"
{
    HOUND :"Hound" ;

}
