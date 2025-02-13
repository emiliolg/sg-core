
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

import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * A custom Function.
 */
public abstract class Fun<R> extends Symbol {

    //~ Instance Fields ..............................................................................................................................

    private final Type[] argTypes;

    private final Type returnType;

    //~ Constructors .................................................................................................................................

    /** Custom function default constructor. */
    @SuppressWarnings("WeakerAccess")
    protected Fun(@NotNull String name, Type returnType, Type... argTypes) {
        super(name);
        this.returnType = returnType;
        this.argTypes   = argTypes;
    }

    //~ Methods ......................................................................................................................................

    /** Returns false if the function cannot be constant. */
    public boolean canBeConstant() {
        return true;
    }

    /** Execute the function with the given args. */
    public abstract R invoke(Object[] args);

    /** Returns the arguments types of the function. */
    public final Type[] getArgTypes() {
        return argTypes;
    }

    /** Return the arity (Number of arguments) of the function. */
    public final int getArity() {
        return getArgTypes().length;
    }

    /** Returns the return type of the method. */
    public final Type getReturnType() {
        return returnType;
    }

    /**
     * Returns the signature of the function Having a signature different fro the name allows to
     * have overload.
     */
    public String getSignature() {
        return getName();
    }

    void invokeUsing(Evaluator ev) {
        final int      arity = getArgTypes().length;
        final Object[] args  = new Object[arity];
        for (int i = 1; i <= arity; i++)
            args[arity - i] = ev.pop();
        ev.push(invoke(args));
    }

    //~ Inner Classes ................................................................................................................................

    public abstract static class Fun0<R> extends Fun<R> {
        Fun0(String name, Type type) {
            super(name, type);
        }

        @Override public final R invoke(Object[] args) {
            return invoke();
        }

        protected abstract R invoke();

        @Override void invokeUsing(Evaluator ev) {
            ev.push(invoke());
        }
    }

    public abstract static class Fun1<T, R> extends Fun<R> {
        Fun1(String name, Type returnType, Type arg1) {
            super(name, returnType, arg1);
        }

        @Override
        @SuppressWarnings("unchecked")
        public final R invoke(Object[] args) {
            return invoke((T) args[0]);
        }

        protected abstract R invoke(T t);

        @Override
        @SuppressWarnings("unchecked")
        void invokeUsing(Evaluator ev) {
            final T v = (T) ev.pop();
            ev.push(invoke(v));
        }
    }

    public abstract static class Fun2<T1, T2, R> extends Fun<R> {
        Fun2(String name, Type returnType, Type arg1, Type arg2) {
            super(name, returnType, arg1, arg2);
        }

        @Override
        @SuppressWarnings("unchecked")
        public final R invoke(Object[] args) {
            return invoke((T1) args[0], (T2) args[1]);
        }

        protected abstract R invoke(T1 a1, T2 a2);
    }

    public abstract static class Fun3<T1, T2, T3, R> extends Fun<R> {
        Fun3(String name, Type returnType, Type arg1, Type arg2, Type arg3) {
            super(name, returnType, arg1, arg2, arg3);
        }

        @Override
        @SuppressWarnings("unchecked")
        public final R invoke(Object[] args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2]);
        }

        protected abstract R invoke(T1 a1, T2 a2, T3 a3);
    }

    abstract static class Real extends Fun1<Double, Double> {
        Real(String name) {
            super(name, Types.realType(), Types.realType());
        }
    }
}  // end class Fun
