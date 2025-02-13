
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.settings;

import java.awt.*;
import java.net.URL;

import javax.swing.*;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMFileType;

/**
 * MMApplication Settings.
 */
@State(name     = "MMApplicationSettingManager", storages = @Storage(id = "MM_app", file = "$APP_CONFIG$/MM_app.xml"))
public class MMApplicationSettingManager implements PersistentStateComponent<MMApplicationSettingManager>, ApplicationComponent {

    //~ Instance Fields ..............................................................................................................................

    private String mmIconPath = MMSettings.GRAY_ICONPATH;

    //~ Methods ......................................................................................................................................

    @Override public void disposeComponent() {}

    @Override
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void initComponent() {
        final URL resource = getClass().getClassLoader().getResource(getInstance().getMmIconPath());
        if (resource != null) {
            final ImageIcon imageIcon   = new ImageIcon(resource);
            final Image     smallImage  = imageIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_AREA_AVERAGING);
            MMFileType.ENTITY_FILE_ICON       = imageIcon;
            MMFileType.ENTITY_FILE_ICON_SMALL = new ImageIcon(smallImage);
        }
    }

    @Override public void loadState(MMApplicationSettingManager mmApplicationSettings) {
        XmlSerializerUtil.copyBean(mmApplicationSettings, getInstance());
    }

    @NotNull @Override public String getComponentName() {
        return "Application Settings";
    }

    /** get MMIcon Path. */
    public String getMmIconPath() {
        return mmIconPath;
    }

    /** set MMIcon Path. */
    public void setMmIconPath(String mmIconPath) {
        this.mmIconPath = mmIconPath;
    }

    @Nullable @Override public MMApplicationSettingManager getState() {
        return getInstance();
    }

    //~ Methods ......................................................................................................................................

    /** Get instance of KeyManager. */
    public static synchronized MMApplicationSettingManager getInstance() {
        if (instance == null) instance = new MMApplicationSettingManager();
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static final int WIDTH  = 18;
    private static final int HEIGHT = 19;

    private static MMApplicationSettingManager instance = null;
}  // end class MMApplicationSettingManager
