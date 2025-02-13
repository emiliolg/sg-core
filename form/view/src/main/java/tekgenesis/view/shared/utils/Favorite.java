
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.utils;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * Favorite.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Favorite implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String formLink;
    private String label;

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    public Favorite() {
        label    = "";
        formLink = "";
    }

    /** Data constructor. */
    public Favorite(@NotNull String label, @NotNull String formLink) {
        this.label    = label;
        this.formLink = formLink;
    }

    //~ Methods ......................................................................................................................................

    /** Favorite Link. */
    public String getFormLink() {
        return formLink;
    }

    /** Favorite Label. */
    public String getLabel() {
        return label;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3890766554076831144L;
}
