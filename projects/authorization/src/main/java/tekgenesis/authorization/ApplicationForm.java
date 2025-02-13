
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.UUID;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: ApplicationForm
 */
public class ApplicationForm extends ApplicationFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void copyTo(@NotNull Application app) {
        super.copyTo(app);
        app.setUser(getUser());
    }

    /** Invoked when creating a form instance. */
    @NotNull @Override
    @SuppressWarnings("MagicNumber")
    public Action create() {
        final Application oldUser = Application.find(getId());
        if (oldUser != null)  // noinspection DuplicateStringLiteralInspection
            return actions.getError().withMessage("Application id '" + getId() + "' already exists.");
        else
        {
            final UUID   uuid  = UUID.randomUUID();
            final String token = new Sha256Hash(uuid.toString(), ByteSource.Util.bytes(getId() + getUserKey()), 512).toBase64();

            setToken(token);

            return super.create();
        }
    }

    /** Invoked when populating a form instance. */
    @NotNull public Application populate() {
        final Application application = super.populate();
        setUser(application.getUser());
        return application;
    }
}
