
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
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefContextMapper;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.core.Option.ofNullable;

/**
 * Expression byte code holder.
 */
public class CompiledExpression implements Expression.Implementation {

    //~ Instance Fields ..............................................................................................................................

    private final Code[] code;

    private final Type type;

    //~ Constructors .................................................................................................................................

    /** Creates a compiled expression with its byte code. */
    public CompiledExpression(Type type, final List<Code> code) {
        this.type = type;
        this.code = code.toArray(new Code[code.size()]);
    }

    private CompiledExpression(final Type type, final Code c) {
        this.type = type;
        code      = new Code[] { c };
    }

    //~ Methods ......................................................................................................................................

    /** Solve late bindings (Function and Variables references). */
    public void bind(Binder binder) {
        for (final Code c : code)
            c.bind(binder);
    }

    @Override public void compile(RefTypeSolver solver) {}

    /** Evaluate the Expression. */
    public Object evaluate(@NotNull Evaluator evaluator, @Nullable Object context) {
        try {
            return evaluator.evaluate(code, context);
        }
        catch (final ArrayIndexOutOfBoundsException e) {
            logger.error("Error evaluating " + toString());
            throw e;
        }
    }  // end method evaluate

    @Override public List<String> extractStrings() {
        final List<String> strings = new ArrayList<>();
        for (final Code c : code) {
            if (c instanceof Constant) {
                final Object o = ((Constant) c).getValue();
                if (o instanceof String) strings.add((String) o);
            }
        }
        return strings;
    }

    @Override public Expression.Implementation reMapReferences(final RefContextMapper mapper) {
        throw new IllegalStateException();
    }

    @Override public Expression.Implementation replaceStrings(List<String> strings) {
        if (strings.isEmpty()) return this;
        final List<Code> newCode = new ArrayList<>(code.length);
        int              s       = 0;
        for (final Code c : code) {
            Code newC = c;
            if (c instanceof Constant) {
                final Constant constant = (Constant) c;
                if (constant.isString()) newC = constant.replaceString(strings.get(s++));
            }
            newCode.add(newC);
        }
        return new CompiledExpression(type, newCode);
    }

    public Collection<String> retrieveReferences() {
        final Collection<String> refs = new ArrayList<>();
        for (final Code c : code) {
            if (c instanceof RefAccess) refs.add(((RefAccess) c).getName());
        }
        return refs;
    }

    /** Serialize a compiled expression. */
    @Override public void serialize(StreamWriter w) {
        Kind.serializeType(w, type.getFinalType());
        w.writeInt(code.length);

        for (final Code c : code) {
            final Instruction instruction = c.getInstruction();
            w.writeInt(instruction.ordinal());

            switch (instruction) {
            case REF:
                assert c instanceof RefAccess;
                final RefAccess ref = (RefAccess) c;
                w.writeString(ref.getName());
                w.writeBoolean(ref.isCol());
                break;
            case FUN:
                assert c instanceof FunctionCall;
                final FunctionCall fun = (FunctionCall) c;
                w.writeString(fun.getName());
                break;
            case FORBIDDEN:
                assert c instanceof ForbiddenAccess;
                final ForbiddenAccess fa = (ForbiddenAccess) c;
                w.writeString(fa.getName());
                break;
            case PUSH:
                assert c instanceof Constant;
                final Constant constant = (Constant) c;
                constant.getKind().serializeValue(w, constant.getValue());
                break;
            case OR:
            case AND:
            case IF:
            case ELSE:
                assert c instanceof Jump;
                final Jump jump = (Jump) c;
                w.writeInt(jump.getAddress());
                break;
            case LIST:
                assert c instanceof ListCode;
                final ListCode list = ((ListCode) c);
                w.writeInt(list.getSize());
                break;
            default:
                // nothing more to do
            }
        }
    }  // end method serialize

    @Override public String toString() {
        final StringBuilder b = new StringBuilder("Code\n");
        for (final Code c : code)
            b.append(c.toString()).append("\n");
        return b.toString();
    }

    @Override public Option<Object> getConstantValue() {
        if (code.length == 1) {
            final Code c = code[0];
            if (c instanceof Constant) return ofNullable(((Constant) c).getValue());
        }
        return Option.empty();
    }

    @Override public boolean isCompiled() {
        return true;
    }

    /** Returns true if its a constant expression. */
    public boolean isConstant() {
        return code.length == 1 && code[0] instanceof Constant;
    }

    @Override public Type getType() {
        return type;
    }

    //~ Methods ......................................................................................................................................

    /** Creates a simple constant expression. */
    public static CompiledExpression constant(final Object obj) {
        final Type type = Types.typeOf(obj);
        return new CompiledExpression(type, new Constant(type.getKind(), obj));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(CompiledExpression.class);
}  // end class CompiledExpression
