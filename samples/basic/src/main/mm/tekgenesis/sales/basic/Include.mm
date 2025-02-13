package tekgenesis.sales.basic;

form CustomerHistoryForm entity Customer {
    documentType;
	documentId;
	sex;
    firstName;
    lastName;

    invoices : subform(CustomerInvoicesForm), inline;
}

form CustomerInvoicesForm entity Customer {

    documentType;
	documentId;
	sex;

    invoices : Invoice, table {
        idKey, display;
        invoiceDate, display;
        items : Int;
        total : Decimal(10,2);
        edit : label, icon edit, on_click editInvoice, abstract_invocation;
    };
}