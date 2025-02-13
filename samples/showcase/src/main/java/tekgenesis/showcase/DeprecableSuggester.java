
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Suggestion;

import static tekgenesis.common.collections.Colls.emptyList;

/**
 * User class for Form: DeprecableSuggester
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class DeprecableSuggester extends DeprecableSuggesterBase {

    //~ Methods ......................................................................................................................................

    @NotNull public static Iterable<Suggestion> suggest(String query, boolean deprecated, String arg) {
        System.out.println("query = " + query);
        System.out.println("deprecated = " + deprecated);
        System.out.println("arg = " + arg);

        return emptyList();
    }
}
