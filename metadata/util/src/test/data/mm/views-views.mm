package views;

entity A "A entity"
    remotable
    described_by id
{
    name          : String;
    desc          : String;
}

entity E
    primary_key r
    described_by r
    searchable
{
    r             : R;
    desc          : String;
}

form EForm entity E;

remote view R "Remote View"
    of views.A
    searchable
    index n
{
    n             : name;
    d             : desc;
}

view V "View"
    of views.A
    as " select * from TableName(VIEWS,A) "
    primary_key id
{
    id      : Int;
    name    : String;
    desc    : String;
}
