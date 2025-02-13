
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for form: SuperNestedMultiples
 */
public class SuperNestedMultiples extends SuperNestedMultiplesBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getSections().add().init(1);
        getSections().add().init(2);
    }

    //~ Inner Classes ................................................................................................................................

    public class SectionsRow extends SectionsRowBase {
        @NotNull @Override public Action delete() {
            if (getSubf() != null) {
                if (getSubf().getTable().size() <= 1) remove();
                else getSubf().getTable().getCurrent().remove();
            }
            return actions().getDefault();
        }

        /** Init with number. */
        public void init(int i) {
            createSubf();
            if (getSubf() != null) getSubf().getTable().add().populate(i);
        }
    }
}
