
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * The custom functions registry.
 */
public class FunctionRegistry {

    //~ Instance Fields ..............................................................................................................................

    private final List<Fun<?>>         functions       = new ArrayList<>();
    private final Map<String, Integer> idFromSignature = new HashMap<>();

    //~ Constructors .................................................................................................................................

    private FunctionRegistry() {
        register(new PredefinedFunctions());
    }

    //~ Methods ......................................................................................................................................

    /** All the functions in the Registry. */
    public Iterable<Fun<?>> functions() {
        return functions;
    }

    /** Registers a new function. */
    public void register(@NotNull final Fun<?> function) {
        // Validate  duplicates
        final String s = function.getSignature();
        if (idFromSignature.containsKey(s)) throw illegalArgumentException(s, " already registered.");
        // Add
        functions.add(function);
        // Add to map by signature
        idFromSignature.put(s, functions.size() - 1);
    }

    /** Returns the registered Method for the given s of fail with an IllegalArgumentException. */
    @NotNull public Fun<?> getFunction(@NotNull final String s) {
        return functions.get(getFunctionId(s));
    }

    private IllegalArgumentException illegalArgumentException(String name, String s) {
        return new IllegalArgumentException("Function: " + name + s);
    }

    /** Register a Function Bundle. */
    @SuppressWarnings("TypeMayBeWeakened")
    private void register(@NotNull final FunctionBundle bundle) {
        for (final Fun<?> fun : bundle)
            register(fun);
    }

    /** Get the numeric id of the function. */
    private int getFunctionId(String signature) {
        final Integer id = idFromSignature.get(signature);
        if (id == null) throw illegalArgumentException(signature, " doesn't exist or was not registered");
        return id;
    }

    //~ Methods ......................................................................................................................................

    /** Singleton instance. */
    public static FunctionRegistry getInstance() {
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static final FunctionRegistry instance = new FunctionRegistry();
}  // end class FunctionRegistry
