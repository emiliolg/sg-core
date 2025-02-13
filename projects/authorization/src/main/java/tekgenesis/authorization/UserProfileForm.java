
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.authorization.shiro.SuiGenerisAuthorizingRealm;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.security.Principal;
import tekgenesis.form.Action;
import tekgenesis.form.configuration.UploadConfiguration;
import tekgenesis.model.KeyMap;
import tekgenesis.persistence.InnerEntitySeq;

import static tekgenesis.authorization.User.hashPassword;
import static tekgenesis.authorization.UserForm.getLanguages;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getPasswordStrengthScore;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.env.security.SecurityUtils.getSession;

/**
 * User class for Form: UserProfileForm
 */

public class UserProfileForm extends UserProfileFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action create() {
        final User user = User.find(getId());
        if (user == null || !user.getId().equals(context.getUser().getId())) throw new IllegalStateException("Trying to change a different user");

        final String locale    = getLocale();
        final String localeStr = locale == null ? null : locale.toLowerCase();
        user.setLocale(localeStr);
        final Principal principal     = getSession().getPrincipal();
        final boolean   localeChanged = principal.getLocale() == null || !principal.getLocale().toString().equals(localeStr);
        if (localeChanged) principal.setLocale(locale == null ? null : new java.util.Locale(localeStr));

        user.setName(getName());
        user.setEmail(getEmail());
        user.setPicture(getPicture());
        // hash pass
        user.setPassword(isDefined(Field.PASSWORD) ? hashPassword(getPassword(), getId()) : getHashPass());
        user.update();

        // reload page on locale change
        return localeChanged ? actions.redirect("/") : actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setPasswordScore(getPasswordStrengthScore(3));

        if (!isDefined(Field.DEFAULT_OU)) {
            final OrgUnit ou = OrgUnit.find(SuiGenerisAuthorizingRealm.ROOT_OU);
            if (ou != null) setDefaultOu(ou);
        }
        final UploadConfiguration configuration = configuration(Field.PICTURE);
        configuration.crop(true);
        configuration.ratio(1, 1);
        configuration.minSize(MIN_SIZE, MIN_SIZE);

        setEnablePass(Context.getEnvironment().get(ShiroProps.class).builtInAuthentication);
        final User user = cast(context.getUser());
        setId(user.getId());
        setName(user.getName());
        setEmail(user.getEmail());
        setPicture(user.getPicture());
        setHashPass(user.getPassword());

        final InnerEntitySeq<Device> devices = user.getDevices();
        for (final Device device : devices)
            getDevices().add().populate(device);

        final String locale    = user.getLocale();
        final KeyMap languages = getLanguages();
        if (locale != null) {
            if (!languages.containsKey(locale)) languages.put(locale, locale);
            setLocaleOptions(languages);
            setLocale(locale);
        }
        else setLocaleOptions(languages);
    }

    //~ Static Fields ................................................................................................................................

    private static final int MIN_SIZE = 150;

    //~ Inner Classes ................................................................................................................................

    public class DevicesRow extends DevicesRowBase {
        void populate(Device device) {
            setDeviceId(device.getDeviceId());
            setDeviceName(device.getName());
            setDisabled(device.isDisabled());
        }
    }
}  // end class UserProfileForm
