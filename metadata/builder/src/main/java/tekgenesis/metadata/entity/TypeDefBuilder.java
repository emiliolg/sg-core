
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.type.Type;

import static tekgenesis.common.collections.Colls.emptyList;

/**
 * Builder for a {@link SimpleType}.
 */
public class TypeDefBuilder extends ModelBuilder.Default<TypeDef, TypeDefBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private FieldOptions options;
    private final Type   type;

    //~ Constructors .................................................................................................................................

    private TypeDefBuilder(String sourceName, @NotNull String pkg, @NotNull String name, final Type type) {
        super(sourceName, pkg, name);
        this.type = type;
        options   = FieldOptions.EMPTY;
    }

    //~ Methods ......................................................................................................................................

    @Override public TypeDef build() {
        return new SimpleType(sourceName, domain, id, label, modifiers, new TypeField("", type, options));
    }

    @NotNull @Override public List<BuilderError> check() {
        return emptyList();
    }

    /** Add options to TypeDef. */
    @NotNull public TypeDefBuilder withOptions(@NotNull FieldOptions opts) {
        options = opts;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Creates an {@link TypeDefBuilder}. */
    public static TypeDefBuilder typeDef(final QName name, final Type type) {
        return new TypeDefBuilder("", name.getQualification(), name.getName(), type);
    }

    /** Creates an {@link TypeDefBuilder}. */
    public static TypeDefBuilder typeDef(String sourceName, final String packageId, final String name, final Type type) {
        return new TypeDefBuilder(sourceName, packageId, name, type);
    }
}  // end class TypeDefBuilder
