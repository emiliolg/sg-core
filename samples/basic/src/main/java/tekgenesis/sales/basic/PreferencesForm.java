
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

import static tekgenesis.sales.basic.TwitterApi.User.NONE;
import static tekgenesis.sales.basic.TwitterApi.getUser;

/**
 * Preferences Form class.
 */
@SuppressWarnings("WeakerAccess")
public class PreferencesForm extends PreferencesFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action checkUser() {
        if (isDefined(Field.TWITTER)) {
            final TwitterApi.User user = getUser(getTwitter());
            if (user != NONE) setImage(user.getProfileImage());
            else setImage(NO_IMAGE);
        }
        else setImage(NO_IMAGE);

        return actions.getDefault();
    }

    //~ Static Fields ................................................................................................................................

    private static final String NO_IMAGE = "http://cdn1.iconfinder.com/data/icons/UII_Icons/48x48/profile_remove.png";
}
