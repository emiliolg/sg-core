
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.type.resource.AbstractResource;

import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.img;
import static tekgenesis.view.shared.utils.UrlUtils.urlStringForBackgroundImage;

/**
 * Anchor with a setImage Method.
 */
public class ImageAnchor extends Anchor {

    //~ Instance Fields ..............................................................................................................................

    private FlowPanel current = div();

    //~ Constructors .................................................................................................................................

    /** Creates default ImageAnchor. */
    public ImageAnchor() {
        super(true);
        setImage(img());
    }

    //~ Methods ......................................................................................................................................

    /** Adds Icon to anchor. */
    public void addIcon(final IconType type) {
        final Element element = getElement();
        DOM.appendChild(element, new Icon(type).getElement());
    }

    /** Set Image as Background Image. */
    public void setBackgroundImage(Tuple<SafeUri, String> imageURL) {
        if (!imageURL.second().equals(AbstractResource.THUMB)) current.addStyleName("cover-background");
        current.getElement().getStyle().setBackgroundImage(urlStringForBackgroundImage(imageURL.first().asString()));
    }

    /** Sets Image to anchor. */
    public void setImage(final Image img) {
        final String backgroundImage = img.getElement().getStyle().getBackgroundImage();
        current.getElement().getStyle().setBackgroundImage(backgroundImage);
        final Element element = getElement();
        DOM.appendChild(element, current.getElement());
    }

    /** Sets Div to anchor. */
    public void setImage(final FlowPanel img) {
        if (current != null) current.getElement().removeFromParent();
        current = img;
        final Element element = getElement();
        DOM.appendChild(element, img.getElement());
    }
}  // end class ImageAnchor
