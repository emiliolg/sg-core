
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import tekgenesis.type.Types;

class DynamicTypeConfigurationImpl implements DynamicTypeConfiguration {

    //~ Instance Fields ..............................................................................................................................

    private final DynamicConfigurationImpl configuration;

    //~ Constructors .................................................................................................................................

    DynamicTypeConfigurationImpl(DynamicConfigurationImpl configuration) {
        this.configuration = configuration;
    }

    //~ Methods ......................................................................................................................................

    @Override public void booleanType() {
        configuration.setType(Types.booleanType());
    }

    @Override public void dateTimeType() {
        configuration.setType(Types.dateTimeType());
    }

    @Override public void dateTimeType(int precision) {
        configuration.setType(Types.dateTimeType(precision));
    }

    @Override public void dateType() {
        configuration.setType(Types.dateType());
    }

    @Override public void decimalType() {
        configuration.setType(Types.decimalType());
    }

    @Override public void decimalType(int precision) {
        configuration.setType(Types.decimalType(precision));
    }

    @Override public void decimalType(int precision, int decimals) {
        configuration.setType(Types.decimalType(precision, decimals));
    }

    @Override public void intType() {
        configuration.setType(Types.intType());
    }

    @Override public void intType(int length) {
        configuration.setType(Types.intType(length));
    }

    @Override public void realType() {
        configuration.setType(Types.realType());
    }

    @Override public void signedIntType() {
        intType();
        configuration.setSigned();
    }

    @Override public void signedIntType(int length) {
        intType(length);
        configuration.setSigned();
    }

    @Override public void stringType() {
        configuration.setType(Types.stringType());
    }

    @Override public void stringType(int length) {
        configuration.setType(Types.stringType(length));
    }
}  // end class DynamicTypeConfigurationImpl
