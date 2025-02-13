
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
 * User: diego Date: 12/26/11 Time: 6:06 PM
 */
public class DuplicateDefinitionException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public DuplicateDefinitionException(String name) {
        super("Adding duplicate definition " + name, name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
