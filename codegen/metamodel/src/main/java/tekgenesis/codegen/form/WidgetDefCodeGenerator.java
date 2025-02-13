
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
import tekgenesis.metadata.form.widget.WidgetDef;

/**
 * Generate code for {@link WidgetDef} user classes.
 */
public class WidgetDefCodeGenerator extends UiModelCodeGenerator<WidgetDef, WidgetDefBaseCodeGenerator> {

    //~ Constructors .................................................................................................................................

    /** Create a {@link WidgetDefCodeGenerator}. */
    public WidgetDefCodeGenerator(JavaCodeGenerator generator, WidgetDef model, WidgetDefBaseCodeGenerator base) {
        super(generator, model, base);
        if (model.isAbstract()) asAbstract();
    }
}
