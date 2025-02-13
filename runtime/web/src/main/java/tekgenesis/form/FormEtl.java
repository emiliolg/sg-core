
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Base class for form Export/Import.
 */
public interface FormEtl<This extends FormEtl<This>> {

    //~ Methods ......................................................................................................................................

    /** Specified form fields arguments. */
    This arguments(@NotNull Map<String, String> arguments);

    /** Defines json as the type of input/output to be used, and returns a configuration for it. */
    JsonConfiguration usingJson();

    /** Defines xml as the type of input/output to be used. */
    void usingXml();
}
