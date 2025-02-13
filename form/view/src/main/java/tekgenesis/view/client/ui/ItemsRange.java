
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

/**
 * Table's items range.
 */
public class ItemsRange {

    //~ Instance Fields ..............................................................................................................................

    private final boolean fire;

    private final int length;
    private final int start;

    //~ Constructors .................................................................................................................................

    /** Creates an ItemsRange from the given start with the given length. */
    public ItemsRange(int start, int length) {
        this(start, length, true);
    }

    public ItemsRange(int start, int length, boolean fire) {
        this.start  = start;
        this.length = length;
        this.fire   = fire;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if an item is contained in this range. */
    public boolean contains(int item) {
        return start <= item && item < (start + length);
    }

    public boolean isFire() {
        return fire;
    }

    /**
     * Get the length of the range.
     *
     * @return  the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the start index of the range.
     *
     * @return  the start index
     */
    public int getStart() {
        return start;
    }

    /** Returns true if this range is empty. */
    public boolean isEmpty() {
        return length == 0;
    }

    //~ Static Fields ................................................................................................................................

    static final ItemsRange EMPTY_RANGE = new ItemsRange(0, 0);
}  // end class ItemsRange
