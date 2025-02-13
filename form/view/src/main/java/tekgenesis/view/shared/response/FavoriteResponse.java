
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.view.shared.utils.Favorite;

/**
 * Favorite Response.
 */
@SuppressWarnings("FieldMayBeFinal")
public class FavoriteResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private List<Favorite> favorites;

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    public FavoriteResponse() {
        favorites = null;
    }

    /** Data constructor. */
    public FavoriteResponse(@NotNull ImmutableList<Favorite> favs) {
        favorites = new ArrayList<>(favs);
    }

    //~ Methods ......................................................................................................................................

    /** Get Favorites. */
    public List<Favorite> getFavorites() {
        return favorites;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1089659476844957449L;
}
