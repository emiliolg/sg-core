
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlInstanceBuilder;

/**
 * Mail based on a template.
 */
@SuppressWarnings("WeakerAccess")
public class TemplateMail extends Mail {

    //~ Instance Fields ..............................................................................................................................

    private final HtmlInstanceBuilder<?> htmlBuilder;

    //~ Constructors .................................................................................................................................

    /** Template Mail constructor based on a Resource. */
    private TemplateMail(@NotNull HtmlInstanceBuilder<?> builder) {
        htmlBuilder = builder;
    }

    //~ Methods ......................................................................................................................................

    /** Pass arguments to TemplateMail. */
    public TemplateMail withArgument(@NotNull String name, @NotNull Object value) {
        htmlBuilder.param(name, value);
        return this;
    }

    /** Pass arguments to TemplateMail. */
    public TemplateMail withArguments(@NotNull Map<String, Object> args) {
        args.forEach(htmlBuilder::param);
        return this;
    }

    /** Set body is not allowed in Template mails. */
    @Override public Mail withBody(@NotNull String body) {
        throw new IllegalStateException("Set body content is not allowed");
    }

    /** Set body is not allowed in Template mails. */
    @Override public Mail withBody(@NotNull Html b)
        throws MailException
    {
        throw new IllegalStateException("Set html body content is not allowed");
    }

    /** Its not a simple Mail.* */
    @Override public boolean isSimple() {
        return false;
    }

    @Override void generate()
        throws MailException
    {
        super.withBody(htmlBuilder.build());
    }

    //~ Methods ......................................................................................................................................

    /** Creates a TemplateMail based on a jade template. */
    public static TemplateMail jade(@NotNull Resource resource) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).jade(resource));
    }

    /** Creates a TemplateMail based on a jade template. */
    public static TemplateMail jade(@NotNull String path) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).jade(path));
    }

    /** Creates a TemplateMail based on a jade template. */
    public static TemplateMail jadeSource(@NotNull String html) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).jadeSource(html));
    }

    /** Creates a TemplateMail based on a mustache template. */
    public static TemplateMail mustache(@NotNull Resource resource) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).mustache(resource));
    }

    /** Creates a TemplateMail based on a mustache template. */
    public static TemplateMail mustache(@NotNull String path) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).mustache(path));
    }

    /** Creates a TemplateMail based on a mustache template. */
    public static TemplateMail mustacheSource(@NotNull String html) {
        return new TemplateMail(Context.getSingleton(HtmlBuilder.class).mustacheSource(html));
    }
}  // end class TemplateMail
