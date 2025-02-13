
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

import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.md.MdConstants;
import tekgenesis.metadata.exception.*;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.type.FieldReference.unresolvedFieldRef;
import static tekgenesis.type.Modifier.OPTIMISTIC_LOCKING;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * Collects the data to build an {@link View}.
 */
public class ViewBuilder extends DbObjectBuilder<View, ViewAttribute, AttributeBuilder, ViewBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private String  asQuery   = "";
    private Integer batchSize = null;

    @NotNull private final List<ModelField> describeFields;
    @NotNull private final List<MetaModel>  entities;

    private List<ModelField> image;

    @NotNull private final List<ModelField> searchFields;
    private boolean                         updatable;

    //~ Constructors .................................................................................................................................

    /** Constructs a ViewBuilder. */
    public ViewBuilder(String sourceName, @NotNull String pkg, @NotNull String name, @NotNull String defaultSchemaId) {
        super(sourceName, pkg, name, defaultSchemaId);
        describeFields = new ArrayList<>();
        searchFields   = new ArrayList<>();
        entities       = new ArrayList<>();
        image          = new ArrayList<>();
        updatable      = false;
        batchSize      = null;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Sets SQL query for view.
     *
     * @return  the same EntityBuilder
     */
    public ViewBuilder as(String query) {
        asQuery = query;
        return this;
    }

    /** Set batch size for remote view synchronization. */
    public ViewBuilder batchSize(int size) {
        batchSize = size;
        return this;
    }

    @Override public View build()
        throws BuilderException
    {
        final Map<String, TypeField>                fieldsByAttributeName = new LinkedHashMap<>();
        final Map<String, Attribute>                attributeMap          = cast(fieldsByAttributeName);
        final List<ModelField>                      describes             = new ArrayList<>();
        final List<Attribute>                       primaryKey            = new ArrayList<>();
        final List<SearchField>                     searchBy              = new ArrayList<>();
        final LinkedHashMap<String, Seq<Attribute>> uniqueMap             = new LinkedHashMap<>();
        final Map<String, Seq<Attribute>>           indexMap              = new HashMap<>();

        final boolean remote = modifiers.contains(REMOTE);
        final View    view   = new View(sourceName,
                domain,
                id,
                label,
                modifiers,
                getDatabaseName(),
                dbNameGenerated,
                attributeMap,
                describes,
                updatable,
                searchBy,
                uniqueMap,
                indexMap,
                asQuery,
                primaryKey,
                entities,
                image,
                documentation,
                batchSize);
        addInternalFields(remote);
        buildAttributes(fieldsByAttributeName, view);
        addFieldsByName(attributeMap, searchFields, describes);
        addAllFields(attributeMap, describeFields, describes);
        buildIndexes(uniqueIndexes, attributeMap, uniqueMap);
        buildIndexes(indexes, attributeMap, indexMap);
        buildPrimaryKey(primaryKeyFields, attributeMap, primaryKey);

        buildSearchByFields(searchByFields, attributeMap, searchBy, false);

        return view;
    }  // end method build

    @NotNull public List<BuilderError> check() {
        if (isEmpty(fields.entrySet())) builderErrors.add(new NoAttributesException(id));
        final boolean remote = modifiers.contains(REMOTE);
        if (remote) {
            if (!isEmpty(asQuery)) builderErrors.add(new RemoteViewWithAsException(id));
            checkEntities();
        }
        if ((!uniqueIndexes.isEmpty() || !indexes.isEmpty()) && !remote) builderErrors.add(new ViewWithIndexesException(id));
        return builderErrors;
    }

    /**
     * Sets the fields that describe the entity.
     *
     * @param   mf  primary key fields
     *
     * @return  the same EntityBuilder
     */
    public ViewBuilder describedBy(Collection<ModelField> mf) {
        describeFields.clear();
        describeFields.addAll(mf);
        return this;
    }

    /**
     * Sets the fields that describe the entity.
     *
     * @param   fs  primary key fields
     *
     * @return  the same EntityBuilder
     */
    public ViewBuilder describedBy(String... fs) {
        return describedBy(convertToModelFields(fs));
    }

    /** Sets the fields that act as image of the view. */

    public ViewBuilder image(ModelField img) {
        image = new ArrayList<>();
        image.add(img);
        return this;
    }

    /** Toggles the entity as optimistic. */
    public ViewBuilder optimistic() {
        return withModifier(OPTIMISTIC_LOCKING);
    }

    /**
     * Toggles the entity as editable.
     *
     * @return  the same ViewBuilder
     */
    @SuppressWarnings("UnusedReturnValue")
    public ViewBuilder updatable() {
        updatable = true;
        return this;
    }

    /** Sets entities references for SQL views. */
    public ViewBuilder withEntities(List<MetaModel> models) {
        entities.addAll(models);
        return this;
    }

    @Override protected void checkDuplicates(AttributeBuilder a)
        throws DuplicateFieldException
    {
        for (final AttributeBuilder builder : fields.values()) {
            if (builder instanceof ViewAttributeBuilder) {
                if (((ViewAttributeBuilder) builder).getBaseAttribute().getName().equals(a.getName()))
                    throw DuplicateFieldException.onEntity(a.getName(), id);
            }
        }
        super.checkDuplicates(a);
    }

    private void addInternalFields(boolean remote) {
        try {
            if (remote || updatable) addInternalField(createAttribute(MdConstants.UPDATE_TIME, Types.dateTimeType(3)).synthesized());
            if (updatable && hasModifier(OPTIMISTIC_LOCKING) && !remote)
                addInternalField(createAttribute(MdConstants.VERSION_FIELD, Types.longType()).synthesized());
            if (remote) {
                final MetaModel entity = entities.isEmpty() ? null : entities.get(0);
                if (entity != null) {
                    if (entity.isInner()) {
                        final String name            = ((Entity) entity).getParent().get().getName();
                        final String parentFieldName = deCapitalizeFirst(name);
                        addField(new ViewAttributeBuilder(parentFieldName, unresolvedFieldRef(parentFieldName)));
                    }
                    if (entity instanceof Entity && ((Entity) entity).isDeprecable()) {
                        addInternalField(createAttribute(MdConstants.DEPRECATION_TIME, Types.dateTimeType(3)).synthesized().optional());
                        addInternalField(createAttribute(MdConstants.DEPRECATION_USER, Types.stringType(100)).synthesized().optional());
                    }
                }
            }
        }
        catch (final BuilderException e) {
            builderErrors.add(e);
        }
    }

    private void buildPrimaryKey(List<ModelField> pks, Map<String, Attribute> attributeMap, List<Attribute> primaryKey) {
        addFieldsByName(attributeMap, pks, primaryKey);
    }

    private void checkEntities() {
        if (!entities.isEmpty()) {
            final MetaModel metaModel = entities.get(0);
            if (metaModel instanceof DbObject) {
                final DbObject dbObject = (DbObject) metaModel;
                if (!(dbObject.isRemotable() || dbObject.isSearchable()))
                    builderErrors.add(new RemoteViewOfNonRemotableException(id, metaModel.getFullName()));
            }
        }
    }

    // end method checkIndex

    private AttributeBuilder createAttribute(String name, Type type) {
        return new AttributeBuilder(name, type);
    }
}  // end class ViewBuilder
