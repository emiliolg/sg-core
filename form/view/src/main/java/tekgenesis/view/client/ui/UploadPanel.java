
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.function.Consumer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.configuration.UploadConfig;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.type.resource.AbstractResource;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.dialog.CropDialog;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.ImageAnchor;
import tekgenesis.view.client.ui.modal.ModalSubscription;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.media.Mimes.APPLICATION;
import static tekgenesis.common.media.Mimes.TEXT;
import static tekgenesis.metadata.form.model.FormConstants.IMAGE;
import static tekgenesis.metadata.form.model.FormConstants.PROGRESS;
import static tekgenesis.metadata.form.model.FormConstants.UPLOAD_IMG;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

class UploadPanel extends Composite implements ClickHandler, CropDialog.CropHandler {

    //~ Instance Fields ..............................................................................................................................

    final Anchor remove;

    private final FlowPanel caption;

    private final UploadConfig    config;
    private JsFile                file           = null;
    private Consumer<FinishEvent> finishCallback = null;
    private ImageAnchor           imageAnchor    = null;

    private boolean           pendingUpload = false;
    private FlowPanel         progressBar   = null;
    private ModalSubscription subscription;

    private String  uploadedId = "";
    private boolean valid      = false;

    //~ Constructors .................................................................................................................................

    UploadPanel(final JsFile file, final Consumer<FinishEvent> finishCallback, final UploadConfig config) {
        this.file           = file;
        this.finishCallback = finishCallback;
        this.config         = config;
        subscription        = null;

        final HtmlList.Item li = li();
        // li.addStyleName("col-sm-6 col-md-4");

        initWidget(li);
        setVisible(false);

        imageAnchor = imageAnchor();
        final FlowPanel thumb = div();
        thumb.setStyleName(FormConstants.THUMBNAIL + UPLOAD_IMG);

        // create thumb
        if (file.isImage() && !file.isPSD()) {
            if (file.isCamera()) {
                final Image image = img();
                image.getElement().setAttribute("src", "data:image/png;base64," + file.getBase64());
                imageAnchor.setImage(image);
            }
            else {
                final FlowPanel image = div();
                readImage(file, image.getElement());
                imageAnchor.setImage(image);
            }

            thumb.add(imageAnchor);
            valid = false;
        }
        else valid = true;

        thumb.add(imageAnchor);

        // create caption with: label, links and progress
        caption = div();
        caption.setStyleName(FormConstants.CAPTION);

        // label
        caption.add(span(file.asString()));

        // cancel link
        remove = anchor();
        remove.setTitle(MSGS.remove());
        remove.getElement().appendChild(new Icon(IconType.TRASH_O).getElement());
        remove.addClickHandler(this);
        caption.add(remove);

        // progressBar
        final FlowPanel progress = div();
        progress.setStyleName(PROGRESS);
        progressBar = div();
        progressBar.setStyleName("progress-bar");
        progress.add(progressBar);
        caption.add(progress);

        thumb.add(caption);
        li.add(thumb);
    }  // end ctor UploadPanel

    //~ Methods ......................................................................................................................................

    @Override public void onClick(final ClickEvent event) {
        abortUpload();  // abort upload
        finishCallback.accept(new FinishEvent(this, "", true));
    }

    @Override public void onCrop(String src, int x, int y, int width, int height) {
        subscription.hide();
        file.setCrop(y, x, width, height);
        final Image image = new Image(src, x, y, width, height);
        imageAnchor.setImage(image);
        uploadFile(file);
        setVisible(true);
    }

    public boolean isCamera() {
        return file != null && file.isCamera();
    }

    public ImageAnchor getLink() {
        return imageAnchor;
    }

    @NotNull public Anchor getRemove() {
        return remove;
    }

    public void setThumbSrc(Tuple<SafeUri, String> uri) {
        imageAnchor.setBackgroundImage(uri);
    }

    void startUpload(boolean force) {
        pendingUpload = true;
        if (valid || force) {
            uploadFile(file);
            setVisible(true);
        }
    }

    String getFileName() {
        return file.getName();
    }

    String getUploadedId() {
        return uploadedId;
    }

    String getUrl() {
        return file.getUrl();
    }

    private native void abortUpload()  /*-{ if (this.xhr) this.xhr.abort(); }-*/;

    private void showCropModal(int width, int height, @NotNull String src, float ratio, int minWidth, int minHeight) {
        subscription = Application.modal(
                new CropDialog(new tekgenesis.view.client.ui.utils.Image(width, height, src), ratio, minWidth, minHeight, this));
    }

    private void uploadComplete(final String uuid, final boolean external, final String name, final String url, final String thumbUrl,
                                final String mimeType) {
        progressBar.getParent().removeFromParent();
        uploadedId = uuid;

        // update thumb name
        caption.remove(0);
        caption.insert(span(name), 0);

        final Resource resource = AbstractResource.createSimpleResource(uuid, external, name, url, thumbUrl, mimeType);
        /*if (resource.getMaster().isExternal() && hasPreview(resource)) {
         *  if (isNotEmpty(thumbUrl)) imageAnchor.setBackgroundImage(getSmallestThumbUri(resource, Variant.THUMB.name()));
         *  else imageAnchor.setBackgroundImage(getSmallestThumbUri(resource, Variant.THUMB.name()));
         *}*/

        finishCallback.accept(new FinishEvent(this, resource));
    }

    private void uploadError(final String msg) {
        finishCallback.accept(new FinishEvent(this, msg, false));
    }

    private void uploadProgress(final double loaded, final double total) {
        final long progress = Math.round(loaded / total * 100);
        progressBar.getElement().getStyle().setWidth(progress, Style.Unit.PCT);
        progressBar.getElement().setInnerText(progress + "%");
    }

    //J-
    private native void readImage(final JsFile f, final Element divElem)  /*-{
        var instance = this;

        var reader = new FileReader();
        reader.onload = function (event) {
            divElem.style.backgroundImage = 'url(' + event.target.result + ')';
            divElem.style.backgroundSize = 'cover';

            var img = new Image();
            img.onload = function () {
                instance.@tekgenesis.view.client.ui.UploadPanel::validateSize(IILjava/lang/String;)(this.width, this.height, this.src)
            };
            img.onerror = function () {
                instance.@tekgenesis.view.client.ui.UploadPanel::uploadError(Ljava/lang/String;)("Error loading image");
            };

            img.src = $wnd.URL.createObjectURL(f);
        };
        reader.onerror = function () {
            instance.@tekgenesis.view.client.ui.UploadPanel::uploadError(Ljava/lang/String;)("Error reading image");
        };
        reader.readAsDataURL(f);
    }-*/;
    //J+

    //J-
    private native void uploadFile(final JsFile f)  /*-{
        //create ajax request with the file
        var instance = this;
        this.xhr = new XMLHttpRequest();

        //noinspection JSUnresolvedVariable
        this.xhr.upload.addEventListener("progress", function(event) {
            //noinspection JSUnresolvedVariable
            instance.@tekgenesis.view.client.ui.UploadPanel::uploadProgress(DD)(event.loaded, event.total);
        });
        this.xhr.addEventListener("load", function(event) {
            var r = JSON.parse(event.target.responseText);
            //noinspection JSUnresolvedVariable
            if (r.uploaded)
                instance.@tekgenesis.view.client.ui.UploadPanel::uploadComplete(Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(r.uuid, r.external, r.name, r.url, r.thumbUrl, r.mimeType);
            else
                instance.@tekgenesis.view.client.ui.UploadPanel::uploadError(Ljava/lang/String;)(r.msg);
        });
        this.xhr.addEventListener("error", function() {
            instance.@tekgenesis.view.client.ui.UploadPanel::uploadError(Ljava/lang/String;)("Error uploading");
        });

        if (f.base64){
            var cameraServletUrl = "/sg/camera";
            if (f.crop) cameraServletUrl+="?left="+f.crop.left+"&top="+f.crop.top+"&width="+f.crop.width+"&height="+f.crop.height;
            this.xhr.open("POST",  cameraServletUrl);
            var cameraData = new FormData();
            cameraData.append("base64", f.base64);
            this.xhr.send(cameraData);
        }

        else if (f.url){
            this.xhr.open("POST", "/sg/upload?url="+ f.url);
            this.xhr.send();
        } else {
            var uploadServletUrl = "/sg/upload";
            if (f.crop){
                uploadServletUrl = uploadServletUrl+"?left="+f.crop.left+"&top="+f.crop.top+"&width="+f.crop.width+"&height="+f.crop.height;
            }
            this.xhr.open("POST", uploadServletUrl);
            var uploadData = new FormData();
            uploadData.append("file", f);
            this.xhr.send(uploadData);
        }
    }-*/;
    //J+

    private void validateSize(int width, int height, String src) {
        final int     maxWidth    = config.getMaxWidth();
        final int     maxHeight   = config.getMaxHeight();
        final int     minWidth    = config.getMinWidth();
        final int     minHeight   = config.getMinHeight();
        final int     widthRatio  = config.getWidthRatio();
        final int     heightRatio = config.getHeightRatio();
        final boolean crop        = config.getCrop();

        valid = true;
        if (crop) {
            showCropModal(width, height, src, (heightRatio == 0) ? 0 : ((float) widthRatio) / heightRatio, minWidth, minHeight);
            return;
        }

        String message = "";
        if ((maxWidth == minWidth && maxHeight == minHeight && minHeight != 0 && minWidth != 0)) {
            valid   = (width == maxWidth && height == maxHeight);
            message = MSGS.imageShouldBe(maxWidth, maxHeight);
        }
        else if ((maxWidth != 0 || maxHeight != 0)) {
            valid   = (width <= maxWidth && height <= maxHeight);
            message = MSGS.imageTooBig(maxWidth, maxHeight);
        }
        else if ((minWidth != 0 || minHeight != 0)) {
            valid   = (width >= minWidth && height >= minHeight);
            message = MSGS.imageTooSmall(minWidth, minHeight);
        }

        if (widthRatio != 0 && heightRatio != 0) {
            final float image = (float) width / height;
            final float ratio = (float) widthRatio / heightRatio;
            valid   = (image == ratio);
            message = MSGS.imageRatioShouldBe(widthRatio, heightRatio);
        }

        if (valid) {
            if (pendingUpload) {
                uploadFile(file);
                setVisible(true);
            }
        }
        else uploadError(message);
    }

    //~ Inner Classes ................................................................................................................................

    static class FinishEvent {
        @NotNull final String                   msg;
        private final boolean                   canceled;
        @NotNull private final Option<Resource> resource;
        @NotNull private final UploadPanel      source;

        private FinishEvent(@NotNull final UploadPanel source, @Nullable final Resource resource) {
            canceled      = false;
            this.source   = source;
            this.resource = Option.option(resource);
            msg           = "OK";
        }

        private FinishEvent(@NotNull final UploadPanel source, @NotNull String msg, boolean canceled) {
            this.canceled = canceled;
            this.source   = source;
            resource      = Option.empty();
            this.msg      = msg;
        }

        boolean isCanceled() {
            return canceled;
        }
        @NotNull String getMsg() {
            return msg;
        }
        @NotNull Option<Resource> getResource() {
            return resource;
        }
        @NotNull UploadPanel getSource() {
            return source;
        }
    }

    /**
     * File overlay type.
     */
    static class JsFile extends JavaScriptObject {
        /** Overlay types always have protected, zero-arg ctors. */
        protected JsFile() {}

        public final native void fromCamera(final String base64)  /*-{
                                                                                                                                                                                                                                                                                                                                                                                                                                this.base64 = base64.replace('data:image/png;base64,', '');
                                                                                                                                                                                                                                                                                                                                                                                                                                this.type = 'image/png';
                                                                                                                                                                                                                                                                                                                                                                                                                                this.size = 0;
                                                                                                                                                                                                                                                                                                                                                                                                                                this.name = 'camera.png';
                                                                                                                                                                                                                                                                                                                                                                                                                                this.isCamera = true;
                                                                                                                                                                                                                                                                                                                        }-*/;

        public final native boolean isCamera()  /*-{ return this.isCamera == true; }-*/;

        public final native String getBase64()  /*-{ return this.base64;  }-*/;

        public final boolean isPSD() {
            // noinspection NonJREEmulationClassesInClientCode
            return getName().contains(".psd");
        }

        public final boolean isImage() {
            final String type = getType();
            // noinspection NonJREEmulationClassesInClientCode
            return type != null && type.startsWith(IMAGE);
        }

        public final boolean isExternal() {
            return isNotEmpty(getUrl());
        }

        public final native String getName()  /*-{ return this.name; }-*/;

        public final boolean isDocument() {
            final String type = getType();
            // noinspection NonJREEmulationClassesInClientCode
            return type != null && (type.startsWith(APPLICATION) || type.startsWith(TEXT));
        }

        public final native String getType()  /*-{ return this.type;  }-*/;

        public final native String getUrl()  /*-{ return this.url;  }-*/;

        protected final native void asExternal(final String url)  /*-{ this.url = url;  }-*/;

        protected final native void setCrop(int top, int left, int width, int height)  /*-{
                                                                                                                                                                                                                                                                                                                                                                                this.crop = {top:top , left:left, width:width, height:height};
                                                                                                                                                                                                                                                                                        }-*/;

        private String asString() {
            final String url = getUrl();
            return isNotEmpty(url) ? url : getName() + " (" + getSizeStr() + ")";
        }

        private native int getSize()  /*-{ return this.size;  }-*/;

        private String getSizeStr() {
            return getSize() > INT_1024 * INT_1024 ? (Math.round(getSize() * 100 / (INT_1024 * INT_1024)) / 100) + "MB"
                                                   : (Math.round(getSize() * 100 / INT_1024) / 100) + "KB";
        }

        private static final int INT_1024 = 1024;
    }  // end class JsFile
}  // end class UploadPanel
