
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Sha;

import static java.util.Arrays.asList;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.*;

/**
 * A Generator for classes.
 */
@SuppressWarnings({ "WeakerAccess", "UnusedReturnValue" })
public class ClassGenerator extends JavaItemGenerator<ClassGenerator> {

    //~ Instance Fields ..............................................................................................................................

    private boolean serializable = false;

    //~ Constructors .................................................................................................................................

    protected ClassGenerator(JavaCodeGenerator cg, String name) {
        super(cg, name, CLASS);
    }
    protected ClassGenerator(JavaItemGenerator<?> parent, String name) {
        super(parent, name, CLASS);
    }

    //~ Methods ......................................................................................................................................

    /** Sets the class as Serializable. */
    public ClassGenerator asSerializable() {
        return withInterfaces(Serializable.class).withSerialVersionUID();
    }

    /** The class will have an equals method, with the specified fields. */
    public ClassGenerator withEquals(@NotNull String... fields) {
        return withEquals(asList(fields));
    }
    /** The class will have an equals method, with the specified fields. */
    public ClassGenerator withEquals(List<String> fs) {
        final Method m = method(EQUALS, Constants.BOOLEAN).asPublic();
        m.arg(THAT, Object.class).withoutNullable();

        final String     that = "((" + getName() + ") " + THAT + ")";
        final StrBuilder b    = new StrBuilder();
        b.append(THIS + EQ + THAT + OR);
        b.startCollection(AND);
        b.appendElement(THAT + " " + INSTANCE_OF + " " + getName());
        if (!fs.isEmpty()) {
            if (fs.size() == 1) b.appendElement(invokeEqual(fs.get(0), that));
            else {
                b.appendElement("eq((" + getName() + ")" + THAT + ")");
                createEq(fs);
            }
        }
        m.return_(b.toString());
        return this;
    }  // end method withEquals

    /** The class will have a hashCode method, with the specified fields. */
    public ClassGenerator withHashCode(@NotNull String... fields) {
        return withHashCode(asList(fields));
    }

    /** The class will have a hashCode method, with the specified fields. */
    public ClassGenerator withHashCode(@NotNull Iterable<String> fields) {
        final Iterable<String> it = Colls.toList(fields).isEmpty() ? Colls.listOf("null") : fields;
        method(HASH_CODE, Integer.TYPE).asPublic().return_(invokeStatic(PREDEFINED_CLASS, HASH_CODE_ALL, it));
        return this;
    }

    /** Sets the class to include SerialVersionUID. */
    public ClassGenerator withSerialVersionUID() {
        serializable = true;
        return this;
    }

    /** Sets the class superclass. */
    public ClassGenerator withSuperclass(@NotNull String nm) {
        superclass = extractImport(nm);
        return this;
    }

    /** Sets the class superclass. */
    public ClassGenerator withSuperclass(@NotNull Class<?> c) {
        return withSuperclass(extractImport(c));
    }

    /** Generates a to String method using the getters for the specified Fields. */
    public ClassGenerator withToString(String start, String sep, String end, Iterable<Field> fields) {
        final List<String> fieldNames = new ArrayList<>();
        for (final Field field : fields)
            fieldNames.add(getterName(field.getName(), field.getType()));
        return withToString(fieldNames, start, sep, end);
    }

    protected void addLogger(String name) {
        withInterfaces(LoggableInstance.class);
        createLoggerField(this, name);
        implementLoggerMethod();
    }

    @Override protected void populate() {
        if (serializable) {
            try {
                field("serialVersionUID", "long").asFinal().asStatic().withValue(serialVersionUID());
            }
            catch (final IOException e) {
                logger.error(e);
            }
        }
    }

    protected List<String> splitTuple(String var, int n) {
        final List<String> ms = new ArrayList<>();
        for (int i = 0; i < n; i++)
            ms.add(invoke(var, TUPLE_METHODS[i]));
        return ms;
    }

    /** Generates a to String method with the specified Field names. */
    ClassGenerator withToString(Iterable<String> fields, String start, String sep, String end) {
        final StrBuilder s = new StrBuilder(quoted(start) + CAT).startCollection(CAT + quoted(sep) + CAT);
        for (final String f : fields)
            s.appendElement(f);
        s.append(CAT + quoted(end));
        method(TO_STRING, String.class).asPublic().withoutNullable().return_(s);
        return this;
    }

    private void createEq(List<String> fs) {
        final StrBuilder b = new StrBuilder();
        b.startCollection(AND);
        for (final String field : fs)
            b.appendElement(invokeEqual(field, THAT));

        final Method m = method("eq", Constants.BOOLEAN).asPrivate();
        m.arg(THAT, getName()).notNull();
        m.return_(b);
    }

    /** Implements logger method from interface LoggableInstance. */
    private void implementLoggerMethod() {
        method(LOGGER_FIELD, Logger.class).return_(LOGGER_FIELD).notNull().override();
    }

    private String invokeEqual(String field, String that) {
        return invokeStatic(EQUAL_METHOD, field, that + "." + field);
    }

    private String serialVersionUID()
        throws IOException
    {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final DataOutputStream      out  = new DataOutputStream(bout);

        out.writeUTF(getName());

        out.writeInt(modifiers);

        for (final Field field : getFields()) {
            out.writeUTF(field.getName());
            out.writeInt(field.modifiers);
            out.writeUTF(field.getType());
        }
        out.flush();

        final Sha sha = new Sha();
        sha.process(bout.toByteArray());
        return sha.getDigestAsLong() + "L";
    }

    //~ Methods ......................................................................................................................................

    private static void createLoggerField(JavaItemGenerator<?> g, String name) {
        g.field(LOGGER_FIELD, Logger.class, g.invoke("Logger", "getLogger", g.classOf(name))).asPrivate().asFinal().asStatic().notNull();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger                                       = Logger.getLogger(ClassGenerator.class);
    public static final String  MODIFICATION_WARNING_LINE_1                  =
        "Don't modify this as this is an auto generated class that's gets generated";
    public static final String  MODIFICATION_WARNING_LINE_2_WITHOUT_SUBCLASS = "every time the meta model file is modified.";
    public static final String  MODIFICATION_WARNING_LINE_2                  = MODIFICATION_WARNING_LINE_2_WITHOUT_SUBCLASS +
                                                                               " Use subclass instead.";
}  // end class ClassGenerator
