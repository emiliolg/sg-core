
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.form.Action;
import tekgenesis.form.extension.FormExtension;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormFieldRef;

import static tekgenesis.metadata.form.widget.FormBuilderPredefined.field;
import static tekgenesis.showcase.DynamicFormABase.Field.NAVIGATE;
import static tekgenesis.showcase.DynamicFormABase.Field.TEXT_FIELD;

/**
 * Extends DynamicFormA.
 */
public class DynamicFormAExtension extends FormExtension<DynamicFormA, Void> {

    //~ Instance Fields ..............................................................................................................................

    private FormFieldRef dynamicTextField = null;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action create() {
        // final String value = f.get(dynamicTextField, String.class);
        // System.out.println("value = " + value);

        // before create
        return super.create();
               // after create
    }

    @Override public void extend(final Extender<DynamicFormA, Void> extender, @Nullable final String pk, @Nullable final String parameters)
        throws BuilderException
    {
        // modify an existing field option
        extender.findWidget(TEXT_FIELD).defaultValue("onLoad value for static field");

        // add a new field on top
        dynamicTextField = extender.addBefore(TEXT_FIELD, field("Extended Field!").id("dynamicTextField"));

        // add on click listener
        extender.onClick(NAVIGATE, (Function<DynamicFormAExtension, Action>) DynamicFormAExtension::navigate);
    }

    @Override public void load() {
        f.set(dynamicTextField, "onLoad value for dynamic field");
    }

    /** Invoked on navigate click. */
    @NotNull public Action navigate() {
        return actions.navigate(DynamicFormB.class);
    }

    @Override public Class<DynamicFormA> getFormType() {
        return DynamicFormA.class;
    }
}  // end class DynamicFormAExtension
