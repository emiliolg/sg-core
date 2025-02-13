
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
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Times;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.dependency.PrecedenceData;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;
import tekgenesis.type.permission.Permission;

import static java.util.Collections.emptyMap;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.type.Modifier.EXTERNAL;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * The class that contains the metadata for the form.
 */
@SuppressWarnings({ "FieldMayBeFinal", "ClassWithTooManyMethods", "ClassWithTooManyFields" })  // GWT !
public class Form extends UiModel {

    //~ Instance Fields ..............................................................................................................................

    private boolean deprecableBoundModel;
    private boolean isSearchable;

    private Map<String, Widget>              keyShortcutsMap = null;
    private boolean                          listing;
    @NotNull private Map<String, Permission> permissions;

    @NotNull private transient List<ModelField> primaryKey;

    private Integer[] widgetsArrayMapping;

    //~ Constructors .................................................................................................................................

    Form() {
        widgetsArrayMapping = EMPTY_INT_ARRAY;
        binding             = QName.EMPTY;
        primaryKey          = new ArrayList<>();
        permissions         = emptyMap();
        listing             = false;
        isSearchable        = false;
    }

    /** Copy constructor. */
    Form(Form f, ImmutableList<Widget> children, FieldOptions options) {
        this(f.modelKey,
            f.sourceName,
            f.binding,
            children,
            options,
            f.primaryKey,
            f.parameters,
            f.permissions,
            f.precedence,
            f.modifiers,
            f.listing,
            f.multipleDimension,
            f.fieldDimension,
            f.subformDimension,
            f.widgetDefDimension,
            f.optionsDimension,
            f.configureDimension,
            f.generated,
            f.deprecableBoundModel,
            f.isSearchable);
    }

    /** Creates a Form. */
    @SuppressWarnings("ConstructorWithTooManyParameters")  // Called from builder
    Form(QName modelKey, String sourceName, @NotNull QName binding, @NotNull ImmutableList<Widget> children, FieldOptions options,
         @NotNull List<ModelField> primaryKey, @NotNull List<ModelField> parameters, @NotNull Map<String, Permission> permissions,
         @Nullable PrecedenceData precedence, @NotNull EnumSet<Modifier> modifiers, boolean listing, int multipleDimension, int fieldDimension,
         int subformDimension, int widgetDefDimension, int optionsDimension, int configureDimension, boolean generated, boolean deprecableBoundModel,
         boolean isSearchable)                             //
    {
        super(modelKey,
            sourceName,
            binding,
            children,
            options,
            parameters,
            precedence,
            multipleDimension,
            fieldDimension,
            subformDimension,
            widgetDefDimension,
            optionsDimension,
            configureDimension,
            generated,
            modifiers);

        this.primaryKey           = primaryKey;
        this.permissions          = permissions;
        this.listing              = listing;
        this.deprecableBoundModel = deprecableBoundModel;
        this.isSearchable         = isSearchable;

        widgetsArrayMapping = createWidgetArrayMapping();
        keyShortcutsMap     = createKeyShortcutsMap();
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasPermission(String permission) {
        return permissions.containsKey(permission);
    }

    /** Resolves all references to binding fields. */
    public void solve(Function<ModelField, ModelField> solver) {
        super.solve(solver);
        primaryKey = map(primaryKey, solver).toList();
    }

    /** Returns true if form is dynamically generated. */
    public boolean isDynamic() {
        return options.getBoolean(FieldOption.DYNAMIC_FORM);
    }

    /**
     * Returns true if the form can be cached once loaded. Both in the server Repository and on the
     * clients browser.
     */
    @Override public boolean isCached() {
        return !options.getBoolean(FieldOption.DISABLE_METADATA_CACHE);
    }

    /** Returns true if form is dynamically extended by the user. */
    public boolean isExtended() {
        return !getExtenderFqn().isEmpty();
    }

    /** Returns true if the form has public access. */
    public boolean isUnrestricted() {
        return options.getBoolean(FieldOption.UNRESTRICTED);
    }

    /** Return true if form has remote modifier. */
    public boolean isRemote() {
        return modifiers.contains(REMOTE);
    }

    /** Return InstanceSearcher class. */
    public boolean isSearchable() {
        return isSearchable;
    }

    /** Return true if form has listing modifier. */
    public boolean isListing() {
        return listing;
    }

    /** Get form handling class. */
    @NotNull public String getHandlerClass() {
        return options.getMetaModelReference(HANDLER).getFullName();
    }

    /** Returns if the entity referenced by this form is a deprecable bound model. */
    public boolean isDeprecableBoundModel() {
        return deprecableBoundModel;
    }

    /** Return true if form has external modifier. */
    public boolean isExternal() {
        return modifiers.contains(EXTERNAL);
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.FORM;
    }

    /** Get the Form on_cancel method name. */
    @NotNull public String getOnCancelMethodName() {
        return options.getString(ON_CANCEL);
    }

    /** Get the Form on_display method name. */
    @NotNull public String getOnDisplayMethodName() {
        return options.getString(ON_DISPLAY);
    }

    /** Get the Form on_load method name. */
    @NotNull public String getOnLoadMethodName() {
        return options.getString(ON_LOAD);
    }

    /** Get the Form on_route path. */
    @NotNull public String getOnRoutePath() {
        return options.getString(ON_ROUTE);
    }

    /** Get the Form on_schedule interval. */
    public int getOnScheduleInterval() {
        return options.getInt(ON_SCHEDULE_INTERVAL);
    }

    /** Get the Form on_schedule method name. */
    @NotNull public String getOnScheduleMethodName() {
        return options.getString(ON_SCHEDULE);
    }

    /** Return the ordinal for the given widget. */
    @Nullable public Integer getOrdinal(@NotNull final Widget widget) {
        for (int i = 0; i < widgetsArray.length; i++)
            if (widget == getWidgetByOrdinal(i)) return i;
        return null;
    }

    /** Returns the form's permissions. */
    @NotNull public Seq<Permission> getPermissions() {
        return immutable(permissions.values());
    }

    /** Get from Primary key. */
    @NotNull public ImmutableList<ModelField> getPrimaryKey() {
        return immutable(primaryKey);
    }

    /** Returns the Ids of the Primary Key. */
    @NotNull public Seq<String> getPrimaryKeyAsStrings() {
        return getPrimaryKey().map(ModelField::getName);
    }

    /** Get external form project name. */
    @NotNull public String getProject() {
        return options.getString(PROJECT);
    }

    /** Get the Expression to calculate an Style class. */
    @NotNull public Expression getStyleClassExpression() {
        return options.getExpression(STYLE);
    }

    /** Return the element with given name. */
    @NotNull public Option<Widget> getWidgetByKeyShortcut(@NotNull String shortcut) {
        return option(keyShortcutsMap.get(shortcut));
    }

    /** Return the redirection from the original ordinal to the new ordinal in the extended form. */
    int mapOrdinal(final int ordinal) {
        return widgetsArrayMapping.length > 0 ? widgetsArrayMapping[ordinal] : ordinal;
    }

    /** Return the form elements dimension. Test purpose only. */
    int getElementsDimension() {
        return widgetsMap.size();
    }

    /** Returns the fqn of the responsible class for extending this form. */
    String getExtenderFqn() {
        return options.getString(FieldOption.EXTENDED_FORM);
    }

    private Map<String, Widget> createKeyShortcutsMap() {
        final Map<String, Widget> result = new LinkedHashMap<>();
        for (final Widget widget : widgetsArray) {
            final String shortcut = widget.getShortcut();
            if (!shortcut.isEmpty()) {
                @SuppressWarnings("NonJREEmulationClassesInClientCode")
                final String cleanShortcut = shortcut.replaceAll("\\s+", "").replace("cmd", "ctrl").replace("meta", "ctrl");
                result.put(cleanShortcut, widget);
            }
        }
        return result;
    }

    /** Only for extended forms. This array maps old ordinal slots to current slots */
    private Integer[] createWidgetArrayMapping() {
        if (!isExtended()) return EMPTY_INT_ARRAY;

        final List<Integer> result = new ArrayList<>();
        for (int i = 0; i < widgetsArray.length; i++) {
            final Widget widget = getWidgetByOrdinal(i);
            if (widget.isOriginalField()) result.add(i);
        }
        return result.toArray(new Integer[result.size()]);
    }  // end method createWidgetArrayMapping

    //~ Static Fields ................................................................................................................................

    private static final Integer[] EMPTY_INT_ARRAY = new Integer[0];

    public static final int DEFAULT_SCHEDULE_INTERVAL = Times.MINUTES_HOUR;  // Every one minute.

    private static final long serialVersionUID = -3624910200330091984L;
}  // end class Form
