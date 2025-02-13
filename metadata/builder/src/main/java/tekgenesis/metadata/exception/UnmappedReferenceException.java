
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
 * Exception to throw when a reference in a form that was inherited from the entity could not be
 * mapped back to the new form context.
 */
public class UnmappedReferenceException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public UnmappedReferenceException(final String entityRefName, final String modelName) {
        super(new BuilderException(String.format("Reference '%s' from the Form main Entity was not found", entityRefName), modelName) {
                private static final long serialVersionUID = 5590160399323323078L;
            });
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderException getCause() {
        return (BuilderException) super.getCause();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
