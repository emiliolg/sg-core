
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

/**
 * Duplicate attribute exception.
 */
public class RemoteViewWithAsException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public RemoteViewWithAsException(String name) {
        super("Remote view " + name + " has 'as' option defined", name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
