package tekgenesis.doc;


enum State{
    CREATED;
    NOT_CREATED;
}
entity CategoryType{
    nothing : String;
}

//#mm_documentation
/**
* Product entity
* <b>Described by: </b> description.
* <b>Searcheable: </b> using ProductSearcher, that looks by id and by model.
*
*/
entity Product "Product"
    primary_key productId
    described_by description // just to test comments are parsed ok, do not remove
    searchable
{
	productId "Id"              : IdType, custom_mask "XX-XXXX-XX"; //- product bar code
	model "Model No."           : String(30); //- product model
	description "Description"   : String(100), optional; //-brief description of the product
	price "Price"               : Decimal(10,2); //- product's price.
	state "State"               : State, default CREATED;
	category "Category"         : CategoryType; //- the category associated to this product
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
	images : entity Image* described_by imageId {
	    imageId : Resource;
	};
}
//#mm_documentation
