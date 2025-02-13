
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.Check;
import tekgenesis.check.CheckMsg;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.i18n.I18nBundle;
import tekgenesis.common.logging.Logger;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.metadata.exception.InvalidKeyToBeLocalized;

import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.field.FieldOption.LABEL;
import static tekgenesis.metadata.exception.InvalidKeyToBeLocalized.InvalidKeyLocalizeType.*;

/**
 * UiModelLocalizer.
 */
public abstract class UiModelLocalizer<T extends UiModel> {

    //~ Instance Fields ..............................................................................................................................

    protected final UiModelBundle bundle;
    protected final Locale        locale;
    protected final T             model;

    //~ Constructors .................................................................................................................................

    private UiModelLocalizer(UiModelBundle bundle, Locale locale, T model) {
        this.bundle = bundle;
        this.locale = locale;
        this.model  = model;
    }

    //~ Methods ......................................................................................................................................

    /** Localize the Form. */
    @NotNull public abstract T localize();

    /** Localize specific property key. */
    @Nullable public String localizeString(String key) {
        if (hasBundle()) return localizeKey(key);
        return defaultStringFromKey(key);
    }

    /** True if bundle exists for specified locale. */
    boolean hasBundle() {
        return bundle.existsFor(locale);
    }

    ImmutableList<Widget> localizeChildren(Iterable<Widget> widgets) {
        final ImmutableList.Builder<Widget> builder = builder();
        for (final Widget w : widgets) {
            final Tuple<FieldOptions, Boolean> localize = localize(w.getOptions(), w.getName());
            if (w.hasGeneratedName() && localize.second())
                logger.warning("Using localization with an auto generated widget id: " + model.getFullName() + ":" + w.getName());

            builder.add((w.copy(localize.first(), localizeChildren(w))));
        }

        return builder.build();
    }

    FieldOptions localizeUiModelOptions() {
        final FieldOptions options = model.getOptions();
        final FieldOptions result  = localize(options, model.getName()).first();
        if (!options.containsKey(LABEL)) localizeOption(options, model.getName(), result, LABEL);
        return result;
    }

    /** It will return the string of the mm from a ".properties" key. */
    private String defaultStringFromKey(String key) {
        final String[] split = key.split("\\.");
        if (split.length > 3) throw new InvalidKeyToBeLocalized(key, TOO_MANY_ARGUMENTS, 3, split.length);

        final Widget widget = model.getWidget(split[0]).getOrFail("That widget doesn't exist");
        if (split.length == 1) return widget.getLabel();

        final FieldOption option = FieldOption.fromId(split[1]);
        if (option == null) throw new InvalidKeyToBeLocalized(key, FIELD_OPTION_DOESNT_EXIST, split[1], model.getName());

        final int n = split.length == 3 ? parseNum(key, split[2]) : 0;

        switch (option.getType()) {
        case CHECK_T:
            final Check.List check = widget.getOptions().getCheck(option);
            if (n < check.size()) return check.get(n).getMsgText();
            throw new InvalidKeyToBeLocalized(key, NOT_THAT_MANY_CHECKS, n, check.size());
        case STRING_T:
            return widget.getOptions().getString(option);
        case STRING_EXPR_T:
        case VALUE_EXPR_T:
            return widget.getOptions().getExpression(option).toString().replaceAll("\"", "");
        default:
            throw new InvalidKeyToBeLocalized(key, FIELD_OPTION_CANNOT_LOCALIZE, option.name());
        }
    }

    private Tuple<FieldOptions, Boolean> localize(FieldOptions options, String id) {
        boolean localized = false;

        final FieldOptions result = new FieldOptions(options);
        for (final FieldOption option : options) {
            if (option.isLocalizable()) localized |= localizeOption(options, id, result, option);
        }
        return Tuple.tuple(result, localized);
    }

    @Nullable private Object localize(FieldOptions options, FieldOption option, String id) {
        switch (option.getType()) {
        case STRING_T:
            return getString(id, option, 0);
        case STRING_EXPR_T:
            return localizeExpression(options, option, id);
        case VALUE_EXPR_T:
            return localizeExpression(options, option, id);
        case CHECK_T:
            return localizeChecks(options.getCheck(option), id);
        default:
            return null;
        }
    }

    private Check.List localizeChecks(Check.List checks, String id) {
        int        n      = 0;
        Check.List result = Check.List.EMPTY;
        for (final Check check : checks) {
            final CheckMsg msg  = check.getMsg();
            final String   text = getString(id, FieldOption.CHECK, n++);
            result = result.addCheck(check.getExpr(), text == null ? msg : msg.withText(text));
        }
        return result;
    }

    @Nullable private Expression localizeExpression(FieldOptions options, FieldOption option, String id) {
        final Expression   e       = options.getExpression(option);
        final List<String> strings = e.extractStrings();

        final List<String> locStrings = new ArrayList<>(strings.size());
        int                n          = 0;
        for (final String s : strings)
            locStrings.add(bundle.getString(option.getI18nKey(id, n++), s, locale));

        return locStrings.equals(strings) ? null : e.replaceStrings(locStrings);
    }

    @Nullable private String localizeKey(String key) {
        return bundle.containsKey(key, locale) ? bundle.getString(key, locale) : null;
    }

    private boolean localizeOption(FieldOptions options, String id, FieldOptions result, FieldOption option) {
        boolean      localized = false;
        final Object loc       = localize(options, option, id);
        if (loc != null) {
            localized = true;
            result.put(option, loc);
        }
        return localized;
    }

    private int parseNum(String key, String s) {
        try {
            return Integer.valueOf(s);
        }
        catch (final NumberFormatException ex) {
            throw new InvalidKeyToBeLocalized(key, NOT_A_NUMBER, s);
        }
    }

    @Nullable private String getString(String id, FieldOption option, int n) {
        return localizeKey(option.getI18nKey(id, n));
    }

    //~ Methods ......................................................................................................................................

    /** Create a UiModelLocalizer for given form with context locale. */
    public static UiModelLocalizer<Form> localizer(Form form) {
        return localizer(form, Context.getContext().getLocale());
    }

    /** Create a UiModelLocalizer for given widget definition with context locale. */
    public static UiModelLocalizer<WidgetDef> localizer(WidgetDef widget) {
        return localizer(widget, Context.getContext().getLocale(), new Simple(widget));
    }

    /** Create a UiModelLocalizer for given form and locale. */
    public static UiModelLocalizer<Form> localizer(Form form, Locale locale) {
        return localizer(form, locale, form.isExtended() ? new Extended(form) : new Simple(form));
    }

    /** Create a UiModelLocalizer for given form, locale, and bundle. */
    public static UiModelLocalizer<Form> localizer(Form form, Locale locale, I18nBundle bundle) {
        return localizer(form, locale, form.isExtended() ? new Extended(form, bundle) : new Simple(bundle));
    }

    /** Return a map with the strings to be localized for a specified form. */
    public static Map<String, String> stringsForUiModel(UiModel model) {
        final Map<String, String> result  = new LinkedHashMap<>();
        final FieldOptions        options = model.getOptions();
        extractStrings(result, options, model.getName());
        if (!options.containsKey(LABEL)) result.put(model.getName(), model.getLabel());
        extractStrings(result, model);
        return result;
    }

    private static void extractStrings(Map<String, String> result, Iterable<Widget> widgets) {
        for (final Widget widget : widgets) {
            final boolean isNotInternal = widget.getWidgetType() != WidgetType.INTERNAL;
            if (isNotInternal) {
                if (!widget.hasGeneratedName()) extractStrings(result, widget.getOptions(), widget.getName());
                extractStrings(result, widget);
            }
        }
    }

    private static void extractStrings(Map<String, String> result, FieldOptions options, String id) {
        for (final FieldOption option : options) {
            if (option.isLocalizable()) {
                int n = 0;
                switch (option.getType()) {
                case STRING_T:
                    final String value = options.getString(option);
                    if (!value.trim().isEmpty()) result.put(option.getI18nKey(id), value);
                    break;
                case STRING_EXPR_T:
                    final Expression e = options.getExpression(option);
                    for (final String s : e.extractStrings())
                        result.put(option.getI18nKey(id, n++), s);
                    break;
                case CHECK_T:
                    for (final Check check : options.getCheck(option))
                        result.put(option.getI18nKey(id, n++), check.getMsgText());
                    break;
                case VALUE_EXPR_T:
                    final Expression ex = options.getExpression(option);
                    if (ex.isConstant() && ex.getType().isString()) {
                        for (final String s : ex.extractStrings())
                            result.put(option.getI18nKey(id, n++), s);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    private static UiModelLocalizer<Form> localizer(Form form, Locale locale, UiModelBundle bundle) {
        return new UiModelLocalizer<Form>(bundle, locale, form) {
            @NotNull @Override public Form localize() {
                return hasBundle() ? new Form(model, localizeChildren(model), localizeUiModelOptions()) : model;
            }
        };
    }

    private static UiModelLocalizer<WidgetDef> localizer(WidgetDef widget, Locale locale, UiModelBundle bundle) {
        return new UiModelLocalizer<WidgetDef>(bundle, locale, widget) {
            @NotNull @Override public WidgetDef localize() {
                return hasBundle() ? new WidgetDef(model, localizeChildren(model), localizeUiModelOptions()) : model;
            }
        };
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(UiModelLocalizer.class);

    //~ Inner Interfaces .............................................................................................................................

    interface UiModelBundle {
        boolean containsKey(final String key, final Locale locale);
        boolean existsFor(Locale locale);
        String getString(final String key, final Locale locale);
        String getString(final String key, final String defaultValue, final Locale locale);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Extended bundle to support multiple bundles for form extensions.
     */
    private static class Extended implements UiModelBundle {
        private final Seq<I18nBundle> bundles;

        private Extended(final Form form) {
            this(form, I18nBundle.getBundle(form.getFullName()));
        }

        private Extended(final Form form, final I18nBundle original) {
            bundles = Colls.listOf(I18nBundle.getBundle(form.getExtenderFqn()), original);
        }

        public boolean containsKey(final String key, final Locale locale) {
            return bundles.exists(b -> b.containsKey(key, locale));
        }

        public boolean existsFor(Locale locale) {
            return bundles.exists(b -> b.existsFor(locale));
        }

        public String getString(final String key, final Locale locale) {
            return bundles.filter(b -> b.containsKey(key, locale)).map(b -> b.getString(key, locale)).getFirst(v -> !v.isEmpty()).orElse("");
        }

        public String getString(final String key, final String defaultValue, final Locale locale) {
            return bundles.filter(b -> b.containsKey(key, locale))
                   .map(b ->
                        b.getString(key, defaultValue, locale))
                   .getFirst(v ->
                        !v.isEmpty())
                   .orElse("");
        }
    }

    /**
     * A simple bundle that just proxy to a I18nBundle.
     */
    private static class Simple implements UiModelBundle {
        private final I18nBundle bundle;

        Simple(final UiModel model) {
            this(I18nBundle.getBundle(model.getFullName()));
        }

        Simple(final I18nBundle bundle) {
            this.bundle = bundle;
        }

        public boolean containsKey(final String key, final Locale locale) {
            return bundle.containsKey(key, locale);
        }

        public boolean existsFor(Locale locale) {
            return bundle.existsFor(locale);
        }

        public String getString(final String key, final Locale locale) {
            return bundle.getString(key, locale);
        }

        public String getString(final String key, final String defaultValue, final Locale locale) {
            return bundle.getString(key, defaultValue, locale);
        }
    }
}  // end class UiModelLocalizer
