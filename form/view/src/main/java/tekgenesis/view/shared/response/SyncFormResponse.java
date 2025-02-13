
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.util.GwtReplaceable.isClient;
import static tekgenesis.view.shared.FormHandling.initMetadata;

/**
 * Response with constraints and sync behavior.
 */
@SuppressWarnings("FieldMayBeFinal")
public class SyncFormResponse extends FormResponse {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private SourceWidget source = null;

    //~ Constructors .................................................................................................................................

    /** Serialization. */
    public SyncFormResponse() {
        super();
    }

    /** Constructs a SyncFormResponse from the FormModel. */
    public SyncFormResponse(FormModel model) {
        super(model);
    }

    //~ Methods ......................................................................................................................................

    /** Ensures that subforms metamodels are initialized. */
    public void ensureMetamodelsInitialization(final Form form, @NotNull UiModelRetriever retriever) {
        initMetadata(super.getModel(form), retriever);
    }

    /** Sync and return the same Model instance. Just for the client! */
    public Option<FormModel> syncModel(final Form form, final FormModel actual) {
        // noinspection ConstantConditions
        if (!isClient()) throw new IllegalStateException("This method should only be called from the client");

        final Option<FormModel> result;

        if (accepts(actual)) {
            final FormModel other = super.getModel(form);
            actual.sync(other);
            result = some(actual);
        }
        else result = Option.empty();

        return result;
    }

    /** Returns the Model. Just for the server! */
    @Override public FormModel getModel(Form meta) {
        // noinspection ConstantConditions
        if (isClient()) throw new IllegalStateException("This method should only be called from the server");
        return super.getModel(meta);
    }

    @Nullable public SourceWidget getSource() {
        return source;
    }

    public void setSourceWidget(@NotNull SourceWidget s) {
        source = s;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8005633027600798439L;
}  // end class SyncFormResponse
