
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.service.html.HtmlInstanceBuilder.Jade;
import tekgenesis.service.html.HtmlInstanceBuilder.Mustache;
import tekgenesis.service.html.HtmlInstanceBuilder.Static;
import tekgenesis.service.html.HtmlInstanceBuilder.Xhtml;
import tekgenesis.type.Struct;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.service.html.HtmlSourceProvider.*;

/**
 * Html Builder for different templates and sources.
 */
@ParametersAreNonnullByDefault public class HtmlBuilderImpl implements HtmlBuilder {

    //~ Constructors .................................................................................................................................

    /** Default constructor for injection. */
    public HtmlBuilderImpl() {}

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Xhtml html(@NotNull String path) {
        return new XhtmlInstanceBuilderImpl(fromPath(path));
    }

    @NotNull @Override public Xhtml html(@NotNull Resource resource) {
        return new XhtmlInstanceBuilderImpl(fromResource(resource));
    }

    @NotNull @Override public Xhtml htmlSource(@NotNull String html) {
        return new XhtmlInstanceBuilderImpl(fromString(html));
    }

    @NotNull @Override public Jade jade(@NotNull String path) {
        return new JadeInstanceBuilderImpl(fromPath(path));
    }

    @NotNull @Override public Jade jade(@NotNull Resource resource) {
        return new JadeInstanceBuilderImpl(fromResource(resource));
    }

    @NotNull @Override public Jade jadeSource(@NotNull String html) {
        return new JadeInstanceBuilderImpl(fromString(html));
    }

    @NotNull @Override public Mustache mustache(@NotNull String path) {
        return new MustacheInstanceBuilderImpl(fromPath(path));
    }

    @NotNull @Override public Mustache mustache(@NotNull Resource resource) {
        return new MustacheInstanceBuilderImpl(fromResource(resource));
    }

    @NotNull @Override public Mustache mustacheSource(@NotNull String html) {
        return new MustacheInstanceBuilderImpl(fromString(html));
    }

    @NotNull @Override public Static staticSource(@NotNull String html) {
        return new StaticInstanceBuilderImpl(fromString(html));
    }

    //~ Inner Classes ................................................................................................................................

    private abstract class BaseInstanceBuilderImpl<T extends HtmlInstanceBuilder<T>> implements HtmlInstanceBuilder<T> {
        final Map<String, Map<String, String>> messages   = new HashMap<>();
        final Map<String, Object>              parameters = new HashMap<>();
        final HtmlSourceProvider               provider;

        final Map<String, Html> views = new HashMap<>();

        protected BaseInstanceBuilderImpl(final HtmlSourceProvider provider) {
            this.provider = provider;
        }

        @NotNull @Override public T html(@NotNull final String name, @NotNull final Html value) {
            views.put(name, value);
            return This();
        }

        @NotNull @Override public T msg(@NotNull final String name, @NotNull final String enumeration) {
            final Map<String, String> labels = new HashMap<>();
            for (final Enumeration<?, ?> e : Enumerations.getValuesFor(enumeration))
                labels.put(e.name(), e.label());
            messages.put(name, labels);
            return This();
        }

        @NotNull @Override public T param(@NotNull final String name, @Nullable final Object value) {
            parameters.put(name, value);
            return This();
        }

        private T This() {
            return cast(this);
        }
    }

    private class JadeInstanceBuilderImpl extends BaseInstanceBuilderImpl<Jade> implements Jade {
        private JadeInstanceBuilderImpl(HtmlSourceProvider provider) {
            super(provider);
        }

        @Override public HtmlInstance.Jade build() {
            return new HtmlInstance.Jade(parameters, views, messages, provider);
        }

        @NotNull @Override public Jade html(@NotNull final String name, @NotNull final Html value) {
            throw Predefined.notImplemented("Jade html params");
        }
    }

    private class MustacheInstanceBuilderImpl extends BaseInstanceBuilderImpl<Mustache> implements Mustache {
        private MustacheInstanceBuilderImpl(HtmlSourceProvider provider) {
            super(provider);
        }

        @Override public HtmlInstance.Mustache build() {
            return new HtmlInstance.Mustache(parameters, views, messages, provider);
        }
    }

    private class StaticInstanceBuilderImpl extends BaseInstanceBuilderImpl<Static> implements Static {
        private StaticInstanceBuilderImpl(HtmlSourceProvider provider) {
            super(provider);
        }

        @Override public HtmlInstance.Static build() {
            return new HtmlInstance.Static(parameters, views, messages, provider);
        }
    }

    private class XhtmlInstanceBuilderImpl extends BaseInstanceBuilderImpl<Xhtml> implements Xhtml {
        @NotNull private final List<Tuple<String, String>> metadata = new ArrayList<>();
        @NotNull private final Map<String, Object>         structs  = new HashMap<>();

        private XhtmlInstanceBuilderImpl(final HtmlSourceProvider provider) {
            super(provider);
        }

        @Override public HtmlInstance.Xhtml build() {
            return new HtmlInstance.Xhtml(parameters, structs, views, messages, metadata, provider);
        }

        public Xhtml metadata(@NotNull String name, @NotNull String value) {
            metadata.add(tuple(name, value));
            return this;
        }

        @NotNull public Xhtml str(@NotNull String name, @NotNull String value) {
            parameters.put(name, value);
            return this;
        }

        @NotNull public Xhtml str(@NotNull String name, @NotNull Seq<String> values) {
            parameters.put(name, values);
            return this;
        }

        @NotNull public Xhtml struct(@NotNull String name, @NotNull Struct value) {
            structs.put(name, value);
            return this;
        }

        @NotNull public Xhtml struct(@NotNull String name, @NotNull Seq<? extends Struct> values) {
            structs.put(name, values);
            return this;
        }
    }
}  // end class HtmlBuilderImpl
