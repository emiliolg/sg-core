
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.View;
import tekgenesis.type.*;
import tekgenesis.type.Kind;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.entity.EntityBaseCodeGenerator.makePrimaryKeyType;
import static tekgenesis.codegen.entity.EntityBaseCodeGenerator.types;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.collections.Maps.hashMap;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.field.TypeField.retrieveSimpleFields;
import static tekgenesis.type.Kind.*;

/**
 * ClassGenerator to generate the QueryDsl Table for a DatabaseObject.
 */
public class DbTableCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final Constructor constructor;

    @NotNull protected final DbObject dbObject;
    @NotNull protected final String   entityClass;
    @NotNull protected final String   keyType;
    @NotNull private final String     singleton;

    //~ Constructors .................................................................................................................................

    /** Construct a new ClassGenerator for the given Entity. */
    public DbTableCodeGenerator(JavaCodeGenerator cg, @NotNull final DbObject dbObj) {
        super(cg, getDbTableName(dbObj));
        dbObject    = dbObj;
        entityClass = dbObj.getImplementationClassName();
        keyType     = makePrimaryKeyType(this, dbObj);

        constructor = constructor().invokeSuper(superConstructorArgs()).asPrivate();
        asPublic().withSuperclass(generic(DB_TABLE, entityClass, keyType));

        withComments("Metadata class for table associated to entity " + entityClass);
        suppressWarnings(DUPLICATED_STRING, MAGIC_NUMBER);

        singleton = singletonName(dbObject).getName();
        final String className = getName();

        field(singleton, className).asPublic().asFinal().notNull().asStatic().withValue(new_(className));

        final List<TypeField> typeFields = collectFields();
        for (final TypeField field : typeFields)
            addTableField(field);
        addPrimaryKey();
        addSecondaryKeys();
        addKeyFromString();
        if (dbObject.isSearchable()) createSearchable();

        final Method as = method("as", className).asPublic().asFinal().override().notNull();
        as.arg(ALIAS, String.class).notNull();
        as.return_(invoke("", "createAlias", new_(className), ALIAS));

        final Method cet = method("createEntityTable", generic(ENTITY_TABLE, entityClass, keyType)).asProtected().asFinal().override().notNull();

        cet.return_(createEntityTableBody());
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return dbObject.getSourceName();
    }

    protected void addTableField(TypeField typeField) {
        if (typeField.hasOption(FieldOption.ABSTRACT)) return;

        final Type finalType = typeField.getFinalType();
        final Kind kind      = finalType.getKind();

        final String fieldType    = classFor(typeField, this);
        String       createMethod = kind != ENUM ? fieldMethod.get(fieldType) : typeField.isMultiple() ? "enumSetField" : ENUM_METHOD;

        final Collection<String> args = new ArrayList<>();
        args.add(quoted(typeField.getName()));
        args.add(quoted(getColumnName(typeField)));
        if (supportsSign.contains(kind)) args.add(String.valueOf(typeField.getOptions().hasOption(FieldOption.SIGNED)));
        if (supportsLength.contains(kind)) args.add(String.valueOf(finalType.getLength().get()));
        if (finalType instanceof StringType && ((StringType) finalType).isIntern()) createMethod = "strInternField";
        if (finalType instanceof DecimalType) args.add(String.valueOf(((DecimalType) finalType).getDecimals()));
        else if (finalType instanceof EnumType) args.add(classOf(typeField.getImplementationClassName()));

        final String fieldName = fieldName(typeField);
        constructor.assign(fieldName, invoke("", createMethod, args));
        field(fieldName, fieldType).asPublic().asFinal().notNull();
    }  // end method addTableField

    protected List<TypeField> collectFields() {
        final List<TypeField> fields = new ArrayList<>();
        if (dbObject.isView()) {
            final View             view       = dbObject.asView();
            final Option<DbObject> baseEntity = view.getBaseEntity();

            if (baseEntity.isPresent() && isEmpty(view.getAsQuery())) {
                for (final Attribute attribute : baseEntity.get().getPrimaryKey()) {
                    if (view.getAttributeByBaseAttributeName(attribute.getName()).isEmpty()) {
                        for (final TypeField typeField : attribute.retrieveSimpleFields())
                            fields.add(typeField);
                    }
                }
            }
        }
        for (final Attribute a : dbObject.attributes()) {
            if (!a.hasOption(FieldOption.ABSTRACT)) for (final TypeField typeField : a.retrieveSimpleFields())
                fields.add(typeField);
        }
        return fields;
    }

    protected String createEntityTableBody() {
        return new_((isInner() ? ENTITY_TABLE_INNER : ENTITY_TABLE) + "<>", singleton);
    }

    protected String getColumnName(TypeField typeField) {
        return typeField.getColumnName();
    }

    private void addKeyFromString() {
        final Method m = method(STR_TO_KEY, keyType).asProtected().boxedNotNull();
        m.arg(KEY_ARG, String.class).notNull();

        final Seq<String> types = types(this, dbObject.retrieveSimpleFields());
        final int         size  = types.size();
        if (size == 0) m.throwNew(UnsupportedOperationException.class);
        else if (size == 1) m.return_(invokeConvertFromString(KEY_ARG, types.getFirst().get()));
        else {
            final Collection<String> args = new ArrayList<>();
            int                      i    = 0;
            for (final String t : types)
                args.add(invokeConvertFromString(PARTS + "[" + i++ + "]", t));
            m.declare(STRING_ARRAY, PARTS, invokeStatic(Strings.class, SPLIT_TO_ARRAY, KEY_ARG, String.valueOf(size)));
            m.return_(invokeStatic(Tuple.class, tupleMethod(args.size()), args));
        }
    }  // end method addKeyFromString

    private void addPrimaryKey() {
        constructor.statement(invoke("", PRIMARY_KEY_METHOD, invokeListOf(dbObject.retrieveSimpleFields().map(this::fieldName))));
    }
    private void addSecondaryKeys() {
        final Seq<String> indexNames = dbObject.getUniqueIndexNames();
        if (!indexNames.isEmpty()) {
            final Seq<String> keys = indexNames.map(this::secondaryKey);
            constructor.statement(invoke("", "secondaryKeys", invokeListOf(keys)));
        }
    }

    private String cacheArg() {
        final CacheType cacheType = dbObject.getCacheType();
        final String    field     = cacheType == CacheType.DEFAULT
                                    ? DEFAULT
                                    : cacheType == CacheType.FULL ? "FULL" : cacheType == CacheType.NONE ? "NONE" : null;
        return field == null ? invoke(refStatic(CacheType.class, DEFAULT), "withSize", String.valueOf(cacheType.getSize()))
                             : refStatic(CacheType.class, field);
    }

    private void createSearchable() {
        final String searcherClass = dbObject.getFullName() + SEARCHER_SUFFIX;
        final String searcherField = fromCamelCase(dbObject.getName() + SEARCHER_SUFFIX);
        method(SEARCHER, generic(Option.class, searcherClass)).asProtected()
            .notNull()
            .override()
            .return_(invokeStatic(Option.class, "of", refStatic(searcherClass, searcherField)));
    }

    @NotNull private String fieldName(TypeField typeField) {
        final String f = fromCamelCase(typeField.getName());
        return f.equals(singleton) ? f + "_" : f;
    }
    private boolean hasGeneratedKey() {
        return !isInner() && dbObject.hasDefaultPrimaryKey();
    }

    private String modifiers() {
        final EnumSet<Modifier> modifiers = dbObject.getModifiers();
        return modifiers.isEmpty() ? refStatic(Modifier.class, "NONE")
                                   : invokeStatic(EnumSet.class, "of", map(modifiers, m -> refStaticImport(Modifier.class, m.name())));
    }

    private String secondaryKey(String indexName) {
        return invokeListOf(retrieveSimpleFields(dbObject.getUniqueIndexByName(indexName)).map(this::fieldName));
    }

    private List<String> superConstructorArgs() {
        final List<String> superArgs = new ArrayList<>();
        final QName        tableName = dbObject.getTableName();
        superArgs.add(classOf(entityClass));
        superArgs.add(quoted(tableName.getQualification()));
        superArgs.add(quoted(tableName.getName()));
        superArgs.add(quoted(hasGeneratedKey() ? dbObject.getPrimaryKey().getFirst().get().getSequenceName() : ""));
        superArgs.add(modifiers());
        superArgs.add(cacheArg());
        return superArgs;
    }

    private boolean isInner() {
        return dbObject.getParent().isPresent();
    }

    //~ Methods ......................................................................................................................................

    /** Get db table class name. */
    public static String getDbTableName(final DbObject dbObj) {
        return dbObj.getName() + TABLE_CLASS_NAME;
    }

    static String classFor(final TypeField typeField, JavaItemGenerator<?> cg) {
        final Type finalType = typeField.getFinalType();
        if (finalType.isString()) {
            if (((StringType) finalType).useClob()) return CLOB_FIELD;
        }
        else if (finalType instanceof EnumType) {
            final String enumType = typeField.getImplementationClassName();
            final String clazz    = typeField.isMultiple() ? ENUMERATION_SET_FIELD : ENUMERATION_FIELD;
            return cg.generic(clazz, enumType, ((EnumType) finalType).getPkType().getImplementationClassName());
        }
        else if (Long.class.equals(finalType.getImplementationClass())) return LONG_FIELD;
        final Kind kind = finalType.getKind();
        return fieldClass.get(kind);
    }

    static QName singletonName(final DbObject ref) {
        String name = fromCamelCase(ref.getName());
        if (name.equals(ref.getName())) name += "_";
        return createQName(createQName(ref.getDomain() + ".g", getDbTableName(ref)).getFullName(), name);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SEARCHER = "searcher";

    private static final EnumSet<Kind>         supportsLength = EnumSet.of(INT, DECIMAL, STRING);
    private static final EnumSet<Kind>         supportsSign   = EnumSet.of(INT, REAL, DECIMAL);
    private static final EnumMap<Kind, String> fieldClass     = enumMap(tuple(INT, INT_FIELD),
            tuple(REAL, REAL_FIELD),
            tuple(DECIMAL, DECIMAL_FIELD),
            tuple(STRING, STRING_FIELD),
            tuple(Kind.RESOURCE, RESOURCE_FIELD),
            tuple(BOOLEAN, BOOLEAN_FIELD),
            tuple(DATE, DATE_FIELD),
            tuple(DATE_TIME, DATE_TIME_FIELD),
            tuple(ENUM, ENUMERATION_FIELD));

    private static final Map<String, String> fieldMethod = hashMap(tuple(INT_FIELD, INT_METHOD),
            tuple(REAL_FIELD, REAL_METHOD),
            tuple(DECIMAL_FIELD, DECIMAL_METHOD),
            tuple(STRING_FIELD, STR_METHOD),
            tuple(RESOURCE_FIELD, "resourceField"),
            tuple(BOOLEAN_FIELD, BOOL_METHOD),
            tuple(DATE_FIELD, DATE_METHOD),
            tuple(DATE_TIME_FIELD, "dTimeField"),
            tuple(CLOB_FIELD, "clobField"),
            tuple(LONG_FIELD, LONG_METHOD));

    // Cache types
    private static final String DEFAULT = "DEFAULT";
}  // end class DbTableCodeGenerator
