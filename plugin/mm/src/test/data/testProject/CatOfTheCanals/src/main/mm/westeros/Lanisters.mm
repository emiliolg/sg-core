package westeros;

entity Jaime "Jaime"
{
    name  : String(50);
    weapon : Weapon;
    provisions "Provisions" : entity Provisions* {
        quantity "Quantity" : Int;
        discount "Discount" : Int;
        saddles "sadle" : entity Saddle{
            kind "Kind" : String(50);
        };
    };

}

