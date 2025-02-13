
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A Service to initialize the context for the tests.
 */
@RemoteServiceRelativePath("InitTestContextService")
public interface InitTestContextService extends RemoteService {

    //~ Methods ......................................................................................................................................

    void destroyContext();
    void initTestContext(final String[] projectPath, String id);

    //~ Inner Classes ................................................................................................................................

    class App {
        private App() {}

        /** Returns the instance. */
        public static synchronized InitTestContextServiceAsync getInstance() {
            return instance;
        }

        private static final InitTestContextServiceAsync instance = GWT.create(InitTestContextService.class);
    }
}
