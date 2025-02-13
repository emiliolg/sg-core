
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
 * Interface for all Errors that originate in Builder.
 */
public interface BuilderError {

    //~ Methods ......................................................................................................................................

    /** Return the error description for the Error. */
    String getMessage();
    /** Return name of the model that the Error relates to. */
    String getModelName();
}
