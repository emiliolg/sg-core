
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.server.Request;
import tekgenesis.common.service.server.Response;
import tekgenesis.common.util.Reflection;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlFactory;
import tekgenesis.service.html.HtmlMessageConverter;
import tekgenesis.type.InterfaceType;

import static tekgenesis.common.service.etl.MessageConverters.*;

class FactoryImpl implements Factory {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final HtmlBuilder                   builder;
    @NotNull private final List<MessageConverter<?>>     converters;
    @NotNull private final ReferenceSupplier<Forwarder>  forwarder;
    @NotNull private final InterfaceTypeProxy            proxy;
    @NotNull private final Request                       request;
    @NotNull private final ResultFactory                 results;

    //~ Constructors .................................................................................................................................

    FactoryImpl(@NotNull HtmlBuilder builder, @NotNull InterfaceTypeProxy proxy, @NotNull Request request, @NotNull Response response,
                @NotNull ReferenceSupplier<Forwarder> forwarder) {
        this.builder   = builder;
        this.proxy     = proxy;
        this.request   = request;
        this.forwarder = forwarder;
        converters     = initializeConverters();
        results        = new ResultFactory(response, converters);
    }

    //~ Methods ......................................................................................................................................

    /** Initialize given context. */
    @NotNull @Override public <T extends InterfaceType> T context(@NotNull Class<T> context) {
        return proxy.proxy(context);
    }

    @NotNull @Override public List<MessageConverter<?>> converters() {
        return converters;
    }

    /** Initialize given html factory. */
    @NotNull @Override public <T extends HtmlFactory> T html(@NotNull Class<T> factory) {
        return Reflection.construct(factory, builder);
    }

    /** Return request. */
    @NotNull @Override public Request request() {
        return request;
    }

    /** Return results factory. */
    @NotNull @Override public Results results() {
        return results;
    }

    /** Supply default converters. */
    @NotNull private List<MessageConverter<?>> initializeConverters() {
        final List<MessageConverter<?>> result = new ArrayList<>();

        // Add html converter
        result.add(new HtmlMessageConverter().withForwarder(forwarder));

        // Add resource converter
        result.add(new ResourceMessageConverter());

        // Add default converters
        withByteConverter(result::add);
        withXmlConverter(result::add);
        withStringConverter(result::add);
        withFormConverter(result::add);
        withBasicTypeConverter(result::add);
        withApplicationExceptionConverter(result::add);
        withJsonConverter(result::add);

        return result;
    }
}  // end class FactoryImpl
