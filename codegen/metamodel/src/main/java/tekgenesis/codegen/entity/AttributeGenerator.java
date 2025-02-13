
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import java.util.EnumSet;
import java.util.Iterator;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.md.MdConstants;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;

import static tekgenesis.codegen.CodeGeneratorConstants.EQ_NULL;
import static tekgenesis.codegen.common.Generators.verifyField;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.entity.DbTableCodeGenerator.singletonName;
import static tekgenesis.codegen.entity.EntityBaseCodeGenerator.makePrimaryKeyType;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.EMPTY_STRING_ARRAY;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.util.JavaReservedWords.NULL;
import static tekgenesis.common.util.JavaReservedWords.THIS;
import static tekgenesis.md.MdConstants.DATA_FIELD_NAME;

class AttributeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Attribute               attr;
    private final String                  attrName;
    private String                        classForField;
    private final String                  className;
    private final DbObject                dbObject;
    private boolean                       fieldAsProtected;
    private String                        markAsModified;
    private final EntityBaseCodeGenerator tg;

    //~ Constructors .................................................................................................................................

    AttributeGenerator(EntityBaseCodeGenerator tg, final String className, Attribute attr) {
        this.attr        = attr;
        dbObject         = tg.dbObject;
        this.className   = className;
        attrName         = attr.getName();
        this.tg          = tg;
        markAsModified   = "";
        fieldAsProtected = false;
        classForField    = "this";
    }

    //~ Methods ......................................................................................................................................

    void generate(ClassGenerator target) {
        if (isAbstract()) createField(target);
        else if (attr.isMultiple()) addMultiple(target);
        else if (attr.isEntity() || attr.isView()) addReference(target);
        else createField(target);
    }

    void generateMutableSetter(boolean hasDataField) {
        markAsModified = invoke("", "markAsModified", hasDataField ? new String[] { DATA_FIELD_NAME } : EMPTY_STRING_ARRAY);
        generateSetter();
    }

    void generatePk(ImmutableList.Builder<JavaElement.Field> fieldList, ClassGenerator target) {
        for (final TypeField field : attr.retrieveSimpleFields()) {
            final String fieldName = field.getName();
            final String fieldType = field.getImplementationClassName();
            fieldList.add(attributeField(dataField(fieldName, fieldType, target), field, true));
        }
        addReferenceAttribute();
    }
    void generateSetter() {
        if (!dbObject.isUpdatable() || attr.isReadOnly() || dbObject.isPrimaryKey(attr)) return;

        if (isAbstract()) addSetter();
        else if (attr.isMultiple()) attr.asEnum().ifPresent(e -> addSetter(tg.generic(EnumSet.class, attr.getImplementationClassName()), true));
        else if (attr.isEntity() || attr.isView()) addReferenceSetter();
        else addSetter();
    }
    void setClassForField(String classForField) {
        this.classForField = classForField;
    }

    void setFieldAsProtected() {
        fieldAsProtected = true;
    }

    private void addMultiple(ClassGenerator target) {
        attr.asDatabaseObject().ifPresent(e -> {
            for (final Attribute foreign : e.getAttribute(attr.getReverseReference())) {
                if (attr.isInner()) addSeqField(foreign, INNER_ENTITY_SEQ);
                else addSeqField(foreign, ENTITY_SEQ);
            }
        });
        attr.asEnum().ifPresent(e -> {
            final String enumType = attr.getImplementationClassName();
            final String type     = tg.generic(EnumSet.class, enumType);

            final String defaultValue = tg.defaultFor(tg, attr, true);
            if (!isAbstract()) dataField(attrName, type, target).required(true).withValue(defaultValue);

            createGetter(attrName, type, true, defaultValue);
        });
    }

    private void addReference(ClassGenerator target) {
        for (final TypeField typeField : attr.retrieveSimpleFields())
            attributeField(dataField(typeField.getName(), typeField.getImplementationClassName(), target), typeField, typeField.isRequired());
        addReferenceAttribute();
    }

    private void addReferenceAttribute() {
        attr.asDatabaseObject().ifPresent(ref -> {
            final String         type       = ref.getImplementationClassName();
            final String         refType    = tg.generic(ENTITY_REF, type, makePrimaryKeyType(tg, ref));
            final Seq<TypeField> attrFields = attr.retrieveSimpleFields();

            // Field
            final String reverse = attr.getReverseReference();

            final String value = tg.newGeneric(ENTITY_REF,
                    tg.extractStaticImport(singletonName(ref)),
                    reverse.isEmpty() ? null : tg.refMethod(type, getterName(reverse)));
            tg.field(attrName, refType).required(true).withValue(value).asPackagePrivate();
            tg.addReference(attrName, value);

            // Getter
            tg.method(getterName(attrName, type), type).required(attr.isRequired())  //
              .withGetterComments(attrName)                   //
              .addModifier(isProtected() ? PROTECTED : PUBLIC)  //
              .return_(invokeSolve(attrName, attrFields));
        });
    }                                                         // end method addReferenceAttribute

    private void addReferenceSetter() {
        for (final TypeField typeField : attr.retrieveSimpleFields()) {
            final String             fieldName = typeField.getName();
            final JavaElement.Method method    = tg.method(setterName(fieldName), className)  //
                                                 .withSetterComments(fieldName)     //
                                                 .addModifier(isProtected() ? PROTECTED : PUBLIC).notNull();
            method.arg(fieldName, typeField.getImplementationClassName()).required(typeField.isRequired());
            method.statement(invoke(attrName, EntityBaseCodeGenerator.INVALIDATE))  //
            .assign(dataRef(typeField), fieldName).return_(tg.referenceThisType(THIS));
        }
        attr.asDatabaseObject().ifPresent(this::addRefSetter);
    }

    private void addRefSetter(final DbObject ref) {
        final boolean nullable = !attr.isRequired();

        final JavaElement.Method setter = tg.method(setterName(attrName), className).required(!nullable).withSetterComments(attrName).notNull();
        setter.arg(attrName, ref.getImplementationClassName()).required(!nullable);
        if (isProtected()) setter.asProtected();
        setter.statement(invoke(THIS(attrName), "set", attrName));

        final Seq<TypeField> attrFields = attr.retrieveSimpleFields();
        if (nullable) {
            setter.suppressWarnings(ASSIGNMENT_TO_NULL);
            setter.startIf(attrName + EQ_NULL);
            for (final TypeField f : attrFields)
                setter.assign(dataRef(f), NULL);
            setter.startElse();
        }
        final Iterator<TypeField> refFields = ref.retrieveSimpleFields().iterator();
        for (final TypeField f : attrFields) {
            final TypeField refField = refFields.next();
            setter.assign(dataRef(f), invoke(attrName, getterName(refField.getName(), refField.getImplementationClassName())));
        }
        if (nullable) setter.endIf();
        setter.return_(tg.referenceThisType(THIS));
    }

    private void addSeqField(@NotNull Attribute foreign, String clazz) {
        final String type    = attr.getElementClassName();
        final String seqType = tg.generic(clazz, type);

        final String singleton = tg.extractStaticImport(singletonName(attr.asDatabaseObject().get()));

        final boolean               inner    = clazz.equals(INNER_ENTITY_SEQ);
        final String                ref;
        final String                thisType = tg.referenceThisType(THIS);
        final ImmutableList<String> args     = listOf(singleton, thisType, getRefFunction(foreign));

        if (inner) ref = tg.invokeStatic(createQName(ENTITY_SEQ).append(CREATE_INNER_ENTITY_SEQ), args);
        else
            ref = tg.invokeStatic(createQName(ENTITY_SEQ).append(CREATE_ENTITY_SEQ),
                    args.append(tg.invokeListOf(foreign.retrieveSimpleFields().map(tf -> singleton + "." + fromCamelCase(tf.getName())))));

        final String            name = attrName;
        final JavaElement.Field f    = tg.field(name, seqType).notNull().withValue(ref);

        tg.addReference(f.getName(), ref);

        // Getter
        tg.method(getterName(name, seqType), seqType)
            .notNull()
            .addModifier(isProtected() ? PROTECTED : PUBLIC)
            .withGetterComments(name)
            .return_(name);
    }  // end method addSeqField

    private void addSetter() {
        addSetter(attr.getImplementationClassName(), attr.isRequired());
    }

    private void addSetter(String attrType, boolean required) {
        final JavaElement.Method setter = tg.method(setterName(attrName), tg.className)
                                          .withSetterComments(attrName)
                                          .addModifier(isProtected() ? PROTECTED : PUBLIC)
                                          .notNull();

        setter.arg(attrName, attrType).required(required);

        if (isAbstract()) setter.asAbstract();
        else {
            if (!markAsModified.isEmpty()) setter.statement(markAsModified);
            setter.assign(dataRef(attr), verifyField(tg, attrName, attr));
            setter.return_(tg.referenceThisType(THIS));
        }
    }  // end method addSetter

    private JavaElement.Field attributeField(final JavaElement.Field f, final TypeField field, final boolean required) {
        final String defaultValue = tg.defaultFor(tg, field, required);

        f.required(required).withValue(defaultValue);
        createGetter(f.getName(), f.getType(), required, defaultValue);
        return f;
    }

    private void createField(ClassGenerator target) {
        final String  type     = attr.getImplementationClassName();
        final boolean required = attr.isRequired();

        if (isAbstract()) createGetter(attrName, type, required, "");
        else {
            final String defaultValue = tg.defaultFor(tg, attr, required);
            dataField(attrName, type, target).required(required).withValue(defaultValue);
            createGetter(attrName, type, required, defaultValue);
        }
    }

    private void createGetter(final String name, final String type, final boolean required, final String defaultValue) {
        final JavaElement.Method getter = tg.method(getterName(name, type), type)  //
                                          .required(required)  //
                                          .addModifier(isProtected() ? PROTECTED : PUBLIC)  //
                                          .addModifier(isAbstract() ? ABSTRACT : 0)  //
                                          .withGetterComments(name).return_(dataRef(name));
        if (MdConstants.isVersionField(name)) getter.override();
        if (required && NULL.equals(defaultValue)) getter.suppressWarnings(NULLABLE_PROBLEMS);
    }

    private JavaElement.Field dataField(final String fieldName, final String fieldType, ClassGenerator target) {
        final JavaElement.Field field = target.field(fieldName, fieldType);
        if (fieldAsProtected) field.asProtected();
        else field.asPackagePrivate();
        return field;
    }

    @NotNull private String dataRef(final TypeField tf) {
        return dataRef(tf.getName());
    }
    @NotNull private String dataRef(final String name) {
        return classForField + "." + name;
    }

    private String invoke(String targetEntity, String method, String... args) {
        return tg.invoke(targetEntity, method, args);
    }

    private String invokeSolve(String name, final Seq<TypeField> attrFields) {
        if (attrFields.size() == 1) return invoke(name, attr.isRequired() ? SOLVE_OR_FAIL : SOLVE, dataRef(attrFields.getFirst().get()));
        final ImmutableList<String> args = attrFields.map(this::dataRef).toList();

        final String makeTuple = tg.invokeStatic(Tuple.class, CodeGeneratorConstants.tupleMethod(args.size()), args);
        if (attr.isRequired()) return invoke(name, SOLVE_OR_FAIL, makeTuple);

        final String condition = attrFields.filter(TypeField::isOptional).map(tf -> dataRef(tf) + " == null").mkString(" || ");

        return condition + " ? null : " + invoke(name, SOLVE, makeTuple);
    }

    private String THIS(String field) {
        return THIS + "." + field;
    }

    private boolean isProtected() {
        return attr.hasOption(FieldOption.PROTECTED) || dbObject.isProtected() && !MdConstants.isVersionField(attrName);
    }

    private String getRefFunction(@NotNull Attribute foreign) {
        final String ownerRef   = foreign.getName();
        final QName  childType  = createQName(foreign.getDbObject().getImplementationClassName());
        final QName  parentType = createQName(tg.getDbObject().getImplementationClassName());

        // If different packages use reflection
        if (!parentType.getQualification().equals(childType.getQualification()))
            return tg.invokeStatic(createQName(ENTITY_REF).append("getRefFunction"), tg.classOf(childType.getFullName()), quoted(ownerRef));

        return format("c -> ((%sBase)c).%s", childType.getName(), foreign.getName());
    }

    private boolean isAbstract() {
        return attr.hasOption(FieldOption.ABSTRACT);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String SOLVE         = "solve";
    @NonNls private static final String SOLVE_OR_FAIL = "solveOrFail";
}  // end class AttributeGenerator
