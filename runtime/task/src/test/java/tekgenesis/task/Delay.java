
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

/**
 * Just an utility class to make a delay.
 */
public final class Delay {

    //~ Constructors .................................................................................................................................

    private Delay() {}

    //~ Methods ......................................................................................................................................

    /** .* */
    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (final InterruptedException e) {
            // ignore
        }
    }
}
