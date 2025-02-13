
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.form.widget.Form;

/**
 * Class to manage the generation of the Handler final (User editable) code.
 */
public class FormHandlerCodeGenerator extends ClassGenerator {

    //~ Constructors .................................................................................................................................

    /** Create a handler code generator. */
    public FormHandlerCodeGenerator(@NotNull JavaCodeGenerator generator, @NotNull String handler, @NotNull Form form,
                                    @NotNull FormHandlerBaseCodeGenerator base) {
        super(generator, handler);
        withSuperclass(extractImport(base.getName())).withComments(
            "User class for handler " + form.getHandlerClass() + " on form: " + form.getName());
    }
}
