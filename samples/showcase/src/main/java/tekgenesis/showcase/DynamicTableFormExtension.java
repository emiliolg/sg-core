
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.extension.FormExtension;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormFieldRef;

import static tekgenesis.metadata.form.widget.FormBuilderPredefined.display;

/**
 * Extends DynamicFormB.
 */
public class DynamicTableFormExtension extends FormExtension<DynamicTableForm, Addresses> {

    //~ Methods ......................................................................................................................................

    @Override public void extend(final Extender<DynamicTableForm, Addresses> extender)
        throws BuilderException
    {
        // extend with a field to show the mapping to ix
        final FormFieldRef ixDisplayWidget = extender.addInside(DynamicTableForm.Field.CLIENT_ADDRESSES,
                display("Ix Mapping").id("ixMapping").optional());

        // populate row with mapping value on each form load
        DynamicTableForm.addListener(FormListenerType.AFTER_LOAD,
            form -> {
                int index = 0;
                for (final DynamicTableForm.ClientAddressesRow row : form.getClientAddresses())
                    // find ix mapping and display in widget
                    rowF(row).set(ixDisplayWidget, "Ix Mapping for row " + index++);
            });
    }

    @Override public Class<DynamicTableForm> getFormType() {
        return DynamicTableForm.class;
    }
}
