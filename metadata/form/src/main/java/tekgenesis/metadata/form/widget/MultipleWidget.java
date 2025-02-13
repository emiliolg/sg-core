
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.Arrays;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.field.FieldOptions;
import tekgenesis.type.Type;

import static tekgenesis.metadata.form.widget.Form.*;

/**
 * Metadata for a Multiple Widget.
 */
@SuppressWarnings("FieldMayBeFinal")
public class MultipleWidget extends Widget implements ModelContainer {

    //~ Instance Fields ..............................................................................................................................

    private int configureDimension;

    private int      fieldDimension;
    private int      optionsDimension;
    private int      subformDimension;
    private Widget[] subformSlotArray;
    private int      widgetDefDimension;
    private Widget[] widgetDefSlotArray;
    private Widget[] widgetsConfigurationArray;
    private Widget[] widgetsFieldArray;
    private Widget[] widgetsOptionArray;

    //~ Constructors .................................................................................................................................

    /** Gwt. */
    MultipleWidget() {
        super(WidgetType.TABLE);
        widgetsFieldArray         = EMPTY_WIDGET_ARRAY;
        widgetsOptionArray        = EMPTY_WIDGET_ARRAY;
        subformSlotArray          = EMPTY_WIDGET_ARRAY;
        widgetDefSlotArray        = EMPTY_WIDGET_ARRAY;
        widgetsConfigurationArray = EMPTY_WIDGET_ARRAY;
    }

    /** Constructs a multiple Widget. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    MultipleWidget(WidgetType widgetType, Type type, FieldOptions options, ImmutableList<Widget> children, int fieldDimension, int subformDimension,
                   int widgetDefDimension, int optionsDimension, int configureDimension) {
        super(widgetType, type, options, children, false);
        setElemType(ElemType.MULTIPLE);

        setChildrenContext(this, this);
        this.fieldDimension       = fieldDimension;
        this.subformDimension     = subformDimension;
        this.widgetDefDimension   = widgetDefDimension;
        this.optionsDimension     = optionsDimension;
        this.configureDimension   = configureDimension;
        widgetsFieldArray         = createFieldsArray(this, fieldDimension);
        widgetsOptionArray        = createOptionsArray(this, optionsDimension);
        subformSlotArray          = createSubFormsArray(this, subformDimension);
        widgetDefSlotArray        = createWidgetDefsArray(this, widgetDefDimension);
        widgetsConfigurationArray = createConfigurationsArray(this, configureDimension);
    }

    //~ Methods ......................................................................................................................................

    @Override public int getConfigureDimension() {
        return configureDimension;
    }

    @Override public int getFieldDimension() {
        return fieldDimension;
    }

    @Override public int getOptionsDimension() {
        return optionsDimension;
    }

    /** Return subform matching specified slot. */
    @NotNull public Widget getSubformBySlot(int slot) {
        return subformSlotArray[slot];
    }

    public int getSubformDimension() {
        return subformDimension;
    }

    /** Return all Table elements (My Children and the children of my children). */
    @NotNull public Collection<Widget> getTableElements() {
        return Arrays.asList(widgetsFieldArray);
    }

    /** Return the widget for given configuration slot. */
    @NotNull public Widget getWidgetByConfigurationSlot(int slot) {
        return widgetsConfigurationArray[slot];
    }

    /** Return the widget for given field slot. */
    @NotNull public Widget getWidgetByFieldSlot(int slot) {
        return widgetsFieldArray[slot];
    }

    /** Return the widget for given option slot. */
    @NotNull @Override public Widget getWidgetByOptionSlot(int slot) {
        return widgetsOptionArray[slot];
    }

    /** Return widget definition matching specified slot. */
    @NotNull @Override public Widget getWidgetDefBySlot(int slot) {
        return widgetDefSlotArray[slot];
    }

    @Override public int getWidgetDefDimension() {
        return widgetDefDimension;
    }

    @Override Widget copy(FieldOptions opts, ImmutableList<Widget> widgets) {
        return new MultipleWidget(getWidgetType(),
            getType(),
            opts,
            widgets,
            fieldDimension,
            subformDimension,
            widgetDefDimension,
            optionsDimension,
            configureDimension);
    }

    //~ Methods ......................................................................................................................................

    private static void setChildrenContext(MultipleWidget multiple, Widget widget) {
        for (final Widget child : widget) {
            child.setMultiple(multiple);
            child.setElemType(ElemType.COLUMN);
            if (child.hasChildren()) setChildrenContext(multiple, child);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Widget[] EMPTY_WIDGET_ARRAY = new Widget[0];
    private static final long     serialVersionUID   = -4104893605978781986L;
}  // end class MultipleWidget
