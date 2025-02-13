
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

import tekgenesis.showcase.g.ListingBase;
import tekgenesis.showcase.g.ViewDataBase;

/**
 * User class for Entity: ViewData
 */
@SuppressWarnings("WeakerAccess")
public class ViewData extends ViewDataBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String toString() {
        return "ViewData {current: " + getCurrent() + ", rows: " + getItems().map(ListingBase::getPk) + "}";
    }

    /** Gets current item id. */
    public Listing getCurrentItemId() {
        return getItems().get(getCurrent());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -193796701720907919L;
}
