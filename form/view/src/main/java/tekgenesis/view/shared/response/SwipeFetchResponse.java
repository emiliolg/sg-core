
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;

/**
 * FormResponse with the instances to swipe through.
 */
@SuppressWarnings("FieldMayBeFinal")
public class SwipeFetchResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private HashMap<Integer, FormModel> models;

    //~ Constructors .................................................................................................................................

    SwipeFetchResponse() {
        super();
        models = null;
    }

    /** Creates a SwipeFetchResponse with the given models. */
    public SwipeFetchResponse(@NotNull final HashMap<Integer, FormModel> models) {
        this.models = models;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the models this SwipeResponse have. */
    public HashMap<Integer, FormModel> getModels(Form form) {
        for (final FormModel model : models.values())
            model.init(form);
        return models;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8049755026308396232L;
}
