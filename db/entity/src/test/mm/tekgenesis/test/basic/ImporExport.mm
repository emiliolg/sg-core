package tekgenesis.test.basic schema BasicTest;

import tekgenesis.test.fleet.Zone;

entity Author
    described_by name, lastName, nickName
    searchable by {
        name;
        lastName;
        extra;
        force;
    }
{
    name : String(30);
    lastName : String(50);
    nickName : String(50), optional;
    extra: String(10), abstract, read_only;
    force: Boolean, abstract, read_only;
}

entity Book
    described_by title
    searchable
{
    title : String(60);
    author: Author;
    cover : Resource, optional;
}

entity Library
    auditable
{
    name : String;
    zone : Zone;
}
entity BookStore
    primary_key codigo
    described_by name
{
    codigo : Int;
    name   : String(60);
    address : entity StoreLocation* {
        dir : String(256);
    };
    extraCode : Int, optional;
}