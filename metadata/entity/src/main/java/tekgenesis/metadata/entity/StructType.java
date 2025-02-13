
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

import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.field.TypeField;
import tekgenesis.type.CompositeType;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.option;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.seq;

/**
 * A Definition of a Composite Type.
 */
public class StructType extends TypeDef implements CompositeType {

    //~ Instance Fields ..............................................................................................................................

    private final ImmutableSet<String> args;
    @NotNull private final String      defaultWidget;
    @NotNull private final String      documentation;

    @NotNull private final Map<String, TypeField> fieldMap;
    @NotNull private final List<StructType>       superTypes;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    StructType(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String description,
               @NotNull EnumSet<Modifier> modifiers, @NotNull Map<String, TypeField> fieldMap, @NotNull Set<String> args,
               @NotNull List<StructType> superTypes, @NotNull String defaultWidget, @NotNull String documentation) {
        super(sourceName, domain, name, description, modifiers);
        this.fieldMap      = fieldMap;
        this.superTypes    = superTypes;
        this.args          = immutable(args);
        this.defaultWidget = defaultWidget;
        this.documentation = documentation;
    }

    //~ Methods ......................................................................................................................................

    @Override public Seq<TypeField> retrieveSimpleFields() {
        return TypeField.retrieveSimpleFields(fieldMap.values());
    }

    /** Return the arguments. */
    public ImmutableSet<String> getArgs() {
        return args;
    }

    /** Returns type attributes. */
    @NotNull public Seq<TypeField> getChildren() {
        return immutable(fieldMap.values());
    }

    /** Returns the default widget definition to be used for this type, or the empty string. */
    @NotNull public String getDefaultWidget() {
        return defaultWidget;
    }

    /** Returns the StructType's documentation. */
    @NotNull @Override public String getDocumentation() {
        return documentation;
    }

    @Override public boolean isComposite() {
        return true;
    }

    /** Returns true if type is interface. */
    public boolean isInterface() {
        return hasModifier(Modifier.INTERFACE);
    }

    /** Returns the attribute with that name or null if it does not exist. */
    @NotNull @Override public Option<TypeField> getField(String fieldName) {
        return option(fieldMap.get(fieldName));
    }

    /** Returns true if type is final. */
    public boolean isFinal() {
        return hasModifier(Modifier.FINAL);
    }

    @Override public Seq<MetaModel> getReferences() {
        final Set<MetaModel> result = new HashSet<>();
        for (final TypeField field : getChildren()) {
            final Type type = field.getType().getFinalType();
            addMetaModelTypes(result, type);
        }
        for (final StructType superType : getSuperTypes())
            addMetaModelTypes(result, superType);
        return seq(result);
    }

    /** Returns type super types. */
    @NotNull public Seq<StructType> getSuperTypes() {
        return immutable(superTypes);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4093534455181687651L;
}  // end class StructType
