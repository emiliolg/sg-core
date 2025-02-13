
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.io.File;
import java.util.ArrayList;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;

import static java.lang.String.format;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.UiModelBaseCodeGenerator.kindAsString;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.BOOLEAN;
import static tekgenesis.common.core.Constants.DEPRECATED;

/**
 * Generate code for {@link UiModel} user classes.
 */
class UiModelCodeGenerator<M extends UiModel, B extends UiModelBaseCodeGenerator<M>> extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final B                 base;
    private final JavaCodeGenerator generator;

    //~ Constructors .................................................................................................................................

    /** Create a {@link UiModel} user class generator. */
    UiModelCodeGenerator(JavaCodeGenerator generator, M model, B base) {
        super(generator, model.getImplementationClassName());
        withSuperclass(model.getFullName() + BASE).withComments("User class for " + kindAsString(model) + ": " + model.getName());
        this.generator = generator;
        this.base      = base;

        traverse(model);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void populate() {
        overrideMethods(base.getAbstractMethods(), this);

        base.getTables().forEach(table -> new InnerTableCodeGenerator(generator, table));

        super.populate();
    }

    private void generateSuggestBoxMethods(Widget w) {
        final String methodName = w.getOnSuggestMethodName();
        if (isNotEmpty(methodName) && !hasMethod(methodName)) {
            final Type t = w.getType().isArray() ? ((ArrayType) w.getType()).getElementType() : w.getType();

            final String generic    = t.isString() || w.getWidgetType() == WidgetType.SEARCH_BOX ? extractImport(SUGGESTION_CLASS)
                                                                                                 : t.getImplementationClassName();
            final String returnType = generic(Iterable.class, generic);

            final Method onSuggest = method(methodName, extractImport(returnType));

            onSuggest.withComments(format(WidgetContainerClassGenerator.ON_SUGGEST_JAVADOC, w));
            onSuggest.notNull();
            onSuggest.asStatic();
            onSuggest.arg(QUERY_METHOD, String.class);

            if (w.getWidgetType() == WidgetType.SEARCH_BOX && w.isBoundedToDeprecable()) onSuggest.arg(DEPRECATED, BOOLEAN);

            final Expression expression = w.getOnSuggestExpr();
            if (!expression.isNull()) onSuggest.arg(VALUE_METHOD, expression.getType().getImplementationClassName());

            final String genericNew = t.isString() || w.getWidgetType() == WidgetType.SEARCH_BOX ? extractImport(SUGGESTION_CLASS)
                                                                                                 : t.getImplementationClassName();
            final String returnNew  = generic(ArrayList.class, genericNew);
            onSuggest.return_(new_(returnNew));
        }
    }

    private void overrideMethods(Iterable<Method> abstractMethods, ClassGenerator cg) {
        abstractMethods.forEach(method -> method.overrideIn(cg));
    }

    private void traverse(Iterable<Widget> widgets) {
        for (final Widget w : widgets) {
            final WidgetType widgetType = w.getWidgetType();

            switch (widgetType) {
            case TAGS_SUGGEST_BOX:
            case SUGGEST_BOX:
            case SEARCH_BOX:
                generateSuggestBoxMethods(w);
                break;
            default:
                traverse(w);
            }
        }
    }

    //~ Methods ......................................................................................................................................

    /** Gets the user source java file for the form. */
    public static File getUserSource(final File sourceDir, final Form f) {
        return new File(sourceDir, f.getImplementationClassFullName().replaceAll("\\.", File.separator) + Constants.JAVA_EXT);
    }

    //~ Inner Classes ................................................................................................................................

    private class InnerTableCodeGenerator extends ClassGenerator {
        InnerTableCodeGenerator(JavaCodeGenerator cg, InnerTableBaseCodeGenerator table) {
            super(cg, table.getTableClassName());
            withSuperclass(table.getName());
            overrideMethods(table.getAbstractMethods(), this);
            UiModelCodeGenerator.this.addInner(this);
        }
    }
}  // end class UiModelCodeGenerator
