
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import tekgenesis.view.shared.response.FormModelResponse;

/**
 * Method to be performed on server after form request.
 */
public interface ServerMethod {

    //~ Methods ......................................................................................................................................

    /** Execute method delegation. */
    FormModelResponse exec();
}
