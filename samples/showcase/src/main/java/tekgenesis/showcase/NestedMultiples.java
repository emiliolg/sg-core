
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
import tekgenesis.form.FormTable;

/**
 * User class for form: NestedMultiples
 */
public class NestedMultiples extends NestedMultiplesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action addComplex() {
        final MultipleInWidget                       multiple = getWidgets().add().getMultiple();
        final FormTable<MultipleInWidget.ChoicesRow> choices  = multiple.getChoices();
        choices.add().setLabel("Nested1");
        choices.add().setLabel("Nested2");
        choices.add().setLabel("Nested3");
        return actions().getDefault();
    }

    @NotNull @Override public Action addSimple() {
        getWidgets().add().getMultiple();
        return actions().getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override MultipleInWidget defineMultiple() {
            return new MultipleInWidget();
        }
    }
}
