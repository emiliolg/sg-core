
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
 * Exception thrown when the primary key is defined as default plus fields.
 */
class IllegalPrimaryKeyDefinition extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public IllegalPrimaryKeyDefinition(String name) {
        super("Illegal Primary Key definition in entity " + name, name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3467242391610143547L;
}
