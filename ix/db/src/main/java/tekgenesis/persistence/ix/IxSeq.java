
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.BaseSeq;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.core.Option;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokerResult;
import tekgenesis.common.invoker.PathResource;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.service.Method;

import static tekgenesis.common.Predefined.cast;

/**
 * Ix List.
 */
public final class IxSeq<T> extends BaseSeq<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Class<T>            clazz;
    private final Map<String, String> headers;
    private final HttpInvoker         invoker;
    private final String              path;

    //~ Constructors .................................................................................................................................

    IxSeq(HttpInvoker invoker, String path, @NotNull Map<String, String> headers, @NotNull Class<T> clazz) {
        this.invoker = invoker;
        this.headers = headers;
        this.clazz   = clazz;
        this.path    = path;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ImmutableIterator<T> iterator() {
        return new ImmutableIterator<T>() {
            private Iterator<T> current = null;

            @Override public boolean hasNext() {
                populate();
                return current.hasNext();
            }

            @Override public T next() {
                populate();
                return current.next();
            }

            private void populate() {
                if (current == null || (!current.hasNext() && headers.get(HeaderNames.X_OFFSET) != null)) {
                    final PathResource<?> resource = invoker.resource(path);

                    if (!headers.isEmpty()) for (final String key : headers.keySet())
                        resource.header(key, headers.get(key));

                    final HttpInvokerResult<List<T>> list = resource.invoke(Method.GET, new GenericType<List<T>>(clazz) {}).execute();

                    final List<T> content = cast(list.get());
                    current = content.iterator();

                    // Reset Offset
                    final Option<String> value = list.getHeaders().getFirst(HeaderNames.X_OFFSET);

                    if (value.isPresent()) headers.put(HeaderNames.X_OFFSET, value.get());
                }
            }
        };
    }
}  // end class IxSeq
