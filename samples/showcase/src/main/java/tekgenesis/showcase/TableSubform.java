
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.configuration.DynamicConfiguration;

/**
 * User class for form: TableSubform
 */
public class TableSubform extends TableSubformBase {

    //~ Static Fields ................................................................................................................................

    public static final String DYNAMIC_VALUE = "Dynamic value nr ";

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        /** Populate row with its number. */
        public void populate(int i) {
            setSomeName("Name nr " + i);
            final DynamicConfiguration configuration = configuration(Field.DYNAMIC_W);
            configuration.getTypeConfiguration().stringType();
            configuration.setWidget(DynamicConfiguration.DynamicWidget.TEXT_FIELD);
            setDynamicW(DYNAMIC_VALUE + i);
        }
    }
}
