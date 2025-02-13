package idea.test;

remote view ProductRemoteView of ProductView
{
    vid : id;
    price : price;
}

remote view ProductSqlRemoteView of ProductSqlView
{
    vid : id;
    price : price;
}

/*remote view InvoiceItemView of Item
{
    product : product;
    qty : qty;
    discount : discount;
}

remote view InvoiceView of Invoice
{
    number : number;
    date : date;
    type : type;
    items : items;
}*/