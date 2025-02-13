
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
 * User class for Form: TestMeNow
 */
public class SubformClearForm extends SubformClearFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeFirst() {
        if (!getSomeSection().isEmpty()) {
            final SomeSectionRow someSectionRow = getSomeSection().get(0);
            final SomeNumberForm mysub          = someSectionRow.getMysub();
            if (mysub != null) {
                final double number = number();
                mysub.setSomeNumber(number);
                someSectionRow.setMyText(String.valueOf(number));
            }
        }
        return actions.getDefault();
    }

    @NotNull @Override public Action clear() {
        getSomeSection().clear();
        return actions.getDefault();
    }

    @NotNull @Override public Action clearAndAdd() {
        clear();
        justAdd();
        return actions.getDefault();
    }

    @NotNull @Override public Action justAdd() {
        final double         number = number();
        final SomeSectionRow add    = getSomeSection().add();
        add.createMysub().setSomeNumber(number);
        add.setMyText(String.valueOf(number));
        return actions.getDefault();
    }
    @Override public void load() {
        justAdd();
    }

    private double number() {
        return 1000 * Math.random();
    }

    //~ Inner Classes ................................................................................................................................

    public class SomeSectionRow extends SomeSectionRowBase {}
}  // end class SubformClearForm
