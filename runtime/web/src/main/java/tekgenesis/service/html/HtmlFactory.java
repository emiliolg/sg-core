
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;

/**
 * Html factory marker.
 */
public abstract class HtmlFactory {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HtmlBuilder builder;

    //~ Constructors .................................................................................................................................

    /** Creates a HtmlFactory. */
    @SuppressWarnings("ConstructorNotProtectedInAbstractClass")
    public HtmlFactory(@NotNull HtmlBuilder builder) {
        this.builder = builder;
    }

    //~ Methods ......................................................................................................................................

    /** Return a Xhtml Html instance builder for given path file. */
    @NotNull public HtmlInstanceBuilder.Xhtml valueOf(@NotNull final String path) {
        return builder.html(path);
    }

    /** Return a Xhtml Html instance builder for given resource. */
    @NotNull public HtmlInstanceBuilder.Xhtml valueOf(@NotNull final Resource resource) {
        return builder.html(resource);
    }

    /** Return a Jade Html instance builder for given path file. */
    @NotNull public HtmlInstanceBuilder.Jade valueOfJade(@NotNull final String path) {
        return builder.jade(path);
    }

    /** Return a Jade Html instance builder for given resource. */
    @NotNull public HtmlInstanceBuilder.Jade valueOfJade(@NotNull final Resource resource) {
        return builder.jade(resource);
    }

    /** Return a Jade Html instance builder for given html source. */
    @NotNull public HtmlInstanceBuilder.Jade valueOfJadeSource(@NotNull final String html) {
        return builder.jadeSource(html);
    }

    /** Return a Mustache Html instance builder for given path file. */
    @NotNull public HtmlInstanceBuilder.Mustache valueOfMustache(@NotNull final String path) {
        return builder.mustache(path);
    }

    /** Return a Mustache Html instance builder for given resource. */
    @NotNull public HtmlInstanceBuilder.Mustache valueOfMustache(@NotNull final Resource resource) {
        return builder.mustache(resource);
    }

    /** Return a Mustache Html instance builder for given html source. */
    @NotNull public HtmlInstanceBuilder.Mustache valueOfMustacheSource(@NotNull final String html) {
        return builder.mustacheSource(html);
    }

    /** Return a Xhtml Html instance builder for given html source. */
    @NotNull public HtmlInstanceBuilder.Xhtml valueOfSource(@NotNull final String html) {
        return builder.htmlSource(html);
    }
}  // end class HtmlFactory
