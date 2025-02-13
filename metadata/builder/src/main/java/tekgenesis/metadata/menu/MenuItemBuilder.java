
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.menu;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.FieldBuilder;
import tekgenesis.metadata.exception.BuilderException;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.field.FieldOption.ID;

/**
 * Collects the data to build a {@link Menu}.
 */
public class MenuItemBuilder extends FieldBuilder<MenuItemBuilder> {

    //~ Constructors .................................................................................................................................

    /** Menu item constructor. */
    MenuItemBuilder() {}

    //~ Methods ......................................................................................................................................

    /** Build a Menu Item. */
    public MenuItem build(@NotNull Menu model) {
        return new MenuItem(model, getOptions());
    }

    /** Return true if builder has defined id. */
    public boolean hasId() {
        return isNotEmpty(getOptions().getString(ID));
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException {}

    /** Make it package visible. */
    @Override protected String getName() {
        return super.getName();
    }
}  // end class MenuItemBuilder
