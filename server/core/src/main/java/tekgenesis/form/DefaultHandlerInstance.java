
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default handler instance.
 */
public class DefaultHandlerInstance extends HandlerInstance<FormInstance<?>> {

    //~ Constructors .................................................................................................................................

    /** Default handler instance constructor. */
    public DefaultHandlerInstance(HttpServletRequest req, HttpServletResponse resp) {
        this.req  = req;
        this.resp = resp;
    }
}
