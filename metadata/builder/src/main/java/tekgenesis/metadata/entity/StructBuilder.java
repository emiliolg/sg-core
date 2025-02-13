
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

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.inheritance.ExtendsFromFinalException;
import tekgenesis.metadata.exception.inheritance.FieldClashesWithUnrelatedTypeException;
import tekgenesis.metadata.exception.inheritance.InterfaceExtendsOnlyException;
import tekgenesis.metadata.exception.inheritance.MultipleInheritanceException;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Option.some;

/**
 * Builder for a {@link SimpleType}.
 */
public class StructBuilder extends CompositeBuilder<StructType, TypeField, TypeFieldBuilder, StructBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, TypeField>     allFieldsByName;
    private final Map<TypeField, StructType> allFieldsByStruct;
    private final Set<String>                args;
    private String                           defaultWidget;

    private final List<StructType> superTypes;

    //~ Constructors .................................................................................................................................

    private StructBuilder(String sourceName, @NotNull String pkg, @NotNull String name) {
        super(sourceName, pkg, name);
        superTypes        = new ArrayList<>();
        allFieldsByName   = new HashMap<>();
        allFieldsByStruct = new HashMap<>();
        args              = new LinkedHashSet<>();
        defaultWidget     = "";
    }

    //~ Methods ......................................................................................................................................

    /** Add an Argument to the builder. */
    public final StructBuilder addArgument(TypeFieldBuilder a)
        throws DuplicateFieldException
    {
        checkDuplicates(a);
        args.add(a.getName());
        fields.put(a.getName(), a);
        return this;
    }

    /** Mark type as interface. */
    public StructBuilder asInterface() {
        withModifiers(EnumSet.of(Modifier.INTERFACE));
        return this;
    }

    @Override public StructType build() {
        final Map<String, TypeField> fs   = new LinkedHashMap<>();
        final StructType             type = new StructType(sourceName,
                domain,
                id,
                label,
                modifiers,
                fs,
                args,
                superTypes,
                defaultWidget,
                documentation);
        buildAttributes(fs, type);
        return type;
    }

    @NotNull public List<BuilderError> check() {
        return builderErrors;
    }

    /** Add default widget for type. */
    public StructBuilder defaultWidget(@NotNull String w) {
        defaultWidget = w;
        return this;
    }

    /** Add super type. */
    public StructBuilder withSuperType(StructType parent)
        throws FieldClashesWithUnrelatedTypeException, InterfaceExtendsOnlyException, MultipleInheritanceException, ExtendsFromFinalException
    {
        if (parent.isFinal()) throw new ExtendsFromFinalException(parent.getFullName(), getFullName());

        if (hasModifier(Modifier.INTERFACE)) {
            if (!parent.hasModifier(Modifier.INTERFACE)) throw new InterfaceExtendsOnlyException(parent.getFullName(), getFullName());
        }
        else if (!parent.isInterface()) {
            for (final StructType superType : superTypes) {
                if (!superType.isInterface()) throw new MultipleInheritanceException(superType.getFullName(), parent.getFullName(), getFullName());
            }
        }

        superTypes.add(parent);

        for (final TypeField field : parent.getChildren()) {
            for (final TypeField duplicate : checkIncompatibleClashes(field.getName(), field.getType()))
                throw fieldsClashes(duplicate, field.getName(), parent.getFullName(), getFullName());
            allFieldsByName.put(field.getName(), field);
            allFieldsByStruct.put(field, parent);
        }
        return this;
    }

    @Override void checkDuplicates(TypeFieldBuilder a)
        throws DuplicateFieldException
    {
        for (final TypeField incompatible : checkIncompatibleClashes(a.getName(), a.getType()))
            throw fieldsClashes(incompatible, a.getName(), getFullName(), getFullName());

        if (fields.containsKey(a.getName())) throw DuplicateFieldException.onType(a.getName(), id);
    }

    private Option<TypeField> checkIncompatibleClashes(@NotNull String name, @NotNull Type type) {
        final TypeField duplicate = allFieldsByName.get(name);  // Also can check required compatibility, length, precision, etc.
        return duplicate != null && !duplicate.getType().equivalent(type) ? some(duplicate) : Option.empty();
    }

    private FieldClashesWithUnrelatedTypeException fieldsClashes(TypeField incompatible, String fieldName, String fieldModel, String model) {
        return new FieldClashesWithUnrelatedTypeException(fieldName,
            fieldModel,
            incompatible.getName(),
            allFieldsByStruct.get(incompatible).getFullName(),
            model);
    }

    //~ Methods ......................................................................................................................................

    /** Creates an {@link StructBuilder}. */
    public static StructBuilder struct(QName name) {
        return new StructBuilder("", name.getQualification(), name.getName());
    }

    /** Creates an {@link StructBuilder}. */
    public static StructBuilder struct(String sourceName, final String packageId, final String name) {
        return new StructBuilder(sourceName, packageId, name);
    }
}  // end class StructBuilder
