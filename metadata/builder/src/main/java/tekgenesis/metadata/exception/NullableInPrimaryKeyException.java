
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
 * Exception thrown when a primary key is mark as optional.
 */
public class NullableInPrimaryKeyException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a OptionalAttributeInPrimaryKeyException for an entity. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public NullableInPrimaryKeyException(String entity, String attribute) {
        super("The attribute " + attribute + " is optional and is part of the primary key of the entity " + entity, attribute);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4714024816463220554L;
}
