
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

/**
 * Refresh lens event.
 */
public class ItemEvent implements LensEvent {

    //~ Instance Fields ..............................................................................................................................

    private final int           item;
    private final ItemEventType type;

    //~ Constructors .................................................................................................................................

    /** Creates a Refresh Lens event. */
    public ItemEvent(ItemEventType t, int i) {
        type = t;
        item = i;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean refresh() {
        return true;
    }

    public int getItem() {
        return item;
    }

    public ItemEventType getType() {
        return type;
    }

    //~ Enums ........................................................................................................................................

    public enum ItemEventType { ITEM_CREATED, ITEM_DELETED }
}
