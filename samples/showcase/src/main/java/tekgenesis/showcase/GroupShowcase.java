
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

/**
 * Group showcase form class.
 */
public class GroupShowcase extends GroupShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<SectRow> sect = getSect();
        for (int i = 0; i < 5; i++)
            sect.add();
    }

    @NotNull @Override public Action popChange() {
        return actions.getDefault();
    }

    @NotNull @Override public Action tabChanged() {
        if (getTab1() == 0) {
            setTf1(date());
            setTf2(date());
        }
        else {
            setTf3(date());
            setTf4(date());
        }

        return actions.getDefault().withMessage("Tab changed! Tab index: " + getTab1());
    }

    private String date() {
        return DateTime.current().toString();
    }

    private String say() {
        return SERVER_SAYS_MATH_RANDOM + Math.random();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String SERVER_SAYS_MATH_RANDOM = "Server says: Math.random() = ";

    //~ Inner Classes ................................................................................................................................

    public class SectRow extends SectRowBase {}
}  // end class GroupShowcase
