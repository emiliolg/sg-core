
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tekgenesis.process.pvm.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.process.pvm.ProcessDefinition;
import tekgenesis.process.pvm.ProcessElement;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Base implementation of process elements including process definition, activity and transition and
 * EventListeners.
 */
public class ProcessElementImpl implements ProcessElement {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final ProcessDefinition processDefinition;

    @NotNull private final String              id;
    @NotNull private final Map<String, Object> properties;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("cast")
    ProcessElementImpl(@NotNull String id, @Nullable ProcessDefinition processDefinition) {
        this.id = notNull(id);
        final ProcessElement processElement = this;

        this.processDefinition = processDefinition == null ? (ProcessDefinition) processElement : processDefinition;
        properties             = new LinkedHashMap<>();
    }

    //~ Methods ......................................................................................................................................

    @NotNull public String getId() {
        return id;
    }

    @NotNull public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    @NotNull @Override public ImmutableCollection<String> getProperties() {
        return immutable(properties.keySet());
    }

    @Nullable public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override public void setProperty(@NotNull String name, @NotNull Object value) {
        properties.put(name, value);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1L;
}  // end class ProcessElementImpl
