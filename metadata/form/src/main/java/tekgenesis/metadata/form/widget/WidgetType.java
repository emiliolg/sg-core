
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

/**
 * Defines a Widget Type.
 */
public enum WidgetType {

    //~ Enum constants ...............................................................................................................................

    ANCHOR, BUTTON, TABS, MESSAGE, TEXT_AREA, RICH_TEXT_AREA, TEXT_FIELD, DISPLAY, LIST_BOX, TABLE, CHART, SECTION, PASSWORD_FIELD, MAIL_FIELD, TAGS,
    CHECK_BOX, COMBO_BOX, DATE_BOX, DATE_TIME_BOX, DATE_PICKER, TIME_PICKER, FORM, VERTICAL, HORIZONTAL, IMAGE, LABEL, RADIO_GROUP, SEARCH_BOX,
    SUGGEST_BOX, SUBFORM, TOGGLE_BUTTON, INTERNAL, GALLERY, UPLOAD, SHOWCASE, BREADCRUMB, TAGS_COMBO_BOX, TAGS_SUGGEST_BOX, CHECK_BOX_GROUP,
    TREE_VIEW, MAP, PROGRESS, HEADER, FOOTER, DIALOG, POPOVER, DYNAMIC, VIDEO, DROPDOWN, RANGE, RANGE_VALUE, NONE, DOUBLE_DATE_BOX, COMBO_DATE_BOX,
    COLOR_PICKER, PICK_LIST, INPUT_GROUP, RATING, WIDGET, IFRAME;

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return getId();
    }

    /** Returns true if the widget holds multiple data (e.g.: tables, charts). */
    public boolean isMultiple() {
        return WidgetTypes.isMultiple(this);
    }

    /** External Id of the widget (The one used in the parser. */
    public String getId() {
        // return Strings.toCamelCase(name())
        return name().toLowerCase();
    }

    /** Returns true if the widget is a group widget. */
    public boolean isGroup() {
        return WidgetTypes.isGroup(this);
    }

    // private static  ResourceBundle WIDGET_DOC = ResourceBundle.getBundle("tekgenesis.metadata.form.widget.WidgetType");
}
