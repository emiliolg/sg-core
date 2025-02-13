
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
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;

/**
 * Represents a client side function invocation.
 */
@SuppressWarnings("UnusedReturnValue")
public interface Invoke {

    //~ Methods ......................................................................................................................................

    /** Set function custom argument. */
    @NotNull ObjectNode arg();

    /** Set function String argument. */
    @NotNull Invoke arg(@Nullable final String s);

    /** Set function BigDecimal argument. */
    @NotNull Invoke arg(@Nullable final BigDecimal d);

    /** Set function Boolean argument. */
    @NotNull Invoke arg(@Nullable final Boolean b);

    /** Set function DateOnly argument. */
    @NotNull Invoke arg(@Nullable final DateOnly d);

    /** Set function DateTime argument. */
    @NotNull Invoke arg(@Nullable final DateTime d);

    /** Set function Double argument. */
    @NotNull Invoke arg(@Nullable final Double d);

    /** Set function Float argument. */
    @NotNull Invoke arg(@Nullable final Float f);

    /** Set function Integer argument. */
    @NotNull Invoke arg(@Nullable final Integer i);

    /** Set function Long argument. */
    @NotNull Invoke arg(@Nullable final Long l);

    /** Set function String Array argument. */
    @NotNull Invoke arg(@Nullable final Iterable<String> s);

    /** Set function json node argument. */
    @NotNull Invoke arg(@NotNull final JsonNode j);

    /** Set function invocation target (a global invocation is performed if target is not set). */
    @NotNull Invoke target(@NotNull final String target);
}  // end interface Invoke
