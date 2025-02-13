
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.menu;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.i18n.I18nBundle;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;

import static tekgenesis.field.FieldOption.MENU_ELEMENT_REF;
import static tekgenesis.field.FieldOptionType.STRING_T;

/**
 * Menu Localizer.
 */
public class MenuLocalizer {

    //~ Instance Fields ..............................................................................................................................

    private final I18nBundle bundle;
    private final Locale     locale;
    private final Menu       menu;

    //~ Constructors .................................................................................................................................

    /** Create a Menu localizer. */
    public MenuLocalizer(@NotNull final Menu menu, Locale locale) {
        this.menu   = menu;
        this.locale = locale;
        bundle      = I18nBundle.getBundle(menu.getFullName());
    }

    //~ Methods ......................................................................................................................................

    /** Localize the Form. */
    public Menu localize() {
        String menuLabel = menu.getLabel();

        if (bundle.existsFor(locale)) menuLabel = bundle.getString(menu.getName(), locale);
        return new Menu(menu.getSourceName(), menu.getDomain(), menu.getName(), localizeItems(menu.getChildren()), menuLabel);
    }

    private Tuple<FieldOptions, Boolean> localize(@NotNull FieldOptions options, @NotNull String id) {
        boolean localized = false;

        final FieldOptions result = new FieldOptions(options);
        for (final FieldOption option : options) {
            if (option.isLocalizable()) {
                String loc = null;
                if (option.getType() == STRING_T) loc = localizeString(options, id, option);

                if (loc != null) {
                    localized = true;
                    result.put(option, loc);
                }
            }
        }
        return Tuple.tuple(result, localized);
    }

    private List<MenuItem> localizeItems(@NotNull final Seq<MenuItem> items) {
        final List<MenuItem> res = new ArrayList<>();

        for (final MenuItem menuItem : items) {
            final FieldOptions fieldOptions = localize(menuItem.getOptions(), menuItem.getName()).first();
            final MenuItem     locMenuItem  = new MenuItem(menuItem.getMenu(), fieldOptions);
            res.add(locMenuItem);
        }

        return res;
    }

    private String localizeString(@NotNull FieldOptions options, @NotNull String id, @NotNull FieldOption option) {
        String loc = getString(id, option, 0);

        if (Predefined.isEmpty(loc)) {
            final String optionId = options.getMetaModelReference(MENU_ELEMENT_REF).getFullName();

            if (Predefined.isNotEmpty(optionId)) {
                final I18nBundle i18nBundle = I18nBundle.getBundle(optionId);
                if (i18nBundle.existsFor(locale)) {
                    final String fqn    = QName.createQName(optionId).getName();
                    final String newLoc = i18nBundle.getString(fqn, locale);
                    loc = Predefined.isEmpty(newLoc) ? loc : newLoc;
                }
            }
        }
        return loc;
    }

    @Nullable private String getString(@NotNull String id, @NotNull FieldOption option, int n) {
        final String key = option.getI18nKey(id, n);
        return bundle.existsFor(locale) && bundle.containsKey(key, locale) ? bundle.getString(key, locale) : null;
    }

    //~ Methods ......................................................................................................................................

    /** Return a map with the strings to be localized for a specified menu. */
    public static Map<String, String> stringsForMenu(@NotNull Menu menu) {
        final Map<String, String> result = new LinkedHashMap<>();
        result.put(menu.getName(), menu.getLabel());
        for (final MenuItem item : menu.getChildren())
            result.put(item.getName(), item.getLabel());
        return result;
    }
}  // end class MenuLocalizer
