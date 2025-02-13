
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
 * User class for widget: MultipleInWidget
 */
public class MultipleInWidget extends MultipleInWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action add() {
        final ChoicesRow row = getChoices().add();
        row.setLabel("Server Value");
        return actions().getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class ChoicesRow extends ChoicesRowBase {
        @NotNull @Override public Action click() {
            setLabel("Click Value");
            return actions().getDefault();
        }
    }
}
