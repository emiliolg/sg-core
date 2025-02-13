
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.sdk;

import com.intellij.ProjectTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMLanguage;
import tekgenesis.lang.mm.ProjectUtils;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.sdk.SuiGenerisSdk.isSuiGenerisSDK;

/**
 * Setup Sui Generis Sdk Notification Provider.
 */
public class SetupSuiGenerisSdkNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> {

    //~ Instance Fields ..............................................................................................................................

    private final Project project;

    //~ Constructors .................................................................................................................................

    /** Constructor for injection. */
    public SetupSuiGenerisSdkNotificationProvider(Project project, final EditorNotifications notifications) {
        this.project = project;
        this.project.getMessageBus().connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {
                @Override public void rootsChanged(ModuleRootEvent event) {
                    notifications.updateAllNotifications();
                }
            });
    }

    //~ Methods ......................................................................................................................................

    @Override public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor) {
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) return null;

        if (psiFile.getLanguage() != MMLanguage.INSTANCE) return null;

        final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
        if (module == null) return null;

        final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
        if (sdk != null && isSuiGenerisSDK(sdk)) return null;

        if (ProjectUtils.isSuiGenerisProject(module.getProject())) return null;

        return createPanel(project, module, sdk == null);
    }

    @NotNull @Override public Key<EditorNotificationPanel> getKey() {
        return KEY;
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static EditorNotificationPanel createPanel(@NotNull final Project project, @NotNull final Module module, final boolean noSDK) {
        final EditorNotificationPanel panel = new EditorNotificationPanel();
        panel.setText(noSDK ? MSGS.suiGenesisSdkNotDefined() : MSGS.invalidSuiGenesisSdk());
        panel.setToolTipText(MSGS.invalidSuiGenesisSdkDefinedDetails());

        panel.createActionLabel(SETUP_SUI_GENERIS_SDK,
            () -> {
                ProjectSettingsService.getInstance(project).chooseAndSetSdk();
                ApplicationManager.getApplication().runWriteAction(() -> ModuleRootModificationUtil.setSdkInherited(module));
            });
        return panel;
    }

    //~ Static Fields ................................................................................................................................

    private static final String                       SETUP_SUI_GENERIS_SDK = MSGS.setupSuiGenesisSdk();
    private static final Key<EditorNotificationPanel> KEY                   = Key.create(SETUP_SUI_GENERIS_SDK);
}  // end class SetupSuiGenerisSdkNotificationProvider
