
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

import tekgenesis.common.core.Resource;
import tekgenesis.showcase.g.ImageBase;

/**
 * User class for Model: Image
 */
public class Image extends ImageBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Resource getSec() {
        return getResource();
    }

    @NotNull @Override public Image setSec(@NotNull Resource sec) {
        return this;
    }
}
