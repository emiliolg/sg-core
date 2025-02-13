
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
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.ModelField;
import tekgenesis.type.*;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.seq;

/**
 * This class represents an Entity of the application. An entity has attributes and a primary key
 */
public class Entity extends DbObject {

    //~ Instance Fields ..............................................................................................................................

    private final boolean defaultPrimaryKey;

    @NotNull private Option<Entity> parent;

    @NotNull private final ImmutableList<Attribute> primaryKey;
    private final boolean                           protect;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    Entity(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String description, @NotNull QName tableName,
           boolean tableNameGenerated, @NotNull EnumSet<Modifier> modifiers, boolean defaultPrimaryKey, @NotNull String defaultForm,
           @NotNull Map<String, Attribute> attributes, @NotNull List<Attribute> primaryKey, @NotNull List<ModelField> describes,
           @NotNull LinkedHashMap<String, Seq<Attribute>> uniqueIndexes, @NotNull Map<String, Seq<Attribute>> indexes,
           List<SearchField> searchByFields, CacheType cacheType, boolean protect, List<ModelField> image, @NotNull String documentation) {
        super(sourceName,
            domain,
            name,
            description,
            modifiers,
            defaultForm,
            attributes,
            tableName,
            tableNameGenerated,
            describes,
            searchByFields,
            cacheType,
            uniqueIndexes,
            indexes,
            image,
            documentation);

        parent                 = Option.empty();
        this.primaryKey        = immutable(primaryKey);
        this.defaultPrimaryKey = defaultPrimaryKey;
        this.protect           = protect;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the entity's attributes. Including ones of the super entity if any */
    @NotNull @Override public Seq<Attribute> allAttributes() {
        return attributes().toList();
    }

    /** Returns true if the Entity has a Composite Key (A Key with more than one field. */
    public boolean hasCompositePrimaryKey() {
        return primaryKey.size() > 1 || primaryKey.get(0).isComposite();
    }

    /** returns true if the entity has a default (auto-generated) primary key. */
    public boolean hasDefaultPrimaryKey() {
        return defaultPrimaryKey;
    }

    @Override public boolean isProtected() {
        return protect;
    }

    @Override public boolean isComposite() {
        return true;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.REFERENCE;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.ENTITY;
    }

    /** Returns the parent (enclosing) entity for inner Entities. */
    @NotNull public Option<Entity> getParent() {
        return parent;
    }

    /** Set the entity parent. */
    public void setParent(@NotNull Option<Entity> parent) {
        this.parent = parent;
    }

    /** Returns the Attributes of the Primary Key. */
    @NotNull public ImmutableList<Attribute> getPrimaryKey() {
        return primaryKey;
    }

    /** Returns if this entity is an Inner entity. */
    public boolean isInner() {
        return !getParent().isEmpty();
    }

    @Override public Seq<MetaModel> getReferences() {
        final Set<MetaModel> result = new HashSet<>();
        for (final Attribute value : attributes()) {
            final Type t = value.getType();
            if (t instanceof MetaModel) result.add((MetaModel) t);
        }
        return seq(result);
    }

    @Override public boolean isEntity() {
        return true;
    }

    /** Returns true if attribute is part of the primary key. */
    public boolean isPrimaryKey(@NotNull Attribute attribute) {
        return primaryKey.contains(attribute);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6431859732037954777L;
}  // end class Entity
