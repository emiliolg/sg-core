
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

/**
 * Configuration for Dynamic widget Types.
 */
public interface DynamicTypeConfiguration {

    //~ Methods ......................................................................................................................................

    /** Set dynamic as boolean type. */
    void booleanType();

    /** Set dynamic as dateTime type. */
    void dateTimeType();

    /** Set dynamic as dateTime type, specifying precision. */
    void dateTimeType(int precision);

    /** Set dynamic as date type. */
    void dateType();

    /** Set dynamic as decimal type. */
    void decimalType();

    /** Set dynamic as decimal type, specifying precision. */
    void decimalType(int precision);

    /** Set dynamic as dateTime type, specifying precision and number of decimals. */
    void decimalType(int precision, int decimals);

    /** Set dynamic as unsigned int type. */
    void intType();

    /** Set dynamic as unsigned int type, specifying length. */
    void intType(int length);

    /** Set dynamic as real type. */
    void realType();

    /** Set dynamic as signed int type. */
    void signedIntType();

    /** Set dynamic as signed int type, specifying length. */
    void signedIntType(int length);

    /** Set dynamic as string type. */
    void stringType();

    /** Set dynamic as string type, specifying length. */
    void stringType(int length);
}  // end interface DynamicTypeConfiguration
