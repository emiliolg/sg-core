
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.inbox.Inbox;
import tekgenesis.type.MetaModelKind;
import tekgenesis.view.client.Application;
import tekgenesis.view.shared.response.MenuOption;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.Response;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_E;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_I;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_N;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.metadata.form.model.FormConstants.BLANK;
import static tekgenesis.metadata.link.Links.formLink;
import static tekgenesis.type.MetaModelKind.FORM;
import static tekgenesis.type.MetaModelKind.LINK;
import static tekgenesis.view.client.Application.modal;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.menu.RecentPopup.getRecentPopup;
import static tekgenesis.view.client.menu.SuggestPopup.getSuggestPopup;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clearStyleName;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.hrefQuerySelector;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelector;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.targetElement;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;

/**
 * BaseMenu Callback.
 */
@SuppressWarnings("GwtToHtmlReferences")
public abstract class BaseMenu extends FlowPanel {

    //~ Constructors .................................................................................................................................

    BaseMenu() {
        addKeyHandlers();
    }

    //~ Methods ......................................................................................................................................

    protected abstract void added();
    protected abstract void addMenuButtons(MenuConfiguration configuration);

    void addRecent(@NotNull final String link, @NotNull final String label) {
        getSuggestPopup().addSuggestion(link, label);
        getRecentPopup().add(link, label);
    }

    void changeSelected(String fqn, String pk, String parameters) {
        deselectActive();
        final Element anchor = hrefQuerySelector(getElement(), formLink(fqn, pk, parameters));

        // Form may not be in the menu.
        if (anchor != null) {
            anchor.getParentElement().setClassName(ACTIVE_STYLE);
            getRecentPopup().appendRecentForm(fqn, pk, parameters);
            getRecentPopup().setRecentSelectedIndex(1);
        }
        formChanged(anchor);
    }

    void changeSelectedUrl(String url) {
        deselectActive();
        final Element anchor = hrefQuerySelector(getElement(), url);

        // Form may not be in the menu.
        if (anchor != null) {
            anchor.getParentElement().setClassName(ACTIVE_STYLE);
            getRecentPopup().appendRecentForm(url, null, null);
            getRecentPopup().setRecentSelectedIndex(1);
        }
        formChanged(anchor);
    }

    void clearSelection() {
        deselectActive();
        getRecentPopup().setRecentSelectedIndex(0);
        formCleared();
    }

    void formChanged(@Nullable final Element anchor) {}
    void formCleared() {}

    Anchor generateMenuItemAnchor(MenuOption menuItem) {
        final MetaModelKind type = menuItem.getType();
        final String        fqn  = menuItem.getTarget();

        final Anchor anchor = anchor(menuItem.getLabel());
        final String link   = type == FORM ? formLink(fqn, null, menuItem.getParametersQueryString()) : fqn;

        addRecent(link, menuItem.getLabel());

        if (type == FORM) {
            anchor.setHref(link);
            anchor.addClickHandler(event -> Application.getInstance().clearStack());
        }
        else if (type == LINK) {
            anchor.setHref(link);
            if (fqn.charAt(0) != '#' && fqn.charAt(0) != '/') anchor.setTarget(BLANK);                       // open the web page in a new window
        }
        return anchor;
    }

    void init(final Response<MenuResponse> result, final MenuConfiguration configuration) {
        populateMenu(result, isNotEmpty(configuration.getFqn()) && configuration.hideRoot(), configuration.getMaxMenus());
        addMenuButtons(configuration);
    }

    abstract void populateMenu(@NotNull final Response<MenuResponse> response, boolean hideRoot, int maxMenus);
    void toggleMenu() {}

    private void addKeyHandlers() {
        RootPanel.get().addDomHandler(this::keyUpHandler, KeyUpEvent.getType());
    }

    private void deselectActive() {
        /* Use native querySelector to change the active className */
        clearStyleName(querySelector(getElement(), "." + ACTIVE_STYLE));
    }

    private void keyUpHandler(final KeyUpEvent e) {
        final Element element = targetElement(e);
        final boolean noFocus = element.hasTagName("BODY");
        final int     keyCode = e.getNativeKeyCode();
        if (noFocus) {
            switch (keyCode) {
            case KeyCodes.KEY_M:
                toggleMenu();
                break;
            case KEY_I:
                getFormHistory().load(Inbox.INBOX_FQN, null);
                break;
            case KEY_E:
                modal(getRecentPopup());
                break;
            case KEY_N:
                modal(getSuggestPopup());
                break;
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    static final String FORM_DIV_ID = "form";  // this is really wrong
}                                              // end class BaseMenu
