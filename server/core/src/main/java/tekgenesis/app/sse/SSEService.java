
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.sse;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

/**
 */
public interface SSEService<T> {

    //~ Methods ......................................................................................................................................

    /** Publish msg over channel. */
    void publish(@NotNull String channel, @NotNull T msg);

    /** Subscribe to msg. */
    void subscribe(@NotNull String channel, Consumer<T> consumer);
}
