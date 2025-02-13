
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.properties;

import java.nio.charset.Charset;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

import static tekgenesis.common.core.Constants.DEFAULT_PROPERTIES_ENCODING;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Provides notifications if properties file is not in the default properties encoding (yellow
 * banner in editor).
 */
public class PropertiesFileEncodingNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public EditorNotificationPanel createNotificationPanel(@NotNull final VirtualFile file,
                                                                               @NotNull final FileEditor  fileEditor) {
        if (!file.getFileType().getDefaultExtension().equals(PROPERTIES_EXTENSION)) return null;

        final Charset encoding = file.getCharset();
        if (DEFAULT_ENCODING == encoding) return null;

        if (EncodingManager.getInstance().getDefaultCharsetForPropertiesFiles(file) == DEFAULT_ENCODING) return createPanel(file, false);

        return createPanel(file, true);
    }

    @NotNull @Override public Key<EditorNotificationPanel> getKey() {
        return KEY;
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static EditorNotificationPanel createPanel(@NotNull final VirtualFile file, boolean withAction) {
        final EditorNotificationPanel panel    = new EditorNotificationPanel();
        final String                  encoding = DEFAULT_ENCODING.toString();
        panel.setText(MSGS.wrongPropertiesEncoding(encoding));
        panel.setToolTipText(MSGS.wrongPropertiesEncodingDetails(encoding));

        if (withAction) panel.createActionLabel(MSGS.setCorrectDefaultEncoding(encoding), () ->
                getApplication().runWriteAction(() -> {
                    EncodingManager.getInstance().setDefaultCharsetForPropertiesFiles(file, DEFAULT_ENCODING);EditorNotifications.updateAll();}));
        return panel;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String  PROPERTIES_EXTENSION = "properties";
    public static final Charset DEFAULT_ENCODING     = Charset.forName(DEFAULT_PROPERTIES_ENCODING);

    private static final Key<EditorNotificationPanel> KEY = Key.create(MSGS.setCorrectDefaultEncoding(DEFAULT_ENCODING.toString()));
}  // end class PropertiesFileEncodingNotificationProvider
