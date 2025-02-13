
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.widget.MultipleWidget;

/**
 * Listener interface to handle changes on models (form, multiple, widget).
 */
public interface ChangeListener {

    //~ Methods ......................................................................................................................................

    /** Called when the model value for the specified widget changes. */
    void onModelChange(@NotNull final IndexedWidget widget);

    /** Called when the model for the specified table widget changes it's cardinality. */
    void onMultipleModelChange(@NotNull final MultipleWidget multipleWidget, @NotNull final MultipleChanges changes);

    /** Called when the widget's definition changes (messages and options, when apply). */
    void onWidgetDefinitionChange(@NotNull final IndexedWidget widget);

    //~ Inner Interfaces .............................................................................................................................

    interface Registration {
        /** Registration to remove listener from attached source. */
        void removeChangeListener();
    }
}
