
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.exception;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.exception.SuiGenerisException;

import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;

/**
 * Form must have pk or bounded entity to be populated.
 */
public class FormCannotBePopulatedException extends SuiGenerisException {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String formName;

    //~ Constructors .................................................................................................................................

    /** Creates an exception. */
    public FormCannotBePopulatedException(@NotNull final String formName) {
        this.formName = formName;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return MSGS.formCannotBePopulated(formName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8270852571944430670L;
}
