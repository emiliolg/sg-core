
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

import static tekgenesis.common.Predefined.isEmpty;

/**
 * User class for form: WidgetInMultipleWithChainedOnChanges
 */
public class WidgetInMultipleWithChainedOnChanges extends WidgetInMultipleWithChainedOnChangesBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getWidgets().add();
    }

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override OnChangesWidget defineChanges() {
            return new OnChangesWidget() {
                @Override void call(@NotNull final String source) {
                    final String calls = getCalls();
                    if (isEmpty(calls)) setCalls(source);
                    else setCalls(calls + "," + source);
                }
            };
        }
    }
}
