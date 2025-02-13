
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.role;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.type.MetaModelKind.ROLE;

/**
 * Class that contains metadata for roles.
 */
public class Role implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private final QName               key;
    private final String              label;
    private final EnumSet<Modifier>   modifiers;
    private final Seq<RolePermission> permissions;
    private final String              sourceName;

    //~ Constructors .................................................................................................................................

    Role(@NotNull final QName key, @NotNull final String label, @NotNull final EnumSet<Modifier> modifiers, @NotNull final String sourceName,
         @NotNull final List<RolePermission> permissions) {
        this.key         = key;
        this.label       = label;
        this.modifiers   = modifiers;
        this.sourceName  = sourceName;
        this.permissions = immutable(permissions);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasModifier(Modifier mod) {
        return modifiers.contains(mod);
    }

    @NotNull @Override public Seq<RolePermission> getChildren() {
        return permissions;
    }

    @NotNull @Override public String getDomain() {
        return key.getQualification();
    }

    @NotNull public final String getFullName() {
        return key.getFullName();
    }

    @NotNull @Override public QName getKey() {
        return key;
    }

    @NotNull @Override public String getLabel() {
        return label;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return ROLE;
    }

    @NotNull @Override public String getName() {
        return key.getName();
    }

    @Override public Seq<MetaModel> getReferences() {
        return emptyIterable();
    }

    @NotNull @Override public String getSchema() {
        return "";
    }

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3624910200330072016L;
}  // end class Role
