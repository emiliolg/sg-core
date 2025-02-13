
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

/**
 * Exception that is thrown when a SuggestBox of type a non searchable Entity has no on_suggest or
 * on_suggest_sync.
 */
public class NoSearchableForSuggestBox extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a NoSearchableForSuggestBox exception. */
    public NoSearchableForSuggestBox(@NotNull String entityName, @NotNull String name) {
        super(format("Entity %s must be searchable for suggest_box", entityName), name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2465822371091300116L;
}
