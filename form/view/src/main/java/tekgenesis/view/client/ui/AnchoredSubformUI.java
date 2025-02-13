
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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.SubformConfig;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.metadata.form.model.FormConstants.DISPLAY_WIDGET;
import static tekgenesis.metadata.form.model.FormConstants.ELLIPSIS;
import static tekgenesis.view.client.event.Events.fireNativeClickEvent;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;

/**
 * A Subform UI widget.
 */
public class AnchoredSubformUI extends FieldWidgetUI implements HasDisplayLinkUI, SubformUI {

    //~ Instance Fields ..............................................................................................................................

    private SubformConfig config = null;

    private boolean hidden;

    private final String icon;
    private final Anchor link = anchor(ELLIPSIS);

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public AnchoredSubformUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        hidden = true;
        initWidget(link);
        icon = model.getIconStyle();
    }

    //~ Methods ......................................................................................................................................

    /** Delegate click handler to gwt link. */
    public void addClickHandler(final ClickHandler handler) {
        link.addClickHandler(handler);
    }

    /** Apply Subform configuration. */
    public void applyConfig(SubformConfig c) {
        config = c;
        if (config.isVisible() == hidden) fireAnchorClicked();
    }

    /** Toggle subform visibility. */
    public void toggle() {
        if (config != null) config.setVisible(hidden);  // Sync config value with model
        hidden = !hidden;
    }

    /** Set focus to link. */
    public void setFocusLink() {
        link.setFocus(true);
    }

    /** Returns this Subform link text. */
    public String getLinkText() {
        return link.getText();
    }

    /** Set link text. */
    public void setLinkText(@Nullable String linkText) {
        final String text = linkText != null ? linkText.trim() : null;
        link.setText(notEmpty(text, ELLIPSIS));
        setIcon(icon);
    }

    /** Return if subform is hidden. */
    public boolean isHidden() {
        return hidden;
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        addStyleName(DISPLAY_WIDGET);
    }

    @Override void clear() {
        super.clear();
        setLinkText(null);
    }

    @NotNull @Override Option<Element> createIcon() {
        return of(link.getElement());
    }

    /** Programmatic link click. */
    private void fireAnchorClicked() {
        fireNativeClickEvent(link);
    }
}  // end class AnchoredSubformUI
