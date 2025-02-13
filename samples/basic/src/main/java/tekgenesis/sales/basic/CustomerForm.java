
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

import static tekgenesis.sales.basic.g.PreferencesTable.PREFERENCES;

/**
 * Customer Form class.
 */
@SuppressWarnings("WeakerAccess")
public class CustomerForm extends CustomerFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cancel() {
        return actions.getDefault().withMessage("Nothing has change!");
    }

    @NotNull @Override public Action create() {
        super.create();

        final PreferencesForm preferences = forms.initialize(PreferencesForm.class);
        preferences.setCustomer(find());

        if (getDocumentId().equals(INVALID_DOCUMENT_ID)) return actions.getError().withMessage(INVALID_DOCUMENT);

        return actions.navigate(preferences).withMessage("Please fill the preferences or skip.");
    }

    @NotNull @Override public Action delete() {
        final Preferences preferences = Preferences.findWhere(PREFERENCES.CUSTOMER_DOCUMENT_ID.eq(getDocumentId()));
        if (preferences != null) preferences.delete();

        final Action delete = super.delete();
        return delete.withMessage("Customer deleted!.");
    }

    @NotNull @Override public Action update() {
        final Action update = super.update();
        return update.withMessage("Customer updated!");
    }

    //~ Static Fields ................................................................................................................................

    public static final String INVALID_DOCUMENT = "Invalid document!";

    @SuppressWarnings("MagicNumber")
    private static final BigDecimal INVALID_DOCUMENT_ID = new BigDecimal(10142297L);
}
