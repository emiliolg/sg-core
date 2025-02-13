
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.service;

import tekgenesis.common.core.Option;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.persistence.etl.ImporterProps;
import tekgenesis.service.Service;
import tekgenesis.service.ServiceManager;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.env.context.Context.getSingleton;

/**
 * ImporterService.
 */
public class ImporterService extends Service {

    //~ Instance Fields ..............................................................................................................................

    private Option<tekgenesis.persistence.etl.ImporterService> importerService = Option.empty();
    private boolean                                            started         = false;

    //~ Constructors .................................................................................................................................

    /** Create an Importer Service. */
    public ImporterService(final ServiceManager sm) {
        super(sm, SERVICE_NAME, SERVICE_START_ORDER, ImporterProps.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isRunning() {
        return started;
    }

    @Override protected void doShutdown() {
        if (importerService.isPresent()) {
            importerService.get().stop();
            started = false;
        }
    }

    @Override protected void doStart() {
        final ResourceHandler                            rh      = getSingleton(ResourceHandler.class);
        final tekgenesis.persistence.etl.ImporterService service = new tekgenesis.persistence.etl.ImporterService(getEnv(), rh);
        importerService = some(service);

        new Thread(() -> {
            service.start();
            started = true;
        }).start();
    }

    //~ Static Fields ................................................................................................................................

    private static final String SERVICE_NAME        = ImporterService.class.getSimpleName();
    private static final int    SERVICE_START_ORDER = 1;
}  // end class ImporterService
