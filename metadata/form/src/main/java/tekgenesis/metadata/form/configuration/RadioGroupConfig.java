
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.configuration;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.WidgetType;

import static java.util.Collections.emptyList;

/**
 * Config for Radio Groups.
 */
public class RadioGroupConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private List<String> cs;

    //~ Constructors .................................................................................................................................

    /** Creates a config for Radio Groups. */
    public RadioGroupConfig() {
        cs = emptyList();
    }

    //~ Methods ......................................................................................................................................

    /** Style classes list. */
    @NotNull public RadioGroupConfig styleClasses(List<String> classes) {
        cs = classes;
        return this;
    }

    /** Returns style classes list. */
    public List<String> getStyleClasses() {
        return cs;
    }

    @Override void deserializeFields(StreamReader r) {
        cs = r.readStrings();
    }

    @Override void serializeFields(StreamWriter w) {
        w.writeStrings(cs);
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.RADIO_GROUP;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 912487571009230484L;

    public static final RadioGroupConfig DEFAULT = new RadioGroupConfig();
}  // end class RadioGroupConfig
