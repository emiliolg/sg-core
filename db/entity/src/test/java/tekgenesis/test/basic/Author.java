
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

import tekgenesis.test.basic.g.AuthorBase;

/**
 * User class for Entity: Author
 */
@Generated(value = "tekgenesis/test/basic/ImporExport.mm", date = "1365455058110")
@SuppressWarnings("ALL")
public class Author extends AuthorBase {

    //~ Methods ......................................................................................................................................

    @Override public boolean isForce() {
        return "joaquin".equals(getName()) && "buccaa".equals(getLastName());
    }

    @NotNull @Override public String getExtra() {
        return "groso";
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5386141969984409714L;
}
