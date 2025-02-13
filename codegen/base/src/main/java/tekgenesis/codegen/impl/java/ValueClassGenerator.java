
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Strings;

import static tekgenesis.codegen.CodeGeneratorConstants.CREATE_COMMENT;
import static tekgenesis.common.core.Constants.VALUE_OF;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.NEW;

/**
 * A Value class is an Immutable class with getter for all methods and a set of default handy
 * methods.
 */
@SuppressWarnings("UnusedReturnValue")
public class ValueClassGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private boolean finalFields = true;

    private String toStrEnd   = ")";
    private String toStrSep   = ",";
    private String toStrStart = "(";

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    protected ValueClassGenerator(JavaItemGenerator<?> parent, String name) {
        super(parent, name);
    }
    ValueClassGenerator(JavaCodeGenerator cg, String name) {
        super(cg, name);
    }

    //~ Methods ......................................................................................................................................

    /** Define to string separators. */
    public ClassGenerator withToStringSeparators(String start, String sep, String end) {
        toStrStart = start;
        toStrSep   = sep;
        toStrEnd   = end;
        return this;
    }

    @Override protected void populate() {
        final List<String>               fieldNames = new ArrayList<>();
        final ImmutableCollection<Field> fields     = getFields();
        for (final Field field : fields)
            fieldNames.add(field.getName());
        addDefaultItems(fields, fieldNames);
        super.populate();
    }

    protected void setFinalFields(boolean b) {
        finalFields = b;
    }

    private void addDefaultItems(ImmutableCollection<Field> fields, List<String> fieldNames) {
        // Equals & hashCode
        withEquals(fieldNames).withHashCode(fieldNames);

        // All fields are final and have a getter
        for (final Field field : fields) {
            field.withGetter();
            if (finalFields) field.asFinal();
        }

        // Constructor with all fields
        final Constructor c = Predefined.cast(constructor().withComments(String.format(CREATE_COMMENT, getName())));
        for (final Field f : fields)
            c.arg(f);
        // To String method
        withToString(fieldNames, toStrStart, toStrSep, toStrEnd);

        // Value Of Methods

        final Method m = method(VALUE_OF, getName()).asFinal().asStatic().notNull();
        m.arg(STR, String.class).notNull();
        m.declare(CodeGeneratorConstants.STRING_ARRAY,
            ARGS,
            invokeStatic(Strings.class,
                CodeGeneratorConstants.SPLIT_TO_ARRAY,
                STR,
                quoted(toStrStart),
                quoted(toStrSep),
                quoted(toStrEnd),
                String.valueOf(fields.size())));

        final StrBuilder n = new StrBuilder(NEW + " " + getName() + "(");
        n.startCollection(", ");
        int i = 0;
        for (final Field f : fields) {
            final String v = ARGS + "[" + i++ + "]";
            n.appendElement(invokeConvertFromString(v, f.getType()));
        }
        n.append(")");
        m.return_(n);
    }  // end method addDefaultItems

    //~ Static Fields ................................................................................................................................

    private static final String ARGS = "args";
    private static final String STR  = "str";
}  // end class ValueClassGenerator
