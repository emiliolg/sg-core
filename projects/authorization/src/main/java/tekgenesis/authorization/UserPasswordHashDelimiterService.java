
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.ServiceLoader;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * User & Pass delimiter service.
 */
public class UserPasswordHashDelimiterService {

    //~ Instance Fields ..............................................................................................................................

    private Option<UserPasswordHashDelimiter> delimiter = Option.empty();

    //~ Constructors .................................................................................................................................

    private UserPasswordHashDelimiterService() {
        final ServiceLoader<UserPasswordHashDelimiter> loader = ServiceLoader.load(UserPasswordHashDelimiter.class);
        for (final UserPasswordHashDelimiter d : loader) {
            if (delimiter.isEmpty()) delimiter = some(d);
            else
                logger.info(
                    "User password hash delimiter already defined. [" + delimiter.getClass().getName() + "]. Ignoring:[" + d
                        .getClass().getName() + "]");
        }
    }

    //~ Methods ......................................................................................................................................

    /** Gets current delimiter. */
    public static Option<UserPasswordHashDelimiter> getDelimiter() {
        return getInstance().delimiter;
    }

    private static synchronized UserPasswordHashDelimiterService getInstance() {
        if (service == null) service = new UserPasswordHashDelimiterService();
        return service;
    }

    //~ Static Fields ................................................................................................................................

    private static UserPasswordHashDelimiterService service = null;

    private static final Logger logger = getLogger(UserPasswordHashDelimiterService.class);
}
