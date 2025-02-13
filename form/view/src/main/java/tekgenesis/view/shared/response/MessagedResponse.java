
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

/**
 * Defines a message for a response.
 */
@SuppressWarnings("WeakerAccess")
public interface MessagedResponse {

    //~ Methods ......................................................................................................................................

    /** Get response message. */
    String getMessage();

    /** Add message to response. */
    void setMessage(final String msg);
}
