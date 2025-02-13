
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.form.Suggestion;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.emptyList;

/**
 * User class for Form: Suggester
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class Suggester extends SuggesterBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the user type something on suggest_box(simpleSync) to create suggest list. */
    @NotNull @Override public Iterable<SimpleEntity> suggestSimpleSync(@Nullable String query) {
        System.out.println("query = " + query);
        return emptyIterable();
    }

    @NotNull @Override public Iterable<Suggestion> suggestStringSync(@Nullable String query) {
        System.out.println("query = " + query);
        return emptyList();
    }

    //~ Methods ......................................................................................................................................

    /** Invoked when the user type something on search_box(search) to create suggest list. */
    @NotNull public static Map<String, String> search(@Nullable String query) {
        System.out.println("query = " + query);

        return new HashMap<>();
    }

    /** Invoked when the user type something on search_box(searchParam) to create suggest list. */
    @NotNull public static Map<String, String> searchParam(@Nullable String query, @Nullable DateOnly value) {
        System.out.println("query = " + query);
        System.out.println("value = " + value);

        return new HashMap<>();
    }

    /** Invoked when the user type something on suggest_box(simple) to create suggest list. */
    @NotNull public static Iterable<SimpleEntity> suggestSimple(@Nullable String query) {
        System.out.println("query = " + query);

        return new ArrayList<>();
    }

    /** Invoked when the user type something on suggest_box(simpleParam) to create suggest list. */
    @NotNull public static Iterable<SimpleEntity> suggestSimpleParam(@Nullable String query, @Nullable DateOnly value) {
        System.out.println("query = " + query);
        System.out.println("value = " + value);

        return new ArrayList<>();
    }

    @NotNull public static Iterable<Suggestion> suggestString(String query) {
        System.out.println("query = " + query);

        return emptyList();
    }

    @NotNull public static Iterable<Suggestion> suggestStringParam(String query, DateOnly arg) {
        System.out.println("query = " + query);
        System.out.println("arg = " + arg);

        return emptyList();
    }
}  // end class Suggester
