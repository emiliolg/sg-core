
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.NotNull;

/**
 * Ui focus listener.
 */
public interface UiFocusListener {

    //~ Methods ......................................................................................................................................

    /** Notify when widgetUI has been granted with focus. */
    void onFocus(@NotNull WidgetUI widgetUI);
}
