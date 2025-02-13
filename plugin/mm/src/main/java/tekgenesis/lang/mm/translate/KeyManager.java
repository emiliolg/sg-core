
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.jetbrains.annotations.NotNull;

/**
 * Manager for keys used in translation.
 */
@State(name     = "KeyManager", storages = @Storage(id = "key_manager", file = "$APP_CONFIG$/key_manager.xml"))
public class KeyManager implements PersistentStateComponent<KeyManager>, ApplicationComponent {

    //~ Instance Fields ..............................................................................................................................

    private String clientID     = "";
    private String clientSecret = "";

    //~ Methods ......................................................................................................................................

    @Override public void disposeComponent() {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override public void initComponent() {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override public void loadState(KeyManager keyManager) {
        XmlSerializerUtil.copyBean(keyManager, getInstance());
    }
    /** Checks if the passed ClientId And ClientSecret are Valid. */
    public boolean validKeys(String testClientId, String testClientSecret) {
        Translate.setClientId(testClientId);
        Translate.setClientSecret(testClientSecret);
        final boolean valid = validKeys();
        Translate.setClientId(clientID);
        Translate.setClientSecret(clientSecret);
        return valid;
    }
    /** Gets ClientId. */
    public String getClientID() {
        return clientID;
    }
    /** Sets ClientId. */
    public void setClientID(String clientID) {
        this.clientID = clientID;
        Translate.setClientId(clientID);
    }

    /** Gets ClientSecret. */
    public String getClientSecret() {
        return clientSecret;
    }
    /** Sets ClientSecret. */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        Translate.setClientSecret(clientSecret);
    }

    @NotNull @Override public String getComponentName() {
        return "Key Manager";
    }

    public KeyManager getState() {
        return getInstance();
    }

    /** Checks if current set keys are valid. */
    boolean validKeys() {
        try {
            Translate.execute("Hola", Language.SPANISH, Language.ENGLISH);
        }
        catch (final Exception e) {
            return false;
        }
        return true;
    }

    //~ Methods ......................................................................................................................................

    /** Get instance of KeyManager. */
    public static synchronized KeyManager getInstance() {
        if (instance == null) instance = new KeyManager();
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static KeyManager instance = null;
}  // end class KeyManager
