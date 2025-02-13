
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.ui.base.ImageAnchor;
import tekgenesis.view.client.ui.base.ThumbPanel;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.metadata.form.model.FormConstants.INDEX;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.imageAnchor;
import static tekgenesis.view.shared.utils.ResourceUtils.isVideo;

/**
 * Image Gallery.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")  // only for testing
public class GalleryUI extends BaseMediaUI {

    //~ Constructors .................................................................................................................................

    // boolean         multiple;

    /** Creates default Image Gallery. */
    public GalleryUI(@NotNull final ModelUI container, @NotNull final tekgenesis.metadata.form.widget.Widget model) {
        super(container, model);
        thumbList.setStyleName("gallery");

        // multiple = model.isMultiple();
        // if (multiple) modal.setPrevNextListener(prevHandler, nextHandler, keyHandler);
        fileList.setVisible(false);
        main.add(thumbList);
        main.add(fileList);
    }  // end constructor GalleryUI

    //~ Methods ......................................................................................................................................

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    @Override Widget createThumbPanel(Resource resource) {
        final ImageAnchor thumb = imageAnchor();
        thumb.setBackgroundImage(getDisplayThumbUri(resource));
        if (isVideo(resource)) thumb.addIcon(IconType.PLAY);
        thumb.getElement().setPropertyObject(INDEX, resource.toString());
        thumb.addClickHandler(new ThumbClickHandler());
        return new ThumbPanel(thumb, resource.getMaster().getName(), ResourceUtils.getUri(resource).first(), null);
    }
}  // end class GalleryUI
