
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.link;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.QName;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.createQName;

/**
 * Utility class for Link.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class Links {

    //~ Constructors .................................................................................................................................

    private Links() {}

    //~ Methods ......................................................................................................................................

    /** Returns the url of the form to be included in an anchor. */
    public static String formLink(@NotNull final String fqn) {
        return formLink(fqn, null);
    }

    /** Returns the url of the form to be included in an anchor. */
    public static String formLink(@NotNull final String fqn, @Nullable final String pk) {
        return formLink(fqn, pk, null);
    }

    /** Returns the url of the form to be included in an anchor. */
    public static String formLink(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        return "#" + formUrl(fqn, pk, parameters);
    }

    /** Returns the url of the form to be included in an anchor. */
    public static String formUrl(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        return LOAD_FORM_URL_PREFIX + SLASH + fqn + (isEmpty(pk) ? "" : SLASH + pk) + (isEmpty(parameters) ? "" : QUESTION_MARK + parameters);
    }

    /** Returns QName for the given link. */
    public static QName getFqn(@NotNull final String formLink) {
        final int index    = formLink.indexOf(QUESTION_MARK);
        final int endIndex = index != -1 ? index : formLink.length();
        return createQName(formLink.substring(LOAD_FORM_URL_PREFIX.length() + 2, endIndex));
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String LOAD_FORM_URL_PREFIX = "form";
    @NonNls public static final String SLASH                = "/";
    @NonNls public static final String QUESTION_MARK        = "?";
}  // end class Links
