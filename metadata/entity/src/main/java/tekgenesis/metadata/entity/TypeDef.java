
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

import tekgenesis.type.Kind;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.ModelType;
import tekgenesis.type.Modifier;

/**
 * Common class of {@link SimpleType} and {@link StructType}.
 */
public abstract class TypeDef extends ModelType {

    //~ Constructors .................................................................................................................................

    TypeDef(String sourceName, String domain, String name, String label, EnumSet<Modifier> modifiers) {
        super(sourceName, domain, name, label, modifiers, "");
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public final Kind getKind() {
        return Kind.TYPE;
    }

    @NotNull @Override public final MetaModelKind getMetaModelKind() {
        return MetaModelKind.TYPE;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1659002281572974547L;
}
