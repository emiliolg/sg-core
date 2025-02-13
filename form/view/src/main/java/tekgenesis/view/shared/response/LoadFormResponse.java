
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;

/**
 * Response with also a Form.
 */
@SuppressWarnings("FieldMayBeFinal")
public class LoadFormResponse extends FormResponse {

    //~ Instance Fields ..............................................................................................................................

    private Form          form;
    private String        parameters;
    private String        pk;
    private List<UiModel> references;

    //~ Constructors .................................................................................................................................

    LoadFormResponse() {
        super();
        form       = null;
        pk         = null;
        parameters = null;
        references = null;
    }

    /** Constructs a LoadFormResponse from the Form and the FormModel. */
    public LoadFormResponse(FormModel model, Form form) {
        this(model, form, null);
    }

    /** Constructs a LoadFormResponse from the Form and the FormModel. */
    LoadFormResponse(final FormModel model, final Form form, @Nullable final String pk) {
        super(model);
        this.form  = form;
        this.pk    = pk;
        parameters = null;
        references = null;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Form. */
    public Form getForm() {
        return form;
    }

    /** Returns the Model. */
    public FormModel getModel() {
        return getModel(form);
    }

    /** Returns the parameters (a.k.a queryString) (if any) of the loaded model. */
    public String getParameters() {
        return parameters;
    }

    /** Set load parameters. */
    public void setParameters(@Nullable String ps) {
        parameters = ps;
    }

    /** Returns the primary key (if any) of the loaded model. */
    @Override public String getPk() {
        return pk;
    }

    /** Set load pk. */
    public void setPk(@Nullable String pk) {
        this.pk = pk;
    }

    /** Returns all attached references. */
    public Iterable<UiModel> getUiReferences() {
        return references;
    }

    /** Sets the ui metamodel references. */
    public void setUiReferences(List<UiModel> r) {
        references = r;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6905633027600760439L;
}  // end class LoadFormResponse
