
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.module;

import javax.swing.Icon;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeId;
import com.intellij.openapi.projectRoots.Sdk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.sdk.SuiGenerisSdk;

/**
 * Virtual Sui Generis module for project configuration.
 */
public class SuiGenerisModuleType extends ModuleType<SuiGenerisModuleBuilder> {

    //~ Constructors .................................................................................................................................

    /** Virtual type for configuration constructor. */
    public SuiGenerisModuleType() {
        super(ModuleTypeId.JAVA_MODULE);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public SuiGenerisModuleBuilder createModuleBuilder() {
        return new SuiGenerisModuleBuilder();
    }

    @Nullable @Override public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep        settingsStep,
                                                                      @NotNull final ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, moduleBuilder::isSuitableSdkType);
    }

    @Override public Icon getBigIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }

    @NotNull @Override public String getDescription() {
        return Constants.SUI_GENERIS;
    }

    @Override public boolean isValidSdk(@NotNull Module module, @Nullable Sdk projectSdk) {
        return projectSdk == SuiGenerisSdk.getInstance();
    }

    @NotNull @Override public String getName() {
        return Constants.SUI_GENERIS;
    }

    @Override public Icon getNodeIcon(@Deprecated boolean isOpened) {
        return MMFileType.ENTITY_FILE_ICON;
    }
}  // end class SuiGenerisModuleType
