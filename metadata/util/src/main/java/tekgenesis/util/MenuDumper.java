
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.common.IndentedWriter;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.menu.MenuItem;
import tekgenesis.repository.ModelRepository;

/**
 * A visitor to generate the Database Schema.
 */
final class MenuDumper extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    private final Menu menu;

    //~ Constructors .................................................................................................................................

    MenuDumper(Menu menu, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(menu, repository, writer, preferences);
        this.menu = menu;
    }

    //~ Methods ......................................................................................................................................

    @Override ModelDumper dump() {
        beginModel();
        newLine();
        dumpMenuItems();
        return this;
    }

    private void dumpMenuItems() {
        computeLengths(menu.getChildren());

        print("{").newLine().indent();
        for (final MenuItem menuItem : menu.getChildren()) {
            printNameAndLabel(menuItem);
            final String target = menuItem.getTarget();
            print(target).semicolon().newLine();
        }
        unIndent().newLine().print("}");
    }
}  // end class MenuDumper
