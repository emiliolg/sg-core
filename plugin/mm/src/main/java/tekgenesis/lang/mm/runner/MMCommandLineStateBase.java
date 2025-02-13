
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.io.File;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathsList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.psi.PsiUtils;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Constants.WEBAPP_DIR;

/**
 * Base class for MM Commands in Idea.
 */
public abstract class MMCommandLineStateBase extends JavaCommandLineState {

    //~ Instance Fields ..............................................................................................................................

    private final MMRunConfigurationBase configuration;

    //~ Constructors .................................................................................................................................

    protected MMCommandLineStateBase(ExecutionEnvironment environment, MMRunConfigurationBase configuration) {
        super(environment);
        this.configuration = configuration;
    }

    //~ Methods ......................................................................................................................................

    protected void addExtraVmParameters(ParametersList parameters) {}

    @SuppressWarnings("SpellCheckingInspection")
    protected void addVmParameters(JavaParameters javaParameters, Module module) {
        final ParametersList vmParameters = javaParameters.getVMParametersList();

        if (isNotEmpty(configuration.getVmOptions())) vmParameters.addParametersString(configuration.getVmOptions());

        vmParameters.add("-D" + SUIGEN_DEVMODE + "=true");

        final PathsList sources = OrderEnumerator.orderEntries(module.getProject()).sources().getPathsList();
        vmParameters.add("-Dsuigen.sources=" + sources.getPathsString());

        addExtraVmParameters(vmParameters);
        vmParameters.add("-Dapplication.noCluster=" + configuration.isNoCluster());

        final Seq<String> dirs = Colls.listOf(module).append(PsiUtils.getDependentModules(module)).map(MMCommandLineStateBase::getResourcesDir);
        vmParameters.add("-Dapplication.resourceSrcDir=" + dirs.mkString(","));
        vmParameters.add("-Dapplication.resourceOutDir=" + configuration.getRunDir() + "/resources.out");
    }

    @Override protected JavaParameters createJavaParameters()
        throws ExecutionException
    {
        final JavaParameters javaParameters = new JavaParameters();

        final Module module = configuration.getModule();
        assert module != null;

        final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
        final Sdk               sdk         = rootManager.getSdk();
        assert sdk != null;

        final String installationDir = sdk.getHomePath();
        assert installationDir != null;

        javaParameters.configureByModule(module, JavaParameters.JDK_ONLY);
        javaParameters.setWorkingDirectory(configuration.getRunDir());
        javaParameters.getProgramParametersList().add("-a", new File(installationDir, WEBAPP_DIR).getAbsolutePath());
        javaParameters.getProgramParametersList().add("-r", configuration.getRunDir());

        final PathsList list = new PathsList();
        OrderEnumerator.orderEntries(module).recursively().runtimeOnly().productionOnly().withoutSdk().classes().collectPaths(list);

        final String tools = ((JavaSdkType) sdk.getSdkType()).getToolsPath(sdk);
        if (new File(tools).exists()) list.add(tools);

        javaParameters.getProgramParametersList().add("-m", list.getPathsString());
        final String props = configuration.getPropertiesFile().trim();
        if (new File(props).isFile()) javaParameters.getProgramParametersList().add("-c", props);
        configCommand(javaParameters);

        if (!isEmpty(configuration.getProgramArguments()))
            javaParameters.getProgramParametersList().addParametersString(configuration.getProgramArguments());

        final File[] bootLibs = new File(installationDir, "lib/boot").listFiles();
        if (bootLibs != null) {
            for (final File lib : bootLibs)
                javaParameters.getClassPath().add(lib);
        }
        final File[] libs = new File(installationDir, "lib").listFiles();
        if (libs != null) {
            for (final File lib : libs)
                javaParameters.getClassPath().add(lib);
        }

        javaParameters.getClassPath().add(new File(installationDir, "lib/boot/server-app.jar"));
        javaParameters.setMainClass("tekgenesis.app.SuiGeneris");

        addVmParameters(javaParameters, module);

        return javaParameters;
    }  // end method createJavaParameters

    abstract void configCommand(JavaParameters javaParameters);

    //~ Methods ......................................................................................................................................

    @NotNull static String getResourcesDir(final Module m) {
        final VirtualFile dir = FileUtils.getResourcesRoot(m);
        return dir != null ? dir.getPath() : "";
    }
}  // end class MMCommandLineStateBase
