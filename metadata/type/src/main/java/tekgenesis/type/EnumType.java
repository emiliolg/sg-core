
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.io.Serializable;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.GwtIncompatible;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.field.TypeField;

import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Option.ofNullable;

/**
 * A type that is a {@link Enum}.
 */
@SuppressWarnings("FieldMayBeFinal")
public class EnumType extends ModelType implements CompositeType {

    //~ Instance Fields ..............................................................................................................................

    private final String          defaultValue;
    @NotNull private final String documentation;

    private final Map<String, TypeField> fieldMap;
    private final String                 indexFieldName;
    private final List<String>           interfaces;
    private final String                 pkFieldName;
    private final String                 tableName;

    private final Map<String, EnumValue> values;

    //~ Constructors .................................................................................................................................

    /** constructor.* */
    EnumType() {
        fieldMap       = null;
        tableName      = null;
        values         = null;
        pkFieldName    = "";
        defaultValue   = "";
        indexFieldName = "";
        documentation  = "";
        interfaces     = Collections.emptyList();
    }

    private EnumType(String domain, String name, List<String> ids, List<String> labels) {
        this("",
            domain,
            name,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            Modifier.NONE,
            ids,
            labels,
            Collections.emptyMap(),
            Colls.emptyList(),
            Colls.emptyList(),
            Colls.emptyList());
    }

    /** Creates an Enum. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public EnumType(String sourceName, String domain, String enumId, String label, @NotNull String documentation, @NotNull String tableName,
                    String defaultForm, String pkFieldName, String indexFieldName, String defaultValue, EnumSet<Modifier> modifiers, List<String> ids,
                    List<String> labels, @NotNull Map<String, TypeField> fields, List<Serializable[]> values, List<String> interfaces,
                    List<String> documentations) {
        super(sourceName, domain, enumId, label, modifiers, defaultForm);

        final Map<String, EnumValue> map = new LinkedHashMap<>(ids.size());
        int                          i   = 0;
        for (final String id : ids) {
            final Serializable[] vs = i < values.size() ? values.get(i) : SERIALIZABLES;
            map.put(id, new EnumValue(this, id, labels.get(i), i, i < documentations.size() ? documentations.get(i) : "", vs));
            i++;
        }

        this.values         = Collections.unmodifiableMap(map);
        fieldMap            = fields;
        this.tableName      = tableName;
        this.documentation  = documentation;
        this.pkFieldName    = pkFieldName;
        this.indexFieldName = indexFieldName;
        this.defaultValue   = defaultValue;
        this.interfaces     = Collections.unmodifiableList(interfaces);
    }

    //~ Methods ......................................................................................................................................

    /** returns true if the Enum contains the specified id. */
    public boolean contains(String id) {
        return values.containsKey(id);
    }

    /** Returns true if the Enum has fields. */
    public boolean hasFields() {
        return !fieldMap.isEmpty();
    }

    @Override public Seq<TypeField> retrieveSimpleFields() {
        return TypeField.retrieveSimpleFields(fieldMap.values());
    }

    /** Serialize the Enum. */
    public void serialize(StreamWriter w) {
        w.writeString(getDomain());
        w.writeString(getName());

        w.writeInt(values.size());
        for (final EnumValue value : values.values()) {
            w.writeString(value.getName());
            w.writeString(value.getLabel());
        }
    }

    /** Get the sql id for the specified EnumValue. */
    @NotNull public String sqlIdFor(final String value, boolean multiple) {
        final EnumValue v = getValue(value);
        if (v == null) return "";
        if (multiple) return String.valueOf(v.getOrdinal() + 1);
        if (pkFieldName.isEmpty()) return "'" + v.getName() + "'";

        int i = 0;
        for (final String f : fieldMap.keySet()) {
            if (f.equals(pkFieldName)) {
                final Object df = v.getValues()[i];
                return df instanceof String ? "'" + df + "'" : String.valueOf(df);
            }
            i++;
        }
        return "'" + v.getName() + "'";
    }

    @GwtIncompatible @Override public Object valueOf(String str) {
        return Enumerations.valueOf(getImplementationClassName(), str);
    }

    @NotNull @Override public Seq<TypeField> getChildren() {
        return immutable(fieldMap.values());
    }

    @NotNull public String getDefaultValue() {
        return defaultValue;
    }

    /** Get the documentation for the EnumType. */
    @NotNull @Override public String getDocumentation() {
        return documentation;
    }

    /** Return extra fields types. */
    public TypeField[] getExtraFieldsTypes() {
        return fieldMap.values().toArray(new TypeField[fieldMap.size()]);
    }

    /** Returns the field with the given name or empty. */
    @NotNull @Override public Option<TypeField> getField(String name) {
        return ofNullable(fieldMap.get(name));
    }

    /** Return the Enum Ids as a Unmodifiable Set. */
    public Set<String> getIds() {
        return Collections.unmodifiableSet(values.keySet());
    }

    /** Returns the index field Name. */
    public String getIndexFieldName() {
        return indexFieldName;
    }

    /** Return enum java interfaces. */
    @NotNull public Iterable<String> getInterfaces() {
        return interfaces;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.ENUM;
    }

    /** Get the Label for the specified Id. */
    public String getLabel(String name) {
        final EnumValue value = values.get(name);
        return value == null ? "" : value.getLabel();
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.ENUM;
    }

    public boolean isException() {
        return hasModifier(Modifier.EXCEPTION);
    }

    /** Returns the enum primary key field Name. */
    public String getPkFieldName() {
        return pkFieldName;
    }

    /** Returns the enum primary key type. */
    @NotNull public Type getPkType() {
        final TypeField idField = fieldMap.get(pkFieldName);
        return idField == null ? Types.stringType(KEY_LENGTH) : idField.getType();
    }

    @Override public Seq<MetaModel> getReferences() {
        return ImmutableList.build(result ->
                fieldMap.values().forEach(typeField -> {
                    if (typeField.getFinalType() instanceof MetaModel) result.add((MetaModel) typeField.getFinalType());
                }));
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return multiple ? Types.longType().getSqlImplementationType(false) : getPkType().getSqlImplementationType(false);
    }

    @Override public int getSqlType() {
        return getPkType().getSqlType();
    }

    /** Returns the Table Name for this Enum. Used when the Enum is open */
    @NotNull public String getTableName() {
        return tableName;
    }

    /** Get the value for the specified Id. */
    @Nullable public EnumValue getValue(String id) {
        return values.get(id);
    }

    /** Return the Enum Values. */
    public ImmutableCollection<EnumValue> getValues() {
        return Colls.immutable(values.values());
    }

    //~ Methods ......................................................................................................................................

    @GwtIncompatible public static EnumType fromEnum(Enumeration<?, ?> enumeration) {
        final Class<?>     c      = enumeration.getClass();
        final QName        qName  = QName.createQName(c);
        final List<String> names  = new ArrayList<>();
        final List<String> labels = new ArrayList<>();
        for (final Enumeration<?, ?> e : Enumerations.getValuesFor(c.getName())) {
            names.add(e.name());
            labels.add(e.label());
        }
        return new EnumType(qName.getQualification(), qName.getName(), names, labels);
    }

    /** Instantiate the Enum from an Stream. */
    public static EnumType instantiate(StreamReader r) {
        final String domain = r.readString();
        final String name   = r.readString();
        final int    n      = r.readInt();

        final String[] ids    = new String[n];
        final String[] labels = new String[n];

        for (int i = 0; i < n; i++) {
            ids[i]    = r.readString();
            labels[i] = r.readString();
        }

        return new EnumType(domain, name, asList(ids), asList(labels));
    }

    //~ Static Fields ................................................................................................................................

    public static final int KEY_LENGTH   = 50;
    public static final int LABEL_LENGTH = 128;

    public static final Serializable[] SERIALIZABLES = new Serializable[0];

    private static final long serialVersionUID = 6342427902172830023L;
}  // end class EnumType
