package tekgenesis.views;

entity A "A entity"
remotable
{
    name : String;
    desc : String;
}

view V "View"
    as """ select * from TableName(VIEWS,A) """
    of A
    primary_key id
{
    id : Int;
    name : String;
    desc : String;
}

remote view R "Remote View"
    of A
    searchable
    index n
{
   n : name;
   d : desc;
}

entity E
 primary_key r
 searchable
{
    r : R;
    desc : String;

}

form EForm entity E;
