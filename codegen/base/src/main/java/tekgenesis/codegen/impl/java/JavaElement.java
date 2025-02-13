
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.Element;
import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.util.JavaReservedWords;

import static java.lang.Math.max;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Arrays.asList;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.exists;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Constants.TO_BE_IMPLEMENTED;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.util.JavaReservedWords.*;
import static tekgenesis.common.util.Primitives.isPrimitive;
import static tekgenesis.common.util.Primitives.primitiveFor;

/**
 * Elements containing a name and modifiers.
 */
@SuppressWarnings({ "UnusedReturnValue", "WeakerAccess", "ClassWithTooManyMethods" })
public class JavaElement<T extends JavaElement<T>> extends Element<T> {

    //~ Instance Fields ..............................................................................................................................

    int modifiers;

    @NotNull private final List<String>  annotations;
    @NotNull private final List<String>  generics = new ArrayList<>();
    private Boolean                      notNull;

    //~ Constructors .................................................................................................................................

    JavaElement(@NotNull String nm, @NotNull String t) {
        super(nm, t);
        modifiers   = PUBLIC;
        annotations = new LinkedList<>();

        notNull = false;
    }

    //~ Methods ......................................................................................................................................

    /** Add generic var to. */
    public T addGenericVar(String generic) {
        generics.add(generic);
        return cast(this);
    }

    /** Add the given modifier {@link Modifier } to the element. */
    public T addModifier(int m) {
        modifiers = m | ((m & VISIBILITY) != 0 ? modifiers & ~VISIBILITY : modifiers);
        return cast(this);
    }
    /** Makes the item <code>abstract</code>. */
    public T asAbstract() {
        return addModifier(Modifier.ABSTRACT);
    }

    /** Makes the item <code>final</code>. */
    public T asFinal() {
        return addModifier(Modifier.FINAL);
    }

    /** Makes the item <code>package private</code>. */
    public T asPackagePrivate() {
        modifiers &= ~VISIBILITY;
        return cast(this);
    }

    /** Makes the item <code>private</code>. */
    public T asPrivate() {
        return addModifier(PRIVATE);
    }

    /** Makes the item <code>protected</code>. */
    public T asProtected() {
        return addModifier(PROTECTED);
    }

    /** Makes the item <code>public</code>. */
    public T asPublic() {
        return addModifier(PUBLIC);
    }
    /** Makes the item <code>static</code>. */
    public T asStatic() {
        return addModifier(STATIC);
    }

    /** Makes the item <code>synchronized</code>. */
    public T asSynchronized() {
        return addModifier(SYNCHRONIZED);
    }

    /** Makes the item <code>transient</code>. */
    @SuppressWarnings("WeakerAccess")
    public T asTransient() {
        return addModifier(TRANSIENT);
    }

    /** Mark the element as not null. */
    public T boxedNotNull() {
        notNull = true;
        return cast(this);
    }

    /** Mark the element as deprecated. */
    public T deprecated() {
        return withAnnotation(DEPRECATED_ANNOTATION);
    }

    /** Mark the element as deprecated. */
    public T deprecated(String replacement) {
        withComments(format("@deprecated Replaced by {@link %s}", replacement));
        return deprecated();
    }

    /** Mark the element as not null. */
    public T notNull() {
        if (!isPrimitive(getType())) {
            final String p = primitiveFor(getType());
            if (!p.isEmpty()) setType(p);
        }
        return boxedNotNull();
    }

    /** Mark the methods as one that overrides other. */
    public T override() {
        return withAnnotation(OVERRIDE);
    }

    /** Mark the element as required. */
    public T required(boolean b) {
        if (b) notNull();
        return cast(this);
    }

    /** Adds an annotation to suppress warnings. */
    public T suppressWarnings(String... ws) {
        return ws.length == 1 ? withAnnotation(SUPPRESS_WARNINGS_ANNOTATION, ws[0]) : withAnnotation(SUPPRESS_WARNINGS_ANNOTATION, fromArray(ws));
    }

    /** Builds a <code>this</code> reference. */
    public String THIS(String field) {
        return THIS + "." + field;
    }

    /** Adds an annotation to the item. */
    public T withAnnotation(@NotNull String annotation) {
        annotations.add(annotation);
        return cast(this);
    }

    /** Adds an annotation with arguments to the item. */
    public T withAnnotation(@NotNull String annotation, Seq<String> args) {
        return withAnnotation(annotation + "(" + args.mkString("{", ", ", "}") + ")");
    }

    /** Adds an annotation with arguments to the item. */
    public T withAnnotation(@NotNull String annotation, String arg) {
        return withAnnotation(annotation + "(" + arg + ")");
    }

    /** Do not generate nullable annotation. */
    public T withoutNullable() {
        notNull = null;
        return cast(this);
    }

    /** Return the annotations. */
    @NotNull public ImmutableList<String> getAnnotations() {
        return immutable(annotations);
    }

    /** Returns true if the element is static. */
    public boolean isStatic() {
        return (modifiers & STATIC) != 0;
    }

    /** Returns true if the field is not null. */
    public boolean isNotNull() {
        return notNull;
    }

    @Nullable protected List<String> body() {
        return null;
    }

    protected boolean supportsAnnotation(final String a) {
        return true;
    }

    void addNotNull(JavaArtifactGenerator ag, StrBuilder result) {
        if (notNull != null && !isPrimitive(getType())) ag.addNotNullAnnotation(result, notNull);
    }

    void annotations(StrBuilder result) {
        for (final String a : annotations) {
            if (supportsAnnotation(a)) result.appendElement("@").append(a);
        }
    }

    String baseDeclaration(StrBuilder result) {
        modifiers(result);
        type(result);
        name(result);
        generic(result);
        return result.toString();
    }

    String declaration(JavaArtifactGenerator ag) {
        final StrBuilder result = new StrBuilder().startCollection(" ");
        annotations(result);
        addNotNull(ag, result);
        return baseDeclaration(result);
    }

    void extractImports(final JavaItemGenerator<?> g) {
        setType(g.extractImport(getType()));
    }

    T removeModifier(int m) {
        modifiers = modifiers & ~m;
        return cast(this);
    }

    void type(StrBuilder result) {
        if (!getType().isEmpty()) result.appendElement(getType());
    }

    private void generic(StrBuilder result) {
        if (!generics.isEmpty()) result.append("<").append(immutable(generics).mkString(",")).append(">");
    }

    private void modifiers(StrBuilder result) {
        final String mods = Modifier.toString(modifiers);
        if (!mods.isEmpty()) result.appendElement(mods);
    }

    private void name(StrBuilder result) {
        result.appendElement(getName());
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_BODY_LENGTH = 60;

    private static final int VISIBILITY = (PUBLIC | PROTECTED | PRIVATE);

    //~ Inner Classes ................................................................................................................................

    public static class Argument extends JavaElement<Argument> {
        private boolean superArg;

        Argument(@NotNull Field f) {
            this(f.getName(), f.getType());
        }

        /** Create an Argument. */
        public Argument(@NotNull String nm, @NotNull String t) {
            super(nm, t);
            modifiers &= ~VISIBILITY;
        }
        Argument(@NotNull String nm, @NotNull Class<?> t) {
            this(nm, t.getCanonicalName());
        }

        /** The argument must be passed to the super constructor. */
        public Argument superArg() {
            superArg = true;
            return this;
        }
    }

    public static class Constructor extends MethodBase<Constructor> {
        Constructor(JavaItemGenerator<?> container, String name) {
            super(container, name, "");
        }

        /** Invoke the super constructor. */
        public Constructor invokeSuper(Iterable<String> superArgs) {
            bodyLines.add(0, SUPER + Colls.mkString(superArgs, "(", ",", ");"));
            return this;
        }

        /** Invoke the super constructor. */
        public Constructor invokeSuper(String... superArgs) {
            return invokeSuper(fromArray(superArgs));
        }

        @Override void addNotNull(JavaArtifactGenerator ag, StrBuilder result) {
            // Do nothing
        }

        @Override void generate(IndentedWriter w, JavaArtifactGenerator ag) {
            final List<String> superArgs = new LinkedList<>();
            for (final Argument argument : arguments) {
                if (argument.superArg) superArgs.add(argument.getName());
            }
            if (!superArgs.isEmpty()) invokeSuper(superArgs);
            super.generate(w, ag);
        }
    }

    public static class Field extends JavaElement<Field> {
        int            accessorsModifiers;
        String         getter;
        String         setter;
        private String initialValue;

        Field(@NotNull String nm, @NotNull String t) {
            this(nm, t, "");
            asPrivate();
        }
        Field(@NotNull String nm, @NotNull Class<?> t) {
            this(nm, t.getCanonicalName());
        }

        /** Create a Field. */
        public Field(String nm, String t, String init) {
            super(nm, t);
            asPrivate();
            getter             = "";
            setter             = "";
            initialValue       = init;
            accessorsModifiers = PUBLIC;
        }

        /** Generate a getter for the field. */
        public Field withGetter() {
            return withGetter(getterName(getName(), getType()), Modifier.PUBLIC);
        }

        /** Generate a getter for the field. */
        public Field withGetter(int mods) {
            return withGetter(getterName(getName(), getType()), mods);
        }

        /** Generate a getter for the field. */
        public Field withGetter(final String nm) {
            return withGetter(nm, Modifier.PUBLIC);
        }

        /** Generate a getter with the specified name for the field. */
        public Field withGetter(final String nm, int accessorsMods) {
            getter             = nm;
            accessorsModifiers = accessorsMods;
            return this;
        }

        /** Generate a setter for the field. */
        public Field withSetter() {
            return withSetter(setterName(getName()));
        }

        /** Sets the initial value of the field. */
        public Field withValue(@NotNull String s) {
            initialValue = s;
            return this;
        }

        /** Sets the initial value of the field. */
        public Field withValue(@NotNull Object o) {
            return withValue(String.valueOf(o));
        }

        @Override protected boolean supportsAnnotation(final String a) {
            return !a.equals(OVERRIDE);
        }

        void generate(IndentedWriter w, JavaArtifactGenerator ag) {
            comments(w);
            w.print(declaration(ag));
            if (isNotEmpty(initialValue)) {
                w.print(" = ");
                w.print(initialValue);
            }
            w.println(";");
        }

        private Field withSetter(String nm) {
            setter = nm;
            return this;
        }
    }  // end class Field

    public static class Method extends MethodBase<Method> {
        @NotNull private final List<String>  generics;
        @NotNull private final List<String>  throws_;

        Method(JavaItemGenerator<?> container, Field field) {
            this(container, field.getter, field.getType());

            for (final String a : field.getAnnotations())
                withAnnotation(a);
            if (field.isNotNull()) notNull();
        }

        Method(JavaItemGenerator<?> container, @NotNull String name, @NotNull String t) {
            super(container, name, t);
            generics = new LinkedList<>();
            throws_  = new LinkedList<>();
        }

        /** Overrides method in given class. */
        public Method overrideIn(@NotNull final ClassGenerator baseClass) {
            return overrideIn(baseClass, true);
        }

        /**
         * Overrides method in given class. Implement body only is specified, if not delegate to
         * caller.
         */
        public Method overrideIn(@NotNull final ClassGenerator baseClass, boolean implementBody) {
            final Method implementation = baseClass.method(getName(), container.insertImport(getType()));
            implementation.override();
            implementation.withGenerics(generics.toArray(new String[generics.size()]));
            implementation.withComments(getComments().toArray(new String[getComments().size()]));
            implementation.removeModifier(Modifier.ABSTRACT);
            if (isNotNull()) implementation.notNull();

            final StrBuilder args = new StrBuilder().startCollection(", ");

            for (final Argument argument : arguments) {
                final String   fqn = container.insertImport(argument.getType());
                final Argument arg = implementation.arg(argument.getName(), baseClass.extractImport(fqn));
                if (argument.isNotNull()) arg.notNull();
                args.appendElement(argument.getName());
            }

            if (implementBody) {
                if (!isAbstract()) {
                    final String superCall = SUPER + "." + getName() + "(" + args.build() + ")";
                    if (VOID.equals(getType())) implementation.statement(superCall);
                    else implementation.return_(superCall);
                }
                else implementation.throwNew(IllegalStateException.class, quoted(TO_BE_IMPLEMENTED));
            }

            return implementation;
        }

        /** Add throws clauses.* */
        public Method throws_(Class<?>... classes) {
            for (final Class<?> clazz : classes)
                throws_.add(container.extractImport(clazz));
            return this;
        }

        /** Add method type parameters to parametrize method generics. */
        public Method withGenerics(@NotNull final String... generic) {
            Collections.addAll(generics, generic);
            return this;
        }

        @Override String declaration(JavaArtifactGenerator ag) {
            final StringBuilder stringBuilder = new StringBuilder(super.declaration(ag));
            if (!throws_.isEmpty()) stringBuilder.append(" throws ").append(immutable(throws_).mkString(","));
            return stringBuilder.toString();
        }

        @Override void type(StrBuilder result) {
            if (!generics.isEmpty()) result.append(" <").append(generics, ",").append('>');
            super.type(result);
        }
    }  // end class Method

    protected static class MethodBase<T extends MethodBase<T>> extends JavaElement<T> {
        @NotNull final List<Argument>       arguments;
        @NotNull final List<String>         bodyLines;
        @NotNull final JavaItemGenerator<?> container;
        private int                         indentation     = 0;
        private boolean                     interfaceMethod = false;

        MethodBase(@NotNull JavaItemGenerator<?> container, @NotNull String name, @NotNull String type) {
            super(name, container.extractImport(type));
            this.container = container;
            arguments      = new LinkedList<>();
            bodyLines      = new LinkedList<>();
        }

        public Argument arg(Field f) {
            final String target = isStatic() ? RESULT + "." + f.getName() : THIS(f.getName());
            assign(target, f.getName());
            final Argument arg = arg(f.getName(), f.getType());
            if (f.isNotNull()) arg.notNull();
            return arg;
        }

        /** Adds an argument to the method. */
        public Argument arg(@NotNull Argument a) {
            return addArgument(a);
        }

        /** Adds an argument to the method. */
        public Argument arg(@NotNull String nm, @NotNull String t) {
            return addArgument(new Argument(nm, t));
        }

        /** Adds an argument to the method. */
        public Argument arg(@NotNull String nm, @NotNull Class<?> t) {
            return addArgument(new Argument(nm, t));
        }

        /** Adds field arguments. */
        public T args(@NotNull Iterable<Field> fields) {
            for (final Field field : fields)
                arg(field);
            return cast(this);
        }
        /** Adds field arguments. */
        public T args(@NotNull Field... fields) {
            return args(asList(fields));
        }

        /** Adds a set of arguments to the method. */
        public MethodBase<T> arguments(@NotNull Iterable<Argument> args) {
            for (final Argument a : args)
                addArgument(a);
            return this;
        }

        public T asInterfaceMethod() {
            interfaceMethod = true;
            return asPackage();
        }

        public T asPackage() {
            return removeModifier(VISIBILITY);
        }

        public T assign(String lv, String rv) {
            return statement(lv + " = " + rv);
        }

        public T blankLine() {
            bodyLines.add("");
            return cast(this);
        }

        public T declare(final Class<?> clazz, String nm, String value) {
            return declare(container.extractImport(clazz), nm, value);
        }

        public T declare(final String clazz, String nm, String value) {
            return assign(JavaReservedWords.FINAL + " " + container.extractImport(clazz) + " " + nm, value);  // for now only default final
        }

        public T declareNew(final Class<?> clazz, String nm, String... args) {
            return declare(container.extractImport(clazz), nm, container.new_(clazz, args));
        }

        public T declareNew(String className, String nm, String... args) {
            return declareNew(className, className, nm, args);
        }

        public T declareNew(String superClassName, String className, String nm, String... args) {
            return declare(container.extractImport(superClassName), nm, container.new_(className, args));
        }

        public T endBlock(@NotNull String endBlockCode) {
            unIndent();
            if (isNotEmpty(endBlockCode)) {
                final String indent = indentation > 0 ? nChars('\t', indentation) : "";
                bodyLines.add(indent + endBlockCode);
            }
            return cast(this);
        }
        public T endFor() {
            return endBlock("}");
        }
        public T endIf() {
            return endBlock("}");
        }
        public T endLambda() {
            return endBlock("});");
        }

        public boolean hasArgument(@NotNull final String argName) {
            return exists(arguments, argument -> argument != null && argument.getName().equals(argName));
        }

        public T ifInlineStatement(@NotNull String condition, @NotNull String statement) {
            statement("if (" + condition + ") " + statement);
            return cast(this);
        }

        public T indent() {
            indentation++;
            return cast(this);
        }

        public T return_(@NotNull CharSequence expr) {
            return statement(RETURN + " " + expr);
        }

        public T singleStatementIf(@NotNull String condition, String statement) {
            statement("if (" + condition + ") " + statement);
            return cast(this);
        }

        public T startBlock(@NotNull String startBlockCode) {
            final String indent = indentation > 0 ? nChars('\t', indentation) : "";
            bodyLines.add(indent + startBlockCode);
            indent();
            return cast(this);
        }
        public T startElse() {
            endIf();
            return startBlock("else {");
        }

        public T startElseIf(@NotNull String condition) {
            endIf();
            return startBlock("else if (" + condition + ") {");
        }

        public T startFor(@NotNull String forCondition) {
            return startBlock("for (" + forCondition + ") {");
        }

        public T startForEach(@NotNull String t, @NotNull String var, @NotNull String value) {
            return startBlock("for (" + JavaReservedWords.FINAL + " " + container.extractImport(t) + " " + var + " : " + value + ") {");
        }

        public T startIf(@NotNull String condition) {
            return startBlock("if (" + condition + ") {");
        }

        public T startLambda(@NotNull String invocation, String... args) {
            final int    i         = invocation.lastIndexOf(')');
            final String inv       = i == -1 ? invocation : invocation.substring(0, i);
            final String argString = args.length == 1 ? args[0] : ImmutableList.fromArray(args).mkString();
            return startBlock(inv + " " + argString + " -> {");
        }

        public T statement(@NotNull String statement) {
            return statement(statement, false);
        }

        public T statement(@NotNull String statement, boolean breakLine) {
            final String   indent = indentation > 0 ? nChars('\t', indentation) : "";
            final String[] split  = statement.split("\n");

            if (breakLine && split.length > 1) {
                bodyLines.add(indent + split[0]);
                for (final String s : fromArray(split).drop(1).take(split.length - 2))
                    bodyLines.add(indent + "\t" + s);
                bodyLines.add(indent + "\t" + split[split.length - 1] + ";");
            }
            else bodyLines.add(indent + statement + ";");

            return cast(this);
        }

        public T throwNew(@NotNull Class<?> clazz, String... args) {
            return statement(THROW + " " + container.new_(clazz, args));
        }

        public T throwNew(@NotNull String clazz, String... args) {
            return statement(THROW + " " + container.new_(clazz, args));
        }

        public T unIndent() {
            indentation = max(0, indentation - 1);
            return cast(this);
        }

        public T withBody(@NotNull String... lines) {
            Collections.addAll(bodyLines, lines);
            return cast(this);
        }

        /** Return the list of arguments. */
        @NotNull public List<Argument> getArguments() {
            return arguments;
        }

        @Nullable @Override protected List<String> body() {
            return isAbstract() ? null : bodyLines;
        }

        @Override
        @SuppressWarnings("Duplicates")
        String declaration(JavaArtifactGenerator ag) {
            final StrBuilder result = new StrBuilder(super.declaration(ag));
            result.append('(');
            for (final Argument a : arguments)
                result.appendElement(a.declaration(ag), ", ");
            result.append(')');
            return result.toString();
        }

        @Override void extractImports(final JavaItemGenerator<?> g) {
            super.extractImports(g);
            for (final Argument a : arguments)
                a.setType(g.extractImport(a.getType()));
        }

        void generate(IndentedWriter w, JavaArtifactGenerator ag) {
            comments(w);
            w.print(declaration(ag));
            if (isAbstract() || isInterfaceMethod()) w.println(";");
            else {
                w.print(" {");
                int n = bodyLines.size();
                if (n == 1 && bodyLines.get(0).length() > MAX_BODY_LENGTH) n = 2;

                switch (n) {
                case 0:
                    // empty method
                    w.println(" }");
                    break;
                case 1:
                    // single line
                    w.print(" ");
                    w.print(bodyLines.get(0));
                    w.println(" }");
                    break;
                default:
                    // multiple lines
                    w.newLine();
                    w.indent();
                    bodyLines.forEach(w::println);
                    w.unIndent();
                    w.println("}");
                }
            }
            w.newLine();  // leave space between methods
        }                 // end method generate

        boolean isInterfaceMethod() {
            return interfaceMethod;
        }

        boolean isAbstract() {
            return Modifier.isAbstract(modifiers);
        }

        private Argument addArgument(Argument a) {
            a.setType(container.extractImport(a.getType()));
            arguments.add(a);
            return a;
        }
    }  // end class MethodBase
}  // end class JavaElement
