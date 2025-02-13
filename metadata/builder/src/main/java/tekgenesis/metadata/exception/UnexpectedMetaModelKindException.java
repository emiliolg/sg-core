
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.type.MetaModelKind;

import static java.lang.String.format;

import static tekgenesis.metadata.exception.BuilderErrors.createError;

/**
 * UnexpectedMetaModelKindException.
 */
public class UnexpectedMetaModelKindException extends BuilderErrorException {

    //~ Instance Fields ..............................................................................................................................

    private final String model;

    //~ Constructors .................................................................................................................................

    /** Default exception constructor. */
    public UnexpectedMetaModelKindException(MetaModelKind kind, String fqn, String model) {
        super(format("Unexpected MetaModel '%s' of kind '%s'", fqn, kind.name()));
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public BuilderError getBuilderError() {
        return createError(getMessage(), model);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3884874063530072016L;
}
