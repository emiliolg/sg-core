
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.sso;

import javax.inject.Named;

import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Mutable;

/**
 * SSO properties.
 */
@Mutable
@Named("sso")
public class SSOProps {

    //~ Instance Fields ..............................................................................................................................

    /** JSessionID cookie domain. */
    public String domain = null;
    /** Dynamo read capacity for sessions. */
    public int dynamoReadCapacity = 5;
    /** Dynamo suffix for session tables. */
    public String dynamoSuffix = "dynamo";
    /** Dynamo write capacity for sessions. */
    public int dynamoWriteCapacity = 5;

    /** Session storage endpoint. */
    public String endPoint = Constants.LOCALHOST;

    /** Use DB table for session invalidation when CLIENT. */
    public boolean enforceInvalidation = false;
    /** Shiro session storage. LOCAL, DYNAMO, REDIS, CLIENT */
    public SSOMode mode = SSOMode.LOCAL;

    /** SSO client session secret. */
    public String secret = "Piluso";
}
