
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
import java.util.ArrayList;
import java.util.Collection;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.ProjectUtils;
import tekgenesis.lang.mm.sdk.SuiGenerisSdk;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.lang.mm.MMModuleComponent.hasMMDir;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MM Run configuration. It extends the java run configuration
 */
public abstract class MMRunConfigurationBase extends ModuleBasedConfiguration<JavaRunConfigurationModule> {

    //~ Instance Fields ..............................................................................................................................

    protected boolean noCluster        = true;
    protected String  programArguments = "";
    protected String  propertiesFile   = "";
    protected String  runDir           = null;
    protected String  vmOptions        = "";
    private boolean   cleanRunDir      = false;

    //~ Constructors .................................................................................................................................

    /** Constructor that receives the config name, a module and a configuration factory. */
    protected MMRunConfigurationBase(String name, JavaRunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        super(name, runConfigurationModule, factory);
    }

    //~ Methods ......................................................................................................................................

    @Override public void checkConfiguration()
        throws RuntimeConfigurationException
    {
        final Module module = getModule();

        if (module == null) throw new RuntimeConfigurationException(MSGS.noSuiGenerisModuleSpecified());

        final String name = ApplicationManager.getApplication().runReadAction((Computable<String>) module::getName);
        if (ProjectUtils.findModuleByName(getProject(), name) == null) throw new RuntimeConfigurationException(MSGS.noSuiGenerisModuleSpecified());

        final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
        final Sdk               sdk         = rootManager.getSdk();
        if (sdk == null) throw new RuntimeConfigurationException(MSGS.noSuiGenerisSdkConfigured(name));

        if (!SuiGenerisSdk.isSuiGenerisSDK(sdk)) throw new RuntimeConfigurationException(MSGS.wrongSdkConfiguredForModule(name));

        if (!hasMMDir(module)) throw new RuntimeConfigurationError(MSGS.noMMDirectoryMarkedAsSource(name));
    }

    @Override public void readExternal(Element element)
        throws InvalidDataException
    {
        super.readExternal(element);
        readModule(element);
        noCluster        = Boolean.valueOf(readOption(NO_CLUSTER, element, Boolean.TRUE.toString()));
        runDir           = readOption(RUN_DIR, element, null);
        propertiesFile   = readOption(PROPERTIES_FILE, element, Constants.DEFAULT);
        vmOptions        = readOption(VM_OPTIONS, element, "");
        programArguments = readOption(PROGRAM_ARGUMENTS, element, "");
        cleanRunDir      = Boolean.valueOf(readOption(CLEAN_RUN_DIR, element, Boolean.FALSE.toString()));
    }
    @Override public void writeExternal(Element element)
        throws WriteExternalException
    {
        super.writeExternal(element);
        writeModule(element);
        writeOption(NO_CLUSTER, String.valueOf(isNoCluster()), element);
        writeOption(RUN_DIR, String.valueOf(runDir), element);
        writeOption(PROPERTIES_FILE, String.valueOf(propertiesFile), element);
        writeOption(CLEAN_RUN_DIR, String.valueOf(cleanRunDir()), element);
        writeOption(VM_OPTIONS, getVmOptions(), element);
        writeOption(PROGRAM_ARGUMENTS, getProgramArguments(), element);
    }

    /** Return current config Module. */
    @Nullable public Module getModule() {
        return getConfigurationModule().getModule();
    }

    /**  */
    public void setNoCluster(boolean enabled) {
        noCluster = enabled;
    }

    /** Gets Program Arguments Options. */
    public String getProgramArguments() {
        return programArguments;
    }

    /** Set Vm Options. */
    public void setProgramArguments(String programArguments) {
        this.programArguments = programArguments;
    }

    /** Returns propertiesFile to run. */
    public String getPropertiesFile() {
        return propertiesFile;
    }

    /** set propertiesFile to run. */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    /** .* */
    public boolean isNoCluster() {
        return noCluster;
    }

    /** Set run dir. */
    public void setRunDir(String path) {
        runDir = path;
    }

    @Override public Collection<Module> getValidModules() {
        final ArrayList<Module> modules = new ArrayList<>();
        for (final Module module : getAllModules()) {
            if (MMModuleComponent.getMMDir(module) != null) modules.add(module);
        }
        return modules;
    }

    /** Gets Vm Options. */
    public String getVmOptions() {
        return vmOptions;
    }

    /** Set Vm Options. */
    public void setVmOptions(String vmOptions) {
        this.vmOptions = vmOptions;
    }

    @Nullable protected String readOption(String optionName, Element element, @Nullable String defaultValue) {
        final Element child  = element.getChild(optionName);
        String        result = defaultValue;
        if (child != null) {
            final String value = child.getAttributeValue(VALUE);
            if (value != null && !"null".equals(value)) result = value;
        }
        return result;
    }

    protected void writeOption(String optionName, String value, Element parent) {
        final Element element = new Element(optionName);
        element.setAttribute(VALUE, value);
        parent.addContent(element);
    }

    boolean cleanRunDir() {
        return cleanRunDir;
    }

    void setCleanRunDir(boolean b) {
        cleanRunDir = b;
    }

    String getRunDir() {
        return isNotEmpty(runDir) ? runDir : new File(System.getProperty("user.home"), "run").getAbsolutePath();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String VALUE = "value";

    public static final String TEKGEN_INSTALL = "tekgen/distribution";

    private static final String VM_OPTIONS        = "VmOptions";
    private static final String PROGRAM_ARGUMENTS = "programArguments";
    private static final String PORT              = "port";
    private static final String INSTALL_DIR       = "installDir";
    private static final String NO_CLUSTER        = "noCluster";
    private static final String CLEAN_RUN_DIR     = "cleanRunDir";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String RUN_DIR         = "runDir";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String PROPERTIES_FILE = "propertiesFile";
}  // end class MMRunConfigurationBase
