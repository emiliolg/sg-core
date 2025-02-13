
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import javax.annotation.ParametersAreNonnullByDefault;

import static java.lang.String.format;

/**
 * Html template error.
 */
@ParametersAreNonnullByDefault class HtmlTemplateException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    HtmlTemplateException(final HtmlInstance.Mustache html, final Throwable cause) {
        super(format("%s file %s", cause.getMessage(), html.key()), cause);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3500351853914826933L;
}
