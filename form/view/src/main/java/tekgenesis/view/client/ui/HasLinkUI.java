
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.link.Links;
import tekgenesis.type.ArrayType;

import static tekgenesis.metadata.form.model.FormConstants.BLANK;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;

/**
 * UI that displays a link.
 */
public interface HasLinkUI extends HasClickUI {

    //~ Methods ......................................................................................................................................

    /** Adds a click handler for link_form. */
    void addClickHandler(final ClickHandler handler);

    /** Adds uri for link option. */
    void setLink(@Nullable final String url);

    /** Get optional pk value. */
    @Nullable String getLinkPk();

    /** Adds the pk to the form link uri for link option. */
    void setLinkPk(@Nullable String pk);

    //~ Inner Classes ................................................................................................................................

    class FormLinkUtil {
        private FormLinkUtil() {}

        /** Returns true if the link pk can be deduced form the model value of the widget. */
        static boolean hasAutoLinkPk(@NotNull Widget model) {
            return model.getLinkForm().isNotEmpty() && model.getLinkPkExpression().isEmpty() &&
                   (model.isMultiple() ? ((ArrayType) model.getType()).getElementType() : model.getType()).isDatabaseObject();
        }

        static boolean hasLink(@NotNull Widget model) {
            return model.getLinkForm().isNotEmpty() || !model.getLinkExpression().isEmpty() || model.hasOnClickMethod();
        }

        static Anchor link(@NotNull final Widget model) {
            final Anchor anchor = anchor();
            if (model.isTargetBlank()) anchor.setTarget(BLANK);
            if (model.getLinkForm().isNotEmpty()) anchor.setHref(Links.formLink(model.getLinkForm().getFullName(), null));
            return anchor;
        }

        static void updateLinkPk(@NotNull final Anchor anchor, @NotNull final Widget model, @Nullable final String pk) {
            anchor.setHref(Links.formLink(model.getLinkForm().getFullName(), pk));
        }
    }
}  // end interface HasLinkUI
