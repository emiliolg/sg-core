
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.*;
import tekgenesis.type.*;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Maps.linkedHashMap;
import static tekgenesis.common.core.Constants.ID;
import static tekgenesis.common.core.Constants.SEQ_ID;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.md.MdConstants.*;
import static tekgenesis.type.FieldReference.unresolvedFieldRef;
import static tekgenesis.type.Modifier.*;
import static tekgenesis.type.Types.*;

/**
 * Collects the data to build an {@link Entity}.
 */
@SuppressWarnings({ "UnusedReturnValue", "ClassWithTooManyMethods" })
public class EntityBuilder extends DbObjectBuilder<Entity, Attribute, AttributeBuilder, EntityBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private CacheType cacheType = CacheType.NONE;

    @NotNull private final List<ModelField> describeFields;

    private List<ModelField> image;
    private final boolean    inner;

    //~ Constructors .................................................................................................................................

    EntityBuilder(String sourceName, @NotNull String pkg, @NotNull String name, String parent, @NotNull String defaultSchemaId) {
        super(sourceName, pkg, name, defaultSchemaId);
        describeFields = new ArrayList<>();
        inner          = !parent.isEmpty();
        if (inner) addInnerAttributes(parent);
        primaryKeyDefault = true;
        image             = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public void addField(final AttributeBuilder a)
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        if (fields.isEmpty() && primaryKeyDefault) {
            assert primaryKeyFields.isEmpty();
            primaryKeyFields.add(unresolvedFieldRef((a.isSerial() ? a : addInternalField(ID, false)).getName()));
        }
        checkValidName(a);
        if (a.getType().isArray() && !a.hasOption(FieldOption.ABSTRACT)) throw new InvalidTypeException(getId(), a.getName(), a.getType());
        super.addField(a);
    }

    /**
     * Toggles the entity as auditable.
     *
     * @return  the same EntityBuilder
     */
    public EntityBuilder auditable() {
        return withModifier(AUDITABLE);
    }

    @Override public Entity build()
        throws BuilderException
    {
        final Map<String, TypeField> fieldMap     = new LinkedHashMap<>();
        final Map<String, Attribute> attributeMap = cast(fieldMap);

        final List<Attribute>                       pks       = new ArrayList<>();
        final List<ModelField>                      describes = new ArrayList<>();
        final List<SearchField>                     searchBy  = new ArrayList<>();
        final LinkedHashMap<String, Seq<Attribute>> uniqueMap = new LinkedHashMap<>();
        final Map<String, Seq<Attribute>>           indexMap  = new LinkedHashMap<>();

        final boolean protectedEntity = modifiers.contains(PROTECTED);
        final Entity  entity          = new Entity(sourceName,
                domain,
                id,
                label,
                getDatabaseName(),
                dbNameGenerated,
                modifiers,
                primaryKeyDefault,
                defaultForm,
                attributeMap,
                pks,
                describes,
                uniqueMap,
                indexMap,
                searchBy,
                cacheType,
                protectedEntity,
                image,
                documentation);

        addInternalFields();

        buildAttributes(fieldMap, entity);

        buildPrimaryKey(attributeMap, pks);
        buildDescribes(attributeMap, describes);
        buildIndexes(uniqueIndexes, attributeMap, uniqueMap);
        buildIndexes(indexes, attributeMap, indexMap);

        buildSearchByFields(searchByFields, attributeMap, searchBy, true);

        return entity;
    }  // end method build

    /**
     * Marks the entity as cached.
     *
     * @return  the same EntityBuilder
     */
    public EntityBuilder cached() {
        cacheType = CacheType.DEFAULT;
        return this;
    }
    /**
     * Marks the entity as cached with the specified size.
     *
     * @return  the same EntityBuilder
     */
    public EntityBuilder cached(int size) {
        cacheType = CacheType.DEFAULT.withSize(size);
        return this;
    }

    @NotNull public List<BuilderError> check() {
        if (isEmpty(fields.entrySet())) builderErrors.add(new NoAttributesException(id));
        if (!inner && describeFields.isEmpty()) describeFields.addAll(primaryKeyFields);
        checkIndex(primaryKeyFields, true);
        for (final Collection<ModelField> f : uniqueIndexes.values())
            checkIndex(f, false);
        for (final Collection<ModelField> f : indexes.values())
            checkIndex(f, false);
        return builderErrors;
    }

    /** Toggles the entity as deprecable. */
    public EntityBuilder deprecable() {
        return withModifier(DEPRECABLE);
    }

    /** Sets the fields that describe the entity. */
    public EntityBuilder describedBy(Collection<ModelField> mf) {
        describeFields.clear();
        describeFields.addAll(mf);
        return this;
    }

    /** Sets the fields that describe the entity. */
    public EntityBuilder describedBy(String... fs) {
        return describedBy(convertToModelFields(fs));
    }

    /** Toggles the entity as fully cached. */
    public EntityBuilder fullyCached() {
        cacheType = CacheType.FULL;
        return this;
    }

    /** Sets the fields that act as image of the view. */
    public EntityBuilder image(ModelField img) {
        image = new ArrayList<>();
        image.add(img);
        return this;
    }

    /** Toggles the entity as optimistic. */
    public EntityBuilder optimistic() {
        return withModifier(OPTIMISTIC_LOCKING);
    }

    /** Sets the entity primary key. */
    public EntityBuilder primaryKey(String... fs) {
        return cast(super.primaryKey(convertToModelFields(fs)));
    }

    /** Add the primary key based on column names. */
    public EntityBuilder primaryKeyFromColumnNames(Seq<String> columnNames) {
        final ArrayList<ModelField> fs = new ArrayList<>();
        for (final String s : columnNames)
            fs.add(unresolvedFieldRef(idFromDbName(s)));
        return primaryKey(fs);
    }

    boolean allowOptionalInPrimaryKey() {
        return false;
    }

    void checkValidName(AttributeBuilder a)
        throws InvalidFieldNameException
    {
        final Internal it = internalFields.get(a.getName());
        if (it == null) return;

        final Type type = a.getType().getFinalType();
        if (it.identity) {
            if (type != Types.intType() || a.isSerial()) return;
        }
        else {
            if (type.equals(it.type) && (it.optional || !a.isOptional())) return;
        }
        throw new InvalidFieldNameException(a.getName(), getId());
    }

    // Call from constructor
    private void addInnerAttributes(String parent) {
        final String parentFieldName = deCapitalizeFirst(parent);
        try {
            addField(createAttribute(parentFieldName, Types.referenceType(domain, parent)));
            addField(createAttribute(SEQ_ID, Types.intType()));
            primaryKey(parentFieldName, SEQ_ID);
        }
        catch (final BuilderException e) {
            throw new IllegalStateException(e);
        }
    }

    private AttributeBuilder addInternalField(final String name, boolean readOnly) {
        final Internal         t = internalFields.get(name);
        final AttributeBuilder a = createAttribute(name, t.type).synthesized();
        try {
            if (t.optional) a.optional();
            if (readOnly) a.readOnly();
        }
        catch (final BuilderException e) {
            throw new IllegalStateException(e);
        }
        if (t.identity) a.serial();
        addInternalField(a);
        return a;
    }

    private void addInternalFields() {
        addInternalField(UPDATE_TIME, true);
        if (hasModifier(OPTIMISTIC_LOCKING)) addInternalField(VERSION_FIELD, true);
        if (hasModifier(DEPRECABLE)) {
            addInternalField(DEPRECATION_TIME, true);
            addInternalField(DEPRECATION_USER, true);
        }
        if (hasModifier(AUDITABLE)) {
            addInternalField(CREATION_TIME, true);
            addInternalField(CREATION_USER, true);
            addInternalField(UPDATE_USER, true);
        }
    }

    private void buildDescribes(Map<String, Attribute> map, Collection<ModelField> describes) {
        if (describeFields.isEmpty()) addAllFields(map, primaryKeyFields, describes);
        else addAllFields(map, describeFields, describes);
    }

    private void buildPrimaryKey(Map<String, Attribute> map, Collection<Attribute> pks) {
        addFieldsByName(map, primaryKeyFields, pks);
    }

    private void checkIndex(Collection<ModelField> fs, boolean pk) {
        for (final ModelField field : fs) {
            final String           fieldName = field.getName();
            final AttributeBuilder attribute = fields.get(fieldName);
            if (attribute == null) {
                if (!fieldName.equals(UPDATE_TIME) && !(hasModifier(AUDITABLE) && auditFields.contains(fieldName)) &&
                    !(hasModifier(DEPRECABLE) && deprecableFields.contains(fieldName)))
                    builderErrors.add(new FieldNotFoundException(getId(), fieldName));
            }
            else if (pk && attribute.isOptional() && !allowOptionalInPrimaryKey())
                builderErrors.add(new NullableInPrimaryKeyException(getFullName(), attribute.getName()));
        }
    }  // end method checkIndex

    @NotNull private String idFromDbName(String dbName) {
        return deCapitalizeFirst(toCamelCase(dbName));
    }

    //~ Methods ......................................................................................................................................

    /** Creates an attribute builder for a {@link BooleanType }. */
    public static AttributeBuilder bool(String nm) {
        return new AttributeBuilder(nm, Types.booleanType());
    }

    /** Builds an Attribute of the specified Type. */
    public static AttributeBuilder createAttribute(String nm, Type type) {
        return new AttributeBuilder(nm, type);
    }

    /** Creates an attribute builder for a {@link DateOnlyType }. */
    public static AttributeBuilder date(String nm) {
        return new AttributeBuilder(nm, Types.dateType());
    }

    /** Creates an attribute builder for a {@link DateTimeType }. */
    public static AttributeBuilder dateTime(String nm) {
        return new AttributeBuilder(nm, dateTimeType());
    }

    /** Creates an attribute builder for a {@link DateTimeType }. */
    public static AttributeBuilder dateTime(String nm, int precision) {
        return new AttributeBuilder(nm, dateTimeType(precision));
    }

    /** Creates an attribute builder for a {@link DecimalType }. */
    public static AttributeBuilder decimal(String nm, int precision) {
        return new AttributeBuilder(nm, Types.decimalType(precision));
    }

    /** Creates an attribute builder for a {@link DecimalType }. */
    public static AttributeBuilder decimal(String nm, int precision, int decimals) {
        return new AttributeBuilder(nm, Types.decimalType(precision, decimals));
    }

    /** Creates an EntityBuilder. */
    public static EntityBuilder entity(final String packageId, final String entityName) {
        return new EntityBuilder("", packageId, entityName, "", "");
    }

    /** Creates an EntityBuilder. */
    public static EntityBuilder entity(final String packageId, final String entityName, final String schemaId) {
        return new EntityBuilder("", packageId, entityName, "", schemaId);
    }

    /** Creates an EntityBuilder. */
    public static EntityBuilder entity(String sourceName, final String packageId, final String entityName, final String parent,
                                       final String schemaId) {
        return new EntityBuilder(sourceName, packageId, entityName, parent, schemaId);
    }

    /** create an EntityBuilder based on an entry from the database. */
    @NotNull public static EntityBuilder entityFromDatabase(String packageName, String schemaName, String tableName) {
        final String        entityName = toCamelCase(tableName);
        final EntityBuilder b          = entity(packageName, entityName, schemaName);
        if (!fromCamelCase(entityName).equals(tableName)) b.databaseName(QName.createQName(schemaName, tableName));
        return b;
    }

    /** Creates an attribute builder for a {@link IntType }. */
    public static AttributeBuilder integer(String nm) {
        return new AttributeBuilder(nm, Types.intType());
    }

    /** Creates an attribute builder for a {@link IntType } with length. */
    public static AttributeBuilder integer(String nm, int length) {
        return new AttributeBuilder(nm, Types.intType(length));
    }

    /** Creates an attribute builder for a {@link RealType }. */
    public static AttributeBuilder real(String nm) {
        return new AttributeBuilder(nm, Types.realType());
    }

    /** Creates an attribute builder for a {@link EnumType}. */
    public static AttributeBuilder reference(String nm, EnumType ref) {
        return new AttributeBuilder(nm, ref);
    }

    /** Creates an attribute builder for a {@link UnresolvedTypeReference}. */
    public static AttributeBuilder reference(String nm, String fqn) {
        return new AttributeBuilder(nm, Types.referenceType(fqn));
    }

    /** Creates an attribute builder for a {@link Entity}. */
    public static AttributeBuilder reference(String nm, DbObject ref) {
        return new AttributeBuilder(nm, ref);
    }

    /** Creates an attribute builder for a {@link UnresolvedTypeReference}. */
    public static AttributeBuilder reference(String nm, String domain, String target) {
        return new AttributeBuilder(nm, Types.referenceType(domain, target));
    }

    /** Creates an attribute builder for a {@link StringType }. */
    public static AttributeBuilder string(String nm) {
        return new AttributeBuilder(nm, stringType());
    }

    /** Creates an attribute builder for a {@link StringType }. */
    public static AttributeBuilder string(String nm, int length) {
        return new AttributeBuilder(nm, stringType(length));
    }

    @NotNull private static Tuple<String, Internal> internal(String name, Type type, boolean optional) {
        return tuple(name, new Internal(type, optional, false));
    }
    @NotNull private static Tuple<String, Internal> internal(String name, Type type, boolean optional, boolean identity) {
        return tuple(name, new Internal(type, optional, identity));
    }

    //~ Static Fields ................................................................................................................................

    static Map<String, Internal> internalFields = linkedHashMap(internal(ID, intType(), false, true),
            internal(UPDATE_TIME, dateTimeType(3), false),
            internal(VERSION_FIELD, longType(), false),
            internal(CREATION_TIME, dateTimeType(3), false),
            internal(DEPRECATION_TIME, dateTimeType(3), true),
            internal(UPDATE_USER, stringType(100).intern(), true),
            internal(CREATION_USER, stringType(100).intern(), true),
            internal(DEPRECATION_USER, stringType(100).intern(), true));

    private static final List<String> auditFields      = asList(CREATION_TIME, CREATION_USER, UPDATE_USER);
    private static final List<String> deprecableFields = asList(DEPRECATION_TIME, DEPRECATION_USER);

    //~ Inner Classes ................................................................................................................................

    static class Internal {
        final boolean identity;
        final boolean optional;
        final Type    type;

        Internal(final Type type, final boolean optional, final boolean identity) {
            this.type     = type;
            this.optional = optional;
            this.identity = identity;
        }
    }
}  // end class EntityBuilder
