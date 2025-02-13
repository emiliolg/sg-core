package tekgenesis.innerentities;

entity Category
    primary_key code, classification
    described_by name
{
    code              : Int;
    name              : String(25);
    classification    : Classification;
    features          : entity FeatureByCat* {
        feature        : Feature;
        validValues    : entity FeatureValue* {
            value           : Value;
        };
    };
}

entity Classification
    described_by name
{
    name    : String(25);
}


entity Feature
    described_by name
{
    name    : String(25);
}



entity GrandFather
    primary_key code
    described_by name
{
    code        : Int;
    name        : String(25);
    children    : entity Father* {
        name             : String(25);
        grandChildren    : entity Son* {
            name      : String(25);
        };
    };
}


type Value = String(25);
