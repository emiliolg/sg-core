
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.CacheType;
import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.common.Generators;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.core.Strings;
import tekgenesis.common.util.Conversions;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.type.MetaModel;

import static java.lang.String.format;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.Generators.makeArguments;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.entity.DbTableCodeGenerator.singletonName;
import static tekgenesis.codegen.entity.EnumCodeGenerator.LABEL_FIELD;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.VALUE_OF;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.util.JavaReservedWords.*;
import static tekgenesis.common.util.Primitives.wrapIfNeeded;
import static tekgenesis.field.TypeField.retrieveSimpleFields;
import static tekgenesis.md.MdConstants.DATA_FIELD_NAME;

/**
 * Class to manage the generation of the EntityBase code.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass", "ClassTooDeepInInheritanceTree" })
public class EntityBaseCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    protected final DbObject dbObject;
    protected final String   tableSingleton;

    final String                           className;
    final ImmutableList<JavaElement.Field> primaryKeyFields;
    @Nullable private final DataGenerator  data;
    private final boolean                  immutable;
    private final String                   mainInterface;
    private final String                   primaryKeyType;
    private final Map<String, String>      references = new HashMap<>();
    private final ClassGenerator           target;

    //~ Constructors .................................................................................................................................

    /** Create an EntityBaseCodeGenerator. */
    public EntityBaseCodeGenerator(JavaCodeGenerator cg, @NotNull final DbObject dbObject, String className) {
        this(cg, dbObject, className, false, null);
    }
    EntityBaseCodeGenerator(JavaCodeGenerator cg, @NotNull final DbObject dbObject, String className, boolean forUpdate,
                            @Nullable ImmutableList<Field> primaryKey) {
        super(cg, className + BASE);
        this.dbObject  = dbObject;
        this.className = className;
        primaryKeyType = makePrimaryKeyType(this, dbObject);
        data           = addData();
        target         = data == null ? this : data;

        primaryKeyFields = primaryKey != null ? primaryKey : hasSingleDefaultPrimaryKey() ? addDefaultPrimaryKeyAttr() : addPrimaryKeyAttributes();
        tableSingleton   = extractStaticImport(singletonName(dbObject));
        immutable        = !forUpdate && dbObject.splitMutator();
        if (immutable) mainInterface = ENTITY_INSTANCE;
        else if (dbObject.isInner()) mainInterface = INNER_INSTANCE;
        else mainInterface = dbObject.isView() ? dbObject.asView().isUpdatable() ? UPDATABLE_INSTANCE : ENTITY_INSTANCE : PERSISTABLE_INSTANCE;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the primary key type. */
    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    @Override public String getSourceName() {
        return dbObject.getSourceName();
    }

    protected void addCreateMethods() {
        final Method c = createMethod();

        // Empty create
        if (dbObject.hasDefaultPrimaryKey()) c.return_(new_(className));
        else if (primaryKeyFields.isEmpty()) c.throwNew(UnsupportedOperationException.class);
        else {
            // Create from key fields
            c.arguments(makeArguments(primaryKeyFields)).notNull();
            c.declare(className, RESULT, new_(className));
            int                            i          = 0;
            final ImmutableList<TypeField> typeFields = dbObject.primaryKeySimpleFields();
            for (final Field field : primaryKeyFields) {
                final String fld = Generators.verifyField(this, field.getName(), typeFields.get(i++));
                c.assign("(" + cast(getImmutableBase(), RESULT) + ")." + (data == null ? "" : DATA_FIELD_NAME + ".") + field.getName(), fld);
            }
            c.return_(RESULT);
        }

        if (hasCompositeKey()) {
            // Create based on primary key object
            final String[] args = new String[primaryKeyFields.size()];
            for (int i = 0; i < args.length; i++)
                args[i] = invoke(KEY_ARG, "_" + (i + 1));

            final JavaElement.Method ck = createMethod().withComments("Based on the primary key object");
            ck.arg(KEY_ARG, primaryKeyType).notNull();
            ck.return_(invoke("", createMethodName(), args));
        }
        // Extra Create method if any of the elements in the key is an instance
        final ImmutableCollection<Attribute> pk = dbObject.getPrimaryKey();
        if (pk.exists(ATTR_IS_ENTITY_NOT_STRING)) addFindOrCreateFromEntities(pk, createMethod());
    }  // end method addCreateMethods

    protected void addInnerMethods(Attribute parentAttribute, String parentType, String parentKey) {
        method(PARENT, generic(ENTITY_REF, parentType, parentKey)).asPublic().notNull().override().return_(parentAttribute.getName());

        final String parentGetter = invoke("", getterName(parentAttribute.getName(), ""));
        method("siblings", generic(INNER_ENTITY_SEQ, className)).asPublic()
            .notNull()
            .override()
            .return_(invoke(parentGetter, getterName(parentAttribute.getReverseReference(), "")));

        method(SEQ_ID, int.class).asPublic().override().return_(invoke("", getterName(dbObject.getPrimaryKey().get(1).getName(), "")));
    }

    protected void addInterfaces(String type, String keyType) {
        withSuperclass(generic(superClass(), className, primaryKeyType));
        if (dbObject.isDeprecable()) withInterfaces(generic(DEPRECABLE_INSTANCE, className, primaryKeyType));
        if (dbObject.isAuditable()) withInterfaces(AUDITABLE_INSTANCE);
        if (dbObject.hasImage()) withInterfaces(HAS_IMAGE_INSTANCE);
    }

    protected void addMyEntityTable() {
        doAddMyEntityTable(ENTITY_TABLE);
    }

    protected String column(@NotNull String column) {
        final String s = fromCamelCase(column);
        return tableSingleton + "." + (s.equals(extractName(tableSingleton)) ? s + "_" : s);
    }

    protected String defaultFor(final ClassGenerator cg, final TypeField field, boolean required) {
        return Generators.defaultFor(cg, field, required);
    }

    protected void doAddMyEntityTable(final String tableImpl) {
        final String forTable = invokeStatic(ENTITY_TABLE, "forTable", tableSingleton);
        method(MY_ENTITY_TABLE, generic(tableImpl, dbObject.getImplementationClassName(), primaryKeyType))  //
        .asPrivate()                                                                                        //
        .notNull()                                                                                          //
        .asStatic()                                                                                         //
        .return_(tableImpl.equals(ENTITY_TABLE) ? forTable : invokeStatic(CAST, forTable));
        method("et", generic(ENTITY_TABLE, className, primaryKeyType)).asPublic().notNull().return_(invoke("", MY_ENTITY_TABLE));

        method(MMCodeGenConstants.TABLE, generic(DB_TABLE, className, primaryKeyType)).asPublic().notNull().override().return_(tableSingleton);
    }

    protected boolean includeAttribute(Attribute attribute) {
        return !dbObject.isPrimaryKey(attribute);
    }

    protected void makeFinal(final String method) {
        method(method, className).asPublic().withAnnotation(OVERRIDE).asFinal().notNull().return_(invoke(refStatic(mainInterface, SUPER), method));
    }

    protected void populate() {
        asAbstract();
        if (dbObject.isInner()) {
            final Attribute parent     = dbObject.getPrimaryKey().getFirst().get();
            final String    parentKey  = makePrimaryKeyType(this, parent.asDatabaseObject().get());
            final String    parentType = parent.getImplementationClassName();
            withInterfaces(generic(mainInterface, className, primaryKeyType, parentType, parentKey));
            addInnerMethods(parent, parentType, parentKey);
        }
        else withInterfaces(generic(mainInterface, className, primaryKeyType));
        final String documentation = dbObject.getDocumentation();
        if (!documentation.isEmpty()) {
            for (final String d : documentation.split("\n"))
                withComments(d.trim());
            withComments("");
        }

        withComments("Generated base class for entity: " + dbObject.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(COMMON_SUPPRESSED_WARNINGS);

        addInterfaces(className, primaryKeyType);

        if (isDeprecatedCached()) generateConstructor();
        addAttributes();
        if (!dbObject.isInner() && !immutable) {
            addCreateMethods();
            if (!dbObject.hasDefaultPrimaryKey() && !dbObject.isView()) addFindOrCreateMethods();
        }
        // addTableField();
        addMyEntityTable();

        if (!dbObject.isProtected()) {
            if (!dbObject.getPrimaryKey().isEmpty()) addFindMethods();
            addListMethods();
            if (!immutable && !dbObject.isView()) addPersistenceMethods();
            addIndexMethods();
        }

        // if (!dbObject.getPrimaryKey().isEmpty()) addEqualsAndHashCode();
        addKeyAsString();
        addKeyObject();
        // addSearchBy();
        if (dbObject.hasImage()) addImage();

        addToStringAndDescribe();
        addHasChildren();
        addRowMapper();
        // addDeprecateMethods();
        addInvalidateMethod();
        super.populate();
    }  // end method populate

    @NotNull protected String refData(final String field) {
        return (data == null ? "this" : DATA_FIELD_NAME) + "." + field;
    }

    protected String referenceThisType(String var) {
        return extractName(className).equals(getName()) ? var : cast(className, var);
    }

    protected DbObject getDbObject() {
        return dbObject;
    }

    String abstractDataClass() {
        return generic(extractImport(superClass()) + ".AbstractData", className, primaryKeyType);
    }

    String addReference(String name, String value) {
        return references.put(name, value);
    }

    void createCopyTo() {
        final Method m = method(COPY_TO_METHOD_NAME, "T").asPackagePrivate().notNull().withGenerics(format("T extends %s", getName()));
        m.arg("to", "T").notNull();
        dbObject.attributes().flatMap(Attribute::retrieveSimpleFields).forEach(tf -> m.assign("to." + tf.getName(), tf.getName()));
        m.return_("to");
    }

    @NotNull String createMethodName() {
        return CREATE_METHOD;
    }

    String dataClassName() {
        return extractImport(className) + "." + DATA_CLASS_NAME;
    }

    @NotNull String getImmutableBase() {
        return getName();
    }

    private void addAttributes() {
        for (final Attribute attribute : dbObject.attributes())
            if (includeAttribute(attribute)) {
                final AttributeGenerator ag = createAttributeGenerator(attribute);
                ag.generate(target);
                if (!immutable) ag.generateMutableSetter(data != null);
            }
    }  // end method addAttributes

    @Nullable private DataGenerator addData() {
        if (!isDeprecatedCached()) return null;

        final DataGenerator dg = new DataGenerator(this);
        addInner(dg);
        final String dataType = dataClassName();
        field(DATA_FIELD_NAME, dataType).notNull().withValue(new_(dataType));

        addDataMethod("_" + DATA_METHOD, dataType).return_(DATA_FIELD_NAME);
        addDataMethod(DATA_METHOD, dataType).return_(cast(dataType, invoke(SUPER, DATA_METHOD)));
        return dg;
    }

    private Method addDataMethod(final String methodName, final String dataType) {
        return method(methodName, dataType).notNull().asProtected().asFinal();
    }

    private ImmutableList<Field> addDefaultPrimaryKeyAttr() {
        final ImmutableList<Attribute> primaryKey = dbObject.getPrimaryKey();
        if (primaryKey.isEmpty()) return ImmutableList.empty();

        final Attribute a            = primaryKey.getFirst().get();
        final String    type         = a.getImplementationClassName();
        final String    name         = a.getName();
        final Field     f            = target.field(name, type).required(true);
        final String    defaultValue = extractImport(ENTITY_TABLE) + "." + EMPTY_KEY_FIELD;

        if (data != null && data.isOpen()) f.asProtected();
        f.withValue(defaultValue);
        method(getterName(f.getName(), type), type).required(true).withGetterComments(name).return_(refData(name));

        target.method("hasEmptyKey", Boolean.TYPE).notNull().override().return_(name + " == " + defaultValue);

        return listOf(f);
    }

    // private void addDeprecateMethods() {
    // if (dbObject.isDeprecable()) {
    // final String dateTimeType = extractImport(DateTime.class);
    //
    // // Deprecate method
    // final Method m = method(DEPRECATE_METHOD, dateTimeType).asPublic();
    // m.withComments("Sets the instance deprecation status.");
    // m.arguments(listOf(new Argument(STATUS_ARG, BOOLEAN)));
    // m.return_(invokeOnTable(DEPRECATE_METHOD, referenceThisType(THIS), STATUS_ARG));
    // addIsDeprecatedMethod();
    // }
    // else if (dbObject.isView()) {
    // final View view = dbObject.asView();
    // if (view.isRemote() && view.getBaseEntity().isPresent() && view.getBaseEntity().get().isDeprecable()) addIsDeprecatedMethod();
    // }
    // }

    private void addFindMethods() {
        final String argName = hasCompositeKey() ? KEY_ARG : primaryKeyFields.get(0).getName();
        // Find by Key Object
        findMethod(FIND_METHOD).return_(invokeOnTable(FIND_METHOD, argName)).arg(argName, primaryKeyType).notNull();
        findMethod(FIND_OR_FAIL_METHOD).notNull().return_(invokeOnTable(FIND_OR_FAIL_METHOD, argName)).arg(argName, primaryKeyType).notNull();
        // Find Persisted by Key Object
        findMethod(FIND_PERSISTED_METHOD).return_(invokeOnTable(FIND_PERSISTED_METHOD, argName)).arg(argName, primaryKeyType).notNull();
        findMethod(FIND_PERSISTED_OR_FAIL_METHOD).notNull()
            .return_(invokeOnTable(FIND_PERSISTED_OR_FAIL_METHOD, argName))
            .arg(argName, primaryKeyType)
            .notNull();
        // Find by String
        if (!hasStringKey()) findMethod(FIND_METHOD).return_(invokeOnTable(FIND_BY_STRING_METHOD, KEY_ARG)).arg(KEY_ARG, String.class).notNull();

        if (hasCompositeKey()) {
            // Find by primitive key fields
            final Method find = findMethod(FIND_METHOD);
            for (final Field f : primaryKeyFields)
                find.arg(f.getName(), f.getType()).notNull();  // .required(!f.isNotNull()) : something nullable in the pk? and
            find.return_(invoke("", FIND_METHOD, primaryKeyObject()));

            // Extra Create method if any of the elements in the key is an instance
            final ImmutableCollection<Attribute> pk = dbObject.getPrimaryKey();
            if (pk.size() > 1 && pk.exists(ATTR_IS_ENTITY_NOT_STRING)) addFindOrCreateFromEntities(pk, findMethod(FIND_METHOD));
        }

        findMethod(FIND_WHERE_METHOD).return_(invoke(invoke(invokeStatic(SELECT_FROM_METHOD, tableSingleton), WHERE, CRITERIA_ARG), "get"))
            .arg(CRITERIA_ARG, CRITERIA_CLASS + "...")
            .notNull();
    }

    private void addFindOrCreateFromEntities(ImmutableCollection<Attribute> pk, Method method) {
        method.withComments("Based on String key for Entities");
        final List<String> args = new ArrayList<>();
        for (final Attribute a : pk)
            addFromEntitiesArg(args, method, a, a.asDatabaseObject(), a.getName());
        method.return_(invoke("", method.getName(), args));
    }

    private void addFindOrCreateMethods() {
        final String argName = hasCompositeKey() ? KEY_ARG : primaryKeyFields.get(0).getName();

        // Find by Key Object
        findOrCreateMethod().return_(invokeOnTable(FIND_OR_CREATE, argName)).arg(argName, primaryKeyType).notNull();
        // Find by String
        if (!hasStringKey()) findOrCreateMethod().return_(invokeOnTable(F_CREATE_BY_STRING_METHOD, KEY_ARG)).arg(KEY_ARG, String.class).notNull();

        if (hasCompositeKey()) {
            // Find by primitive key fields
            final Method find = findOrCreateMethod();
            for (final Field f : primaryKeyFields)
                find.arg(f.getName(), f.getType()).notNull();  // .required(!f.isNotNull()) : something nullable in the pk? and
            // calling tuple with is not null?
            find.return_(invoke("", FIND_OR_CREATE, primaryKeyObject()));
        }
    }

    private void addFromEntitiesArg(List<String> args, Method ce, Attribute a, Option<DbObject> e, String name) {
        if (e.isEmpty()) {
            ce.arg(name, a.getImplementationClassName()).notNull();
            args.add(name);
        }
        else {
            ce.arg(name, String.class).notNull();
            final Seq<String> types = types(this, e.get().retrieveSimpleFields());
            final int         size  = types.size();
            if (size == 1) args.add(invokeConvertFromString(name, types.getFirst().get()));
            else {
                final String parts = name + "Parts";
                ce.declare(STRING_ARRAY, parts, invokeStatic(Strings.class, SPLIT_TO_ARRAY, name, str(size)));
                int i = 0;
                for (final String t : types)
                    args.add(invokeConvertFromString(parts + "[" + i++ + "]", t));
            }
        }
    }  // end method addFromEntitiesArg

    private void addHasChildren() {  //

        dbObject.attributes()                                                        //
        .getFirst(a -> a != null && a.isMultiple() && dbObject.equals(a.getType()))  //
        .ifPresent(selfReference -> {
            withInterfaces(generic("tekgenesis.persistence.HasChildren", selfReference.getElementClassName()));
            final Method m = method(CHILDREN, generic(Seq.class, selfReference.getElementClassName())).notNull();
            m.return_(invokeGetter(selfReference));
        });
    }

    private void addImage() {
        final Method image = method(IMAGE_PATH_METHOD, String.class);
        image.notNull();
        image.withComments("Returns the image path build using the image resource field.");
        image.return_(
            invoke(extractImport("tekgenesis.common.util.Resources"), IMAGE_PATH_METHOD, dbObject.getAttribute(dbObject.image()).filter(a ->
                        a.hasOption(FieldOption.ABSTRACT)).isPresent() ? getterName(dbObject.image()) + "()" : refData(dbObject.image())));
    }

    private void addIndexMethod(boolean list, String indexName, final int indexId, Seq<Attribute> index, String returnType) {
        final ImmutableList<TypeField> keyFields = retrieveSimpleFields(index);

        final MethodBase<Method> method = method((list ? LIST_BY_METHOD : FIND_BY_METHOD) + capitalizeFirst(indexName), returnType)  //
                                          .withComments(
                    format(list ? "List the instances" + " of '%s' that matches the given parameters." : "Finds the instance", dbObject.getName()))
                                          .asStatic()
                                          .required(list)                                              //
                                          .arguments(makeArguments(keyFields, true));

        if (list) {
            final Seq<String> conditions = keyFields.map(a -> invoke(column(a.getName()), "eq", a.getName()));
            method.return_(invoke(invoke(invokeStatic(SELECT_FROM_METHOD, tableSingleton), WHERE, conditions), "list"));
        }
        else {
            final String key = keyFields.size() == 1 ? keyFields.get(0).getName()
                                                     : invokeStatic(Tuple.class, tupleMethod(keyFields.size()), keyFields.map(TypeField::getName));
            method.return_(invokeOnTable("findByKey", String.valueOf(indexId), key));
        }
    }

    private void addIndexMethods() {
        int uniqueId = 0;
        for (final String name : dbObject.getUniqueIndexNames())
            addIndexMethod(false, name, uniqueId++, dbObject.getUniqueIndexByName(name), className);
        int indexId = 0;
        for (final String name : dbObject.getIndexNames())
            addIndexMethod(true, name, indexId++, dbObject.getIndexByName(name), generic(ImmutableList.class, className));
    }

    private void addInvalidateMethod() {
        if (!references.isEmpty()) {
            final Method invalidateMethod = method(INVALIDATE).override();
            for (final String attr : references.keySet())
                invalidateMethod.statement(invoke(attr, INVALIDATE));
        }
    }

    private void addKeyAsString() {
        final String ret;
        if (hasCompositeKey()) {
            final StrBuilder b = new StrBuilder().startCollection(" + \":\" + ");
            for (final Field k : primaryKeyFields) {
                final String name = k.getName();
                b.appendElement(k.isString() ? invokeStatic(Strings.class, ESCAPE_CHAR_ON, name, "':'") : name);
            }
            ret = b.toString();
        }
        else if (primaryKeyFields.isEmpty()) {
            target.method(KEY_AS_STRING_METHOD, String.class).notNull().throwNew(UnsupportedOperationException.class);
            return;
        }
        else {
            final String keyName = primaryKeyFields.get(0).getName();
            ret = hasStringKey() ? keyName : invokeStatic(String.class, VALUE_OF, keyName);
        }
        target.method(KEY_AS_STRING_METHOD, String.class).notNull().return_(ret);
    }  // end method addKeyAsString

    private void addKeyObject() {
        target.method(KEY_OBJECT_METHOD, primaryKeyType).boxedNotNull().return_(primaryKeyObject());
    }

    private void addListenerMethod(final String listenerMethod, final String comment) {
        final Method m = method(listenerMethod, VOID).asStatic()
                         .asPublic()
                         .withComments(comment)
                         .statement(invokeOnTable(listenerMethod, LISTENER_TYPE, LISTENER));

        m.arg(LISTENER_TYPE, ENTITY_LISTENER_TYPE).notNull();
        m.arg(LISTENER, generic(ENTITY_LISTENER, className)).notNull();
    }

    private void addListMethods() {
        final String name = dbObject.getName();

        final String listComment = format("Create a selectFrom(%s).", tableSingleton);
        final String selectFrom  = invokeStatic(SELECT_FROM_METHOD, tableSingleton);

        method(LIST_METHOD, generic(SELECT, className)).asStatic().asPublic().notNull().return_(selectFrom).withComments(listComment);

        method(FOR_EACH, VOID).asStatic().asPublic().withComments(format("Performs the given action for each %s", name))  //
        .statement(invoke(selectFrom, FOR_EACH, CONSUMER))                                                                //
        .arg(CONSUMER, generic(Consumer.class, className));

        final String keys    = KEY_ARG + "s";
        final String comment = format("List instances of '%s' with the specified keys.", name);

        if (!hasStringKey())
            listMethod()                                          //
            .return_(invokeOnTable(LIST_METHOD, keys)).notNull()  //
            .withComments(comment).arg(keys, generic(Set.class, primaryKeyType));

        listMethod()                                                       //
        .return_(invokeOnTable(LIST_FROM_STRINGS_METHOD, keys)).notNull()  //
        .withComments(comment).arg(keys, generic(Iterable.class, String.class));

        method(LIST_WHERE_METHOD, generic(SELECT, className)).asStatic()
            .asPublic()                                                                                     //
            .return_(invoke(selectFrom, WHERE, CRITERIA_ARG))
                .notNull()
                .withComments(format("List the instances of '%s' that verify the specified condition.", name))  //
            .arg(CRITERIA_ARG, CRITERIA_CLASS)
                .notNull();
        }

        private void addPersistenceMethods() {
            if (hasSingleDefaultPrimaryKey()) {
                final String id = primaryKeyFields.get(0).getName();
                final Method m  = method(INSERT_METHOD).asPublic().withComments("Insert specifying the primary key");
                m.arg(KEY_ARG, int.class);
                m.assign(refData(id), KEY_ARG);
                m.statement(invokeOnTable(INSERT_DO_NOT_GENERATE, referenceThisType(THIS)));
                if ("TRUE".equals(System.getenv("MAKE_FINAL"))) {
                    makeFinal(UPDATE_METHOD);
                    makeFinal(INSERT_METHOD);
                }
            }
            else {
                makeFinal(UPDATE_METHOD);
                makeFinal(INSERT_METHOD);
            }

            addListenerMethod(ADD_LISTENER, "Register a Listener");
            addListenerMethod(REMOVE_LISTENER, "Remove a Listener");
        }

        private ImmutableList<Field> addPrimaryKeyAttributes() {  //
            return ImmutableList.build(b ->
                    dbObject.getPrimaryKey()                  //
                    .forEach(a -> createAttributeGenerator(a).generatePk(b, target)));
        }

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private void addRowMapper() {
            method(ROW_MAPPER_METHOD, generic(ROW_MAPPER_CLASS, className)).asStatic()
                .asPublic()
                .notNull()
                .withComments("Gets mapper for SQL statements")
                .return_(invoke(invoke(tableSingleton, "metadata"), "getRowMapper"));
        }

        private void addSearchBy() {
            if (!dbObject.isSearchable()) return;  // not searchable

            final Method forceIndex = method(INDEX_METHOD).asPublic();
            forceIndex.statement(invokeOnTable(INDEX_METHOD, referenceThisType(THIS)));
        }

        private void addToStringAndDescribe() {
            final Seq<ModelField> describes = dbObject.describes();
            if (!describes.isEmpty()) {
                final StringBuilder describeFields = new StringBuilder();
                final StringBuilder toStringFields = new StringBuilder();

                for (final ModelField a : describes) {
                    if (describeFields.length() != 0) describeFields.append(", ");
                    describeFields.append(invoke(getterName(a.getName(), a.getType().getImplementationClassName())));

                    final boolean first = toStringFields.length() == 0;
                    if (first) toStringFields.append(EMPTY);
                    toStringFields.append(CAT);
                    final String getterName = getterName(a.getName(), a.getType().getImplementationClassName()) + "()";
                    final String getter     = a.getType().isEnum() ? invoke(getterName, LABEL_FIELD) : getterName;
                    final String g          = first ? getter : CodeGeneratorConstants.SPACE + CAT + getter;
                    if (isAttrRequired(a)) toStringFields.append(g);
                    else toStringFields.append("(").append(getterName).append("==null ? ").append(EMPTY).append(" : ").append(g).append(")");
                }

                method(DESCRIBE, generic(Seq.class, String.class)).asPublic()
                    .notNull()
                    .asFinal()
                    .override()
                    .return_(invoke("", extractStaticImport(Conversions.class, "formatList"), describeFields.toString()));

                method(TO_STRING, String.class).asPublic().notNull().override().return_(toStringFields.toString());
            }
        }

        @NotNull private AttributeGenerator createAttributeGenerator(Attribute attribute) {
            final AttributeGenerator ag = new AttributeGenerator(this, className, attribute);
            if (data != null) {
                ag.setClassForField(DATA_FIELD_NAME);
                ag.setFieldAsProtected();
            }
            return ag;
        }

        private Method createMethod() {
            final String comment = format("Creates a new %s instance.", link(className));
            final Method method  = method(createMethodName(), className).asStatic().notNull().withComments(comment);
            if (dbObject.isView()) method.asProtected();
            else method.asPublic();
            return method;
        }

        private Method findMethod(String methodName) {
            final Method method = method(methodName, className).asStatic().asPublic();

            method.withComments(format("Try to finds an Object of type '%s' in the database.", dbObject.getName()))
                .withComments(methodName.equals(FIND_WHERE_METHOD) ? "That verifies the specified condition." : IDENTIFIED_BY_KEY);
            if (methodName.equals(FIND_PERSISTED_METHOD) || methodName.equals(FIND_PERSISTED_OR_FAIL_METHOD))
                method.withComments("Ignoring caches and accessing the database");
            if (methodName.equals(FIND_OR_FAIL_METHOD) || methodName.equals(FIND_PERSISTED_OR_FAIL_METHOD)) method.withComments(FAIL_IF_NOT_PRESENT);
            else method.withComments(NULL_IF_NOT_PRESENT);
            return method;
        }

        private Method findOrCreateMethod() {
            return method(FIND_OR_CREATE, className).asStatic().asPublic().notNull()  //
                   .withComments(format("Find (or create if not present) a '%s' in the database.", dbObject.getName()), IDENTIFIED_BY_KEY);
        }

        private void generateConstructor() {
            final String getData = format("i -> ((%s) i)._data", getName());
            final String setData = format("(i,d) -> ((%s) i)._data = (%s) d", getName(), dataClassName());
            constructor().asProtected().invokeSuper(getData, setData);
        }

        private boolean hasCompositeKey() {
            return primaryKeyFields.size() > 1;
        }

        private boolean hasSingleDefaultPrimaryKey() {
            return dbObject.hasDefaultPrimaryKey() && !dbObject.hasCompositePrimaryKey();
        }

        private boolean hasStringKey() {
            return !hasCompositeKey() && !primaryKeyFields.isEmpty() && primaryKeyFields.get(0).getType().equals(String.class.getSimpleName());
        }

        private String invokeGetter(Attribute attribute) {
            return invoke("", getterName(attribute.getName(), attribute.getElementClassName()));
        }

        private String invokeOnTable(String method, String... args) {
            return invoke(invoke("", MY_ENTITY_TABLE), method, args);
        }

        private Method listMethod() {
            return method(LIST_METHOD, generic(ImmutableList.class, className)).asStatic().asPublic().notNull();
        }

        private String primaryKeyObject() {
            if (primaryKeyFields.isEmpty()) return "new Object()";
            final ImmutableList<TypeField> fields = dbObject.retrieveSimpleFields();
            return fields.size() == 1 ? fields.get(0).getName()
                                      : invokeStatic(Tuple.class, tupleMethod(fields.size()), fields.map(TypeField::getName));
        }

        private String str(int n) {
            return String.valueOf(n);
        }

        @NotNull private String superClass() {
            return PERSISTENCE_PACKAGE +
                   (isDeprecatedCached() ? "CachedEntityInstanceImpl" : immutable ? "EntityInstanceBaseImpl" : "EntityInstanceImpl");
        }

        private boolean isAttrRequired(ModelField f) {
            if (!(f instanceof Attribute)) return true;
            final Attribute a = (Attribute) f;
            return a.isRequired() || dbObject.isPrimaryKey(a);
        }

        private boolean isDeprecatedCached() {
            final CacheType cacheType = dbObject.getCacheType();
            return cacheType.isDefined() && !cacheType.isFull();
        }

        //~ Methods ......................................................................................................................................

        /** Make primary key type. */
        public static String makePrimaryKeyType(ClassGenerator cg, DbObject object) {
            final Seq<TypeField> pkFields = object.retrieveSimpleFields();
            final Seq<String>    pkTypes  = types(cg, pkFields);

            final int size = pkFields.size();
            if (size == 0) return "Object";
            if (size == 1) return pkTypes.getFirst().get();
            String tuple = Tuple.class.getName();
            if (size > 2) tuple += size;
            return cg.generic(tuple, pkTypes);
        }

        /** Returns the qualified java class name to the base class for the given model table. */
        public static String getBaseTableClassName(@NotNull final MetaModel model) {
            return model.getFullName() + BASE + "." + TABLE_CLASS_NAME + BASE;
        }

        static Seq<String> types(final ClassGenerator cg, Seq<TypeField> pkFields) {
            return pkFields.map(value -> cg.extractImport(wrapIfNeeded(value.getImplementationClassName())));
        }

        //~ Static Fields ................................................................................................................................

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private static final String SEQ_ID = "seqId";

        @SuppressWarnings("DuplicateStringLiteralInspection")
        public static final String PARENT = "parent";

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private static final String CHILDREN = "children";

        @NonNls private static final String CONSUMER = "consumer";

        static final String INVALIDATE = "invalidate";

        private static final QName CAST = createQName(PREDEFINED_CLASS, "cast");

        protected static final String MY_ENTITY_TABLE = "myEntityTable";

        private static final Predicate<Attribute> ATTR_IS_ENTITY_NOT_STRING = new Predicate<Attribute>() {
                @Override public boolean test(@Nullable Attribute a) {
                    boolean result = false;
                    if (a != null) {
                        for (final DbObject e : a.asDatabaseObject())
                            result = e.hasCompositePrimaryKey() || !checkSingleStringPK(a);
                    }
                    return result;
                }

                private boolean checkSingleStringPK(final Attribute a) {
                    boolean result = a.getType().getFinalType().isString();
                    for (final DbObject e : a.asDatabaseObject())
                        result = checkSingleStringPK(e.getPrimaryKey().getFirst().get());
                    return result;
                }
            };

        private static final String EMPTY_KEY_FIELD = "DEFAULT_EMPTY_KEY";

        @NonNls private static final String IDENTIFIED_BY_KEY = "Identified by the primary key.";

        //~ Inner Classes ................................................................................................................................

        static class DataGenerator extends ClassGenerator {
            DataGenerator(final EntityBaseCodeGenerator parent) {
                super(parent, "Open" + DATA_CLASS_NAME);
                withSuperclass(parent.abstractDataClass()).asStatic().asProtected().suppressWarnings(FIELD_MAY_BE_FINAL);
            }

            public boolean isOpen() {
                return true;
            }
        }
    }  // end class EntityBaseCodeGenerator
