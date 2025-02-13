
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

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.form.widget.Form;

import static tekgenesis.codegen.common.MMCodeGenConstants.HANDLER_INSTANCE;
import static tekgenesis.codegen.common.MMCodeGenConstants.JAVA_DOC;
import static tekgenesis.codegen.common.MMCodeGenConstants.WEAKER_ACCESS;

/**
 * Class to manage the generation of the Handler base code.
 */
public class FormHandlerBaseCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Form form;

    //~ Constructors .................................................................................................................................

    /** Create a FormBaseCodeGenerator. */
    public FormHandlerBaseCodeGenerator(@NotNull JavaCodeGenerator codeGenerator, @NotNull String handler, @NotNull Form form, String baseExt) {
        super(codeGenerator, handler + baseExt);
        this.form = form;
        asAbstract().withSuperclass(generic(HANDLER_INSTANCE, extractImport(form.getFullName())));
        suppressWarnings(WEAKER_ACCESS, JAVA_DOC);
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return form.getSourceName();
    }
}
