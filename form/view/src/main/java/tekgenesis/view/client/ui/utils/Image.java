
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.utils;

/**
 * Image data.
 */
public class Image {

    //~ Instance Fields ..............................................................................................................................

    private final int    height;
    private final String src;
    private final int    width;

    //~ Constructors .................................................................................................................................

    /** Image data. */
    public Image(int width, int height, String src) {
        this.width  = width;
        this.height = height;
        this.src    = src;
    }

    //~ Methods ......................................................................................................................................

    /** Returns img height. */
    public int getHeight() {
        return height;
    }

    /** Returns img src. */
    public String getSrc() {
        return src;
    }

    /** Returns img width. */
    public int getWidth() {
        return width;
    }
}
