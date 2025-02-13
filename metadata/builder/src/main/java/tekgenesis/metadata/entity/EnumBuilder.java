
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.FieldNotFoundException;
import tekgenesis.metadata.exception.InvalidDefaultValueException;
import tekgenesis.metadata.exception.InvalidTypeException;
import tekgenesis.type.EnumType;
import tekgenesis.type.Names;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.core.QName.createQName;

/**
 * Collects the data to build an {@link EnumType}.
 */
@SuppressWarnings("UnusedReturnValue")
public class EnumBuilder extends CompositeBuilder<EnumType, TypeField, TypeFieldBuilder, EnumBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private String             default_;
    private final List<String> documentations;

    private final List<String> ids;
    private String             indexField;
    private final List<String> interfaces;
    private final List<String> labels;

    private String                     primaryKeyField;
    private final String               tableName;
    private final List<Serializable[]> valuesList;

    //~ Constructors .................................................................................................................................

    private EnumBuilder(String sourceName, String pkg, String name) {
        super(sourceName, pkg, name);
        ids             = new ArrayList<>();
        labels          = new ArrayList<>();
        valuesList      = new ArrayList<>();
        interfaces      = new ArrayList<>();
        documentations  = new ArrayList<>();
        primaryKeyField = "";
        indexField      = "";
        default_        = "";
        tableName       = Names.tableName(name);
    }

    //~ Methods ......................................................................................................................................

    @Override public EnumType build() {
        if (default_.isEmpty() && !ids.isEmpty()) default_ = ids.get(0);

        final Map<String, TypeField> fs   = new LinkedHashMap<>();
        final EnumType               type = new EnumType(sourceName,
                domain,
                getId(),
                label,
                documentation,
                tableName,
                defaultForm,
                primaryKeyField,
                indexField,
                default_,
                modifiers,
                ids,
                labels,
                fs,
                valuesList,
                interfaces,
                documentations);
        buildAttributes(fs, type);
        return type;
    }

    @NotNull public List<BuilderError> check() {
        checkField(primaryKeyField, null);
        checkField(indexField, Types.intType());

        if (!default_.isEmpty() && !ids.contains(default_)) builderErrors.add(new InvalidDefaultValueException(default_, getId()));
        // final Set<String> keySet = fields.keySet();
        // for (final Serializable[] ss : valuesList) {
        // for (int j = 0; j < ss.length; j++) {
        // final String key  = keySet.toArray(new String[keySet.size()])[j];
        // final Type   type = fields.get(key).getType();
        // final Type   t    = Types.typeOf(ss[j]);
        // if (!type.equivalent(t) ||
        // (type.getLength().isDefined() && t.getLength().isDefined() && t.getLength().get() > type.getLength().get()))
        // builderErrors.add(new InvalidValueException(getId(), key, ss[j].toString()));
        // }
        // }
        return builderErrors;
    }

    /** Adds a new <key,value> pair to the enum. */
    public EnumBuilder value(String key, String lbl) {
        return value(key, lbl, EnumType.SERIALIZABLES);
    }

    /** Adds a new <key,value> pair and its documentation to the enum. */
    public EnumBuilder value(String key, String lbl, String doc) {
        return value(key, lbl, doc, EnumType.SERIALIZABLES);
    }

    /** Adds an entry key, label, values... to the enum. */
    public EnumBuilder value(String key, String lbl, Serializable[] values) {
        return value(key, lbl, "", values);
    }
    /** Adds an entry key, label, values... and documentation to the enum. */
    public EnumBuilder value(String key, String lbl, String doc, Serializable[] values) {
        ids.add(key);
        labels.add(lbl);
        valuesList.add(values);
        documentations.add(doc);
        return this;
    }

    /** Set Enum Default Value. */
    public EnumBuilder withDefault(final String defaultValue) {
        default_ = defaultValue;
        return this;
    }

    /** Define an index field for the enum. */
    public EnumBuilder withIndex(String field) {
        indexField = field;
        return this;
    }

    /** Enum generated java class interfaces. */
    public EnumBuilder withInterface(String clazz) {
        interfaces.add(clazz);
        return this;
    }

    /** Define an Id field for the enum. */
    public EnumBuilder withPrimaryKey(String field) {
        primaryKeyField = field;
        return this;
    }

    private void checkField(String field, @Nullable Type requiredType) {
        if (field.isEmpty()) return;
        final TypeFieldBuilder tfb = fields.get(field);
        if (tfb == null) builderErrors.add(new FieldNotFoundException(getId(), field));
        else if (requiredType != null && !tfb.getType().equivalent(requiredType)) builderErrors.add(new InvalidTypeException(getId(), tfb.getType()));
    }

    //~ Methods ......................................................................................................................................

    /** Creates an {@link EnumType}. from a {@link Enum} */
    public static EnumType enumType(Class<? extends Enum<?>> enumClass) {
        final QName       name    = createQName(enumClass.getCanonicalName());
        final EnumBuilder builder = new EnumBuilder("", name.getQualification(), name.getName());
        for (final Enum<?> e : enumClass.getEnumConstants())
            builder.value(e.name(), e instanceof Enumeration ? Enumeration.class.cast(e).label() : "");
        return builder.build();
    }

    /** Creates an {@link EnumBuilder}. */
    public static EnumBuilder enumType(String sourceName, final String packageId, final String enumName) {
        return new EnumBuilder(sourceName, packageId, enumName);
    }

    //~ Static Fields ................................................................................................................................

    public static final String[] STRINGS = new String[0];
}  // end class EnumBuilder
