
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.form.ActionType.EXTERNAL;

/**
 * External SuiGeneris form navigation action implementation.
 */
public class ExternalNavigateImpl<T> extends ActionImpl implements ExternalNavigate<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String  application;
    @NotNull private final String  endpoint;
    @Nullable private final String key;

    //~ Constructors .................................................................................................................................

    private ExternalNavigateImpl(@NotNull final String endpoint, @NotNull final String application, @Nullable final String key) {
        super(EXTERNAL);
        this.endpoint    = endpoint;
        this.application = application;
        this.key         = key;
    }

    //~ Methods ......................................................................................................................................

    /** Return external application composed url. */
    @NotNull public String getUrl() {
        return endpoint + getContext().getPath() + "#form/" + application + (isEmpty(key) ? "" : "/" + key);
    }

    //~ Methods ......................................................................................................................................

    /** Create external navigate action. */
    public static <T> ExternalNavigate<T> navigate(@NotNull final String endpoint, @NotNull final String application, @Nullable final String key) {
        return new ExternalNavigateImpl<>(endpoint, application, key);
    }
}
