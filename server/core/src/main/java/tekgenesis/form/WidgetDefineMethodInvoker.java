
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import tekgenesis.metadata.form.widget.WidgetDef;

/**
 * Exposes ability to invoke 'define' methods for {@link WidgetDef widget definitions}.
 */
public interface WidgetDefineMethodInvoker {

    //~ Methods ......................................................................................................................................

    /** Invoke 'define' method for specified widget definition field. */
    <W extends WidgetInstance<?>> W invokeDefine(String name);
}
