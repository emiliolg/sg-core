
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * A Definition of a Simple (not composite) Type.
 */
public class SimpleType extends TypeDef implements ModelField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final TypeField typeDef;

    //~ Constructors .................................................................................................................................

    SimpleType(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String description,
               @NotNull EnumSet<Modifier> modifiers, @NotNull TypeField field) {
        super(sourceName, domain, name, description, modifiers);
        typeDef = field;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasChildren() {
        return false;
    }

    @Override public Object valueOf(String str) {
        return getFinalType().valueOf(str);
    }

    /** Returns the entity's attributes. */
    @NotNull public ImmutableList<TypeField> getChildren() {
        return listOf(typeDef);
    }

    @Override public boolean isComposite() {
        return getFinalType().isComposite();
    }

    @NotNull @Override public Type getFinalType() {
        return typeDef.getFinalType();
    }

    @Override public String getImplementationClassName() {
        return typeDef.getImplementationClassName();
    }

    @NotNull @Override public FieldOptions getOptions() {
        return typeDef.getOptions();
    }

    @NotNull @Override public String getSqlImplementationType(boolean multiple) {
        return getFinalType().getSqlImplementationType(multiple);
    }

    @NotNull @Override public Type getType() {
        return typeDef.getType();
    }

    @Override public void setType(@NotNull Type type) {
        throw new IllegalStateException();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4093534455181687651L;
}  // end class SimpleType
