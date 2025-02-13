
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.HashMap;
import java.util.Map;

import tekgenesis.common.Predefined;
import tekgenesis.report.fo.Fo;

/**
 * Fo Builder.
 */
public abstract class FoBuilder<This, T extends Fo> implements FoB {

    //~ Instance Fields ..............................................................................................................................

    protected String content = "";

    private final Map<String, String> properties = new HashMap<>();

    //~ Methods ......................................................................................................................................

    /** Adds generic property. */
    public This addProperty(String key, String value) {
        properties.put(key, value);
        return Predefined.cast(this);
    }

    public abstract T build();

    /** Sets content. */
    public This content(String c) {
        content = c;
        return Predefined.cast(this);
    }

    /** Returns all properties. */
    public Map<String, String> getProperties() {
        return properties;
    }
}
