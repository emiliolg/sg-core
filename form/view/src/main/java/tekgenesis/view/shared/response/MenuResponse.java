
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.menu.MenuItem;

import static tekgenesis.type.MetaModelKind.*;
import static tekgenesis.view.shared.response.MenuOption.createFormDomainMenuOption;
import static tekgenesis.view.shared.response.MenuOption.createMenuOption;

/**
 * Map with all the menus by domain. Used to populate the menus apps tree.
 */
@SuppressWarnings("FieldMayBeFinal")
public class MenuResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private Map<MenuOption, List<MenuOption>> menuItemsMap;
    private Map<MenuOption, List<MenuOption>> subMenuItemsMap;

    //~ Constructors .................................................................................................................................

    MenuResponse() {
        menuItemsMap    = new LinkedHashMap<>();
        subMenuItemsMap = new LinkedHashMap<>();
    }

    private MenuResponse(final Map<MenuOption, List<MenuOption>> menuItems, final Map<MenuOption, List<MenuOption>> subMenuItemsMap) {
        menuItemsMap         = menuItems;
        this.subMenuItemsMap = subMenuItemsMap;
    }

    //~ Methods ......................................................................................................................................

    /** Returns map of domain-forms. */
    public Map<MenuOption, List<MenuOption>> getMenus() {
        return menuItemsMap;
    }

    /** Returns map of domain-forms. */
    public Map<MenuOption, List<MenuOption>> getSubMenus() {
        return subMenuItemsMap;
    }

    //~ Methods ......................................................................................................................................

    /** Creates the response from a list of Menus. */
    public static MenuResponse create(@NotNull final Iterable<Menu> menus, @Nullable final Iterable<Form> forms,
                                      @NotNull final Seq<Menu> allDefinedMenus, @NotNull Function<Menu, Menu> permissionFunction,
                                      @NotNull Function<Menu, Menu> localizedFunction) {
        final Map<MenuOption, List<MenuOption>> menusItems    = new TreeMap<>();
        final Map<MenuOption, List<MenuOption>> subMenusItems = new TreeMap<>();

        for (final Menu menu : menus) {
            final Menu allowedMenu = permissionFunction.apply(menu);

            if (!allowedMenu.getChildren().isEmpty()) {
                final Menu localizedMenu = localizedFunction.apply(allowedMenu);

                final MenuOption menuNode = createMenuOption(localizedMenu);

                if (subMenusItems.containsKey(menuNode)) menusItems.remove(menuNode);
                else {
                    final List<MenuOption> items = new ArrayList<>();
                    menusItems.put(menuNode, items);
                    generateMenuItems(allDefinedMenus, menusItems, subMenusItems, localizedMenu, items, permissionFunction, localizedFunction);
                }
            }
        }

        // If there are no menus defined ... use the old way
        if (menusItems.isEmpty() && forms != null) {
            for (final Form form : forms) {
                final MenuOption domainMenu = createFormDomainMenuOption(form);

                final List<MenuOption> domain = menusItems.computeIfAbsent(domainMenu, k -> new ArrayList<>());

                final MenuOption menuItem = createMenuOption(form);

                domain.add(menuItem);

                Collections.sort(domain);
            }
        }

        return new MenuResponse(menusItems, subMenusItems);
    }  // end method create

    private static void generateMenuItems(@NotNull final Seq<Menu> allDefinedMenus, @NotNull Map<MenuOption, List<MenuOption>> menusItems,
                                          @NotNull Map<MenuOption, List<MenuOption>> subMenusItems, @NotNull Menu menu,
                                          @NotNull List<MenuOption> items, @NotNull Function<Menu, Menu> permissionFunction,
                                          @NotNull Function<Menu, Menu> localizedFunction) {
        for (final MenuItem item : menu.getChildren()) {
            final MenuOption menuItem = MenuOption.createMenuItemOption(menu.getDomain(), item);
            items.add(menuItem);

            if (menuItem.getType() == MENU) {
                final String target = menuItem.getTarget();
                generateSubMenu(allDefinedMenus, target, menusItems, subMenusItems, permissionFunction, localizedFunction);
            }
        }
    }

    private static void generateSubMenu(@NotNull final Seq<Menu> allDefinedMenus, @NotNull final String menuItemTarget,
                                        @NotNull Map<MenuOption, List<MenuOption>> menusItems,
                                        @NotNull Map<MenuOption, List<MenuOption>> subMenusItems, @NotNull Function<Menu, Menu> permissionFunction,
                                        @NotNull Function<Menu, Menu> localizedFunction) {
        final Menu menu = allDefinedMenus  //
                          .getFirst(metaModel -> metaModel != null && metaModel.getFullName().equals(menuItemTarget))
                          .getOrFail("Menu Not Found '" + menuItemTarget + "'");

        final Menu allowedMenu = permissionFunction.apply(menu);

        if (allowedMenu.getChildren().isEmpty()) return;

        final Menu localizedMenu = localizedFunction.apply(allowedMenu);

        final MenuOption menuNode = createMenuOption(localizedMenu);

        menusItems.remove(menuNode);

        final List<MenuOption> items = new ArrayList<>();
        subMenusItems.put(menuNode, items);

        generateMenuItems(allDefinedMenus, menusItems, subMenusItems, localizedMenu, items, permissionFunction, localizedFunction);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8908259889817922881L;
}  // end class MenuResponse
