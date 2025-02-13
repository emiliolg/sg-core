
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.DateTimeBase;
import tekgenesis.common.core.Enumeration;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.persistence.EntityInstance;

import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.MultiMap.createMultiMap;

/**
 * Form parameters instance.
 */
public abstract class FormParameters<T extends FormInstance<?>> {

    //~ Instance Fields ..............................................................................................................................

    private final MultiMap<Enum<?>, String> parameters = createMultiMap();

    //~ Methods ......................................................................................................................................

    /** Return set parameters as map. */
    public MultiMap<String, String> asMap() {
        final MultiMap<String, String> result = createMultiMap();
        parameters.asMap().forEach((key, value) -> result.putAll(((FormFieldRef) key).id(), value));
        return result;
    }

    protected <This extends FormParameters<T>> This put(final Enum<?> field, @Nullable final Object value) {
        if (nonNull(value)) parameters.put(field, stringParameter(value));
        return self();
    }

    protected <This extends FormParameters<T>> This putAll(final Enum<?> field, @Nullable final Iterable<?> value) {
        if (nonNull(value)) parameters.putAll(field, stringParameter(value));
        return self();
    }

    @NotNull protected <This extends FormParameters<T>> This self() {
        return cast(this);
    }

    //~ Methods ......................................................................................................................................

    private static Iterable<String> stringParameter(Iterable<?> v) {
        return filter(v, Objects::nonNull).map(FormParameters::stringParameter);
    }

    private static String stringParameter(Object v) {
        if (v instanceof DateTimeBase) return valueOf(((DateTimeBase<?>) v).toMilliseconds());
        if (v instanceof Enumeration) return ((Enumeration<?, ?>) v).name();
        if (v instanceof EntityInstance) return ((EntityInstance<?, ?>) v).keyAsString();
        return valueOf(v);
    }
}  // end class FormParameters
