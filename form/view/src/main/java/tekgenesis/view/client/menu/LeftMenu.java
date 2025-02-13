
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.type.MetaModelKind;
import tekgenesis.view.client.FormStorage;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.ModalGlass;
import tekgenesis.view.shared.response.MenuOption;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.Response;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.metadata.form.model.FormConstants.ROTATE_STYLE;
import static tekgenesis.metadata.form.widget.IconType.CHEVRON_LEFT;
import static tekgenesis.metadata.form.widget.IconType.CHEVRON_RIGHT;
import static tekgenesis.metadata.form.widget.IconType.THUMB_TACK;
import static tekgenesis.type.MetaModelKind.MENU;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.menu.LeftMenu.LeftMenuState.retrieveState;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.ul;

/**
 * Left Menu Bar.
 */
@SuppressWarnings("GwtToHtmlReferences")
public class LeftMenu extends BaseMenu {

    //~ Instance Fields ..............................................................................................................................

    private FlowPanel dockDiv        = null;
    private ExtButton showMenuButton = null;

    private final LeftMenuState state;

    //~ Constructors .................................................................................................................................

    LeftMenu() {
        state = retrieveState();
    }

    //~ Methods ......................................................................................................................................

    @Override protected void added() {
        if (state.hidden) hideMenu();
        else showMenu();
    }

    protected void addMenuButtons(MenuConfiguration configuration) {
        final FlowPanel buttonContainer = div();
        buttonContainer.addStyleName("menu-buttons");

        showMenuButton = new ExtButton();
        showMenuButton.setIcon(CHEVRON_RIGHT);
        showMenuButton.setStyleName("show-menu-button well");
        showMenuButton.setTitle(MSGS.showMenu());
        showMenuButton.addClickHandler(event -> showMenu());
        RootPanel.get().add(showMenuButton);

        final ExtButton hideButton = new ExtButton();
        hideButton.removeStyleName("btn btn-default");
        hideButton.setIcon(CHEVRON_LEFT);
        hideButton.setTitle(MSGS.hideMenu());
        hideButton.addStyleName("hide-menu");
        hideButton.addClickHandler(event -> hideMenu());

        dockDiv = div();
        dockDiv.addStyleName("dock-menu");

        final ExtButton dockButton = new ExtButton();
        dockButton.removeStyleName("btn btn-default");
        if (!state.docked) dockDiv.addStyleName(ROTATE_STYLE);
        dockButton.setIcon(THUMB_TACK);
        dockButton.addClickHandler(event -> dock());
        dockDiv.add(dockButton);

        buttonContainer.add(dockDiv);
        buttonContainer.add(hideButton);

        insert(buttonContainer, 0);
    }  // end method addMenuButtons

    @Override protected void formChanged(Element anchor) {
        if (!state.docked) hideMenu();
    }

    @Override protected void formCleared() {
        if (!state.docked) showMenu();
    }

    @Override protected void populateMenu(@NotNull final Response<MenuResponse> result, boolean hideRoot, int maxMenus) {
        final HtmlList.Unordered ul = ul();
        ul.setStyleName("treeview menuBar");

        // populate tree menu
        final Map<MenuOption, List<MenuOption>> menusMap    = result.getResponse().getMenus();
        final Map<MenuOption, List<MenuOption>> subMenusMap = result.getResponse().getSubMenus();

        final Set<Map.Entry<MenuOption, List<MenuOption>>> menus = menusMap.entrySet();

        final Map<String, Boolean> menuCustomizations = getMenuCustomizations();

        if (hideRoot && menus.size() == 1) {
            for (final Map.Entry<MenuOption, List<MenuOption>> menu : menus) {
                final MenuOption menuKey = menu.getKey();
                generateRootlessMenu(ul, menusMap, subMenusMap, menuKey, menuCustomizations);
            }
        }
        else {
            for (final Map.Entry<MenuOption, List<MenuOption>> menu : menus) {
                final MenuOption menuKey = menu.getKey();
                generateMenu(ul, menusMap, subMenusMap, menuKey, true, menuCustomizations);
            }
        }

        setStyleName("left-hideable");
        add(ul);

        addAttachHandler(event -> addContentMargin());
    }

    @Override protected void toggleMenu() {
        if (state.hidden) showMenu();
        else hideMenu();
    }

    private void addContentMargin() {
        if (state.docked) {
            final RootPanel form = RootPanel.get(FORM_DIV_ID);
            if (form != null) form.getElement().getStyle().setMarginLeft(getOffsetWidth() + MARGIN_LEFT, Style.Unit.PX);
        }
    }

    private void dock() {
        if (state.docked) {
            state.docked = false;
            dockDiv.addStyleName(ROTATE_STYLE);
            removeContentMargin();
            setGlass();
        }
        else {
            state.docked = true;
            dockDiv.removeStyleName(ROTATE_STYLE);
            addContentMargin();
            ModalGlass.hide();
            getElement().getStyle().setZIndex(Z_INDEX_BTM);
        }
        state.store();
    }

    private boolean generateMenu(@NotNull final HtmlList.Unordered ul, @NotNull final Map<MenuOption, List<MenuOption>> menusMap,
                                 @NotNull final Map<MenuOption, List<MenuOption>> subMenusMap, @NotNull final MenuOption menu, boolean expandMenu,
                                 final Map<String, Boolean> menuCustomizations) {
        final List<MenuOption> menuItemList = menusMap.get(menu);

        final HtmlList.Unordered subUl = ul();
        subUl.addStyleName("nav-list");

        final int itemsGenerated = generateMenuItems(subMenusMap, menuItemList, subUl, menuCustomizations);

        boolean menuAdded = false;

        if (itemsGenerated > 0) {
            final HtmlList.Item li = li();

            ul.add(li);

            final String uid = menu.getFullQualifiedName();

            final Icon icon = new Icon(IconType.CARET_RIGHT);
            icon.addStyleName("fa-lg");
            icon.addStyleName("menuBar");

            final InputElement input = DOM.createInputCheck().cast();
            input.setId(uid);

            final Boolean isCustomized = menuCustomizations.get(uid);
            boolean       menuStatus   = expandMenu;
            if (isCustomized != null) menuStatus = isCustomized;
            input.setChecked(menuStatus);

            final HtmlList.Label label = new HtmlList.Label(menu.getLabel(), uid);

            Event.sinkEvents(input, Event.ONCHANGE);

            Event.setEventListener(input,
                event -> {
                    final EventTarget  eventTarget = event.getEventTarget();
                    final InputElement cast        = eventTarget.cast();
                    final String       id          = cast.getId();

                    final boolean checked = cast.isChecked();
                    menuCustomizations.put(id, checked);

                    for (final FormStorage storage : FormStorage.getInstance()) {
                        final Set<String> menusStatus = menuCustomizations.keySet();

                        final StringBuilder buffer = new StringBuilder();
                        for (final String fqn : menusStatus)
                            buffer.append(fqn).append('=').append(menuCustomizations.get(fqn) ? 1 : 0).append(',');
                        storage.set(MENU_STATUS, buffer.toString());
                    }
                    if (Event.ONCHANGE == event.getTypeInt()) addContentMargin();
                });

            li.getElement().appendChild(input);
            li.add(icon);
            li.add(label);

            li.add(subUl);

            menuAdded = true;
        }
        return menuAdded;
    }  // end method generateMenu

    private int generateMenuItems(@NotNull final Map<MenuOption, List<MenuOption>> subMenusMap, @NotNull final List<MenuOption> menuItems,
                                  @NotNull final HtmlList.Unordered subUl, final Map<String, Boolean> menuCustomizations) {
        int itemsGenerated = 0;

        for (final MenuOption menuItem : menuItems) {
            final MetaModelKind type   = menuItem.getType();
            final String        target = menuItem.getTarget();

            if (type == MENU) {
                final Option<MenuOption> filter = Colls.filter(subMenusMap.keySet(),
                        item -> item != null && target.equals(item.getFullQualifiedName()))
                                                  .getFirst();

                if (filter.isEmpty()) logger.info("Menu '" + target + "' was not found");
                else {
                    final MenuOption subMenu = filter.get();

                    final boolean wasGenerated = generateMenu(subUl, subMenusMap, subMenusMap, subMenu, false, menuCustomizations);
                    if (wasGenerated) itemsGenerated++;
                }
            }
            else {
                final HtmlList.Item menuItemLi = li();
                subUl.add(menuItemLi);
                menuItemLi.add(generateMenuItemAnchor(menuItem));
                itemsGenerated++;
            }
        }
        return itemsGenerated;
    }  // end method generateMenuItems

    private void generateRootlessMenu(HtmlList.Unordered ul, Map<MenuOption, List<MenuOption>> menusMap,
                                      Map<MenuOption, List<MenuOption>> subMenusMap, MenuOption menuKey, Map<String, Boolean> menuCustomizations) {
        final List<MenuOption> menuItemList = menusMap.get(menuKey);
        generateMenuItems(subMenusMap, menuItemList, ul, menuCustomizations);
    }

    private void hideMenu() {
        state.hidden = true;
        final Style style = getElement().getStyle();
        style.setLeft(-(getElement().getOffsetWidth()) - LEFT, Style.Unit.PX);
        removeContentMargin();
        showMenuButton.getElement().getStyle().setLeft(0, Style.Unit.PX);
        if (!state.docked) {
            ModalGlass.hide();
            style.setZIndex(Z_INDEX_BTM);
        }
        state.store();
    }

    private void removeContentMargin() {
        RootPanel.get(FORM_DIV_ID).getElement().getStyle().setMarginLeft(MARGIN_LEFT, Style.Unit.PX);
    }

    private void showMenu() {
        state.hidden = false;
        getElement().getStyle().setDisplay(Style.Display.BLOCK);
        getElement().getStyle().setLeft(0, Style.Unit.PX);
        if (state.docked) addContentMargin();
        else setGlass();
        showMenuButton.getElement().getStyle().setLeft(B_LEFT, Style.Unit.PX);
        state.store();
    }

    private void setGlass() {
        final ModalGlass glass = ModalGlass.show(true);
        getElement().getStyle().setZIndex(Z_INDEX_TOP);
        glass.setClickHandler(event -> hideMenu());
        glass.setKeyHandler(event -> hideMenu());
    }

    private Map<String, Boolean> getMenuCustomizations() {
        final String               mapStr             = FormStorage.getInstance().get().get(MENU_STATUS);
        final Map<String, Boolean> menuCustomizations = new HashMap<>();

        if (!isEmpty(mapStr)) {
            @SuppressWarnings("NonJREEmulationClassesInClientCode")
            final String[] split = mapStr.split(",");
            for (final String pair : split) {
                final int idx = pair.lastIndexOf('=');

                final boolean expanded = Integer.parseInt(pair.substring(idx + 1, idx + 2)) == 1;
                menuCustomizations.put(pair.substring(0, idx), expanded);
            }
        }
        return menuCustomizations;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String MENU_STATE_KEY = "LEFT_MENU_STATE";

    private static final int LEFT        = 30;
    private static final int B_LEFT      = -35;
    private static final int Z_INDEX_TOP = 1000;
    private static final int Z_INDEX_BTM = 800;

    private static final int    MARGIN_LEFT = 20;
    private static final String MENU_STATUS = "menu-status";
    private static final Logger logger      = getLogger(LeftMenu.class);

    //~ Inner Classes ................................................................................................................................

    static class LeftMenuState {
        boolean docked = true;

        boolean hidden = false;

        void store() {
            FormStorage.getInstance().ifPresent(s -> s.set(MENU_STATE_KEY, toJSON()));
        }

        private String toJSON() {
            final JSONObject o = new JSONObject();
            o.put(HIDDEN_KEY, JSONBoolean.getInstance(hidden));
            o.put(DOCKED_KEY, JSONBoolean.getInstance(docked));
            return o.toString();
        }

        static LeftMenuState retrieveState() {
            return FormStorage.getInstance().map(s -> fromJSON(s.get(MENU_STATE_KEY))).orElseGet(LeftMenuState::new);
        }

        private static LeftMenuState fromJSON(@Nullable final String json) {
            if (json == null) return new LeftMenuState();

            final JSONObject object = JSONParser.parseStrict(json).isObject();

            final LeftMenuState s = new LeftMenuState();
            s.hidden = object.get(HIDDEN_KEY).isBoolean().booleanValue();
            s.docked = object.get(DOCKED_KEY).isBoolean().booleanValue();
            return s;
        }

        @NonNls private static final String HIDDEN_KEY = "hidden";
        @NonNls private static final String DOCKED_KEY = "docked";
    }
}
