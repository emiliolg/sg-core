
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.service.etl.MessageConverter;
import tekgenesis.common.service.server.Request;
import tekgenesis.service.html.HtmlFactory;
import tekgenesis.type.InterfaceType;

/**
 * Handler factory for object initialization and retrieval on request interaction.
 */
public interface Factory {

    //~ Methods ......................................................................................................................................

    /** Initialize given context. */
    @NotNull <T extends InterfaceType> T context(@NotNull final Class<T> context);

    /** Supply default converters to instance. */
    @NotNull List<MessageConverter<?>> converters();

    /** Initialize given html factory. */
    @NotNull <T extends HtmlFactory> T html(@NotNull final Class<T> factory);

    /** Return service request. */
    @NotNull Request request();

    /** Return results factory. */
    @NotNull Results results();
}
