
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
 * User class for form: IFrameShowcase
 */
public class IFrameShowcase extends IFrameShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action testChanged() {
        setDisplay(getTest());
        return actions.getDefault();
    }

    @NotNull @Override public Action urlTextChanged() {
        setTest(getUrlText());
        return actions.getDefault();
    }
}
