package tekgenesis.test.basic schema BasicTest;

entity OptionalReferenceMapping
{
    grupart : OptionalReferenceGroup, optional;
}

entity OptionalReferenceGroup
    primary_key company, group
{
  company : String(256);
  group : Int;
}