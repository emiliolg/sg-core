
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Map;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.EnumGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.EnumException;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import tekgenesis.field.TypeField;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;
import tekgenesis.type.Type;

import static java.lang.String.format;

import static tekgenesis.codegen.CodeGeneratorConstants.FIRST;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.impl.java.ClassGenerator.MODIFICATION_WARNING_LINE_1;
import static tekgenesis.codegen.impl.java.ClassGenerator.MODIFICATION_WARNING_LINE_2;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Constants.EXCEPTION;
import static tekgenesis.common.core.Enumeration.MAP_METHOD;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.env.i18n.I18nBundle.GET_METHOD;
import static tekgenesis.common.util.JavaReservedWords.THIS;

/**
 * Class to generate the enums.
 */
public class EnumCodeGenerator extends EnumGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final EnumType enumType;
    private final String   keyClass;

    //~ Constructors .................................................................................................................................

    /** Create an EnumCodeGenerator. */
    public EnumCodeGenerator(JavaCodeGenerator cg, EnumType enumType, boolean forExport) {
        super(cg, enumType.getName());
        this.enumType = enumType;
        addConstants(makeConstants());
        keyClass = enumType.getPkType().getImplementationClassName();

        final String enumeration = generic(Enumeration.class, getName(), keyClass);
        if (enumType.isException()) withInterfaces(enumeration, generic(EnumException.class, innerExceptionName(getName())));
        else withInterfaces(enumeration);

        if (!forExport) withInterfaces(enumType.getInterfaces());
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return enumType.getSourceName();
    }

    @Override protected void populate() {
        final String documentation = enumType.getDocumentation();
        if (!documentation.isEmpty()) {
            for (final String d : documentation.split("\n"))
                withComments(d.trim());
            withComments("");
        }

        withComments("Generated base class for enum: " + getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);

        suppressWarnings(COMMON_SUPPRESSED_WARNINGS);

        // A field to hold the list of values
        final String mapName = fromCamelCase(getName() + "Map");
        field(mapName,                                                    //
                generic(Map.class, keyClass, getName()),                  //
                invokeStatic(Enumerations.class, "buildMap", invoke("", VALUES_METHOD)))  //
        .asFinal().asStatic().notNull()                                   //
        .withGetter(MAP_METHOD);

        // The constructor
        final Constructor cons = constructor().asPackage();

        // A field to hold the label
        cons.arg(field(LABEL_FIELD, String.class).asFinal().notNull());
        addPropertiesForFields(this, enumType, cons);

        // The bundle field and the method to get the labels
        field(BUNDLE_FIELD, I18nBundle.class.getName(), invokeStatic(I18nBundle.class, "getBundle", classOf(getName()))).asFinal()
            .asStatic()
            .notNull();

        method(LABEL_FIELD, String.class).notNull().asFinal()  //
        .withComments("Returns the field label in the current locale").return_(invoke(BUNDLE_FIELD, GET_METHOD, NAME_METHOD, LABEL_FIELD));

        final String indexField  = enumType.getIndexFieldName();
        final String indexMethod = !indexField.isEmpty() ? "get" + capitalizeFirst(indexField) : keyClassIsInteger(keyClass) ? KEY_METHOD : "ordinal";
        method(INDEX, Integer.TYPE).return_(invoke(indexMethod));

        final Method inMethod = method("in", Boolean.class).notNull()
                                .asFinal()  //
                                .withComments("Check that the enum is one of the specified values")
                                .return_(invoke(invokeStatic(EnumSet.class, "of", FIRST, "rest"), "contains", "this"));
        inMethod.arg(FIRST, getName()).notNull();
        inMethod.arg("rest", getName() + "...").notNull();

        // method(LABEL_FIELD, String.class).notNull()
        // .asFinal()  //
        // .withComments("Returns the field label in the current locale formatted with arguments")
        // .return_(invokeStatic(String.class, FORMAT, invoke("", LABEL_FIELD), "args"))
        // .arg(ARGS, OBJECT_ARRAY)
        // .notNull();

        method(IMAGE_PATH_METHOD, String.class)                             //
        .notNull().asFinal().withComments("Returns the field image path ")  //
        .return_(invoke(BUNDLE_FIELD, GET_METHOD, format("%s + \".%s\"", NAME_METHOD, IMAGE_EXT), EMPTY_STRING));

        final String pkFieldName = enumType.getPkFieldName();
        method(KEY_METHOD, keyClass).boxedNotNull().asFinal()  //
        .withComments("Returns the enum primary key").return_(pkFieldName.isEmpty() ? NAME_METHOD : pkFieldName);
        generateFactory(pkFieldName.isEmpty() ? "name" : pkFieldName, mapName);

        if (enumType.isException()) generateExceptionMethods();

        super.populate();
    }  // end method populate

    private void generateExceptionMethods() {
        final String exceptionName = innerExceptionName(getName());

        final Method withoutArgs = method(EXCEPTION.toLowerCase(), exceptionName).override().notNull().asFinal();
        withoutArgs.return_(new_(exceptionName, THIS));

        final Method withArgs = method(EXCEPTION.toLowerCase(), exceptionName).override().notNull().asFinal();
        withArgs.arg(ARGS, OBJECT_ARRAY).notNull();
        withArgs.return_(new_(exceptionName, THIS, ARGS));
    }

    private void generateFactory(String pkFieldName, String mapName) {
        // /** Get a Color from the id */
        // @NotNull public static Option<Color> fromId(int id) { return Option.ofNullable(COLOR_MAP.get(id)); }
        final String methodName = format("from%s", capitalizeFirst(pkFieldName));
        method(methodName, generic(Option.class, getName())).withComments(format("Get a %s from the %s", getName(), pkFieldName))
            .asStatic()
            .asFinal()
            .return_(invokeStatic(Option.class, OF_NULLABLE, invoke(mapName, "get", pkFieldName)))
            .arg(pkFieldName, keyClass)
            .notNull();
    }

    /** Create the Sequence of constants for the Enum based on the EnumType. */
    private Seq<String> makeConstants() {
        final int         fieldsNo   = enumType.getChildren().size();
        final TypeField[] enumFields = new TypeField[fieldsNo];
        int               i          = 0;
        for (final TypeField t : enumType.getChildren())
            enumFields[i++] = t;
        return enumType.getValues().map(value -> valueAsConstant(value, fieldsNo, enumFields));
    }

    private String quoteObject(Type type, Object o) {
        if (o == null) return "null";
        final String value = o.toString();
        if (o instanceof BigDecimal) return new_(BigDecimal.class, quoted(value));
        if (o instanceof Number || o instanceof Boolean) return value;
        if (type.isEnum()) return extractImport(type.getImplementationClassName()) + "." + value;
        return quoted(value);
    }

    @NotNull private String valueAsConstant(final EnumValue value, final int fieldsNo, final TypeField[] enumFields) {
        final String[] args = new String[fieldsNo + 1];
        args[0] = quoted(value.getLabel());
        for (int j = 0; j < fieldsNo; j++)
            args[j + 1] = quoteObject(enumFields[j].getFinalType(), value.getValues()[j]);

        final String doc     = value.getFieldDocumentation();
        final String javaDoc = "/** " + (doc.isEmpty() ? value.getLabel() : doc) + " */\n\t";

        return javaDoc + value.getName() + ImmutableList.fromArray(args).mkString("(", ", ", ")");
    }

    //~ Methods ......................................................................................................................................

    @NotNull static String innerExceptionName(@NotNull String name) {
        return name.replace(EXCEPTION, "") + "ApplicationException";
    }

    private static void addPropertiesForFields(JavaItemGenerator<?> cg, EnumType enumType, Constructor cons) {
        // The additional fields
        for (final TypeField f : enumType.getChildren()) {
            final Field fld = cg.readOnlyProperty(f.getName(), f.getType().getImplementationClassName()).asFinal().required(f.isRequired());
            cons.arg(fld);
        }
    }

    private static boolean keyClassIsInteger(String keyClass) {
        return keyClass.endsWith("Integer");
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String BUNDLE_FIELD = "BUNDLE";

    @NonNls private static final String KEY_METHOD = "key";

    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String         LABEL_FIELD = "label";

    private static final String NAME_METHOD = "name()";
}  // end class EnumCodeGenerator
