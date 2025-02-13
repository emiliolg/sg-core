
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

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.*;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Names;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.field.FieldOption.ABSTRACT;
import static tekgenesis.field.FieldOption.READ_ONLY;
import static tekgenesis.metadata.exception.FieldNotFoundException.searchableFieldNotFound;
import static tekgenesis.metadata.exception.IllegalSearchableField.illegalAbstract;
import static tekgenesis.type.FieldReference.unresolvedFieldRef;

/**
 * Builder for Database Object with index.
 */
public abstract class DbObjectBuilder<T extends MetaModel,
                                      F extends TypeField, FB extends CompositeFieldBuilder<FB>, This extends CompositeBuilder<T, F, FB, This>>
    extends CompositeBuilder<T, F, FB, This>
{

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected QName databaseName;

    @NotNull protected final Map<String, Collection<ModelField>> indexes;
    protected boolean                                            primaryKeyDefault;

    @NotNull protected final List<ModelField>                     primaryKeyFields;
    @NotNull protected final Map<String, SearchFieldBuilder>      searchByFields;
    @NotNull protected final Map<String, Collection<ModelField>>  uniqueIndexes;
    boolean                                                       dbNameGenerated;
    private final String                                          defaultSchemaId;

    //~ Constructors .................................................................................................................................

    DbObjectBuilder(String sourceName, @NotNull String pkg, @NotNull String name, @NotNull String defaultSchemaId) {
        super(sourceName, pkg, name);
        uniqueIndexes        = new LinkedHashMap<>();
        indexes              = new LinkedHashMap<>();
        primaryKeyFields     = new ArrayList<>();
        primaryKeyDefault    = false;
        dbNameGenerated      = true;
        databaseName         = QName.EMPTY;
        this.defaultSchemaId = Names.validateSchemaId(defaultSchemaId, pkg);
        searchByFields       = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Searchable by { fields }. */
    public void addSearchableField(final SearchFieldBuilder f)
        throws DuplicateFieldException
    {
        final String fieldId = f.getId();
        if (searchByFields.containsKey(fieldId)) throw DuplicateFieldException.onSearchable(fieldId);
        searchByFields.put(fieldId, f);
    }

    /** Toggles the entity as searchable. */
    public This databaseName(QName dbName) {
        databaseName    = dbName;
        dbNameGenerated = false;
        return cast(this);
    }

    /** Makes db object defaultSearchable. */
    public void defaultSearchable() {
        if (searchByFields.isEmpty()) withModifier(Modifier.DEFAULT_SEARCHABLE);
    }

    /** Sets the entity primary key. */
    public This primaryKey(String... fs) {
        return primaryKey(convertToModelFields(fs));
    }

    /** Sets the entity primary key. */
    public This primaryKey(Collection<ModelField> fs) {
        if (!primaryKeyFields.isEmpty())  // noinspection DuplicateStringLiteralInspection
            throw new IllegalStateException("Primary Key already set");
        primaryKeyDefault = false;
        primaryKeyFields.clear();
        primaryKeyFields.addAll(fs);
        return cast(this);
    }

    /**
     * Toggles the entity as remotable.
     *
     * @return  the same EntityBuilder
     */
    public This remotable()
    {
        return withModifier(Modifier.REMOTABLE);
    }

    /** Make db object searchable with default described by fields. */
    public This searchable() {
        return withModifier(Modifier.DEFAULT_SEARCHABLE);
    }

    /** Searchable by primary_key. */
    public void searchByDatabase() {
        withModifier(Modifier.DATABASE_SEARCHABLE);
    }

    /** Adds index to the entity. */
    public This withIndex(String indexId, Collection<ModelField> fs)
        throws DuplicateIndexException, NoAttributesIndexException
    {
        if (fs.isEmpty()) throw new NoAttributesIndexException(indexId, getId());
        if (indexes.get(indexId) != null || uniqueIndexes.get(indexId) != null) throw new DuplicateIndexException(indexId, getId());
        indexes.put(indexId, fs);
        return cast(this);
    }

    /**
     * Adds index to the entity.
     *
     * @param   indexId  index name
     * @param   fs       unique fields
     *
     * @return  the same EntityBuilder
     */
    public This withIndex(String indexId, String... fs)
        throws DuplicateIndexException, NoAttributesIndexException
    {
        return withIndex(indexId, convertToModelFields(fs));
    }

    /**
     * Adds unique index to the entity.
     *
     * @param   indexId  index name
     * @param   fs       unique fields
     *
     * @return  the same EntityBuilder
     */
    public This withUnique(String indexId, String... fs)
        throws DuplicateIndexException, NoAttributesIndexException
    {
        return withUnique(indexId, convertToModelFields(fs));
    }

    /** Adds unique index to the entity. */
    public This withUnique(String indexId, Collection<ModelField> fs)
        throws DuplicateIndexException, NoAttributesIndexException
    {
        if (fs.isEmpty()) throw new NoAttributesIndexException(indexId, getId());
        if (uniqueIndexes.get(indexId) != null || indexes.get(indexId) != null) throw new DuplicateIndexException(indexId, getId());
        uniqueIndexes.put(indexId, fs);
        return cast(this);
    }

    /** Return the database name of the object. */
    public QName getDatabaseName() {
        return dbNameGenerated
               ? QName.createQName(defaultSchemaId, Names.tableName(getId()))
               : databaseName.getQualification().isEmpty() ? QName.createQName(defaultSchemaId, databaseName.getName()) : databaseName;
    }

    protected void addFieldsByName(Map<String, Attribute> attributeMap, Iterable<ModelField> fs, Collection<? super Attribute> destination) {
        for (final ModelField field : fs) {
            final Attribute attribute = attributeMap.get(field.getName());
            if (attribute != null) destination.add(attribute);
            else builderErrors.add(new FieldNotFoundException(getId(), field.getName()));
        }
    }

    protected void buildIndexes(Map<String, Collection<ModelField>> indexesMap, Map<String, Attribute> map, Map<String, Seq<Attribute>> finalMap) {
        for (final String indexId : indexesMap.keySet()) {
            final List<Attribute> fs = new ArrayList<>();
            addFieldsByName(map, indexesMap.get(indexId), fs);
            finalMap.put(indexId, Colls.seq(fs));
        }
    }

    protected List<ModelField> convertToModelFields(String[] fs) {
        final List<ModelField> result = new ArrayList<>();
        for (final String s : fs)
            result.add(unresolvedFieldRef(s));
        return result;
    }

    void addAllFields(Map<String, Attribute> attributeMap, Iterable<ModelField> fs, Collection<ModelField> destination) {
        for (final ModelField field : fs) {
            final Attribute attribute = attributeMap.get(field.getName());
            if (attribute != null) destination.add(attribute);
            else
            // Leave unresolved
            destination.add(field);
        }
    }

    void buildSearchByFields(@NotNull final Map<String, SearchFieldBuilder> searchByFieldMap, Map<String, Attribute> attributeMap,
                             @NotNull final List<SearchField> searchBy, boolean notFoundError)
        throws BuilderException
    {
        for (final SearchFieldBuilder b : searchByFieldMap.values()) {
            final String fieldName = b.getName();
            if (!attributeMap.containsKey(fieldName) && notFoundError) throw searchableFieldNotFound(getId(), fieldName);
            else {
                final Attribute attr = attributeMap.get(fieldName);
                if (attr != null && attr.hasOption(ABSTRACT) && !attr.hasOption(READ_ONLY)) throw new IllegalAbstractSearchableField(fieldName);
                if (hasModifier(Modifier.DATABASE_SEARCHABLE) && attr != null) {
                    if (attr.hasOption(ABSTRACT)) throw illegalAbstract(fieldName);
                }
                searchBy.add(b.build());
            }
        }
    }  // end method buildSearchByFields
}  // end class DbObjectBuilder
