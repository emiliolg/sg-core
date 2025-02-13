
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

import tekgenesis.form.Action;
import tekgenesis.form.configuration.UploadConfiguration;

/**
 * User class for Form: UploadForm
 */
public class UploadForm extends UploadFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void cropConfig() {
        final UploadConfiguration up = configuration(Field.UP);
        up.crop(true);
        final UploadConfiguration camera = configuration(Field.CAMERA);
        camera.crop(true);
    }

    @NotNull @Override public Action fillGallery() {
        setShowcase(getUp());
        setGal(getUp());
        return actions.getDefault();
    }

    @NotNull @Override public Action resetMe() {
        final int size = getUp().size();
        reset(Field.UP);
        return actions.getError().withMessage(String.valueOf(size));
    }
}
