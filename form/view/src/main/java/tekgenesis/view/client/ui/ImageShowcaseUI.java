
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
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.ui.base.*;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.metadata.form.model.FormConstants.NO_IMAGE_URL;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;
import static tekgenesis.view.shared.utils.ResourceUtils.isVideo;
import static tekgenesis.view.shared.utils.UrlUtils.urlStringForBackgroundImage;

/**
 * Image Gallery Showcase.
 */
public class ImageShowcaseUI extends BaseMediaUI {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel controls;

    private boolean fitsInWidth;

    private final ClickablePanel mainImagePanel;
    private final ScrollPanel    scroll;
    private int                  thumbs;
    private final Frame          videoPanel;

    //~ Constructors .................................................................................................................................

    /** Creates default Image Gallery Showcase. */
    public ImageShowcaseUI(@NotNull final ModelUI container, @NotNull final tekgenesis.metadata.form.widget.Widget model) {
        super(container, model);
        mainImagePanel = new ClickablePanel();
        mainImagePanel.addStyleName("main-image");
        main.insert(mainImagePanel, 0);
        // noinspection DuplicateStringLiteralInspection
        addStyleName("showcase");
        main.remove(thumbList);

        controls = div();
        controls.addStyleName("showcaseControls");
        scroll = new ScrollPanel(thumbList);
        // noinspection SpellCheckingInspection
        scroll.addStyleName("scroller");
        controls.add(scroll);
        main.add(controls);
        fitsInWidth = true;

        videoPanel = new Frame();
        videoPanel.setStyleName(VideoPanel.VIDEO_FRAME);
    }

    //~ Methods ......................................................................................................................................

    /** All resources (including external ones) should be image type. */
    @Override protected boolean hasThumb(Resource resource) {
        return true;
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    @Override Widget createThumbPanel(final Resource resource) {
        final ImageAnchor thumb = new ImageAnchor();
        thumb.setBackgroundImage(getDisplayThumbUri(resource));
        thumb.addClickHandler(event -> setMainImage(resource));
        thumb.addMouseOverHandler(event -> setMainImage(resource));
        if (isVideo(resource)) thumb.addIcon(IconType.PLAY);
        final HtmlList.Item li = li();
        li.add(thumb);
        thumbs++;
        if (thumbs == 1)
            mainImagePanel.getElement().getStyle().setBackgroundImage(urlStringForBackgroundImage(ResourceUtils.getUri(resource).first().asString()));
        if (fitsInWidth && thumbs > 5) {
            fitsInWidth = false;
            addScrollingButtons();
        }
        return li;
    }

    @Override void reset() {
        super.reset();
        thumbs = 0;
        mainImagePanel.getElement().getStyle().setBackgroundImage(urlStringForBackgroundImage(NO_IMAGE_URL));
    }

    private void addScrollingButtons() {
        scroll.addStyleName("scroll-shortened");
        final ExtButton leftButton  = new ExtButton();
        final ExtButton rightButton = new ExtButton();

        leftButton.setIcon(IconType.CHEVRON_LEFT);
        rightButton.setIcon(IconType.CHEVRON_RIGHT);

        leftButton.addClickHandler(event -> scroll.setHorizontalScrollPosition(scroll.getHorizontalScrollPosition() - SCROLL_DISTANCE));
        rightButton.addClickHandler(event -> scroll.setHorizontalScrollPosition(scroll.getHorizontalScrollPosition() + SCROLL_DISTANCE));

        controls.insert(leftButton, 0);
        controls.insert(rightButton, 2);
    }

    private void setMainImage(Resource resource) {
        final SafeUri uri = getDisplayLargeUri(resource);
        if (isVideo(resource)) {
            mainImagePanel.getElement().getStyle().setBackgroundImage("none");
            videoPanel.setUrl(uri);
            mainImagePanel.add(videoPanel);
        }
        else {
            mainImagePanel.remove(videoPanel);
            mainImagePanel.getElement().getStyle().setBackgroundImage(urlStringForBackgroundImage(uri.asString()));
            mainImagePanel.addClickHandler(new ThumbClickHandler());
            mainImagePanel.getElement().setPropertyObject(FormConstants.INDEX, resource.toString());
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int SCROLL_DISTANCE = 150;
}  // end class ImageShowcaseUI
