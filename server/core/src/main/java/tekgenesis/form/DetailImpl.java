
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

/**
 * Form navigate action.
 */
@SuppressWarnings("ParameterHidesMemberVariable")
public class DetailImpl<T extends FormInstance<?>> extends ActionImpl implements Detail {

    //~ Instance Fields ..............................................................................................................................

    private final T form;

    private boolean fullscreen;
    private int     height;
    private int     marginTop;
    private int     width;

    //~ Constructors .................................................................................................................................

    private DetailImpl(T form) {
        super(ActionType.DETAIL);
        this.form = form;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Detail dimension(int width, int height) {
        this.width  = width;
        this.height = height;
        return this;
    }

    @NotNull @Override public Detail fullscreen() {
        fullscreen = true;
        return this;
    }

    @NotNull @Override public Detail marginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    /** Returns true if dimension is set by the user. */
    public boolean isDimensionDefined() {
        return width != 0 && height != 0;
    }

    /** Returns this detail form. */
    public T getForm() {
        return form;
    }

    /** Returns popup panels height. */
    public int getHeight() {
        return height;
    }

    /** Get margin top. */
    public int getMarginTop() {
        return marginTop;
    }

    /** Returns true if popup panels must be displayed in full screen mode. */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /** Returns popup panels width. */
    public int getWidth() {
        return width;
    }

    //~ Methods ......................................................................................................................................

    /** Details of a FormInstance. */
    public static <T extends FormInstance<?>> Detail detail(T form) {
        return new DetailImpl<>(form);
    }
}  // end class DetailImpl
