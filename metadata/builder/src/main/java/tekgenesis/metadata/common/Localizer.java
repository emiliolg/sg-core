
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.link.Link;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;
import tekgenesis.type.MetaModel;

import static java.util.Collections.emptyMap;

import static tekgenesis.metadata.form.widget.UiModelLocalizer.stringsForUiModel;
import static tekgenesis.metadata.link.LinkLocalizer.stringsForLink;
import static tekgenesis.metadata.menu.MenuLocalizer.stringsForMenu;

/**
 * Utilities to deal with Localization.
 */
public class Localizer {

    //~ Constructors .................................................................................................................................

    private Localizer() {}

    //~ Methods ......................................................................................................................................

    /**
     * Retrieve a Map with the Strings to be localized for a particular MetaModel If no localization
     * is needed an empty map will be returned.
     */
    @NotNull public static Map<String, String> stringsToLocalize(@NotNull MetaModel model) {
        switch (model.getMetaModelKind()) {
        case ENUM:
            return stringsForEnum((EnumType) model);
        case FORM:
        case WIDGET:
            return stringsForUiModel((UiModel) model);
        case MENU:
            return stringsForMenu((Menu) model);
        case LINK:
            return stringsForLink((Link) model);
        case ENTITY:
        case ROLE:
        case TYPE:
        case CASE:
        case VIEW:
        case TASK:
        case UNDEFINED:
        case HANDLER:
        case SEARCHABLE:
            break;
        }
        return emptyMap();
    }

    private static Map<String, String> stringsForEnum(EnumType enumType) {
        final Map<String, String> result = new LinkedHashMap<>();
        for (final EnumValue enumValue : enumType.getValues())
            result.put(enumValue.getName(), enumValue.getLabel());
        return result;
    }
}  // end class Localizer
