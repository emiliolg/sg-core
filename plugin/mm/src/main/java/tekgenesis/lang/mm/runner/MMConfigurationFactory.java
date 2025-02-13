
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

/**
 * MM ConfigurationFactory for intellij.
 */
public class MMConfigurationFactory extends ConfigurationFactoryEx<MMRunConfigurationBase> {

    //~ Constructors .................................................................................................................................

    MMConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new MMRunConfiguration(new JavaRunConfigurationModule(project, true), this);
    }

    @Override public void onNewConfigurationCreated(@NotNull MMRunConfigurationBase configuration) {
        configuration.onNewConfigurationCreated();
    }
}
