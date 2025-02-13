
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.sdk;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.intellij.ProjectTopics;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.lang.mm.MMLanguage;
import tekgenesis.lang.mm.ui.MMUIInformer;

import static com.intellij.util.containers.ContainerUtil.newConcurrentMap;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.Constants.SOURCES;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.sdk.SuiGenerisSdk.*;

/**
 * Sanitize Sui Generis Sdk Notification Provider.
 */
public class SanitizeSuiGenerisSdkNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> {

    //~ Instance Fields ..............................................................................................................................

    private final Project project;

    private final Map<Sdk, Boolean> sdkSanitationCache = newConcurrentMap();

    //~ Constructors .................................................................................................................................

    /** Constructor for injection. */
    public SanitizeSuiGenerisSdkNotificationProvider(Project project, final EditorNotifications notifications) {
        this.project = project;

        this.project.getMessageBus().connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {
                @Override public void rootsChanged(ModuleRootEvent event) {
                    sdkSanitationCache.clear();
                    notifications.updateAllNotifications();
                }
            });
    }

    //~ Methods ......................................................................................................................................

    @Override public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor) {
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) return null;

        final Language language = psiFile.getLanguage();
        if (language.is(MMLanguage.INSTANCE) || language.isKindOf(JavaLanguage.INSTANCE)) {
            final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);

            if (module != null) {
                final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
                if (sdk != null && isSuiGenerisSDK(sdk) && needsSanitation(sdk)) return createPanel(project, sdk);
            }
        }

        return null;
    }

    @NotNull @Override public Key<EditorNotificationPanel> getKey() {
        return KEY;
    }

    /** Sdk check sanitation fails if stored build differs from sdk home build information. */
    private boolean checkSanitation(@NotNull final Sdk sgSdk) {
        final String path = sgSdk.getHomePath();
        if (path == null) return false;

        final File home = new File(path);
        if (!isValidSgSdk(home)) return false;  // Skip any sanitation for invalid or corrupted sdk-s

        final SuiGenerisSdkData data = (SuiGenerisSdkData) sgSdk.getSdkAdditionalData();
        if (data == null) return false;

        final String build = getSdkVersion(getVersionPropertiesFile(home)).second();
        if (equal(build, data.getSgBuild())) return false;

        final Set<VirtualFile> libs = getMissingLibOrderEntries(sgSdk);
        return !libs.isEmpty();
    }

    private boolean needsSanitation(@NotNull final Sdk sgSdk) {
        if (sdkSanitationCache.containsKey(sgSdk)) return sdkSanitationCache.get(sgSdk);
        final boolean result = checkSanitation(sgSdk);
        sdkSanitationCache.put(sgSdk, result);
        return result;
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static EditorNotificationPanel createPanel(@NotNull final Project project, final Sdk sgSdk) {
        final EditorNotificationPanel panel = new EditorNotificationPanel();
        panel.setText(MSGS.suiGenesisSdkMissingLibraries());
        panel.setToolTipText(MSGS.suiGenesisSdkMissingLibrariesDetails());

        panel.createActionLabel(SANITIZE_SUI_GENERIS_SDK,
            () -> {
                final SdkModificator         modifier = sgSdk.getSdkModificator();
                final Tuple<String, Integer> result   = sanitize(sgSdk, modifier);
                ApplicationManager.getApplication().runWriteAction(modifier::commitChanges);
                if (result.second() != 0) MMUIInformer.showLogMessage(project, MSGS.suiGenesisSdkSanitized(result.first(), result.second()));
            });
        return panel;
    }

    private static Tuple<String, Integer> sanitize(@NotNull Sdk sgSdk, @NotNull SdkModificator modifier) {
        final Set<VirtualFile> toBeAdded = getMissingLibOrderEntries(sgSdk);
        for (final VirtualFile mmLibrary : toBeAdded) {
            modifier.addRoot(mmLibrary, OrderRootType.CLASSES);
            // Attempt to add sources...
            final File sourceJar = new File(sgSdk.getHomePath() + File.separator + SOURCES, mmLibrary.getNameWithoutExtension() + SOURCES_SUFFIX);
            addSources(modifier, sourceJar);
        }

        final SuiGenerisSdkData data = (SuiGenerisSdkData) sgSdk.getSdkAdditionalData();
        final String            home = sgSdk.getHomePath();
        assert home != null && data != null;
        final String build = getSdkVersion(getVersionPropertiesFile(new File(home))).second();
        final Sdk    jdk   = data.getJavaSdk();
        if (jdk != null) {
            final SuiGenerisSdkData updated = new SuiGenerisSdkData(sgSdk, jdk, build);
            modifier.setSdkAdditionalData(updated);
        }
        return Tuple.tuple(build, toBeAdded.size());
    }

    private static Set<VirtualFile> getMissingLibOrderEntries(@NotNull Sdk sdk) {
        final String home = sdk.getHomePath();
        assert home != null;

        final Set<VirtualFile> libs = new HashSet<>();

        final VirtualFile[] libraries = getMetaModelLibraries(home);
        Collections.addAll(libs, libraries);

        for (final VirtualFile entry : sdk.getRootProvider().getFiles(OrderRootType.CLASSES))
            libs.remove(entry);

        return libs;
    }

    //~ Static Fields ................................................................................................................................

    private static final String                       SANITIZE_SUI_GENERIS_SDK = MSGS.sanitizeSuiGenesisSdk();
    private static final Key<EditorNotificationPanel> KEY                      = Key.create(SANITIZE_SUI_GENERIS_SDK);
}  // end class SanitizeSuiGenerisSdkNotificationProvider
