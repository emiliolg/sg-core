
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.common;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.Option;

import static tekgenesis.codegen.impl.java.JavaElement.Method;
import static tekgenesis.common.core.Option.some;

/**
 * Collect abstract methods. Join method by name, allow multiple methods with same name but
 * different signature.
 */
public class AbstractMethods {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ClassGenerator cg;

    @NotNull private final MultiMap<String, Method> map = MultiMap.createSortedMultiMap();

    //~ Constructors .................................................................................................................................

    /** Abstract methods constructor. */
    public AbstractMethods(@NotNull ClassGenerator cg) {
        this.cg = cg;
    }

    //~ Methods ......................................................................................................................................

    /** Add given method. */
    public void add(String name, Method method) {
        map.put(name, method);
    }

    /** Find all methods matching given name. */
    public Iterable<Method> findAllMethods(String methodName) {
        return map.get(methodName);
    }

    /** Add custom user method. */
    public Method generateAbstractMethod(String methodName, String type) {
        return generateAbstractMethod(methodName, type, EMPTY);
    }

    /** Add custom user method with arguments. */
    public Method generateAbstractMethod(String methodName, String type, JavaElement.Argument... args) {
        final Method method = cg.method(methodName, type).asAbstract().notNull();
        ImmutableList.fromArray(args).forEach(method::arg);
        map.put(methodName, method);
        return method;
    }

    /** Add custom user methods. Match only by name, check no signature. */
    public Option<Method> generateAbstractMethod(final String methodName, final String comment, final String type) {
        return generateAbstractMethod(methodName, comment, type, EMPTY);
    }

    /** Add custom user methods with arguments. Match only by name, check no signature. */
    public Option<Method> generateAbstractMethod(final String methodName, final String comment, final String type,
                                                 final JavaElement.Argument... args) {
        if (methodName.isEmpty()) return Option.empty();
        Method method = findAnyMethod(methodName);
        if (method == null) method = generateAbstractMethod(methodName, type, args);
        method.withComments(comment);
        return some(method);
    }

    /** Return true if given method name was already collected. */
    public boolean has(String name) {
        return map.containsKey(name);
    }

    /** Return all collected methods. */
    public Iterable<Method> methods() {
        final List<Method> set = new ArrayList<>();
        map.values().forEach(set::addAll);
        return set;
    }

    /** Find any method matching only given name. */
    @Nullable private Method findAnyMethod(String methodName) {
        final ImmutableCollection<Method> methods = map.get(methodName);
        return methods.getFirst().getOrNull();
    }

    //~ Static Fields ................................................................................................................................

    private static final JavaElement.Argument[] EMPTY = new JavaElement.Argument[0];
}  // end class AbstractMethods
