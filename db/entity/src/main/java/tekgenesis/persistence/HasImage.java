
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * Entity with resource image.
 */
public interface HasImage {

    //~ Methods ......................................................................................................................................

    /** Path to image. */
    @NotNull String imagePath();
}
