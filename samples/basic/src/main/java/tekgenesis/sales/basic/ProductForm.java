
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
import tekgenesis.form.MappingCallback;

/**
 * Product Form class.
 */
public class ProductForm extends ProductFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void copyTo(@NotNull final Product product) {
        super.copyTo(product);
        product.getImages().merge(getGallery(), Image::setImageId);
    }

    @NotNull @Override public Action create() {
        super.create();
        final ProductForm next = forms.initialize(ProductForm.class);
        next.setCategory(getCategory());
        return actions.navigate(next);
    }

    @NotNull public Action createCategory(String text) {
        final CategoryForm simpleCategoryForm = forms.initialize(CategoryForm.class);

        simpleCategoryForm.setName(text);

        return actions.navigate(simpleCategoryForm)
               .callback(OutFromCategory.class)
               .withMessage("Create a category for product " + (isDefined(Field.MODEL) ? getModel() : ""));
    }

    @NotNull @Override public Product populate() {
        final Product product = super.populate();
        setGallery(product.getImageResources());

        return product;
    }

    /** Expose read only. */
    public ProductForm readOnly() {
        forms.readOnly(true);
        return this;
    }

    //~ Inner Classes ................................................................................................................................

    public static class OutFromCategory implements MappingCallback<CategoryForm, ProductForm> {
        @Override public void onSave(@NotNull CategoryForm categoryForm, @NotNull ProductForm productForm) {
            productForm.setCategory(categoryForm.find());
        }
    }
}  // end class ProductForm
