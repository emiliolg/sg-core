
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

import static java.lang.String.valueOf;

/**
 * User class for form: ReadOnlyForm
 */
public class ReadOnlyForm extends ReadOnlyFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        forms.readOnly(true);
    }

    @NotNull @Override public Action otherSync() {
        return actions.getDefault();
    }

    @NotNull @Override public Action readOnly() {
        final boolean readOnly = !forms.isReadOnly();
        forms.readOnly(readOnly);
        final Action action = actions.getDefault();
        action.withCustomMessage(valueOf(readOnly)).sticky();
        return action;
    }
}
