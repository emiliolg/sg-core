
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

/**
 * Form navigate action.
 */
public class RedirectImpl extends ActionImpl implements Redirect {

    //~ Instance Fields ..............................................................................................................................

    private boolean inline;
    private String  url;

    //~ Constructors .................................................................................................................................

    private RedirectImpl(final String url) {
        super(ActionType.REDIRECT);
        this.url = url;
    }

    //~ Methods ......................................................................................................................................

    /** Specify if the redirect should be inline in the form box. */
    @NotNull @Override public Redirect inline() {
        inline = true;
        return this;
    }

    /** Return if the redirect is inline. */
    public boolean isInline() {
        return inline;
    }

    /** Return out-of-flow url navigation. */
    @NotNull public String getUrl() {
        return url;
    }

    @NotNull Redirect withRedirect(@NotNull final String redirect) {
        url = redirect;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Invoked from interface. User initialized the instance. */
    static RedirectImpl redirect(@NotNull final String url) {
        return new RedirectImpl(url);
    }
}  // end class RedirectImpl
