
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * Social provider configuration properties.
 */
@Mutable
@Named("provider")
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SocialProviderProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Provider callback url. */
    public String callback = null;

    /** Enable provider. */
    public boolean enabled;

    /** Provider key. */
    public String key = null;

    /** Provider secret. */
    public String secret = null;
}
