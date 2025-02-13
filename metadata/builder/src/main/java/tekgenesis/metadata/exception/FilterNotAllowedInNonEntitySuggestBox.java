
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

import tekgenesis.type.Type;

import static java.lang.String.format;

/**
 * Exception that is thrown when a SuggestBox of type different to Entity has FieldOption filter.
 */
public class FilterNotAllowedInNonEntitySuggestBox extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a FilterNotAllowedInNonEntitySuggestBox exception. */
    public FilterNotAllowedInNonEntitySuggestBox(@NotNull Type type, @NotNull String name) {
        super(format("SuggestBox of type %s does not allow FieldOption Filter", type), name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2465822371091701324L;
}
