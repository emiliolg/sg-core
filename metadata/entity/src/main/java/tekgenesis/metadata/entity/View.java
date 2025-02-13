
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
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.CacheType;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.ModelField;
import tekgenesis.type.Kind;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * This class represents a View.
 */
public class View extends DbObject {

    //~ Instance Fields ..............................................................................................................................

    private final String asQuery;

    private Map<String, Attribute> attributesByBaseAttributeName = null;
    private final Integer          batchSize;

    private List<MetaModel>          entities;
    private final List<Attribute>    primaryKey;
    private ImmutableList<Attribute> primaryKeyAttributes        = null;
    private final boolean            updatable;

    //~ Constructors .................................................................................................................................

    /** Constructs a View. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    View(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String label, @NotNull EnumSet<Modifier> modifiers,
         @NotNull QName viewName, boolean viewNameGenerated, @NotNull Map<String, Attribute> attributeMap, List<ModelField> describeFields,
         boolean updatable, List<SearchField> searchByFields, @NotNull LinkedHashMap<String, Seq<Attribute>> uniqueIndexes,
         @NotNull Map<String, Seq<Attribute>> indexes, @Nullable String asQuery, List<Attribute> primaryKeyFields, List<MetaModel> entities,
         List<ModelField> image, @NotNull String documentation, @Nullable Integer batchSize) {
        super(sourceName,
            domain,
            name,
            label,
            modifiers,
            "",
            attributeMap,
            viewName,
            viewNameGenerated,
            describeFields,
            searchByFields,
            CacheType.NONE,
            uniqueIndexes,
            indexes,
            image,
            documentation);
        this.updatable = updatable;
        this.asQuery   = notNull(asQuery, "");
        this.entities  = entities;
        this.batchSize = batchSize;
        primaryKey     = primaryKeyFields;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Seq<Attribute> allAttributes() {
        if (getBaseEntity().isPresent()) {
            final List<Attribute> result = new ArrayList<>();
            for (final Attribute attribute : getBaseEntity().get().getPrimaryKey()) {
                if (!getAttributesByBaseAttributeName().containsKey(attribute.getName())) result.add(attribute);
            }
            result.addAll(attributes().toList());
            return seq(result);
        }
        else return attributes();
    }

    /** Return references entities. */
    public Collection<MetaModel> entities() {
        return entities;
    }

    @Override public boolean hasCompositePrimaryKey() {
        return getBaseEntity().isPresent() && getBaseEntity().get().hasCompositePrimaryKey();
    }

    @Override public boolean hasDefaultPrimaryKey() {
        return getBaseEntity().isPresent() && getBaseEntity().get().hasDefaultPrimaryKey();
    }

    /** Replace entities. */
    public void replaceEntities(ArrayList<DbObject> resolved) {
        entities.clear();
        entities.addAll(resolved);
    }

    /** Solves all references on views of entities and other views. */
    public void solveMetaModels(Function<MetaModel, MetaModel> solver) {
        entities = map(entities, solver).toList();
    }

    /** Return as SQL query. */
    public String getAsQuery() {
        return asQuery;
    }
    /** Returns the view attribute with that base attribute name. */
    @NotNull public Option<Attribute> getAttributeByBaseAttributeName(String baseAttributeName) {
        return option(getAttributesByBaseAttributeName().get(baseAttributeName));
    }

    /** Returns the entity. */
    @NotNull public Option<DbObject> getBaseEntity() {
        if (!asQuery.isEmpty() || entities.size() != 1) return Option.empty();
        final MetaModel baseEntity = entities.get(0);
        if (baseEntity instanceof DbObject) return some(((DbObject) baseEntity));
        else throw new IllegalStateException("Entity could not be resolved or has not been resolved yet");
    }

    /** Returns the entity model type. */
    @Nullable public MetaModel getBaseEntityModelType() {
        return entities.isEmpty() ? null : entities.get(0);
    }

    /** Return batch size for remote view synchronization. */
    @Nullable public Integer getBatchSize() {
        return batchSize;
    }

    @Override public boolean isComposite() {
        return true;
    }

    /** Indicates whether the View is Remote or not. */
    public boolean isRemote() {
        return hasModifier(REMOTE);
    }

    /** Indicates whether the View is Editable or not. */
    public boolean isUpdatable() {
        return updatable;
    }

    @NotNull @Override public Option<Attribute> getField(String fieldName) {
        return getAttribute(fieldName);
    }

    @NotNull @Override public Kind getKind() {
        return Kind.REFERENCE;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.VIEW;
    }

    @NotNull @Override public Option<Entity> getParent() {
        final Option<DbObject> base = getBaseEntity();
        return base.isPresent() ? base.get().getParent() : super.getParent();
    }

    @NotNull @Override public ImmutableList<Attribute> getPrimaryKey() {
        if (primaryKeyAttributes == null) primaryKeyAttributes = calculatePrimaryKey();
        return primaryKeyAttributes;
    }

    @Override public boolean isInner() {
        return getBaseEntity().isPresent() && getBaseEntity().get().isInner();
    }

    @Override public Seq<MetaModel> getReferences() {
        return !isRemote() ? immutable(entities) : Colls.emptyList();
    }

    @Override public boolean isView() {
        return true;
    }

    @Override public boolean isPrimaryKey(@NotNull Attribute attribute) {
        return getPrimaryKey().contains(attribute);
    }

    @Override protected List<SearchField> notEmptySearchByFields() {
        return modelFieldsToSearchFields(describes().isEmpty() ? getPrimaryKey() : describes());
    }

    private Attribute attributeByBaseName(Attribute attr) {
        return getAttributesByBaseAttributeName().getOrDefault(attr.getName(), attr);
    }

    private Map<String, Attribute> buildAttributesByBaseAttributeName(Seq<Attribute> as) {
        final Map<String, Attribute> result = new LinkedHashMap<>();
        for (final Attribute a : as) {
            if (a instanceof ViewAttribute) {
                final ViewAttribute viewAttribute = (ViewAttribute) a;
                result.put(viewAttribute.getBaseAttributeModelField().getName(), viewAttribute);
            }
        }
        return result;
    }

    private ImmutableList<Attribute> calculatePrimaryKey() {
        if (!primaryKey.isEmpty()) return immutable(primaryKey);

        if (!isEmpty(getAsQuery())) return emptyList();

        return getBaseEntity()                                  //
               .map(DbObject::getPrimaryKey)                    //
               .map(pk -> pk.map(this::attributeByBaseName).toList())  //
               .orElse(emptyList());
    }

    private Map<String, Attribute> getAttributesByBaseAttributeName() {
        if (attributesByBaseAttributeName == null) attributesByBaseAttributeName = buildAttributesByBaseAttributeName(attributes());
        return attributesByBaseAttributeName;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -572105091881323844L;
}  // end class View
