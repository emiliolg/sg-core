
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * Product description entity class.
 */
@SuppressWarnings("WeakerAccess")
public class ProductDescription extends ProductDescriptionBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Product populate() {
        final Product product = getProduct();
        setModel(product.getModel());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
        return product;
    }

    /** Invoked when button(review) is clicked. */
    @NotNull @Override public Action review() {
        final ProductForm form = forms.initialize(ProductForm.class, getProductKey()).readOnly();
        return actions.navigate(form).withMessage("Review product as a whole");
    }

    @NotNull @Override public Action update() {
        if (forms.isCurrentWidget(Field.SUBMIT)) System.out.println("ProductDescription.update :: submit");
        if (forms.isCurrentWidget(Field.DRAFT)) System.out.println("ProductDescription.update :: draft");
        if (forms.isCurrentWidget(Field.$B3)) System.out.println("ProductDescription.update :: save");

        switch (forms.<Field>getCurrentWidgetEnum()) {
        case SUBMIT:
            System.out.println("ProductDescription.update :: submit!");
            break;
        case DRAFT:
            System.out.println("ProductDescription.update :: draft!");
            break;
        case $B3:
            System.out.println("ProductDescription.update :: save!");
            break;
        default:
        }

        final Product product = getProduct();
        copyTo(product);
        product.persist();
        return actions.getDefault();
    }

    private void copyTo(@NotNull final Product product) {
        product.setModel(getModel());
        product.setDescription(getDescription());
        product.setPrice(getPrice());
    }
}  // end class ProductDescription
