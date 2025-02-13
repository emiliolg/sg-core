
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

/**
 * A device.
 */
public interface Device {

    //~ Methods ......................................................................................................................................

    /** Returns device id. */
    String getDeviceId();

    /** Returns device fancy name. */
    String getName();
}
