
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;

import org.jetbrains.annotations.NotNull;

/**
 * MM Framework Support Provider.
 */
class MMFrameworkSupportProvider extends FrameworkSupportInModuleProvider {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public FrameworkSupportInModuleConfigurable createConfigurable(@NotNull FrameworkSupportModel frameworkSupportModel) {
        return new MMFrameworkConfigurable();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    @NotNull @Override public FrameworkTypeEx getFrameworkType() {
        return FrameworkTypeEx.EP_NAME.findExtension(MMFrameworkType.class);
    }

    //~ Methods ......................................................................................................................................

    public static String getNewModuleFilePath() {
        return newModuleFilePath;
    }

    public static void setNewModuleFilePath(String newModuleFilePath) {
        MMFrameworkSupportProvider.newModuleFilePath = newModuleFilePath;
    }

    //~ Static Fields ................................................................................................................................

    private static String newModuleFilePath = null;
}
