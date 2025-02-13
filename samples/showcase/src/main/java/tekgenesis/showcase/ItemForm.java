
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.Navigate;
import tekgenesis.showcase.g.ListingBase;

/**
 * Item Form class.
 */
@SuppressWarnings("WeakerAccess")
public class ItemForm extends ItemFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when button($B3) is clicked. */
    @NotNull @Override public Action next() {
        if (getNext() != -1) {
            final ViewData data = getData();
            data.setCurrent(getNext());
            data.update();
            return navigate(actions, data, forms.initialize(ItemForm.class)).leave();
        }
        return actions.getDefault();
    }

    /** Invoked when button($B1) is clicked. */
    @NotNull @Override public Action prev() {
        if (getPrev() != -1) {
            final ViewData data = getData();
            data.setCurrent(getPrev());
            data.update();
            return navigate(actions, data, forms.initialize(ItemForm.class)).leave();
        }
        return actions.getDefault();
    }

    @Override public String toString() {
        return getName();
    }

    private void setNavigation(ViewData data) {
        setData(data);
        final List<Integer> pks   = data.getItems().map(ListingBase::getPk).toList();
        final int           index = pks.indexOf(data.getCurrent());
        final int           next  = pks.get(index + 1 < pks.size() ? index + 1 : 0);
        final int           prev  = pks.get((index - 1 < 0 ? pks.size() : index) - 1);
        if (next != data.getCurrent()) setNext(next);
        if (prev != data.getCurrent()) setPrev(prev);
    }

    //~ Methods ......................................................................................................................................

    /** Return a Navigate action to specific ItemForm id. */
    public static Navigate<ItemForm> navigate(@NotNull Actions actions, @NotNull ViewData data, @NotNull ItemForm form) {
        form.setId(data.getCurrent());
        form.setNavigation(data);
        return actions.navigate(form);
    }
}  // end class ItemForm
