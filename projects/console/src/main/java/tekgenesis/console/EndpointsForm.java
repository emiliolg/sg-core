
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.util.List;

import tekgenesis.common.core.Tuple;
import tekgenesis.form.FormTable;
import tekgenesis.persistence.IxProps;
import tekgenesis.persistence.IxService;

import static tekgenesis.common.env.context.Context.getEnvironment;

/**
 * User class for Form: EndpointsForm
 */
public class EndpointsForm extends EndpointsFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void init() {
        final List<Tuple<String, IxProps>> registeredEndpoints = IxService.getRegisteredEndpoints(getEnvironment());
        final FormTable<EndpointsRow>      endpoints           = getEndpoints();
        endpoints.clear();

        registeredEndpoints.forEach(f -> endpoints.add().set(f));
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * The Row.
     */
    public class EndpointsRow extends EndpointsRowBase {
        /**  */
        public void set(Tuple<String, IxProps> f) {
            setUrl(f.second().url);
            setMode(f.second().mode.name());
            IxService.setDomain(f.first());
            setAvailable(IxService.isAvailable());
        }
    }
}
