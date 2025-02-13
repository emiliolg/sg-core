
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.sdk;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.ValidatableSdkAdditionalData;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.INTERNAL_JDK;
import static tekgenesis.common.core.Constants.SG_BUILD;

/**
 * Additional MetaModel sdk data (Sui Generis version number & related jdk name).
 */
public class SuiGenerisSdkData implements ValidatableSdkAdditionalData {

    //~ Instance Fields ..............................................................................................................................

    private Sdk javaSdk = null;

    /** Related jdk name. */
    private String javaSdkName = "";

    /** Sui Generis sdk build. */
    private String sgBuild = "";

    private final Sdk sgSdk;

    //~ Constructors .................................................................................................................................

    /** Related sdk constructor for persisted state. */
    public SuiGenerisSdkData(@NotNull Sdk sgSdk, @NotNull Element element) {
        this.sgSdk  = sgSdk;
        javaSdkName = element.getAttributeValue(INTERNAL_JDK);
        sgBuild     = notNull(element.getAttributeValue(SG_BUILD), "");
    }

    /** Related sdk default constructor. */
    public SuiGenerisSdkData(@NotNull Sdk sgSdk, @NotNull Sdk javaSdk, @NotNull String sgBuild) {
        this.sgSdk   = sgSdk;
        this.javaSdk = javaSdk;
        this.sgBuild = sgBuild;
    }

    //~ Methods ......................................................................................................................................

    @Override public void checkValid(SdkModel sdkModel)
        throws ConfigurationException
    {
        if (getJavaSdk() == null) throw new ConfigurationException("No Java SDK found. Please configure internal JDK.");
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone()
        throws CloneNotSupportedException
    {
        final Sdk sdk = getJavaSdk();
        assert sdk != null;
        return new SuiGenerisSdkData(sgSdk, sdk, sgBuild);
    }

    /** Set internal java jdk. */
    public void setJavaSdk(final Sdk javaSdk) {
        this.javaSdk = javaSdk;
    }

    /** Set sg build. */
    public void setSgBuild(String sgBuild) {
        this.sgBuild = sgBuild;
    }

    void save(Element element) {
        final Sdk sdk = getJavaSdk();
        if (sdk != null) element.setAttribute(INTERNAL_JDK, sdk.getName());
        element.setAttribute(SG_BUILD, sgBuild);
    }

    @Nullable Sdk getJavaSdk() {
        final ProjectJdkTable jdkTable = ProjectJdkTable.getInstance();
        if (javaSdk == null) {
            if (javaSdkName != null) {
                javaSdk     = jdkTable.findJdk(javaSdkName);
                javaSdkName = null;
            }
            else {
                for (final Sdk jdk : jdkTable.getAllJdks()) {
                    if (SuiGenerisSdk.isValidInternalJdk(sgSdk, jdk)) {
                        javaSdk = jdk;
                        break;
                    }
                }
            }
        }
        return javaSdk;
    }

    @NotNull String getSgBuild() {
        return sgBuild;
    }
}  // end class SuiGenerisSdkData
