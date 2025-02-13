
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.FormBoxListener;
import tekgenesis.view.client.service.BaseAsyncCallback;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.response.ResponseError;
import tekgenesis.view.shared.service.FormService;

import static java.util.Arrays.asList;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.view.client.menu.MenuConfiguration.LEFT_POSITION;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.label;

/**
 * CLass responsibly of fetching and attaching a menu.
 */
public class MenuBox implements FormBoxListener {

    //~ Instance Fields ..............................................................................................................................

    private BaseMenu baseMenu = null;

    private final MenuConfiguration     configuration;
    private final List<MenuBoxListener> listeners;
    private final Panel                 menuPanel;

    //~ Constructors .................................................................................................................................

    private MenuBox(final Panel panel, final MenuConfiguration configuration, final MenuBoxListener... ls) {
        this.configuration = configuration;
        menuPanel          = panel;

        listeners = new ArrayList<>();
        listeners.addAll(asList(ls));

        // Fetch the MenuResponse from the server and create the menu
        FormService.App.getInstance().getMenu(configuration.getFqn(), new BaseAsyncCallback<Response<MenuResponse>>() {
                @Override public void onSuccess(Response<MenuResponse> result) {
                    clearLoadingMsg();
                    menuPanel.clear();
                    baseMenu = createMenu(result);
                    menuPanel.add(baseMenu);
                    baseMenu.added();

                    for (final MenuBoxListener listener : listeners)
                        listener.onLoad();
                }

                @Override public void onFailure(@NotNull final String message, @NotNull final Throwable caught) {
                    clearLoadingMsg();
                    final Label loadingMsg = label(message);
                    loadingMsg.setStyleName("loading-menu");
                    menuPanel.add(loadingMsg);
                }
            });
    }

    //~ Methods ......................................................................................................................................

    /** Adds a listener. */
    public void addListener(MenuBoxListener l) {
        listeners.add(l);
    }

    /** Detaches this menu box. */
    public void detach() {
        if (baseMenu != null) baseMenu.clear();
        listeners.clear();
        menuPanel.clear();
        menuPanel.removeFromParent();
        menuBoxes.remove(this);
    }

    @Override public void onError(ResponseError error) {}

    @Override public void onLoad(String url) {
        if (baseMenu != null) baseMenu.changeSelectedUrl("#" + url);
    }

    @Override public void onLoad(String fqn, String pk, String parameters) {
        if (baseMenu != null) baseMenu.changeSelected(fqn, pk, parameters);
    }

    @Override public void onUnload() {
        if (baseMenu != null) baseMenu.clearSelection();
    }

    @Override public void onUpdate() {}

    /** Returns menu implementation. */
    public BaseMenu getBaseMenu() {
        return baseMenu;
    }

    private void clearLoadingMsg() {
        menuPanel.getElement().setInnerHTML("");  // clear "Loading app..." msg
    }

    private BaseMenu createMenu(Response<MenuResponse> result) {
        final BaseMenu menu = configuration.getPosition().equals(LEFT_POSITION) ? new LeftMenu() : new TopMenu();
        menu.init(result, configuration);
        return menu;
    }

    //~ Methods ......................................................................................................................................

    /** Fetches and attaches the menu to the dom. */
    public static void attach(@NotNull final String menuId, @NotNull final MenuConfiguration configuration, final MenuBoxListener... ls) {
        final RootPanel menuPanel = RootPanel.get(menuId);
        final MenuBox   menuBox   = new MenuBox(menuPanel, configuration, ls);
        Application.getInstance().registerMenuElement(menuPanel.getElement());
        menuBoxes.add(menuBox);
    }

    /** Returns registered menus. */
    public static List<MenuBox> getMenuBoxes() {
        return menuBoxes;
    }

    //~ Static Fields ................................................................................................................................

    private static final List<MenuBox> menuBoxes = new ArrayList<>();

    private static final Logger logger = getLogger(MenuBox.class);

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Interface to be implemented by the someone that wants to get notified that the menu is
     * loaded.
     */
    public interface MenuBoxListener {
        /** Menu is loaded. */
        void onLoad();
    }
}  // end class MenuBox
