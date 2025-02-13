
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.expr.ExpressionAST;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.md.MdConstants;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.MissingDefaultWidgetForTypeException;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.Types;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.check.CheckType.ENTITY;
import static tekgenesis.check.CheckType.TITLE;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.ID;
import static tekgenesis.common.core.Constants.SHOW_DEPRECABLE_INFO;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.metadata.form.widget.ButtonType.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.metadata.form.widget.WidgetBuilder.listWidgetBuilder;
import static tekgenesis.metadata.form.widget.WidgetBuilder.widgetBuilder;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.type.permission.PredefinedPermission.CREATE;
import static tekgenesis.type.permission.PredefinedPermission.HANDLE_DEPRECATED;

/**
 * Generates a default {@link Form} for a given {@link Entity}.
 */
public class DefaultUiModelGenerator<T extends MetaModel, B extends UiModelBuilder<T, B>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final B               builder;
    @NotNull private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    private DefaultUiModelGenerator(@NotNull final ModelRepository repository, @NotNull final B builder) {
        this.builder    = builder.setGenerated(true);
        this.repository = repository;
    }

    //~ Methods ......................................................................................................................................

    private WidgetBuilder builderForAttribute(DbObject dbObject, Attribute a)
        throws BuilderException
    {
        final WidgetBuilder result;
        if (dbObject.isPrimaryKey(a) && a.isSerial()) result = widgetBuilder(INTERNAL).withBinding(a).optional();
        else result = builderForTypeField(a);

        return result;
    }

    private WidgetBuilder builderForTypeField(TypeField f)
        throws BuilderException
    {
        return widgetBuilder(f.getFinalType(), f.isMultiple(), f.isSynthesized()).label(f.getLabel()).withBinding(f);
    }

    private Set<String> collectCollidingIds(@NotNull DbObject dbObject) {
        final Set<String> colliding = new HashSet<>();
        final Set<String> visited   = new HashSet<>();

        for (final Attribute field : dbObject.allAttributes()) {
            registerVisit(colliding, visited, field);

            if (field.isMultiple() && field.isEntity()) {
                final DbObject tableEntity = getInnerEntity(field.asDatabaseObject());
                for (final Attribute column : tableAttributes(field, tableEntity))
                    registerVisit(colliding, visited, column);
            }
        }

        return colliding;
    }

    private B generate()
        throws BuilderException
    {
        for (final DbObject binding : builder.getBinding().castTo(DbObject.class)) {
            if (builder.isListing()) {
                generateHeader(false, true);
                generateListingEntityTable(binding);
            }
            else {
                if (builder instanceof FormBuilder) generateHeader(binding.isSearchable(), false);
                generateEntityFields(binding);
                if (builder instanceof FormBuilder) generateFooter(binding.isDeprecable());
            }
        }

        for (final StructType binding : builder.getBinding().castTo(StructType.class))
            generateTypeFields(binding);

        return builder;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private WidgetBuilder generateActionsGroupForTable(String tableName)
        throws BuilderException
    {
        final WidgetBuilder actionsGroup = horizontalGroup("").styleClass(MARGIN_TOP_20);
        final WidgetBuilder addButton    = button(ADD_ROW).table(tableName).styleClass(MARGIN_RIGHT_5);
        final WidgetBuilder removeButton = button(REMOVE_ROW).table(tableName);

        actionsGroup.addWidget(addButton);
        actionsGroup.addWidget(removeButton);
        return actionsGroup;
    }

    /** Generate form fields for databaseObject. */
    private void generateEntityFields(DbObject dbObject)
        throws BuilderException
    {
        final Set<String> colliding = collectCollidingIds(dbObject);

        for (final Attribute a : dbObject.allAttributes()) {
            if (!a.isSynthesized() || a.getName().equals(ID)) {
                final WidgetBuilder widgetBuilder = builderForAttribute(dbObject, a);

                if (a.isMultiple() && a.isEntity()) generateTableFields(widgetBuilder, a, colliding);

                builder.addWidget(widgetBuilder);

                if (a.isMultiple() && a.isEntity()) builder.addWidget(generateActionsGroupForTable(a.getName()));
            }
        }
    }

    /**
     * Generate Form footer.
     *
     * @param  deprecable  to add or not the deprecate toggle button.
     */
    private void generateFooter(boolean deprecable)
        throws BuilderException
    {
        final WidgetBuilder footer = footer();

        footer.addWidget(button(SAVE)).addWidget(button(ButtonType.CANCEL));

        footer.addWidget(button(DELETE).styleClass(FormConstants.PULL_RIGHT));

        if (deprecable) footer.addWidget(toggleButton(ToggleButtonType.DEPRECATE).styleClass(FormConstants.PULL_RIGHT));

        builder.addWidget(footer);
    }

    /** Generate Form header. */
    private void generateHeader(final boolean addSearchBox, boolean isListing)
        throws BuilderException
    {
        final WidgetBuilder header = header();

        header.addWidget(widgetBuilder(MESSAGE).msg(isListing ? TITLE : ENTITY));

        if (addSearchBox) header.addWidget((widgetBuilder(SEARCH_BOX)).widgetCol(4).styleClass(FormConstants.PULL_RIGHT));

        builder.addWidget(header);
    }

    /** Generate a table listing the databaseObject. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void generateListingEntityTable(DbObject dbObject)
        throws BuilderException
    {
        final String        dbObjectName = dbObject.getName();
        final String        tableName    = deCapitalizeFirst(pluralize(dbObjectName));
        final WidgetBuilder tableBuilder = listWidgetBuilder(dbObject.getFinalType()).id(tableName);

        final ExpressionAST forbidden  = forbidden(HANDLE_DEPRECATED.getName());
        final boolean       deprecable = dbObject.isDeprecable();

        for (final Attribute a : dbObject.allAttributes()) {
            final String name = a.getName();
            if (!MdConstants.isVersionField(name)) {
                WidgetBuilder forAttribute = builderForAttribute(dbObject, a);
                if (deprecable && (name.equals(MdConstants.DEPRECATION_TIME) || name.equals(MdConstants.DEPRECATION_USER)))
                    forAttribute = forAttribute.hideColumn(or(forbidden, eq(ref(SHOW_DEPRECABLE_INFO), bool(false))));

                tableBuilder.addWidget(forAttribute);
            }
        }

        if (deprecable) builder.addWidget(check("Show deprecable").id(SHOW_DEPRECABLE_INFO).hide(forbidden));

        builder.addWidget(tableBuilder);

        final WidgetBuilder actionsGroup = horizontalGroup("").styleClass(MARGIN_TOP_20);
        final WidgetBuilder addButton    = button(ADD_ROW).table(tableName).styleClass(MARGIN_RIGHT_5).disable(forbidden(CREATE.getName()));
        final WidgetBuilder removeButton = button(REMOVE_ROW).table(tableName)
                                           .onClick("remove" + dbObjectName)
                                           .disable(forbidden(PredefinedPermission.DELETE.getName()));

        actionsGroup.addWidget(addButton);
        actionsGroup.addWidget(removeButton);

        builder.addWidget(actionsGroup);
    }

    /**
     * Generate Table fields: 1- Filter all primary keys for inner entities 2- Filter reverse
     * reference attribute 3- Do not filter sequence or other primary keys nor other attributes
     */
    private void generateTableFields(@NotNull WidgetBuilder inner, @NotNull final Attribute multiple, @NotNull Set<String> colliding)
        throws BuilderException
    {
        final DbObject entity = getInnerEntity(multiple.asDatabaseObject());

        for (final Attribute a : tableAttributes(multiple, entity).filter(this::isNotSynthesized)) {
            final WidgetBuilder widgetBuilder = builderForAttribute(entity, a);
            if (a.isSerial() || colliding.contains(a.getName())) widgetBuilder.id(multiple.getName() + capitalizeFirst(a.getName()));
            inner.addWidget(widgetBuilder);
        }
    }

    /** Generate widget definitional fields for struct type. */
    private void generateTypeFields(StructType type)
        throws BuilderException
    {
        for (final TypeField f : type.getChildren()) {
            if (!f.isSynthesized() || f.getName().equals(ID)) {
                WidgetBuilder wb = null;

                if (f.isType()) wb = getDefaultWidgetForType(f, f.getType(), f.getName(), f.getLabel());
                else {
                    final Type elementType = Types.elementType(f.getType());
                    if (elementType != null) {
                        final WidgetBuilder defaultW = getDefaultWidgetForType(null,
                                elementType,
                                singularize(f.getName()),
                                singularize(f.getLabel()));
                        if (defaultW != null)
                            wb = widgetBuilder(SECTION).id(f.getName()).with(FieldOption.NO_LABEL).withBinding(f).addWidget(defaultW);
                    }
                    else wb = builderForTypeField(f);
                }
                // if (f.isMultiple() && f.isEntity()) generateTableFields(wb, f, colliding);

                if (wb != null) builder.addWidget(wb);

                // if (f.isMultiple() && f.isEntity()) builder.addWidget(generateActionsGroupForTable(f.getName()));
            }
        }
    }

    /** Predicate that filters inner entities primary keys. */
    private Predicate<Attribute> innerPks(@NotNull final DbObject dbObject) {
        return a -> !dbObject.isInner() || a != null && !dbObject.isPrimaryKey(a);
    }

    /** Predicate that filters reverse references attributes. */
    private Predicate<Attribute> references(@NotNull final Attribute multiple) {
        return a -> a != null && !multiple.getReverseReference().equals(a.getName());
    }

    private void registerVisit(Set<String> colliding, Set<String> visited, Attribute field) {
        final String name = field.getName();
        if (!visited.add(name)) colliding.add(name);
    }

    private Seq<Attribute> tableAttributes(Attribute multiple, DbObject dbObject) {
        return dbObject.allAttributes().filter(innerPks(dbObject)).filter(references(multiple));
    }

    private WidgetBuilder toggleButton(ToggleButtonType type)
        throws BuilderException
    {
        return widgetBuilder(TOGGLE_BUTTON).toggleButtonType(type);
    }

    private boolean isNotSynthesized(@Nullable final Attribute attribute) {
        return attribute != null && (!attribute.isSynthesized() || attribute.getDbObject().isPrimaryKey(attribute));
    }

    @Nullable private WidgetBuilder getDefaultWidgetForType(@Nullable TypeField tf, Type f, String name, String label)
        throws BuilderException
    {
        final Option<StructType> t = repository.getModel(f.getImplementationClassName(), StructType.class);
        if (t.isPresent()) {
            final StructType field = t.get();
            final String     dw    = field.getDefaultWidget();
            if (isEmpty(dw)) throw new MissingDefaultWidgetForTypeException(field.getFullName(), builder.getFullName());
            final WidgetBuilder wb = widgetBuilder(WIDGET).id(name).label(label).widgetDef(dw);
            return tf != null ? wb.withBinding(tf) : wb.withPlaceholderBinding();
        }
        return null;
    }

    private DbObject getInnerEntity(Option<DbObject> type) {
        return type.getOrFail("Should be an Entity Type!");
    }

    //~ Methods ......................................................................................................................................

    /** Populates the given {@link UiModelBuilder} if binding is present. */
    public static <T extends MetaModel, B extends UiModelBuilder<T, B>> B defaultUiModel(@NotNull final ModelRepository repository,
                                                                                         @NotNull final B               builder)
        throws BuilderException
    {
        return new DefaultUiModelGenerator<T, B>(repository, builder).generate();
    }

    //~ Static Fields ................................................................................................................................

    private static final String MARGIN_TOP_20  = "margin-top-20";
    private static final String MARGIN_RIGHT_5 = "margin-right-5";
}  // end class DefaultUiModelGenerator
