
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.storage.client.Storage;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.option;
import static tekgenesis.view.client.SuiGenerisClientApi.user;

/**
 * Class that manages interactions with local storage for a user.
 */
public class FormStorage {

    //~ Constructors .................................................................................................................................

    /** Creates a Form Storage for a given user. */
    private FormStorage() {}

    //~ Methods ......................................................................................................................................

    /**
     * Returns the value associated with a given key or 'null' if there is no storage or value
     * associated with that key.
     */
    public String get(String key) {
        return getLocalStorage().getItem(key(key));
    }

    /** Removes the value associated with a given key. */
    public void remove(String key) {
        getLocalStorage().removeItem(key(key));
    }

    /** Sets a value for a given key. */
    public void set(String key, String value) {
        getLocalStorage().setItem(key(key), value);
    }

    private String key(String key) {
        return SUIGEN_KEY_PREFIX + KEY_SEPARATOR + user() + KEY_SEPARATOR + key;
    }

    private Storage getLocalStorage() {
        return Storage.getLocalStorageIfSupported();
    }

    //~ Methods ......................................................................................................................................

    /** Returns the form storage instance. */
    public static Option<FormStorage> getInstance() {
        if (INSTANCE == null && Storage.isLocalStorageSupported()) INSTANCE = new FormStorage();
        return option(INSTANCE);
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SUIGEN_KEY_PREFIX = "suigeneris";
    private static final String KEY_SEPARATOR     = "/";

    private static FormStorage INSTANCE = null;
}  // end class FormStorage
