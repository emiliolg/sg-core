
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.js;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.Element;
import tekgenesis.codegen.impl.js.JsArtifactGenerator.LastLine;
import tekgenesis.common.IndentedWriter;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.StrBuilder;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.isPrivate;

import static tekgenesis.codegen.impl.js.JsArtifactGenerator.LastLine.NEW_LINE;
import static tekgenesis.codegen.impl.js.JsArtifactGenerator.LastLine.NO_NEW_LINE;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.util.JavaReservedWords.RETURN;
import static tekgenesis.common.util.JavaReservedWords.THIS;

/**
 * Elements containing a name and modifiers.
 */
@SuppressWarnings({ "UnusedReturnValue", "WeakerAccess", "ClassWithTooManyMethods" })
public class JsElement<T extends JsElement<T>> extends Element<T> {

    //~ Instance Fields ..............................................................................................................................

    int modifiers;

    //~ Constructors .................................................................................................................................

    JsElement(@NotNull String nm, @NotNull String t) {
        super(nm, t);
        modifiers = PUBLIC;
    }

    //~ Methods ......................................................................................................................................

    /** Add the given modifier {@link Modifier } to the element. */
    public T addModifier(int m) {
        modifiers = m | ((m & VISIBILITY) != 0 ? modifiers & ~VISIBILITY : modifiers);
        return cast(this);
    }
    /** Makes the item <code>private</code>. */
    public T asPrivate() {
        return addModifier(PRIVATE);
    }

    /** Makes the item <code>public</code>. */
    public T asPublic() {
        return addModifier(PUBLIC);
    }

    /** Builds a <code>this</code> reference. */
    public String THIS(String field) {
        return THIS + "." + field;
    }

    String baseDeclaration(StrBuilder result) {
        modifiers(result);
        type(result);
        name(result);
        return result.toString();
    }

    String declaration(JsArtifactGenerator ag) {
        final StrBuilder result = new StrBuilder().startCollection(" ");
        return baseDeclaration(result);
    }

    void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {}

    void generateCollection(IndentedWriter w, JsArtifactGenerator ag, Iterable<? extends JsElement<?>> elements) {
        boolean next = false;
        for (final JsElement<?> arg : elements) {
            if (next) w.print(", ");
            arg.generate(w, ag, NO_NEW_LINE);
            next = true;
        }
    }

    /** Element length. */
    int length() {
        return 1;
    }

    void name(StrBuilder result) {
        result.appendElement(getName());
    }

    T removeModifier(int m) {
        modifiers = modifiers & ~m;
        return cast(this);
    }

    void type(StrBuilder result) {}

    private void modifiers(StrBuilder result) {
        if (isPrivate(modifiers)) result.appendElement("var");
    }

    //~ Static Fields ................................................................................................................................

    private static final int VISIBILITY = (PUBLIC | PROTECTED | PRIVATE);

    //~ Inner Classes ................................................................................................................................

    public static class Argument extends JsElement<Argument> {
        /** Create an Argument. */
        public Argument(@NotNull String nm) {
            super(nm, ARGUMENT);
            modifiers &= ~VISIBILITY;
        }
    }

    public static class Array extends JsElement<Array> {
        @NotNull private final List<JsElement<?>> elements;

        @SuppressWarnings("DuplicateStringLiteralInspection")
        Array() {
            super("", "array");
            elements = new ArrayList<>();
        }

        /** Adds an element to array (treated as constant). */
        public Array elem(String element) {
            return elem(new Constant(element));
        }

        /** Adds an element to array. */
        public Array elem(JsElement<?> element) {
            elements.add(element);
            return this;
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.print("[");
            generateCollection(w, ag, elements);
            visit.print(w, "]");
        }
    }

    private abstract static class Block<T extends Block<T>> extends JsItemGenerator<T> {
        protected Block(@NotNull JsArtifactGenerator ag, @NotNull String nm) {
            super(ag, nm, "");
        }

        @Override protected void generate(@NotNull IndentedWriter w, @NotNull JsArtifactGenerator ag, @NotNull LastLine visit) {
            generateBefore(w, ag, visit);
            w.indent();
            super.generate(w, ag, visit);
            w.unIndent();
            generateAfter(w, ag, visit);
        }

        protected void generateAfter(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            visit.print(w, "}");
        }

        protected void generateBefore(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.println("{");
        }
    }

    public static class Constant extends JsElement<Constant> {
        Constant(@NotNull String constant) {
            super(constant, CONSTANT);
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            visit.print(w, getName());
        }
    }

    public static class DeclareStatement extends JsElement<DeclareStatement> {
        @NotNull private final JsElement<?> statement;

        DeclareStatement(@NotNull String nm, @NotNull JsElement<?> statement) {
            super(nm, "");
            this.statement = statement;
        }

        @Override public DeclareStatement asPrivate() {
            statement.asPrivate();
            return this;
        }

        @Override public DeclareStatement asPublic() {
            statement.asPublic();
            return this;
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            if (isPrivate(statement.modifiers)) w.print(Constants.VAR + " ");
            w.print(getName() + " = ");
            statement.generate(w, ag, NO_NEW_LINE);
            visit.print(w, ";");
        }

        @Override int length() {
            return statement.length();
        }
    }

    public static class Function extends JsItemGenerator<Function> {
        @NotNull final List<Argument> arguments;

        /** Inline function if possible. */
        private boolean inline = true;

        Function(JsArtifactGenerator ag, @NotNull String nm) {
            // noinspection DuplicateStringLiteralInspection
            super(ag, nm, "function");
            arguments = new LinkedList<>();
            inline    = true;
        }

        /** Adds an argument to the function. */
        public Argument arg(@NotNull String nm) {
            return addArgument(new Argument(nm));
        }

        /** Adds a set of arguments to the function. */
        public Function arguments(@NotNull Iterable<Argument> args) {
            for (final Argument a : args)
                addArgument(a);
            return this;
        }

        /** Mark function as anonymous. */
        public Function asAnonymous() {
            return addModifier(Modifier.NATIVE);
        }

        /** Insert line break. */
        public Function blankLine() {
            elements.add(new LineBreak());
            return this;
        }

        /** Expand function even if it's a one-liner. */
        public Function expand() {
            inline = false;
            return this;
        }

        /** Return invocation to this function. */
        public JsElement<?> invoke() {
            return new InvokeStatement(this);
        }

        /** Return the list of arguments. */
        @NotNull public List<Argument> getArguments() {
            return arguments;
        }

        protected void generate(IndentedWriter w, @NotNull JsArtifactGenerator ag, LastLine lastLine) {
            comments(w);
            w.print(declaration(ag));
            if (isAbstract()) w.println(";");
            else {
                w.print(" {");

                if (inline && elements.size() == 1 && elements.get(0).length() == 1) {
                    // Single line
                    w.print(" ");
                    elements.get(0).generate(w, ag, NO_NEW_LINE);
                }
                else {
                    // Multiple lines
                    w.newLine();
                    w.indent();
                    elements.forEach(l -> l.generate(w, ag, NEW_LINE));
                    w.unIndent();
                }
                lastLine.print(w, " }");
            }
        }  // end method generate

        @Override
        @SuppressWarnings("Duplicates")
        String declaration(JsArtifactGenerator ag) {
            final StrBuilder result = new StrBuilder(super.declaration(ag));
            result.append('(');
            for (final Argument a : arguments)
                result.appendElement(a.declaration(ag), ", ");
            result.append(')');
            return result.toString();
        }

        @Override void name(StrBuilder result) {
            if (!isAnonymous()) super.name(result);
        }

        @Override void type(StrBuilder result) {
            result.appendElement(getType());
        }

        boolean isAnonymous() {
            return Modifier.isNative(modifiers);
        }

        boolean isAbstract() {
            return Modifier.isAbstract(modifiers);
        }

        private Argument addArgument(Argument a) {
            arguments.add(a);
            return a;
        }
    }  // end class Function

    public static class If extends Block<If> {
        private final JsElement<?> condition;
        private boolean            multiline = true;

        protected If(@NotNull JsArtifactGenerator ag, JsElement<?> condition) {
            super(ag, "if");
            this.condition = condition;
        }

        /** Mark if as inline. */
        public If inline() {
            multiline = false;
            return this;
        }

        @Override protected void generateAfter(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            if (multiline) super.generateAfter(w, ag, visit);
        }

        @Override protected void generateBefore(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.print(getName() + "(");
            condition.generate(w, ag, NO_NEW_LINE);
            w.print(") ");
            if (multiline) w.println("{");
        }
    }

    public static class InvocationStatement extends JsElement<InvocationStatement> {
        @NotNull private final List<JsElement<?>> arguments;

        @NotNull private final JsElement<?> element;
        private boolean                     semicolon;
        private Target                      target;

        InvocationStatement(@NotNull JsElement<?> element) {
            super("", "invocation");
            this.element = element;
            semicolon    = false;
            target       = null;
            arguments    = new ArrayList<>();
        }

        /** Adds an argument to invocation (treated as constant). */
        public InvocationStatement arg(@NotNull String constant) {
            return arg(new Constant(constant));
        }

        /** Adds an argument to invocation. */
        public InvocationStatement arg(@NotNull JsElement<?> arg) {
            arguments.add(arg);
            return this;
        }

        /** Append semicolon after invocation. */
        public InvocationStatement semicolon() {
            semicolon = true;
            return this;
        }

        /** Prepend target before invocation. */
        public Target target(@NotNull String t) {
            return target(new Constant(t));
        }

        /** Prepend target before invocation. */
        public Target target(@NotNull JsElement<?> t) {
            target = new Target(t);
            return target;
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            if (target != null) target.before(w, ag);

            w.print(element.getName() + "(");
            generateCollection(w, ag, arguments);
            visit.print(w, ")" + (semicolon ? ";" : ""));

            if (target != null) target.after(w);
        }

        public static class Target {
            @NotNull private final JsElement<?> element;
            private boolean                     newLine;

            private Target(@NotNull JsElement<?> element) {
                this.element = element;
            }

            /** Break line and indent target invocation. */
            public Target newLine() {
                newLine = true;
                return this;
            }

            private void after(IndentedWriter w) {
                if (newLine) w.unIndent();
            }

            private void before(IndentedWriter w, JsArtifactGenerator ag) {
                element.generate(w, ag, NO_NEW_LINE);
                if (newLine) {
                    w.newLine();
                    w.indent();
                }
                w.print(".");
            }
        }
    }  // end class InvocationStatement

    private static class InvokeStatement extends JsElement<DeclareStatement> {
        @NotNull private final Function function;

        InvokeStatement(@NotNull Function function) {
            super("", INVOKE);
            this.function = function;
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.print("(");
            function.generate(w, ag, NO_NEW_LINE);
            visit.print(w, ")()");
        }

        @Override int length() {
            return function.length();
        }
    }

    private static class LineBreak extends JsElement<LineBreak> {
        LineBreak() {
            super("", "");
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.newLine();
        }
    }

    public static class LineStatement extends JsElement<LineStatement> {
        LineStatement(@NotNull String statement) {
            super(statement, "");
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.println(getName());
        }
    }

    public static class Object extends JsItemGenerator<Function> {
        @NotNull final List<Member> members;

        protected Object(@NotNull JsArtifactGenerator ag) {
            super(ag, "", OBJECT);
            members = new ArrayList<>();
        }

        /** Adds a member to the object. */
        public Member member(@NotNull String name, @NotNull String constant) {
            return member(name, new Constant(constant));
        }

        /** Adds a member to the object. */
        public Member member(@NotNull String name, @NotNull JsElement<?> member) {
            final Member m = new Member(name, member);
            members.add(m);
            return m;
        }

        @Override protected void generate(IndentedWriter w, @NotNull JsArtifactGenerator ag, LastLine visit) {
            w.println("{");
            w.indent();
            int current = 0;
            for (final Member member : members) {
                member.comments(w);
                w.print(member.getName() + " : ");
                member.getValue().generate(w, ag, visit);
                w.println(++current < members.size() ? "," : "");
            }
            w.unIndent();
            visit.print(w, "}");
        }

        @Override int length() {
            return members.size();
        }

        public class Member extends JsElement<Member> {
            @NotNull private final JsElement<?> value;

            Member(@NotNull String nm, @NotNull JsElement<?> value) {
                super(nm, MEMBER);
                this.value = value;
            }

            @Override public Member asPrivate() {
                Object.this.asPrivate();
                return this;
            }

            @Override public Member asPublic() {
                Object.this.asPublic();
                return this;
            }

            /** Adds a member to parent object. */
            public Member member(@NotNull String name, @NotNull JsElement<?> member) {
                return Object.this.member(name, member);
            }

            /** Adds a member to parent object. */
            public Member member(@NotNull String name, @NotNull String constant) {
                return Object.this.member(name, constant);
            }

            @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
                Object.this.generate(w, ag, visit);
            }

            @NotNull private JsElement<?> getValue() {
                return value;
            }
        }
    }  // end class Object

    static class ReturnStatement extends JsElement<ReturnStatement> {
        @NotNull private final JsElement<?> statement;

        ReturnStatement(@NotNull JsElement<?> statement) {
            super(RETURN, "");
            this.statement = statement;
        }

        @Override void generate(IndentedWriter w, JsArtifactGenerator ag, LastLine visit) {
            w.print(getName() + " ");
            statement.generate(w, ag, NO_NEW_LINE);
            visit.print(w, ";");
        }

        @Override int length() {
            return statement.length();
        }
    }
}  // end class JsElement
