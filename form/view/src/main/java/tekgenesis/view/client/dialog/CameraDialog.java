
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.VideoElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import tekgenesis.view.client.ui.UploadUI;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.button;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

/**
 * Camera modal.
 */
public class CameraDialog extends ModalContent implements CropDialog.CropHandler, ModalListener {

    //~ Instance Fields ..............................................................................................................................

    private CanvasElement      canvas  = null;
    private CameraHandler      handler = null;
    private final FlowPanel    main;
    private final VideoElement video;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public CameraDialog(CameraHandler handler) {
        main = div();

        video = Document.get().createVideoElement();
        video.setAutoplay(true);

        main.getElement().insertFirst(video);

        final Button button = button();
        Icon.inWidget(button, UploadUI.CAMERA);
        button.addStyleName("video-button");

        button.addClickHandler(event -> {
            if (canvas != null) {
                takePicture();
                event.stopPropagation();
            }
        });

        main.add(button);
        this.handler = handler;

        setBody(main);
        setBodyOnly(true);
        setListener(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public void onCrop(String src, int x, int y, int width, int height) {
        // if (handler != null) handler.onPictureTaken(src, x, y, width, height);
    }

    @Override public void onHide(ModalButtonType buttonClicked) {
        stopCamera(video);
    }

    @Override public void onShow() {
        initVideo(video);
    }

    protected final native void initVideo(VideoElement videoElement)  /*-{
                                                                var instance = this;
                                                                var errorCallback = function(e) {
                                                                    console.log('Reeeejected!', e);
                                                                };
                                                                if (instance.videoStream) instance.videoStream.stop();
                                                                navigator.getUserMedia  = navigator.getUserMedia ||
                                                                    navigator.webkitGetUserMedia ||
                                                                    navigator.mozGetUserMedia ||
                                                                    navigator.msGetUserMedia;
    
                                                                if (navigator.getUserMedia) {
                                                                    navigator.getUserMedia({audio: true, video: true}, function(stream) {
                                                                        instance.videoStream  = stream;
                                                                        videoElement.src = $wnd.URL.createObjectURL(stream);
                                                                        instance.@tekgenesis.view.client.dialog.CameraDialog::initCanvas()();
                                                                    }, errorCallback);
                                                                } else {
                                                                    videoElement.src = 'somevideo.webm'; // fallback.
                                                                }
    }-*/;

    protected final native void stopCamera(VideoElement videoElement)  /*-{
                                                            if (this.videoStream) {
                                                                this.videoStream.getVideoTracks().forEach(function(track){
                                                                    track.stop();
                                                                });
                                                                this.videoStream.getAudioTracks().forEach(function(track){
                                                                    track.stop();
                                                                });
                                                                videoElement.src='';
                                                            }
    }-*/;

    void initCanvas() {
        if (canvas == null) {
            canvas = Document.get().createCanvasElement();
            canvas.addClassName("pic-canvas");
            main.getElement().appendChild(canvas);
            canvas.setWidth(video.getClientWidth());
            canvas.setHeight(video.getClientHeight());
        }
    }

    private void takePicture() {
        final int clientWidth  = video.getClientWidth();
        final int clientHeight = video.getClientHeight();
        canvas.setWidth(clientWidth);
        canvas.setHeight(clientHeight);
        final Context2d ctx = canvas.getContext2d();
        ctx.drawImage(video, 0, 0, canvas.getWidth(), canvas.getHeight());
        final String imageData = canvas.toDataUrl();
        handler.onPictureTaken(imageData, clientWidth, clientHeight);
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface CameraHandler {
        /** Called when Picture is taken. */
        void onPictureTaken(String imgData, int imgWidth, int imgHeight);
    }
}  // end class CameraDialog
