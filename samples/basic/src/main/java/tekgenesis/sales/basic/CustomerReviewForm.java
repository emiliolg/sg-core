
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

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Customer Review Form class.
 */
@SuppressWarnings("WeakerAccess")
public class CustomerReviewForm extends CustomerReviewFormBase {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("EmptyMethod")  // do not copyTo
    public void copyTo(@NotNull Customer customer) {
        // do nothing
    }

    @NotNull @Override public Action create() {
        return actions.getDefault();
    }

    @NotNull @Override public Customer find() {
        return ensureNotNull(Customer.find(DocType.valueOf(getDocumentType()), getDocumentId(), Sex.valueOf(getSex())), "Customer not found");
    }
}
