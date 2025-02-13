
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.documentationProvider;

import java.util.Locale;

import tekgenesis.common.env.i18n.I18nBundle;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;

/**
 * Manager with utils for Documentation.
 */
public class DocumentationManager {

    //~ Constructors .................................................................................................................................

    private DocumentationManager() {}

    //~ Methods ......................................................................................................................................

    /** find doc for given key and Locale. */
    public static String findDoc(String key) {
        return findDoc(key, Locale.getDefault());
    }

    /** find doc for given key and Locale. */
    private static String findDoc(String key, Locale locale) {
        final FieldOption fieldOption = FieldOption.fromId(key);
        if (fieldOption != null) return findDocForWidgetOption(fieldOption, locale);
        final WidgetType widgetType = WidgetTypes.fromId(key);
        return widgetType != null ? findDocForWidget(widgetType, locale) : MMDocumentationProvider.NO_DOCUMENTATION_FOUND;
    }

    private static String findDocForWidget(WidgetType type, Locale locale) {
        return WIDGET_DOC.getString(type.name(), locale);
    }

    private static String findDocForWidgetOption(FieldOption option, Locale locale) {
        return FIELD_DOC.getString(option.name(), locale);
    }

    //~ Static Fields ................................................................................................................................

    private static final I18nBundle WIDGET_DOC = I18nBundle.getBundle("documentation.WidgetTypeDoc")
                                                 .setClassLoader(DocumentationManager.class.getClassLoader());
    private static final I18nBundle FIELD_DOC  = I18nBundle.getBundle("documentation.FieldOptionsDoc")
                                                 .setClassLoader(DocumentationManager.class.getClassLoader());
}
