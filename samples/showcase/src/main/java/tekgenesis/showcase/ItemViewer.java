
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

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.NamedItemTable.NAMED_ITEM;

/**
 * Item viewer form class.
 */
@SuppressWarnings("WeakerAccess")
public class ItemViewer extends ItemViewerBase {

    //~ Methods ......................................................................................................................................

    @Override public void copyTo(@NotNull ViewData data) {
        data.getItems().merge(getItems(), (i, r) -> i.setPk(r.getItem()));
    }

    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    public Action create() {
        final ViewData viewData = ViewData.create();
        viewData.insert();
        copyTo(viewData);
        viewData.update();
        setId(viewData.getId());
        return actions.getDefault();
    }

    @NotNull @Override public Action refresh() {
        final FormTable<ItemsRow> items = getItems();
        items.clear();
        selectFrom(NAMED_ITEM).where(NAMED_ITEM.COLOR.in(getColors())).forEach(item -> {
            final ItemsRow row = items.add();
            row.setItem(item.getIdKey());
            row.setName(item.getName());
            row.setColor(item.getColor());
        });
        updateDataItems();
        return actions.getDefault();
    }

    private void updateDataItems() {
        if (isDefined(Field.ID)) {
            final ViewData data = find();
            copyTo(data);
            data.update();
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class ItemsRow extends ItemsRowBase {
        @NotNull @Override public Action navigate() {
            if (!ItemViewer.this.isDefined(Field.ID)) create();
            final ViewData data = find();
            data.setCurrent(getItems().getCurrent().getItem());
            data.update();
            System.out.println("Navigate with data: " + data);
            return ItemForm.navigate(actions, data, forms.initialize(ItemForm.class));
        }
    }
}  // end class ItemViewer
