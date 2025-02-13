
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.common.core.Builder;

/**
 * Exception occurred during {@link Builder} construction.
 */
public abstract class BuilderException extends Exception implements BuilderError {

    //~ Instance Fields ..............................................................................................................................

    private final String modelName;

    //~ Constructors .................................................................................................................................

    protected BuilderException(String msg, String modelName) {
        super(msg);
        this.modelName = modelName;
    }

    //~ Methods ......................................................................................................................................

    /** returns name of the model of which the exception is referring to. */
    public String getModelName() {
        return modelName;
    }

    //~ Static Fields ................................................................................................................................

    protected static final String TO_ENTITY              = " to entity ";
    protected static final String TO_TYPE                = " to type ";
    protected static final String TO_CASE                = " to case ";
    protected static final String TO_SEARCHABLE_OPTION   = " to searchable by option";
    protected static final String TO_FORM                = " to form ";
    protected static final String TO_MENU                = " to menu ";
    static final String           ADDING_DUPLICATE_FIELD = "Adding duplicate field ";

    private static final long serialVersionUID = 1892149089640076402L;
}
