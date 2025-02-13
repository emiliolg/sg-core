
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
 * User class for form: WidgetInMultiple
 */
public class WidgetInMultiple extends WidgetInMultipleBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action add() {
        final FatherWidget father = getWidgets().add().getFather();
        father.setId("Server Alpha");
        father.getChild().setId("Server Theta");
        return actions().getDefault();
    }

    @Override public void load() {
        final FormTable<WidgetsRow> widgets = getWidgets();
        for (int i = 0; i < 3; i++) {
            final FatherWidget father = widgets.add().getFather();
            father.setId("Alpha" + i);
            father.getChild().setId("Theta" + i);
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override FatherWidget defineFather() {
            return new FatherWidget();
        }
    }
}
