
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import static java.lang.String.format;

/**
 * Exception to throw when a Field is not found.
 */
public class InvalidDefaultValueException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a NoDescribeByFieldException for an entity. */
    public InvalidDefaultValueException(String enumName, String defaultValue) {
        super(format("Invalid default value: '%s' for enum: %s.", enumName, defaultValue), enumName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4714024816463220557L;
}
