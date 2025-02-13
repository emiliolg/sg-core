package tekgenesis.test;

form FormA
    entity EA
{
    a;
    b;
    c;
}

form FormB
    entity EB
{
    a;
    b;
    c;
}

remote form FormRemote "Form Remote Label"
    primary_key entity
{
    entity : EA;
}
