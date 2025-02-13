package tekgenesis.sales.basic;

form ProductDescription
    primary_key product
{
    header { message(title); };

    product : Product, internal;
	model "Model" : String(30);
	description "Description" : String(100), default model, optional;
	price "Price" : Decimal(10,2), disable;

    review "Review whole product" : button, on_click review;

	footer {
        submit "Submit" : button(save);
        draft "Draft" : button(save);
        button(save);
        button(cancel);
    };
}


form ProductImages
    primary_key product
{
    header {
        message(title);
    };

    product : Product, internal;
	model "Model": String(30), disable;
    gallery "Images" : upload, multiple;

	footer {
        button(save);
        button(cancel);
    };
}

case ProductData
    entity Product
    notify

{
    details : form ProductDescription;
    images : form ProductImages, actions(hansel "Accept", gretel "Reject");
    //verify : process ProductPromotion;
}