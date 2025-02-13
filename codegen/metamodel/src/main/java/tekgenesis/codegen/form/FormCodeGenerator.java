
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.form.widget.Form;

/**
 * Generate code for {@link Form} user classes.
 */
public class FormCodeGenerator extends UiModelCodeGenerator<Form, FormBaseCodeGenerator> {

    //~ Constructors .................................................................................................................................

    /** Create a {@link FormCodeGenerator}. */
    public FormCodeGenerator(JavaCodeGenerator generator, Form model, FormBaseCodeGenerator base) {
        super(generator, model, base);
    }
}
