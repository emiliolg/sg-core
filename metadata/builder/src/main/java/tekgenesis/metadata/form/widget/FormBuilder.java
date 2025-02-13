
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.CollidingPathException;
import tekgenesis.metadata.exception.InvalidPathException;
import tekgenesis.metadata.form.ref.UiModelReferenceMapper;
import tekgenesis.metadata.routing.Routes;
import tekgenesis.metadata.routing.Routing;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.permission.Permission;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.field.MetaModelReference.referenceMetaModel;
import static tekgenesis.metadata.form.widget.Form.DEFAULT_SCHEDULE_INTERVAL;
import static tekgenesis.type.Modifier.LISTING;
import static tekgenesis.type.permission.Permission.Custom.valueOf;

/**
 * Builder to create {@link Form} instances.
 */
public class FormBuilder extends UiModelBuilder<Form, FormBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Map<String, Permission> permissions;
    @NotNull private final List<ModelField>        primaryKeyFields;

    //~ Constructors .................................................................................................................................

    FormBuilder(@NotNull String sourceName, @NotNull String pkg, @NotNull String name) {
        this(sourceName, pkg, name, new FieldOptions());
    }

    FormBuilder(@NotNull String sourceName, @NotNull String pkg, @NotNull String name, @NotNull FieldOptions fieldOptions) {
        super(sourceName, pkg, name, WidgetType.FORM, fieldOptions);
        primaryKeyFields = new ArrayList<>();
        permissions      = new LinkedHashMap<>();
        addPredefinedPermissions();
    }

    //~ Methods ......................................................................................................................................

    /** Mark form as listing. */
    public FormBuilder asListing() {
        return withModifiers(LISTING);
    }

    @Override public Form build()
        throws BuilderException
    {
        if (repository == null) throw new IllegalStateException(FORGOT_WITH_REPOSITORY);

        final ImmutableList<Widget> children = buildChildren(this, this);

        final QName key = createQName(domain, id);

        final Form form = new Form(key,
                sourceName,
                getBindingKey(),
                children,
                getOptions(),
                resolvePrimaryKeyFields(children),
                parameters,
                permissions,
                null,
                modifiers,
                isListing(),
                multipleDimension,
                fieldDimension,
                subformDimension,
                widgetDefDimension,
                globalOptionsDimension,
                configureDimension,
                generated,
                isDeprecable(),
                isSearchable());

        checkRouting(form, repository);

        // Consolidate expressions
        final UiModelReferenceMapper m = new UiModelReferenceMapper(form, repository);

        int i = 0;
        for (final WidgetBuilder builder : Colls.deepSeq(this))
            builder.ordinal(i++).consolidateExpressions(m, form);

        return form;
    }  // end method build

    /** Sets this form as cache. */
    public FormBuilder cached(boolean cache)
        throws BuilderException
    {
        withOption(FieldOption.DISABLE_METADATA_CACHE, !cache);
        return this;
    }

    /** Add form handler class. */
    @SuppressWarnings("UnusedReturnValue")
    public FormBuilder handler(@NotNull final String handlingClass)
        throws BuilderException
    {
        return (FormBuilder) with(FieldOption.HANDLER, referenceMetaModel(domain, handlingClass));
    }

    /** Add on_cancel method to the Form. */
    public FormBuilder onCancel(@NotNull final String methodName)
        throws BuilderException
    {
        return (FormBuilder) with(FieldOption.ON_CANCEL, methodName);
    }

    /** Add on_load method to the Form. */
    public FormBuilder onLoad(@NotNull final String methodName)
        throws BuilderException
    {
        return (FormBuilder) with(FieldOption.ON_LOAD, methodName);
    }

    /** Add on_route method to the Form. */
    @SuppressWarnings("UnusedReturnValue")
    public FormBuilder onRoute(@NotNull final String path)
        throws BuilderException
    {
        if (!Routing.isValid(path)) throw new InvalidPathException(path, getId());
        return (FormBuilder) with(FieldOption.ON_ROUTE, Routing.normalize(path));
    }

    /** Add on_schedule method to the Form, using default schedule interval (one minute). */
    public FormBuilder onSchedule(@NotNull final String methodName)
        throws BuilderException
    {
        return onSchedule(methodName, DEFAULT_SCHEDULE_INTERVAL);
    }

    /** Add on_schedule method to the Form, specifying custom interval. */
    @SuppressWarnings("WeakerAccess")
    public FormBuilder onSchedule(@NotNull final String methodName, int interval)
        throws BuilderException
    {
        return (FormBuilder) with(FieldOption.ON_SCHEDULE, methodName).with(FieldOption.ON_SCHEDULE_INTERVAL, interval);
    }

    /** Sets the form permissions. */
    public void permissions(Collection<String> fs) {
        // Add custom ones...
        for (final String permissionName : fs)
            permissions.put(permissionName, valueOf(permissionName));
    }

    /** Sets the form primary key. */
    public void primaryKey(Collection<ModelField> fs) {
        if (!primaryKeyFields.isEmpty())  // noinspection DuplicateStringLiteralInspection
            throw new IllegalStateException("Primary Key already set");
        primaryKeyFields.addAll(fs);
    }

    /** Add project for external forms. */
    public FormBuilder project(@NotNull final String project)
        throws BuilderException
    {
        return (FormBuilder) with(FieldOption.PROJECT, notEmpty(project, ""));
    }

    /** Add a Style to the Form. */
    public FormBuilder styleClass(@NotNull final String style)
        throws BuilderException
    {
        return (FormBuilder) super.styleClass(style);
    }

    /** Sets the public access to the form. */
    public FormBuilder unrestricted()
        throws BuilderException
    {
        with(FieldOption.UNRESTRICTED);
        return this;
    }

    private void addPredefinedPermissions() {
        for (final PredefinedPermission predefined : PredefinedPermission.values())
            permissions.put(predefined.getName(), predefined);
    }

    private Map<String, Widget> buildBindingsMap(List<Widget> children) {
        final Map<String, Widget> map = new TreeMap<>();
        for (final Widget descendant : Colls.deepSeq(children))
            map.put(descendant.getBinding(), descendant);
        return map;
    }

    private List<ModelField> buildPrimaryKeyFromBoundModel(@NotNull List<Widget> children) {
        final List<ModelField>    result   = new ArrayList<>();
        final Map<String, Widget> bindings = buildBindingsMap(children);

        final Option<List<ModelField>> lists = bindingAsDbObject().map(e -> {
                for (final Attribute a : e.getPrimaryKey()) {
                    final Widget widget = bindings.get(a.getFullName());
                    if (widget == null) return new ArrayList<ModelField>();  // If not all primary is bind, default to none.
                    else result.add(widget);
                }
                return result;
            });
        return lists.orElse(result);
    }

    private void checkRouting(@NotNull final Form form, @NotNull final ModelRepository r)
        throws CollidingPathException
    {
        final String route = form.getOnRoutePath();

        if (Routing.isValid(route)) {
            final Routing<Form> routing = Routes.routing(r);
            if (!routing.checkCollisions(route)) throw new CollidingPathException(route, null, form.getFullName());
        }
    }

    /**
     * Resolve form primary key. If key is set, return it. If not, attempt to find widgets binding
     * bound model primary key.
     */
    private List<ModelField> resolvePrimaryKeyFields(@NotNull List<Widget> children) {
        final List<ModelField> result;
        if (primaryKeyFields.isEmpty() && binding.isPresent() && !isListing()) result = buildPrimaryKeyFromBoundModel(children);
        else result = primaryKeyFields;
        return result;
    }  // end method resolvePrimaryKeyFields

    private boolean isDeprecable() {
        return bindingAsDbObject().map(DbObject::isDeprecable).orElse(false);
    }

    private boolean isSearchable() {
        return bindingAsDbObject().map(DbObject::isSearchable).orElse(false);
    }
}  // end class FormBuilder
