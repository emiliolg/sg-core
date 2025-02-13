
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
 * User class for Form: TableMultipleOnChange
 */
@Generated(value = "tekgenesis/showcase/TableShowcase.mm", date = "1383578969282")
public class TableMultipleOnChange extends TableMultipleOnChangeBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action anyClick() {
        if (!forms.isCurrentWidget(Field.ANY)) throw new IllegalStateException("Expected ANY current widget!");
        if (forms.getCurrentWidget() != Field.ANY) throw new IllegalStateException("Expected ANY current widget!");
        appendLine("anyClick clicked!");
        return actions.getDefault();
    }

    @NotNull @Override public Action bchanged() {
        appendLine("twoTimesField");
        return actions.getDefault();
    }

    @NotNull @Override public Action create() {
        appendLine("save clicked!");
        return super.create();
    }

    @NotNull @Override public Action echanged() {
        appendLine("otherField");
        return actions.getDefault();
    }

    @NotNull @Override public Action sumchanged() {
        appendLine("sum");
        return actions.getDefault();
    }

    @NotNull @Override public Action setField() {
        setField(10);
        return actions.getDefault();
    }

    private void appendLine(final String field) {
        final boolean has     = getItems().current().isPresent();
        final String  current = "hasCurrent(): " + has + (has ? ", getCurrentIndex(): " + getItems().getCurrentIndex().get() : "");
        setProgressDebug(getProgressDebug() + field + " changed, " + current + "\n");
    }

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {
        /** Invoked when text_field(c2) value changes. */
        @NotNull @Override public Action changed() {
            appendLine("column2");
            setOtherField(getColumn1());
            return actions.getDefault();
        }
    }
}  // end class TableMultipleOnChange
