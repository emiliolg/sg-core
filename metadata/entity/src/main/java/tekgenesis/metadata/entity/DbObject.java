
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.type.CompositeType;
import tekgenesis.type.ModelType;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.metadata.entity.SearchField.searchField;

/**
 * Implemented by Entity and View.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class DbObject extends ModelType implements Type, CompositeType {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final Map<String, Seq<Attribute>> indexes;

    @NotNull private final Map<String, Attribute> attributes;

    @NotNull private final CacheType cacheType;

    @NotNull private List<ModelField> describes;
    @NotNull private final String     documentation;

    @NotNull private List<ModelField>   image;
    @NotNull private List<SearchField>  searchByFields;
    @NotNull private final QName        tableName;

    private final boolean                                        tableNameGenerated;
    @NotNull private final LinkedHashMap<String, Seq<Attribute>> uniqueIndexes;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    DbObject(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String label, @NotNull EnumSet<Modifier> modifiers,
             String defaultForm, @NotNull Map<String, Attribute> attributes, @NotNull QName tableName, boolean tableNameGenerated,
             @NotNull List<ModelField> describes, @NotNull List<SearchField> searchByFields, @NotNull final CacheType cacheType,
             @NotNull LinkedHashMap<String, Seq<Attribute>> uniqueIndexes, @NotNull Map<String, Seq<Attribute>> indexes,
             @NotNull List<ModelField> image, @NotNull String documentation) {
        super(sourceName, domain, name, label, modifiers, defaultForm);
        this.tableName          = tableName;
        this.tableNameGenerated = tableNameGenerated;
        this.describes          = describes;
        this.attributes         = attributes;
        this.uniqueIndexes      = uniqueIndexes;
        this.indexes            = indexes;
        this.image              = image;
        this.searchByFields     = searchByFields;
        this.cacheType          = cacheType;
        this.documentation      = documentation;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the entity's attributes. Including ones of the super entity if any */
    @NotNull public Seq<Attribute> allAttributes() {
        return attributes();
    }

    /** Returns the composite object as a View. */
    public View asView() {
        return cast(this);
    }

    /** Returns the entity's attributes. Not including ones of the super entity if any */
    @NotNull public Seq<Attribute> attributes() {
        return immutable(attributes.values());
    }

    /** Compile searchable field specific options. */
    public void compileSearchByOptions(@NotNull final RefTypeSolver solver) {
        for (final SearchField f : searchByFields())
            f.getOptions().compile(solver);
    }

    /** Return the elements that describes this entity. */
    @NotNull public Seq<ModelField> describes() {
        return immutable(describes);
    }

    /** Returns true if the Entity has a Composite Key (A Key with more than one field. */
    public abstract boolean hasCompositePrimaryKey();

    /** Returns true if the entity has a default (auto-generated) primary key. */
    public abstract boolean hasDefaultPrimaryKey();

    /** Returns if this entity has image. */
    public boolean hasImage() {
        return isNotEmpty(image);
    }

    /** Return the image field name of this entity. */
    @NotNull public String image() {
        if (!image.isEmpty()) return image.get(0).getName();
        else return "";
    }

    /** Returns the set of simple PrimaryKey Fields. */
    public ImmutableList<TypeField> primaryKeySimpleFields() {
        return TypeField.retrieveSimpleFields(getPrimaryKey());
    }

    @Override public ImmutableList<TypeField> retrieveSimpleFields() {
        return primaryKeySimpleFields();
    }

    /** Return searchable by fields. */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public ImmutableList<SearchField> searchByFields() {
        if (searchByFields.isEmpty() && hasModifier(Modifier.DEFAULT_SEARCHABLE)) searchByFields = notEmptySearchByFields();
        return immutable(searchByFields);
    }

    /** Solve described by. */
    public void solveDescribes(Function<ModelField, ModelField> function) {
        describes = map(describes, function).toList();
    }

    /**
     * Solves all references on entity fields.
     *
     * @param  function  solver function
     */
    public void solveReferences(Function<ModelField, ModelField> function) {
        solveDescribes(function);
        image = map(image, function).toList();
        solveSearchByFields(function);
    }

    /** Solve searchable fields of db object. */
    public void solveSearchByFields(Function<ModelField, ModelField> function) {
        searchByFields = map(searchByFields(),
                    searchField -> searchField(searchField.getId(), function.apply(searchField.getField()), searchField.getOptions())).toList();
    }

    /** Whether to separate inmutable and 'ForUpdate' objects. */
    public boolean splitMutator() {
        // for the time being when the cache is full
        return cacheType.isFull();
    }

    /** Validate that database searchable fields have valid types. */
    public void validateSearchableDatabaseTypes(Consumer<SearchField> validator) {
        if (isDatabaseSearchable()) searchByFields().forEach(validator);
    }

    /** Returns the attribute with that name or null if it does not exist. */
    @NotNull public Option<Attribute> getAttribute(String attribute) {
        return option(attributes.get(attribute));
    }

    /** Returns the entity's attributes. Including ones of the super entity if any */
    @NotNull public Seq<TypeField> getBoundableFields() {
        return cast(attributes());
    }
    /** Returns whether the entity can be updated using optimistic lock. */
    public boolean isOptimistic() {
        return hasModifier(Modifier.OPTIMISTIC_LOCKING);
    }

    /** Returns the cache type of the entity . */
    @NotNull public CacheType getCacheType() {
        return cacheType;
    }

    /** Returns the entity's attributes. */
    @NotNull public Seq<Attribute> getChildren() {
        return immutable(attributes.values());
    }

    /** Returns true if is protected. */
    public boolean isProtected() {
        return false;
    }

    /** Returns true if the table name is generated. */
    public boolean isTableNameGenerated() {
        return tableNameGenerated;
    }

    /** Returns the documentation of the entity . */
    @NotNull @Override public String getDocumentation() {
        return documentation;
    }

    /** Returns whether the entity is auditable or not. */
    public boolean isAuditable() {
        return hasModifier(Modifier.AUDITABLE);
    }

    /** Returns if this database object contains 'searchable by database' option. */
    public boolean isDatabaseSearchable() {
        return hasModifier(Modifier.DATABASE_SEARCHABLE);
    }

    /** Returns whether the entity is deprecable or not. */
    public boolean isDeprecable() {
        return hasModifier(Modifier.DEPRECABLE);
    }
    /** Returns whether the entity is remotable or not. */
    public boolean isRemotable() {
        return hasModifier(Modifier.REMOTABLE);
    }

    /** Returns if this entity is searchable. */
    public boolean isSearchable() {
        return !searchByFields().isEmpty() || hasModifier(Modifier.DEFAULT_SEARCHABLE);
    }

    /** Indicates if the database object is an updatable. */
    public boolean isUpdatable() {
        return true;
    }

    /** Returns the attribute with that name or null if it does not exist. */
    @NotNull public Option<Attribute> getField(String fieldName) {
        return option(attributes.get(fieldName));
    }

    /** Return the package where the ForUpdate class will be generated. */
    public String getForUpdatePackage() {
        return getDomain();
    }

    /** Return a list of indexes by name as Attributes. */
    @NotNull public Seq<Attribute> getIndexByName(@NotNull String name) {
        final Seq<Attribute> fields = indexes.get(name);
        if (fields == null) return emptyIterable();
        else return fields;
    }

    /** Return list of indexes names. */
    @NotNull public Seq<String> getIndexNames() {
        return immutable(indexes.keySet());
    }

    /** Returns the parent (enclosing) entity for inner Entities. */
    @NotNull public Option<Entity> getParent() {
        return Option.empty();
    }

    /** Returns the Attributes of the Primary Key. */
    @NotNull public abstract ImmutableList<Attribute> getPrimaryKey();

    /** Returns if this entity is searchable by fields (if it needs base and user class). */
    public boolean isSearchableByFields() {
        return !searchByFields().isEmpty();
    }

    /** Returns the schema name. */
    @NotNull @Override public String getSchema() {
        return tableName.getQualification();
    }

    @Override public boolean isDatabaseObject() {
        return true;
    }

    /** Returns the Table Name for this Entity. */
    @NotNull public QName getTableName() {
        return tableName;
    }

    /** Return a list of unique indexes by name as Attributes. */
    @NotNull public Seq<Attribute> getUniqueIndexByName(@NotNull String name) {
        final Seq<Attribute> fields = uniqueIndexes.get(name);
        if (fields == null) return emptyIterable();
        else return fields;
    }

    /** Return list of unique indexes names. */
    @NotNull public Seq<String> getUniqueIndexNames() {
        return immutable(uniqueIndexes.keySet());
    }

    /** Returns true if attribute is part of the primary key. */
    public abstract boolean isPrimaryKey(@NotNull Attribute attribute);

    protected List<SearchField> notEmptySearchByFields() {
        return modelFieldsToSearchFields(isDatabaseSearchable() ? getPrimaryKey() : describes());
    }

    @NotNull ImmutableList<SearchField> modelFieldsToSearchFields(final Iterable<? extends ModelField> fields) {
        return fromIterable(fields).map(SearchField::unresolvedSearchField).toList();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6074642150038128884L;
}  // end class DbObject
