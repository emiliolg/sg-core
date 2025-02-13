
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.metadata.form.InvokeData;

/**
 * Represents a client side function invocation. Exposes invocation configuration methods.
 */
class InvokeImpl implements Invoke {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ArrayNode args;

    @NotNull private final String function;
    @NotNull private String       target = "";

    //~ Constructors .................................................................................................................................

    /** Create a client side invocation with given function name. */
    InvokeImpl(@NotNull final String function) {
        this.function = function;
        args          = JsonMapping.shared().createArrayNode();
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ObjectNode arg() {
        return args.addObject();
    }

    @NotNull @Override public Invoke arg(@NotNull final JsonNode j) {
        args.add(j);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final String s) {
        args.add(s);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final BigDecimal d) {
        args.add(d);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Boolean b) {
        args.add(b);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable DateOnly d) {
        final ObjectNode date = args.addObject();
        if (d != null) putDate(date, d.toMilliseconds(), d.toString());
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable DateTime d) {
        final ObjectNode date = args.addObject();
        if (d != null) putDate(date, d.toMilliseconds(), d.toString());
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Double d) {
        args.add(d);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Float f) {
        args.add(f);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Integer i) {
        args.add(i);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Long l) {
        args.add(l);
        return this;
    }

    @NotNull @Override public Invoke arg(@Nullable final Iterable<String> s) {
        final ArrayNode array = args.addArray();
        if (s != null) for (final String arg : s)
            array.add(arg);
        return this;
    }

    @NotNull @Override public Invoke target(@NotNull final String t) {
        target = t;
        return this;
    }

    InvokeData generateInvokeData() {
        final InvokeData d = new InvokeData(function);
        d.setArgs(args.toString());
        d.setTarget(target);
        return d;
    }

    private void putDate(ObjectNode date, long m, String s) {
        date.put(MILLISECONDS_FIELD, m);
        date.put(ISO_DATE_FIELD, s);
    }

    //~ Static Fields ................................................................................................................................

    private static final String ISO_DATE_FIELD     = "iso";
    private static final String MILLISECONDS_FIELD = "millis";
}  // end class InvokeImpl
