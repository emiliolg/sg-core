
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;

/**
 * Collects the data to build a {@link Menu}.
 */
public class MenuBuilder extends ModelBuilder.Default<Menu, MenuBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Map<String, MenuItemBuilder> items;

    //~ Constructors .................................................................................................................................

    private MenuBuilder(@NotNull String src, @NotNull String pkg, @NotNull String name) {
        super(src, pkg, name);
        items = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Add a menu item to the builder. */
    public final void addMenuItem(@NotNull MenuItemBuilder builder)
        throws DuplicateFieldException
    {
        if (items.containsKey(builder.getName())) throw DuplicateFieldException.onMenu(builder.getName(), id);
        items.put(builder.getName(), builder);
    }

    @Override public Menu build()
        throws BuilderException
    {
        final List<MenuItem> menuItemMap = new ArrayList<>();
        final Menu           menu        = new Menu(sourceName, domain, id, menuItemMap, label);

        // Process MenuItems
        for (final MenuItemBuilder itemBuilder : items.values()) {
            final MenuItem menuItem = itemBuilder.build(menu);
            menuItemMap.add(menuItem);
        }

        return menu;
    }

    @NotNull @Override public List<BuilderError> check() {
        return Colls.emptyList();
    }

    /** Builds a MenuItem builder. */
    public MenuItemBuilder menuItem() {
        return new MenuItemBuilder();
    }

    //~ Methods ......................................................................................................................................

    /** Creates a {@link MenuBuilder}. */
    public static MenuBuilder create(String sourceName, final String packageId, final String caseName) {
        return new MenuBuilder(sourceName, packageId, caseName);
    }
}  // end class MenuBuilder
