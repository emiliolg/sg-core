package tekgenesis.sales.basic;

remote view CategoryView of Category
searchable by {
    vdescr;
    vname;
}
batch_size 10000
described_by vdescr
{
    vid : idKey;
    vname : name;
    vdescr : descr;
}
remote view CategoryCompositeView of CategoryComposite
described_by vdescr
{
    vid : idKey;
    vname : name;
    vdescr : descr;
    vshort : shortDesc;
}

remote view ProductView of Product
searchable by {
    vdescr;
    categoryAtt, filter_only;
}
described_by vdescr
index vdescr
{
    vid : productId;
    vdescr : description;
    vcategory : category;
    secondary : secondary;
    categoryAtt: categoryAttPersisted;
    myDesc : String, abstract;
    tags : tags;
}

remote view ProdByCatView of ProductByCat
{
    cat : secondaryCategory;
}

remote view CityView of City
{
    name : name; stateProvince : stateProvince;
 }

remote view StateProvinceView of StateProvince
primary_key country, code
index country(country)
{
    country : country;
    code : code;
    name : name;
}

remote view CountryView of Country
primary_key iso
{
    iso : iso2;
    name : name;
}


remote view CategoryDefaultView of CategoryDefault
searchable by {
    vname;
    vdescr;
}
described_by vdescr
{
    vname : name;
    vdescr : descr;
    vparent: parent;
}

remote view CategorySqlViewView of CategorySqlView
searchable by {
    vdescr;
}
described_by vdescr
primary_key vid
{
    vname : name;
    vid : idKey;
    vdescr: descr;
}


remote view ProductDefaultView of ProductDefault
searchable
described_by vdescr
{
    vdescr : description;
    vCategory : mainCategory;
    image : image;
    state : state;
    price : price;
    revs : reviews;
    comments : comments;
}

remote view ProductDefaultSqlView of ProductSqlView
searchable
primary_key id
{
    id    : id;
    model : model;
    price : price;
}

remote view ProductDefaultViewInners of ProductDefaultInners
searchable
described_by vdescr
{
    vdescr : description;
    vCategory : mainCategory;
    image : image;
    revsInner : reviews;
}

remote view RevView of Review
{
    rev : review;
    prod : product;
}
remote view RevInnerView of Rev
{
    rev : review;
}

form Categories
    primary_key category
{
    category : CategoryDefaultView; // Use StoreChain type when SUI-927 is fixed
}

form CatViewForm entity CategoryView;
