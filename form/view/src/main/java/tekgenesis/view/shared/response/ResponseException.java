
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Creates a Response for a SuiGeneris Exception. This is not an error, is a valid exception thrown
 * by SuiGeneris code.
 */
@SuppressWarnings("FieldMayBeFinal")
public class ResponseException implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String exceptionMessage;

    //~ Constructors .................................................................................................................................

    /** Required no-arg contructor. */
    public ResponseException() {
        exceptionMessage = "";
    }

    /** Creates a ResponseException for a SuiGenerisException. */
    public ResponseException(@NotNull String e) {
        exceptionMessage = e;
    }

    //~ Methods ......................................................................................................................................

    /** Returns a well-formatted user message to be displayed based on the SuiGenerisException. */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7782267159595608850L;
}
