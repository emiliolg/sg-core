
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect.delta;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

/**
 * Delta between 2 MetadataObjects.
 */
public interface MdDelta {

    //~ Instance Fields ..............................................................................................................................

    MdDelta EMPTY = new MdDelta() {
            @Override public List<String> getChanged() {
                return emptyList();
            }
            @Override public List<String> getFromOnly() {
                return emptyList();
            }
            @Override public Map<String, String> getRenamed() {
                return emptyMap();
            }
            @Override public List<String> getToOnly() {
                return emptyList();
            }
            @Override public boolean isEmpty() {
                return true;
            }
            @Override public boolean isMinor() {
                return true;
            }
            @Override public void generate(final PrintWriter pw) {}
            @Override public void dropElements(final PrintWriter pw) {}
            @Override public void createElements(final PrintWriter pw) {}
        };

    //~ Methods ......................................................................................................................................

    /** Create elements. */
    void createElements(PrintWriter pw);

    /** Drop elements. */
    void dropElements(PrintWriter pw);

    /** Generate Code for differences. */
    void generate(PrintWriter pw);

    /** Returns the elements that have changed. */
    List<String> getChanged();

    /** Return the list of elements that exist only in the original schema. */
    List<String> getFromOnly();

    /** Returns true if the difference is a minor one. */
    boolean isMinor();

    /** Returns the Renamed elements. */
    Map<String, String> getRenamed();

    /** Return the list of elements that exist only in the target schema. */
    List<String> getToOnly();

    /** Returns true if there are no deltas. */
    boolean isEmpty();
}  // end interface MdDelta
