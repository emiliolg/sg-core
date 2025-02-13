package views;

entity Product "Product"
    primary_key productId
    described_by description
    searchable
{
	productId "Id"              : String(8), custom_mask "XX-XXXX-XX";
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Decimal(10,2);
	category "Category"         : CategoryType;
	images : entity Image* described_by imageId {
	    imageId : Resource;
	};
}

type CategoryType = Category;

entity Category "Category"
    primary_key id
    described_by descr
    searchable
{
    id "Id #"           : Int, check id > 0 : "Must be positive";
    name "Name"         : String (30);
    descr "Description" : String (120);
    products "Products" : Product*;
}

entity NonSearcheable
{
	name: String;
}

remote view NonSearcheableView of NonSearcheable {
	name: name;
}

remote view ProductSqlView as "select * from dual"
{
    vid : Int;
}

view ProductSqlView2 as "select * from dual"
of Product
index vid
{
    vid : Int;
}

remote view ProductView of Product
searchable
described_by vdescr
{
    vid : productId;
    vdescr : description;
    vcategory : category;
}
