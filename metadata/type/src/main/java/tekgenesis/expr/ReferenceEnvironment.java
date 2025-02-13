
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.SymbolNotFoundException;
import tekgenesis.common.core.QName;
import tekgenesis.type.Type;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.isQualified;

/**
 * A reference environment acting as Binder and RefTypeSolver that allows dot notation.
 */
public class ReferenceEnvironment extends SimpleReferenceSolver {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, ReferenceEnvironment> qualifications = new HashMap<>();

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ValueType bindRef(@NotNull String name, boolean col) {
        return isQualified(name) ? bindQualifiedRef(name, col) : super.bindRef(name, col);
    }

    /** Simple implementation of {@link RefTypeSolver}. */
    @NotNull @Override public Type doResolve(@NotNull String referenceName, boolean isColumn) {
        return bindRef(referenceName, false).getType();
    }

    @NotNull @Override public ReferenceEnvironment put(@NotNull String name, @NotNull Object o) {
        return (ReferenceEnvironment) super.put(name, o);
    }

    @NotNull @Override public ReferenceEnvironment put(@NotNull String name, @NotNull Object o, Type type) {
        return (ReferenceEnvironment) super.put(name, o, type);
    }

    /** Adds a new qualified Environment and returns it to be populated. */
    @NotNull public ReferenceEnvironment putEnvironment(@NotNull String name) {
        return qualifications.computeIfAbsent(name, k -> new ReferenceEnvironment());
    }

    private ValueType bindQualifiedRef(String fqn, boolean col) {
        final QName                qualified = stripScope(fqn);
        final ReferenceEnvironment scope     = qualifications.get(qualified.getQualification());
        if (scope == null) throw new SymbolNotFoundException(qualified.getQualification());
        return scope.bindRef(qualified.getName(), col);
    }

    private QName stripScope(String fqn) {
        final int dot = fqn.indexOf('.');
        return createQName(fqn.substring(0, dot), fqn.substring(dot + 1));
    }
}  // end class ReferenceEnvironment
