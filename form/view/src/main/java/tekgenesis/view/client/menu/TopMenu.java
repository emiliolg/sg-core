
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.type.MetaModelKind;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.shared.response.MenuOption;
import tekgenesis.view.shared.response.MenuResponse;
import tekgenesis.view.shared.response.Response;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.metadata.form.widget.IconType.CARET_DOWN;
import static tekgenesis.type.MetaModelKind.MENU;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.findParentElement;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelectorAll;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.ul;

/**
 * Top Menu Bar.
 */
public class TopMenu extends BaseMenu {

    //~ Methods ......................................................................................................................................

    @Override protected void added() {}

    @Override protected void addMenuButtons(MenuConfiguration configuration) {
        // add searchBar?
    }

    @Override protected void populateMenu(@NotNull Response<MenuResponse> response, boolean hideRoot, int maxMenus) {
        final HtmlList.Unordered ul = ul();
        ul.addStyleName("nav navbar-nav " + FLOATING_MODAL + " sui-menu");

        // addResizeListener();
        final Map<MenuOption, List<MenuOption>> menusMap    = response.getResponse().getMenus();
        final Map<MenuOption, List<MenuOption>> subMenusMap = response.getResponse().getSubMenus();

        final Set<Map.Entry<MenuOption, List<MenuOption>>> menus = menusMap.entrySet();

        final int     topLevelSize   = menusMap.values().iterator().next().size();
        final boolean shouldHideRoot = menus.size() == 1 && (hideRoot || topLevelSize < maxMenus);

        if (shouldHideRoot) {
            for (final Map.Entry<MenuOption, List<MenuOption>> menu : menus) {
                final MenuOption menuKey = menu.getKey();
                generateRootlessMenu(ul, menusMap, subMenusMap, menuKey);
            }
        }
        else {
            for (final Map.Entry<MenuOption, List<MenuOption>> menu : menus) {
                final MenuOption menuKey = menu.getKey();
                generateMenu(ul, menusMap, subMenusMap, menuKey, true);
            }
        }
        add(ul);
    }

    @Override void formChanged(@Nullable final Element anchor) {
        // close opened menus
        closeAllMenus();
        if (anchor != null) {
            final Element ul = anchor.getParentElement().getParentElement();
            ul.getStyle().setDisplay(Style.Display.NONE);
            ul.removeClassName(OPEN);
            new Timer() {
                    @Override public void run() {
                        ul.getStyle().clearDisplay();
                    }
                }.schedule(100);
            final Element menu = findParentElement("push-overlay-nav", ul, false);
            if (menu != null) menu.removeClassName("in");
        }
    }

    //J-
    private void addKeyHandler(@NotNull final Widget li, boolean isMenu, boolean openDown, @NotNull final Widget subUl) {
        li.addDomHandler(event -> {
            event.preventDefault();
            handleKey(li, isMenu, openDown, subUl, event);
            event.stopPropagation();
        },
        KeyDownEvent.getType());
    }
    //J+

    private void closeAllMenus() {
        final JsArray<Element> opened = querySelectorAll(RootPanel.get().getElement(), "nav li.open");
        for (int i = 0; i < opened.length(); i++)
            opened.get(i).removeClassName(OPEN);
    }

    private void focusFirstChild(@NotNull final Widget subUl) {
        subUl.getElement().getFirstChildElement().getFirstChildElement().focus();
    }

    private void focusNextElement(@NotNull final Widget li, boolean next, boolean checkMenus) {
        final Element sibling   = next ? li.getElement().getNextSiblingElement() : li.getElement().getPreviousSiblingElement();
        final Element nextFocus;
        if (sibling == null) {
            final List<Element> menuElements = Application.getInstance().getMenuElements();
            final int           size         = menuElements.size();
            if (size > 1 && checkMenus) nextFocus = getNextMenu(li, menuElements, size, next);
            else nextFocus = next ? li.getParent().getElement().getFirstChildElement() : (Element) li.getParent().getElement().getLastChild();
        }
        else nextFocus = sibling;
        nextFocus.getFirstChildElement().focus();
    }

    private int generateMenuItems(@NotNull final Map<MenuOption, List<MenuOption>> subMenusMap, @NotNull final List<MenuOption> menuItems,
                                  @NotNull final HtmlList.Unordered subUl, boolean isFirstLevel) {
        int itemsGenerated = 0;

        for (final MenuOption menuItem : menuItems) {
            final MetaModelKind type   = menuItem.getType();
            final String        target = menuItem.getTarget();

            if (type == MENU) {
                final Option<MenuOption> filter = Colls.filter(subMenusMap.keySet(),
                        item -> item != null && target.equals(item.getFullQualifiedName()))
                                                  .getFirst();

                if (filter.isEmpty()) logger.info("Menu '" + target + "' not  found");
                else {
                    final MenuOption subMenu      = filter.get();
                    final boolean    wasGenerated = generateMenu(subUl, subMenusMap, subMenusMap, subMenu, isFirstLevel);
                    if (wasGenerated) itemsGenerated++;
                }
            }
            else {
                final HtmlList.Item menuItemLi = li();
                subUl.add(menuItemLi);
                menuItemLi.add(generateMenuItemAnchor(menuItem));
                addKeyHandler(menuItemLi, false, isFirstLevel, subUl);
                itemsGenerated++;
            }
        }
        return itemsGenerated;
    }  // end method generateMenuItems

    private void generateRootlessMenu(HtmlList.Unordered ul, Map<MenuOption, List<MenuOption>> menusMap,
                                      Map<MenuOption, List<MenuOption>> subMenusMap, MenuOption menuKey) {
        final List<MenuOption> menuItemList = menusMap.get(menuKey);
        generateMenuItems(subMenusMap, menuItemList, ul, true);
    }

    //J-
    private boolean generateMenu(@NotNull final HtmlList.Unordered ul, @NotNull final Map<MenuOption, List<MenuOption>> menusMap,
                                 @NotNull final Map<MenuOption, List<MenuOption>> subMenusMap, @NotNull final MenuOption menu, boolean isFirstLevel) {
        final List<MenuOption>   menuItemList = menusMap.get(menu);
        final HtmlList.Unordered subUl        = ul();
        subUl.addStyleName(DROPDOWN_MENU);

        final int itemsGenerated = generateMenuItems(subMenusMap, menuItemList, subUl, false);

        boolean menuAdded = false;

        if (itemsGenerated > 0) {
            final HtmlList.Item li = li();
            ul.add(li);
            final Anchor anchor = anchor(menu.getLabel());
            li.add(anchor);
            li.add(subUl);
            final String styleName;
            if (isFirstLevel) {
                styleName = DROPDOWN;
                final Element element = anchor.getElement();
                element.appendChild(new Icon(CARET_DOWN).getElement());
                element.setAttribute(DATA_TOGGLE, DROPDOWN);
                anchor.addClickHandler(e -> {
                    closeAllMenus();
                    li.addStyleName(OPEN);
                });

                anchor.addDomHandler(event -> handleFirstLevelOver(li, anchor), MouseOverEvent.getType());
            }
            else {
                styleName = DROPDOWN_SUBMENU;
                li.addDomHandler(event -> handleSubMenuPos(li, subUl), MouseOverEvent.getType());
            }
            li.addStyleName(styleName);
            menuAdded = true;
            addKeyHandler(li, true, isFirstLevel, subUl);
        }
        return menuAdded;
    }  // end method generateMenu
    //J+

    private void handleFirstLevelOver(final HtmlList.Item li, final Anchor anchor) {
        final JsArray<Element> all = querySelectorAll(RootPanel.get().getElement(), ".sui-menu > li.open");
        if (all.length() > 0) {
            final Element liOpen = all.get(0);
            liOpen.removeClassName(OPEN);

            li.addStyleName(OPEN);
        }
        anchor.getElement().focus();
    }

    @SuppressWarnings({ "IfStatementWithTooManyBranches", "OverlyComplexMethod" })
    private void handleKey(@NotNull Widget li, boolean isMenu, boolean openDown, @NotNull Widget subUl, KeyDownEvent event) {
        if (event.isDownArrow()) {
            if (isMenu && openDown) {
                li.addStyleName(OPEN);
                focusFirstChild(subUl);
            }
            else focusNextElement(li, true, false);
        }
        else if (event.isRightArrow()) {
            if (isMenu && !openDown) {
                li.addStyleName(OPEN);
                focusFirstChild(subUl);
            }
            else if (!isMenu && isSubItem(li)) moveToParent(li);
            else focusNextElement(li, true, true);
        }
        else if (event.isUpArrow()) {
            if (isMenu && openDown) li.removeStyleName(OPEN);
            else focusNextElement(li, false, false);
        }
        else if (event.isLeftArrow()) {
            // simplify to 2 ifs
            if (isMenu && !openDown) moveToParent(li);
            if (!isMenu) {
                if (isSubItem(li)) moveToParent(li);
                else focusNextElement(li, false, true);
            }
            if (isMenu && openDown) focusNextElement(li, false, true);
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !isMenu) {
            final WidgetCollection children = getChildren();

            for (final Widget child : children)
                child.removeStyleName(OPEN);
            HtmlDomUtils.clickElement(li.getElement().getFirstChildElement());
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) closeAllMenus();
    }

    private void handleSubMenuPos(HtmlList.Item li, HtmlList.Unordered subUl) {
        ((HtmlList.Unordered) li.getParent()).forEach(w -> w.removeStyleName("open"));

        li.addStyleName(OPEN);

        final int leftOver = Window.getClientWidth() - (li.getAbsoluteLeft() + li.getOffsetWidth());
        if (leftOver < subUl.getOffsetWidth()) li.addStyleName(DROPDOWN_OPEN_LEFT);
        else li.removeStyleName(DROPDOWN_OPEN_LEFT);
    }

    private void moveToParent(@NotNull Widget li) {
        final Element parentLi = HtmlDomUtils.findParentElementByTag("LI", li.getElement());
        if (parentLi != null) {
            parentLi.removeClassName(OPEN);
            parentLi.getFirstChildElement().focus();
        }
    }

    private boolean isSubItem(@NotNull Widget li) {
        return li.getParent().getStyleName().contains(DROPDOWN_MENU);
    }

    private Element getNextMenu(@NotNull Widget li, List<Element> menuElements, int size, boolean next) {
        final int current = menuElements.indexOf(HtmlDomUtils.findParentElementByTag("NAV", li.getElement()));
        final int nextIdx = next ? (current == size - 1 ? 0 : current + 1) : ((current <= 0 ? size : current) - 1);

        return next ? menuElements.get(nextIdx).getElementsByTagName("UL").getItem(0).getFirstChildElement()
                    : (Element) menuElements.get(nextIdx).getElementsByTagName("UL").getItem(0).getLastChild();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(TopMenu.class);
}  // end class TopMenu
