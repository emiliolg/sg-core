
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Form Action.
 */
@SuppressWarnings("FieldMayBeFinal")  // Gwt
public class FormAction implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String action;
    @NotNull private String label;

    //~ Constructors .................................................................................................................................

    FormAction() {
        action = "";
        label  = "";
    }

    /** Form Action. */
    public FormAction(@NotNull String action, @NotNull String label) {
        this.action = action;
        this.label  = label;
    }

    //~ Methods ......................................................................................................................................

    /** Get associated action. */
    @NotNull public String getAction() {
        return action;
    }

    /** Get action label. */
    @NotNull public String getLabel() {
        return label;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3624910200381738444L;

    public static final String SAVE = "save";
}
