
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus.test.msg;

/**
 */
@SuppressWarnings("WeakerAccess")
public class CustomEvent {

    //~ Instance Fields ..............................................................................................................................

    public String msg = "";
    public int    nro;

    //~ Methods ......................................................................................................................................

    public String toString() {
        return msg + "-" + nro;
    }
}
