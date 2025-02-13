
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: TableImageForm
 */
@Generated(value = "tekgenesis/showcase/ImageShowcase.mm", date = "1362743055820")
public class TableImageForm extends TableImageFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clear() {
        setImg(null);
        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class ImgsRow extends ImgsRowBase {}
}
