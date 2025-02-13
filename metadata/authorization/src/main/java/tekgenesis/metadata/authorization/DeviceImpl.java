
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
 * Implementation of Device.
 */
public class DeviceImpl implements Device {

    //~ Instance Fields ..............................................................................................................................

    private final String deviceId;
    private final String name;

    //~ Constructors .................................................................................................................................

    /** Creates a device. */
    public DeviceImpl(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name     = name;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getDeviceId() {
        return deviceId;
    }

    @Override public String getName() {
        return name;
    }
}
