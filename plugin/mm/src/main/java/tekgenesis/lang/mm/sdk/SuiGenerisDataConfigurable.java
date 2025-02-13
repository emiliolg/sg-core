
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.sdk;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.*;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.JBUI;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.lang.mm.sdk.SuiGenerisSdk.isValidInternalJdk;

/**
 * Configurable UI for SuiGeneris additional data (eg: internal JDK)
 */
public class SuiGenerisDataConfigurable implements AdditionalDataConfigurable {

    //~ Instance Fields ..............................................................................................................................

    private final DefaultComboBoxModel<Sdk> jdks = new DefaultComboBoxModel<>();

    private final SdkModel.Listener listener;
    private final SdkModificator    modificator;

    private boolean modified;

    private boolean myFreeze = false;

    private final JLabel        myInternalJreLabel = new JLabel("Internal Java Platform:");
    private final ComboBox<Sdk> myInternalJres     = new ComboBox<>(jdks);

    private final SdkModel sdks;

    private String sgBuild = "";

    private Sdk sgSdk = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public SuiGenerisDataConfigurable(final SdkModel sdks, final SdkModificator modificator) {
        this.sdks        = sdks;
        this.modificator = modificator;

        listener = new SdkModel.Listener() {
                public void sdkAdded(Sdk sdk) {
                    if (sdk.getSdkType().equals(JavaSdk.getInstance())) addJavaSdk(sdk);
                }

                public void beforeSdkRemove(Sdk sdk) {
                    if (sdk.getSdkType().equals(JavaSdk.getInstance())) removeJavaSdk(sdk);
                }

                public void sdkChanged(Sdk sdk, String previousName) {
                    if (sdk.getSdkType().equals(JavaSdk.getInstance())) updateJavaSdkList(sdk, previousName);
                }

                public void sdkHomeSelected(final Sdk sdk, final String newSdkHome) {
                    if (sdk.getSdkType() instanceof SuiGenerisSdk) internalJdkUpdate(sdk);
                }
            };

        this.sdks.addListener(listener);
    }

    //~ Methods ......................................................................................................................................

    public void apply()
        throws ConfigurationException
    {
        final SuiGenerisSdkData data           = new SuiGenerisSdkData(sgSdk, (Sdk) myInternalJres.getSelectedItem(), sgBuild);
        final SdkModificator    sdkModificator = sgSdk.getSdkModificator();
        sdkModificator.setSdkAdditionalData(data);
        ApplicationManager.getApplication().runWriteAction(sdkModificator::commitChanges);
        ((ProjectJdkImpl) sgSdk).resetVersionString();
        modified = false;
    }

    @SuppressWarnings("rawtypes")
    public JComponent createComponent() {
        final JPanel result = new JPanel(new GridBagLayout());
        result.add(myInternalJreLabel,
            new GridBagConstraints(0,
                GridBagConstraints.RELATIVE,
                1,
                1,
                0,
                1,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                JBUI.emptyInsets(),
                0,
                0));
        // noinspection MagicNumber
        result.add(myInternalJres,
            new GridBagConstraints(1,
                GridBagConstraints.RELATIVE,
                1,
                1,
                1,
                1,
                GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL,
                JBUI.insetsLeft(30),
                0,
                0));
        myInternalJres.setRenderer(new ListCellRendererWrapper<Sdk>() {
                @Override public void customize(JList list, Sdk value, int index, boolean selected, boolean hasFocus) {
                    if (value != null) setText(value.getName());
                }
            });

        myInternalJres.addItemListener(e -> {
            if (myFreeze) return;
            final Sdk javaJdk = (Sdk) e.getItem();
            for (final OrderRootType type : OrderRootType.getAllTypes()) {
                if (((SdkType) javaJdk.getSdkType()).isRootTypeApplicable(type)) {
                    final VirtualFile[] internalRoots   = javaJdk.getSdkModificator().getRoots(type);
                    final VirtualFile[] configuredRoots = modificator.getRoots(type);
                    for (final VirtualFile file : internalRoots) {
                        if (e.getStateChange() == ItemEvent.DESELECTED) modificator.removeRoot(file, type);
                        else {
                            if (ArrayUtil.find(configuredRoots, file) == -1) modificator.addRoot(file, type);
                        }
                    }
                }
            }
        });

        modified = true;
        return result;
    }  // end method createComponent

    public void disposeUIResources() {
        sdks.removeListener(listener);
    }

    public void reset() {
        myFreeze = true;
        updateJdkList();
        myFreeze = false;
        if (sgSdk != null && sgSdk.getSdkAdditionalData() instanceof SuiGenerisSdkData) {
            final SuiGenerisSdkData data       = (SuiGenerisSdkData) sgSdk.getSdkAdditionalData();
            final Sdk               relatedJdk = data.getJavaSdk();
            if (relatedJdk != null) {
                for (int i = 0; i < jdks.getSize(); i++) {
                    if (equal(jdks.getElementAt(i).getName(), relatedJdk.getName())) {
                        myInternalJres.setSelectedIndex(i);
                        break;
                    }
                }
            }
            sgBuild  = data.getSgBuild();
            modified = false;
        }
    }

    public boolean isModified() {
        return modified;
    }

    public void setSdk(Sdk sgSdk) {
        this.sgSdk = sgSdk;
    }

    private void addJavaSdk(final Sdk sdk) {
        jdks.addElement(sdk);
    }

    private void internalJdkUpdate(final Sdk updated) {
        final SuiGenerisSdkData data = (SuiGenerisSdkData) updated.getSdkAdditionalData();
        if (data != null) {
            final Sdk javaSdk = data.getJavaSdk();
            if (jdks.getIndexOf(javaSdk) == -1) jdks.addElement(javaSdk);
            else jdks.setSelectedItem(javaSdk);
        }
    }

    private void removeJavaSdk(final Sdk sdk) {
        jdks.removeElement(sdk);
    }

    private void updateJavaSdkList(Sdk sdk, String previousName) {
        for (final Sdk current : sdks.getSdks()) {
            if (current.getSdkType() instanceof SuiGenerisSdk) {
                final SuiGenerisSdkData data = (SuiGenerisSdkData) current.getSdkAdditionalData();
                if (data != null) {
                    final Sdk internalJava = data.getJavaSdk();
                    if (internalJava != null && equal(internalJava.getName(), previousName)) data.setJavaSdk(sdk);
                }
            }
        }
        updateJdkList();
    }

    private void updateJdkList() {
        jdks.removeAllElements();
        for (final Sdk current : sdks.getSdks()) {
            if (isValidInternalJdk(sgSdk, current)) jdks.addElement(current);
        }
    }
}
