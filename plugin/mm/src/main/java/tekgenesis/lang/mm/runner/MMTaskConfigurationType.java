
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

import static tekgenesis.lang.mm.MMPluginConstants.SUI_GEN_TASK_CONFIGURATION;

/**
 * Intellij Idea Configuration type for MM Task runner. Created by Jose on 8/26/14.
 */
public class MMTaskConfigurationType implements ConfigurationType {

    //~ Instance Fields ..............................................................................................................................

    private final MMTaskConfigurationFactory myTaskConfigurationTypeFactory;

    //~ Constructors .................................................................................................................................

    protected MMTaskConfigurationType() {
        myTaskConfigurationTypeFactory = new MMTaskConfigurationFactory(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { myTaskConfigurationTypeFactory };
    }

    @Override public String getConfigurationTypeDescription() {
        return "SuiGeneris Task Configuration Description";
    }

    @Override public String getDisplayName() {
        return SUI_GEN_TASK_CONFIGURATION;
    }

    @Override public Icon getIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }

    @NotNull @Override public String getId() {
        return "MM.TaskConfigurationType";
    }

    //~ Methods ......................................................................................................................................

    /** Returns an instance of MMConfigurationFactory. */
    public static MMTaskConfigurationFactory getFactory() {
        return getInstance().myTaskConfigurationTypeFactory;
    }

    /** Get configuration type instance. */
    public static MMTaskConfigurationType getInstance() {
        return ContainerUtil.findInstance(Extensions.getExtensions(CONFIGURATION_TYPE_EP), MMTaskConfigurationType.class);
    }
}  // end class MMTaskConfigurationType
