
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

/**
 * User class for Form: TableLocalGlobalOptionsShowcase
 */
public class TableLocalGlobalOptionsShowcase extends TableLocalGlobalOptionsShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeOptions() {
        setCOptions(EnumSet.of(Type.C, Type.D, Type.A));
        setB1Options(EnumSet.of(Type.B, Type.D));

        final FormTable<TableRow> table = getTable();

        for (int i = 0; i < 10; i += 3) {
            final TableRow row = table.get(i);
            row.setDOptions(EnumSet.of(Type.E, Type.F, Type.B));
            row.setComment("Updated options for row");
        }

        return actions().getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setOneOptionComboOptions(Colls.listOf(1));
        final ImmutableList<Integer> intsList = ImmutableList.of(1, 2, 3);
        setThreeOptionsOptions(intsList);

        setCOptions(EnumSet.of(Type.B, Type.C));

        final FormTable<TableRow> table = getTable();
        setComboOptions(EnumSet.of(Type.A, Type.B, Type.C));
        setTagsComboOptions(ImmutableList.of(Type.A, Type.B, Type.C));
        setIntCombosOptions(intsList);
        setIntCombosOptionalOptions(intsList);
        final ImmutableList<String> strings = ImmutableList.of("One", "Two", "Three");
        setStringCombosOptions(strings);
        setStringOptionalOptions(strings);
        setStringRequiredOptions(strings);

        setIntCombos(1);
        setIntCombosOptional(1);
        setStringCombos("Two");

        for (int i = 0; i < 10; i++) {
            final TableRow row = table.add();
            row.setA("Row " + i);
            if (i % 3 == 0) {
                row.setDOptions(EnumSet.of(Type.D, Type.E));
                row.setComment("Custom options for row");
            }
        }
    }

    @NotNull @Override public Action resetCombos() {
        reset(Field.THREE_OPTIONS, Field.ONE_OPTION_COMBO);
        return actions().getDefault();
    }

    @NotNull @Override public Action setE() {
        setCombo(Type.E);
        return actions().getDefault();
    }

    @NotNull @Override public Action setTagsE() {
        setTagsCombo(Colls.listOf(Type.E));
        return actions().getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        @NotNull @Override public Action setAtoB() {
            setB1(Type.A);
            return actions.getDefault();
        }
    }
}  // end class TableLocalGlobalOptionsShowcase
