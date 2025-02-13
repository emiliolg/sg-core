
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: OnChangeShowcase
 */
@Generated(value = "tekgenesis/showcase/ExpressionShowcase.mm", date = "1378128805846")
public class OnChangeShowcase extends OnChangeShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void onLoad() {
        setItem(1);
    }

    @NotNull @Override public Action uiChanged() {
        System.out.println("getItem() = " + (isDefined(Field.ITEM) ? getItem() : "Not defined"));

        return actions.getDefault();
    }
}
