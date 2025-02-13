
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.type;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.type.*;

import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.Generators.defaultFor;
import static tekgenesis.codegen.common.Generators.verifyField;
import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.codegen.common.MMCodeGenConstants.COMMON_SUPPRESSED_WARNINGS;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Constants.CONSTRUCTOR_FOR;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.THIS;

/**
 * Class to manage the generation of the StructType class.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })
public class StructTypeCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    protected final StructType         type;
    private final ImmutableSet<String> args;
    private final Map<String, Field>   fields;

    //~ Constructors .................................................................................................................................

    /** Create an StructTypeCodeGenerator. */
    public StructTypeCodeGenerator(JavaCodeGenerator cg, @NotNull final StructType type) {
        this(cg, type, type.hasModifier(Modifier.FINAL));
    }

    /** Create an StructTypeCodeGenerator. */
    public StructTypeCodeGenerator(JavaCodeGenerator cg, @NotNull final StructType type, boolean isFinal) {
        super(cg, type.getName() + (isFinal ? "" : BASE));
        this.type = type;
        args      = type.getArgs();
        fields    = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return type.getSourceName();
    }

    protected void populate() {
        withComments("Generated base class for type: " + type.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(COMMON_SUPPRESSED_WARNINGS);
        if (type.hasModifier(Modifier.FINAL)) asFinal();

        asSerializable();

        addExtendsAndImplements();

        addFields();
        withAnnotation(extractImport(JSON_PROPERTY_ORDER), seq(fields.keySet()).map(Strings::quoted));
        final String none = extractStaticImport(QName.createQName(JSON_AUTODETECT + ".Visibility", "NONE"));
        withAnnotation(extractImport(JSON_AUTODETECT), V.map(s -> s + "=" + none).mkString(","));
        addConstructor();

        addEqualsAndHashCode();

        addJsonMappings();

        addToStringMethod();

        super.populate();
    }

    private void addArgumentFromSuperTypes(List<Field> superArgs, Constructor c) {
        type.getSuperTypes().forEach((parent) -> c.invokeSuper(parent.getArgs()));
        superArgs.forEach((field) -> {
            final String   fieldName = field.getName();
            final Argument arg       = c.arg(fieldName, field.getType())
                                        .withAnnotation(extractImport(CodeGeneratorConstants.JSON_PROPERTY), quoted(fieldName));
            if (field.isNotNull()) arg.notNull();
        });
    }

    private void addArgumentsFromComposition(List<String> compositeArgs, Constructor c) {
        compositeArgs.forEach((arg) -> {
            final Field field = fields.get(arg);
            if (field.isNotNull()) {
                final StructType structType = (StructType) type.getField(arg).get().getType();

                structType.getArgs().forEach((childArg) -> {
                    final TypeField typeField = structType.getField(childArg).get();
                    final Argument  argument  = c.arg(childArg, getImplementationClassName(typeField))
                                                 .withAnnotation(extractImport(CodeGeneratorConstants.JSON_PROPERTY), quoted(childArg));
                    if (typeField.isRequired()) argument.notNull();
                });

                c.assign(field.getName(), new_(structType.getImplementationClassName(), structType.getArgs()));
            }
        });
    }

    private void addConstructor() {
        final List<Field>  superArgs     = getSuperTypeArguments();
        final List<String> compositeArgs = getCompositeArguments();

        if (args.isEmpty() && superArgs.isEmpty() && compositeArgs.isEmpty()) return;

        final Constructor c = constructor().withAnnotation(extractImport(CodeGeneratorConstants.JSON_CREATOR))
                              .withComments(typeConstructorComment(type));

        addArgumentFromSuperTypes(superArgs, c);
        addArgumentsFromComposition(compositeArgs, c);

        args.forEach((a) -> c.arg(fields.get(a)).withAnnotation(extractImport(CodeGeneratorConstants.JSON_PROPERTY), quoted(a)));
    }

    private void addEqualsAndHashCode() {
        @SuppressWarnings("Convert2MethodRef")
        final Seq<String>           fieldNames = args.isEmpty() ? getFields().map(field -> field.getName()) : args;
        final ImmutableList<String> fs         = fieldNames.toList();
        withEquals(fs).withHashCode(fieldNames);
    }

    private void addExtendsAndImplements() {
        boolean noExtension = true;

        for (final StructType parent : type.getSuperTypes()) {
            if (parent.isInterface()) withInterfaces(parent.getFullName());
            else {
                noExtension = false;
                withSuperclass(parent.getFullName());
            }
        }
        if (noExtension) withInterfaces(Struct.class);
    }  // end method addExtendsAndImplements

    private void addField(@NotNull final TypeField field) {
        final String t = getImplementationClassName(field);
        final Field  f = field(field.getName(), t);
        f.withAnnotation(extractImport(JSON_PROPERTY));
        fields.put(f.getName(), f);
        classField(f, field);
    }
    private void addFields() {
        final Map<String, TypeField> fieldMap = new LinkedHashMap<>();

        for (final StructType parent : type.getSuperTypes()) {
            if (parent.isInterface()) {
                for (final TypeField field : parent.getChildren())
                    fieldMap.put(field.getName(), field);
            }
        }

        for (final TypeField field : type.getChildren())
            fieldMap.put(field.getName(), field);

        for (final StructType parent : type.getSuperTypes()) {
            if (!parent.isInterface()) {
                for (final TypeField field : parent.getChildren())
                    fieldMap.remove(field.getName());
            }
        }

        fieldMap.values().forEach(this::addField);
    }

    private void addFieldSetter(TypeField field, String name, String t) {
        final Method setter = setter(name, type.getImplementationClassName());
        setter.boxedNotNull();
        setter.arg(name, t).required(field.isRequired());
        setter.withSetterComments(name);
        setter.assign(THIS(name), verifyField(this, name, field));
        setter.return_(referenceThisType(THIS));
    }

    private void addJsonMappings() {
        final String className = type.getFinalType().getImplementationClassName();

        method(FROM_JSON, className).asStatic()
            .notNull()
            .withComments(String.format("Attempt to construct a %s instance from an InputStream.", type.getName()))
            .return_(invokeStatic(JsonMapping.class, FROM_JSON, STREAM, classOf(className)))
            .arg(STREAM, InputStream.class)
            .notNull()
            .asFinal();

        method(FROM_JSON, className).asStatic()
            .notNull()
            .withComments(String.format("Attempt to construct a %s instance from a String.", type.getName()))
            .return_(invokeStatic(JsonMapping.class, FROM_JSON, VALUE, classOf(className)))
            .arg(VALUE, String.class)
            .notNull()
            .asFinal();
        //J+
    }

    private void addToStringMethod() {
        method(TO_STRING, String.class).override().notNull().return_(invoke("", TO_JSON));
    }

    private Field classField(final Field f, final TypeField field) {
        final boolean isMutable = !args.contains(f.getName());
        final boolean required  = field.isRequired();

        f.withGetter(field.hasOption(FieldOption.PROTECTED) ? PROTECTED : PUBLIC).required(required);
        if (isMutable) {
            final String defaultValue = getDefaultFieldValue(field, required);
            f.withValue(defaultValue);
            addFieldSetter(field, field.getName(), getImplementationClassName(field));
        }
        return f;
    }

    private String referenceThisType(String var) {
        return extractName(type.getImplementationClassName()).equals(getName()) ? var : cast(type.getImplementationClassName(), var);
    }

    @NotNull private ImmutableList<String> getCompositeArguments() {
        return type.getChildren()
               .filter(typeField -> typeField.getType() instanceof StructType && !((StructType) typeField.getType()).getArgs().isEmpty())
               .map(TypeField::getName)
               .filter(fieldName -> !type.getArgs().exists(arg -> arg.equals(fieldName)))
               .toList();
    }

    private String getDefaultFieldValue(TypeField field, boolean required) {
        final Type fieldType = field.getType();
        if (fieldType.getKind() == Kind.ARRAY && required) {
            extractImport(ArrayList.class);
            return "new ArrayList<>()";
        }
        if (fieldType.isComposite() && required) {
            if (fieldType instanceof StructType && !((StructType) fieldType).getArgs().isEmpty()) return "";
            return new_(fieldType.getImplementationClassName());
        }
        return defaultFor(this, field, required);
    }

    private String getImplementationClassName(TypeField field) {
        if (field.getType().getKind() == Kind.ARRAY)
            return generic(List.class, ((ArrayType) field.getType()).getElementType().getImplementationClassName());
        else return field.getImplementationClassName();
    }

    @NotNull private List<Field> getSuperTypeArguments() {
        final List<Field> superArgs = new ArrayList<>();

        type.getSuperTypes().forEach((parent) ->
                parent.getArgs().forEach((arg) ->
                        parent.getField(arg).ifPresent((t) -> {
                            final Field field = new Field(t.getName(), getImplementationClassName(t), "");
                            field.required(t.isRequired());
                            superArgs.add(field);
                        })));
        return superArgs;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the comment used for struct type's constructor. */
    @NotNull static String typeConstructorComment(@NotNull StructType type) {
        return CONSTRUCTOR_FOR + type.getName();
    }

    //~ Static Fields ................................................................................................................................

    private static final ImmutableList<String> V = listOf("getterVisibility",
            "isGetterVisibility",
            "setterVisibility",
            "creatorVisibility",
            "fieldVisibility");

    @NonNls public static final String TO_JSON   = "toJson";
    @NonNls public static final String STREAM    = "stream";
    @NonNls public static final String FROM_JSON = "fromJson";
}  // end class StructTypeCodeGenerator
