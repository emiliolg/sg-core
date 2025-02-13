
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.LanguageLevelModuleExtension;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.pom.java.LanguageLevel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.facet.MMFrameworkConfigurable;
import tekgenesis.lang.mm.sdk.SuiGenerisSdk;

/**
 * Sui Generis module builder.
 */
public class SuiGenerisModuleBuilder extends JavaModuleBuilder {

    //~ Constructors .................................................................................................................................

    /** Sui Generis module builder. */
    public SuiGenerisModuleBuilder() {
        // Enforce Sui Generis facet.
        addModuleConfigurationUpdater(new ModuleConfigurationUpdater() {
                @Override public void update(@NotNull Module module, @NotNull ModifiableRootModel rootModel) {
                    new MMFrameworkConfigurable().addSupport(module, rootModel, new IdeaModifiableModelsProvider());

                    final LanguageLevelModuleExtension extension = rootModel.getModuleExtension(LanguageLevelModuleExtension.class);

                    if (extension != null) {
                        LanguageLevel level = extension.getLanguageLevel();
                        if (level == null) {
                            final LanguageLevelProjectExtension project = LanguageLevelProjectExtension.getInstance(rootModel.getProject());

                            if (project != null) level = project.getLanguageLevel();
                        }
                        if (level == LanguageLevel.JDK_1_6) extension.setLanguageLevel(LanguageLevel.JDK_1_7);
                    }
                }
            });
    }

    //~ Methods ......................................................................................................................................

    @Override public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep) {
        return StdModuleTypes.JAVA.modifyProjectTypeStep(settingsStep, this);
    }

    @Override public boolean isSuitableSdkType(SdkTypeId sdk) {
        return sdk == SuiGenerisSdk.getInstance();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ModuleType getModuleType() {
        return new SuiGenerisModuleType();
    }
}  // end class SuiGenerisModuleBuilder
