
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import tekgenesis.check.CheckMsg;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

/**
 * Validation Listener.
 */
public interface ValidationListener {

    //~ Methods ......................................................................................................................................

    /** Called on widget validation. */
    void onValidation(Widget widget, Model model, Option<Integer> item, final Option<String> subformPath, Seq<CheckMsg> messages);
}
