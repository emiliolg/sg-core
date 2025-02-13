
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import com.google.code.gwt.crop.client.GWTCropper;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.utils.Image;

/**
 * Shortcuts help modal.
 */
public class CropDialog extends ModalContent {

    //~ Constructors .................................................................................................................................

    public CropDialog(final Image img, float ratio, int minWidth, int minHeight, @NotNull final CropHandler handler) {
        float     r    = 1;
        final int h    = img.getHeight();
        final int w    = img.getWidth();
        int       newH = h;
        int       newW = w;
        if (h > IMG_HEIGHT) {  // adjust image ratio
            newH = IMG_HEIGHT;
            r    = (float) newH / h;
            newW = (int) (w * r);
        }
        final FlowPanel  panel   = new FlowPanel();
        final GWTCropper cropper = new GWTCropper(img.getSrc());
        cropper.setSize(newW, newH);
        cropper.setAspectRatio(ratio);
        cropper.setInitialSelection(-1, -1, ((int) (minWidth * r)), ((int) (minHeight * r)));
        cropper.setMinimalWidth((int) (minWidth * r));
        cropper.setMinimalHeight((int) (minHeight * r));
        panel.add(cropper);
        final Button button = new Button("Apply");
        button.addStyleName("btn btn-primary");
        setCloseButton(true);
        setClickOutsideDisabled(true);

        final float finalR = r;
        button.addClickHandler(event ->
                handler.onCrop(img.getSrc(),
                    ((int) (cropper.getSelectionXCoordinate() / finalR)),
                    ((int) (cropper.getSelectionYCoordinate() / finalR)),
                    ((int) (cropper.getSelectionWidth() / finalR)),
                    ((int) (cropper.getSelectionHeight() / finalR))));

        setBody(panel);
        setTitle("Crop Image");
        setFooter(button);
    }

    //~ Static Fields ................................................................................................................................

    private static final int IMG_HEIGHT = 455;

    //~ Inner Interfaces .............................................................................................................................

    public interface CropHandler {
        /** Called when Crop button is clicked. */
        void onCrop(String src, int x, int y, int width, int height);
    }
}  // end class CropDialog
