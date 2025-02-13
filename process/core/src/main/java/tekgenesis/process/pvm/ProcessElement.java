
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

package tekgenesis.process.pvm;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;

/**
 * The Base Interface for process elements.
 */

public interface ProcessElement extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Return the Id of the element. */
    @NotNull String getId();

    /** Return the Process that this element belongs to. */
    @NotNull ProcessDefinition getProcessDefinition();

    /** Get the list of properties for this element. */
    ImmutableCollection<String> getProperties();

    /** Return the value of the property with the specified name. */
    @Nullable Object getProperty(String name);

    /** Set a property value. */
    void setProperty(String name, Object value);
}
