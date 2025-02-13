
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.persistence.Criteria;
import tekgenesis.test.basic.g.AuthorSearcherBase;

import static tekgenesis.test.basic.g.AuthorTable.AUTHOR;

/**
 * User class for index and searching Author.
 */
public class AuthorSearcher extends AuthorSearcherBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Criteria allow() {
        return AUTHOR.NAME.ne("lucas").and(AUTHOR.LAST_NAME.ne("luppani"));
    }
}
