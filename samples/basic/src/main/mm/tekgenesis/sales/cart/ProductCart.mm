package tekgenesis.sales.cart schema cart;

import tekgenesis.sales.basic.ProductDefault;

type Product = tekgenesis.sales.basic.Product;

entity Cart "Cart"
    unique user
{
    user : String(20);
    items "Items" : entity CartItem* {
        product "Product" : Product;
        quantity "Quantity" : Int;
    };
}

form AddToCart "Add to Cart Form"
{
    header { message(title); };
    api : Boolean, internal, default false;
    product "Product" : Product, on_change productChanged;
    "Add to cart" : button, on_click addClicked;
    amount "Amount" : Int;
    current : Int, internal;
}

form CartForm "Cart"
    entity Cart
    on_load load
{
    header {
        message(title);
    };
    id, internal, optional;
    user, display;
    items "Items" : items, table {
        product, on_change updateUnitPrice;
        unit "Unit Price" : Decimal(10, 2), disable;
        quantity, default 1;
        total "Total Price" : Decimal(10,2), is unit * quantity;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, items), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, items);
    };

    subtotal "$" : Decimal(10,2), is sum(total), optional, style "pull-right aggregator";

    footer {
        "Checkout" : button, on_click checkout;
        "Discard" : button(delete), style "pull-right";
    };
}

form Products "Products"
    on_load load
{
    header { message(title); };

    products : tekgenesis.sales.basic.Product, table {
        productId "Product Id" : productId, display;
        model "Model" : model, display;
        category "Cateogry": category;
        description "Description" : description;
        active "Active?" : Boolean, disable;
        price "Price" : price, display;
        image "Image" : Resource, image;
        goTo "Details" : button, on_click goToProduct;
    };

    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, products), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, products);
    };
}

form ProductDetail "Product Details"
    entity tekgenesis.sales.basic.Product
{
    header { message(title); };

    horizontal {
        images : showcase;

        vertical {
            productId, disable, style "small";
            model, disable;
            category, disable;
            state, disable, style "small";
            price, disable, style "small";
        };
    };

    description, disable;
}

view ProductlView "ProductView" of ProductDefault
as "select ID,MODEL,PRICE,UPDATE_TIME from QName(BASIC,PRODUCT_DEFAULT)"
primary_key id
{
    id  : Int;
    model : String(30);
    price "Price" : Decimal(10,2); // This should work with type. Is not always working
    updateTime: DateTime(3);
}