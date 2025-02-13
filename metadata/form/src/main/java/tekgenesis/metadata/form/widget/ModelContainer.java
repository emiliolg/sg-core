
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.type.Scope;

/**
 * Interface implemented by form, widget definition, and multiples describing the number of fields,
 * widget definitions, and subforms contained by the Widget.
 */
public interface ModelContainer extends Scope {

    //~ Methods ......................................................................................................................................

    /** The number of configurable slots. */
    int getConfigureDimension();

    /** The number of fields (scalar or columns). */
    int getFieldDimension();

    /** The number of options slots. */
    int getOptionsDimension();

    /** Return subform matching specified slot. */
    @NotNull Widget getSubformBySlot(int slot);

    /** Subform slots count. */
    int getSubformDimension();

    /** Return the widget for given configuration slot. */
    @NotNull Widget getWidgetByConfigurationSlot(int slot);

    /** Return the widget for given field slot. */
    @NotNull Widget getWidgetByFieldSlot(int slot);

    /** Return the widget for given option slot. */
    @NotNull Widget getWidgetByOptionSlot(int slot);

    /** Return widget definition matching specified slot. */
    @NotNull Widget getWidgetDefBySlot(int slot);

    /** Widget defintion slots count. */
    int getWidgetDefDimension();
}
