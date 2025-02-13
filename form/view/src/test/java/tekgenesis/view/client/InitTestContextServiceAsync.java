
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async Service to initialize the context for the tests.
 */
public interface InitTestContextServiceAsync {

    //~ Methods ......................................................................................................................................

    /** Destroys initiated test context. */
    void destroyContext(final AsyncCallback<Void> async);

    /** Initialize the context for the specified project. */
    void initTestContext(final String[] projectPath, String id, final AsyncCallback<Void> async);
}
