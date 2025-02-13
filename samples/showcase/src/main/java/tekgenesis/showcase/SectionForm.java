
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

/**
 * User class for Form: SectionForm
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SectionForm extends SectionFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clear() {
        getRooms().clear();
        return actions.getDefault();
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public void load() {
        addRooms(getRooms(), 5);

        loadPath();

        final FormTable<CellsRow> cells = getCells();
        for (int i = 1; i < 26; i++) {
            createCell(cells.add(), i);
            final ScrollRow scrollRow = getScroll().add();
            scrollRow.setImg("http://cdn-images.deezer.com/images/cover/d343dd825aad41f5aba14ede29c8ce52/120x120-000000-80-0-0.jpg");
        }

        for (int i = 0; i < 6; i++) {
            final SomeRow row = getSome().add();
            row.setColImg("http://www.sans.org/images/design/custom/icons/misc/medium/award-ribbon.png");
            row.setDisplayText("col 4 #" + i);
        }

        for (int i = 0; i < 5; i++) {
            final SecRow row = getSec().add();
            row.setLab("Label number " + i);
            if (i == 2) row.setWidth(40);
            else row.setWidth(15);
        }
    }

    @NotNull @Override public Action more() {
        addRooms(getRooms(), 5);
        return actions.getDefault();
    }

    @NotNull @Override public Action removeFirstRoom() {
        getRooms().remove(0);
        return actions.getDefault();
    }

    private void addRooms(FormTable<RoomsRow> rooms, int amount) {
        final int base = rooms.size();
        for (int i = base; i < base + amount; i++)
            rooms.add();
    }

    private void createCell(CellsRow cell, int i) {
        cell.setStyle(i % 5 != 0 ? "pull-left" : "");
        cell.setValue((i < 10 ? "0" : "") + i);
    }

    private void loadPath() {
        final FormTable<PathRow> path = getPath();

        final PathRow item0 = path.add();
        item0.setFqn("suigeneris");
        item0.setDisplay("Suigeneris");

        final PathRow item1 = path.add();
        item1.setFqn("suigeneris.data");
        item1.setDisplay("Data");

        final PathRow item2 = path.add();
        item2.setFqn("suigeneris.data.default");
        item2.setDisplay("Default");

        final PathRow item3 = path.add();
        item3.setFqn("suigeneris.data.default.data");
        item3.setDisplay("Data");
    }

    //~ Inner Classes ................................................................................................................................

    public class CellsRow extends CellsRowBase {}

    public class PathRow extends PathRowBase {
        @NotNull @Override public Action link() {
            return actions.getDefault().withMessage("SectionForm.link path = " + getFqn());
        }
    }

    public class RoomsRow extends RoomsRowBase {
        @NotNull @Override public Action changed() {
            return actions.getDefault().withMessage("SectionForm.changed current = " + getRooms().indexOf(this));
        }
    }

    public class ScrollRow extends ScrollRowBase {}

    public class SecRow extends SecRowBase {}

    public class SomeRow extends SomeRowBase {}
}  // end class SectionForm
