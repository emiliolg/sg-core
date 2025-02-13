
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.properties;

import javax.inject.Named;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.env.Properties;

/**
 * Map properties.
 */
@Named("forms")
@SuppressWarnings("DuplicateStringLiteralInspection")
public class FormProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Enable/disable mail address validator. */
    public boolean mailValidatorEnabled = false;

    /** Map widget. */
    public String mapUnigisServer = UNIGIS_SERVER;

    //~ Static Fields ................................................................................................................................

    /** Unigis server default. */
    @NonNls private static final String UNIGIS_SERVER = "unigisdesa.garba.com.ar";
}
