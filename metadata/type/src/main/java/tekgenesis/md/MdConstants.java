
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.md;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Metadata related constants.
 */
@NonNls public interface MdConstants {

    //~ Instance Fields ..............................................................................................................................

    String CREATION_TIME    = "creationTime";
    String CREATION_USER    = "creationUser";
    String DATA_FIELD_NAME  = "_data";
    String DEPRECATED_FIELD = "_deprecated";
    String DEPRECATION_TIME = "deprecationTime";
    String DEPRECATION_USER = "deprecationUser";
    String UPDATE_TIME      = "updateTime";
    String UPDATE_USER      = "updateUser";
    String VERSION_FIELD    = "instanceVersion";

    ImmutableList<String> versionFields = listOf(UPDATE_TIME, VERSION_FIELD);

    //~ Methods ......................................................................................................................................

    static boolean isVersionField(String name) {
        return versionFields.contains(name);
    }
}
