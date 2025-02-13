
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.ColumnMatcherEnum;
import tekgenesis.admin.util.CacheUtils;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.logging.Logger;
import tekgenesis.console.dynamic.WidgetBuilderFactory;
import tekgenesis.database.Database;
import tekgenesis.database.SqlStatement;
import tekgenesis.form.Action;
import tekgenesis.form.DynamicFormAccessor;
import tekgenesis.form.DynamicFormTable;
import tekgenesis.form.DynamicFormWidgetAccessor;
import tekgenesis.form.DynamicInstance;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EnumBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.persistence.*;
import tekgenesis.persistence.resource.DbResource;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.admin.Message.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.TABLE;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.env.context.Context.getContext;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.util.Reflection.*;
import static tekgenesis.console.EntityFixerDynamicForm.DActions.*;
import static tekgenesis.console.EntityFixerDynamicForm.DField.*;
import static tekgenesis.console.dynamic.Utils.getEntityReferenceFieldName;
import static tekgenesis.console.dynamic.Utils.getLabel;
import static tekgenesis.database.Databases.openDefault;
import static tekgenesis.database.SqlConstants.WHERE;
import static tekgenesis.field.FieldOption.SCROLLABLE;
import static tekgenesis.field.FieldOption.SORTABLE;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.persistence.Sql.databaseFor;

/**
 * Entity Fixer Form.
 */
public class EntityFixerDynamicForm<T extends EntityInstance<T, K>, K> extends DynamicInstance {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings({ "DuplicateStringLiteralInspection", "OverlyLongMethod", "MagicNumber" })
    public void build(@NotNull FormBuilder builder, @Nullable final String param)
        throws BuilderException
    {
        final WidgetBuilder filter = verticalGroup("Filter").id("vFilter");
        builder.addWidget(filter);

        final WidgetBuilder entityFqn = internal(ENTITY_FQN);
        builder.addWidget(entityFqn);

        final WidgetBuilder filterOpts = horizontalGroup("");
        filter.addWidget(filterOpts);
        filterOpts.addWidget(combo("").id(COLUMN_CMB.name()));
        final EnumType matchType = EnumBuilder.enumType(ColumnMatcherEnum.class);
        filterOpts.addWidget(combo("").id(MATCH_CMB.name()).withType(matchType).styleClass("margin-left-10"));
        filterOpts.addWidget(field("").id(VALUE.name()).length(255).styleClass("margin-left-10"));

        filterOpts.addWidget(button(AND.label()).id("andBtn").styleClass("margin-left-10").onClick(AND_EXPRESSION.name()));
        filterOpts.addWidget(button(OR.label()).id("orBtn").styleClass("margin-left-10").onClick(OR_EXPRESSION.name()));

        final WidgetBuilder textExp = horizontalGroup("").id("textExp");
        filter.addWidget(textExp);

        textExp.addWidget(area("").id(EXPRESSION.name()));
        textExp.addWidget(
            button("").id("cleanExpression").styleClass("margin-left-10").contentStyleClass("fa fa-eraser").onClick(CLEAN_EXP_BOX.name()));

        builder.addWidget(button(SEACH.label()).id(DField.SEARCH.name()).onClick(DActions.SEARCH.name()));

        final Option<DbObject> model = getDatabaseObject(param);
        if (model.isPresent()) {  // Adding Table's columns
            final WidgetBuilder table = table("").id(TABLE_RESULT.name())
                                        .with(SORTABLE)
                                        .with(SCROLLABLE)
                                        .displayedItems(10)
                                        .styleClass("entity_table");

            final DbObject       entity     = model.get();
            final Seq<Attribute> attributes = entity.allAttributes();

            final List<WidgetBuilder> columns = new ArrayList<>();
            // noinspection SpellCheckingInspection
            columns.add(button("").onClick(REFRESH_CACHE_ELEMENT.name()).contentStyleClass("fa fa-retweet"));
            columns.add(internal(INSTANCE_ID));
            final ImmutableList<Attribute> primaryKeys = entity.getPrimaryKey();

            for (final Attribute attribute : attributes) {
                if (!attribute.isMultiple()) {
                    final WidgetBuilder widgetBuilder;
                    if (attribute.getFinalType().isResource()) {
                        final String label      = getLabel(attribute);
                        final String columnName = attribute.getColumnName();
                        columns.add(field(label).optional().id(columnName));
                        widgetBuilder = label("view").onClick(SHOW_IMAGE + columnName);
                    }
                    else widgetBuilder = WidgetBuilderFactory.create(primaryKeys, attribute);
                    columns.add(widgetBuilder);
                }
            }

            table.children(columns.toArray(new WidgetBuilder[columns.size()])).onChange(UPDATE_ENTITY.name());
            builder.addWidget(table);
        }
    }  // end method build

    @NotNull @Override public Action cancel(@NotNull DynamicFormAccessor form, String param) {
        return actions.getDefault();
    }

    @NotNull @Override public Action create(@NotNull DynamicFormAccessor form, @Nullable String param) {
        return actions.getDefault();
    }

    @NotNull @Override public Action delete(@NotNull DynamicFormAccessor form, String param) {
        return actions.getDefault();
    }

    @NotNull @Override public Action handleClick(@NotNull DynamicFormAccessor form, @NotNull String methodName) {
        Action result = actions.getDefault();

        if (methodName.startsWith(SHOW_IMAGE)) {
            final String fieldId = methodName.substring(SHOW_IMAGE.length());
            final String imgUuid = form.getTable(TABLE_RESULT.name()).getCurrentRow().get().getString(fieldId);
            return actions.navigate(ViewResource.class, imgUuid).dialog();
        }

        final DActions buttonActions = DActions.valueOf(methodName);
        switch (buttonActions) {
        case AND_EXPRESSION:
        case OR_EXPRESSION:
            result = buildExpression(form, buttonActions);
            break;
        case SEARCH:
            result = executeQuery(form);
            break;
        case CLEAN_EXP_BOX:
            form.setFieldValue(DField.EXPRESSION.name(), "");
            break;
        case UPDATE_ENTITY:
            result = updateEntity(form);
            break;
        case REFRESH_CACHE_ELEMENT:
            result = refreshCacheElement(form);
            break;
        }

        return result;
    }  // end method handleClick

    public void populate(@NotNull DynamicFormAccessor form, @NotNull String param) {
        final Option<DbObject> entityModel = getDatabaseObject(param);

        form.setFieldValue(ENTITY_FQN, param);
        if (entityModel.isPresent()) {
            final Seq<Attribute> attributes = entityModel.get().allAttributes();
            final List<String>   cols       = new ArrayList<>();

            for (final Attribute attribute : attributes) {
                final String columnName = attribute.getColumnName();
                if (attribute.isEntity()) {
                    final Entity                   entity     = (Entity) attribute.getFinalType();
                    final ImmutableList<Attribute> primaryKey = entity.getPrimaryKey();
                    for (final Attribute pkAttr : primaryKey)
                        cols.add(columnName + "_" + pkAttr.getColumnName());
                }
                else cols.add(columnName);
            }

            form.setOptions(COLUMN_CMB.name(), Colls.seq(cols));
        }
    }

    @NotNull @Override public Action update(@NotNull DynamicFormAccessor form, @NotNull String param) {
        return actions.getDefault();
    }

    private Action buildExpression(DynamicFormAccessor form, DActions buttonActions) {
        final String value   = notNull(form.getString(VALUE.name()), "");
        final String column  = notNull(form.getString(COLUMN_CMB.name()), "");
        final String matcher = notNull(form.getString(MATCH_CMB.name()), "");

        if (isEmpty(matcher) || isEmpty(column) ||
            (isEmpty(value) && !(ColumnMatcherEnum.IS_NULL.name().equals(matcher) || ColumnMatcherEnum.IS_NOT_NULL.name()
                                                                                                                  .equals(matcher))))
            return actions.getError().withMessage(MISSED_FIELDS.label());

        final ColumnMatcherEnum columnMatcher = ColumnMatcherEnum.valueOf(matcher);

        String expression = notNull(form.getString(EXPRESSION.name()), "");

        final String operator = buttonActions == AND_EXPRESSION ? " and " : " or ";

        if (isNotEmpty(expression)) expression = expression + operator;

        expression = expression + expressionBuilder(columnMatcher, column, value);

        form.setFieldValue(EXPRESSION.name(), expression);

        return actions.getDefault();
    }

    private Action executeQuery(@NotNull DynamicFormAccessor form) {
        final String entityFqn = form.getString(ENTITY_FQN);

        final EntityTable<T, K>   et       = EntityTable.forName(entityFqn);
        final DbTable<T, K>       dbTable  = et.getDbTable();
        final TableMetadata<T, K> metadata = dbTable.metadata();

        try {
            final String       expression = form.getString(DField.EXPRESSION.name());
            final String       queryExp   = isEmpty(expression) ? "" : WHERE + expression;
            final SqlStatement stmt       = databaseFor(dbTable).sqlStatement("select * from TableName(%s,%s) %s",
                    metadata.getSchemaName(),
                    metadata.getTableName(),
                    queryExp);

            return fillResultTable(form, metadata.getType(), stmt.list(metadata.getRowMapper()), metadata.getTypeName());
        }
        catch (final Exception e) {
            logger.error(e);
            return actions.getError().withMessage(e.getMessage());
        }
    }  // end method executeQuery

    private String expressionBuilder(@NotNull ColumnMatcherEnum matcher, @NotNull String columnName, @NotNull String value) {
        final String exp;
        switch (matcher) {
        case IN:
        case NOT_IN:
            exp = mkString(Strings.split(value, ',').map(this::singleQuote));
            break;
        case IS_NULL:
        case IS_NOT_NULL:
            exp = "";
            break;
        default:
            exp = singleQuote(value);
        }

        return columnName + SEPARATOR + matcher.getSql() + SEPARATOR + exp;
    }

    private Action fillResultTable(@NotNull DynamicFormAccessor form, final Class<?> aClass, ImmutableList<T> list, String typeName) {
        Action         action = actions.getDefault();
        final DbObject entity = getDatabaseObject(typeName).getOrFail("Not Found");

        final DynamicFormTable table = form.getTable(TABLE_RESULT.name());
        table.clear();

        try {
            for (final T entityInstance : list) {
                final DynamicFormWidgetAccessor row = table.add();

                final Seq<Attribute> attributes = entity.allAttributes();

                row.setFieldValue(INSTANCE_ID, entityInstance.keyAsString());

                for (final Attribute attribute : attributes) {
                    final Type type = attribute.getType().getFinalType();

                    if (!type.isInner() && !attribute.isMultiple()) {
                        final String columnName = attribute.getColumnName();
                        String       widgetName = columnName;
                        if (type.isEntity()) widgetName = getEntityReferenceFieldName(type, columnName);

                        final String fieldName  = Strings.deCapitalizeFirst(Strings.toCamelCase(columnName));
                        final String methodName = Strings.getterName(fieldName, type.toString());
                        final Object value      = findMethod(aClass, methodName).get().invoke(entityInstance);
                        row.setFieldValue(widgetName, value);
                    }
                }
            }
        }
        catch (final Exception e) {
            logger.error(e);
            action = actions.getError().withMessage(e.getMessage());
        }
        return action;
    }

    private Action refreshCacheElement(@NotNull DynamicFormAccessor form) {
        final DynamicFormTable                  table      = form.getTable(TABLE_RESULT.name());
        final Option<DynamicFormWidgetAccessor> currentRow = table.getCurrentRow();
        if (currentRow.isPresent()) {
            final String entityFqn  = form.getString(ENTITY_FQN);
            final String instanceId = currentRow.get().getString(INSTANCE_ID);
            CacheUtils.clearCache(entityFqn, instanceId);
        }
        return actions.getDefault();
    }

    @Nullable private Object resolveValue(@NotNull DynamicFormWidgetAccessor row, @NotNull Attribute attribute, @NotNull Type type,
                                          @NotNull String columnName) {
        if (type.isTime()) return type.getKind() == Kind.DATE ? row.getDateOnly(columnName) : row.getDateTime(columnName);

        if (type.isBoolean()) return row.is(columnName);

        if (type.isNumber()) return type.getKind() == Kind.DECIMAL ? row.getDecimal(columnName) : row.getInt(columnName);

        if (type.isEnum()) {
            final String enumValue = row.getString(columnName);
            return enumValue == null ? null : attribute.getType().valueOf(enumValue);
        }

        return type.isString() ? row.getString(columnName) : row.getObject(columnName);
    }

    private String singleQuote(@NotNull String value) {
        return "'" + value + "'";
    }

    private Action updateEntity(@NotNull DynamicFormAccessor form) {
        final DynamicFormTable                  table      = form.getTable(TABLE_RESULT.name());
        final Option<DynamicFormWidgetAccessor> currentRow = table.getCurrentRow();
        if (!currentRow.isPresent()) return actions.getError().withMessage(ROW_NOT_SELECTED);

        final String entityFqn = form.getString(ENTITY_FQN);
        if (getDatabaseObject(entityFqn).isEmpty()) return actions.getError().withMessage(ENTITY_NOT_FOUND, entityFqn);

        try {
            final T      instance = getEntityInstance(entityFqn);
            final Method m        = instance.getClass().getSuperclass().getDeclaredMethod("data");
            m.setAccessible(true);
            final Object                    data = m.invoke(instance);
            final DynamicFormWidgetAccessor row  = currentRow.get();
            for (final Attribute attribute : getDatabaseObject(entityFqn).get().allAttributes()) {
                final Type type = attribute.getType().getFinalType();

                final String columnName = attribute.getColumnName();
                final String fieldName  = Strings.deCapitalizeFirst(Strings.toCamelCase(columnName));
                if (!type.isEntity()) {
                    Object         obj = resolveValue(row, attribute, type, columnName);
                    final Database db  = openDefault();
                    if (type.isResource() && obj != null) obj = new DbResource(db, (String) obj);
                    setPrivateField(data, fieldName, obj);
                }
                else if (!type.isInner() && !attribute.isMultiple()) {
                    final EntityTable<T, K> entityTable = getEntityTable(instance, fieldName);

                    final String rowFieldName = getEntityReferenceFieldName(type, columnName);
                    final String key          = row.getString(rowFieldName);
                    if (key != null) {
                        final EntityTable<?, ?> ti = cast(entityTable);
                        invoke(instance, setterName(fieldName), ti.findByString(key));
                    }
                }
            }

            if (instance instanceof PersistableInstance) ((PersistableInstance<?, ?>) instance).update();
        }
        catch (final Exception e) {
            logger.error(e);
            return actions.getError().withMessage(e.getMessage());
        }
        return actions.getDefault();
    }  // end method updateEntity

    private Option<DbObject> getDatabaseObject(@Nullable String param) {
        final ModelRepository modelRepository = getContext().getSingleton(ModelRepository.class);
        if (param == null) return Option.empty();
        return modelRepository.getModel(QName.createQName(param), DbObject.class);
    }

    private T getEntityInstance(@NotNull String entityFqn)
        throws InstantiationException, IllegalAccessException
    {
        final Class<T> aClass = findClass(entityFqn);
        return aClass.newInstance();
    }

    private EntityTable<T, K> getEntityTable(@NotNull T entityInstance, @NotNull String fieldName) {
        final EntityRef<T, K> fieldValue = getPrivateField(entityInstance, fieldName);
        return ensureNotNull(getPrivateField(ensureNotNull(fieldValue), TABLE));
    }

    //~ Static Fields ................................................................................................................................

    private static final String ENTITY_FQN  = "entityFqn";
    private static final String INSTANCE_ID = "instanceId";
    private static final String SHOW_IMAGE  = "showImage";
    private static final String SEPARATOR   = " ";
    private static final Logger logger      = getLogger(EntityFixerDynamicForm.class);

    //~ Enums ........................................................................................................................................

    enum DActions { AND_EXPRESSION, OR_EXPRESSION, SEARCH, CLEAN_EXP_BOX, UPDATE_ENTITY, REFRESH_CACHE_ELEMENT }

    enum DField { EXPRESSION, TABLE_RESULT, SEARCH, VALUE, COLUMN_CMB, MATCH_CMB }
}  // end class EntityFixerDynamicForm
