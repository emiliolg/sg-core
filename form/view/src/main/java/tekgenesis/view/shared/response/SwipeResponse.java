
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;

/**
 * FormResponse with the instances to swipe through.
 */
@SuppressWarnings({ "FieldMayBeFinal", "ParameterHidesMemberVariable" })
public class SwipeResponse extends SwipeFetchResponse implements MessagedResponse {

    //~ Instance Fields ..............................................................................................................................

    private int           fetchSize;
    private Form          form;
    private boolean       fullscreen;
    private int           height;
    private List<Integer> indexesMap;
    private String        loaderClassName;

    private boolean loop;
    private int     marginTop;

    private String        message;
    private List<UiModel> references;
    private int           startIndex;
    private int           width;

    //~ Constructors .................................................................................................................................

    SwipeResponse() {
        super();
        form            = null;
        message         = null;
        loaderClassName = null;
        indexesMap      = null;
        references      = null;
    }

    /** Creates a Swipe Response. */
    public SwipeResponse(@NotNull final Form form, @NotNull final String loaderClassName, @NotNull final HashMap<Integer, FormModel> models,
                         @NotNull final List<Integer> indexesMap) {
        super(models);
        this.form            = form;
        this.loaderClassName = loaderClassName;
        this.indexesMap      = indexesMap;
        message              = null;
        references           = null;
    }

    //~ Methods ......................................................................................................................................

    /** Configs the Swipe Response with the given parameters. */
    public void config(int startIndex, boolean loop, int fetchSize) {
        this.startIndex = startIndex;
        this.loop       = loop;
        this.fetchSize  = fetchSize;
    }

    /** Width and height of swiper popup. By default, we will center it. */
    public void setDimension(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    /** Returns the fetch size of this response. */
    public int getFetchSize() {
        return fetchSize;
    }

    /** Returns the model metadata of this response. */
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

    /** Returns the map that is used to remap indexes. */
    public List<Integer> getIndexesMap() {
        return indexesMap;
    }

    /** Returns which class is used to load (fetch) more models. */
    public String getLoaderClassName() {
        return loaderClassName;
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

    /** Returns the different models of this response. */
    public HashMap<Integer, FormModel> getModels() {
        return getModels(form);
    }

    /** Returns true if popup panels must be displayed in full screen mode. */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /** Returns true if this response is configured to cyclic. */
    public boolean isLoop() {
        return loop;
    }

    /** Returns the start index of this response. */
    public int getStartIndex() {
        return startIndex;
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

    private static final long serialVersionUID = 8901564328631834262L;
}  // end class SwipeResponse
