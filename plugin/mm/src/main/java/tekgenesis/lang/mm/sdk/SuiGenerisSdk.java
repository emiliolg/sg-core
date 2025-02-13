
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
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.swing.*;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.JavaDependentSdkType;
import com.intellij.openapi.projectRoots.impl.JavaSdkImpl;
import com.intellij.openapi.roots.AnnotationOrderRootType;
import com.intellij.openapi.roots.JavadocOrderRootType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.ArrayUtil;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.settings.MMSettings;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.SOURCES;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * SuiGeneris language SDK definition.
 */
public class SuiGenerisSdk extends JavaDependentSdkType implements JavaSdkType {

    //~ Constructors .................................................................................................................................

    /** SuiGeneris language SDK default constructor. */
    public SuiGenerisSdk() {
        super(DEFAULT_SDK_NAME);
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdk, @NotNull SdkModificator modifier) {
        return new SuiGenerisDataConfigurable(sdk, modifier);
    }

    @Nullable @Override public SuiGenerisSdkData loadAdditionalData(@NotNull Sdk sgSdk, Element additional) {
        return new SuiGenerisSdkData(sgSdk, additional);
    }

    @Override public void saveAdditionalData(@NotNull SdkAdditionalData data, @NotNull Element additional) {
        if (data instanceof SuiGenerisSdkData) ((SuiGenerisSdkData) data).save(additional);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean setupSdkPaths(@NotNull final Sdk sdk, @NotNull SdkModel sdkModel) {
        final SdkModificator modifier = sdk.getSdkModificator();
        final List<String>   javaSdk  = new ArrayList<>();

        for (final Sdk jdk : sdkModel.getSdks()) {
            if (isValidInternalJdk(sdk, jdk)) javaSdk.add(jdk.getName());
        }
        if (javaSdk.isEmpty()) {
            final JavaSdkVersion required = getRequiredJdkVersion(sdk);
            Messages.showErrorDialog(String.format("No Java SDK for Sui Generis SDK found. " +
                    "At least version %s is required.", required),
                "No Java SDK Found");
            return false;
        }

        final int choice;

        if (javaSdk.size() > 1)
            choice = Messages.showChooseDialog("Select Java SDK to be used for Sui Generis SDK",
                    "Select Internal Java Platform",
                    ArrayUtil.toStringArray(javaSdk),
                    javaSdk.get(0),
                    Messages.getQuestionIcon());
        else choice = 0;

        if (choice != -1) {
            final String name = javaSdk.get(choice);
            final Sdk    jdk  = sdkModel.findSdk(name);
            assert jdk != null;
            final String home = sdk.getHomePath();
            assert home != null;
            setupSdkPaths(modifier, home, jdk);
            final String sgBuild = getSdkVersion(getVersionPropertiesFile(new File(home))).second();
            modifier.setSdkAdditionalData(new SuiGenerisSdkData(sdk, jdk, sgBuild));
            modifier.setVersionString(jdk.getVersionString());
            JavaSdkImpl.attachJdkAnnotations(modifier);
            modifier.commitChanges();
            return true;
        }
        return false;
    }  // end method setupSdkPaths

    @Nullable @Override public String suggestHomePath() {
        return null;
    }

    @Override public String suggestSdkName(String currentSdkName, String sdkHome) {
        if (isEmpty(currentSdkName) || currentSdkName.startsWith(DEFAULT_SDK_NAME)) {
            final File home = new File(sdkHome);
            return DEFAULT_SDK_NAME + String.format(" (%s)", home.getParentFile().getName().toLowerCase());
        }
        return DEFAULT_SDK_NAME;
    }

    @Nullable @Override public String getBinPath(Sdk sdk) {
        final Sdk jdk = getInternalJavaSdk(sdk);
        return jdk == null ? null : JavaSdk.getInstance().getBinPath(jdk);
    }

    @Override public boolean isRootTypeApplicable(@NotNull OrderRootType type) {
        return type == OrderRootType.CLASSES || type == OrderRootType.SOURCES || type == JavadocOrderRootType.getInstance() ||
               type == AnnotationOrderRootType.getInstance();
    }

    @Override public boolean isValidSdkHome(String path) {
        return isValidSgSdk(new File(path));
    }

    @Override public Icon getIcon() {
        return IconLoader.getIcon(MMSettings.GRAY_ICONPATH);
    }

    @NotNull @Override public Icon getIconForAddAction() {
        return getIcon();
    }

    @NotNull @Override public String getPresentableName() {
        return DEFAULT_SDK_NAME;
    }

    @Nullable @Override public String getToolsPath(Sdk sdk) {
        final Sdk jdk = getInternalJavaSdk(sdk);
        if (jdk != null && jdk.getVersionString() != null) return JavaSdk.getInstance().getToolsPath(jdk);
        return null;
    }

    @Nullable public final String getVersionString(@NotNull final Sdk sdk) {
        final Sdk java = getInternalJavaSdk(sdk);
        return java != null ? java.getVersionString() : null;
    }

    @Nullable @Override public String getVMExecutablePath(Sdk sdk) {
        final Sdk jdk = getInternalJavaSdk(sdk);
        return jdk == null ? null : JavaSdk.getInstance().getVMExecutablePath(jdk);
    }

    //~ Methods ......................................................................................................................................

    /** Return Sui Generis sdk type instance. */
    public static SdkType getInstance() {
        return SdkType.findInstance(SuiGenerisSdk.class);
    }

    /** Return true if given sdk is not null and has Sui Generis type. */
    public static boolean isSuiGenerisSDK(@Nullable Sdk sdk) {
        return sdk != null && sdk.getSdkType() instanceof SuiGenerisSdk;
    }

    /** Check MetaModel sdk accepts internal Java jdk version. */
    public static boolean isValidInternalJdk(Sdk metaModelSdk, Sdk sdk) {
        final SdkTypeId sdkType = sdk.getSdkType();
        if (sdkType instanceof JavaSdk) {
            final JavaSdkVersion version         = JavaSdk.getInstance().getVersion(sdk);
            final JavaSdkVersion requiredVersion = getRequiredJdkVersion(metaModelSdk);
            if (version != null) return version.isAtLeast(requiredVersion);
        }
        return false;
    }

    static void addSources(SdkModificator modifier, @Nullable File... sources) {
        final JarFileSystem jsf = JarFileSystem.getInstance();
        if (sources != null) {
            for (final File jar : sources) {
                if (jar.exists()) {
                    final String path = jar.getAbsolutePath().replace(File.separatorChar, '/') + JarFileSystem.JAR_SEPARATOR;
                    jsf.setNoCopyJarForPath(path);
                    modifier.addRoot(jsf.findFileByPath(path), OrderRootType.SOURCES);
                }
            }
        }
    }
    static void appendMetaModelLibraries(File directory, final Collection<VirtualFile> result) {
        if (directory.isDirectory()) {
            final File[] jars = directory.listFiles();
            if (jars != null) {
                final JarFileSystem jfs = JarFileSystem.getInstance();
                for (final File jar : jars) {
                    final String name = jar.getName();
                    if (jar.isFile() && (name.endsWith(".jar") || name.endsWith(".zip")))
                        result.add(jfs.findFileByPath(jar.getPath() + JarFileSystem.JAR_SEPARATOR));
                }
            }
        }
    }

    static boolean isValidSgSdk(final File path) {
        final File version = getVersionPropertiesFile(path);
        if (!version.exists()) return false;

        try {
            getSdkVersion(version);
        }
        catch (final UncheckedIOException e) {
            return false;
        }

        final File bin = new File(path.getAbsolutePath() + File.separator + Constants.BIN);
        if (!bin.exists()) return false;

        final File lib = getLibDirectory(path);
        if (!lib.exists()) return false;

        final File test = getTestDirectory(lib);
        if (!test.exists()) return false;

        final File boot = new File(lib.getAbsolutePath() + File.separator + Constants.BOOT);
        return boot.exists();
    }

    static File getLibDirectory(File home) {
        return new File(home.getAbsolutePath() + File.separator + Constants.LIB);
    }

    static VirtualFile[] getMetaModelLibraries(String home) {
        final List<VirtualFile> result = new ArrayList<>();

        // Add lib jars.
        final File lib = getLibDirectory(new File(home));
        appendMetaModelLibraries(lib, result);

        // Add lib/test jars.
        final File test = getTestDirectory(lib);
        appendMetaModelLibraries(test, result);

        return VfsUtilCore.toVirtualFileArray(result);
    }

    static Tuple<String, String> getSdkVersion(@NotNull File version) {
        try {
            final Properties properties = new Properties();
            properties.load(new FileReader(version));
            return tuple(properties.getProperty(Constants.SDK_VERSION), properties.getProperty(Constants.SDK_BUILD));
        }
        catch (final IOException e) {
            logger.error(e);
            throw new UncheckedIOException(e);
        }
    }

    static File getTestDirectory(File lib) {
        return new File(lib.getAbsolutePath() + File.separator + Constants.TEST);
    }

    static File getVersionPropertiesFile(File home) {
        return new File(home.getAbsolutePath() + File.separator + Constants.VERSION_PROPERTIES);
    }

    private static void addJdkClasses(SdkModificator sdkModificator, final Sdk javaSdk) {
        addOrderEntries(OrderRootType.CLASSES, javaSdk, sdkModificator);
    }

    private static void addJdkDocs(SdkModificator sdkModificator, final Sdk javaSdk) {
        if (!addOrderEntries(JavadocOrderRootType.getInstance(), javaSdk, sdkModificator) && SystemInfo.isMac) {
            for (final Sdk jdk : ProjectJdkTable.getInstance().getAllJdks()) {
                if (jdk.getSdkType() instanceof JavaSdk) {
                    addOrderEntries(JavadocOrderRootType.getInstance(), jdk, sdkModificator);
                    break;
                }
            }
        }
    }

    private static void addJdkSources(SdkModificator sdkModificator, final Sdk javaSdk) {
        if (javaSdk != null) {
            if (!addOrderEntries(OrderRootType.SOURCES, javaSdk, sdkModificator)) {
                if (SystemInfo.isMac) {
                    for (final Sdk jdk : ProjectJdkTable.getInstance().getAllJdks()) {
                        if (jdk.getSdkType() instanceof JavaSdk) {
                            addOrderEntries(OrderRootType.SOURCES, jdk, sdkModificator);
                            break;
                        }
                    }
                }
                else {
                    final String javaHomePath = javaSdk.getHomePath();
                    if (javaHomePath != null) {
                        final File   jdkHome = new File(javaHomePath).getParentFile();
                        final String srcZip  = "src.zip";
                        final File   jarFile = new File(jdkHome, srcZip);
                        if (jarFile.exists()) {
                            final JarFileSystem jarFileSystem = JarFileSystem.getInstance();
                            final String        path          = jarFile.getAbsolutePath().replace(File.separatorChar, '/') +
                                                                JarFileSystem.JAR_SEPARATOR;
                            jarFileSystem.setNoCopyJarForPath(path);
                            sdkModificator.addRoot(jarFileSystem.findFileByPath(path), OrderRootType.SOURCES);
                        }
                    }
                }
            }
        }
    }

    private static boolean addOrderEntries(OrderRootType orderRootType, Sdk sdk, SdkModificator modifier) {
        boolean        someAdded = false;
        final String[] entries   = sdk.getRootProvider().getUrls(orderRootType);
        for (final String entry : entries) {
            final VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(entry);
            if (virtualFile != null) {
                modifier.addRoot(virtualFile, orderRootType);
                someAdded = true;
            }
        }
        return someAdded;
    }

    private static void addSources(File home, SdkModificator modifier) {
        final File src = new File(home, SOURCES);
        if (!src.exists()) return;

        addSources(modifier, src.listFiles(pathname -> {
                final String path = pathname.getPath();
                return path.endsWith(SOURCES_SUFFIX);
            }));
    }

    private static void setupSdkPaths(final SdkModificator modifier, final String home, final Sdk jdk) {
        // Roots from internal jre
        addJdkClasses(modifier, jdk);
        addJdkDocs(modifier, jdk);
        addJdkSources(modifier, jdk);

        // Roots for suigeneris libraries
        for (final VirtualFile mmLibrary : getMetaModelLibraries(home))
            modifier.addRoot(mmLibrary, OrderRootType.CLASSES);

        // Roots for suigeneris sources
        addSources(new File(home), modifier);
    }

    @Nullable private static Sdk getInternalJavaSdk(final Sdk sdk) {
        final SdkAdditionalData data = sdk.getSdkAdditionalData();
        if (data instanceof SuiGenerisSdkData) return ((SuiGenerisSdkData) data).getJavaSdk();
        return null;
    }

    @NotNull private static JavaSdkVersion getRequiredJdkVersion(Sdk metaModelSdk) {
        final String path = metaModelSdk.getHomePath();
        if (path == null) return JavaSdkVersion.JDK_1_7;

        try {
            final Tuple<String, String> version = getSdkVersion(getVersionPropertiesFile(new File(path)));
            switch (version.first()) {
            case "2.2":
                return JavaSdkVersion.JDK_1_7;
            case "3.0":
                return JavaSdkVersion.JDK_1_8;
            default:
                return JavaSdkVersion.JDK_1_7;
            }
        }
        catch (final UncheckedIOException e) {
            return JavaSdkVersion.JDK_1_7;
        }
    }

    //~ Static Fields ................................................................................................................................

    static final String SOURCES_SUFFIX = "-src.jar";

    private static final String DEFAULT_SDK_NAME = Constants.SUI_GENERIS_SDK;
    private static final Logger logger           = Logger.getLogger(SuiGenerisSdk.class);
}  // end class SuiGenerisSdk
