
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import tekgenesis.field.FieldOption;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.type.Types;

import static tekgenesis.field.FieldOption.CHART_TYPE;
import static tekgenesis.metadata.form.widget.WidgetType.*;

/**
 * Common static methods providing access to Form and Widget construction.
 */
@SuppressWarnings({ "WeakerAccess", "ClassWithTooManyMethods" })
public class FormBuilderPredefined {

    //~ Constructors .................................................................................................................................

    private FormBuilderPredefined() {}

    //~ Methods ......................................................................................................................................

    /** Anchor creation. */
    public static WidgetBuilder anchor(final String label) {
        return new WidgetBuilder(ANCHOR).label(label);
    }

    /** TextAreaBuilder creation. */
    public static WidgetBuilder area(final String label) {
        return new WidgetBuilder(TEXT_AREA).label(label);
    }

    /** BreadCrumb creation. */
    public static WidgetBuilder breadCrumb(final String label) {
        return new WidgetBuilder(BREADCRUMB).label(label);
    }

    /** ButtonBuilder creation. */
    public static WidgetBuilder button(final String label) {
        return new WidgetBuilder(BUTTON).label(label);
    }

    /** ButtonBuilder creation. */
    public static WidgetBuilder button(final ButtonType type)
        throws BuilderException
    {
        return new WidgetBuilder(BUTTON).buttonType(type);
    }

    /** Chart creation. */
    public static WidgetBuilder chart(final String label, final ChartType type)
        throws BuilderException
    {
        return new WidgetBuilder(CHART).with(CHART_TYPE, type).label(label);
    }

    /** CheckBoxBuilder creation. */
    public static WidgetBuilder check(final String label) {
        return new WidgetBuilder(CHECK_BOX).label(label);
    }

    /** ColorPickerBuilder creation. */
    public static WidgetBuilder color_picker(final String label) {
        return new WidgetBuilder(COLOR_PICKER).label(label);
    }

    /** ComboBoxBuilder creation. */
    public static WidgetBuilder combo(final String label) {
        return new WidgetBuilder(COMBO_BOX).label(label);
    }

    /** ComboDateBox Builder creation. */
    public static WidgetBuilder comboDateBox(final String label) {
        return new WidgetBuilder(COMBO_DATE_BOX).label(label);
    }

    /** DateBoxBuilder creation. */
    public static WidgetBuilder dateBox(final String label) {
        return new WidgetBuilder(DATE_BOX).label(label);
    }

    /** DatePickerBuilder creation. */
    public static WidgetBuilder datePicker(final String label) {
        return new WidgetBuilder(DATE_PICKER).label(label);
    }

    /** DateBoxBuilder creation. */
    public static WidgetBuilder datetimeBox(final String label) {
        return new WidgetBuilder(DATE_TIME_BOX).label(label);
    }

    /** Dialog GroupBuilder creation. */
    public static WidgetBuilder dialogGroup(final String label) {
        return new WidgetBuilder(DIALOG).label(label);
    }

    /** Display Builder creation. */
    public static WidgetBuilder display(final String label) {
        return new WidgetBuilder(DISPLAY).label(label);
    }

    /** DoubleDateBoxBuilder creation. */
    public static WidgetBuilder doubleDateBox(final String label) {
        return new WidgetBuilder(DOUBLE_DATE_BOX).label(label);
    }

    /** Dropdown creation. */
    public static WidgetBuilder dropdown(final String label) {
        return new WidgetBuilder(DROPDOWN).label(label);
    }

    /** Email Field creation. */
    public static WidgetBuilder email(final String label) {
        return new WidgetBuilder(MAIL_FIELD).label(label);
    }

    /** TextFieldBuilder creation. */
    public static WidgetBuilder field(final String label) {
        return new WidgetBuilder(TEXT_FIELD).label(label);
    }

    /** Footer widget creation. */
    public static WidgetBuilder footer() {
        return new WidgetBuilder(FOOTER);
    }

    /** FormBuilder creation. */
    public static FormBuilder form(String sourceName, String packageId, String id) {
        return new FormBuilder(sourceName, packageId, id);
    }

    /** Image Gallery creation. */
    public static WidgetBuilder gallery(final String label) {
        return new WidgetBuilder(GALLERY).label(label);
    }

    /** Header widget creation. */
    public static WidgetBuilder header() {
        return new WidgetBuilder(HEADER);
    }

    /** Horizontal GroupBuilder creation. */
    public static WidgetBuilder horizontalGroup(final String label) {
        return new WidgetBuilder(HORIZONTAL).label(label);
    }

    /** IFrame creation. */
    public static WidgetBuilder iFrame(final String label) {
        return new WidgetBuilder(WidgetType.IFRAME).label(label);
    }

    /** TextFieldBuilder creation for integer fields. */
    public static WidgetBuilder integer(final String label)
        throws BuilderException
    {
        return field(label).withType(Types.intType());
    }

    /** Internal Field creation. */
    public static WidgetBuilder internal(final String id) {
        return new WidgetBuilder(INTERNAL).id(id);
    }
    /** TextLabelBuilder creation. */
    public static WidgetBuilder label(final String label) {
        return new WidgetBuilder(WidgetType.LABEL).label(label);
    }

    /** ListBoxBuilder creation. */
    public static WidgetBuilder list(final String label) {
        return new WidgetBuilder(LIST_BOX).label(label);
    }

    /** Map creation. */
    public static WidgetBuilder map(final String label) {
        return new WidgetBuilder(MAP).label(label);
    }

    /** MessageBuilder creation. */
    public static WidgetBuilder message(final String label) {
        return new WidgetBuilder(MESSAGE).label(label);
    }

    /** TextFieldBuilder creation for numeric fields. */
    public static WidgetBuilder numeric(final String label, final int precision, final int decimals)
        throws BuilderException
    {
        return field(label).withType(Types.decimalType(precision, decimals));
    }

    /** TextFieldBuilder creation for numeric integer fields. */
    public static WidgetBuilder numericInt(final String label)
        throws BuilderException
    {
        return field(label).withType(Types.intType());
    }

    /** TextFieldBuilder creation for numeric integer fields. */
    public static WidgetBuilder numericInt(final String label, final int length)
        throws BuilderException
    {
        return field(label).withType(Types.intType(length));
    }

    /** Password Field creation. */
    public static WidgetBuilder password(final String label) {
        return new WidgetBuilder(PASSWORD_FIELD).label(label);
    }

    /** PickListBuilder creation. */
    public static WidgetBuilder pickList(final String label) {
        return new WidgetBuilder(PICK_LIST).label(label);
    }

    /** Dialog GroupBuilder creation. */
    public static WidgetBuilder popover(final String label) {
        return new WidgetBuilder(POPOVER).label(label);
    }

    /** ProgressBar creation. */
    public static WidgetBuilder progress(final String label) {
        return new WidgetBuilder(PROGRESS).label(label);
    }

    /** Radio Group creation. */
    public static WidgetBuilder radioGroup(final String label) {
        return new WidgetBuilder(RADIO_GROUP).label(label);
    }

    /** Rating creation. */
    public static WidgetBuilder rating(final String label) {
        return new WidgetBuilder(RATING).label(label);
    }

    /** RichTextAreaBuilder creation. */
    public static WidgetBuilder richText(final String label) {
        return new WidgetBuilder(RICH_TEXT_AREA).label(label);
    }

    /** Horizontal ScrollSectionBuilder creation. */
    public static WidgetBuilder scrollSection(final String label)
        throws BuilderException
    {
        return new WidgetBuilder(SECTION).label(label).with(FieldOption.SCROLLABLE);
    }

    /** Section creation. */
    public static WidgetBuilder section(final String label) {
        return new WidgetBuilder(SECTION).label(label);
    }

    /** SubForm Field creation. */
    public static WidgetBuilder subform(final String label, final String formId)
        throws BuilderException
    {
        return new WidgetBuilder(SUBFORM).label(label).subForm(formId);
    }

    /** SuggestBoxBuilder creation. */
    public static WidgetBuilder suggest(final String label) {
        return new WidgetBuilder(SUGGEST_BOX).label(label);
    }

    /** Tabbed GroupBuilder creation. */
    public static WidgetBuilder tabbedGroup(final String label) {
        return new WidgetBuilder(TABS).label(label);
    }

    /** TableBuilder creation. */
    public static WidgetBuilder table(final String label) {
        return new WidgetBuilder(TABLE).label(label);
    }

    /** Password Field creation. */
    public static WidgetBuilder tags(final String label) {
        return new WidgetBuilder(TAGS).label(label);
    }

    /** ToggleButtonBuilder creation. */
    public static WidgetBuilder toggle(final String label) {
        return new WidgetBuilder(TOGGLE_BUTTON).label(label);
    }

    /** Tree creation. */
    public static WidgetBuilder treeView(final String label) {
        return new WidgetBuilder(TREE_VIEW).label(label);
    }

    /** Vertical GroupBuilder creation. */
    public static WidgetBuilder verticalGroup(final String label) {
        return new WidgetBuilder(VERTICAL).label(label);
    }

    /** WidgetDef field creation. */
    public static WidgetBuilder widget(final String label, final WidgetDef def)
        throws BuilderException
    {
        return new WidgetBuilder(WIDGET).widgetDef(def.getFullName());
    }

    /** WidgetDef field creation. */
    public static WidgetBuilder widget(final String label, final String widgetFqn)
        throws BuilderException
    {
        return new WidgetBuilder(WIDGET).widgetDef(widgetFqn);
    }

    /** WidgetDefBuilder creation. */
    public static WidgetDefBuilder widget(String sourceName, String packageId, String id) {
        return new WidgetDefBuilder(sourceName, packageId, id);
    }
}  // end class FormBuilderPredefined
