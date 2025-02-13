
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import javax.swing.*;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.util.containers.ContainerUtil;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.lang.mm.MMPluginConstants.SUI_GEN_CONFIGURATION;

/**
 * Intellij Idea Configuration type for MM runner.
 */
@SuppressWarnings("WeakerAccess")
public class MMConfigurationType implements ConfigurationType {

    //~ Instance Fields ..............................................................................................................................

    private final MMConfigurationFactory myConfigurationTypeFactory;

    //~ Constructors .................................................................................................................................

    protected MMConfigurationType() {
        myConfigurationTypeFactory = new MMConfigurationFactory(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { myConfigurationTypeFactory };
    }

    @Override public String getConfigurationTypeDescription() {
        return "SuiGeneris Configuration Description";
    }

    @Override public String getDisplayName() {
        return SUI_GEN_CONFIGURATION;
    }

    @Override public Icon getIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }

    @NotNull @Override public String getId() {
        return "MM.ConfigurationType";
    }

    //~ Methods ......................................................................................................................................

    /** Returns an instance of MMConfigurationFactory. */
    public static MMConfigurationFactory getFactory() {
        return getInstance().myConfigurationTypeFactory;
    }

    /** Get configuration type instance. */
    public static MMConfigurationType getInstance() {
        return ContainerUtil.findInstance(Extensions.getExtensions(CONFIGURATION_TYPE_EP), MMConfigurationType.class);
    }
}  // end class MMConfigurationType
