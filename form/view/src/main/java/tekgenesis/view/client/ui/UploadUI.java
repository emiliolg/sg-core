
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.media.Mime;
import tekgenesis.metadata.form.configuration.UploadConfig;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.dialog.CameraDialog;
import tekgenesis.view.client.dialog.CropDialog;
import tekgenesis.view.client.ui.base.*;
import tekgenesis.view.client.ui.modal.ModalSubscription;
import tekgenesis.view.client.ui.utils.Image;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.ACCEPT;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.metadata.form.configuration.UploadConfig.DEFAULT;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;
import static tekgenesis.metadata.form.model.FormConstants.INDEX;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.UploadPanel.FinishEvent;
import static tekgenesis.view.client.ui.Widgets.checkScalarAccess;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clickElement;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;
import static tekgenesis.view.shared.utils.ResourceUtils.*;

/**
 * Renders an upload widget.
 */
public class UploadUI extends BaseMediaUI implements HasScalarValueUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private UploadConfig config = null;
    private TextField    field  = null;

    private final FileUpload          fileUpload;
    private final UploadUI.Handler    handler;
    private final boolean             multiple;
    private ModalSubscription         subscription;
    private final FlowPanel           uploadControls;
    private final Map<String, String> uploads;

    //~ Constructors .................................................................................................................................

    /** Creates a Label UI widget. */
    public UploadUI(@NotNull final ModelUI container, @NotNull final tekgenesis.metadata.form.widget.Widget model) {
        super(container, model);
        applyConfig(DEFAULT);
        setEditable(true);
        uploadControls = new FlowPanel();
        uploadControls.addStyleName("uploadControls");
        handler  = new Handler();
        multiple = model.isMultiple();
        uploads  = new HashMap<>();
        thumbList.addStyleName("uploadThumbs");
        thumbList.setVisible(true);
        fileUpload = new FileUpload();
        fileUpload.getElement().setPropertyBoolean(Constants.MULTIPLE, multiple);
        final Mime fileType = model.getFileType();
        if (fileType != Mime.ALL) fileUpload.getElement().setPropertyString(ACCEPT, fileType.name());
        fileUpload.addChangeHandler(handler);
        fileUpload.setVisible(false);
        fileList.setVisible(false);
        subscription = null;

        uploadControls.add(fileUpload);
        if (fileType != Mime.VIDEO) uploadControls.add(createVisibleUpload(fileUpload));
        if (model.getCamera()) uploadControls.add(createCamera(handler));

        main.add(uploadControls);
        main.add(fileList);
        main.add(thumbList);

        switch (fileType) {
        case VIDEO:
            addTextInput(handler);
            break;
        case ALL:
            addTextInput(handler);
            break;
        default:
            break;
        }

        addBitlessDomHandler(handler, DropEvent.getType());
        addBitlessDomHandler(handler, DragOverEvent.getType());
        addBitlessDomHandler(handler, DragLeaveEvent.getType());

        setFocusTarget(ofNullable(field));
    }  // end ctor UploadUI

    //~ Methods ......................................................................................................................................

    /** Applies a given config. */
    public void applyConfig(UploadConfig c) {
        if (c != config) config = c;
    }

    @Nullable @Override public DeleteHandler getDeleteHandler() {
        return handler;
    }

    @Override public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
        if (disabled) fileUpload.setEnabled(false);
    }

    @Nullable @Override public Object getValue() {
        checkScalarAccess(getModel());
        return resources.isEmpty() ? null : resources.get(0);
    }

    @Override public void setValue(@Nullable Object value) {
        checkScalarAccess(getModel());
        if (value != null) {
            clearSetAndUpdate(listOf((Resource) value));
            hideControls(true);
        }
        else {
            reset();
            fileList.resizeRows(0);
            hideControls(false);
        }
    }  // end method setValue

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        setValue(value);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(FormConstants.UPLOAD);
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    @Override Widget createThumbPanel(final Resource resource) {
        final ImageAnchor thumb = imageAnchor();
        thumb.setBackgroundImage(getDisplayThumbUri(resource));
        if (isVideo(resource)) thumb.addIcon(IconType.PLAY);
        thumb.getElement().setPropertyObject(INDEX, resource.toString());
        thumb.addClickHandler(new ThumbClickHandler());
        return new ThumbPanel(thumb,
            resource.getMaster().getName(),
            ResourceUtils.getUri(resource).first(),
            event -> {
                final int index = resources.indexOf(resource);
                removeImage(resource.toString());
                // remove from uploads list  uploads.remove(source.getFileName());
                thumbList.remove(index);
                hideControls(false);
            });
    }

    @Override void reset() {
        super.reset();
        resetFileUpload();
        uploads.clear();
    }

    private void addResource(Resource resource) {
        resources.add(resource);
        assert changeHandler != null;
        changeHandler.onValueChange(null);  // hack
    }

    private void addTextInput(Handler clickHandler) {
        field = textField();
        field.addStyleName(FORM_CONTROL);
        HtmlDomUtils.setPlaceholder(field, MSGS.insertURL());  // should be MSGS.insertUrl()
        final ExtButton button = new ExtButton(new Icon(IconType.LINK));
        button.addClickHandler(clickHandler);
        uploadControls.add(field);
        uploadControls.add(button);
    }

    private void clearImages() {
        resources.clear();
        assert changeHandler != null;
        changeHandler.onValueChange(null);  // hack
    }

    private Widget createCamera(final Handler cameraHandler) {
        final Button button = button();
        Icon.inWidget(button, CAMERA);
        button.addClickHandler(event -> {
            event.stopPropagation();
            subscription = Application.modal(new CameraDialog(cameraHandler));
        });
        return button;
    }

    private ExtButton createVisibleUpload(final Widget upload) {
        final ExtButton browse = new ExtButton(MSGS.browse(), IconType.UPLOAD);
        browse.addClickHandler(event -> {
            final boolean enabled     = fileUpload.isEnabled();
            final boolean notEmpty    = !isEmpty(fileUpload.getFilename());
            final boolean notUploaded = !uploads.containsKey(fileUpload.getFilename());
            if (enabled && notEmpty && notUploaded) {
                resetFileUpload();
                handler.resetHidedPendings();
            }
            clickElement(upload.getElement());
        });
        return browse;
    }

    private void hideControls(boolean hide) {
        uploadControls.setVisible(!hide);
    }

    private void resetFileUpload() {
        final InputElement inputElem = fileUpload.getElement().cast();
        inputElem.setValue("");
    }

    private void showErrorMessage(@NotNull String fileName, String msg) {
        main.add(new MessageWidget(fileName, msg, CheckType.ERROR));
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String CAMERA = "camera";

    @NonNls private static final String HOVER_CLASS_NAME = "hover";

    //~ Inner Classes ................................................................................................................................

    private class Handler
        implements ChangeHandler, DropHandler, DragLeaveHandler, DragOverHandler, Consumer<FinishEvent>, ClickHandler, CameraDialog.CameraHandler,
                   CropDialog.CropHandler, DeleteHandler
    {
        private ModalSubscription       cropSubscription = null;
        private final List<UploadPanel> pendingUploads;

        private Handler() {
            pendingUploads = new ArrayList<>();
        }

        @Override public void accept(final FinishEvent data) {
            final UploadPanel source = data.getSource();
            pendingUploads.remove(source);

            final Option<Resource> resource = data.getResource();
            final String           fileName = source.getFileName();
            final String           fileUrl  = source.getUrl();
            if (resource.isPresent()) {
                // uploaded
                final Resource upload = resource.get();
                if (upload.getMaster().isExternal() && hasPreview(upload)) source.getLink().setBackgroundImage(getDisplayThumbUri(upload));

                if (!hasPreview(upload)) {
                    thumbList.remove(source);
                    addToFileList(upload, uploads, this);
                }
                else if (source.isCamera() || config.getCrop()) source.setThumbSrc(getUri(resource.get()));

                source.getLink().getElement().setPropertyString(Constants.INDEX, upload.getUuid());
                source.getLink().addClickHandler(new ThumbClickHandler());
                if (!multiple) {
                    uploads.clear();
                    clearImages();
                    hideControls(true);
                }

                uploads.put(isEmpty(fileName) ? fileUrl : fileName, upload.getUuid());
                addResource(upload);
            }
            else {
                // error or cancel
                uploads.remove(isEmpty(fileName) ? fileUrl : fileName);
                thumbList.remove(source);
                if (!data.isCanceled()) showErrorMessage(notNull(fileName, ""), data.getMsg());
                resetFileUpload();
                removeImage(source.getUploadedId());
                if (!multiple) hideControls(false);
            }

            // loadMainImage();
            startNextUpload();
        }  // end method accept

        @Override public void onChange(final ChangeEvent event) {
            uploadFile(event.getNativeEvent().getEventTarget());
        }

        @Override public void onClick(ClickEvent event) {
            final String value = field.getValue();
            if (isNotEmpty(value)) uploadExternalFile(value);
            field.setText(null);
        }

        @Override public void onCrop(String src, int x, int y, int width, int height) {
            if (cropSubscription != null) cropSubscription.hide();

            final UploadPanel.JsFile jsFile = JavaScriptObject.createObject().cast();
            jsFile.fromCamera(src);
            jsFile.setCrop(y, x, width, height);
            uploadFile(jsFile);
        }

        @Override public void onDelete() {
            if (!multiple) hideControls(false);
            resetFileUpload();
        }

        @Override public void onDragLeave(final DragLeaveEvent event) {
            stopEvent(event);
            removeStyleName(HOVER_CLASS_NAME);
        }

        @Override public void onDragOver(final DragOverEvent event) {
            stopEvent(event);
            addStyleName(HOVER_CLASS_NAME);
        }

        @Override public void onDrop(final DropEvent event) {
            stopEvent(event);
            removeStyleName(HOVER_CLASS_NAME);
            final String url = event.getData("url");
            if (isNotEmpty(url)) uploadExternalFile(url);
            else uploadFile(event.getNativeEvent().getDataTransfer());
        }

        @Override public void onPictureTaken(String imageData, int imgWidth, int imgHeight) {
            subscription.hide();

            if (config.getCrop()) cropSubscription = Application.modal(new CropDialog(new Image(imgWidth, imgHeight, imageData), 1, 0, 0, this));
            else onCrop(imageData, 0, 0, imgWidth, imgHeight);
        }

        void resetHidedPendings() {
            for (final UploadPanel panel : pendingUploads) {
                if (!panel.isVisible()) pendingUploads.remove(panel);
            }
        }

        private boolean acceptFile(final UploadPanel.JsFile file) {
            final String name = file.getName();
            final String url  = file.getUrl();
            return acceptFileType(file) && (!isUploaded(name != null ? name : url) || file.isCamera());
        }

        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        private boolean acceptFileType(final UploadPanel.JsFile file) {
            final Mime fileType = getModel().getFileType();
            switch (fileType) {
            case ALL:
                return true;
            case IMAGE:
                return file.isImage();
            case VIDEO:
                return file.isExternal();
            case DOCUMENT:
                return file.isDocument();
            default:
                final String type = file.getType();
                System.out.println("type = " + type);
                return type.toLowerCase().contains(fileType.getMime());
            }
        }

        private void startNextUpload() {
            if (!pendingUploads.isEmpty()) pendingUploads.get(0).startUpload(false);
        }

        private void stopEvent(final DomEvent<?> event) {
            event.stopPropagation();
            event.preventDefault();
        }

        private void uploadExternalFile(final String url) {
            if (isDisabled()) return;
            final UploadPanel.JsFile jsFile = JavaScriptObject.createObject().cast();
            jsFile.asExternal(url);
            uploadFile(jsFile);
        }

        private void uploadFile(final JavaScriptObject target) {
            if (isDisabled()) return;
            final Element                     targetElm = target.cast();
            final JsArray<UploadPanel.JsFile> files     = targetElm.getPropertyJSO("files").cast();
            for (int i = 0; i < files.length(); i++)
                uploadFile(files.get(i));
        }

        private void uploadFile(UploadPanel.JsFile file) {
            if (acceptFile(file)) {
                final UploadPanel upload = new UploadPanel(file, this, config);
                if (!multiple) {
                    thumbList.clear();
                    fileList.resize(0, 0);
                }
                thumbList.add(upload);
                pendingUploads.add(upload);
                if (pendingUploads.size() == 1) upload.startUpload(file.isCamera());
            }
        }

        private boolean isUploaded(final String fileName) {
            boolean uploaded = uploads.containsKey(fileName);
            if (!uploaded) {
                for (final UploadPanel pendingUpload : pendingUploads) {
                    if (pendingUpload.getFileName().equals(fileName) && pendingUpload.isVisible()) {
                        uploaded = true;  // uploading
                        break;
                    }
                }
            }
            return uploaded;
        }
    }  // end class Handler
}  // end class UploadUI
