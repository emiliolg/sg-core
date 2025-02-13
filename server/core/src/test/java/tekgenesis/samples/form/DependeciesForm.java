
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.FormTable;

/**
 * User class for Form: DependeciesForm
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class DependeciesForm extends DependeciesFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when populating a form instance. */
    @NotNull @Override public Object populate() {
        final int                        product = indexOf(products, getProduct());
        final FormTable<DependenciesRow> table   = getDependencies();
        table.clear();
        for (int dependency = 0; dependency < dependencies[product].length; dependency++) {
            if (dependencies[product][dependency]) table.add().setDependency(products[dependency]);
        }
        return getProduct();
    }

    private int indexOf(String[] ps, String p) {
        for (int i = 0; i < ps.length; i++)
            if (p.equals(ps[i])) return i;
        return -1;
    }

    //~ Methods ......................................................................................................................................

    public static int dependenciesCount(boolean[] deps) {
        int result = 0;
        for (final boolean dep : deps)
            result += dep ? 1 : 0;
        return result;
    }

    //~ Static Fields ................................................................................................................................

    public static final String[] products = { "runtime", "server", "tasks", "metadata", "core" };

    public static final boolean[][] dependencies = {
        { false, true, true, true, true },
        { false, false, false, true, true },
        { false, false, false, true, true },
        { false, false, false, false, true },
        { false, false, false, false, false }
    };

    //~ Inner Classes ................................................................................................................................

    public class DependenciesRow extends DependenciesRowBase {}
}  // end class DependeciesForm
