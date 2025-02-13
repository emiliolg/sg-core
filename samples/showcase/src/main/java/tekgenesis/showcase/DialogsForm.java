
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
 * User class for Form: DialogsForm
 */
public class DialogsForm extends DialogsFormBase {

    //~ Instance Fields ..............................................................................................................................

    private boolean flag;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action confirm() {
        setText(BOCA);
        return actions.getDefault();
    }

    @NotNull @Override public Action focusText() {
        focus(Field.TEXT3);
        return actions.getDefault();
    }

    @NotNull @Override public Action goBackToPrevious() {
        setOther(false);
        setSome(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action hideDialog() {
        setSome(false);
        return actions.getDefault();
    }
    @Override public void load() {
        getTable().add();
        getTable().add();
        getSection().add();
        getSection().add();
    }

    @NotNull @Override public Action openDialog() {
        setDiag(true);
        focus(Field.TEXT2);
        return actions.getDefault();
    }

    @NotNull @Override public Action openSectionOne() {
        setOther(false);
        getSection().get(1).setSectionDialog(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action openSectionZero() {
        setOther(false);
        getSection().get(0).setSectionDialog(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action openTableOne() {
        setOther(false);
        getTable().get(1).setTableDialog(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action openTableZero() {
        setOther(false);
        getTable().get(0).setTableDialog(true);
        return actions.getDefault();
    }

    @NotNull @Override public Action putText4() {
        setText4("i am your father riber");
        return actions.getDefault();
    }

    /** Invoked when button($B1) is clicked. */
    @NotNull @Override public Action showOtherDialog() {
        setSome(false);
        setOther(true);
        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    public static final String BOCA = "Esto es boca";

    //~ Inner Classes ................................................................................................................................

    public class SectionRow extends SectionRowBase {
        @NotNull @Override public Action goBackToOther() {
            setSectionDialog(false);
            getTable().get(0).setTableDialog(true);
            return actions.getDefault();
        }
    }

    public class TableRow extends TableRowBase {
        @NotNull @Override public Action goBackToOther() {
            setTableDialog(false);
            getSection().get(0).setSectionDialog(true);
            return actions.getDefault();
        }
    }
}  // end class DialogsForm
