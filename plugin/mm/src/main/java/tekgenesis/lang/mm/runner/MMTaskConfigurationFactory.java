
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

/**
 * MM Task ConfigurationFactory for intellij. Created by Jose on 8/27/14.
 */
public class MMTaskConfigurationFactory extends MMConfigurationFactory {

    //~ Constructors .................................................................................................................................

    MMTaskConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new MMRunTaskConfiguration(new JavaRunConfigurationModule(project, true), this);
    }
}
