
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.WidgetDef;

/**
 * Also binds and computes new {@link WidgetDef widget definitions}.
 */
public interface UiModelContext extends UiModelRetriever {

    //~ Methods ......................................................................................................................................

    void compute(@NotNull final WidgetDefModel wdm);
}
