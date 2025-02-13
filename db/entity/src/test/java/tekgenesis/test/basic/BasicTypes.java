
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test.basic;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.test.basic.g.BasicTypesBase;

/**
 * User class for Entity: BasicTypes
 */
@Generated(value = "tekgenesis/test/basic/BasicTypes.mm", date = "1363103631615")
@SuppressWarnings("WeakerAccess")
public class BasicTypes extends BasicTypesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getAName() {
        return "";
    }

    @Override public BasicTypes setAName(@NotNull final String aName) {
        return this;
    }
}
