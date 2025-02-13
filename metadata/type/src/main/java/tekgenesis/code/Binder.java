
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import org.jetbrains.annotations.NotNull;

/**
 * Binder for functions and references.
 */
public interface Binder {

    //~ Instance Fields ..............................................................................................................................

    Binder EMPTY = new Default();

    //~ Methods ......................................................................................................................................

    /** Bind the function name to an actual function. */
    Fun<?> bindFunction(String name);

    /** Binds the permission name to the actual one. */
    BoundRef<Boolean> bindPermissionRef(String name);

    /** Binds the readOnly value to the actual one. */
    BoundRef<Boolean> bindReadOnly();

    /** Bind the reference name to an actual bound reference for late retrieval. */
    BoundRef<?> bindRef(String name, boolean col);

    /** Binds the update value to the actual one. */
    BoundRef<Boolean> bindUpdate();

    //~ Inner Classes ................................................................................................................................

    /**
     * Default Binder implementation.
     */
    class Default implements Binder {
        @NotNull final FunctionRegistry functions;

        protected Default() {
            functions = FunctionRegistry.getInstance();
        }

        @Override public Fun<?> bindFunction(String name) {
            return functions.getFunction(name);
        }

        @Override public BoundRef<Boolean> bindPermissionRef(String name) {
            throw new SymbolNotFoundException(name);
        }

        @Override public BoundRef<Boolean> bindReadOnly() {
            throw new SymbolNotFoundException();
        }

        @Override public BoundRef<Object> bindRef(String name, boolean col) {
            throw new SymbolNotFoundException(name);
        }

        @Override public BoundRef<Boolean> bindUpdate() {
            throw new SymbolNotFoundException();
        }
    }
}  // end interface Binder
