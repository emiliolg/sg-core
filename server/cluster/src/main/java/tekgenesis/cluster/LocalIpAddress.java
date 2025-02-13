
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster;

import org.jgroups.stack.IpAddress;

/**
 * Local Ip Address.
 */
@SuppressWarnings("JavaDoc")
public class LocalIpAddress extends IpAddress {

    //~ Constructors .................................................................................................................................

    // Used only by Externalization
    public LocalIpAddress() {}

    /**  */
    public LocalIpAddress(int port) {
        super(port);
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {  // noinspection DuplicateStringLiteralInspection
        return "Local";
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7717114005246144589L;
}
