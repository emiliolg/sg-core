
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import javax.annotation.Nullable;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;

import static tekgenesis.metadata.form.model.FormConstants.UPLOAD_IMG;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * Thumb Panel.
 */
public class ThumbPanel extends Composite {

    //~ Instance Fields ..............................................................................................................................

    private final ImageAnchor imageAnchor = null;
    private Anchor            remove      = null;

    //~ Constructors .................................................................................................................................

    /** Thmb panel constructor. */
    public ThumbPanel(ImageAnchor imageAnchor, @NotNull String name, @NotNull final SafeUri downloadUrl, @Nullable ClickHandler removeHandler) {
        final HtmlList.Item li    = li();
        final FlowPanel     thumb = div();
        thumb.setStyleName(FormConstants.THUMBNAIL + UPLOAD_IMG);

        thumb.add(imageAnchor);

        if (removeHandler != null) {
            final FlowPanel caption = div();
            caption.setStyleName(FormConstants.CAPTION);
            caption.add(span(name));
            remove = anchor();
            remove.setTitle(MSGS.remove());
            remove.getElement().appendChild(new Icon(IconType.TRASH_O).getElement());
            remove.addClickHandler(removeHandler);
            final Anchor download = anchor();
            download.setTitle(MSGS.download());
            download.getElement().appendChild(new Icon(IconType.DOWNLOAD).getElement());
            download.setHref(downloadUrl.asString());
            download.addStyleName("download-icon");
            caption.add(remove);
            caption.add(download);
            thumb.add(caption);
        }
        li.add(thumb);
        initWidget(li);
    }

    //~ Methods ......................................................................................................................................

    /** Returns remove anchor. */
    @Nullable public Anchor getRemove() {
        return remove;
    }
}  // end class ThumbPanel
