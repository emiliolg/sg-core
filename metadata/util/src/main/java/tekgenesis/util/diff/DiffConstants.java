
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

/**
 * Diff constants.
 */
class DiffConstants {

    //~ Constructors .................................................................................................................................

    private DiffConstants() {}

    //~ Static Fields ................................................................................................................................

    static final String HAS_BEEN_ADDED_TO     = " has been added to ";
    static final String HAS_BEEN_REMOVED_FROM = " has been removed from ";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String CHECK = "check";

    static final String HAS_BEEN_ADDED = " has been added";

    static final String HAS_BEEN_REMOVED = " has been removed";
    static final String OPTION_SPC       = "Option ";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String MODEL_SPC     = "Model ";
    static final String HAS_CHANGED   = " has changed: ";
    static final String IN_ATTRIBUTE  = " in attribute ";
    static final String ATTRIBUTE_SPC = "Attribute ";
    static final String INDEX         = "Index on fields  ";
    static final String UNIQUE_INDEX  = "Unique index on fields ";
}
