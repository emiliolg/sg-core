package tekgenesis.sales.basic schema basic;

type CategoryType = Category;
type IdType = String(8);

type Amount = Decimal(10,2);

type SalClass = SalableClassification;

/**
   * Role type that could be import
*/

type Role = tekgenesis.authorization.Role;

/**
* Enum Salable Classification documentation
* <b> Default comission amount </b>
*/
enum SalableClassification
with
{ defaultCommision: Amount; }
{
    A : "A", 1.8; //- A documentation
    B : "B", 1.3; //- B documentation
    C : "C", 1.2; //- C documentation
    D : "D", 0.9; //- D documentation
    E : "E", 0.5; //- E documentation
    Z : "Z", 0.5;
    INVALID : "Invalid" , 0; //- This is invalid
}

/**
* Product entity
* <b>Described by: </b> description.
* <b>Searcheable: </b> using ProductSearcher, that looks by id and by model.
*
*/
entity Product "Product"
    primary_key productId
    described_by description // just to test comments are parsed ok, do not remove
    searchable by {
        description;
        productId;
        model;
    }
{
	productId "Id"              : IdType, custom_mask "XX-XXXX-XX"; //- product bar code
	model "Model No."           : String(30); //- product model
	description "Description"   : String(100), optional; //-brief description of the product
	price "Price"               : Decimal(10,2); //- product's price.
	state "State"               : State, default CREATED;
//	state "State"               : State, default CREATED; //- current state, active, created or discontinued
	category "Category"         : CategoryType; //- the category associated to this product
	categoryAttPersisted : String(150); //TODO delete this and allow abstract fields in views -> to ProductView.
	secondary "Secondary Categories" :
        /**
        * Product by category documentation
        */
	    entity ProductByCat* auditable described_by secondaryCategory
	                                           remotable
                                               index updateTime
                                               index category(secondaryCategory)
        {
        secondaryCategory    : Category; //- second category of the product
    };
    tags                        : Tag*;
	images : entity Image* described_by imageId {
	    imageId : Resource;
	};
}

protected entity ProtectedProduct "Protected Product"
    primary_key productId
    described_by description
    searchable
{
	productId "Id"              : IdType, custom_mask "XX-XXXX-XX";
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Decimal(10,2);
	state "State"               : State, default CREATED;
	category "Category"         : CategoryType;
}

entity City
remotable
{
    name "Name" : String(30);
    stateProvince : StateProvince;
    country : Country, abstract, read_only;
   polygon: Int*, abstract, read_only;
   countries: Country*, abstract, read_only;
    countryName : String(30), abstract, read_only;
}

entity Country
primary_key iso2
remotable
{
    name : String(30);
    iso2 : String(2);
}

entity StateProvince
 primary_key country,code
 remotable
{
    country "Country" : Country;
    code "Code" : String(2);
    name "Name" : String(30);
}

entity ProductDefault "Product"
    described_by description
    searchable
{
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Amount;
	state "State"               : State, default CREATED;
	mainCategory "Category"         : CategoryDefault;
	image "Image"               : Resource, optional;
	reviews "Reviews"   : Review*;
	comments "Comments" : String(5000);
}

view ProductSqlView "ProductView" of ProductDefault
as "select ID,MODEL,PRICE,UPDATE_TIME from QName(BASIC,PRODUCT_DEFAULT)"
primary_key id
remotable
{
    id  : Int;
    model : String(30);
    price "Price" : Decimal(10,2); // This should work with type. Is not always working
    updateTime: DateTime(3);
}
entity ProductDefaultInners "Product"
    described_by description
    searchable by {
        id;
        model;
        description;
    }
{
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Decimal(10,2);
	state "State"               : State, default CREATED;
	mainCategory "Category"         : CategoryDefault;
	image "Image"               : Resource, optional;
	reviews "Reviews"   : entity Rev* auditable remotable{
        review : String;
	};
}

/**
* Review comment, if double asterisk is used will be a documentation token and not a comment one.
 */

entity Review auditable
    index product
    remotable
{
    product : ProductDefault;
    review : String;
}

form ProductForm "Product"
    entity Product
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    productId;
	model;
	description, default model;
	price;
	state;
	category, on_new createCategory;
    gallery "Images" : upload, multiple, optional;
	footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form ProductImgShowcase
    on_load load
{
    header { message(title); };

    imgShowcase : showcase;
}

/**
* Category entity
* <b>Searchable: </b> using CategorySearcher, indexing fields idKey and name
*/
entity Category "Category"
    primary_key idKey
    described_by descr
    searchable by {
        idKey;
        name;
        descr;
    }
{
    idKey "Id #"        : Int(18), serial CAT_SEQ , start_with 20, check idKey > 0 : "Must be positive"; //- The id of the category
    name "Name"         : String (30); //- The name of the category
    descr "Description" : String (120); //- The description of the category.
    products "Products" : Product*; //- The products associated to this category. This one should've a link to Product Entity
}
/**
* Category entity
* <b>Searchable: </b> using CategorySearcher, indexing fields idKey and name
*/
entity CategoryComposite "Category"
    primary_key idKey, descr, shortDesc
    described_by descr
    remotable
{
    idKey "Id #"        : Int(18), serial CATC_SEQ , start_with 20, check idKey > 0 : "Must be positive"; //- The id of the category
    name "Name"         : String (30); //- The name of the category
    descr "Description" : String (120); //- The description of the category.
    shortDesc "Short Description" : String (120); //- The description of the category.
}

view CategorySqlView as "select ID_KEY, NAME, DESCR, UPDATE_TIME from TableName(BASIC, CATEGORY)"
of Category
primary_key idKey
remotable
{
    idKey : Int;
    name : String(30);
    descr : String(120);
    updateTime : DateTime(3);
}

entity CategoryDefault "Category"
    described_by descr
    searchable by {
        descr;
        id;
        name;
    }
    deprecable
    index deprecationTime
{
    name "Name"         : String (30);
    descr "Description" : String (120);
    products "Products" : ProductDefault*;
    parent  "Parent"    : CategoryDefault, optional;
}

enum State
{
    CREATED;
    ACTIVE;
    DISCONTINUED;
}

form CategoriesService
    on_load load
    parameters from, limit
{
    from : Int(18), optional;
    limit : Int, default 3;

    categories : Category, table {
       id : idKey;
        name;
        descr;
    };

    next : Int(18), optional;
}

form CategoryForm "Category"
    entity Category
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    id       "Id #"        : idKey, check id > 0 : "Must be positive";
    name     "Name"        : name;
    descr    "Description" : descr, text_area(10, 80), default name;

    products "Products"    : products, table, hide when !isUpdate() {
        productId   "Id"          : productId;
        model       "Model No."   : model;
        description "Description" : description, text_field, optional;
        price       "Price"       : price;
        state       "State"       : state;
    };

    horizontal, style "margin-top-20", hide when !isUpdate() {
        addRowBottom "Add" : button(add_row, products), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, products);
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

entity LongKey
 primary_key a1, a2, a3, a4, a5, a6
{
    a1:Int;
    a2:Int;
    a3:Int;
    a4:Int;
    a5:Int;
    a6:Int;
    a7:Int;
}

enum Tag {
    WEB : "Web";
    OUTLET : "Outlet";
    ACCESORY : "Accesory";
}