
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.type.ArrayType;
import tekgenesis.view.client.ui.base.*;
import tekgenesis.view.client.ui.modal.ModalListener;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.common.core.Constants.MULTIPLE;
import static tekgenesis.metadata.form.model.FormConstants.ANCHOR_DISABLED;
import static tekgenesis.metadata.form.model.FormConstants.THUMBNAILS;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.Widgets.checkArrayAccess;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;
import static tekgenesis.view.shared.utils.ResourceUtils.*;

/**
 * Base MediaUI.
 */
public abstract class BaseMediaUI extends FieldWidgetUI implements HasArrayValueUI, ImageModal.Listener, ModalListener {

    //~ Instance Fields ..............................................................................................................................

    ValueChangeHandler<Object> changeHandler = null;
    final Grid                 fileList;
    final FlowPanel            main;

    final List<Resource>       resources;
    final HtmlList.Unordered   thumbList     = ul();
    private final String       customVariant;
    private final List<String> filesUuid;
    private boolean            isEditable;
    private final String       largeVariant;
    private final ImageModal   modal;
    private int                showing       = 0;
    private final String       thumbVariant;

    //~ Constructors .................................................................................................................................

    BaseMediaUI(@NotNull final ModelUI container, @NotNull final tekgenesis.metadata.form.widget.Widget model) {
        super(container, model);
        resources     = new ArrayList<>();
        main          = div();
        thumbVariant  = model.getThumbVariant();
        largeVariant  = model.getLargeVariant();
        customVariant = model.getCustomVariant();
        initWidget(main);
        thumbList.addStyleName(MULTIPLE);
        thumbList.addStyleName(THUMBNAILS);
        thumbList.setVisible(false);
        filesUuid = new ArrayList<>();
        fileList  = new Grid();
        fileList.addStyleName("table-condensed table-striped");
        modal = new ImageModal();
        modal.setPrevNextListener(this);
        modal.addDisplayListener(this);
        main.add(modal);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(final ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void onHide(ModalButtonType buttonClicked) {
        modal.clear();
    }

    public void onNext() {
        displayImage(resources.get(getNext(showing)));
    }

    public void onPrevious() {
        displayImage(resources.get(getPrevious(showing)));
    }

    @Override public void onShow() {}

    @Nullable public DeleteHandler getDeleteHandler() {
        return null;
    }

    @Override public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
        disable(div, disabled);

        for (final Widget widget : thumbList) {
            if (widget instanceof ThumbPanel) disableAnchor(disabled, ((ThumbPanel) widget).getRemove());
            if (widget instanceof UploadPanel) disableAnchor(disabled, ((UploadPanel) widget).getRemove());
        }

        for (int i = 0; i < fileList.getRowCount(); i++)
            disableAnchor(disabled, fileList.getWidget(i, 4));
    }

    @NotNull @Override public Iterable<?> getValues() {
        checkArrayAccess(getModel());
        return ResourceUtils.getValuesFromResource(((ArrayType) getModel().getType()).getElementType(), resources);
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        checkArrayAccess(getModel());
        clearSetAndUpdate(ResourceUtils.getResourceValues(((ArrayType) getModel().getType()).getElementType(), values));
    }

    @Override public void setValues(@NotNull Iterable<Object> values, boolean fireEvents) {
        setValues(values);
    }

    protected boolean hasThumb(Resource resource) {
        return hasPreview(resource);
    }

    void addToFileList(@NotNull Resource resource, @Nullable Map<String, String> uploadedMap, @Nullable DeleteHandler deleteCallback) {
        if (!fileList.isVisible()) fileList.setVisible(true);
        filesUuid.add(resource.getUuid());
        final int row = fileList.getRowCount();
        fileList.resizeRows(row + 1);
        final int columns = isEditable ? 5 : 4;
        fileList.resizeColumns(columns);
        fileList.setWidget(row, 0, new Icon(IconType.FILE));
        fileList.setText(row, 1, resource.getMaster().getName());
        fileList.setText(row, 2, getMimeType(resource));
        fileList.setWidget(row, 3, createDownloadAnchor(resource));
        if (isEditable) fileList.setWidget(row, 4, createRemoveAnchor(resource, uploadedMap, deleteCallback));
    }

    void clearSetAndUpdate(Iterable<Resource> values) {
        reset();
        Colls.into(values, resources);
        updateThumbs();
    }

    abstract Widget createThumbPanel(Resource resource);

    void removeImage(String uuid) {
        final Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            if (next.toString().equals(uuid)) {     // compare uuid or url
                iterator.remove();
                assert changeHandler != null;
                changeHandler.onValueChange(null);  // hack
                break;
            }
        }
    }

    void reset() {
        resources.clear();
        thumbList.clear();
        final int rowCount = fileList.getRowCount();
        for (int i = 0; i < rowCount; i++)
            fileList.removeRow(0);
        filesUuid.clear();
    }

    SafeUri getDisplayLargeUri(Resource value) {
        return ResourceUtils.getSmallestDisplayUri(value, largeVariant).first();
    }

    Tuple<SafeUri, String> getDisplayThumbUri(Resource value) {
        return ResourceUtils.getSmallestThumbUri(value, thumbVariant);
    }

    void setEditable(boolean editable) {
        isEditable = editable;
    }

    private void addToThumbList(Resource resource) {
        if (!thumbList.isVisible()) thumbList.setVisible(true);
        thumbList.add(createThumbPanel(resource));
    }

    private Widget createDownloadAnchor(final Resource resource) {
        final Anchor download = anchor();
        download.setTitle(MSGS.download());
        download.getElement().appendChild(new Icon(IconType.DOWNLOAD).getElement());
        download.setHref(getUri(resource).asString());
        return download;
    }

    private Widget createRemoveAnchor(@NotNull final Resource resource, @Nullable final Map<String, String> uploadedMap,
                                      @Nullable final DeleteHandler deleteCallback) {
        final Anchor remove = anchor();
        remove.setTitle(MSGS.remove());
        remove.getElement().appendChild(new Icon(IconType.TRASH_O).getElement());
        remove.addClickHandler(event -> {
            removeImage(resource.getUuid());
            final int i = filesUuid.indexOf(resource.getUuid());
            fileList.removeRow(i);
            fileList.setVisible(fileList.getRowCount() > 0);
            filesUuid.remove(i);
            if (uploadedMap != null) uploadedMap.remove(resource.getMaster().getName());
            if (deleteCallback != null) deleteCallback.onDelete();
        });
        return remove;
    }

    private void disable(final Widget widget, boolean disabled) {
        if (widget instanceof HasEnabled) ((HasEnabled) widget).setEnabled(!disabled);

        if (widget instanceof HasWidgets) {
            for (final Widget w : (HasWidgets) widget)
                disable(w, disabled);
        }
    }

    private void disableAnchor(boolean disabled, @Nullable Widget remove) {
        if (remove != null) {
            if (disabled) remove.addStyleName(ANCHOR_DISABLED);
            else remove.removeStyleName(ANCHOR_DISABLED);
        }
    }

    private void displayImage(final Resource resource) {
        showing = resources.indexOf(resource);
        final SafeUri uri     = getCustomOrMasterUri(resource, customVariant).first();
        final Widget  display;
        final String  url     = uri.asString();
        if (isVideo(resource)) {
            display = new Frame(url);
            display.setStyleName("video-large");
            modal.setContent(display);
            modal.show();
        }
        else {
            display = HtmlWidgetFactory.img();
            ((Image) display).setUrl(url);
            final boolean scalableImage = isScalableImage(resource, url);
            if (scalableImage) display.addStyleName("min-image");
            display.setVisible(false);
            ((HasLoadHandlers) display).addLoadHandler(event -> {
                display.setVisible(true);
                modal.setContent(display);
                modal.show();
            });
        }
        modal.add(display);
        Image.prefetch(getUri(resources.get(getNext(showing))));  // prefetch onNext image
    }

    private void updateThumbs() {
        for (final Resource resource : resources) {
            if (hasThumb(resource)) addToThumbList(resource);
            else addToFileList(resource, null, getDeleteHandler());
        }
    }

    private int getNext(int current) {
        for (int i = current + 1; i < resources.size(); i++) {
            final Resource resource = resources.get(i);
            if (hasThumb(resource)) return i;
        }
        return getNext(-1);
    }

    private int getPrevious(int current) {
        for (int i = current - 1; i > -1; i--) {
            final Resource resource = resources.get(i);
            if (hasThumb(resource)) return i;
        }
        return getPrevious(resources.size());
    }

    private SafeUri getUri(Resource value) {
        return ResourceUtils.getUri(value).first();
    }

    //~ Static Fields ................................................................................................................................

    private static final String DOWNLOAD_ANCHOR_ID = "download_anchor";
    private static final String REMOVE_ANCHOR_ID   = "remove_anchor";

    @NonNls public static final String BLANK_DOWNLOAD_PARAMS = "status=0,toolbar=0,menubar=0,location=0";

    //~ Inner Interfaces .............................................................................................................................

    public interface DeleteHandler {
        /** Called when file is deleted. */
        void onDelete();
    }

    //~ Inner Classes ................................................................................................................................

    class ThumbClickHandler implements ClickHandler {
        @Override public void onClick(ClickEvent event) {
            final String uuid = event.getRelativeElement().getPropertyString(Constants.INDEX);
            for (final Resource resource : resources) {
                if (resource.toString().equals(uuid) && hasThumb(resource)) {
                    displayImage(resource);
                    break;
                }
            }
        }
    }
}  // end class BaseMediaUI
