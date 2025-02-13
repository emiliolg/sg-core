
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

import tekgenesis.form.Action;
import tekgenesis.form.extension.FormExtension;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormFieldRef;

import static tekgenesis.metadata.form.widget.FormBuilderPredefined.field;
import static tekgenesis.showcase.DynamicFormBBase.Field.TEXT_FIELD;

/**
 * Extends DynamicFormB.
 */
public class DynamicFormBExtension extends FormExtension<DynamicFormB, Void> {

    //~ Instance Fields ..............................................................................................................................

    private FormFieldRef dynamicTextField = null;

    //~ Methods ......................................................................................................................................

    @Override public void extend(final Extender<DynamicFormB, Void> extender)
        throws BuilderException
    {
        // modify an existing field option
        extender.findWidget(TEXT_FIELD).defaultValue("onLoad value for static field");

        // add a new field on top
        dynamicTextField = extender.addBefore(TEXT_FIELD, field("Extended Field!").id("dynamicTextField"));
        extender.onChange(dynamicTextField, (Function<DynamicFormBExtension, Action>) DynamicFormBExtension::onDynamicFieldChange);
    }

    @Override public void load() {
        f.set(dynamicTextField, "onLoad value for dynamic field");
    }

    /** Invoked on dynamicTextFieldChange. */
    public Action onDynamicFieldChange() {
        f.set(dynamicTextField, "onChange value for dynamic field");
        return actions.getDefault();
    }

    @Override public Class<DynamicFormB> getFormType() {
        return DynamicFormB.class;
    }
}
