
form ProductForm "Product Form"
    entity Product
{
    header {
        message(title);
        search_box(ProductForm), style "pull-right";
    };
    "Id"          : id;
    "Description" : description;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form ItemForm "Item Form"
    entity Item
{
    header {
        message(title);
        search_box(ItemForm), style "pull-right";
    };
    "Slot"       : slot;
    "Product"    : product;
    "Invoice"    : invoice;
    "Quantity"   : qty, default 1;
    "Unit Price" : unit_price;
    "Discount"   : discount;
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
