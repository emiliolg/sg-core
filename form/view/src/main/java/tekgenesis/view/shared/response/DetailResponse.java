
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
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;

/**
 * FormResponse with the instance to show detail.
 */
@SuppressWarnings({ "FieldMayBeFinal", "GwtInconsistentSerializableClass" })
public class DetailResponse implements Serializable, MessagedResponse {

    //~ Instance Fields ..............................................................................................................................

    private Form    form;
    private boolean fullscreen;
    private int     height;
    private int     marginTop;

    private String message;

    private FormModel     model;
    private List<UiModel> references;
    private int           width;

    //~ Constructors .................................................................................................................................

    private DetailResponse() {
        super();
        model      = null;
        form       = null;
        message    = null;
        references = null;
    }

    /** Creates a DetailResponse with the given model. */
    public DetailResponse(@NotNull final FormModel model, @NotNull final Form form) {
        this();
        this.model = model;
        this.form  = form;
    }

    //~ Methods ......................................................................................................................................

    /** Width and height of detail popup. By default, we will center it. */
    public void setDimension(int w, int h) {
        width  = w;
        height = h;
    }

    /** Returns this detail form. */
    public Form getForm() {
        return form;
    }

    /** Sets the modal to full screen mode. */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    /** Returns popup panels height. */
    public int getHeight() {
        return height;
    }

    /** Returns the margin top of the dialog. */
    public int getMarginTop() {
        return marginTop;
    }

    /** Sets the margin top of the dialog. */
    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    @Override public String getMessage() {
        return message;
    }

    @Override public void setMessage(String msg) {
        message = msg;
    }

    /** Returns this detail model. */
    public FormModel getModel() {
        model.init(form);
        return model;
    }

    /** Returns true if popup panels must be displayed in full screen mode. */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /** Returns all attached references. */
    public Iterable<UiModel> getUiReferences() {
        return references;
    }

    /** Sets the ui metamodel references. */
    public void setUiReferences(List<UiModel> r) {
        references = r;
    }

    /** Returns popup panels width. */
    public int getWidth() {
        return width;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8049755026308396232L;
}  // end class DetailResponse
