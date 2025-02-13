
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.io.File;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.util.Conversions;

import static java.lang.reflect.Modifier.STATIC;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Constants.STRING;
import static tekgenesis.common.core.Constants.VALUE_OF;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.util.JavaReservedWords.*;

/**
 * The base class for generated Items (Class, Enum & Interface) .
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "WeakerAccess", "UnusedReturnValue", "OverlyComplexClass" })
public abstract class JavaItemGenerator<T extends JavaItemGenerator<T>> extends JavaElement<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull String superclass;

    @NotNull private final JavaArtifactGenerator      artifactGenerator;
    @NotNull private final List<Constructor>          constructors;
    @NotNull private final Map<String, Field>         fields;
    @NotNull private final List<JavaItemGenerator<?>> inners;
    @NotNull private final List<String>               interfaces;
    @NotNull private final List<Method>               methods;
    @NotNull private final List<String>               staticBlock;

    //~ Constructors .................................................................................................................................

    /** Constructor for top level items. */
    JavaItemGenerator(JavaCodeGenerator cg, String name, @NotNull String type) {
        this(cg.newArtifactGenerator(name), name, type);
    }

    /** Constructor for inner items. */
    JavaItemGenerator(JavaItemGenerator<?> parent, String name, String type) {
        this(parent.artifactGenerator, name, type);
    }

    private JavaItemGenerator(@NotNull JavaArtifactGenerator ag, @NotNull String nm, @NotNull String type) {
        super(nm, type);
        artifactGenerator = ag;
        superclass        = "";
        inners            = new ArrayList<>();
        interfaces        = new ArrayList<>();
        fields            = new LinkedHashMap<>();
        constructors      = new ArrayList<>();
        methods           = new ArrayList<>();
        staticBlock       = new ArrayList<>();
        addIgnored(extractName(nm));
    }

    //~ Methods ......................................................................................................................................

    /** Add the item to ignored import list. */
    public void addIgnored(String simpleName) {
        artifactGenerator.addIgnored(simpleName);
    }

    /** Builds a <code>cast</code> expression extracting the import from the fqn type. */
    @SuppressWarnings("WeakerAccess")
    public String cast(String fqn, String expr) {
        return "(" + extractImport(fqn) + ") " + expr;
    }

    /** Builds a <code>cast</code> expression extracting the import from the specified class. */
    public String cast(Class<?> clazz, String expr) {
        return cast(extractImport(clazz), expr);
    }

    /** Return the '.class' object for the specified class. */
    public String classOf(String clazz) {
        return extractImport(clazz) + "." + CLASS;
    }

    /** Creates a {@link Constructor}. */
    public Constructor constructor() {
        final Constructor constructor = new Constructor(this, getName());
        constructors.add(constructor);
        return constructor;
    }

    /** Process the class to extract the package and add it to the import list. */
    public String extractImport(Class<?> clazz) {
        return artifactGenerator.extractImport(clazz);
    }

    /** Process the type to extract the package and add it to the import list. */
    public final String extractImport(String fqn) {
        return artifactGenerator.extractImport(fqn);
    }

    /** Process the method/field to extract the name and add it to the static import list. */
    public String extractStaticImport(QName fqn) {
        return artifactGenerator.extractStaticImport(fqn).getFullName();
    }

    /** Process the method/field to extract the name and add it to the static import list. */
    public String extractStaticImport(@NotNull final Class<?> c, @NotNull String reference) {
        return artifactGenerator.extractStaticImport(createQName(c).append(reference)).getFullName();
    }

    /** Creates a {@link Field}. */
    public Field field(@NotNull String fieldName, @NotNull String fieldType) {
        return field(fieldName, fieldType, "");
    }

    /** Creates a {@link Field}. */
    public Field field(@NotNull String fieldName, @NotNull Class<?> fieldType) {
        return field(fieldName, extractImport(fieldType));
    }

    /** Creates a {@link Field}. */
    public Field field(@NotNull String fieldName, @NotNull Class<?> fieldType, @NotNull String initialValue) {
        return field(fieldName, extractImport(fieldType), initialValue);
    }

    /** Creates a {@link Field} with initial value. */
    public Field field(@NotNull String fieldName, String fieldType, @NotNull String initialValue) {
        if (fieldType == null) throw new IllegalStateException("Null type for field: " + fieldName + " " + getName());
        final Field f = new Field(fieldName, extractImport(fieldType), initialValue);
        fields.put(fieldName, f);
        return f;
    }

    /** Generate the source code. */
    public void generate() {
        artifactGenerator.generate(this);
    }

    /**
     * Generate an element.
     *
     * @param  w   the writer
     * @param  ag  the Artifact Generator
     */
    public void generateElement(IndentedWriter w, @NotNull JavaArtifactGenerator ag) {
        populate();
        generateHeader(w);
        generateBody(w, ag);
        generateFooter(w);
    }
    /** Generate the source code if the file is not present. */
    @SuppressWarnings("UnusedReturnValue")
    public final boolean generateIfAbsent() {
        final boolean generate = !getTargetFile().exists();
        if (generate) generate();
        return generate;
    }

    /** Generate the source code if the file does not exists or is older than the specified file. */
    public final boolean generateIfOlder(File source) {
        final File    target   = getTargetFile();
        final boolean generate = !target.exists() || target.lastModified() < source.lastModified();
        if (generate) generate();
        return generate;
    }

    /** Creates a generic type, extract imports. */
    public String generic(@NotNull Class<?> clazz, @NotNull Class<?>... args) {
        return generic(clazz, fromArray(args));
    }

    /** Creates a generic type, extract imports. */
    public String generic(@NotNull Class<?> clazz, @NotNull String... args) {
        return generic(extractImport(clazz), args);
    }

    /** Creates a generic type, extract imports. */
    public String generic(@NotNull String clazz, @NotNull String... args) {
        return generic(extractImport(clazz), fromArray(args));
    }

    /** Creates a generic type, extract imports. */
    public String generic(@NotNull String clazz, @NotNull Iterable<String> args) {
        return extractImport(clazz) +  //
               Colls.map(args, this::extractImport).mkString("<", ",", ">");
    }

    /** Creates a {@link Method}. */
    public Method getter(@NotNull String propertyName, @NotNull String methodType) {
        return method(Strings.getterName(propertyName, methodType), extractImport(methodType));
    }

    /** Creates a {@link Method}. */
    public Method getter(@NotNull String propertyName, @NotNull Class<?> methodType) {
        return getter(propertyName, extractImport(methodType));
    }

    /** Adds a new class to be generated. */
    public ClassGenerator innerClass(@NotNull String nm) {
        return addInner(new ClassGenerator(this, nm));
    }

    /** Adds a new enum to be generated. */
    public EnumGenerator innerEnum(@NotNull String nm, @NotNull String... enumConstants) {
        return addInner(new EnumGenerator(this, nm, fromArray(enumConstants)));
    }
    /** Adds a new enum to be generated. */
    @SuppressWarnings("UnusedReturnValue")
    public EnumGenerator innerEnum(@NotNull String nm, @NotNull Seq<String> enumConstants) {
        return addInner(new EnumGenerator(this, nm, enumConstants));
    }

    /** Adds a new enum to be generated. */
    @SuppressWarnings("UnusedReturnValue")
    public EnumGenerator innerEnum(@NotNull String nm, @NotNull Seq<String> enumConstants, String... implementing) {
        return addInner(new EnumGenerator(this, nm, enumConstants).withInterfaces(implementing));
    }

    /** Process the type to insert the corresponding package if exists in the import list. */
    public final String insertImport(String simpleName) {
        return artifactGenerator.insertImport(simpleName);
    }

    /** Invoke a Method. */
    public String invoke(@NotNull String method) {
        return invoke("", method);
    }

    /** Invoke a Method. */
    public String invoke(@NotNull String target, @NotNull String method, String... args) {
        return invoke(target, method, fromArray(args));
    }

    /** Invoke a Method. */
    public String invoke(String target, String method, Iterable<String> args) {
        return invoke(target, method, false, args);
    }

    /** Invoke a Method breaking line. */
    public String invoke(@NotNull String target, @NotNull String method, boolean breakLine, String... args) {
        return invoke(target, method, breakLine, fromArray(args));
    }

    /** Invoke a Method breaking line. */
    public String invoke(String target, String method, boolean breakLine, Iterable<String> args) {
        return target + (target.isEmpty() ? "" : breakLine ? "\n." : ".") + method + Colls.mkString(args, "(", ", ", ")");
    }

    /** Invoke the method to convert the value of the specified type to an String. */
    public String invokeConvertFromString(String value, String typeName) {
        final String t = typeName.startsWith(JAVA_LANG) ? typeName.substring(JAVA_LANG.length() + 1) : typeName;
        if (STRING.equals(t)) return value;
        final String m = Conversions.getConversionFor(t);
        return m.isEmpty() ? invokeStatic(t, VALUE_OF, value) : invokeStatic(Conversions.class, m, value);
    }

    /** Generate listOf(a, b, c....). */
    public String invokeListOf(String... elements) {
        return invokeListOf(fromArray(elements));
    }
    /** Generate listOf(a, b, c....). */
    public String invokeListOf(Seq<String> elements) {
        return invokeStatic(elements.isEmpty() ? EMPTY_LIST : LIST_OF, elements);
    }

    /** Invoke an Static Method, try to extract static import. */
    public String invokeStatic(QName fqn, String... args) {
        return invokeStatic(fqn, fromArray(args));
    }
    /** Invoke an Static Method, try to extract static import. */
    public String invokeStatic(QName fqn, Iterable<String> args) {
        final QName name = artifactGenerator.extractStaticImport(fqn);

        final String q = name.getQualification().isEmpty() ? "" : extractImport(fqn.getQualification());
        return invoke(q, name.getName(), args);
    }

    /** Invoke an Static Method. */
    public String invokeStatic(Class<?> c, String method, String... args) {
        return invokeStatic(extractImport(c), method, fromArray(args));
    }

    /** Invoke an Static Method. */
    public String invokeStatic(String c, String method, String... args) {
        return invokeStatic(c, method, fromArray(args));
    }

    /** Invoke an Static Method. */
    public String invokeStatic(String c, String method, Iterable<String> list) {
        return invoke(extractImport(c), method, list);
    }

    /** Invoke an Static Method. */
    public String invokeStatic(Class<?> c, String method, Iterable<String> list) {
        return invoke(extractImport(c), method, list);
    }

    /** Invoke String.valueOf method with arg argument. */
    public final String invokeStringValue(final String arg) {
        return invoke(STRING, VALUE_OF, arg);
    }

    /** Creates a void {@link Method}. */
    public Method method(@NotNull String methodName) {
        return method(methodName, VOID);
    }

    /** Creates a {@link Method}. */
    public Method method(@NotNull String methodName, @NotNull String methodType) {
        final Method f = new Method(this, methodName, methodType);
        methods.add(f);
        return f;
    }
    /** Creates a {@link Method}. */
    public Method method(@NotNull String methodName, @NotNull Class<?> methodType) {
        return method(methodName, extractImport(methodType));
    }

    /** Invoke new. */
    public String new_(Class<?> clazz, String... args) {
        return new_(clazz.getCanonicalName(), args);
    }
    /** Invoke new. */
    public String new_(String className, String... args) {
        return new_(className, fromArray(args));
    }

    /** Invoke new. */
    public String new_(String className, Iterable<String> args) {
        return NEW + " " + extractImport(className) + filter(args, Objects::nonNull).mkString("(", ", ", ")");
    }

    /** Invoke new. */
    public String newGeneric(String className, String... args) {
        return newGeneric(className, fromArray(args));
    }

    /** Invoke new. */
    public String newGeneric(String className, Iterable<String> args) {
        return new_(className + "<>", args);
    }

    /** Creates a {@link Field} with associated getter and setter. */
    public Field property(@NotNull String fieldName, @NotNull String fieldType) {
        return field(fieldName, fieldType).withGetter().withSetter();
    }

    /** Creates a {@link Field} with associated getter and setter. */
    public Field property(@NotNull String fieldName, @NotNull Class<?> fieldType) {
        return property(fieldName, extractImport(fieldType));
    }

    /** Creates a final {@link Field} with an associated getter. */
    public Field readOnlyProperty(@NotNull String fieldName, @NotNull String fieldType) {
        return field(fieldName, fieldType).asFinal().withGetter();
    }

    /** Creates a final {@link Field} with an associated getter. */
    public Field readOnlyProperty(@NotNull String fieldName, @NotNull Class<?> fieldType) {
        return readOnlyProperty(fieldName, extractImport(fieldType));
    }

    /** Reference a Method (Lambda). */
    public String refMethod(String clazz, String methodName) {
        return extractImport(clazz) + "::" + methodName;
    }

    /** Reference a static Field. */
    public String refStatic(String clazz, String fieldName) {
        return extractImport(clazz) + "." + fieldName;
    }
    /** Reference a static Field. */
    public String refStatic(Class<?> clazz, String fieldName) {
        return extractImport(clazz) + "." + fieldName;
    }
    /** Reference a static Field. */
    public String refStaticImport(Class<?> clazz, String fieldName) {
        return extractStaticImport(createQName(clazz.getTypeName(), fieldName));
    }

    /** remove an specific import. */
    public void removeImport(String fqn) {
        artifactGenerator.removeImport(fqn);
    }

    /** Method invoke to EnumSet. */
    public String seqToEnumSet(@NotNull final String invoke, String className) {
        final String arg = invoke(invoke, "toList");
        return invokeStatic(Enumerations.class, "enumSet", classOf(className), arg);
    }

    /** Creates a {@link Method}. */
    public Method setter(@NotNull String propertyName) {
        return setter(propertyName, VOID);
    }

    /** Creates a {@link Method}. */
    public Method setter(@NotNull String propertyName, @NotNull String methodType) {
        return method((Strings.setterName(propertyName)), methodType);
    }

    /** Extracts the import and appends the "? extends " to the type */
    public final String wildcardExtends(String fqn) {
        return WILDCARD_EXTENDS + extractImport(fqn);
    }

    /** Extracts the import and appends the "? extends " to the type */
    public final String wildcardExtends(Class<?> clazz) {
        return WILDCARD_EXTENDS + extractImport(clazz);
    }

    /** Extracts the import and appends the "? super " to the type */
    public final String wildcardSuper(String fqn) {
        return WILDCARD_SUPER + extractImport(fqn);
    }

    /** Extracts the import and appends the "? super " to the type */
    public final String wildcardSuper(Class<?> clazz) {
        return WILDCARD_SUPER + extractImport(clazz);
    }

    /**
     * Specifies the interfaces implemented by this item (Or the interfaces extended by this
     * interface).
     */
    public T withInterfaces(String... implementing) {
        for (final String s : implementing)
            interfaces.add(extractImport(s));
        return Predefined.cast(this);
    }

    /**
     * Specifies the interfaces implemented by this item (Or the interfaces extended by this
     * interface).
     */
    public T withInterfaces(Iterable<String> implementing) {
        for (final String s : implementing)
            interfaces.add(extractImport(s));
        return Predefined.cast(this);
    }

    /**
     * Specifies the interfaces implemented by this item (Or the interfaces extended by this
     * interface).
     */
    public T withInterfaces(Class<?>... implementing) {
        return withInterfaces(true, implementing);
    }

    /** Insert the current package. Used for inner classes qualification. */
    public final String withPackage(String simpleName) {
        return artifactGenerator.withPackage(simpleName);
    }

    /** Return a field for the given name. */
    public Field getField(@NotNull String fieldName) {
        return fields.get(fieldName);
    }

    /** Returns the package + element name. */
    @NotNull public String getFullName() {
        return artifactGenerator.getPackageName() + "." + getName();
    }

    /** Returns the package Name. */
    @NotNull public String getPackageName() {
        return artifactGenerator.getPackageName();
    }

    /** return the target File of the Item. */
    public File getTargetFile() {
        return artifactGenerator.getTargetFile();
    }

    /** Add an Inner element to this class. */
    protected <E extends JavaItemGenerator<?>> E addInner(E innerElement) {
        artifactGenerator.addIgnored(innerElement.getName());
        inners.add(innerElement);
        return innerElement;
    }

    /** Add a static block statement. */
    protected void addStaticBlockStatement(String statement) {
        staticBlock.add(statement + ";");
    }

    protected boolean hasInner(@NotNull final String innerClassName) {
        return exists(inners, inner -> inner != null && inner.getName().equals(innerClassName));
    }

    protected boolean hasMethod(@NotNull final String methodName) {
        return exists(methods, method -> method != null && method.getName().equals(methodName));
    }

    /** Text to link in javadoc. */
    protected String link(String fqn) {
        return link(fqn, null);
    }

    /** Text to link in javadoc. */
    protected String link(@NotNull final String fqn, @Nullable final String text) {
        return "{@link " + extractImport(fqn) + (isNotEmpty(text) ? " " + text : "") + "}";
    }

    /** Populate the generator. */
    protected void populate() {}

    protected T withInterfaces(boolean extractImports, Class<?>... implementing) {
        for (final Class<?> s : implementing)
            interfaces.add(extractImports ? extractImport(s) : s.getCanonicalName());
        return Predefined.cast(this);
    }

    /** Return the Fields of the Item. */
    @NotNull protected ImmutableCollection<Field> getFields() {
        return immutable(fields.values());
    }

    @Override void addNotNull(JavaArtifactGenerator ag, StrBuilder result) {
        // Do nothing for classes
    }

    void generateBody(@NotNull IndentedWriter w, @NotNull JavaArtifactGenerator ag) {
        boolean hasGetters        = false;
        boolean hasInstanceFields = false;
        boolean hasSetters        = false;
        boolean hasStaticFields   = false;

        for (final Field f : fields.values()) {
            if (isNotEmpty(f.getter)) hasGetters = true;
            if (isNotEmpty(f.setter)) hasSetters = true;
            if (f.isStatic()) hasStaticFields = true;
            else hasInstanceFields = true;
        }
        w.newLine();

        if (hasInstanceFields) generateFields(w, ag, false);

        generateConstructors(w, ag);

        if (hasGetters) generateGetters(w, ag);

        if (hasSetters) generateSetters(w, ag);

        generateMethods(w, ag);

        if (hasStaticFields) generateFields(w, ag, true);

        generateStaticBlock(w);

        generateInnerClasses(w, ag);
    }  // end method generateBody

    boolean isInterface() {
        return false;
    }

    private void generateConstructors(IndentedWriter w, JavaArtifactGenerator ag) {
        if (!constructors.isEmpty()) {
            w.println(CONSTRUCTORS_COMMENT);
            w.newLine();
            for (final Constructor c : constructors)
                c.generate(w, ag);
        }
    }

    private void generateFields(IndentedWriter w, JavaArtifactGenerator ag, boolean aStatic) {
        w.println(FIELDS_COMMENT);
        w.newLine();
        for (final Field f : fields.values()) {
            if (f.isStatic() == aStatic) f.generate(w, ag);
        }
        w.newLine();
    }

    private void generateFooter(IndentedWriter w) {
        w.unIndent().println("}");
    }

    private void generateGetters(IndentedWriter w, JavaArtifactGenerator ag) {
        w.println(GETTERS_COMMENT);
        w.newLine();
        for (final Field f : fields.values()) {
            if (isNotEmpty(f.getter))
                new Method(this, f).addModifier(f.isStatic() ? STATIC : 0).addModifier(f.accessorsModifiers)  //
                .withGetterComments(f.getName()).return_(f.getName())                                         //
                .generate(w, ag);
        }
    }

    private void generateHeader(IndentedWriter w) {
        w.newLine();  // leave space
        comments(w);
        printAnnotations(w);

        w.println(baseDeclaration(new StrBuilder().startCollection(" ")));

        if (isNotEmpty(superclass)) {
            w.indent();
            w.print(EXTENDS);
            w.print(" ");
            w.println(superclass);
            w.unIndent();
        }

        if (isNotEmpty(interfaces)) {
            w.indent().print(isInterface() ? EXTENDS : IMPLEMENTS);
            w.print(" ");
            for (int i = 0; i < interfaces.size(); i++) {
                if (i > 0) w.print(", ");
                w.print(interfaces.get(i));
            }
            w.newLine();
            w.unIndent();
        }
        w.println("{");
        w.indent();
    }

    private void generateInnerClasses(IndentedWriter w, JavaArtifactGenerator ag) {
        if (!inners.isEmpty()) {
            w.println(INNER_CLASSES_COMMENT);
            for (final JavaItemGenerator<?> g : inners)
                g.generateElement(w, ag);
        }
    }

    private void generateMethods(IndentedWriter w, JavaArtifactGenerator ag) {
        if (!methods.isEmpty()) {
            w.println(METHODS_COMMENT);
            w.newLine();
            for (final Method m : methods)
                m.generate(w, ag);
        }
    }

    private void generateSetters(IndentedWriter w, JavaArtifactGenerator ag) {
        w.println(SETTERS_COMMENT);
        w.newLine();
        for (final Field f : fields.values()) {
            if (isNotEmpty(f.setter)) {
                final Method m = new Method(this, f.setter, VOID);
                if (f.isStatic()) m.asStatic();
                m.arg(f);
                m.withSetterComments(f.getName());
                m.generate(w, ag);
            }
        }
    }

    private void generateStaticBlock(IndentedWriter w) {
        if (!staticBlock.isEmpty()) {
            w.println("static {");
            w.indent();
            for (final String line : staticBlock)
                w.println(line);
            w.unIndent();
            w.println("}");
            w.newLine();
        }
    }

    /** Creates a generic type, extract imports. */
    private String generic(@NotNull Class<?> clazz, @NotNull Iterable<Class<?>> args) {
        return extractImport(clazz) + Colls.map(args, this::extractImport).mkString("<", ",", ">");
    }

    private void printAnnotations(IndentedWriter w) {
        final StrBuilder result = new StrBuilder().startCollection("\n");
        annotations(result);
        if (!result.isEmpty()) w.println(result.toString());
    }

    //~ Static Fields ................................................................................................................................

    private static final QName LIST_OF    = createQName(Colls.class).append("listOf");
    private static final QName EMPTY_LIST = createQName(Colls.class).append("emptyList");

    public static final String WILDCARD         = "?";
    public static final String WILDCARD_SUPER   = WILDCARD + " super ";
    public static final String WILDCARD_EXTENDS = WILDCARD + " extends ";
}  // end class JavaItemGenerator
