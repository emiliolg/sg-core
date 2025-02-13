
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;

final class ApiLoader {

    //~ Constructors .................................................................................................................................

    private ApiLoader() {}

    //~ Methods ......................................................................................................................................

    /* Loads external script and calls onLoad method */
    public static void go(final String url, Runnable onLoad) {
        load(url, onLoad, () -> System.out.println("Cant load api = " + url));
    }

    /* Loads external script and calls onLoad method */
    public static void go(String url, Runnable onLoad, Runnable onLoadFail) {
        load(url, onLoad, onLoadFail);
    }

    private static void load(final String url, final Runnable onLoad, final Runnable onLoadFail) {
        final ExternalScript externalScript = externalScripts.get(url);
        if (externalScript == null) externalScripts.put(url, new ExternalScript(url, onLoad));
        else externalScript.callback(onLoad);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, ExternalScript> externalScripts = new HashMap<>();

    //~ Inner Classes ................................................................................................................................

    private static class ExternalScript implements Callback<Void, Exception> {
        private final List<Runnable> callbacks = new ArrayList<>();
        private boolean              loaded    = false;

        private ExternalScript(String url, Runnable onLoad) {
            callbacks.add(onLoad);
            ScriptInjector.fromUrl(url).setCallback(this).setWindow(ScriptInjector.TOP_WINDOW).inject();
        }

        public void callback(Runnable onLoad) {
            if (loaded) onLoad.run();
            else callbacks.add(onLoad);
        }

        public void onFailure(Exception reason) {
            // onLoadFail.run();
        }
        public void onSuccess(Void result) {
            loaded = true;
            for (final Runnable callback : callbacks)
                callback.run();
            callbacks.clear();
        }
    }
}  // end class ApiLoader
